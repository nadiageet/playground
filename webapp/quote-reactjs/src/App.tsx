import React, {useState} from 'react';
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


function useAuthentication() {

    const [user, setUser] = useState<UserInfo | null>(null);
    const [jwt, setJwt] = useState<boolean>(isJwtTokenPresent());

    console.log({jwt});
    const query = accountQuery;
    useQuery([query.queryKey, jwt], query.queryFn, {
        enabled: jwt,
        onSuccess: data => {
            console.log("User information retrieved from server successfully");
            setUser(data)
        },
        onError: error => {
            console.error(error)
            deleteLocalJwtToken();
            setJwt(false);
        },
        staleTime: 60_000,
        retry: false
    })

    return {
        user, setUser,
        jwt, setJwt,
    }
}


function App() {
    const {
        user, setUser,
        jwt, setJwt,
    } = useAuthentication();

    function handleLogin(loginResponse: LoginJwtResponse) {
        console.log("User successfully logged in")
        saveLocalJwtToken(loginResponse.jwt);
        setJwt(true);
    }

    function handleLogout(): void {
        console.log("User logged out")
        deleteLocalJwtToken();
        setUser(null);
        setJwt(false);
        // queryClient.invalidateQueries("account");
    }

    const accountLoader = (jwt: boolean) => async () => {
        console.count("account loader");
        const query = accountQuery;
        return (
            queryClient.getQueryData([query.queryKey, jwt]) ??
            await queryClient.fetchQuery([query.queryKey, jwt], query.queryFn, {staleTime: 60_000})
        )
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
                    element: <ProtectedRoute hasLocalJwt={jwt} children={<HomeRoutes/>}/>,
                    loader: accountLoader(jwt)
                }
            ]
        }


    ])

    return (
        <div className="App">
            <Toaster position={"top-center"}/>
            <UserContext.Provider value={user || null}>
                <RouterProvider router={router}/>
            </UserContext.Provider>
        </div>
    );
}

export default App;
