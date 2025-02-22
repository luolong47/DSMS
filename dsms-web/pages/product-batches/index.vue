<template>
  <UContainer>
    <UCard class="min-h-screen bg-gradient-to-br from-gray-50 to-blue-50">
      <!-- 头部区域 -->
      <template #header>
        <div class="flex justify-between items-center">
          <div>
            <h2 class="text-3xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">产品批次关联管理</h2>
            <p class="mt-2 text-gray-600">管理产品和批次的关联关系</p>
          </div>
          <UButton
            icon="i-heroicons-plus"
            color="primary"
            variant="solid"
            @click="handleCreate"
            class="transform transition-all duration-200 hover:scale-105"
          >
            新增关联
          </UButton>
        </div>
      </template>

      <!-- 搜索筛选 -->
      <div class="mb-8">
        <UForm :state="searchForm">
          <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
            <UFormGroup>
              <UInput
                v-model="searchForm.productNameEn"
                placeholder="搜索产品名称"
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
                v-model="searchForm.batchNo"
                placeholder="搜索批次号"
                icon="i-heroicons-magnifying-glass"
                :ui="{ icon: { trailing: { pointer: '' } } }"
              >
                <template #trailing>
                  <UButton
                    v-show="searchForm.batchNo !== ''"
                    color="gray"
                    variant="link"
                    icon="i-heroicons-x-mark"
                    :padded="false"
                    @click="searchForm.batchNo = ''"
                  />
                </template>
              </UInput>
            </UFormGroup>
            <UFormGroup>
              <UPopover :popper="{ placement: 'bottom-start' }">
                <UButton
                  color="primary"
                  variant="soft"
                  :icon="searchForm.startDate ? undefined : 'i-heroicons-calendar'"
                  class="w-full justify-start"
                >
                  {{ searchForm.startDate ? formatDate(searchForm.startDate) : '选择开始日期' }}
                  <UIcon
                    v-if="searchForm.startDate"
                    name="i-heroicons-x-mark"
                    class="ml-auto cursor-pointer"
                    @click.stop="searchForm.startDate = ''"
                  />
                </UButton>
                <template #panel="{ close }">
                  <DatePicker
                    v-model="searchForm.startDate"
                    :max-date="searchForm.endDate"
                    @close="close"
                  />
                </template>
              </UPopover>
            </UFormGroup>
            <UFormGroup>
              <UPopover :popper="{ placement: 'bottom-start' }">
                <UButton
                  color="primary"
                  variant="soft"
                  :icon="searchForm.endDate ? undefined : 'i-heroicons-calendar'"
                  class="w-full justify-start"
                >
                  {{ searchForm.endDate ? formatDate(searchForm.endDate) : '选择结束日期' }}
                  <UIcon
                    v-if="searchForm.endDate"
                    name="i-heroicons-x-mark"
                    class="ml-auto cursor-pointer"
                    @click.stop="searchForm.endDate = ''"
                  />
                </UButton>
                <template #panel="{ close }">
                  <DatePicker
                    v-model="searchForm.endDate"
                    :min-date="searchForm.startDate"
                    @close="close"
                  />
                </template>
              </UPopover>
            </UFormGroup>
          </div>
        </UForm>
      </div>

      <!-- 数据表格 -->
      <UTable
        :rows="productBatches"
        :columns="columns"
        :loading="loading"
        :empty-state="{ icon: 'i-heroicons-list-bullet', label: '暂无数据' }"
      >
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
              icon="i-heroicons-trash"
              color="red"
              variant="ghost"
              size="xs"
              @click="handleDelete(row)"
            />
          </UButtonGroup>
        </template>
      </UTable>
    </UCard>

    <!-- 新增/查看弹窗 -->
    <UModal v-model="showDialog" :ui="{ width: 'md' }">
      <UCard :ui="{ divide: 'divide-y divide-gray-100 dark:divide-gray-800' }">
        <template #header>
          <div class="flex items-center justify-between">
            <h3 class="text-xl font-bold text-gray-900">{{ isViewMode ? '查看关联详情' : '新增产品批次关联' }}</h3>
            <UButton
              icon="i-heroicons-x-mark"
              color="gray"
              variant="ghost"
              @click="showDialog = false"
            />
          </div>
        </template>

        <UForm v-if="!isViewMode" :state="form" class="space-y-6 py-4" @submit="handleSubmit">
          <UFormGroup label="产品" required>
            <USelect
              v-model="form.productNameEn"
              :options="products"
              option-attribute="productNameEn"
              value-attribute="productNameEn"
              :ui="{ option: { selected: 'font-medium text-primary-500 bg-primary-50' } }"
            >
              <template #option="{ option }">
                {{ option.productNameEn }} [{{ option.productNameCn }}]
              </template>
            </USelect>
          </UFormGroup>
          <UFormGroup label="批次" required>
            <USelect
              v-model="form.batchNo"
              :options="batches"
              option-attribute="batchNo"
              value-attribute="batchNo"
              :ui="{ option: { selected: 'font-medium text-primary-500 bg-primary-50' } }"
            >
              <template #option="{ option }">
                {{ option.batchNo }} ({{ formatDate(option.startDate) }} - {{ formatDate(option.endDate) }})
              </template>
            </USelect>
          </UFormGroup>
        </UForm>
        <div v-else class="space-y-6 py-4">
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">产品英文名称</label>
              <div class="mt-1 text-gray-900">{{ currentItem.productNameEn }}</div>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">产品中文名称</label>
              <div class="mt-1 text-gray-900">{{ currentItem.productNameCn }}</div>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">批次号</label>
              <div class="mt-1 text-gray-900">{{ currentItem.batchNo }}</div>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">批次开始日期</label>
              <div class="mt-1 text-gray-900">{{ formatDateTime(currentItem.batchStartDate) }}</div>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">批次结束日期</label>
              <div class="mt-1 text-gray-900">{{ formatDateTime(currentItem.batchEndDate) }}</div>
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

        <template #footer>
          <div class="flex justify-end gap-3">
            <UButton
              color="gray"
              variant="soft"
              @click="showDialog = false"
            >
              {{ isViewMode ? '关闭' : '取消' }}
            </UButton>
            <UButton
              v-if="!isViewMode"
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
  </UContainer>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useApi } from '~/composables/useApi'
