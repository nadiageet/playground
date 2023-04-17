import React, {useState} from 'react';
import './App.css';
import AppNavbar from "./components/navbar";
import LoginForm from "./auth/LoginForm";
import Container from "react-bootstrap/Container";
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Secured from "./components/Secured";
import ProtectedRoute from "./auth/ProtectedRoute";


import UserContext from './auth/UserContext';
import {UserInfo} from "./auth/UserInfo";
import {useQuery} from 'react-query';
import fetchClient from "./client/FetchClient";
import {deleteLocalJwtToken, isJwtTokenPresent, saveLocalJwtToken} from "./auth/AuthUtils";
import {LoginJwtResponse} from "./auth/LoginJwtResponse";

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
            <UserContext.Provider value={user}>
                <AppNavbar onLogout={handleLogout}></AppNavbar>
                <Container>
                    <Router>
                        <Routes>
                            <Route path={"/login"}
                                   element={
                                       <LoginForm
                                           hasLocalJwt={jwt}
                                           onSuccessfullyLogin={handleLogin}/>
                                   }>
                            </Route>
                            <Route path={"/"} element={
                                <ProtectedRoute hasLocalJwt={jwt}>
                                    <Secured></Secured>
                                </ProtectedRoute>
                            }>
                            </Route>
                            <Route path="*" element={<p>There's nothing here: 404!</p>}/>
                        </Routes>
                    </Router>
                </Container>
            </UserContext.Provider>
        </div>
    );
}

export default App;
