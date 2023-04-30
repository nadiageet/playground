import fetchClient from "../client/FetchClient";

export const QUOTE_REGISTRATION_QUERY = "quote_registrations";
export function fetchQuoteRegistrations() {
    return fetchClient.get<QuoteRegistration[]>("/api/v1/quotesRegistrations")
        .then(axiosResponse => axiosResponse.data);
}

export type QuoteRegistration = {
    registrationId: number,
    quoteId: number,
    quoteContent: string,
    isProposed: boolean
}


export function proposeQuoteQuery(quoteRegistrationId: number) {
    return fetchClient.post(`/api/v1/trade/propose/${quoteRegistrationId}`);
}