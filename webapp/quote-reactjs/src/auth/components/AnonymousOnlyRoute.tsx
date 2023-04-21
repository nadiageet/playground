import {Navigate} from 'react-router-dom';
import {isJwtTokenPresent} from "../AuthUtils";

interface AnonymousOnlyRouteProps {
    children?: any
}

function AnonymousOnlyRoute({
                                children,
                            }: AnonymousOnlyRouteProps) {
    if (isJwtTokenPresent()) {
        console.log("User must be anonymous to access this route, redirecting to '/'");
        return <Navigate to={"/"}/>;
    } else {
        return children;
    }
}


export default AnonymousOnlyRoute;
