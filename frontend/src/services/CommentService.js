import api from "@/services/api.js";

export const commentService = {
    async getComments() {
        try {
            const response = await api.get("/comments");
            return response.data;
        } catch(error) {
            console.error(error);
        }
    },

    async addComment(comment) {
        try {
            const response = await api.post("/comments", comment);
            return response.data;
        } catch(error) {
            console.error(error);
        }
    }
}