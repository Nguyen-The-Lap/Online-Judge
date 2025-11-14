<template>
  <RouterLink :to="`/problems/${problem.slug}`" class="card block transition hover:border-indigo-500 hover:shadow-lg">
    <div class="flex items-start justify-between gap-4">
      <div>
        <h2 class="text-lg font-semibold text-slate-200">{{ problem.title }}</h2>
        <p class="mt-2 text-xs uppercase tracking-wide text-slate-500">
          Cập nhật {{ new Date(problem.updatedAt).toLocaleString() }}
        </p>
      </div>
      <DifficultyBadge :difficulty="problem.difficulty" />
    </div>
    <p class="mt-3 text-sm text-slate-400">
      {{ problem.tags || 'Chưa gắn thẻ' }}
    </p>
  </RouterLink>
</template>

<script setup lang="ts">
import type { ProblemSummary } from '@/types';
import DifficultyBadge from '@/components/DifficultyBadge.vue';

defineProps<{
  problem: ProblemSummary;
}>();
</script>

