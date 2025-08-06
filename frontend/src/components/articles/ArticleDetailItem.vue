<template>
  <div class="article-detail-page">
    <div v-if="article" class="article-detail">
      <div class="article-header">
        <h1 class="article-title">{{ article.title }}</h1>
        <div class="article-meta">
          <span class="date-badge">
            <span class="date-icon">📅</span>
            {{
              (article.updated_at > article.published_at)
                  ? new Date(article.updated_at).toLocaleDateString()
                  : new Date(article.published_at).toLocaleDateString()
            }}
          </span>
        </div>
      </div>

      <div class="article-content">{{ article.content }}</div>

      <div class="article-actions">
        <router-link to="/" class="btn btn-secondary">
          <span class="btn-icon">←</span>
          Back to Home
        </router-link>
        <button @click="openForm" class="btn btn-primary">
          <span class="btn-icon">💬</span>
          Add Comment
        </button>
      </div>

      <div class="comments-section">
        <h3 class="comments-title">
          <span class="comments-icon">💬</span>
          Comments ({{ comments.length }})
        </h3>
        <div v-if="comments.length === 0" class="no-comments">
          <span class="no-comments-icon">🤔</span>
          <p>No comments yet. Be the first to share your thoughts!</p>
        </div>
        <div v-else class="comments-list">
          <div v-for="item in comments" :key="item.id" class="comment-item">
            <div class="comment-header">
              <span class="comment-author">{{ item.commentingUser }}</span>
            </div>
            <div class="comment-content">{{ item.comment }}</div>
          </div>
        </div>
      </div>
    </div>

    <div v-else-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p class="loading-text">Loading article...</p>
    </div>

    <div v-else class="error-container">
      <div class="error-icon">❌</div>
      <h2 class="error-title">Article Not Found</h2>
      <p class="error-message">The article you're looking for doesn't exist or has been removed.</p>
      <router-link to="/" class="btn btn-primary">
        <span class="btn-icon">←</span>
        Back to Home
      </router-link>
    </div>

    <div v-if="showForm" class="modal-overlay" @click.self="closeForm">
      <div class="modal">
        <div class="modal-header">
          <h2>Write Your Comment</h2>
          <button @click="closeForm" class="close-btn">&times;</button>
        </div>
        <form @submit.prevent="publishComment" class="comment-form">
          <div class="form-group">
            <label for="comment">Your Comment</label>
            <textarea
                id="comment"
                v-model="comment"
                placeholder="Share your thoughts about this article..."
                rows="4"
                required
            ></textarea>
          </div>
          <div class="form-actions">
            <button type="button" @click="closeForm" class="btn btn-outline">
              Cancel
            </button>
            <button type="submit" class="btn btn-primary">
              <span class="btn-icon">📝</span>
              Post Comment
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { articleService } from '@/services/ArticleService';
import {commentService} from "@/services/CommentService.js";
import {authService} from "@/services/AuthService.js";

export default {
  name: 'ArticleDetailItem',
  data() {
    return {
      article: null,
      loading: true,
      showForm: false,
      comment: '',
      comments: [],
      commentingUser: null
    }
  },

  async mounted() {
    const id = this.$route.params.id;
    await this.fetchArticle(id);
    this.loadAllComments();
  },

  watch: {
    '$route'(to, from) {
      if (to.params.id !== from.params.id) {
        this.fetchArticle(to.params.id);
      }
    }
  },

  methods: {
    async fetchArticle(id) {
      try {
        this.loading = true;
        this.article = await articleService.getArticleById(id);
      } catch(error) {
        console.error('Error fetching article:', error);
        this.article = null;
      } finally {
        this.loading = false;
      }
    },

    openForm() {
      this.showForm = true;
    },

    closeForm(){
      this.showForm = false;
      this.comment = '';
    },

    async publishComment() {
      try{
        await commentService.addComment(
            {comment: this.comment,
              commentingUser: authService.getCurrentUser().username}
        )
        this.closeForm();
        await this.loadAllComments();
      } catch (error) {
        console.error(error);
      }
    },

    async loadAllComments() {
      try {
        this.comments = await commentService.getComments();
      } catch(error) {
        console.error(error);
        this.comments = [];
      }
    }
  }
}
</script>

