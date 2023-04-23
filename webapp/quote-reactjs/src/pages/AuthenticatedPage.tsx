import {Outlet} from "react-router-dom";
import AuthenticatedRoute from "../auth/components/AuthenticatedRoute";
import {Home} from "../home/HomeRoutes";

export function AuthenticatedPage() {

    console.log("authenticated page");

    return <AuthenticatedRoute>
        <div className={"mb-4"}>
            <Home/>
        </div>
        <Outlet/>
    </AuthenticatedRoute>
}