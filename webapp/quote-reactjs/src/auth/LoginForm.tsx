import Button from 'react-bootstrap/Button';

import Form from 'react-bootstrap/Form';
import {useState} from "react";
import axios from "axios";
import {LoginJwtResponse} from "./LoginJwtResponse";
import {useNavigate} from "react-router-dom";


interface LoginFormProps {
    onSuccessfullyLogin: () => void
}

export default function LoginForm({onSuccessfullyLogin}: LoginFormProps) {
    const [userName, setUserName] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();

    async function handleSubmit(event: any) {
        event.preventDefault();
        console.log("Submitted", userName, password);

        try {
            const response = await axios.post<LoginJwtResponse>('http://localhost:8080/api/v1/auth/login', {
                userName,
                password
            });
            const data: LoginJwtResponse = response.data;
            localStorage.setItem("react-jwt", data.jwt);
            console.log(response.data);
            onSuccessfullyLogin();
            navigate("/");
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