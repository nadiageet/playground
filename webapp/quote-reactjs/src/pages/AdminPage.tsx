import {Outlet} from "react-router-dom";
import ProtectedAdminRoute from "../auth/components/ProtectedAdminRoute";

export function AdminPage() {

    console.log("Admin Page");

    return <ProtectedAdminRoute>
        <Outlet/>
    </ProtectedAdminRoute>
}