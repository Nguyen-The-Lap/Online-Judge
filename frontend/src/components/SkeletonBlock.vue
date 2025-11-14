<template>
  <div
    class="animate-pulse rounded-lg bg-slate-800/60"
    :style="{
      height: height,
      width: width,
      minHeight: height
    }"
  />
</template>

<script setup lang="ts">
defineProps<{
  height: string;
  width?: string;
}>();
</script>

