import { defineStore } from 'pinia'
import { ref } from 'vue'
import { useCookie } from '#app'

interface User {
  id: string
  username: string
  email?: string
}

// Cookie相关的配置
const TOKEN_COOKIE_NAME = 'auth_token'
const USER_COOKIE_NAME = 'auth_user'
const COOKIE_OPTIONS = {
  maxAge: 7 * 24 * 60 * 60, // 7天过期
  path: '/',
  secure: process.env.NODE_ENV === 'production',
  sameSite: 'strict' as const
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(null)
  const user = ref<User | null>(null)
  const isAuthenticated = ref(false)

  // 初始化状态
  const initializeAuth = () => {
    try {
      const tokenCookie = useCookie<string>(TOKEN_COOKIE_NAME)
      const userCookie = useCookie<string>(USER_COOKIE_NAME)

      if (tokenCookie.value) {
        token.value = tokenCookie.value
      }

      if (userCookie.value) {
        try {
          const parsedUser = JSON.parse(userCookie.value)
          if (parsedUser && typeof parsedUser === 'object' && parsedUser.username) {
            user.value = parsedUser
            isAuthenticated.value = true
          }
        } catch (e) {
          console.error('解析用户信息失败:', e)
          clearAuth()
        }
      }
    } catch (e) {
      console.error('初始化认证状态失败:', e)
      clearAuth()
    }
  }

  // 设置认证信息
  const setAuth = (newToken: string, newUser: User) => {
    try {
      token.value = newToken
      user.value = newUser
      isAuthenticated.value = true
      
      const tokenCookie = useCookie(TOKEN_COOKIE_NAME, COOKIE_OPTIONS)
      const userCookie = useCookie(USER_COOKIE_NAME, COOKIE_OPTIONS)
      
      tokenCookie.value = newToken
      userCookie.value = JSON.stringify({
        id: newUser.id,
        username: newUser.username,
        email: newUser.email
      })
    } catch (e) {
      console.error('设置认证信息失败:', e)
      clearAuth()
    }
  }

  // 清除认证信息
  const clearAuth = () => {
    try {
      token.value = null
      user.value = null
      isAuthenticated.value = false
      
      const tokenCookie = useCookie(TOKEN_COOKIE_NAME)
      const userCookie = useCookie(USER_COOKIE_NAME)
      
      tokenCookie.value = null
      userCookie.value = null
    } catch (e) {
      console.error('清除认证信息失败:', e)
    }
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