import {GiftRecord} from "./GiftRecord";
import {useMutation, useQuery, useQueryClient} from "react-query";
import fetchClient from "../client/FetchClient";
import './gift.css'
import React, {useEffect} from "react";
import toast from 'react-hot-toast';
import axios from "axios";
import {Gift} from "./Gift";

toast.error('Something went wrong')

export function GiftList() {

    const queryClient = useQueryClient();

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

    return <>
        <h3 className={"text-center mb-5"}>Mes récompenses à débloquer</h3>
        {!unopenedGift &&
            <div className={"mb-4 alert-warning alert"}>Vous n'avez pas de nouveaux cadeaux, revenez plus tard !</div>}
        <div className={"gifts"}>
            {data?.map(g => <Gift gift={g} key={g.id} onClickHandler={handleGiftClick}/>)}
        </div>
    </>;
}

