<template>
  <NuxtLayout name="empty">
    <div class="min-h-screen flex items-center justify-center bg-gradient-to-r from-blue-100 to-indigo-100">
      <div class="max-w-md w-full transform transition-all duration-300 hover:scale-105">
        <div class="bg-white rounded-2xl shadow-2xl p-8 space-y-6">
          <div class="text-center">
            <h2 class="text-3xl font-bold text-gray-900 mb-2">欢迎登录</h2>
            <p class="text-gray-600 text-sm">请输入您的账号信息</p>
          </div>

          <form class="space-y-6" @submit.prevent="handleLogin">
            <div class="space-y-4">
              <div class="relative">
                <label for="username" class="text-sm font-medium text-gray-700 block mb-1">用户名</label>
                <input id="username" v-model="formData.username" name="username" type="text" required
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition duration-200 bg-gray-50 hover:bg-white"
                  placeholder="请输入用户名">
              </div>

              <div class="relative">
                <label for="password" class="text-sm font-medium text-gray-700 block mb-1">密码</label>
                <input id="password" v-model="formData.password" name="password" type="password" required
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition duration-200 bg-gray-50 hover:bg-white"
                  placeholder="请输入密码">
              </div>
            </div>

            <div v-if="error" 
              class="p-3 rounded-lg bg-red-50 text-red-600 text-sm text-center animate-bounce">
              {{ error }}
            </div>

            <button type="submit"
              class="w-full py-3 px-4 border border-transparent rounded-lg text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-4 focus:ring-indigo-500 focus:ring-opacity-50 transition-all duration-300 transform hover:-translate-y-0.5 shadow-lg hover:shadow-xl font-medium text-sm">
              登录
            </button>
          </form>
        </div>
      </div>
    </div>
  </NuxtLayout>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { useAuthStore } from '~/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const formData = ref({
  username: '',
  password: ''
})
const error = ref('')

const handleLogin = async () => {
  try {
    error.value = ''
    const response = await axios.post('http://localhost:8080/api/auth/login', formData.value)
    const data = response.data
    if (data && data.token && data.userInfo) {
      authStore.setAuth(data.token, data.userInfo)
      // 确保认证状态已更新
      authStore.initializeAuth()
      await router.push('/')
    } else {
      error.value = '登录失败：服务器响应格式错误'
    }
  } catch (e) {
    error.value = e.response?.data?.message || '登录失败：请检查网络连接'
  }
}
</script>