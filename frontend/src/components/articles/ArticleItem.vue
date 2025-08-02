<template>
  <div class="articles-container">
    <div v-if="articles.length === 0" class="empty-state">
      <div class="empty-icon">📝</div>
      <h3>No articles yet</h3>
      <p>Start creating your first article to see it here!</p>
    </div>

    <div v-else class="articles-grid">
      <div
          v-for="article in articles"
          :key="article.id" class="article-card" @click="navigateToArticle(article.id)"
      >
        <div class="article-header">
          <h3 class="article-title">{{ article.title }}</h3>
          <div class="article-date">
            <span class="date-icon">📅</span>
            {{
              (article.updated_at > article.published_at)
                  ? new Date(article.updated_at).toLocaleDateString()
                  : new Date(article.published_at).toLocaleDateString()
            }}
          </div>
        </div>

        <div class="article-content">
          <p class="article-excerpt">{{ getExcerpt(article.content) }}</p>
        </div>

        <div class="article-footer">
          <router-link :to="`/articles/${article.id}`" class="read-more-btn">
            <span class="btn-icon">👁️</span>
            Read More
          </router-link>

          <div v-if="isAdmin" class="admin-actions">
            <button @click.stop="openEditForm(article.id)" class="btn btn-outline edit-btn">
              <span class="btn-icon">✏️</span>
              Edit
            </button>
            <button @click.stop="confirmDelete(article.id)" class="btn btn-danger delete-btn">
              <span class="btn-icon">🗑️</span>
              Delete
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Edit Modal -->
    <div v-if="showEditForm" class="modal-overlay" @click.self="closeEditForm">
      <div class="modal">
        <div class="modal-header">
          <h2>Edit Article</h2>
          <button @click="closeEditForm" class="close-btn">&times;</button>
        </div>
        <form @submit.prevent="submitEdit" class="edit-form">
          <div class="form-group">
            <label for="edit-title">Title</label>
            <input
                id="edit-title" v-model="selectedArticle.title"
                type="text" placeholder="Article title..." required
            />
          </div>
          <div class="form-group">
            <label for="edit-content">Content</label>
            <textarea
                id="edit-content" v-model="selectedArticle.content"
                placeholder="Article content..." rows="8" required
            ></textarea>
          </div>
          <div class="form-actions">
            <button type="button" @click="closeEditForm" class="btn btn-outline">
              Cancel
            </button>
            <button type="submit" class="btn btn-primary">
              <span class="btn-icon">💾</span>
              Save Changes
            </button>
          </div>
        </form>
      </div>
    </div>

    <div v-if="showDeleteConfirm" class="modal-overlay" @click.self="closeDeleteConfirm">
      <div class="modal confirmation-modal">
        <div class="modal-header">
          <h2>Confirm Delete</h2>
          <button @click="closeDeleteConfirm" class="close-btn">&times;</button>
        </div>
        <div class="confirmation-content">
          <div class="warning-icon">⚠️</div>
          <p>Are you sure you want to delete this article? This action cannot be undone.</p>
        </div>
        <div class="form-actions">
          <button @click="closeDeleteConfirm" class="btn btn-outline">
            Cancel
          </button>
          <button @click="executeDelete" class="btn btn-danger">
            <span class="btn-icon">🗑️</span>
            Delete Article
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { articleService } from '@/services/ArticleService';
import { authService } from "@/services/AuthService.js";
import { useRouter } from 'vue-router';

export default {
  name: 'ArticleItem',
  props: {
    // Add this props section
    articles: {
      type: Array,
      default: () => []
    }
  },
  setup() {
    const router = useRouter();
    return { router };
  },
  data() {
    return {
      // Remove 'articles: []' from here since it's now a prop
      selectedArticle: null,
      showEditForm: false,
      showDeleteConfirm: false,
      articleToDelete: null,
      loading: false
    }
  },

  // Remove the mounted() hook since parent will provide articles
  // async mounted() {
  //   await this.fetchArticles();
  // },

  computed: {
    isAdmin() {
      const user = authService.getCurrentUser();
      return user && user.role === 'ADMIN';
    }
  },

  methods: {
    navigateToArticle(id) {
      this.router.push(`/articles/${id}`);
    },

    formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
      });
    },

    getExcerpt(content) {
      if (!content) return '';
      return content.length > 150 ? content.substring(0, 150) + '...' : content;
    },

    openEditForm(id) {
      this.showEditForm = true;
      const article = this.articles.find(a => a.id === id);
      this.selectedArticle = { ...article };
    },

    closeEditForm() {
      this.showEditForm = false;
      this.selectedArticle = null;
    },

    confirmDelete(id) {
      this.articleToDelete = id;
      this.showDeleteConfirm = true;
    },

    closeDeleteConfirm() {
      this.showDeleteConfirm = false;
      this.articleToDelete = null;
    },

    async executeDelete() {
      try {
        await articleService.deleteArticle(this.articleToDelete);
        // Emit event to parent to refresh articles instead of fetching here
        this.$emit('article-deleted');
        this.closeDeleteConfirm();
      } catch (error) {
        console.error('Error deleting article:', error);
      }
    },

    // Remove fetchArticles() method since parent handles this now

    async submitEdit() {
      try {
        await articleService.updateArticle(this.selectedArticle.id, {
          title: this.selectedArticle.title,
          content: this.selectedArticle.content,
          updated_at: new Date().toISOString()
        });
        this.closeEditForm();
        // Emit event to parent to refresh articles
        this.$emit('article-updated');
      } catch (error) {
        console.error('Error updating article:', error);
      }
    }
  }
}
</script>

