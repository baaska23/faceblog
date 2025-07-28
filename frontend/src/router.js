// router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import ArticlePage from '@/pages/ArticlePage.vue';
import HomePage from '@/pages/HomePage.vue';

const routes = [
  { 
    path: '/', 
    name: 'Home',
    component: HomePage 
  },
  { 
    path: '/articles/:id', 
    name: 'ArticleDetail',
    component: ArticlePage,
    props: true
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;