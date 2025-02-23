<template>
  <div class="flex h-screen">
    <!-- 左侧菜单栏 -->
    <aside :style="{ width: `${sidebarWidth}px` }" class="flex flex-col flex-shrink-0 bg-white border-r border-gray-200">
      <div class="p-4 overflow-hidden whitespace-nowrap flex items-center">
        <NuxtLink to="/" class="flex items-center">
          <img
            src="/logo.svg"
            class="w-8 h-8 mr-2 text-primary-600 hover:text-primary-700 transition-colors"
            alt=""
          />
        </NuxtLink>
        <h1 class="text-xl font-bold text-gray-900">{{ sidebarWidth > 64 ? 'DSMS' : '' }}</h1>
      </div>
      <nav class="mt-4 flex-1">
        <NuxtLink
          to="/products"
          class="flex items-center px-4 py-2 text-gray-700 hover:bg-gray-100"
          :class="{ 'bg-gray-100': route.path.startsWith('/products') }"
        >
          <UIcon name="i-heroicons-cube" class="w-5 h-5 flex-shrink-0" />
          <span v-if="sidebarWidth > 64" class="ml-2 overflow-hidden text-ellipsis whitespace-nowrap">产品管理</span>
        </NuxtLink>
        <NuxtLink
          to="/batches"
          class="flex items-center px-4 py-2 text-gray-700 hover:bg-gray-100"
          :class="{ 'bg-gray-100': route.path.startsWith('/batches') }"
        >
          <UIcon name="i-heroicons-squares-2x2" class="w-5 h-5 flex-shrink-0" />
          <span v-if="sidebarWidth > 64" class="ml-2 overflow-hidden text-ellipsis whitespace-nowrap">批次管理</span>
        </NuxtLink>
        <NuxtLink
          to="/database/config"
          class="flex items-center px-4 py-2 text-gray-700 hover:bg-gray-100"
          :class="{ 'bg-gray-100': route.path.startsWith('/database/config') }"
        >
          <UIcon name="i-heroicons-server" class="w-5 h-5 flex-shrink-0" />
          <span v-if="sidebarWidth > 64" class="ml-2 overflow-hidden text-ellipsis whitespace-nowrap">数据库管理</span>
        </NuxtLink>
        <NuxtLink
          to="/product-batches"
          class="flex items-center px-4 py-2 text-gray-700 hover:bg-gray-100"
          :class="{ 'bg-gray-100': route.path.startsWith('/product-batches') }"
        >
          <UIcon name="i-heroicons-rectangle-stack" class="w-5 h-5 flex-shrink-0" />
          <span v-if="sidebarWidth > 64" class="ml-2 overflow-hidden text-ellipsis whitespace-nowrap">产品批次管理</span>
        </NuxtLink>
        <NuxtLink
          to="/product-batch-configs"
          class="flex items-center px-4 py-2 text-gray-700 hover:bg-gray-100"
          :class="{ 'bg-gray-100': route.path.startsWith('/product-batch-configs') }"
        >
          <UIcon name="i-heroicons-cog" class="w-5 h-5 flex-shrink-0" />
          <span v-if="sidebarWidth > 64" class="ml-2 overflow-hidden text-ellipsis whitespace-nowrap">产品批次配置管理</span>
        </NuxtLink>
      </nav>
      <!-- 展开/收起按钮 -->
      <div class="p-4 border-t border-gray-200">
        <button
          class="flex items-center justify-center w-full h-8 text-gray-500 hover:text-gray-900 transition-colors rounded hover:bg-gray-100"
          @click="toggleSidebar"
        >
          <UIcon
            :name="sidebarWidth > 64 ? 'i-heroicons-chevron-double-left' : 'i-heroicons-chevron-double-right'"
            class="w-5 h-5"
          />
        </button>
      </div>
    </aside>

    <!-- 拖动条 -->
    <div
      class="w-1 cursor-col-resize bg-transparent hover:bg-gray-200 active:bg-gray-300 transition-colors"
      @mousedown="startResize"
    ></div>

    <!-- 主要内容区域 -->
    <div class="flex-1 flex flex-col min-w-0">
      <!-- 顶部导航栏 -->
      <header class="bg-white border-b border-gray-200">
        <div class="flex justify-end h-16 px-4">
          <div class="flex items-center space-x-4">
            <span class="text-gray-700">欢迎，{{ authStore.user?.username }}</span>
            <UButton
              icon="i-heroicons-arrow-right-on-rectangle"
              color="primary"
              variant="soft"
              @click="handleLogout"
            >
              退出登录
            </UButton>
          </div>
        </div>
      </header>

      <!-- 页面内容 -->
      <main class="flex-1 overflow-auto">
        <slot />
      </main>
    </div>
  </div>
</template>

<script setup>
const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

// 侧边栏宽度状态
const defaultWidth = 180 // 默认宽度改为180px
const sidebarWidth = ref(defaultWidth)
const minWidth = 64 // 最小宽度，只显示图标
const maxWidth = 600 // 最大宽度

// 展开/收起菜单
const toggleSidebar = () => {
  sidebarWidth.value = sidebarWidth.value > 64 ? minWidth : defaultWidth
}

// 拖动调整宽度
const startResize = (e) => {
  e.preventDefault()
  
  const startX = e.clientX
  const startWidth = sidebarWidth.value
  
  const handleMouseMove = (e) => {
    const newWidth = startWidth + (e.clientX - startX)
    sidebarWidth.value = Math.min(Math.max(newWidth, minWidth), maxWidth)
  }
  
  const handleMouseUp = () => {
    document.removeEventListener('mousemove', handleMouseMove)
    document.removeEventListener('mouseup', handleMouseUp)
  }
  
  document.addEventListener('mousemove', handleMouseMove)
  document.addEventListener('mouseup', handleMouseUp)
}

const handleLogout = () => {
  authStore.clearAuth()
  router.push('/login')
}
</script>

<style>
/* 防止文本在拖动时被选中 */
.cursor-col-resize {
  user-select: none;
}

/* 平滑过渡效果 */
aside {
  transition: width 0.2s ease;
}
</style>