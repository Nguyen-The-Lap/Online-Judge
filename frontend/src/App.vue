<template>
  <div class="min-h-screen bg-slate-950 text-slate-100">
    <header class="border-b border-slate-800 bg-slate-950/80 backdrop-blur">
      <div class="mx-auto flex max-w-6xl items-center justify-between px-6 py-4">
        <RouterLink to="/" class="text-xl font-semibold text-indigo-400">
          ByteForge Judge
        </RouterLink>
        <nav class="flex items-center gap-4 text-sm">
          <RouterLink to="/problems" class="hover:text-indigo-300">Danh sách bài toán</RouterLink>
          <RouterLink v-if="isAuthenticated" to="/submissions" class="hover:text-indigo-300">Bài nộp</RouterLink>
          <RouterLink v-if="canManage" to="/admin" class="hover:text-indigo-300">Quản trị</RouterLink>
          <UserMenu />
        </nav>
      </div>
    </header>
    <main class="mx-auto max-w-6xl px-6 py-8">
      <RouterView />
    </main>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { RouterLink, RouterView } from 'vue-router';
import UserMenu from '@/components/UserMenu.vue';
import { useAuthStore } from '@/stores/useAuthStore';

const auth = useAuthStore();
const isAuthenticated = computed(() => Boolean(auth.token));
const canManage = computed(() => auth.user?.roles.includes('ADMIN') || auth.user?.roles.includes('PROBLEM_SETTER'));
</script>

