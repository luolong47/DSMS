<template>
  <div class="h-full">
    <div class="h-full bg-gradient-to-br from-gray-50 to-blue-50">
      <!-- 头部区域 -->
      <div class="p-4 bg-white border-b">
        <div class="flex justify-between items-center">
          <div>
            <h2 class="text-3xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">产品批次配置管理</h2>
            <p class="mt-2 text-gray-600">管理产品批次的数据库配置</p>
          </div>
          <UButton
            icon="i-heroicons-plus"
            color="primary"
            variant="solid"
            @click="handleCreate"
            class="transform transition-all duration-200 hover:scale-105"
          >
            新增配置
          </UButton>
        </div>
      </div>

      <!-- 搜索筛选 -->
      <div class="p-4">
        <UForm :state="searchForm">
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
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
              <UInput
                v-model="searchForm.configName"
                placeholder="搜索数据库配置名称"
                icon="i-heroicons-magnifying-glass"
                :ui="{ icon: { trailing: { pointer: '' } } }"
              >
                <template #trailing>
                  <UButton
                    v-show="searchForm.configName !== ''"
                    color="gray"
                    variant="link"
                    icon="i-heroicons-x-mark"
                    :padded="false"
                    @click="searchForm.configName = ''"
                  />
                </template>
              </UInput>
            </UFormGroup>
          </div>
        </UForm>
      </div>

      <!-- 数据表格 -->
      <div class="px-4">
        <UTable
          :rows="filteredConfigs"
          :columns="columns"
          :loading="loading"
          :empty-state="{ icon: 'i-heroicons-list-bullet', label: '暂无数据' }"
        >
          <template #type-data="{ row }">
            <UBadge
              :color="getDbTypeColor(row.dbType)"
              variant="subtle"
              size="sm"
              class="whitespace-nowrap"
            >
              {{ row.dbType }}
            </UBadge>
          </template>

          <template #url-data="{ row }">
            <UTooltip :text="row.dbUrl">
              <div class="break-all cursor-help text-gray-600">
                {{ row.dbUrl }}
              </div>
            </UTooltip>
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

    <!-- 新增/编辑弹窗 -->
    <UModal v-model="showDialog" :ui="{ width: 'md' }">
      <UCard :ui="{ divide: 'divide-y divide-gray-100 dark:divide-gray-800' }">
        <template #header>
          <div class="flex items-center justify-between">
            <h3 class="text-xl font-bold text-gray-900">{{ isViewMode ? '查看配置详情' : (isEdit ? '编辑配置' : '新增配置') }}</h3>
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
                <label class="block text-sm font-medium text-gray-700">数据库配置名称</label>
                <div class="mt-1 text-gray-900">{{ currentItem.configName }}</div>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">数据库类型</label>
                <div class="mt-1">
                  <UBadge
                    :color="getDbTypeColor(currentItem.dbType)"
                    variant="subtle"
                    size="sm"
                  >
                    {{ currentItem.dbType }}
                  </UBadge>
                </div>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">数据库URL</label>
                <div class="mt-1 text-gray-900 break-all">{{ currentItem.dbUrl }}</div>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">数据库用户名</label>
                <div class="mt-1 text-gray-900">{{ currentItem.dbUsername }}</div>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">数据库Schema</label>
                <div class="mt-1 text-gray-900">{{ currentItem.dbSchema }}</div>
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
          <UFormGroup label="产品批次" required>
            <USelect
              v-model="form.productBatchId"
              :options="productBatches"
              option-attribute="productBatchId"
              value-attribute="productBatchId"
              :ui="{ option: { selected: 'font-medium text-primary-500 bg-primary-50' } }"
            >
              <template #option="{ option }">
                {{ option.productNameEn }} [{{ option.productNameCn }}] - {{ option.batchNo }}
              </template>
            </USelect>
          </UFormGroup>
          <UFormGroup label="数据库配置" required>
            <USelect
              v-model="form.configName"
              :options="dbConfigs"
              option-attribute="configName"
              value-attribute="configName"
              :ui="{ option: { selected: 'font-medium text-primary-500 bg-primary-50' } }"
            >
              <template #option="{ option }">
                {{ option.configName }} ({{ option.type }})
              </template>
            </USelect>
          </UFormGroup>
        </UForm>

        <template #footer>
          <div class="flex justify-end gap-3">
            <UButton
              color="gray"
              variant="soft"
              @click="showDialog = false"
            >
              {{ isViewMode ? '关闭' : '取消' }}
            </UButton>
            <template v-if="!isViewMode">
              <UButton
                type="submit"
                color="primary"
                :loading="submitting"
                @click="handleSubmit"
              >
                确定
              </UButton>
            </template>
          </div>
        </template>
      </UCard>
    </UModal>

    <!-- 删除确认弹窗 -->
    <UModal v-model="showDeleteModal">
      <UCard :ui="{ divide: 'divide-y divide-gray-100 dark:divide-gray-800' }">
        <template #header>
          <div class="flex items-center justify-between">
            <h3 class="text-xl font-bold text-gray-900">确认删除</h3>
            <UButton
              icon="i-heroicons-x-mark"
              color="gray"
              variant="ghost"
              @click="showDeleteModal = false"
            />
          </div>
        </template>

        <div class="p-4">
          <p>确定要删除该配置吗？此操作不可恢复。</p>
        </div>

        <template #footer>
          <div class="flex justify-end gap-3">
            <UButton
              color="gray"
              variant="soft"
              @click="showDeleteModal = false"
            >
              取消
            </UButton>
            <UButton
              color="red"
              variant="soft"
              :loading="submitting"
              @click="confirmDelete"
            >
              删除
            </UButton>
          </div>
        </template>
      </UCard>
    </UModal>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { format } from 'date-fns'
