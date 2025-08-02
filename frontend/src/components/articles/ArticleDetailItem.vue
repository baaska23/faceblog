<template>
  <div v-if="article" class="article-detail">
    <h1>{{ article.title }}</h1>
    <div class="date">
      {{
        (article.updated_at > article.published_at)
            ? new Date(article.updated_at).toLocaleDateString()
            : new Date(article.published_at).toLocaleDateString()
      }}
    </div>
    <div class="content">{{ article.content }}</div>
    <router-link to="/" class="back-btn">Back</router-link>
  </div>
  <div v-else-if="loading" class="loading">
    Loading article...
  </div>
  <div v-else class="error">
    Article not found
  </div>
</template>

<script>
import { articleService } from '@/services/ArticleService';

export default {
  name: 'ArticleDetailItem',
  data() {
    return {
      article: null,
      loading: true
    }
  },

  async mounted() {
    const id = this.$route.params.id;
    await this.fetchArticle(id);
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
    }
  }
}
</script>

<style scoped>
/* Improved styles for ArticleDetailItem component */

.article-detail {
  margin: 2rem auto;
  padding: 2rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid #e5e7eb;
  max-width: 800px;
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.article-detail h1 {
  margin: 0 0 1rem 0;
  font-size: 2rem;
  font-weight: 700;
  color: #1f2937;
  line-height: 1.3;
  border-bottom: 3px solid #4f46e5;
  padding-bottom: 0.75rem;
}

.date {
  display: inline-flex;
  align-items: center;
  font-size: 0.9rem;
  color: #6b7280;
  background: #f3f4f6;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  margin-bottom: 1.5rem;
  font-weight: 500;
}

.date::before {
  content: "📅";
  margin-right: 0.5rem;
  font-size: 0.8rem;
}

.content {
  font-size: 1.1rem;
  line-height: 1.7;
  color: #374151;
  white-space: pre-line;
  background: #f9fafb;
  padding: 1.5rem;
  border-radius: 8px;
  border-left: 4px solid #4f46e5;
  margin-bottom: 1.5rem;
  font-family: 'Georgia', 'Times New Roman', serif;
}

.loading {
  text-align: center;
  padding: 3rem 2rem;
  font-size: 1.1rem;
  color: #6b7280;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid #e5e7eb;
  max-width: 400px;
  margin: 2rem auto;
}

.loading::before {
  content: "⏳";
  display: block;
  font-size: 2rem;
  margin-bottom: 1rem;
  animation: pulse 1.5s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

.error {
  text-align: center;
  padding: 3rem 2rem;
  font-size: 1.1rem;
  color: #dc2626;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid #fecaca;
  max-width: 400px;
  margin: 2rem auto;
}

.error::before {
  content: "❌";
  display: block;
  font-size: 2rem;
  margin-bottom: 1rem;
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  background: #4f46e5;
  color: white;
  border-radius: 8px;
  text-decoration: none;
  font-weight: 600;
  font-size: 0.9rem;
  transition: all 0.2s ease;
  margin-top: 1rem;
}

.back-btn::before {
  content: "←";
  font-size: 1rem;
  font-weight: bold;
}

.back-btn:hover {
  background: #4338ca;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(79, 70, 229, 0.3);
}

.back-btn:active {
  transform: translateY(0);
}

/* Responsive Design */
@media (max-width: 768px) {
  .article-detail {
    margin: 1rem;
    padding: 1.5rem;
  }

  .article-detail h1 {
    font-size: 1.75rem;
  }

  .content {
    font-size: 1rem;
    padding: 1.25rem;
  }

  .loading,
  .error {
    margin: 1rem;
    padding: 2rem 1.5rem;
  }
}

@media (max-width: 480px) {
  .article-detail {
    margin: 0.5rem;
    padding: 1.25rem;
  }

  .article-detail h1 {
    font-size: 1.5rem;
  }

  .content {
    font-size: 0.95rem;
    line-height: 1.6;
    padding: 1rem;
  }

  .date {
    font-size: 0.8rem;
    padding: 0.4rem 0.8rem;
  }

  .back-btn {
    width: 100%;
    justify-content: center;
    padding: 1rem 1.5rem;
  }
}
</style>