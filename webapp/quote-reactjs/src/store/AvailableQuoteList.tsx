import {proposeQuoteQuery, QUOTE_REGISTRATION_QUERY, QuoteRegistration} from "./UserStoreQueries";
import {QuoteRegistrationComponent} from "./QuoteRegistrationComponent";
import {queryClient} from "../client/QueryClientConfiguration";
import {useMutation} from "react-query";
import toast from "react-hot-toast";

export type AvailableQuoteListPros = {
    quoteRegistrations: QuoteRegistration[],
}

export function AvailableQuoteList({quoteRegistrations}: AvailableQuoteListPros) {

    const availableQuoteList = quoteRegistrations.filter(qr => !qr.isProposed);

    const {mutate: proposeQuote} = useMutation(proposeQuoteQuery, {
        onSuccess: () => {
            toast.success("Citation ajoutée a votre magasin pour échange!");
            queryClient.invalidateQueries(QUOTE_REGISTRATION_QUERY).then()
        },
    });

    return <>
        <p>Voici vos citations en stock que vous n'avez pas partagé</p>
        <ul className={"quote-registration-list"}>
            {availableQuoteList.map(qr => <ol key={qr.quoteId}>
                <QuoteRegistrationComponent
                    onPropose={quoteRegistrationId => proposeQuote(quoteRegistrationId)}
                    quoteRegistration={qr}
                />
            </ol>)}
        </ul>
        <pre>
            {JSON.stringify(quoteRegistrations, null, 4)}
        </pre>
    </>;
}