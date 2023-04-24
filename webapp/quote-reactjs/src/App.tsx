import React, {useState} from 'react';
import './App.css';
import {createBrowserRouter, RouterProvider} from 'react-router-dom';


import UserContext from './auth/UserContext';
import {UserInfo} from "./auth/dto/UserInfo";
import {useQuery} from 'react-query';
import {JWT_KEY} from "./auth/AuthUtils";
import {LoginJwtResponse} from "./auth/dto/LoginJwtResponse";
import {Toaster} from "react-hot-toast";
import {ACCOUNT_QUERY_KEY, fetchAccount} from "./auth/queries/AccountQuery";
import {collectorRoutes} from "./routes/collector.route";
import {adminRoutes} from "./routes/admin.route";
import {HeaderLayout} from "./components/HeaderLayout";
import {AdminPage} from "./pages/AdminPage";
import {AuthenticatedPage} from "./pages/AuthenticatedPage";
import {LoginPage} from "./pages/LoginPage";
import useLocalStorage from 'react-use/lib/useLocalStorage';
import {Spinners} from "./components/Spinners";


function App() {
    const [jwt, setJwt, removeJwt] = useLocalStorage<string>(JWT_KEY, undefined, {raw: true})
    const [user, setUser] = useState<UserInfo | null>(null);

    const {data, isLoading: isLoadingUserAuthFromJwtToken} = useQuery([ACCOUNT_QUERY_KEY, jwt], fetchAccount, {
        onSuccess: (data) => setUser(data),
        onError: error => {
            console.log("error happened fetching user account")
            // setJwt(null);
            removeJwt();
        },
    });

    if (isLoadingUserAuthFromJwtToken) {
        return <div className={"loading-screen"}>
            <Spinners/>
        </div>
    }

    function handleLogin(loginResponse: LoginJwtResponse) {
        console.log("User successfully logged in")
        console.log("set jwt " + loginResponse.jwt);
        setJwt(loginResponse.jwt);
    }

    function handleLogout(): void {
        console.log("User logged out")
        removeJwt();
        setUser(null);
    }

    const router = createBrowserRouter([
        {
            element: <HeaderLayout onLogout={handleLogout}/>,
            children: [
                {
                    path: "/login",
                    element: <LoginPage onSuccessfullyLogin={handleLogin}/>
                },
                {
                    path: "/admin/*",
                    element: <AdminPage/>,
                    children: adminRoutes,
                },
                {
                    path: "/*",
                    element: <AuthenticatedPage/>,
                    children: collectorRoutes,
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
