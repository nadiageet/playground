import React, {useEffect, useState} from 'react';
import './App.css';
import {createBrowserRouter, Outlet, RouterProvider} from 'react-router-dom';
import AuthenticatedRoute from "./auth/components/AuthenticatedRoute";


import UserContext from './auth/UserContext';
import {UserInfo} from "./auth/dto/UserInfo";
import {useQuery} from 'react-query';
import {isJwtTokenPresent, JWT_KEY} from "./auth/AuthUtils";
import {LoginJwtResponse} from "./auth/dto/LoginJwtResponse";
import {Toaster} from "react-hot-toast";
import {queryClient} from "./client/QueryClientConfiguration";
import {ACCOUNT_QUERY, fetchAccount} from "./auth/queries/AccountQuery";
import ProtectedAdminRoute from "./auth/components/ProtectedAdminRoute";
import {collectorRoutes} from "./routes/collector.route";
import {adminRoutes} from "./routes/admin.route";
import {HeaderLayout} from "./components/HeaderLayout";
import useLocalStorage from "./hooks/UseLocalStorage";
import {JwtToken} from "./auth/dto/JwtToken";
import LoginForm from "./auth/LoginForm";
import AnonymousOnlyRoute from "./auth/components/AnonymousOnlyRoute";


function App() {
    const [jwt, setJwt] = useLocalStorage<JwtToken>(JWT_KEY);
    const [user, setUser] = useState<UserInfo | null>(null);

    const {data} = useQuery([ACCOUNT_QUERY], fetchAccount, {
        enabled: !!jwt,
        onError: error => {
            console.error(error)
            setJwt(null);
        },
    });

    useEffect(() => {
        setUser(data ?? null)
    }, [data])

    function handleLogin(loginResponse: LoginJwtResponse) {
        console.log("User successfully logged in")
        console.log("set jwt " + loginResponse.jwt);
        setJwt({value: loginResponse.jwt});
        queryClient.invalidateQueries([ACCOUNT_QUERY]).then();
    }

    function handleLogout(): void {
        console.log("User logged out")
        setUser(null);
        console.log("remove jwt token")
        setJwt(null);
        queryClient.invalidateQueries(ACCOUNT_QUERY).then();
    }

    const accountLoader =
        async () => {
            if (!isJwtTokenPresent()) {
                return null;
            }
            return (
                queryClient.getQueryData([ACCOUNT_QUERY]) ??
                await queryClient.fetchQuery([ACCOUNT_QUERY], fetchAccount)
            );
        }


    const router = createBrowserRouter([
        {
            element: <HeaderLayout onLogout={handleLogout}/>,
            children: [
                {
                    path: "/login",
                    element: <AnonymousOnlyRoute>
                        <LoginForm onSuccessfullyLogin={handleLogin}/>
                    </AnonymousOnlyRoute>
                },
                {
                    path: "/admin/*",
                    element: <ProtectedAdminRoute> <Outlet/></ProtectedAdminRoute>,
                    children: adminRoutes,
                    loader: accountLoader
                },
                {
                    path: "/*",
                    element: <AuthenticatedRoute> <Outlet/> </AuthenticatedRoute>,
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
