import { onBeforeUnmount, ref } from 'vue';

export function usePolling(intervalMs = 2000) {
  const isActive = ref(false);
  let handle: number | null = null;

  const start = (callback: () => void) => {
    stop();
    isActive.value = true;
    handle = window.setInterval(() => {
      if (isActive.value) {
        callback();
      }
    }, intervalMs);
  };

  const stop = () => {
    if (handle !== null) {
      window.clearInterval(handle);
      handle = null;
    }
    isActive.value = false;
  };

  onBeforeUnmount(stop);

  return {
    start,
    stop,
    isActive
  };
}

