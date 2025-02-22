import { useAuthStore } from '~/stores/auth'

export const useApi = () => {
  const authStore = useAuthStore()

  const fetchWithAuth = <T>(url: string, options: any = {}) => {
    const headers = {
      ...options.headers,
      ...(authStore.token ? { Authorization: `Bearer ${authStore.token}` } : {})
    }

    return $fetch<T>(url, {
      ...options,
      headers,
      onResponseError: async (error) => {
        if (error.response?.status === 401) {
          authStore.clearAuth()
          await navigateTo('/login')
        }
        throw error
      }
    })
  }

  return {
    get: <T>(url: string, options = {}) => fetchWithAuth<T>(url, { ...options, method: 'GET' }),
    post: <T>(url: string, body: any, options = {}) => fetchWithAuth<T>(url, { ...options, method: 'POST', body }),
    put: <T>(url: string, body: any, options = {}) => fetchWithAuth<T>(url, { ...options, method: 'PUT', body }),
    delete: <T>(url: string, options = {}) => fetchWithAuth<T>(url, { ...options, method: 'DELETE' })
  }
} 