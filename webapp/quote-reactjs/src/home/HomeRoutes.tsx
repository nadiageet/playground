import {Link} from "react-router-dom";
import React from "react";

export function Home() {
    return <div className={"home-btn-grp"}>
        {/*<Nav.Link href="/quotedex">Active</Nav.Link>*/}
        <Link to={"/quotedex"} className={"button-17"}>Quotedex</Link>
        <Link to={"/quotes/create"} className={"button-17"}>Inspirer les autres</Link>
        <Link to={"/gifts"} className={"button-17"}>Mes cadeaux</Link>
        <Link to={"/admin/fetch-rapid-api"} className={"button-17"}>Admin</Link>
        {/*<button className={"button-17"}>Quotedex</button>*/}
    </div>;
}
