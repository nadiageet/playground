import Container from 'react-bootstrap/Container';
import Navbar from 'react-bootstrap/Navbar';
import React, {useContext} from "react";
import UserContext from "../auth/UserContext";
import {NavDropdown} from "react-bootstrap";
import {useNavigate} from "react-router-dom";


export type AppNavbarProps = {

    onLogout: () => void,

};

function AppNavbar({onLogout}: AppNavbarProps) {
    const userContext = useContext(UserContext);

    const navigate = useNavigate();

    function navigateToHome() {
        // do not navigate if the user is not connected
        if (userContext) {
            navigate("/");
        }
    }

    return (
        <>
            <Navbar bg="light" variant="light" className={"mb-5"}>
                <Container>
                    <Navbar.Brand className={"navbar-brand"} onClick={navigateToHome}>
                        <img src={"quote.png"} alt="logo"/> Collector
                    </Navbar.Brand>

                    {userContext && (

                        <NavDropdown style={{fontFamily: "cursive", fontSize: "1.5rem"}} title={userContext.userName}>
                            <NavDropdown.Item style={{fontFamily: "sans-serif"}} onClick={onLogout}>Se
                                déconnecter</NavDropdown.Item>
                        </NavDropdown>

                    )}

                </Container>
            </Navbar>
        </>
    );
}

export default AppNavbar;