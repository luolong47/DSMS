<template>
  <div class="min-h-screen bg-gray-50 flex">
    <!-- 左侧菜单栏 -->
    <div class="w-64 bg-white shadow-md">
      <div class="p-4">
        <h1 class="text-xl font-bold text-gray-900">数据结构管理系统</h1>
      </div>
      <nav class="mt-4">
        <NuxtLink
          to="/products"
          class="flex items-center px-4 py-2 text-gray-700 hover:bg-gray-100"
        >
          <span class="ml-2">产品管理</span>
        </NuxtLink>
        <NuxtLink
          to="/batches"
          class="flex items-center px-4 py-2 text-gray-700 hover:bg-gray-100"
        >
          <span class="ml-2">批次管理</span>
        </NuxtLink>
      </nav>
    </div>

    <!-- 主要内容区域 -->
    <div class="flex-1">
      <!-- 顶部导航栏 -->
      <nav class="bg-white shadow-md">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="flex justify-end h-16">
            <div class="flex items-center space-x-4">
              <span class="text-gray-700">欢迎，{{ authStore.user?.username }}</span>
              <button
                @click="handleLogout"
                class="px-4 py-2 text-sm font-medium text-white bg-indigo-600 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                退出登录
              </button>
            </div>
          </div>
        </div>
      </nav>

      <!-- 页面内容 -->
      <main class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <slot />
      </main>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '~/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const handleLogout = () => {
  authStore.clearAuth()
  router.push('/login')
}
</script>