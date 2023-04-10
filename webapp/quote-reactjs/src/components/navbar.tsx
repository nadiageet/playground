import Container from 'react-bootstrap/Container';
import Navbar from 'react-bootstrap/Navbar';
import React, {useContext} from "react";
import UserContext from "../auth/UserContext";
import {NavDropdown} from "react-bootstrap";


export type AppNavbarProps = {

    onLogout: () => void

};

function AppNavbar({onLogout}: AppNavbarProps) {
    const userContext = useContext(UserContext);

    return (
        <>
            <Navbar bg="light" variant="light" className={"mb-5"}>
                <Container>
                    <Navbar.Brand>Quote collector</Navbar.Brand>

                    {userContext && (

                        <NavDropdown title={userContext.userName}>
                            <NavDropdown.Item onClick={onLogout}>Se déconnecter</NavDropdown.Item>
                        </NavDropdown>

                    )}

                </Container>
            </Navbar>
        </>
    );
}

export default AppNavbar;