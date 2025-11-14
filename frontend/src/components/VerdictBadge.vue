<template>
  <span
    class="inline-flex items-center rounded-full px-3 py-1 text-xs font-semibold uppercase tracking-wide"
    :class="classes"
  >
    {{ label }}
  </span>
</template>

<script setup lang="ts">
import type { Submission } from '@/types';
import { computed } from 'vue';

const props = defineProps<{
  verdict: Submission['verdict'];
}>();

const classes = computed(() => {
  switch (props.verdict) {
    case 'ACCEPTED':
      return 'bg-emerald-900/60 text-emerald-200 border border-emerald-700';
    case 'WRONG_ANSWER':
      return 'bg-rose-900/60 text-rose-200 border border-rose-700';
    case 'TIME_LIMIT_EXCEEDED':
      return 'bg-amber-900/60 text-amber-200 border border-amber-700';
    case 'MEMORY_LIMIT_EXCEEDED':
      return 'bg-sky-900/60 text-sky-200 border border-sky-700';
    case 'RUNTIME_ERROR':
      return 'bg-purple-900/60 text-purple-200 border border-purple-700';
    case 'COMPILATION_ERROR':
      return 'bg-indigo-900/60 text-indigo-200 border border-indigo-700';
    case 'INTERNAL_ERROR':
      return 'bg-slate-900/60 text-slate-200 border border-slate-700';
  }
});

const label = computed(() => {
  switch (props.verdict) {
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
  }
});
</script>

