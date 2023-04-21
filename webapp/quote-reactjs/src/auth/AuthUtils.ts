import {getValueFromStorage, LocalStorageContainer} from "../hooks/UseLocalStorage";

export const JWT_KEY = "react-jwt";

export function getLocalJwtToken(): string | null {
    return getValueFromStorage<LocalStorageContainer<string>>(JWT_KEY)?.value ?? null
};


export function isJwtTokenPresent(): boolean {
    return getLocalJwtToken() !== null;
}

export function deleteLocalJwtToken() {
    localStorage.removeItem(JWT_KEY);
    console.log("Authentication cleared : JWT token was deleted")
}

export function saveLocalJwtToken(jwt: string) {
    localStorage.setItem(JWT_KEY, jwt);
    console.log("Authentication saved as JWT token in local storage")
}