import React from 'react';
import {Navigate, useLoaderData} from 'react-router-dom';

interface PrivateRouteProps {
    hasLocalJwt: boolean,
    children: any
}

function ProtectedRoute({
                            hasLocalJwt,
                            children,
                        }: PrivateRouteProps) {

    const user = useLoaderData();
    if (!user && hasLocalJwt) {
        throw Error("User was null from loader data in Protected Route");
    }
    if (!user) {
        console.log("User must be authenticated to access this route, redirecting to '/login'");
        return <Navigate to={"/login"}/>;
    } else {
        return children;
    }
}

export default ProtectedRoute;
