<template>
  <div class="home-page">
    <div class="header">
      <h1>Personal Blog</h1>
      <button @click="openForm()" class="add-btn">+Add</button>
    </div>
    <ArticleItem />
    <div v-if="showForm" class="modal-overlay" @click.self="closeForm">
      <div class="modal">
        <form @submit.prevent="publishArticle">
          <p>Add article</p>
          <label>Title</label>
          <input v-model="title"/>
          <label>Content</label>
          <input v-model="content"/>
          <button type="submit">Publish</button>
          <button type="button" @click="closeForm">Cancel</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import ArticleItem from '@/components/articles/ArticleItem.vue';
import {articleService} from "@/services/ArticleService.js";
import {format} from "date-fns";
export default {
  name: 'HomePage',
  data() {
    return {
      showForm: false,
      title: '',
      content: ''
    }
  },
  components: {
    ArticleItem
  },
  methods: {
    openForm() {
      this.showForm = true;
    },

    closeForm() {
      this.showForm = false;
      this.title = '';
      this.content = '';
    },

    async publishArticle() {
      await articleService.addArticle({
        title: this.title,
        content: this.content,
        published_at: new Date().toISOString()
      })
      this.closeForm();
    }
  }



}
</script>
<style scoped>
.home-page {
  padding: 20px;
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
