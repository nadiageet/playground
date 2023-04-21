import {createContext} from "react";
import {UserInfo} from "./dto/UserInfo";


const UserContext = createContext<UserInfo | null>(null)

export default UserContext;