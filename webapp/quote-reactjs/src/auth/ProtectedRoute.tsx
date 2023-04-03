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
        return <Navigate to={"/login"}/>
    } else {
        return children;
    }
}

export default ProtectedRoute;
