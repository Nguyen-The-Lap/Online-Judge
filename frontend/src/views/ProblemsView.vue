<template>
  <section class="space-y-6">
    <div class="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
      <div>
        <h1 class="text-2xl font-semibold text-slate-100">Danh sách bài toán</h1>
        <p class="text-sm text-slate-400">Chọn bài toán phù hợp với trình độ của bạn</p>
      </div>
      <RouterLink v-if="canCreate" to="/admin/problems/new" class="btn-primary">Tạo bài toán</RouterLink>
    </div>

    <div class="grid gap-4 md:grid-cols-[2fr,1fr]">
      <div class="card space-y-4 md:col-span-1">
        <label class="block text-sm text-slate-300">
          Tìm kiếm
          <input
            v-model="keyword"
            type="text"
            placeholder="Tìm theo tiêu đề hoặc tag..."
            class="input mt-1"
          />
        </label>
        <div>
          <p class="text-xs uppercase text-slate-400">Độ khó</p>
          <div class="mt-3 flex flex-wrap gap-2">
            <button
              v-for="option in difficultyOptions"
              :key="option.value ?? 'ALL'"
              class="rounded-full border px-3 py-1 text-xs font-semibold transition"
              :class="
                selectedDifficulty === option.value
                  ? 'border-indigo-500 bg-indigo-600/30 text-indigo-200'
                  : 'border-slate-700 bg-slate-900/70 text-slate-300 hover:border-indigo-500 hover:text-indigo-200'
              "
              @click="selectedDifficulty = option.value"
            >
              {{ option.label }}
            </button>
          </div>
        </div>
      </div>

      <div class="card space-y-4">
        <h2 class="text-sm font-semibold uppercase text-slate-400">Thông tin nhanh</h2>
        <div class="grid gap-4 md:grid-cols-3">
          <div class="rounded-lg border border-slate-800 bg-slate-900/50 p-4 text-center">
            <p class="text-xs uppercase text-slate-500">Tổng số</p>
            <p class="mt-2 text-2xl font-semibold text-slate-100">{{ problems.value?.length ?? '—' }}</p>
          </div>
          <div class="rounded-lg border border-slate-800 bg-slate-900/50 p-4 text-center">
            <p class="text-xs uppercase text-slate-500">Độ khó trung bình</p>
            <p class="mt-2 text-lg font-semibold text-indigo-200">{{ difficultyDistribution.mean }}</p>
          </div>
          <div class="rounded-lg border border-slate-800 bg-slate-900/50 p-4 text-center">
            <p class="text-xs uppercase text-slate-500">Thẻ phổ biến</p>
            <p class="mt-2 text-sm text-slate-300">{{ topTags }}</p>
          </div>
        </div>
      </div>
    </div>

    <div class="grid gap-4 md:grid-cols-2">
      <template v-if="loading">
        <div v-for="i in 4" :key="i" class="card space-y-4">
          <SkeletonBlock height="20px" width="60%" />
          <SkeletonBlock height="16px" width="90%" />
          <SkeletonBlock height="16px" width="40%" />
        </div>
      </template>
      <template v-else>
        <ProblemCard v-for="problem in filteredProblems" :key="problem.id" :problem="problem" />
        <p v-if="filteredProblems.length === 0" class="text-sm text-slate-400">
          Không tìm thấy bài toán nào phù hợp.
        </p>
      </template>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import { RouterLink } from 'vue-router';
import ProblemCard from '@/components/ProblemCard.vue';
import SkeletonBlock from '@/components/SkeletonBlock.vue';
import { useAuthStore } from '@/stores/useAuthStore';
import type { ProblemSummary } from '@/types';
import { useApi } from '@/composables/useApi';

const auth = useAuthStore();
const keyword = ref('');
const selectedDifficulty = ref<ProblemSummary['difficulty'] | null>(null);

const { data: problems, loading } = useApi<ProblemSummary[]>({
  url: '/problems',
  defaultValue: [],
  transform: (items) => items?.sort((a: ProblemSummary, b: ProblemSummary) => (a.updatedAt < b.updatedAt ? 1 : -1))
});

const canCreate = computed(() => !!auth.user?.roles.some((role) => ['ADMIN', 'PROBLEM_SETTER'].includes(role)));

const difficultyOptions: Array<{ label: string; value: ProblemSummary['difficulty'] | null }> = [
  { label: 'Tất cả', value: null },
  { label: 'Dễ', value: 'EASY' },
  { label: 'Trung bình', value: 'MEDIUM' },
  { label: 'Khó', value: 'HARD' }
];

const filteredProblems = computed(() => {
  if (!problems.value) return [];
  return problems.value.filter((problem) => {
    const matchesDifficulty = selectedDifficulty.value ? problem.difficulty === selectedDifficulty.value : true;
    const normalizedKeyword = keyword.value.trim().toLowerCase();
    const matchesKeyword =
      normalizedKeyword.length === 0 ||
      problem.title.toLowerCase().includes(normalizedKeyword) ||
      (problem.tags ?? '').toLowerCase().includes(normalizedKeyword);
    return matchesDifficulty && matchesKeyword;
  });
});

const difficultyDistribution = computed(() => {
  if (!problems.value || problems.value.length === 0) {
    return { mean: '—' };
  }
  const weights = { EASY: 1, MEDIUM: 2, HARD: 3 } as const;
  const total = problems.value.reduce((sum, problem) => sum + weights[problem.difficulty], 0);
  const meanValue = total / problems.value.length;
  if (meanValue < 1.5) return { mean: 'Dễ' };
  if (meanValue < 2.5) return { mean: 'Trung bình' };
  return { mean: 'Khó' };
});

const topTags = computed(() => {
  if (!problems.value) return '—';
  const tagCounter = new Map<string, number>();
  problems.value.forEach((problem) => {
    problem.tags
      ?.split(',')
      .map((tag) => tag.trim())
      .filter(Boolean)
      .forEach((tag) => {
        tagCounter.set(tag, (tagCounter.get(tag) ?? 0) + 1);
      });
  });
  const [top] = [...tagCounter.entries()].sort((a, b) => b[1] - a[1]);
  return top ? `#${top[0]}` : '—';
});

watch(keyword, (value) => {
  if (value.length > 48) {
    keyword.value = value.slice(0, 48);
  }
});
</script>

<style scoped>
.input {
  @apply w-full rounded-lg border border-slate-700 bg-slate-900/60 px-3 py-2 text-sm text-slate-100 focus:border-indigo-500 focus:outline-none;
}
</style>

