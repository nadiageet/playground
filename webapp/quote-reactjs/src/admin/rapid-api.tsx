import {useMutation} from "react-query";
import fetchClient from "../client/FetchClient";
import toast from "react-hot-toast";
import Button from "react-bootstrap/Button";
import {queryClient} from "../client/QueryClientConfiguration";


export function RapidApi() {

    const {mutate, isLoading} = useMutation(async () => {
        return fetchClient.post("/api/v1/quote/random", {
            why: "by admin from website",
            generationNumber: 1
        });
    }, {
        onSuccess: () => {
            toast.success("Nouvelle citation enregistrée au jeu !");
            queryClient.invalidateQueries("quotedex").then()
        },
    });

    function handleOnClick() {
        mutate();
    }

    return (<>
            <Button disabled={isLoading} onClick={handleOnClick}>
                Ajouter une citation aléatoire au jeu
            </Button>
            {isLoading && (<div>Chargement...</div>)}
        </>
    )
}