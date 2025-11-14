import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '@/views/HomeView.vue';
import ProblemsView from '@/views/ProblemsView.vue';
import ProblemDetailView from '@/views/ProblemDetailView.vue';
import SubmissionsView from '@/views/SubmissionsView.vue';
import SubmissionDetailView from '@/views/SubmissionDetailView.vue';
import AdminView from '@/views/AdminView.vue';
import AuthView from '@/views/AuthView.vue';
import { useAuthStore } from '@/stores/useAuthStore';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: AuthView,
      meta: { guestOnly: true, mode: 'login' }
    },
    {
      path: '/register',
      name: 'register',
      component: AuthView,
      meta: { guestOnly: true, mode: 'register' }
    },
    {
      path: '/problems',
      name: 'problems',
      component: ProblemsView
    },
    {
      path: '/problems/:slug',
      name: 'problem-detail',
      component: ProblemDetailView,
      props: true
    },
    {
      path: '/submissions',
      name: 'submissions',
      component: SubmissionsView,
      meta: { requiresAuth: true }
    },
    {
      path: '/submissions/:id',
      name: 'submission-detail',
      component: SubmissionDetailView,
      props: true,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminView,
      meta: { requiresAuth: true, roles: ['ADMIN'] }
    }
  ]
});

router.beforeEach(async (to) => {
  const auth = useAuthStore();
  if (!auth.user && auth.token) {
    await auth.initialize();
  }

  if (to.meta.requiresAuth && !auth.token) {
    return { name: 'login', query: { redirect: to.fullPath } };
  }

  if (to.meta.roles && auth.user) {
    const allowed = (to.meta.roles as string[]).some((role) => auth.user?.roles.includes(role));
    if (!allowed) {
      return { name: 'home' };
    }
  }

  if (to.meta.guestOnly && auth.token) {
    return { name: 'home' };
  }

  return true;
});

export default router;

