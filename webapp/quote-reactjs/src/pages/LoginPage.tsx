import LoginForm, {LoginFormProps} from "../auth/LoginForm";
import AnonymousOnlyRoute from "../auth/components/AnonymousOnlyRoute";

export function LoginPage({onSuccessfullyLogin}: LoginFormProps) {
    console.log("login page");
    return <AnonymousOnlyRoute>
        <LoginForm onSuccessfullyLogin={onSuccessfullyLogin}/>
    </AnonymousOnlyRoute>
}