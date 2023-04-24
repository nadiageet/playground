import React from 'react';
import {Navigate} from 'react-router-dom';
import {useAuthenticatedUser} from "../UserContext";

interface PrivateRouteProps {
    children?: any
}

function ProtectedAdminRoute({
                                 children,
                             }: PrivateRouteProps) {
    const user = useAuthenticatedUser();
    if (user?.roles?.includes("ADMIN")) {
        return children;
    } else {
        console.log("User must be an admin to access this route, redirecting to '/'")
        return <Navigate to={"/"}/>
    }
}

export default ProtectedAdminRoute;
