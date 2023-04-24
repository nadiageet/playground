import React from 'react';
import {Navigate} from 'react-router-dom';
import {useAuthentication} from "../hooks/useAuthentication";

interface PrivateRouteProps {
    children?: any
}

function ProtectedAdminRoute({
                                 children,
                             }: PrivateRouteProps) {
    const {isAdmin} = useAuthentication();
    if (isAdmin) {
        return children;
    } else {
        console.log("User must be an admin to access this route, redirecting to '/'")
        return <Navigate to={"/"}/>
    }
}

export default ProtectedAdminRoute;
