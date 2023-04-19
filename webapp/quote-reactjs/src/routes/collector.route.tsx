import {RouteObject} from "react-router-dom";
import {CreateQuoteForm} from "../quotes/CreateQuoteForm";
import {Quotedex} from "../home/Quotedex";
import {GiftList} from "../gift/GiftList";
import React from "react";
import {Home} from "../home/HomeRoutes";

export const collectorRoutes: RouteObject[] = [
    {
        path: "",
        element: <Home/>
    },
    {
        path: "quotes/create",
        element: <CreateQuoteForm/>
    },
    {
        path: "quotedex",
        element: <Quotedex/>
    },
    {
        path: "gifts",
        element: <GiftList/>
    }
]

