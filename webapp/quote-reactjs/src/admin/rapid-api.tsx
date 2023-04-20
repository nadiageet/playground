import {useMutation} from "react-query";
import fetchClient from "../client/FetchClient";
import toast from "react-hot-toast";
import Button from "react-bootstrap/Button";
import {queryClient} from "../client/QueryClientConfiguration";
import {useState} from "react";


export function RapidApi() {

    const [disabled, setDisabled] = useState(false);
    const {mutate, isLoading} = useMutation(async () => {
        return fetchClient.post("/api/v1/quote/random", {
            why: "by admin from website",
            generationNumber: 1
        });
    }, {
        onSuccess: () => {
            toast.success("Nouvelle citation enregistrée au jeu !");
            setDisabled(false);
            queryClient.invalidateQueries("quotedex").then()
        },
    });

    function handleOnClick() {
        mutate();
        setDisabled(true);
    }

    return <Button disabled={disabled} onClick={handleOnClick}>
        Ajouter une citation aléatoire au jeu
    </Button>
}