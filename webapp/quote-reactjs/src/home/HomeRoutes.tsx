import {Link} from "react-router-dom";
import React from "react";
import {useAuthentication} from "../auth/useAuthentication";

export function Home() {
    const {isAdmin} = useAuthentication();

    return <div className={"home-btn-grp"}>
        <Link to={"/quotedex"} className={"button-17"}>Quotedex</Link>
        <Link to={"/quotes/create"} className={"button-17"}>Inspirer les autres</Link>
        <Link to={"/gifts"} className={"button-17"}>Mes cadeaux</Link>
        {isAdmin && <Link to={"/admin/fetch-rapid-api"} className={"button-17"}>Admin</Link>}
    </div>;
}
