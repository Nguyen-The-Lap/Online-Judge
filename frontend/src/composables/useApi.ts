import { ref } from 'vue';
import http from '@/lib/http';

type HttpMethod = 'get' | 'post' | 'put' | 'patch' | 'delete';

interface UseApiOptions<TData, TBody> {
  immediate?: boolean;
  defaultValue?: TData;
  method?: HttpMethod;
  url: string;
  body?: TBody;
  transform?: (data: any) => TData;
}

export function useApi<TData = unknown, TBody = unknown>(options: UseApiOptions<TData, TBody>) {
  const data = ref<TData | null>(options.defaultValue ?? null);
  const loading = ref(Boolean(options.immediate ?? true));
  const error = ref<string | null>(null);

  const execute = async (payload?: Partial<TBody>, overrideUrl?: string) => {
    loading.value = true;
    error.value = null;
    try {
      const method = options.method ?? 'get';
      const requestBody = { ...options.body, ...payload } as TBody;
      const url = overrideUrl ?? options.url;
      const response =
        method === 'get' || method === 'delete'
          ? await http[method](url, { params: requestBody })
          : await http[method](url, requestBody);
      const responseData = options.transform ? options.transform(response.data) : response.data;
      data.value = responseData;
      return responseData;
    } catch (err: any) {
      error.value = err?.response?.data?.message ?? err.message ?? 'Đã xảy ra lỗi';
      throw err;
    } finally {
      loading.value = false;
    }
  };

  if (options.immediate ?? true) {
    // eslint-disable-next-line @typescript-eslint/no-floating-promises
    execute();
  }

  return {
    data,
    loading,
    error,
    execute
  };
}

