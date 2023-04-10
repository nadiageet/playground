import Button from 'react-bootstrap/Button';

import Form from 'react-bootstrap/Form';
import {useState} from "react";
import {LoginJwtResponse} from "./LoginJwtResponse";
import {Navigate} from "react-router-dom";
import fetchClient from "../client/FetchClient";


interface LoginFormProps {
    isAuthenticated: boolean
    onSuccessfullyLogin: (jwt: LoginJwtResponse) => void
}

export default function LoginForm({isAuthenticated, onSuccessfullyLogin}: LoginFormProps) {
    const [userName, setUserName] = useState('');
    const [password, setPassword] = useState('');


    if (isAuthenticated) {
        console.log("User already authenticated : redirecting from '/login' to '/'")
        return <Navigate to={"/"}/>
    }

    async function handleSubmit(event: any) {
        event.preventDefault();
        console.log("Submitted", userName, password);

        try {
            const response = await fetchClient.post<LoginJwtResponse>('/api/v1/auth/login', {
                userName,
                password
            });
            console.log("Server response after login : ", response.data);
            onSuccessfullyLogin(response.data);
        } catch (e: any) {
            if (e.response.status === 403) {
                alert("bad username / password");
            }
            console.error(e);
        }


    }

    return (
        <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label>Login</Form.Label>
                <Form.Control onChange={e => setUserName(e.target.value)} placeholder="Votre login..."/>
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicPassword">
                <Form.Label>Password</Form.Label>
                <Form.Control onChange={e => setPassword(e.target.value)} type="password"
                              placeholder="Votre mot de passe..."/>
            </Form.Group>

            <Button type={"submit"}>Se connecter</Button>

        </Form>);


}