import React from 'react';
import {Navigate, useLoaderData} from 'react-router-dom';

interface PrivateRouteProps {
    children: any
}

function ProtectedRoute({
                            children,
                        }: PrivateRouteProps) {

    const user = useLoaderData();
    console.log("user from loader data : ", user);

    if (!user) {
        console.log("User must be authenticated to access this route, redirecting to '/login'");
        return <Navigate to={"/login"}/>;
    } else {
        return children;
    }
}

export default ProtectedRoute;
