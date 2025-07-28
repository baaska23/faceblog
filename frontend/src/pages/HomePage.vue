<template>
  <div class="home-page">
    <div class="header">
      <h1>Personal Blog</h1>
      <button class="add-btn">+Add</button>
    </div>
    <ArticleItem />
  </div>
</template>

<script>
import ArticleItem from '@/components/articles/ArticleItem.vue';

export default {
  name: 'HomePage',
  components: {
    ArticleItem
  }
}
</script>

<style scoped>
.home-page {
  padding: 20px;
}

h1 {
  font-weight: bold;
  font-size: large;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 2px solid black;
  gap: 16px;
}

.add-btn {
  padding: 8px 24px;
  background: #1976d2;
  color: #fff;
  border-radius: 5px;
  font-weight: bold;
  padding: 5px;
  cursor: pointer;
}
</style>
