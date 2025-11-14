<template>
  <span class="inline-flex items-center gap-2 rounded-full px-3 py-1 text-xs font-semibold uppercase" :class="classes">
    <slot />
    {{ label }}
  </span>
</template>

<script setup lang="ts">
import type { ProblemSummary } from '@/types';
import { computed } from 'vue';

const props = defineProps<{
  difficulty: ProblemSummary['difficulty'];
}>();

const label = computed(() => {
  switch (props.difficulty) {
    case 'EASY':
      return 'Dễ';
    case 'MEDIUM':
      return 'Trung bình';
    case 'HARD':
      return 'Khó';
  }
});

const classes = computed(() => {
  switch (props.difficulty) {
    case 'EASY':
      return 'border border-emerald-700 bg-emerald-900/50 text-emerald-200';
    case 'MEDIUM':
      return 'border border-amber-700 bg-amber-900/50 text-amber-200';
    case 'HARD':
      return 'border border-rose-700 bg-rose-900/50 text-rose-200';
  }
});
</script>

