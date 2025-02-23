<template>
  <UContainer>
    <UCard class="min-h-screen bg-gradient-to-br from-gray-50 to-blue-50">
      <!-- 主要内容区域 -->
      <div class="py-8">
        <!-- 欢迎信息 -->
        <div class="mb-8">
          <h1 class="text-3xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent mb-2">
            欢迎回来，{{ user?.name || '用户' }}
          </h1>
          <p class="text-gray-600 flex items-center gap-2">
            <UIcon name="i-heroicons-calendar" />
            今天是 {{ currentDate }}，祝您工作愉快！
          </p>
        </div>

        <!-- 功能卡片网格 -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <!-- 产品管理卡片 -->
          <UCard
            @click="router.push('/products')"
            class="cursor-pointer transform transition-all duration-300 hover:scale-102 hover:shadow-lg"
          >
            <template #header>
              <div class="flex items-center justify-between">
                <div class="p-3 bg-blue-100 rounded-xl">
                  <UIcon name="i-heroicons-cube" class="text-2xl text-blue-600" />
                </div>
                <UIcon
                  name="i-heroicons-arrow-right"
                  class="text-blue-600 opacity-0 group-hover:opacity-100 transition-opacity"
                />
              </div>
            </template>
            <h3 class="text-xl font-semibold text-gray-900 mb-2">产品管理</h3>
            <p class="text-gray-600">管理和维护产品信息，查看产品详情</p>
          </UCard>

          <!-- 批次管理卡片 -->
          <UCard
            @click="router.push('/batches')"
            class="cursor-pointer transform transition-all duration-300 hover:scale-102 hover:shadow-lg"
          >
            <template #header>
              <div class="flex items-center justify-between">
                <div class="p-3 bg-purple-100 rounded-xl">
                  <UIcon name="i-heroicons-squares-2x2" class="text-2xl text-purple-600" />
                </div>
                <UIcon
                  name="i-heroicons-arrow-right"
                  class="text-purple-600 opacity-0 group-hover:opacity-100 transition-opacity"
                />
              </div>
            </template>
            <h3 class="text-xl font-semibold text-gray-900 mb-2">批次管理</h3>
            <p class="text-gray-600">管理产品批次，追踪生产进度</p>
          </UCard>

          <!-- 系统概览卡片 -->
          <UCard>
            <template #header>
              <div class="flex items-center justify-between">
                <div class="p-3 bg-green-100 rounded-xl">
                  <UIcon name="i-heroicons-chart-bar" class="text-2xl text-green-600" />
                </div>
              </div>
            </template>
            <h3 class="text-xl font-semibold text-gray-900 mb-2">系统概览</h3>
            <div class="space-y-2">
              <p class="text-gray-600 flex items-center gap-2">
                <UIcon name="i-heroicons-clock" />
                上次登录: {{ formatDate(user?.lastLogin) }}
              </p>
              <p class="text-gray-600 flex items-center gap-2">
                <UIcon name="i-heroicons-information-circle" />
                系统版本: v1.0.0
              </p>
            </div>
          </UCard>
        </div>
      </div>
    </UCard>
  </UContainer>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const user = ref(null)
const currentDate = new Date().toLocaleDateString('zh-CN', {
  year: 'numeric',
  month: 'long',
  day: 'numeric',
  weekday: 'long'
})

const formatDate = (date) => {
  if (!date) return '未知'
  return new Date(date).toLocaleString('zh-CN')
}

onMounted(() => {
  const authStore = useAuthStore()
  
  if (!authStore.isAuthenticated) {
    router.push('/login')
    return
  }
  
  user.value = authStore.user
})
</script>