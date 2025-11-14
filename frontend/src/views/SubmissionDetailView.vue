<template>
  <section v-if="submission" class="space-y-6">
    <div class="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
      <div>
        <h1 class="text-2xl font-semibold text-slate-100">
          Submission {{ submission.id.slice(0, 8) }}
        </h1>
        <div class="mt-2 flex flex-wrap items-center gap-3 text-sm text-slate-400">
          <span>Trạng thái hiện tại:</span>
          <VerdictBadge :verdict="submission.verdict" />
          <span v-if="isPolling" class="flex items-center gap-2 text-xs text-slate-500">
            <span class="h-2 w-2 animate-ping rounded-full bg-indigo-400"></span> Đang cập nhật...
          </span>
        </div>
      </div>
      <div class="flex gap-2">
        <RouterLink :to="`/problems/${submission.problemId}`" class="btn-ghost text-sm">Xem đề</RouterLink>
        <RouterLink to="/problems" class="btn-ghost text-sm">Danh sách bài toán</RouterLink>
      </div>
    </div>

    <div class="grid gap-6 lg:grid-cols-[1.6fr,1fr]">
      <div class="card space-y-4">
        <header class="flex items-center justify-between">
          <h2 class="text-lg font-semibold text-slate-100">Source code</h2>
          <span class="text-xs text-slate-500">{{ submission.code.length }} ký tự</span>
        </header>
        <pre class="code-box">
{{ submission.code }}
        </pre>
      </div>
      <div class="space-y-4">
        <div class="card space-y-3">
          <h2 class="text-sm font-semibold uppercase text-slate-400">Thông tin</h2>
          <ul class="space-y-2 text-sm text-slate-200">
            <li class="flex justify-between">
              <span class="text-slate-500">Ngôn ngữ</span>
              <span>{{ submission.language }}</span>
            </li>
            <li class="flex justify-between">
              <span class="text-slate-500">Điểm</span>
              <span>{{ submission.score }}</span>
            </li>
            <li class="flex justify-between">
              <span class="text-slate-500">Trạng thái</span>
              <VerdictBadge :verdict="submission.verdict" />
            </li>
          </ul>
        </div>
        <div class="card space-y-3">
          <h2 class="text-sm font-semibold uppercase text-slate-400">Kết quả testcase</h2>
          <ul class="space-y-2 text-sm">
            <li
              v-for="(result, index) in submission.testcaseResults"
              :key="result.testcaseId ?? index"
              class="flex items-center justify-between rounded-lg border border-slate-800 bg-slate-900/40 px-3 py-2"
            >
              <span class="font-mono text-xs text-slate-400">
                {{ result.testcaseId ? result.testcaseId.slice(0, 6) : 'Ẩn' }}
              </span>
              <VerdictBadge :verdict="result.verdict" />
            </li>
            <li v-if="!submission.testcaseResults.length" class="text-sm text-slate-500">
              Chưa có dữ liệu testcase. Submission đang được chấm?
            </li>
          </ul>
        </div>
      </div>
    </div>
  </section>
  <section v-else class="space-y-6">
    <div class="card space-y-4">
      <SkeletonBlock height="32px" width="50%" />
      <SkeletonBlock height="16px" width="70%" />
      <SkeletonBlock height="280px" />
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue';
import { RouterLink, useRoute } from 'vue-router';
import VerdictBadge from '@/components/VerdictBadge.vue';
import SkeletonBlock from '@/components/SkeletonBlock.vue';
import { useApi } from '@/composables/useApi';
import { usePolling } from '@/composables/usePolling';
import type { SubmissionDetail } from '@/types';

const route = useRoute();

const { data: submission, execute: fetchSubmission } = useApi<SubmissionDetail>({
  url: `/submissions/${route.params.id}`,
  defaultValue: null
});

const { start, stop, isActive } = usePolling(2000);

const isPolling = computed(() => isActive.value);

const shouldPoll = (verdict: SubmissionDetail['verdict']) =>
  verdict === 'QUEUED' || verdict === 'RUNNING' || verdict === 'INTERNAL_ERROR';

const refresh = async () => {
  const value = await fetchSubmission();
  if (!value) {
    stop();
    return;
  }
  if (!shouldPoll(value.verdict)) {
    stop();
  }
};

onMounted(async () => {
  await refresh();
  if (submission.value && shouldPoll(submission.value.verdict)) {
    start(refresh);
  }
});
</script>

<style scoped>
.code-box {
  @apply max-h-[460px] overflow-auto rounded-xl bg-slate-950/80 p-4 text-xs leading-relaxed text-slate-200;
}
</style>

