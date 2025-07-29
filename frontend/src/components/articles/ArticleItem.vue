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
          <td>{{ new Date(article.published_at).toLocaleDateString() }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import { articleService } from '@/services/ArticleService';

export default {
  name: 'ArticleItem',
  data() {
    return {
      articles: []
    }
  },

  async mounted() {
    await this.fetchArticles();
  },

  methods: {
    async fetchArticles() {
      try {
        this.articles = await articleService.getArticles();
      } catch(error) {
        console.error('Error fetching articles:', error);
      }
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
</style>