import { defineStore } from 'pinia';
import http, { setHttpAuthToken } from '@/lib/http';
import type { User } from '@/types';

interface AuthState {
  token: string | null;
  user: User | null;
  loading: boolean;
}

const TOKEN_KEY = 'byteforge_token';

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    token: localStorage.getItem(TOKEN_KEY),
    user: null,
    loading: false
  }),
  actions: {
    async initialize() {
      if (this.token) {
        setHttpAuthToken(this.token);
        await this.fetchProfile();
      }
    },
    async login(payload: { usernameOrEmail: string; password: string }) {
      this.loading = true;
      try {
        const { data } = await http.post('/auth/login', payload);
        this.applyAuthResponse(data.token, data.user);
      } finally {
        this.loading = false;
      }
    },
    async register(payload: { username: string; email: string; password: string; displayName: string }) {
      this.loading = true;
      try {
        const { data } = await http.post('/auth/register', payload);
        this.applyAuthResponse(data.token, data.user);
      } finally {
        this.loading = false;
      }
    },
    async fetchProfile() {
      try {
        const { data } = await http.get('/me');
        this.user = data;
      } catch (error) {
        this.logout();
        throw error;
      }
    },
    logout() {
      this.token = null;
      this.user = null;
      localStorage.removeItem(TOKEN_KEY);
      setHttpAuthToken(undefined);
    },
    applyAuthResponse(token: string, user: User) {
      this.token = token;
      this.user = user;
      localStorage.setItem(TOKEN_KEY, token);
      setHttpAuthToken(token);
    }
  }
});

