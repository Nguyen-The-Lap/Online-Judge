<template>
  <section class="mx-auto max-w-xl space-y-8">
    <div class="text-center space-y-3">
      <h1 class="text-3xl font-semibold text-slate-100">{{ heading }}</h1>
      <p class="text-sm text-slate-400">{{ subtitle }}</p>
    </div>
    <form class="card space-y-5" @submit.prevent="submit">
      <div v-if="mode === 'register'" class="grid gap-4 md:grid-cols-2">
        <label class="block text-sm text-slate-300">
          Tên hiển thị
          <input v-model.trim="form.displayName" class="input" required maxlength="100" />
        </label>
        <label class="block text-sm text-slate-300">
          Username
          <input v-model.trim="form.username" class="input" required maxlength="50" />
        </label>
      </div>
      <label class="block text-sm text-slate-300">
        Email hoặc username
        <input v-model.trim="form.email" type="text" class="input" required />
      </label>
      <label class="block text-sm text-slate-300">
        <div class="flex items-center justify-between">
          <span>Mật khẩu</span>
          <button type="button" class="text-xs text-indigo-300 hover:text-indigo-200" @click="togglePassword">
            {{ showPassword ? 'Ẩn' : 'Hiện' }}
          </button>
        </div>
        <input
          v-model="form.password"
          :type="showPassword ? 'text' : 'password'"
          class="input"
          required
          minlength="6"
        />
      </label>
      <div v-if="mode === 'register'" class="flex items-center justify-between text-xs text-slate-500">
        <span>Độ mạnh mật khẩu:</span>
        <span :class="passwordStrengthClass">{{ passwordStrengthLabel }}</span>
      </div>
      <button class="btn-primary w-full" :disabled="auth.loading || !formValid">
        {{ auth.loading ? 'Đang xử lý...' : primaryLabel }}
      </button>
      <p v-if="error" class="rounded-lg border border-rose-700 bg-rose-900/40 px-3 py-2 text-sm text-rose-200">
        {{ error }}
      </p>
      <p class="text-center text-sm text-slate-400">
        <RouterLink v-if="mode === 'login'" to="/register" class="text-indigo-300 hover:text-indigo-200">
          Chưa có tài khoản? Đăng ký
        </RouterLink>
        <RouterLink v-else to="/login" class="text-indigo-300 hover:text-indigo-200">
          Đã có tài khoản? Đăng nhập
        </RouterLink>
      </p>
    </form>
  </section>
</template>

<script setup lang="ts">
import { computed, reactive, ref, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/useAuthStore';

const route = useRoute();
const router = useRouter();
const auth = useAuthStore();
const error = ref('');
const showPassword = ref(false);
const mode = ref<'login' | 'register'>('login');

const form = reactive({
  username: '',
  displayName: '',
  email: '',
  password: ''
});

watchEffect(() => {
  mode.value = (route.meta.mode as 'login' | 'register') ?? 'login';
  error.value = '';
});

const heading = computed(() => (mode.value === 'login' ? 'Đăng nhập ByteForge' : 'Tạo tài khoản mới'));
const subtitle = computed(() =>
  mode.value === 'login'
    ? 'Chào mừng trở lại, tiếp tục hành trình giải thuật nào!'
    : 'Tham gia cộng đồng ByteForge Judge ngay hôm nay.'
);
const primaryLabel = computed(() => (mode.value === 'login' ? 'Đăng nhập' : 'Đăng ký'));

const passwordStrength = computed(() => {
  const pwd = form.password;
  if (pwd.length < 6) return 0;
  let score = 0;
  if (/[A-Z]/.test(pwd)) score += 1;
  if (/[0-9]/.test(pwd)) score += 1;
  if (/[^A-Za-z0-9]/.test(pwd)) score += 1;
  if (pwd.length >= 10) score += 1;
  return score;
});

const passwordStrengthLabel = computed(() => {
  const score = passwordStrength.value;
  if (score >= 3) return 'Mạnh';
  if (score === 2) return 'Ổn';
  if (score === 1) return 'Yếu';
  return 'Rất yếu';
});

const passwordStrengthClass = computed(() => {
  switch (passwordStrength.value) {
    case 0:
    case 1:
      return 'text-rose-300';
    case 2:
      return 'text-amber-300';
    default:
      return 'text-emerald-300';
  }
});

const formValid = computed(() => {
  if (!form.email || !form.password) return false;
  if (mode.value === 'register') {
    return Boolean(form.displayName && form.username && form.password.length >= 6);
  }
  return true;
});

const submit = async () => {
  error.value = '';
  try {
    if (mode.value === 'login') {
      await auth.login({ usernameOrEmail: form.email, password: form.password });
    } else {
      await auth.register({
        username: form.username,
        displayName: form.displayName,
        email: form.email,
        password: form.password
      });
    }
    const redirect = (route.query.redirect as string) || '/';
    router.replace(redirect);
  } catch (err: any) {
    error.value = err?.response?.data?.message ?? 'Không thể xác thực.';
  }
};

const togglePassword = () => {
  showPassword.value = !showPassword.value;
};
</script>

<style scoped>
.input {
  @apply mt-1 w-full rounded-lg border border-slate-700 bg-slate-900/70 px-3 py-2 text-sm text-slate-100 focus:border-indigo-500 focus:outline-none;
}
</style>

