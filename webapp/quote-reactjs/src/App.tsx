import React, {useState} from 'react';
import './App.css';
import AppNavbar from "./components/navbar";
import LoginForm from "./auth/LoginForm";
import Container from "react-bootstrap/Container";
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import ProtectedRoute from "./auth/ProtectedRoute";


import UserContext from './auth/UserContext';
import {UserInfo} from "./auth/UserInfo";
import {useQuery} from 'react-query';
import fetchClient from "./client/FetchClient";
import {deleteLocalJwtToken, isJwtTokenPresent, saveLocalJwtToken} from "./auth/AuthUtils";
import {LoginJwtResponse} from "./auth/LoginJwtResponse";
import {HomeRoutes} from "./home/HomeRoutes";
import {Toaster} from "react-hot-toast";

function useAuthentication() {

    const [user, setUser] = useState<UserInfo | null>(null);
    const [jwt, setJwt] = useState<boolean>(isJwtTokenPresent());

    useQuery(["account", jwt], () => {
        if (jwt) {
            console.log("User is authenticated thanks to the JWT Token in Local Storage. Proceeding to fetch its account information from server...");
            return fetchClient.get<UserInfo>('/api/v1/account')
                .then(axiosResponse => axiosResponse.data);
        } else {
            console.log("NO JWT found => not fetching user account");
            return Promise.resolve(null);
        }
    }, {
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
    }

    return (
        <div className="App">
            <Toaster position={"top-center"}/>
            <UserContext.Provider value={user}>
                <Router>
                    <AppNavbar onLogout={handleLogout}></AppNavbar>
                    <Container>
                        <Routes>
                            <Route path={"/login"}
                                   element={
                                       <LoginForm
                                           hasLocalJwt={jwt}
                                           onSuccessfullyLogin={handleLogin}/>
                                   }>
                            </Route>
                            <Route path={"/*"} element={
                                <ProtectedRoute hasLocalJwt={jwt}>
                                    <HomeRoutes></HomeRoutes>
                                </ProtectedRoute>
                            }>
                            </Route>
                        </Routes>
                    </Container>
                </Router>
            </UserContext.Provider>
        </div>
    );
}

export default App;
