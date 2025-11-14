<template>
  <section v-if="problem" class="space-y-8">
    <div class="flex flex-col justify-between gap-4 lg:flex-row">
      <div>
        <h1 class="text-3xl font-semibold text-slate-100">{{ problem.title }}</h1>
        <p class="text-sm text-slate-400">
          Thời gian: {{ problem.timeLimitMs }} ms • Bộ nhớ: {{ problem.memoryLimitMb }} MB
        </p>
        <p v-if="problem.tags" class="mt-2 text-xs uppercase tracking-wide text-indigo-300">
          {{ formattedTags }}
        </p>
      </div>
      <DifficultyBadge :difficulty="problem.difficulty" />
    </div>

    <div class="grid gap-8 lg:grid-cols-[2fr,1fr]">
      <article class="prose prose-invert max-w-none space-y-8">
        <section>
          <h2>Đề bài</h2>
          <p class="body-text">{{ problem.statement }}</p>
        </section>
        <section v-if="problem.inputSpecification">
          <h3>Input</h3>
          <p class="body-text">{{ problem.inputSpecification }}</p>
        </section>
        <section v-if="problem.outputSpecification">
          <h3>Output</h3>
          <p class="body-text">{{ problem.outputSpecification }}</p>
        </section>
        <section class="grid gap-4 md:grid-cols-2">
          <div v-if="problem.sampleInput">
            <h3>Sample Input</h3>
            <pre class="sample-box">{{ problem.sampleInput }}</pre>
          </div>
          <div v-if="problem.sampleOutput">
            <h3>Sample Output</h3>
            <pre class="sample-box">{{ problem.sampleOutput }}</pre>
          </div>
        </section>
      </article>

      <aside class="space-y-6">
        <div class="card space-y-4">
          <header class="flex items-center justify-between">
            <h2 class="text-lg font-semibold text-slate-100">Gửi bài</h2>
            <span class="text-xs text-slate-500">{{ languages?.length ?? 0 }} ngôn ngữ</span>
          </header>
          <div v-if="!isAuthenticated" class="rounded-xl border border-slate-800 bg-slate-900/60 p-4 text-sm">
            <p class="text-indigo-200">Đăng nhập để nộp bài và theo dõi kết quả!</p>
            <RouterLink to="/login" class="mt-2 block text-indigo-400 hover:text-indigo-300">Đăng nhập ngay</RouterLink>
          </div>
          <template v-else>
            <label class="block">
              <span class="text-xs uppercase text-slate-400">Ngôn ngữ</span>
              <select v-model="form.languageId" class="input mt-1">
                <option v-for="language in languages" :key="language.id" :value="language.id">
                  {{ language.name }} {{ language.version }}
                </option>
              </select>
            </label>
            <label class="block">
              <div class="flex items-center justify-between">
                <span class="text-xs uppercase text-slate-400">Source code</span>
                <span class="text-xs text-slate-500">{{ form.sourceCode.length }} ký tự</span>
              </div>
              <textarea
                v-model="form.sourceCode"
                rows="16"
                class="mt-1 w-full rounded-xl border border-slate-700 bg-slate-950/80 p-3 font-mono text-sm text-slate-100 transition focus:border-indigo-500 focus:outline-none"
                placeholder="Viết lời giải của bạn tại đây..."
              ></textarea>
            </label>
            <button class="btn-primary w-full" :disabled="submitting || !formValid" @click="submit">
              {{ submitting ? 'Đang gửi...' : 'Nộp bài' }}
            </button>
            <p v-if="error" class="text-sm text-rose-300">{{ error }}</p>
            <RouterLink
              v-if="submissionId"
              :to="`/submissions/${submissionId}`"
              class="block text-center text-sm text-indigo-300 hover:text-indigo-200"
            >
              Xem kết quả mới nhất
            </RouterLink>
          </template>
        </div>
        <div class="card space-y-4">
          <h2 class="text-sm font-semibold uppercase text-slate-400">Testcase hiển thị</h2>
          <ul v-if="problem.testcases.length" class="space-y-3 text-sm text-slate-300">
            <li
              v-for="(testcase, index) in problem.testcases"
              :key="testcase.id ?? index"
              class="flex items-center justify-between rounded-lg border border-slate-800 bg-slate-900/50 px-3 py-2"
            >
              <span class="text-xs text-slate-400">
                Test {{ testcase.id ? testcase.id.slice(0, 6) : index + 1 }}
              </span>
              <span class="font-medium text-slate-100">{{ testcase.score }} điểm</span>
            </li>
          </ul>
          <p v-else class="text-sm text-slate-500">Bài toán không có testcase hiển thị.</p>
        </div>
      </aside>
    </div>
  </section>
  <section v-else class="space-y-6">
    <div class="card space-y-4">
      <SkeletonBlock height="32px" width="70%" />
      <SkeletonBlock height="16px" width="50%" />
      <SkeletonBlock height="300px" />
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue';
import { RouterLink, useRoute, useRouter } from 'vue-router';
import DifficultyBadge from '@/components/DifficultyBadge.vue';
import SkeletonBlock from '@/components/SkeletonBlock.vue';
import { useApi } from '@/composables/useApi';
import { useAuthStore } from '@/stores/useAuthStore';
import type { ProblemDetail, ProgrammingLanguage } from '@/types';
import http from '@/lib/http';

const route = useRoute();
const router = useRouter();
const auth = useAuthStore();
const isAuthenticated = computed(() => Boolean(auth.token));

const { data: problem, execute: reloadProblem } = useApi<ProblemDetail>({
  url: `/problems/${route.params.slug}`,
  defaultValue: null
});

watch(
  () => route.params.slug,
  (slug) => {
    if (slug) {
      reloadProblem(undefined, `/problems/${slug}`);
      submissionId.value = null;
    }
  }
);

const { data: languages } = useApi<ProgrammingLanguage[]>({
  url: '/languages',
  defaultValue: []
});

const submissionId = ref<string | null>(null);
const submitting = ref(false);
const error = ref<string | null>(null);

const form = reactive({
  languageId: '',
  sourceCode: ''
});

watch(
  languages,
  (langs) => {
    if (langs && langs.length > 0 && !form.languageId) {
      form.languageId = langs[0].id;
    }
  },
  { immediate: true }
);

const formValid = computed(() => form.sourceCode.trim().length > 0 && form.languageId.length > 0);

const formattedTags = computed(() =>
  (problem.value?.tags ?? '')
    .split(',')
    .map((tag) => `#${tag.trim()}`)
    .join(' ')
);

const submit = async () => {
  if (!problem.value || !formValid.value) return;
  submitting.value = true;
  error.value = null;
  try {
    const { data } = await http.post('/submissions', {
      problemId: problem.value.id,
      languageId: form.languageId,
      sourceCode: form.sourceCode
    });
    submissionId.value = data.id;
    form.sourceCode = '';
    router.push(`/submissions/${data.id}`);
  } catch (err: any) {
    error.value = err?.response?.data?.message ?? 'Không thể nộp bài, hãy thử lại!';
  } finally {
    submitting.value = false;
  }
};
</script>

<style scoped>
.body-text {
  @apply whitespace-pre-wrap rounded-xl bg-slate-900/60 p-4 text-sm leading-relaxed text-slate-100 shadow-inner;
}

.sample-box {
  @apply rounded-xl bg-slate-950/80 p-4 text-sm text-slate-100;
}

.input {
  @apply w-full rounded-xl border border-slate-700 bg-slate-900/80 px-3 py-2 text-sm text-slate-100 focus:border-indigo-500 focus:outline-none;
}
</style>