import { useApi } from '~/composables/useApi'
import { useNuxtApp } from '#app'

const api = useApi()
const loading = ref(false)
const submitting = ref(false)
const showDialog = ref(false)
const showDeleteModal = ref(false)
const isEdit = ref(false)
const isViewMode = ref(false)
const currentItem = ref(null)

// 数据列表
const configs = ref([])
const productBatches = ref([])
const dbConfigs = ref([])

// 搜索表单
const searchForm = ref({
  productNameEn: '',
  batchNo: '',
  configName: ''
})

// 编辑表单
const form = ref({
  productBatchId: '',
  configName: ''
})

// 表格列定义
const columns = [
  {
    key: 'productNameEn',
    label: '产品名称'
  },
  {
    key: 'batchNo',
    label: '批次号'
  },
  {
    key: 'configName',
    label: '数据库配置'
  },
  {
    key: 'actions',
    label: '操作',
    sortable: false
  }
]

// 筛选后的配置列表
const filteredConfigs = computed(() => {
  return configs.value.filter(config => {
    const matchProductName = !searchForm.value.productNameEn || 
      config.productNameEn.toLowerCase().includes(searchForm.value.productNameEn.toLowerCase())
    const matchBatchNo = !searchForm.value.batchNo || 
      config.batchNo.toLowerCase().includes(searchForm.value.batchNo.toLowerCase())
    const matchConfigName = !searchForm.value.configName || 
      config.configName.toLowerCase().includes(searchForm.value.configName.toLowerCase())
    return matchProductName && matchBatchNo && matchConfigName
  })
})

// 获取配置列表
const fetchConfigs = async () => {
  loading.value = true
  try {
    const data = await api.get('/api/product-batch-configs')
    configs.value = data
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

// 获取产品批次列表
const fetchProductBatches = async () => {
  try {
    productBatches.value = await api.get('/api/product-batches')
  } catch (error) {
    useToast().add({
      title: '获取产品批次列表失败',
      description: error.message,
      color: 'red'
    })
  }
}

// 获取数据库配置列表
const fetchDbConfigs = async () => {
  try {
    const response = await api.post('/api/database/config/search', {})
    dbConfigs.value = response
  } catch (error) {
    useToast().add({
      title: '获取数据库配置列表失败',
      description: error.message,
      color: 'red'
    })
  }
}

// 格式化日期时间
const formatDateTime = (date) => {
  if (!date) return '-'
  return format(new Date(date), 'yyyy-MM-dd HH:mm:ss')
}

// 获取数据库类型对应的颜色
const getDbTypeColor = (type) => {
  const colorMap = {
    'MySQL': 'orange',
    'PostgreSQL': 'blue',
    'Oracle': 'red',
    'SQL Server': 'purple',
    'H2': 'green',
    'SQLite3': 'gray'
  }
  return colorMap[type] || 'gray'
}

// 新增配置
const handleCreate = () => {
  isViewMode.value = false
  isEdit.value = false
  form.value = {
    productBatchId: '',
    configName: ''
  }
  showDialog.value = true
}

// 编辑配置
const handleEdit = (config) => {
  isViewMode.value = false
  isEdit.value = true
  form.value = {
    productBatchId: config.productBatchId,
    configName: config.configName
  }
  showDialog.value = true
}

// 查看详情
const handleView = (config) => {
  isViewMode.value = true
  currentItem.value = config
  showDialog.value = true
}

// 删除配置
const handleDelete = (config) => {
  currentItem.value = config
  showDeleteModal.value = true
}

// 确认删除
const confirmDelete = async () => {
  if (submitting.value || !currentItem.value) return
  
  submitting.value = true
  try {
    const configId = `${currentItem.value.productBatchId}_${currentItem.value.configName}`
    await api.delete(`/api/product-batch-configs/${configId}`)
    showDeleteModal.value = false
    await fetchConfigs()
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
  } finally {
    submitting.value = false
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!form.value.productBatchId || !form.value.configName) {
    useToast().add({
      title: '请填写完整信息',
      description: '产品批次和数据库配置都是必选项',
      color: 'red'
    })
    return
  }

  submitting.value = true
  try {
    if (isEdit.value) {
      const configId = `${form.value.productBatchId}_${form.value.configName}`
      await api.put(`/api/product-batch-configs/${configId}`, form.value)
    } else {
      await api.post('/api/product-batch-configs', form.value)
    }
    
    showDialog.value = false
    await fetchConfigs()
    useToast().add({
      title: isEdit.value ? '更新成功' : '创建成功',
      color: 'green'
    })
  } catch (error) {
    useToast().add({
      title: isEdit.value ? '更新失败' : '创建失败',
      description: error.message,
      color: 'red'
    })
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  fetchConfigs()
  fetchProductBatches()
  fetchDbConfigs()
})
</script> 