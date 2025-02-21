import { defineStore } from 'pinia'
import { ref } from 'vue'

interface User {
  username: string
  // 根据需要添加其他用户属性
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(null)
  const user = ref<User | null>(null)
  const isAuthenticated = ref(false)

  // 初始化状态
  const initializeAuth = () => {
    const storedToken = localStorage.getItem('token')
    const storedUser = localStorage.getItem('user')

    if (storedToken && storedUser) {
      try {
        const parsedUser = JSON.parse(storedUser)
        if (parsedUser && parsedUser.username) {
          token.value = storedToken
          user.value = parsedUser
          isAuthenticated.value = true
        } else {
          clearAuth()
        }
      } catch (e) {
        console.error('解析用户信息失败:', e)
        clearAuth()
      }
    }
  }

  // 设置认证信息
  const setAuth = (newToken: string, newUser: User) => {
    token.value = newToken
    user.value = newUser
    isAuthenticated.value = true
    localStorage.setItem('token', newToken)
    localStorage.setItem('user', JSON.stringify(newUser))
  }

  // 清除认证信息
  const clearAuth = () => {
    token.value = null
    user.value = null
    isAuthenticated.value = false
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  return {
    token,
    user,
    isAuthenticated,
    initializeAuth,
    setAuth,
    clearAuth
  }
})