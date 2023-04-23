import fetchClient from "../../client/FetchClient";
import {GiftRecord} from "../../gift/GiftRecord";

export const GIFT_QUERY_KEY = "gifts";


export function fetchGifts() {
    return fetchClient.get<GiftRecord[]>('/api/v1/gift')
        .then(axiosResponse => axiosResponse.data);
}