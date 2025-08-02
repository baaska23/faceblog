import api from "@/services/api.js";

export const authService = {
    async login(username, password) {
        try {
            const response = await api.post("/auth/login", { username, password });
            return response.data;
        } catch (error) {
            alert(error);
            throw error;
        }
    },

    logout() {
        sessionStorage.removeItem('user');
    },

    isAuthenticated() {
        return !!sessionStorage.getItem('user');
    },

    getCurrentUser() {
        const userStr = sessionStorage.getItem('user');
        return userStr ? JSON.parse(userStr) : null;
    },

    getAuthToken() {
        const userStr = sessionStorage.getItem('user');
        const user = userStr ? JSON.parse(userStr) : null;
        return user && user.token ? user.token : null;
    }
};