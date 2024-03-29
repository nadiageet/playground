import {useQuery} from "react-query";
import {useState} from "react";
import './quotedex.css'
import {fetchQuotedex, QUOTEDEX_QUERY_KEY, QuotedexRecord} from "../auth/queries/QuotedexQuery";
import {Spinners} from "../components/Spinners";


export function Quote(quote: QuotedexRecord) {
    const content = quote.numberOfQuotes === 0 ? "???" : quote.content;

    return <div className={quote.numberOfQuotes == 0 ? "quote not-possessed" : "quote"}>
        <p className={"content"}>"{content}"</p>
        <p className={"originator"}>- {quote.originator}</p>
        <div className={"id"}>{quote.quoteId}</div>
    </div>;
}

export function Quotedex() {

    const [showNotPossessedQuotes, setShowNotPossessedQuotes] = useState(false);

    const {data: quotedex, isLoading} = useQuery(QUOTEDEX_QUERY_KEY, fetchQuotedex,
        {
            staleTime: 6000,
        });

    if (isLoading) {
        return <div className={"loading"}>
            <Spinners/>
        </div>
    }

    if (!quotedex) {
        return <></>
    }

    function handleOnChange(e: any) {
        setShowNotPossessedQuotes(e.target.checked);
    }

    const totalQuotes = quotedex.length;

    function countPossessedQuotes(quotedex: QuotedexRecord[]) {
        return quotedex.reduce((n, val) => {
            if (val.numberOfQuotes === 0) {
                return n;
            } else {
                return n + 1;
            }
        }, 0);
    }

    const possessedCount = countPossessedQuotes(quotedex);

    const isFullQuotedex = totalQuotes === possessedCount;

    const quotes = quotedex.filter(e => showNotPossessedQuotes || e.numberOfQuotes > 0)
        .map(quote => {
            return <Quote quoteId={quote.quoteId}
                          key={quote.quoteId}
                          content={quote.content}
                          originator={quote.originator}
                          numberOfQuotes={quote.numberOfQuotes}
            />
        });
    return <>
        <div className="quotedex-header">
            <div className="form-check mb-4">
                <input className="form-check-input"
                       type="checkbox"
                       id="flexCheckDefault"
                       onChange={handleOnChange}/>
                <label className="form-check-label" htmlFor="flexCheckDefault">
                    Afficher les citations non possédées
                </label>
            </div>
            <p className={"alert " + (isFullQuotedex ? "alert-success" : "alert-secondary")}>
                <strong> {possessedCount} / {totalQuotes} </strong></p>
        </div>

        <hr/>
        <div className={"quotedex"}>
            {quotes}
        </div>
    </>

}