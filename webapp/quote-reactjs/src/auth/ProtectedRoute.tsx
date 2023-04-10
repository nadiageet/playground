import React from 'react';
import {Navigate} from 'react-router-dom';

interface PrivateRouteProps {
    isAuthenticated: boolean,
    children: any
}

function ProtectedRoute({
                            isAuthenticated,
                            children,
                        }: PrivateRouteProps) {

    if (!isAuthenticated) {
        console.log("User must be authenticated to access this route, redirecting to '/login'")
        return <Navigate to={"/login"}/>
    } else {
        return children;
    }
}

export default ProtectedRoute;
