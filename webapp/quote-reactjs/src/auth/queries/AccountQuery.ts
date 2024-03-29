import {getLocalJwtToken, isJwtTokenPresent} from "../AuthUtils";
import fetchClient from "../../client/FetchClient";
import {UserInfo} from "../dto/UserInfo";


export const ACCOUNT_QUERY_KEY = "account";

export function fetchAccount() {
    console.group("fetchAccount");
    console.log(getLocalJwtToken());
    console.groupEnd();
    if (isJwtTokenPresent()) {
        console.log("User is authenticated thanks to the JWT Token in Local Storage. Proceeding to fetch its account information from server...");
        return fetchClient.get<UserInfo>('/api/v1/account')
            .then(axiosResponse => axiosResponse.data);
    } else {
        console.log("NO JWT found => not fetching user account");
        return Promise.resolve(null);
    }
}