<style scoped>
/* Simplified and improved styles for existing template structure */

.articles-container {
  margin-top: 2rem;
}

.empty-state {
  text-align: center;
  padding: 3rem 2rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid #e5e7eb;
}

.empty-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
  opacity: 0.6;
}

.empty-state h3 {
  margin: 0 0 0.5rem 0;
  color: #374151;
  font-size: 1.25rem;
  font-weight: 600;
}

.empty-state p {
  margin: 0;
  color: #6b7280;
  font-size: 0.95rem;
}

.articles-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 1.25rem;
  margin-top: 1rem;
}

.article-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  border: 1px solid #e5e7eb;
  transition: all 0.2s ease;
  cursor: pointer;
}

.article-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.article-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
  gap: 1rem;
}

.article-title {
  margin: 0;
  font-size: 1.125rem;
  font-weight: 600;
  color: #1f2937;
  line-height: 1.4;
  flex: 1;
}

.article-date {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.8rem;
  color: #6b7280;
  background: #f3f4f6;
  padding: 0.25rem 0.5rem;
  border-radius: 6px;
  white-space: nowrap;
}

.date-icon {
  font-size: 0.75rem;
}

.article-content {
  margin-bottom: 1.25rem;
}

.article-excerpt {
  margin: 0;
  color: #6b7280;
  line-height: 1.5;
  font-size: 0.9rem;
}

.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
}

.read-more-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: #4f46e5;
  color: white;
  text-decoration: none;
  border-radius: 6px;
  font-size: 0.85rem;
  font-weight: 500;
  transition: background-color 0.2s ease;
}

.read-more-btn:hover {
  background: #4338ca;
}

.admin-actions {
  display: flex;
  gap: 0.5rem;
}

/* Modern Button Styles */
.btn {
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
  padding: 0.5rem 0.875rem;
  border: none;
  border-radius: 6px;
  font-size: 0.8rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  text-decoration: none;
}

.btn-primary {
  background: #4f46e5;
  color: white;
}

.btn-primary:hover {
  background: #4338ca;
}

.btn-outline {
  background: transparent;
  color: #4f46e5;
  border: 1px solid #4f46e5;
}

.btn-outline:hover {
  background: #4f46e5;
  color: white;
}

.btn-danger {
  background: #dc2626;
  color: white;
}

.btn-danger:hover {
  background: #b91c1c;
}

.btn-icon {
  font-size: 0.85rem;
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background: white;
  border-radius: 12px;
  min-width: 400px;
  max-width: 90vw;
  max-height: 90vh;
  overflow: hidden;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem 1.5rem 1rem;
  border-bottom: 1px solid #e5e7eb;
}

.modal-header h2 {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 600;
  color: #1f2937;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #6b7280;
  padding: 0.25rem;
  border-radius: 4px;
  transition: color 0.2s ease;
}

.close-btn:hover {
  color: #374151;
  background: #f3f4f6;
}

.edit-form {
  padding: 1.5rem;
}

.form-group {
  margin-bottom: 1.25rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #374151;
  font-size: 0.9rem;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 0.9rem;
  transition: border-color 0.2s ease;
  font-family: inherit;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #4f46e5;
  box-shadow: 0 0 0 2px rgba(79, 70, 229, 0.1);
}

.form-group textarea {
  resize: vertical;
  min-height: 100px;
}

.form-actions {
  display: flex;
  gap: 0.75rem;
  justify-content: flex-end;
  margin-top: 1.5rem;
  padding-top: 1rem;
  border-top: 1px solid #e5e7eb;
}

.confirmation-modal {
  min-width: 320px;
}

.confirmation-content {
  padding: 1.5rem;
  text-align: center;
}

.warning-icon {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.confirmation-content p {
  margin: 0;
  font-size: 0.9rem;
  color: #6b7280;
  line-height: 1.4;
}

/* Responsive Design */
@media (max-width: 768px) {
  .articles-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }

  .article-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }

  .article-date {
    align-self: flex-start;
  }

  .article-footer {
    flex-direction: column;
    align-items: stretch;
    gap: 0.75rem;
  }

  .admin-actions {
    justify-content: space-between;
  }

  .modal {
    min-width: 0;
    margin: 1rem;
  }

  .form-actions {
    flex-direction: column;
  }
}

@media (max-width: 480px) {
  .articles-container {
    margin-top: 1rem;
  }

  .article-card {
    padding: 1.25rem;
  }

  .modal-header,
  .edit-form,
  .confirmation-content {
    padding: 1.25rem;
  }

  .btn {
    justify-content: center;
    padding: 0.625rem 1rem;
  }
}
</style>