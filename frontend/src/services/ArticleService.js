import api from "./api";

export const articleService = {
    async getArticles(){
        try {
            const response = await api.get("/articles");
            return response.data;
        } catch(error) {
            console.error(error);
        }
    },

    async getArticleById(id) {
        try {
            const response = await api.get(`/articles/${id}`);
            return response.data;
        } catch(error) {
            console.error(error);
        }
    },

    async addArticle(article) {
        try {
            const response = await api.post("/articles", article);
            return response.data;
        } catch (error) {
            console.error(error);
        }
    }
}