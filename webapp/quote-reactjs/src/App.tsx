import React, {useState} from 'react';
import './App.css';
import AppNavbar from "./components/navbar";
import LoginForm from "./auth/LoginForm";
import Container from "react-bootstrap/Container";
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Secured from "./components/Secured";
import ProtectedRoute from "./auth/ProtectedRoute";

function App() {

    const [isAuthenticated, setAuthenticated] = useState(!!localStorage.getItem("react-jwt"));


    function handleLogin() {
        console.log("user login !")
        setAuthenticated(true);
    }

    return (
        <div className="App">
            <AppNavbar></AppNavbar>
            <Container>
                <Router>
                    <Routes>
                        <Route path={"/login"}
                               element={
                                   <LoginForm onSuccessfullyLogin={handleLogin}/>
                               }>
                        </Route>
                        <Route path={"/"} element={
                            <ProtectedRoute isAuthenticated={isAuthenticated}>
                                <Secured></Secured>
                            </ProtectedRoute>
                        }>
                        </Route>
                        <Route path="*" element={<p>There's nothing here: 404!</p>}/>
                    </Routes>
                </Router>
            </Container>
        </div>
    );
}

export default App;
