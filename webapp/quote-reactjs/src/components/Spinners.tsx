import Spinner from "react-bootstrap/esm/Spinner";

export function Spinners() {
    return (
        <div className={"loading-spinners"}>
            <Spinner role="status" animation="grow" variant={"warning"}>
                <span className="visually-hidden">Chargement de l'application en cours</span>
            </Spinner>
            <Spinner role="status" animation="grow" variant={"warning"}/>
            <Spinner role="status" animation="grow" variant={"warning"}/>
        </div>
    )
}