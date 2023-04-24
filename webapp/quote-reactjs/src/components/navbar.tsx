import Container from 'react-bootstrap/Container';
import Navbar from 'react-bootstrap/Navbar';
import React from "react";
import {Nav, NavDropdown} from "react-bootstrap";
import {NavLink, useNavigate} from "react-router-dom";
import {useAuthentication} from "../auth/hooks/useAuthentication";
import {fetchGifts, GIFT_QUERY_KEY} from "../auth/queries/GiftQuery";
import {useQuery} from 'react-query';


export type AppNavbarProps = {

    onLogout: () => void,

};

function AppNavbar({onLogout}: AppNavbarProps) {
    const {isAuthenticated, isAdmin, userName} = useAuthentication()
    const navigate = useNavigate();


    const {data, isLoading} = useQuery(GIFT_QUERY_KEY, fetchGifts,
        {
            enabled: isAuthenticated,
            staleTime: 6000,
        });

    function getGiftLogo() {
        if (data?.some(gift => !gift.isUsed)) {
            return "gift-full-transparent.gif";
        } else {
            return "gift-navbar.png";
        }
    }

    function navigateToHome() {
        // do not navigate if the user is not connected
        if (isAuthenticated) {
            navigate("/");
        }
    }

    return (
        <>
            <Navbar bg="light" variant="light" className={"mb-5"} fixed={"top"}>
                <Container>
                    <Navbar.Brand className={"navbar-brand"} onClick={navigateToHome}>
                        <img src={"quote.png"} alt="logo"/> Collector
                    </Navbar.Brand>
                    {isAuthenticated && (<>
                        <Nav className="navbar-menu">
                            <NavLink to={'/store'}
                                     className={"navbar-activable"}>
                                Mon magasin
                            </NavLink>
                            <NavLink to={'/quotes/create'}
                                     className={"navbar-activable"}>
                                S'exprimer
                            </NavLink>

                            {isAdmin && (
                                <NavLink to={"/admin/fetch-rapid-api"} className={"navbar-activable"}>
                                    Administration
                                </NavLink>)}
                        </Nav>

                        <div className={"navbar-icon-menu"}>
                            <NavLink to={'/gifts'} className={"navbar-activable"}><img height={32} src={getGiftLogo()}/>
                            </NavLink>
                        </div>

                        <NavDropdown style={{fontFamily: "cursive", fontSize: "1.5rem"}} title={userName}>
                            <NavDropdown.Item style={{fontFamily: "sans-serif"}} onClick={onLogout}>
                                Se déconnecter
                            </NavDropdown.Item>
                        </NavDropdown>
                    </>)}
                </Container>
            </Navbar>
        </>
    );
}

export default AppNavbar;