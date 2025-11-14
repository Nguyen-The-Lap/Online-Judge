<template>
  <div v-if="auth.user" class="relative" @mouseleave="open = false">
    <button
      class="flex items-center gap-2 rounded-full bg-slate-900/80 px-3 py-1 text-sm transition hover:bg-slate-800/80"
      @click="toggle"
    >
      <span class="flex h-8 w-8 items-center justify-center rounded-full bg-indigo-600/40 text-sm font-semibold text-indigo-200">
        {{ initials }}
      </span>
      <div class="text-left leading-tight">
        <p class="text-sm font-semibold text-indigo-300">{{ auth.user.displayName }}</p>
        <p class="text-xs text-slate-500">{{ auth.user.username }}</p>
      </div>
      <svg class="h-4 w-4 text-slate-400" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" d="M19 9l-7 7-7-7" />
      </svg>
    </button>
    <div
      v-if="open"
      class="absolute right-0 z-20 mt-2 w-60 space-y-2 rounded-xl border border-slate-800 bg-slate-900/95 p-3 text-sm shadow-2xl backdrop-blur"
    >
      <div class="rounded-lg border border-slate-800 bg-slate-950/60 p-3">
        <p class="text-xs uppercase text-slate-500">Quyền hạn</p>
        <p class="mt-1 text-xs text-indigo-200">{{ roles }}</p>
      </div>
      <RouterLink
        to="/submissions"
        class="block rounded-lg px-3 py-2 text-slate-200 transition hover:bg-slate-800"
        @click="open = false"
      >
        Bài nộp của tôi
      </RouterLink>
      <RouterLink
        v-if="canManage"
        to="/admin"
        class="block rounded-lg px-3 py-2 text-slate-200 transition hover:bg-slate-800"
        @click="open = false"
      >
        Bảng điều khiển Admin
      </RouterLink>
      <button
        class="flex w-full items-center justify-between rounded-lg px-3 py-2 text-left text-rose-300 transition hover:bg-rose-500/10"
        @click="logout"
      >
        Đăng xuất
        <span class="text-xs text-rose-400">⌘K</span>
      </button>
    </div>
  </div>
  <div v-else class="flex gap-3">
    <RouterLink to="/login" class="btn-ghost text-sm">Đăng nhập</RouterLink>
    <RouterLink to="/register" class="btn-primary text-sm">Đăng ký</RouterLink>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue';
import { RouterLink } from 'vue-router';
import { useAuthStore } from '@/stores/useAuthStore';

const auth = useAuthStore();
const open = ref(false);

const initials = computed(() =>
  auth.user?.displayName
    .split(' ')
    .map((part) => part.charAt(0))
    .join('')
    .slice(0, 2)
    .toUpperCase() ?? 'BF'
);

const roles = computed(() => auth.user?.roles.join(', ') ?? 'USER');
const canManage = computed(() => auth.user?.roles.includes('ADMIN') || auth.user?.roles.includes('PROBLEM_SETTER'));

const onKey = (event: KeyboardEvent) => {
  if ((event.metaKey || event.ctrlKey) && event.key.toLowerCase() === 'k') {
    event.preventDefault();
    logout();
  }
};

onMounted(() => {
  window.addEventListener('keydown', onKey);
});

onUnmounted(() => {
  window.removeEventListener('keydown', onKey);
});

const toggle = () => {
  open.value = !open.value;
};

const logout = () => {
  auth.logout();
  open.value = false;
};
</script>

