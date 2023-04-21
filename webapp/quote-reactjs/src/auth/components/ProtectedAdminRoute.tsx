import React from 'react';
import {Navigate, useLoaderData} from 'react-router-dom';
import {UserInfo} from "../dto/UserInfo";

interface PrivateRouteProps {
    children?: any
}

function ProtectedAdminRoute({
                                 children,
                             }: PrivateRouteProps) {


    const user = useLoaderData() as UserInfo | null;
    if (user?.roles?.includes("ADMIN")) {
        return children;
    } else {
        console.log("User must be an admin to access this route, redirecting to '/'")
        return <Navigate to={"/"}/>
    }
}

export default ProtectedAdminRoute;
