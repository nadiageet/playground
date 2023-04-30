import {QuoteRegistration} from "./UserStoreQueries";

import "./QuoteRegistration.css"
import {Button} from "react-bootstrap";


export type QuoteRegistrationComponentProps = {
    quoteRegistration: QuoteRegistration,
    onPropose: (quoteRegistrationId: number) => void

}

export function QuoteRegistrationComponent({quoteRegistration, onPropose}: QuoteRegistrationComponentProps) {
    return <>
        <div className={"quote-registration-container"}>
            <div>
                {quoteRegistration.quoteContent}
            </div>
            <Button onClick={() => onPropose(quoteRegistration.registrationId)} variant={"primary"}>Proposer a
                l'échange</Button>
        </div>
    </>
}