import DatePicker from '~/components/DatePicker.vue'
import { format } from 'date-fns'

const api = useApi()
const allProductBatches = ref([]) // 存储所有数据
const productBatches = ref([]) // 存储筛选后的数据
const products = ref([])
const batches = ref([])
const showDialog = ref(false)
const isViewMode = ref(false)
const loading = ref(false)
const submitting = ref(false)
const currentItem = ref(null)

// 搜索表单
const searchForm = ref({
  productNameEn: '',
  batchNo: '',
  startDate: '',
  endDate: ''
})

// 编辑表单
const form = ref({
  productNameEn: '',
  batchNo: ''
})

// 表格列定义
const columns = [
  {
    key: 'productNameEn',
    label: '产品英文名称'
  },
  {
    key: 'productNameCn',
    label: '产品中文名称'
  },
  {
    key: 'batchNo',
    label: '批次号'
  },
  {
    key: 'batchStartDate',
    label: '批次开始日期',
    formatter: (value) => formatDate(value)
  },
  {
    key: 'batchEndDate',
    label: '批次结束日期',
    formatter: (value) => formatDate(value)
  },
  {
    key: 'createdAt',
    label: '创建时间',
    formatter: (value) => formatDate(value)
  },
  {
    key: 'actions',
    label: '操作',
    sortable: false
  }
]

// 获取产品批次关联列表
const fetchProductBatches = async () => {
  loading.value = true
  try {
    const response = await api.get('/api/product-batches')
    allProductBatches.value = response
    filterProductBatches() // 获取数据后立即进行筛选
  } catch (error) {
    useToast().add({
      title: '获取数据失败',
      description: error.message,
      color: 'red'
    })
  } finally {
    loading.value = false
  }
}

