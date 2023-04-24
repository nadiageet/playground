import React, {useState} from "react";
import {FloatingLabel, Toast, ToastContainer} from "react-bootstrap";
import Form from 'react-bootstrap/Form';
import Button from "react-bootstrap/Button";
import {useMutation} from "react-query";
import fetchClient from "../client/FetchClient";

export function CreateQuoteForm() {

    const [content, setContent] = useState('');
    const [showToast, setShowToast] = useState(false);

    const {mutate, isLoading} = useMutation(async (content: string) => {
        return fetchClient.post("/api/v1/quote", {
            content
        });
    }, {
        onSuccess: () => {
            setShowToast(true)
            setContent('')
        }
    });
    const closeToast = () => setShowToast(false);


    function handleOnChange(e: any) {
        setContent(e.target.value);
    }

    function handleOnSubmit(e: any) {
        e.preventDefault();
        mutate(content);
    }

    return <Form onSubmit={handleOnSubmit}>
        <FloatingLabel className={"mb-4"} controlId="floatingTextarea2" label="Inspirer les autres">
            <Form.Control
                as="textarea"
                placeholder="Leave a comment here"
                style={{height: '100px'}}
                value={content}
                onChange={handleOnChange}
            />
        </FloatingLabel>
        <Button disabled={isLoading || !content} variant={"outline-primary"} type={"submit"}>Soumettre</Button>

        <ToastContainer
            className="p-3 position-static"
            position={"bottom-end"}
            style={{zIndex: 1}}
        >
            <Toast show={showToast} onClose={closeToast} autohide={true}>
                <Toast.Header>
                    <strong className="me-auto">Citation sauvegardée</strong>
                    <small></small>
                </Toast.Header>
                <Toast.Body>Vous pouvez la retrouver dans votre quotedex.</Toast.Body>
            </Toast>
        </ToastContainer>
    </Form>;
}