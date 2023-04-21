import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import React, {useState} from "react";
import fetchClient from "../client/FetchClient";
import useLocalStorage, {LocalStorageContainer} from "../hooks/UseLocalStorage";
import {LoginJwtResponse} from "./dto/LoginJwtResponse";


interface LoginFormProps {
    onSuccessfullyLogin: (jwt: LoginJwtResponse) => void
}

const USERNAME_KEY = "username";
export default function LoginForm({onSuccessfullyLogin}: LoginFormProps) {
    const [userName, setUserName] = useLocalStorage<LocalStorageContainer<string>>('username');
    const [password, setPassword] = useState<string>('pass');

    async function handleSubmit(event: any) {
        event.preventDefault();
        console.log("Submitted", userName, password);

        try {
            const response = await fetchClient.post<LoginJwtResponse>('/api/v1/auth/login', {
                userName: userName?.value,
                password
            });
            console.log("Server response after login : ", response.data);
            onSuccessfullyLogin(response.data);
        } catch (e: any) {
            if (e.response.status === 403) {
                setPassword('')
                alert("bad credentials");
            }
            console.error(e);
        }


    }

    function handleUsernameChange(e: any) {
        setUserName({value: e?.target?.value});
    }

    return (
        <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label>Login</Form.Label>
                <Form.Control
                    value={userName?.value || ''}
                    onChange={(e) => handleUsernameChange(e)}
                    placeholder="Votre login..."/>
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicPassword">
                <Form.Label>Password</Form.Label>
                <Form.Control
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    type="password"
                    placeholder="Votre mot de passe..."/>
            </Form.Group>

            <Button type={"submit"}>Se connecter</Button>

        </Form>);


}