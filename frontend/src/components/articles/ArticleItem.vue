<template>
  <div class="articles">
    <table>
      <tbody>
        <tr v-for="article in articles" :key="article.id" class="rows">
          <td>
            <router-link :to="`/articles/${article.id}`" class="article-link">
              {{ article.title }}
            </router-link>
          </td>
          <td>{{ new Date(article.updated_at).toLocaleDateString() }}</td>
          <button @click="openEditForm(article.id)" class="edit-btn">Edit</button>
          <button @click="deleteArticle(article.id)" class="delete-btn" >Delete</button>
        </tr>
      </tbody>
    </table>
    <div v-if="showEditForm" class="modal-overlay" @click.self="closeEditForm">
      <div class="modal">
        <form @submit.prevent="submitEdit">
          <p>Edit article</p>
          <label>Title</label>
          <input v-model="selectedArticle.title"/>
          <label>Content</label>
          <input v-model="selectedArticle.content"/>
          <button type="submit" @click="submitEdit">Save</button>
          <button type="button" @click="closeEditForm">Cancel</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { articleService } from '@/services/ArticleService';

export default {
  name: 'ArticleItem',
  data() {
    return {
      articles: [],
      selectedArticle: null,
      showEditForm: false,
      id: '',
    }
  },

  async mounted() {
    await this.fetchArticles();
  },

  methods: {
    openEditForm(id){
      this.showEditForm = true;
      const article = this.articles.find(a => a.id === id);
      this.selectedArticle = {...article};
    },

    closeEditForm(){
      this.showEditForm = false;
    },

    async fetchArticles() {
      try {
        this.articles = await articleService.getArticles();
      } catch(error) {
        console.error('Error fetching articles:', error);
      }
    },

    async submitEdit() {
      await articleService.updateArticle(this.selectedArticle.id, {
        title: this.selectedArticle.title,
        content: this.selectedArticle.content,
        updated_at: new Date().toISOString()
      });
      this.showEditForm = false;
      this.fetchArticles();
    },

    async deleteArticle(id) {
      await articleService.deleteArticle(id);
      await this.fetchArticles();
    }
  }
}
</script>

<style scoped>
.articles {
  margin-top: 24px;
}

.rows td:first-child {
  padding-right: 48px;
}
.rows td:last-child {
  padding-left: 96px;
}

.article-link {
  color: blue;
  text-decoration: none;
}

.article-link:hover {
  background-color: lightskyblue;
}

.edit-btn {
  margin-left: 20px;
}

.delete-btn {
  margin-left: 20px;
}

.modal-overlay {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal {
  background: #fff;
  padding: 32px;
  border-radius: 8px;
  min-width: 300px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.2);
}
</style>