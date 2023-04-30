import {proposeQuoteQuery, QUOTE_REGISTRATION_QUERY, QuoteRegistration} from "./UserStoreQueries";
import {Button} from "react-bootstrap";
import {queryClient} from "../client/QueryClientConfiguration";
import toast from "react-hot-toast";
import {useMutation} from "react-query";

export type ProposedQuotesListProps = {
    quoteRegistrations: QuoteRegistration[]
}

type ProposedQuoteRegistrationComponentProps = {
    quoteContent: string,
    quoteRegistrationId: number,
    unProposeQuote: (quoteRegistrationId: number) => void
}

function ProposedQuoteRegistrationComponent({
                                                quoteContent,
                                                unProposeQuote,
                                                quoteRegistrationId
                                            }: ProposedQuoteRegistrationComponentProps) {

    return <div className={"quote-registration-container"}>
        <p>{quoteContent}</p>
        <Button variant={"danger"} onClick={() => unProposeQuote(quoteRegistrationId)}>
            Annuler
        </Button>
    </div>
}

export function ProposedQuotesList({quoteRegistrations}: ProposedQuotesListProps) {

    const proposedQuoteRegistrations = quoteRegistrations?.filter(d => d.isProposed);

    const {mutate: unProposeMutation} = useMutation(proposeQuoteQuery, {
        onSuccess: () => {
            toast.success("Citation ajoutée a votre magasin pour échange!");
            queryClient.invalidateQueries(QUOTE_REGISTRATION_QUERY).then()
        },
    });

    function handleUnProposeQuote(quoteRegistrationId: number) {
        console.log(`unproposing quote registration ${quoteRegistrationId}`);
        unProposeMutation({
            quoteRegistrationId,
            isProposed: false
        });
    }

    return <>
        <p>Retrouvez ici toutes les citations que vous avez proposé à l'échange</p>
        <ul>
            {proposedQuoteRegistrations.map(qr => <ProposedQuoteRegistrationComponent quoteContent={qr.quoteContent}
                                                                                      quoteRegistrationId={qr.registrationId}
                                                                                      unProposeQuote={(quoteRegistrationId) => handleUnProposeQuote(quoteRegistrationId)}/>
            )}
        </ul>
    </>
}