import {Outlet} from "react-router-dom";
import AuthenticatedRoute from "../auth/components/AuthenticatedRoute";

export function AuthenticatedPage() {

    console.log("authenticated page");

    return <AuthenticatedRoute>
        <Outlet/>
    </AuthenticatedRoute>
}