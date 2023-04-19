import {Link, Route, Routes} from "react-router-dom";
import {Quotedex} from "./Quotedex";
import {CreateQuoteForm} from "../quotes/CreateQuoteForm";
import {GiftList} from "../gift/GiftList";
import React from "react";

function Home() {
    return <div className={"home-btn-grp"}>
        {/*<Nav.Link href="/quotedex">Active</Nav.Link>*/}
        <Link to={"/quotedex"} className={"button-17"}>Quotedex</Link>
        <Link to={"/quotes/create"} className={"button-17"}>Inspirer les autres</Link>
        <Link to={"/gifts"} className={"button-17"}>Mes cadeaux</Link>
        <Link to={"/admin/fetch-rapid-api"} className={"button-17"}>Admin</Link>
        {/*<button className={"button-17"}>Quotedex</button>*/}
    </div>;
}

export function HomeRoutes() {

    return <Routes>
        <Route path={"/"} element={<Home/>}/>
        <Route path={"/quotes/create"} element={<CreateQuoteForm/>}/>
        <Route path={"/quotedex"} element={<Quotedex/>}/>
        <Route path={"/gifts"} element={<GiftList/>}/>
    </Routes>
}