import React, {useEffect, useState} from 'react';
import './App.css';
import AppNavbar from "./components/navbar";
import LoginForm from "./auth/LoginForm";
import Container from "react-bootstrap/Container";
import {createBrowserRouter, Outlet, RouterProvider} from 'react-router-dom';
import ProtectedRoute from "./auth/ProtectedRoute";


import UserContext from './auth/UserContext';
import {UserInfo} from "./auth/UserInfo";
import {useQuery} from 'react-query';
import {deleteLocalJwtToken, isJwtTokenPresent, saveLocalJwtToken} from "./auth/AuthUtils";
import {LoginJwtResponse} from "./auth/LoginJwtResponse";
import {HomeRoutes} from "./home/HomeRoutes";
import {Toaster} from "react-hot-toast";
import {queryClient} from "./client/QueryClientConfiguration";
import {accountQuery} from "./auth/AccountQuery";
import ProtectedAdminRoute from "./auth/ProtectedAdminRoute";


function App() {
    const [user, setUser] = useState<UserInfo | null>(null);
    const [jwt, setJwt] = useState<boolean>(isJwtTokenPresent());

    console.log({jwt, user});
    const query = accountQuery;
    const {data} = useQuery([query.queryKey], query.queryFn, {
        enabled: jwt,
        onError: error => {
            console.error(error)
            deleteLocalJwtToken();
            setJwt(false);
        },
    });

    useEffect(() => {
        setUser(data ?? null)
    }, [data])

    console.count('APP');

    function handleLogin(loginResponse: LoginJwtResponse) {
        console.log("User successfully logged in")
        saveLocalJwtToken(loginResponse.jwt);
        setJwt(true);
        // queryClient.invalidateQueries("account").then(() => console.log("query invalidated after login"));
    }

    function handleLogout(): void {
        console.log("User logged out")
        deleteLocalJwtToken();
        setUser(null);
        setJwt(false);
        queryClient.invalidateQueries("account").then();
    }

    const accountLoader = async () => {
        const jwt = isJwtTokenPresent();
        console.count("account loader");
        const query = accountQuery;
        const cached = queryClient.getQueryData(query.queryKey);
        console.log("cached", cached);
        console.log(jwt);
        return (
            cached ??
            await queryClient.fetchQuery(query.queryKey, query.queryFn, {staleTime: 1000})
        );
    }


    const HeaderLayout = () => {
        return (<>
            <header>
                <AppNavbar onLogout={handleLogout}/>
            </header>
            <Container>
                <Outlet/>
            </Container>
        </>);
    }

    const router = createBrowserRouter([
        {
            element: <HeaderLayout/>,
            // loader: rootLoader,
            children: [
                {
                    path: "/login",
                    element: <LoginForm hasLocalJwt={jwt}
                                        onSuccessfullyLogin={handleLogin}/>
                },
                {
                    path: "/*",
                    element: <ProtectedRoute children={<HomeRoutes/>}/>,
                    loader: accountLoader
                },
                {
                    path: "/admin/*",
                    element: <ProtectedAdminRoute> <Outlet/></ProtectedAdminRoute>,
                    children: [
                        {
                            path: "fetch-rapid-api",
                            element: <p>FETCH RAPID API</p>
                        }
                    ],
                    loader: accountLoader
                }
            ]
        }


    ])

    return (
        <div className="App">
            <Toaster position={"top-center"}/>
            <UserContext.Provider value={user}>
                <RouterProvider router={router}/>
            </UserContext.Provider>
        </div>
    );
}

export default App;
