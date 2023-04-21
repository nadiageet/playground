import {useContext} from "react";
import UserContext from "../UserContext";


export type UseAuthenticationRecord = {
    isAuthenticated: boolean,
    isAdmin?: boolean,
    userName?: string
}

export function useAuthentication(): UseAuthenticationRecord {
    const user = useContext(UserContext);

    return {
        isAuthenticated: !!user,
        isAdmin: user?.roles?.includes("ADMIN"),
        userName: user?.userName
    }
}