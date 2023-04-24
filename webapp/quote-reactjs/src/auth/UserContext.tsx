import {UserInfo} from "./dto/UserInfo";
import React, {createContext} from "react";


const UserContext = createContext<UserInfo | null>(null)

export default UserContext;
