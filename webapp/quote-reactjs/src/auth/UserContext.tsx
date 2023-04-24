import {UserInfo} from "./dto/UserInfo";
import React, {createContext, useContext} from "react";


const UserContext = createContext<UserInfo | null>(null)

export default UserContext;


export function useAuthenticatedUser() {

    return useContext(UserContext);
}