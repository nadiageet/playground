import fetchClient from "../../client/FetchClient";

export const QUOTEDEX_QUERY_KEY = "quotedex";

export function fetchQuotedex() {
    return fetchClient.get<QuotedexRecord[]>('/api/v2/quotedex')
        .then(axiosResponse => axiosResponse.data);
}


export type QuotedexRecord = {
    quoteId: number,
    content: string,
    originator: string,
    numberOfQuotes: number
}
