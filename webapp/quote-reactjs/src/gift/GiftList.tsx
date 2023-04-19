import {GiftRecord} from "./GiftRecord";
import {useMutation, useQuery, useQueryClient} from "react-query";
import fetchClient from "../client/FetchClient";
import './gift.css'
import React, {useEffect, useState} from "react";
import toast from 'react-hot-toast';
import axios from "axios";
import {Gift} from "./Gift";

toast.error('Something went wrong')

export function GiftList() {

    const queryClient = useQueryClient();
    const [showOpenedGifts, setShowOpenedGifts] = useState(true);

    const {mutate: openGiftMutate, error} = useMutation(async (giftId: string) => {
        return fetchClient.post("/api/v1/gift/use", {
            giftId
        });
    }, {
        onSuccess: () => {
            toast.success("Le cadeau a bien été utilisé");
            queryClient.invalidateQueries("gifts");
        },
    });

    useEffect(() => {
        if (axios.isAxiosError(error)) {
            toast.error(error?.response?.data.message || '', {
                position: 'bottom-right'
            })
        }
    }, [error])


    const {data, isLoading} = useQuery("gifts", () => {
        return fetchClient.get<GiftRecord[]>('/api/v1/gift')
            .then(axiosResponse => axiosResponse.data);
    });

    if (isLoading) {
        return <p>Loading...</p>
    }


    function handleGiftClick(gift: GiftRecord) {
        if (!gift.isUsed) {
            openGiftMutate(gift.id);
        }
    }

    const unopenedGift = data?.some(g => !g.isUsed) || false

    function handleOnChange() {
        setShowOpenedGifts(!showOpenedGifts);
    }

    return <>
        <div className="form-check mb-4">
            <input className="form-check-input"
                   type="checkbox"
                   id="flexCheckDefault"
                   value={showOpenedGifts ? "checked" : ""}
                   onChange={handleOnChange}/>
            <label className="form-check-label" htmlFor="flexCheckDefault">
                Masquer les cadeaux ouverts
            </label>
        </div>
        <h3 className={"text-center mb-5"}>Mes récompenses à débloquer</h3>
        {!unopenedGift &&
            <div className={"mb-4 alert-warning alert"}>Vous n'avez pas de nouveaux cadeaux, revenez plus tard !</div>}
        <div className={"gifts"}>
            {data?.filter(g => showOpenedGifts || !g.isUsed)
                ?.map(g => <Gift gift={g} key={g.id} onClickHandler={handleGiftClick}/>)}
        </div>
    </>;
}

