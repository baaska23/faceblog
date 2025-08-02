<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-card">
        <div class="login-header">
          <div class="logo-section">
            <div class="logo-icon">📝</div>
            <h1 class="page-title">Welcome to FaceBlog</h1>
            <p class="subtitle">Share your thoughts with the world</p>
          </div>
        </div>

        <div class="login-body">
          <form @submit.prevent="submitForm" class="login-form">
            <div v-if="error" class="error-message">
              <span class="error-icon">⚠️</span>
              {{ error }}
            </div>

            <div class="form-group">
              <label for="username">Username</label>
              <input
                  id="username"
                  v-model="username"
                  type="text"
                  placeholder="Enter your username"
                  :disabled="loading"
                  required
              />
            </div>

            <div class="form-group">
              <label for="password">Password</label>
              <input
                  id="password"
                  v-model="password"
                  type="password"
                  placeholder="Enter your password"
                  :disabled="loading"
                  required
              />
            </div>

            <button type="submit" class="btn btn-primary login-btn" :disabled="loading">
              <span v-if="loading" class="btn-icon">⏳</span>
              <span v-else class="btn-icon">🚀</span>
              {{ loading ? 'Logging in...' : 'Login' }}
            </button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {useRouter} from "vue-router";
import {authService} from "@/services/AuthService.js";

export default {
  name: 'LoginPage',
  setup() {
    const router = useRouter();
    return {router};
  },
  data() {
    return {
      username: '',
      password: '',
      error: '',
      loading: false
    }
  },
  mounted() {
    if (authService.isAuthenticated()) {
      const user = authService.getCurrentUser();
      if (user) this.router.push('/home');
    }
  },
  methods: {
    async submitForm() {
      if (!this.username || !this.password) {
        this.error = 'Please fill in all fields';
        return;
      }

      this.loading = true;
      this.error = '';

      try {
        const response = await authService.login(this.username, this.password);
        sessionStorage.setItem('user', JSON.stringify(response.user));
        this.router.push('/home');
      } catch (error) {
        this.error = error.response?.data?.error || 'Login failed';
      } finally {
        this.loading = false;
      }
    }
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  padding: 2rem;
}

.login-container {
  width: 100%;
  max-width: 450px;
}

.login-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transform: translateY(0);
  animation: slideUp 0.6s ease-out;
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

.login-header {
  padding: 3rem 2rem 2rem;
  text-align: center;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.logo-icon {
  font-size: 3rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.3);
}

.page-title {
  margin: 0;
  font-size: 2rem;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.subtitle {
  margin: 0;
  color: #666;
  font-size: 1rem;
  opacity: 0.8;
}

.login-body {
  padding: 2rem;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  font-weight: 600;
  color: #333;
  font-size: 0.95rem;
}

.form-group input {
  padding: 0.875rem 1rem;
  border: 2px solid #e1e5e9;
  border-radius: 12px;
  font-size: 1rem;
  transition: all 0.3s ease;
  font-family: inherit;
  background: white;
}

.form-group input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  transform: translateY(-1px);
}

.form-group input:disabled {
  background: #f8f9fa;
  opacity: 0.7;
  cursor: not-allowed;
}

.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  padding: 0.875rem 1.5rem;
  border: none;
  border-radius: 12px;
  font-size: 1rem;
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

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.6);
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.2);
}

.login-btn {
  margin-top: 1rem;
  font-size: 1.1rem;
  padding: 1rem 1.5rem;
}

.btn-icon {
  font-size: 1.1rem;
  font-weight: bold;
}

.error-message {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #d32f2f;
  background: linear-gradient(135deg, #ffebee 0%, #ffcdd2 100%);
  padding: 1rem;
  border: 1px solid #ffcdd2;
  border-radius: 12px;
  font-size: 0.95rem;
  animation: shake 0.5s ease-in-out;
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-5px); }
  75% { transform: translateX(5px); }
}

.error-icon {
  font-size: 1.2rem;
}

/* Responsive Design */
@media (max-width: 768px) {
  .login-page {
    padding: 1rem;
  }

  .login-header {
    padding: 2rem 1.5rem 1.5rem;
  }

  .login-body {
    padding: 1.5rem;
  }

  .page-title {
    font-size: 1.75rem;
  }

  .logo-icon {
    width: 60px;
    height: 60px;
    font-size: 2rem;
  }
}

@media (max-width: 480px) {
  .login-page {
    padding: 0.5rem;
  }

  .page-title {
    font-size: 1.5rem;
  }

  .subtitle {
    font-size: 0.9rem;
  }
}
</style>