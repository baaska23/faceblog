<template>
  <div class="home-page">
    <div class="header">
      <h1 class="page-title">FaceBlog</h1>
      <div class="button-group">
        <button @click="openForm()" class="btn btn-primary">
          <span class="btn-icon">+</span>
          Add Article
        </button>
        <button @click="logout" class="btn btn-secondary">
          <span class="btn-icon">→</span>
          Log out
        </button>
      </div>
    </div>

    <div class="content-area">
      <div class="search-area">
        <input type="search" class="search-bar" v-model="searchPrefix" @input="handleSearchInput"
               @keyup.passive="searchTrie(searchPrefix)" placeholder="Search article"/>
      </div>
      <ArticleItem :articles="articles" />
    </div>
    <div v-if="showForm" class="modal-overlay" @click.self="closeForm">
      <div class="modal">
        <div class="modal-header">
          <h2>Create New Article</h2>
          <button @click="closeForm" class="close-btn">&times;</button>
        </div>
        <form @submit.prevent="publishArticle" class="article-form">
          <div class="form-group">
            <label for="title">Title</label>
            <input
                id="title"
                v-model="title"
                type="text"
                placeholder="Enter article title..."
                required
            />
          </div>
          <div class="form-group">
            <label for="content">Content</label>
            <textarea
                id="content"
                v-model="content"
                placeholder="Write your article content..."
                rows="6"
                required
            ></textarea>
          </div>
          <div class="form-actions">
            <button type="button" @click="closeForm" class="btn btn-outline">
              Cancel
            </button>
            <button type="submit" class="btn btn-primary">
              <span class="btn-icon">📝</span>
              Publish Article
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import ArticleItem from '@/components/articles/ArticleItem.vue';
import {articleService} from "@/services/ArticleService.js";
import {authService} from "@/services/AuthService.js";
import {useRouter} from "vue-router";

export default {
  name: 'HomePage',
  setup() {
    const router = useRouter();
    return {router};
  },
  data() {
    return {
      articles: [], // This will hold all articles or search results
      showForm: false,
      title: '',
      content: '',
      searchPrefix: ''
    }
  },
  components: {
    ArticleItem
  },

  async mounted() {
    // Load all articles when component mounts
    await this.loadAllArticles();
  },

  methods: {
    async loadAllArticles() {
      try {
        this.articles = await articleService.getArticles(); // or getAllArticles()
      } catch (error) {
        console.error('Failed to load articles:', error);
        this.articles = [];
      }
    },

    async searchTrie(prefix) {
      console.log('Searching for:', prefix); // Debug log
      try {
        if (prefix && prefix.trim()) {
          this.articles = await articleService.searchArticleByTrie(prefix.trim());
          console.log('Search results:', this.articles.length, 'articles'); // Debug log
        } else {
          await this.loadAllArticles();
        }
      } catch (error) {
        console.error('Search failed:', error);
        this.articles = [];
      }
    },

    handleSearchInput() {
      // If search is cleared, show all articles
      if (!this.searchPrefix.trim()) {
        this.loadAllArticles();
      }
    },

    openForm() {
      this.showForm = true;
    },

    logout() {
      authService.logout();
      this.$router.push('/');
    },

    closeForm() {
      this.showForm = false;
      this.title = '';
      this.content = '';
    },

    async publishArticle() {
      try {
        await articleService.addArticle({
          title: this.title,
          content: this.content,
          published_at: new Date().toISOString()
        });
        this.closeForm();
        // Refresh articles list
        await this.loadAllArticles();
      } catch (error) {
        console.error('Failed to publish article:', error);
      }
    }
  }
}
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 2rem;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.page-title {
  margin: 0;
  font-size: 2.5rem;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.button-group {
  display: flex;
  gap: 1rem;
  align-items: center;
}

/* Modern Button Styles */
.btn {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 12px;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  text-decoration: none;
  position: relative;
  overflow: hidden;
  transform: translateY(0);
}

.btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s;
}

.btn:hover::before {
  left: 100%;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.6);
}

.btn-secondary {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(245, 87, 108, 0.4);
}

.btn-secondary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(245, 87, 108, 0.6);
}

.btn-outline {
  background: transparent;
  color: #667eea;
  border: 2px solid #667eea;
  box-shadow: none;
}

.btn-outline:hover {
  background: #667eea;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.btn-icon {
  font-size: 1.1rem;
  font-weight: bold;
}

.content-area {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(5px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  opacity: 0;
  animation: fadeIn 0.3s ease-out forwards;
}

@keyframes fadeIn {
  to {
    opacity: 1;
  }
}

.modal {
  background: white;
  border-radius: 20px;
  min-width: 500px;
  max-width: 90vw;
  max-height: 90vh;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  transform: scale(0.9) translateY(20px);
  animation: modalSlideIn 0.3s ease-out forwards;
}

@keyframes modalSlideIn {
  to {
    transform: scale(1) translateY(0);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 2rem 2rem 1rem;
  border-bottom: 1px solid #f0f0f0;
}

.modal-header h2 {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 700;
  color: #333;
}

.close-btn {
  background: none;
  border: none;
  font-size: 2rem;
  cursor: pointer;
  color: #999;
  transition: color 0.2s;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}

.close-btn:hover {
  color: #333;
  background: #f5f5f5;
}

.article-form {
  padding: 2rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
  color: #333;
  font-size: 0.95rem;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 2px solid #e1e5e9;
  border-radius: 12px;
  font-size: 1rem;
  transition: all 0.3s ease;
  font-family: inherit;
  resize: vertical;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  transform: translateY(-1px);
}

.form-group textarea {
  min-height: 120px;
  line-height: 1.5;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 2rem;
  padding-top: 1rem;
  border-top: 1px solid #f0f0f0;
}

.search-area {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.search-bar {
  width: 100%;
  max-width: 400px;
  padding: 0.75rem 1.25rem;
  border: 2px solid #e1e5e9;
  border-radius: 12px;
  font-size: 1rem;
  background: #fff;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.07);
  transition: border-color 0.3s, box-shadow 0.3s;
  font-family: inherit;
}

.search-bar:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.12);
  background: #f5f7fa;
}

/* Responsive Design */
@media (max-width: 768px) {
  .header {
    flex-direction: column;
    gap: 1rem;
    padding: 1.5rem;
  }

  .page-title {
    font-size: 2rem;
  }

  .button-group {
    width: 100%;
    justify-content: center;
  }

  .modal {
    min-width: 0;
    margin: 1rem;
  }

  .form-actions {
    flex-direction: column;
  }

  .btn {
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .content-area {
    padding: 1rem;
  }

  .modal-header,
  .article-form {
    padding: 1.5rem;
  }

  .page-title {
    font-size: 1.75rem;
  }
}
</style>