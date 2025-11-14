<template>
  <section class="space-y-8">
    <div>
      <h1 class="text-2xl font-semibold text-slate-100">Bảng điều khiển quản trị</h1>
      <p class="text-sm text-slate-400">Theo dõi trạng thái hệ thống và quản lý bài toán</p>
    </div>
    <div class="grid gap-6 md:grid-cols-3">
      <div class="card space-y-2">
        <p class="text-xs uppercase text-slate-500">Người dùng</p>
        <p class="text-3xl font-bold text-slate-100">{{ stats?.users ?? '—' }}</p>
      </div>
      <div class="card space-y-2">
        <p class="text-xs uppercase text-slate-500">Bài toán</p>
        <p class="text-3xl font-bold text-slate-100">{{ stats?.problems ?? '—' }}</p>
      </div>
      <div class="card space-y-2">
        <p class="text-xs uppercase text-slate-500">Bài nộp</p>
        <p class="text-3xl font-bold text-slate-100">{{ stats?.submissions ?? '—' }}</p>
      </div>
    </div>
    <div class="card space-y-4">
      <h2 class="text-lg font-semibold text-slate-100">Tạo bài toán nhanh</h2>
      <label class="block">
        <span class="text-xs uppercase text-slate-400">Slug</span>
        <input v-model="problem.slug" class="input" />
      </label>
      <label class="block">
        <span class="text-xs uppercase text-slate-400">Tiêu đề</span>
        <input v-model="problem.title" class="input" />
      </label>
      <label class="block">
        <span class="text-xs uppercase text-slate-400">Độ khó</span>
        <select v-model="problem.difficulty" class="input">
          <option value="EASY">Dễ</option>
          <option value="MEDIUM">Trung bình</option>
          <option value="HARD">Khó</option>
        </select>
      </label>
      <label class="block">
        <span class="text-xs uppercase text-slate-400">Đề bài</span>
        <textarea v-model="problem.statement" rows="6" class="input"></textarea>
      </label>
      <button class="btn-primary" :disabled="creating" @click="createProblem">
        {{ creating ? 'Đang tạo...' : 'Tạo bài toán' }}
      </button>
      <p v-if="message" class="text-sm text-emerald-300">{{ message }}</p>
    </div>
  </section>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import http from '@/lib/http';

const stats = ref<{ users: number; problems: number; submissions: number } | null>(null);
const creating = ref(false);
const message = ref('');

const problem = reactive({
  slug: '',
  title: '',
  difficulty: 'EASY',
  statement: ''
});

const loadStats = async () => {
  const { data } = await http.get('/admin/stats');
  stats.value = data;
};

const createProblem = async () => {
  creating.value = true;
  message.value = '';
  try {
    await http.post('/problems', {
      ...problem,
      tags: '',
      inputSpecification: '',
      outputSpecification: '',
      sampleInput: '',
      sampleOutput: '',
      testcases: []
    });
    message.value = 'Đã tạo bài toán';
    await loadStats();
  } catch (error: any) {
    message.value = error?.response?.data?.message ?? 'Không thể tạo bài toán';
  } finally {
    creating.value = false;
  }
};

onMounted(loadStats);
</script>

<style scoped>
.input {
  @apply mt-1 w-full rounded-lg border border-slate-700 bg-slate-900/70 p-2 text-sm text-slate-100 focus:border-indigo-500 focus:outline-none;
}
</style>

