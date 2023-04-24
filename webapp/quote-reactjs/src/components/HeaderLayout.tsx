import AppNavbar from "./navbar";
import Container from "react-bootstrap/Container";
import {Outlet} from "react-router-dom";
import React from "react";


export type HeaderLayoutProps = {
    onLogout: () => void
}
export const HeaderLayout = ({onLogout}: HeaderLayoutProps) => {
    return (<>
        <header>
            <AppNavbar onLogout={onLogout}/>
        </header>
        <Container className={"app-container-layout"}>
            <Outlet/>
        </Container>
    </>);
}