<template>
  <section class="space-y-6">
    <div class="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
      <div>
        <h1 class="text-2xl font-semibold text-slate-100">Bài nộp của tôi</h1>
        <p class="text-sm text-slate-400">Theo dõi tiến độ và trạng thái mới nhất</p>
      </div>
      <div class="flex gap-2">
        <select v-model="selectedVerdict" class="input text-sm">
          <option value="">Tất cả trạng thái</option>
          <option v-for="verdict in verdictOptions" :key="verdict" :value="verdict">{{ verdictLabel(verdict) }}</option>
        </select>
        <button class="btn-ghost text-sm" @click="refresh" :disabled="loading">Làm mới</button>
      </div>
    </div>

    <div v-if="loading" class="grid gap-3">
      <div v-for="i in 5" :key="i" class="card space-y-3">
        <SkeletonBlock height="18px" width="40%" />
        <SkeletonBlock height="16px" width="60%" />
      </div>
    </div>

    <div v-else>
      <SubmissionTable :submissions="filteredSubmissions" />
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import SubmissionTable from '@/components/SubmissionTable.vue';
import SkeletonBlock from '@/components/SkeletonBlock.vue';
import type { Submission } from '@/types';
import { useApi } from '@/composables/useApi';
import { usePolling } from '@/composables/usePolling';

const selectedVerdict = ref('');

const { data: submissions, loading, execute } = useApi<Submission[]>({
  url: '/submissions',
  defaultValue: [],
  immediate: false
});

const { start, stop } = usePolling(4000);

const verdictOptions: Submission['verdict'][] = [
  'ACCEPTED',
  'WRONG_ANSWER',
  'TIME_LIMIT_EXCEEDED',
  'MEMORY_LIMIT_EXCEEDED',
  'RUNTIME_ERROR',
  'COMPILATION_ERROR',
  'INTERNAL_ERROR',
  'QUEUED',
  'RUNNING'
];

const verdictLabel = (verdict: Submission['verdict']) => {
  switch (verdict) {
    case 'ACCEPTED':
      return 'Accepted';
    case 'WRONG_ANSWER':
      return 'Wrong Answer';
    case 'TIME_LIMIT_EXCEEDED':
      return 'Time Limit';
    case 'MEMORY_LIMIT_EXCEEDED':
      return 'Memory Limit';
    case 'RUNTIME_ERROR':
      return 'Runtime Error';
    case 'COMPILATION_ERROR':
      return 'Compilation Error';
    case 'INTERNAL_ERROR':
      return 'Internal Error';
    case 'QUEUED':
      return 'Queued';
    case 'RUNNING':
      return 'Running';
  }
};

const refresh = async () => {
  const result = await execute();
  if (result?.some((item) => item.status === 'QUEUED' || item.status === 'RUNNING')) {
    start(refresh);
  } else {
    stop();
  }
};

const filteredSubmissions = computed(() => {
  if (!submissions.value) return [];
  if (!selectedVerdict.value) {
    return submissions.value;
  }
  return submissions.value.filter((submission) => submission.verdict === selectedVerdict.value);
});

refresh();
</script>

<style scoped>
.input {
  @apply rounded-lg border border-slate-700 bg-slate-900/60 px-3 py-2 text-slate-100 focus:border-indigo-500 focus:outline-none;
}
</style>

