<template>
  <div class="h-full">
    <div class="h-full bg-gradient-to-br from-gray-50 to-blue-50">
      <!-- 头部区域 -->
      <div class="p-4 bg-white border-b">
        <div class="flex justify-between items-center">
          <div>
            <h2 class="text-3xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">产品管理</h2>
            <p class="mt-2 text-gray-600">管理和维护产品信息</p>
          </div>
          <UButton
            icon="i-heroicons-plus"
            color="primary"
            variant="solid"
            @click="handleAdd"
            class="transform transition-all duration-200 hover:scale-105"
          >
            新增产品
          </UButton>
        </div>
      </div>

      <!-- 搜索筛选 -->
      <div class="p-4">
        <UForm :state="searchForm">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <UFormGroup>
              <UInput
                v-model="searchForm.productNameEn"
                placeholder="搜索产品英文名称"
                icon="i-heroicons-magnifying-glass"
                :ui="{ icon: { trailing: { pointer: '' } } }"
              >
                <template #trailing>
                  <UButton
                    v-show="searchForm.productNameEn !== ''"
                    color="gray"
                    variant="link"
                    icon="i-heroicons-x-mark"
                    :padded="false"
                    @click="searchForm.productNameEn = ''"
                  />
                </template>
              </UInput>
            </UFormGroup>
            <UFormGroup>
              <UInput
                v-model="searchForm.productNameCn"
                placeholder="搜索产品中文名称"
                icon="i-heroicons-magnifying-glass"
                :ui="{ icon: { trailing: { pointer: '' } } }"
              >
                <template #trailing>
                  <UButton
                    v-show="searchForm.productNameCn !== ''"
                    color="gray"
                    variant="link"
                    icon="i-heroicons-x-mark"
                    :padded="false"
                    @click="searchForm.productNameCn = ''"
                  />
                </template>
              </UInput>
            </UFormGroup>
          </div>
        </UForm>
      </div>

      <!-- 产品列表 -->
      <div class="px-4">
        <UTable
          :rows="filteredProducts"
          :columns="columns"
          :loading="loading"
          :empty-state="{ icon: 'i-heroicons-list-bullet', label: '暂无数据' }"
        >
          <template #description-data="{ row }">
            <span class="line-clamp-2">{{ row.description }}</span>
          </template>
          <template #actions-data="{ row }">
            <UButtonGroup>
              <UButton
                icon="i-heroicons-eye"
                color="primary"
                variant="ghost"
                size="xs"
                @click="handleView(row)"
              />
              <UButton
                icon="i-heroicons-pencil"
                color="primary"
                variant="ghost"
                size="xs"
                @click="handleEdit(row)"
              />
              <UButton
                icon="i-heroicons-trash"
                color="red"
                variant="ghost"
                size="xs"
                @click="handleDelete(row)"
              />
            </UButtonGroup>
          </template>
        </UTable>
      </div>
    </div>

    <!-- 产品表单对话框 -->
    <UModal v-model="showDialog" :ui="{ width: 'md' }">
      <UCard :ui="{ divide: 'divide-y divide-gray-100 dark:divide-gray-800' }">
        <template #header>
          <div class="flex items-center justify-between">
            <h3 class="text-xl font-bold text-gray-900">{{ isViewMode ? '查看产品详情' : (isEdit ? '编辑产品' : '新增产品') }}</h3>
            <UButton
              icon="i-heroicons-x-mark"
              color="gray"
              variant="ghost"
              @click="showDialog = false"
            />
          </div>
        </template>

        <template v-if="isViewMode">
          <div class="space-y-6 py-4">
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700">英文名称</label>
                <div class="mt-1 text-gray-900">{{ currentItem.productNameEn }}</div>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">中文名称</label>
                <div class="mt-1 text-gray-900">{{ currentItem.productNameCn }}</div>
              </div>
              <div class="col-span-2">
                <label class="block text-sm font-medium text-gray-700">描述</label>
                <div class="mt-1 text-gray-900">{{ currentItem.description || '-' }}</div>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">创建时间</label>
                <div class="mt-1 text-gray-900">{{ formatDateTime(currentItem.createdAt) }}</div>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">最后修改时间</label>
                <div class="mt-1 text-gray-900">{{ formatDateTime(currentItem.updatedAt) }}</div>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">最后修改人</label>
                <div class="mt-1 text-gray-900">{{ currentItem.lastModifiedBy }}</div>
              </div>
            </div>
          </div>
        </template>

        <UForm v-else :state="form" class="space-y-6 py-4" @submit="handleSubmit">
          <UFormGroup label="英文名称" required>
            <UInput
              v-model="form.productNameEn"
              :disabled="isEdit"
              color="primary"
            />
          </UFormGroup>
          <UFormGroup label="中文名称" required>
            <UInput
              v-model="form.productNameCn"
              color="primary"
            />
          </UFormGroup>
          <UFormGroup label="描述">
            <UTextarea
              v-model="form.description"
              :rows="4"
              color="primary"
              class="resize-none"
            />
          </UFormGroup>
        </UForm>

        <template #footer>
          <div class="flex justify-end gap-3">
            <UButton
              color="gray"
              variant="soft"
              @click="showDialog = false"
            >
              取消
            </UButton>
            <UButton
              type="submit"
              color="primary"
              :loading="submitting"
              @click="handleSubmit"
            >
              确定
            </UButton>
          </div>
        </template>
      </UCard>
    </UModal>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useApi } from '~/composables/useApi'

