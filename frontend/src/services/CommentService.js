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

    async addComment(comment, articleId) {
        try {
            const response = await api.post(`/comments/${articleId}`, comment);
            return response.data;
        } catch(error) {
            console.error(error);
        }
    },

    async getCommentsPerArticle(articleId) {
        try {
            const response = await api.get(`/comments-per-article/${articleId}`);
            return response.data;
        } catch(error) {
            console.error(error);
        }
    }
}