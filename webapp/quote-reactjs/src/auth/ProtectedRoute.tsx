import React from 'react';
import {Navigate} from 'react-router-dom';

interface PrivateRouteProps {
    hasLocalJwt: boolean,
    children: any
}

function ProtectedRoute({
                            hasLocalJwt,
                            children,
                        }: PrivateRouteProps) {

    if (!hasLocalJwt) {
        console.log("User must be authenticated to access this route, redirecting to '/login'")
        return <Navigate to={"/login"}/>
    } else {
        return children;
    }
}

export default ProtectedRoute;
