import {useQuery} from "react-query";
import fetchClient from "../client/FetchClient";
import {useState} from "react";

type QuotedexRecord = {
    id: number,
    content: string,
    originator: string,
    numberOfQuotes: number
}

export function Quote(quote: QuotedexRecord) {
    const content = quote.numberOfQuotes === 0 ? "???" : quote.content;

    return <div className={quote.numberOfQuotes == 0 ? "quote not-possessed" : "quote"}>
        <p className={"content"}>"{content}"</p>
        <p className={"originator"}>- {quote.originator}</p>
        <div className={"id"}>{quote.id}</div>
    </div>;
}

export function Quotedex() {

    const [onlyShowPossessed, setOnlyShowPossessed] = useState(false);

    const {data, isFetching} = useQuery("quotedex", () => {
        return fetchClient.get<QuotedexRecord[]>('/api/v2/quotedex')
            .then(axiosResponse => axiosResponse.data);
    })
    if (isFetching) {
        return <div>fetching...</div>
    }

    function handleOnChange(e: any) {
        setOnlyShowPossessed(e.target.checked);
    }

    const quotes = data?.filter(e => !onlyShowPossessed || e.numberOfQuotes > 0)
        .map(quote => {
            return <Quote id={quote.id}
                          key={quote.id}
                          content={quote.content}
                          originator={quote.originator}
                          numberOfQuotes={quote.numberOfQuotes}
            />
        });
    return <>
        <div className="form-check mb-4">
            <input className="form-check-input"
                   type="checkbox"
                   id="flexCheckDefault"
                   onChange={handleOnChange}/>
            <label className="form-check-label" htmlFor="flexCheckDefault">
                Masquer les citations non possédées
            </label>
        </div>
        <hr/>
        <div className={"quotedex"}>
            {quotes}
        </div>
    </>

}