const api = useApi()
const products = ref([])
const showDialog = ref(false)
const isEdit = ref(false)
const loading = ref(false)
const submitting = ref(false)
const isViewMode = ref(false)
const currentItem = ref(null)

// 搜索表单
const searchForm = ref({
  productNameEn: '',
  productNameCn: ''
})

// 编辑表单
const form = ref({
  productNameEn: '',
  productNameCn: '',
  description: ''
})

// 表格列定义
const columns = [
  {
    key: 'productNameEn',
    label: '英文名称'
  },
  {
    key: 'productNameCn',
    label: '中文名称'
  },
  {
    key: 'actions',
    label: '操作',
    sortable: false
  }
]

// 筛选后的产品列表
const filteredProducts = computed(() => {
  return products.value.filter(product => {
    const matchEnName = product.productNameEn.toLowerCase().includes(searchForm.value.productNameEn.toLowerCase())
    const matchCnName = product.productNameCn.includes(searchForm.value.productNameCn)
    return matchEnName && matchCnName
  })
})

// 获取产品列表
const fetchProducts = async () => {
  loading.value = true
  try {
    const data = await api.get('/api/products')
    products.value = data
  } catch (error) {
    console.error('获取产品列表失败:', error)
    useToast().add({
      title: '获取数据失败',
      description: error.message,
      color: 'red'
    })
  } finally {
    loading.value = false
  }
}

// 格式化日期时间
const formatDateTime = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 新增产品
const handleAdd = () => {
  isEdit.value = false
  form.value = {
    productNameEn: '',
    productNameCn: '',
    description: ''
  }
  showDialog.value = true
}

// 编辑产品
const handleEdit = (product) => {
  isEdit.value = true
  form.value = { ...product }
  showDialog.value = true
}

// 删除产品
const handleDelete = async (product) => {
  const confirmed = await useConfirmDialog({
    title: '确认删除',
    content: '确定要删除该产品吗？此操作不可恢复。',
    confirmLabel: '删除',
    cancelLabel: '取消',
    confirmButtonProps: {
      color: 'red',
      variant: 'soft'
    }
  })
  
  if (!confirmed) return
  
  try {
    await api.delete(`/api/products/${product.productNameEn}`)
    await fetchProducts()
    useToast().add({
      title: '删除成功',
      color: 'green'
    })
  } catch (error) {
    console.error('删除产品失败:', error)
    useToast().add({
      title: '删除失败',
      description: error.message,
      color: 'red'
    })
  }
}

// 提交表单
const handleSubmit = async () => {
  submitting.value = true
  try {
    if (isEdit.value) {
      await api.put(`/api/products/${form.value.productNameEn}`, form.value)
    } else {
      await api.post('/api/products', form.value)
    }
    
    showDialog.value = false
    await fetchProducts()
    useToast().add({
      title: isEdit.value ? '更新成功' : '创建成功',
      color: 'green'
    })
  } catch (error) {
    console.error(isEdit.value ? '更新产品失败:' : '新增产品失败:', error)
    useToast().add({
      title: isEdit.value ? '更新失败' : '创建失败',
      description: error.message,
      color: 'red'
    })
  } finally {
    submitting.value = false
  }
}

// 查看产品
const handleView = (item) => {
  isViewMode.value = true
  isEdit.value = false
  currentItem.value = item
  showDialog.value = true
}

onMounted(() => {
  fetchProducts()
})
</script>