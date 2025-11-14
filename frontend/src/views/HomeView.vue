<template>
  <section class="space-y-10">
    <div class="grid gap-12 lg:grid-cols-[1.3fr,1fr]">
      <div class="space-y-6">
        <h1 class="text-4xl font-bold leading-tight text-indigo-300">
          ByteForge Judge
        </h1>
        <p class="text-lg text-slate-300">
          Nền tảng chấm bài trực tuyến hướng tới trải nghiệm hiện đại, realtime dashboard và công cụ
          tiện ích cho cả thí sinh lẫn ban ra đề.
        </p>
        <div class="flex flex-wrap gap-4">
          <RouterLink to="/problems" class="btn-primary">Bắt đầu giải</RouterLink>
          <RouterLink to="/register" class="btn-ghost">Tạo tài khoản</RouterLink>
          <RouterLink to="/admin" class="btn-ghost">Bảng điều khiển</RouterLink>
        </div>
        <div class="grid gap-4 md:grid-cols-2">
          <div class="card space-y-2">
            <p class="text-xs uppercase text-indigo-300">Tốc độ chấm</p>
            <p class="text-2xl font-semibold text-slate-100">Asynchronous judge queue</p>
            <p class="text-sm text-slate-400">Kết quả cập nhật liên tục với auto refresh trên bảng bài nộp.</p>
          </div>
          <div class="card space-y-2">
            <p class="text-xs uppercase text-indigo-300">Trải nghiệm người dùng</p>
            <p class="text-2xl font-semibold text-slate-100">UI tối giản</p>
            <p class="text-sm text-slate-400">Tailwind + component tùy biến cho workflow competitive coding.</p>
          </div>
        </div>
      </div>
      <div class="card space-y-5">
        <h2 class="text-lg font-semibold text-slate-200">Trạng thái hệ thống</h2>
        <div v-if="loading" class="space-y-3">
          <SkeletonBlock height="22px" width="40%" />
          <SkeletonBlock height="18px" width="70%" />
          <SkeletonBlock height="18px" width="50%" />
        </div>
        <div v-else class="grid gap-4 text-center sm:grid-cols-3">
          <div class="rounded-lg bg-slate-950/60 p-4 shadow-sm">
            <p class="text-sm text-slate-400">Bài toán</p>
            <p class="mt-2 text-3xl font-semibold text-slate-100">{{ stats?.problems ?? '—' }}</p>
          </div>
          <div class="rounded-lg bg-slate-950/60 p-4 shadow-sm">
            <p class="text-sm text-slate-400">Người dùng</p>
            <p class="mt-2 text-3xl font-semibold text-slate-100">{{ stats?.users ?? '—' }}</p>
          </div>
          <div class="rounded-lg bg-slate-950/60 p-4 shadow-sm">
            <p class="text-sm text-slate-400">Bài nộp</p>
            <p class="mt-2 text-3xl font-semibold text-slate-100">{{ stats?.submissions ?? '—' }}</p>
          </div>
        </div>
        <div class="space-y-3">
          <h3 class="text-sm font-semibold text-slate-200">Highlights</h3>
          <ul class="space-y-2 text-sm text-slate-300">
            <li v-for="problem in highlightProblems" :key="problem.id" class="flex items-center justify-between">
              <RouterLink :to="`/problems/${problem.slug}`" class="text-indigo-400 hover:text-indigo-200">
                {{ problem.title }}
              </RouterLink>
              <DifficultyBadge :difficulty="problem.difficulty" />
            </li>
          </ul>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { RouterLink } from 'vue-router';
import SkeletonBlock from '@/components/SkeletonBlock.vue';
import DifficultyBadge from '@/components/DifficultyBadge.vue';
import { useApi } from '@/composables/useApi';
import type { ProblemSummary } from '@/types';

const { data: stats, loading } = useApi<{ problems: number; users: number; submissions: number }>({
  url: '/system/stats',
  defaultValue: null
});

const { data: problemList } = useApi<ProblemSummary[]>({
  url: '/problems',
  defaultValue: [],
  transform: (items) => items?.slice(0, 3)
});

const highlightProblems = computed(() => problemList.value ?? []);
</script>

