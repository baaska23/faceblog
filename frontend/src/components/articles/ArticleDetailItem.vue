<template>
  <div v-if="article" class="article-detail">
    <h1>{{ article.title }}</h1>
    <div class="date">{{ new Date(article.updated_at).toLocaleDateString() }}</div>
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
.article-detail {
  margin-top: 32px;
  padding: 32px;
  background: #f9f9f9;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  max-width: 700px;
  margin-left: auto;
  margin-right: auto;
}

.date {
  font-size: 0.95rem;
  color: #888;
  margin-bottom: 24px;
}

.content {
  font-size: 1.15rem;
  line-height: 1.7;
  color: #222;
  white-space: pre-line;
  background: #fff;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(25,118,210,0.07);
}

.loading, .error {
  text-align: center;
  padding: 32px;
  font-size: 1.1rem;
}

.error {
  color: #d32f2f;
}

.back-btn {
  display: inline-block;
  padding: 8px 24px;
  background: #1976d2;
  color: #fff;
  border-radius: 6px;
  text-decoration: none;
  font-weight: bold;
  transition: background 0.2s;
  margin-top: 24px;
}
</style>