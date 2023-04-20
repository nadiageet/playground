import {RouteObject} from "react-router-dom";
import React from "react";
import {RapidApi} from "../admin/rapid-api";

export const adminRoutes: RouteObject[] = [
    {
        path: "fetch-rapid-api",
        element: <RapidApi/>
    }
]