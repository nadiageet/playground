import React from 'react';
import {Navigate} from 'react-router-dom';
import {useAuthenticatedUser} from "../UserContext";

interface PrivateRouteProps {
    children?: any
}

/**
 * Needs a loader providing the UserInfo
 *
 * redirect to /login if no authentication
 */
function AuthenticatedRoute({
                                children,
                            }: PrivateRouteProps) {

    const user = useAuthenticatedUser();

    if (!user) {
        console.log("User must be authenticated to access this route, redirecting to '/login'");
        return <Navigate to={"/login"}/>;
    } else {
        return children;
    }
}

export default AuthenticatedRoute;
