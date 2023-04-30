import {QuoteRegistration} from "./UserStoreQueries";

export type ProposedQuotesListProps = {
    quoteRegistrations: QuoteRegistration[]
}

export function ProposedQuotesList({quoteRegistrations}: ProposedQuotesListProps) {

    const proposedQuoteRegistrations = quoteRegistrations?.filter(d => d.isProposed);

    return <>
        <p>Retrouvez ici toutes les citations que vous avez proposé à l'échange</p>
        <pre>
            {JSON.stringify(proposedQuoteRegistrations, null, 4)}
        </pre>
    </>
}