<style scoped>
.article-detail-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  padding: 1rem;
}

.article-detail {
  max-width: 800px;
  margin: 0 auto;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  overflow: hidden;
  animation: slideUp 0.6s cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.article-header {
  padding: 2rem 2rem 1.5rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  position: relative;
  overflow: hidden;
}

.article-header::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
  animation: float 6s ease-in-out infinite;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-10px);
  }
}

.article-title {
  margin: 0 0 1rem 0;
  font-size: 1.8rem;
  font-weight: 700;
  line-height: 1.2;
  position: relative;
  z-index: 1;
}

.article-meta {
  position: relative;
  z-index: 1;
}

.date-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 500;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.date-icon {
  font-size: 1rem;
}

.article-content {
  padding: 2rem;
  font-size: 1rem;
  line-height: 1.6;
  color: #374151;
  white-space: pre-line;
  background: white;
}

.article-actions {
  display: flex;
  gap: 1rem;
  padding: 0 2rem 2rem;
  justify-content: space-between;
  align-items: center;
  background: white;
}

.btn {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.6rem 1.2rem;
  border: none;
  border-radius: 10px;
  font-size: 0.9rem;
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

.comments-section {
  background: #f8fafc;
  border-top: 1px solid #e2e8f0;
  padding: 2rem;
}

.comments-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin: 0 0 1.5rem 0;
  font-size: 1.3rem;
  font-weight: 700;
  color: #1f2937;
}

.comments-icon {
  font-size: 1.25rem;
}

.no-comments {
  text-align: center;
  padding: 2rem;
  color: #6b7280;
}

.no-comments-icon {
  display: block;
  font-size: 2.5rem;
  margin-bottom: 0.75rem;
}

.no-comments p {
  font-size: 1rem;
  margin: 0;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.comment-item {
  background: white;
  border-radius: 12px;
  padding: 1.25rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  border: 1px solid #e5e7eb;
  transition: all 0.3s ease;
}

.comment-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.comment-header {
  margin-bottom: 0.5rem;
}

.comment-author {
  font-weight: 600;
  color: #667eea;
  font-size: 0.9rem;
}

.comment-content {
  color: #374151;
  line-height: 1.5;
  font-size: 0.95rem;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  text-align: center;
}

.loading-spinner {
  width: 50px;
  height: 50px;
  border: 3px solid #f3f4f6;
  border-top: 3px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1.5rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-text {
  font-size: 1.1rem;
  color: #6b7280;
  margin: 0;
}

.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  text-align: center;
  max-width: 500px;
  margin: 0 auto;
}

.error-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.error-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #dc2626;
  margin: 0 0 0.75rem 0;
}

.error-message {
  font-size: 1rem;
  color: #6b7280;
  margin: 0 0 1.5rem 0;
  line-height: 1.5;
}

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
  border-radius: 16px;
  min-width: 450px;
  max-width: 90vw;
  max-height: 90vh;
  overflow: hidden;
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.3);
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
  padding: 1.5rem 1.5rem 1rem;
  border-bottom: 1px solid #f0f0f0;
}

.modal-header h2 {
  margin: 0;
  font-size: 1.3rem;
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

.comment-form {
  padding: 1.5rem;
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

@media (max-width: 768px) {
  .article-detail-page {
    padding: 1rem;
  }

  .article-header {
    padding: 2rem 2rem 1.5rem;
  }

  .article-title {
    font-size: 2rem;
  }

  .article-content {
    padding: 2rem;
    font-size: 1.1rem;
  }

  .article-actions {
    padding: 0 2rem 2rem;
    flex-direction: column;
    gap: 1rem;
  }

  .comments-section {
    padding: 2rem;
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
  .article-detail-page {
    padding: 0.5rem;
  }

  .article-header {
    padding: 1.5rem;
  }

  .article-title {
    font-size: 1.75rem;
  }

  .article-content {
    padding: 1.5rem;
    font-size: 1rem;
  }

  .article-actions {
    padding: 0 1.5rem 1.5rem;
  }

  .comments-section {
    padding: 1.5rem;
  }

  .modal-header,
  .comment-form {
    padding: 1.5rem;
  }
}
</style>