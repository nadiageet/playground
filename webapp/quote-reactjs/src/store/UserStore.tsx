import {ProposedQuotesList} from "./ProposedQuotesList";
import {AvailableQuoteList} from "./AvailableQuoteList";
import {fetchQuoteRegistrations, QUOTE_REGISTRATION_QUERY} from "./UserStoreQueries";
import {Spinners} from "../components/Spinners";
import {useQuery} from "react-query";

export function UserStore() {
    const {data, isLoading} = useQuery(QUOTE_REGISTRATION_QUERY, fetchQuoteRegistrations,
        {
            staleTime: 6000,
        });


    if (isLoading) {
        return <div className={"loading"}>
            <Spinners/>
        </div>
    }
    return <>
        <ProposedQuotesList quoteRegistrations={data ?? []}/>
        <AvailableQuoteList quoteRegistrations={data ?? []}/>
    </>
}