import {useQuery} from "react-query";
import {Spinners} from "../components/Spinners";
import {fetchQuoteRegistrations, QUOTE_REGISTRATION_QUERY} from "./UserStoreQueries";

export function ProposedQuotesList() {
    const {data, isLoading} = useQuery(QUOTE_REGISTRATION_QUERY, fetchQuoteRegistrations,
        {
            staleTime: 6000,
        });

    if (isLoading) {
        return <div className={"loading"}>
            <Spinners/>
        </div>
    }

    const proposedQuoteRegistrations = data?.filter(d => d.isProposed);

    return <>
        <p>Retrouvez ici toutes les citations que vous avez proposé à l'échange</p>
        <pre>
            {JSON.stringify(proposedQuoteRegistrations, null, 4)}
        </pre>
    </>
}