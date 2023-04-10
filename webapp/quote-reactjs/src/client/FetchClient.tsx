import axios from 'axios';
import {getLocalJwtToken} from "../auth/AuthUtils";

const fetchClient = () => {
    const defaultOptions = {
        baseURL: 'http://localhost:8080',
        method: 'get',
        headers: {
            'Content-Type': 'application/json',
        },
    };

    // Create instance
    let instance = axios.create(defaultOptions);

    // Set the AUTH token for any request
    instance.interceptors.request.use((config) => {
        const token = getLocalJwtToken();
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    });

    return instance;
};

export default fetchClient();