<template>
  <NuxtLayout name="empty">
    <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50">
      <UCard class="max-w-md w-full transform transition-all duration-300 hover:scale-102 bg-white/80 backdrop-blur-sm">
        <template #header>
          <div class="text-center space-y-2">
            <h2 class="text-3xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">欢迎登录</h2>
            <p class="text-gray-500 text-sm">请输入您的账号信息</p>
          </div>
        </template>

        <UForm :state="formData" class="space-y-6" @submit="handleLogin">
          <UFormGroup label="用户名">
            <UInput
              v-model="formData.username"
              name="username"
              icon="i-heroicons-user"
              placeholder="请输入用户名"
              required
              autocomplete="username"
              color="primary"
            />
          </UFormGroup>

          <UFormGroup label="密码">
            <UInput
              v-model="formData.password"
              name="password"
              type="password"
              icon="i-heroicons-lock-closed"
              placeholder="请输入密码"
              required
              autocomplete="current-password"
              color="primary"
            />
          </UFormGroup>

          <UAlert
            v-if="error"
            type="error"
            title="登录失败"
            :description="error"
            icon="i-heroicons-exclamation-circle"
            class="animate-bounce"
          />

          <UButton
            type="submit"
            color="primary"
            size="lg"
            block
            :loading="loading"
            icon="i-heroicons-arrow-right-on-rectangle"
          >
            登录
          </UButton>
        </UForm>
      </UCard>
    </div>
  </NuxtLayout>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useApi } from '~/composables/useApi'
import { useAuthStore } from '~/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const api = useApi()
const formData = ref({
  username: 'admin',
  password: '123456'
})
const error = ref('')
const loading = ref(false)

const handleLogin = async () => {
  try {
    loading.value = true
    error.value = ''
    const data = await api.post('/api/auth/login', formData.value)
    if (data && data.token && data.userInfo) {
      authStore.setAuth(data.token, data.userInfo)
      // 确保认证状态已更新
      authStore.initializeAuth()
      await router.push('/')
    } else {
      error.value = '服务器响应格式错误'
    }
  } catch (e) {
    error.value = e.data?.message || '请检查网络连接'
  } finally {
    loading.value = false
  }
}
</script>