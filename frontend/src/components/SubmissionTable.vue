<template>
  <div class="overflow-hidden rounded-xl border border-slate-800">
    <table class="min-w-full divide-y divide-slate-800">
      <thead class="bg-slate-900/70 text-xs uppercase text-slate-400">
        <tr>
          <th class="px-4 py-3 text-left">Id</th>
          <th class="px-4 py-3 text-left">Trạng thái</th>
          <th class="px-4 py-3 text-left">Điểm</th>
          <th class="px-4 py-3 text-left">Thời gian</th>
          <th class="px-4 py-3 text-right"></th>
        </tr>
      </thead>
      <tbody class="divide-y divide-slate-900/60 bg-slate-950/40 text-sm text-slate-200">
        <tr v-for="submission in submissions" :key="submission.id">
          <td class="px-4 py-3 font-mono text-xs text-indigo-300">
            {{ submission.id.slice(0, 8) }}
          </td>
          <td class="px-4 py-3">
            <VerdictBadge :verdict="submission.verdict" />
          </td>
          <td class="px-4 py-3">{{ submission.score }}</td>
          <td class="px-4 py-3 text-slate-400">
            {{ new Date(submission.createdAt).toLocaleString() }}
          </td>
          <td class="px-4 py-3 text-right">
            <RouterLink :to="`/submissions/${submission.id}`" class="text-indigo-300 hover:text-indigo-200">
              Chi tiết
            </RouterLink>
          </td>
        </tr>
        <tr v-if="submissions.length === 0">
          <td colspan="5" class="px-4 py-6 text-center text-slate-500">Chưa có bài nộp nào</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import VerdictBadge from '@/components/VerdictBadge.vue';
import type { Submission } from '@/types';

defineProps<{
  submissions: Submission[];
}>();
</script>

