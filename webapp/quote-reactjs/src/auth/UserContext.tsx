import {createContext} from "react";
import {UserInfo} from "./UserInfo";


const UserContext = createContext<UserInfo | null>(null)

export default UserContext;