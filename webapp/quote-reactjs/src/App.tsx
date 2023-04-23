import React, {useState} from 'react';
import './App.css';
import {createBrowserRouter, RouterProvider} from 'react-router-dom';


import UserContext from './auth/UserContext';
import {UserInfo} from "./auth/dto/UserInfo";
import {QueryClient, useQuery, useQueryClient} from 'react-query';
import {isJwtTokenPresent, JWT_KEY} from "./auth/AuthUtils";
import {LoginJwtResponse} from "./auth/dto/LoginJwtResponse";
import {Toaster} from "react-hot-toast";
import {ACCOUNT_QUERY_KEY, fetchAccount} from "./auth/queries/AccountQuery";
import {collectorRoutes} from "./routes/collector.route";
import {adminRoutes} from "./routes/admin.route";
import {HeaderLayout} from "./components/HeaderLayout";
import useLocalStorage from "./hooks/UseLocalStorage";
import {JwtToken} from "./auth/dto/JwtToken";
import {AdminPage} from "./pages/AdminPage";
import {AuthenticatedPage} from "./pages/AuthenticatedPage";
import {LoginPage} from "./pages/LoginPage";


function App() {

    const queryClient = useQueryClient();
    console.group("APP");
    const [jwt, setJwt] = useLocalStorage<JwtToken>(JWT_KEY);
    const [user, setUser] = useState<UserInfo | null>(null);

    const {data} = useQuery([ACCOUNT_QUERY_KEY, jwt], fetchAccount, {
        onSuccess: (data) => setUser(data),
        onError: error => {
            console.log("error happened fetching user account")
            setJwt(null);
        },
    });

    function handleLogin(loginResponse: LoginJwtResponse) {
        console.log("User successfully logged in")
        console.log("set jwt " + loginResponse.jwt);
        setJwt({value: loginResponse.jwt});
    }

    function handleLogout(): void {
        console.log("User logged out")
        localStorage.removeItem(JWT_KEY);
        setJwt(null);
        setUser(null);
    }

    const accountLoader = (queryClient: QueryClient, jwt: JwtToken | null) =>
        async () => {
            console.group("account loader")
            if (!isJwtTokenPresent()) {
                console.log("no jwt token, not fetching account info");
                console.groupEnd();
                return null;
            }
            const cached = queryClient.getQueryData([ACCOUNT_QUERY_KEY, jwt]);
            if (cached) {
                console.log("get user info from query cache");
                console.groupEnd();
                return cached
            }
            const answer = await queryClient.fetchQuery([ACCOUNT_QUERY_KEY, jwt], fetchAccount);
            console.log("Fetching user info, no cache were available");
            console.groupEnd();
            return answer;
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
                    loader: accountLoader(queryClient, jwt)
                },
                {
                    path: "/*",
                    element: <AuthenticatedPage/>,
                    children: collectorRoutes,
                    loader: accountLoader(queryClient, jwt)
                },
            ]
        }
    ])


    console.groupEnd();
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
