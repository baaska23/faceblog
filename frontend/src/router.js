import { createRouter, createWebHistory } from 'vue-router';
import ArticlePage from '@/pages/ArticlePage.vue';
import HomePage from '@/pages/HomePage.vue';
import LoginPage from "@/pages/LoginPage.vue";
import { authService } from "@/services/AuthService.js";

const routes = [
  {
    path: '/',
    name: 'Login',
    component: LoginPage,
  },
  {
    path: '/home',
    name: 'Home',
    component: HomePage,
    meta: { requiresAuth: true }
  },
  {
    path: '/articles/:id',
    name: 'ArticleDetail',
    component: ArticlePage,
    props: true,
    meta: { requiresAuth: true }
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth && !authService.isAuthenticated()) {
    next('/');
  } else if (to.path === '/' && authService.isAuthenticated()) {
    next('/home');
  } else {
    next();
  }
});

export default router;