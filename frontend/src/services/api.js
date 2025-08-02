import axios from "axios";
import {authService} from "@/services/AuthService.js";

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    timeout: 10000,
    headers: {
        "Content-Type": 'application/json'
    }
});

api.interceptors.request.use(
    (config) => {
        const token = authService.getAuthToken();
        if (token) {
            config.headers.Authorization = token;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            authService.logout();
            window.location.href = '/';
        }
        return Promise.reject(error);
    }
);

export default api;