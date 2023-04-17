import {Link, Route, Routes} from "react-router-dom";
import {Quotedex} from "./Quotedex";
import {CreateQuoteForm} from "../quotes/CreateQuoteForm";

function Home() {
    return <div className={"home-btn-grp"}>
        {/*<Nav.Link href="/quotedex">Active</Nav.Link>*/}
        <Link to={"/quotedex"} className={"button-17"}>Quotedex</Link>
        <Link to={"/quotes/create"} className={"button-17"}>Inspirer les autres</Link>
        {/*<button className={"button-17"}>Quotedex</button>*/}
    </div>;
}

export function HomeRoutes() {

    return <Routes>
        <Route path={"/"} element={<Home/>}/>
        <Route path={"/quotes/create"} element={<CreateQuoteForm/>}/>
        <Route path={"/quotedex"} element={<Quotedex/>}>
        </Route>
    </Routes>
}