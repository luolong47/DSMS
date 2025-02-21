import { useAuthStore } from '~/stores/auth'

export default defineNuxtRouteMiddleware((to, from) => {
  // 在服务器端运行时跳过验证
  if (process.server) return

  const authStore = useAuthStore()
  
  // 初始化认证状态
  authStore.initializeAuth()

  // 如果用户访问登录页
  if (to.path === '/login') {
    // 如果已经登录，重定向到首页
    if (authStore.isAuthenticated) {
      return navigateTo('/')
    }
    // 未登录则允许访问登录页
    return
  }

  // 对于其他页面，如果未登录，重定向到登录页
  if (!authStore.isAuthenticated) {
    return navigateTo('/login')
  }
})