// 本地筛选函数
const filterProductBatches = () => {
  let filteredData = [...allProductBatches.value]

  // 按产品名称筛选
  if (searchForm.value.productNameEn) {
    filteredData = filteredData.filter(item => 
      item.productNameEn.toLowerCase().includes(searchForm.value.productNameEn.toLowerCase())
    )
  }

  // 按批次号筛选
  if (searchForm.value.batchNo) {
    filteredData = filteredData.filter(item => 
      item.batchNo.toLowerCase().includes(searchForm.value.batchNo.toLowerCase())
    )
  }

  // 按开始日期筛选
  if (searchForm.value.startDate) {
    const startDate = new Date(searchForm.value.startDate)
    startDate.setHours(0, 0, 0, 0)
    filteredData = filteredData.filter(item => {
      const itemDate = new Date(item.batchStartDate)
      return itemDate >= startDate
    })
  }

  // 按结束日期筛选
  if (searchForm.value.endDate) {
    const endDate = new Date(searchForm.value.endDate)
    endDate.setHours(23, 59, 59, 999)
    filteredData = filteredData.filter(item => {
      const itemDate = new Date(item.batchEndDate)
      return itemDate <= endDate
    })
  }

  productBatches.value = filteredData
}

// 获取产品列表
const fetchProducts = async () => {
  try {
    products.value = await api.get('/api/products')
  } catch (error) {
    useToast().add({
      title: '获取产品列表失败',
      description: error.message,
      color: 'red'
    })
  }
}

// 获取批次列表
const fetchBatches = async () => {
  try {
    batches.value = await api.get('/api/batches')
  } catch (error) {
    useToast().add({
      title: '获取批次列表失败',
      description: error.message,
      color: 'red'
    })
  }
}

// 格式化日期
const formatDate = (date) => {
  if (!date) return '-'
  return format(new Date(date), 'yyyy-MM-dd')
}

// 格式化日期时间
const formatDateTime = (date) => {
  if (!date) return '-'
  return format(new Date(date), 'yyyy-MM-dd HH:mm:ss')
}

// 新增关联
const handleCreate = () => {
  isViewMode.value = false
  form.value = {
    productNameEn: '',
    batchNo: ''
  }
  showDialog.value = true
}

// 查看详情
const handleView = (item) => {
  isViewMode.value = true
  currentItem.value = item
  showDialog.value = true
}

// 删除关联
const handleDelete = async (item) => {
  const confirmed = await useConfirmDialog({
    title: '确认删除',
    content: '确定要删除该关联吗？此操作不可恢复。',
    confirmLabel: '删除',
    cancelLabel: '取消',
    confirmButtonProps: {
      color: 'red',
      variant: 'soft'
    }
  })
  
  if (!confirmed) return
  
  try {
    await api.delete(`/api/product-batches/${item.productBatchId}`)
    // 更新本地数据
    allProductBatches.value = allProductBatches.value.filter(
      pb => pb.productBatchId !== item.productBatchId
    )
    filterProductBatches() // 重新筛选
    useToast().add({
      title: '删除成功',
      color: 'green'
    })
  } catch (error) {
    useToast().add({
      title: '删除失败',
      description: error.message,
      color: 'red'
    })
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!form.value.productNameEn || !form.value.batchNo) {
    useToast().add({
      title: '请填写完整信息',
      description: '产品和批次都是必选项',
      color: 'red'
    })
    return
  }

  submitting.value = true
  try {
    const response = await api.post('/api/product-batches', form.value)
    
    if (response) {
      // 更新本地数据
      allProductBatches.value = [response, ...allProductBatches.value]
      filterProductBatches() // 重新筛选
    }
    
    showDialog.value = false
    useToast().add({
      title: '创建成功',
      color: 'green'
    })
  } catch (error) {
    useToast().add({
      title: '创建失败',
      description: error.message,
      color: 'red'
    })
  } finally {
    submitting.value = false
  }
}

// 重置搜索条件
const handleReset = () => {
  searchForm.value = {
    productNameEn: '',
    batchNo: '',
    startDate: '',
    endDate: ''
  }
  filterProductBatches() // 重新筛选
}

// 监听搜索表单变化
watch(
  () => searchForm.value,
  () => {
    filterProductBatches() // 当搜索条件变化时，重新筛选
  },
  { deep: true }
)

onMounted(() => {
  fetchProductBatches() // 初始加载数据
  fetchProducts()
  fetchBatches()
})
</script> 