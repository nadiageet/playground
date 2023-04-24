import {RouteObject} from "react-router-dom";
import {CreateQuoteForm} from "../quotes/CreateQuoteForm";
import {Quotedex} from "../quotedex/Quotedex";
import {GiftList} from "../gift/GiftList";
import React from "react";
import {UserStore} from "../store/UserStore";

export const collectorRoutes: RouteObject[] = [
    {
        path: "",
        element: <Quotedex/>
    },
    {
        path: "quotes/create",
        element: <CreateQuoteForm/>
    },
    {
        path: "gifts",
        element: <GiftList/>
    },
    {
        path: "store",
        element: <UserStore/>
    }
];

