import React, {useEffect, useState} from 'react';
import './App.css';
import LoginForm from "./auth/LoginForm";
import {createBrowserRouter, Outlet, RouterProvider} from 'react-router-dom';
import ProtectedRoute from "./auth/ProtectedRoute";


import UserContext from './auth/UserContext';
import {UserInfo} from "./auth/UserInfo";
import {useQuery} from 'react-query';
import {deleteLocalJwtToken, isJwtTokenPresent, saveLocalJwtToken} from "./auth/AuthUtils";
import {LoginJwtResponse} from "./auth/LoginJwtResponse";
import {Toaster} from "react-hot-toast";
import {queryClient} from "./client/QueryClientConfiguration";
import {accountQuery} from "./auth/AccountQuery";
import ProtectedAdminRoute from "./auth/ProtectedAdminRoute";
import {collectorRoutes} from "./routes/collector.route";
import {adminRoutes} from "./routes/admin.route";
import {HeaderLayout} from "./components/HeaderLayout";


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




    const router = createBrowserRouter([
        {
            element: <HeaderLayout onLogout={handleLogout}/>,
            children: [
                {
                    path: "/login",
                    element: <LoginForm hasLocalJwt={jwt}
                                        onSuccessfullyLogin={handleLogin}/>
                },
                {
                    path: "/admin/*",
                    element: <ProtectedAdminRoute> <Outlet/></ProtectedAdminRoute>,
                    children: adminRoutes,
                    loader: accountLoader
                },
                {
                    path: "/*",
                    element: <ProtectedRoute> <Outlet/> </ProtectedRoute>,
                    children: collectorRoutes,
                    loader: accountLoader
                },
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
