<template>
  <div class="article-page">
    <ArticleDetailItem />
  </div>
</template>

<script>
import ArticleDetailItem from '@/components/articles/ArticleDetailItem.vue';

export default {
  name: 'ArticlePage',
  components: {
    ArticleDetailItem
  }
}
</script>

<style scoped>
.article-page {
  padding: 20px;
}
</style>