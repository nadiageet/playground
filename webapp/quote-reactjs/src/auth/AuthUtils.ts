export const JWT_KEY = "jwt";

export function getLocalJwtToken(): string | null {
    return localStorage.getItem(JWT_KEY);
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