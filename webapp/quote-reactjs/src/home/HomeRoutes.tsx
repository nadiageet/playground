import {Link} from "react-router-dom";
import React from "react";
import {useAuthentication} from "../auth/hooks/useAuthentication";

export function Home() {
    const {isAdmin} = useAuthentication();

    return <div className={"home-btn-grp"}>
        <Link to={"/quotes/create"} className={"button-17"}>Inspirer les autres</Link>
        <Link to={"/gifts"} className={"button-17"}>Mes cadeaux</Link>
        {isAdmin && <Link to={"/admin/fetch-rapid-api"} className={"button-17"}>Admin</Link>}
    </div>;
}
