<template>
  <div class="h-full flex flex-col">
    <!-- 头部区域 -->
    <div class="bg-white border-b">
      <div class="p-4 flex justify-between items-center">
        <div>
          <h2 class="text-3xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">数据库配置管理</h2>
          <p class="mt-2 text-gray-600">管理数据库连接配置信息</p>
        </div>
        <UButton
          icon="i-heroicons-plus"
          color="primary"
          variant="solid"
          @click="handleAdd"
          class="transform transition-all duration-200 hover:scale-105"
        >
          新增配置
        </UButton>
      </div>
    </div>

    <!-- 搜索筛选 -->
    <div class="bg-white/50 backdrop-blur-sm">
      <div class="p-4">
        <UForm :state="searchForm">
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <UFormGroup>
              <UInput
                v-model="searchForm.configName"
                placeholder="搜索配置名称"
                icon="i-heroicons-magnifying-glass"
                :ui="{ icon: { trailing: { pointer: '' } } }"
              >
                <template #trailing>
                  <UButton
                    v-if="searchForm.configName"
                    color="gray"
                    variant="link"
                    icon="i-heroicons-x-mark"
                    :padded="false"
                    @click="searchForm.configName = ''"
                  />
                </template>
              </UInput>
            </UFormGroup>
            <UFormGroup>
              <UInput
                v-model="searchForm.url"
                placeholder="搜索数据库URL"
                icon="i-heroicons-magnifying-glass"
                :ui="{ icon: { trailing: { pointer: '' } } }"
              >
                <template #trailing>
                  <UButton
                    v-if="searchForm.url"
                    color="gray"
                    variant="link"
                    icon="i-heroicons-x-mark"
                    :padded="false"
                    @click="searchForm.url = ''"
                  />
                </template>
              </UInput>
            </UFormGroup>
            <UFormGroup>
              <UInput
                v-model="searchForm.username"
                placeholder="搜索用户名"
                icon="i-heroicons-magnifying-glass"
                :ui="{ icon: { trailing: { pointer: '' } } }"
              >
                <template #trailing>
                  <UButton
                    v-if="searchForm.username"
                    color="gray"
                    variant="link"
                    icon="i-heroicons-x-mark"
                    :padded="false"
                    @click="searchForm.username = ''"
                  />
                </template>
              </UInput>
            </UFormGroup>
          </div>
        </UForm>
      </div>
    </div>

    <!-- 数据表格区域 -->
    <div class="flex-1 overflow-hidden">
      <div class="h-full p-4 overflow-auto">
        <div class="bg-white rounded-lg shadow">
          <UTable
            :rows="tableData"
            :columns="columns"
            :loading="loading"
            :empty-state="{ icon: 'i-heroicons-server', label: '暂无数据' }"
            class="w-full min-w-[800px]"
            :ui="{
              wrapper: 'relative',
              td: {
                base: 'whitespace-normal break-words py-4'
              },
              th: {
                base: 'whitespace-normal'
              }
            }"
          >
            <template #configName-data="{ row }">
              <div class="font-medium text-gray-900">{{ row.configName }}</div>
            </template>

            <template #type-data="{ row }">
              <UBadge
                :color="getDbTypeColor(row.type)"
                variant="subtle"
                size="sm"
                class="whitespace-nowrap"
              >
                {{ row.type }}
              </UBadge>
            </template>

            <template #url-data="{ row }">
              <UTooltip :text="row.url">
                <div class="break-all cursor-help text-gray-600">
                  {{ row.url }}
                </div>
              </UTooltip>
            </template>

            <template #username-data="{ row }">
              <div class="text-gray-600">{{ row.username }}</div>
            </template>

            <template #schema-data="{ row }">
              <div class="text-gray-600">{{ row.schema || '-' }}</div>
            </template>

            <template #createdAt-data="{ row }">
              <UTooltip :text="row.createdAt">
                <div class="text-gray-500 text-sm">
                  {{ formatDateTime(row.createdAt) }}
                </div>
              </UTooltip>
            </template>

            <template #updatedAt-data="{ row }">
              <UTooltip :text="row.updatedAt">
                <div class="text-gray-500 text-sm">
                  {{ formatDateTime(row.updatedAt) }}
                </div>
              </UTooltip>
            </template>

            <template #lastModifiedBy-data="{ row }">
              <div class="text-gray-600">{{ row.lastModifiedBy }}</div>
            </template>

            <template #actions-data="{ row }">
              <div class="flex justify-end items-center space-x-1">
                <div class="hidden sm:block">
                  <UButtonGroup>
                    <UButton
                      icon="i-heroicons-eye"
                      color="primary"
                      variant="ghost"
                      size="xs"
                      @click="handleView(row)"
                    />
                    <UButton
                      icon="i-heroicons-pencil-square"
                      color="blue"
                      variant="ghost"
                      size="xs"
                      @click="handleEdit(row)"
                    />
                    <UButton
                      icon="i-heroicons-document-duplicate"
                      color="blue"
                      variant="ghost"
                      size="xs"
                      @click="handleCopy(row)"
                    />
                    <UButton
                      icon="i-heroicons-trash"
                      color="red"
                      variant="ghost"
                      size="xs"
                      @click="handleDelete(row)"
                    />
                  </UButtonGroup>
                </div>
                <UDropdown
                  :items="getActionItems(row)"
                  :ui="{
                    base: 'min-w-[140px]',
                    items: 'divide-y divide-gray-100',
                    item: {
                      base: 'flex items-center gap-2 px-4 py-2 cursor-pointer text-sm',
                      active: 'bg-gray-100 text-gray-900',
                      inactive: 'text-gray-700 hover:bg-gray-50',
                      icon: { base: 'flex-shrink-0 h-4 w-4' }
                    }
                  }"
                  class="sm:hidden"
                >
                  <UButton
                    icon="i-heroicons-ellipsis-vertical"
                    color="gray"
                    variant="ghost"
                    size="xs"
                  />
                </UDropdown>
              </div>
            </template>
          </UTable>
        </div>
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <UModal v-model="showModal" :ui="{ width: 'md' }">
      <UCard :ui="{ divide: 'divide-y divide-gray-100 dark:divide-gray-800' }">
        <template #header>
          <div class="flex items-center justify-between">
            <h3 class="text-xl font-bold text-gray-900">{{ modalTitle }}</h3>
            <UButton
              icon="i-heroicons-x-mark"
              color="gray"
              variant="ghost"
              @click="showModal = false"
            />
          </div>
        </template>

        <UForm 
          :validate="validateForm" 
          :state="formData"
          class="space-y-6 py-4" 
          @submit="handleSubmit"
        >
          <UFormGroup label="配置名称" name="configName" required>
            <UInput v-model="formData.configName" />
          </UFormGroup>
          <UFormGroup label="数据库URL" name="url" required>
            <UInput v-model="formData.url" />
          </UFormGroup>
          <UFormGroup label="数据库类型" name="type">
            <UInput v-model="formData.type" disabled />
          </UFormGroup>
          <UFormGroup label="驱动类名" name="driverClassName">
            <UInput v-model="formData.driverClassName" disabled />
          </UFormGroup>
          <UFormGroup label="Schema" name="schema">
            <UInput v-model="formData.schema" />
          </UFormGroup>
          <UFormGroup label="用户名" name="username" required>
            <UInput v-model="formData.username" />
          </UFormGroup>
          <UFormGroup label="密码" name="password" required>
            <UInput v-model="formData.password" type="password" />
          </UFormGroup>
          <div class="flex justify-end gap-3">
            <UButton
              color="gray"
              variant="soft"
              @click="showModal = false"
            >
              取消
            </UButton>
            <UButton
              @click="handleTestConnection"
              color="primary"
              variant="soft"
              :loading="testing"
              :disabled="testing"
            >
              {{ testing ? '测试中' : '测试连接' }}
            </UButton>
            <UButton
              type="submit"
              color="primary"
              :loading="submitting"
            >
              确定
            </UButton>
          </div>
        </UForm>

        <template #footer>
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

    <!-- 查看详情弹窗 -->
    <UModal v-model="showViewModal" :ui="{ width: 'md' }">
      <UCard :ui="{ divide: 'divide-y divide-gray-100 dark:divide-gray-800' }">
        <template #header>
          <div class="flex items-center justify-between">
            <h3 class="text-xl font-bold text-gray-900">查看数据库配置</h3>
            <UButton
              icon="i-heroicons-x-mark"
              color="gray"
              variant="ghost"
              @click="showViewModal = false"
            />
          </div>
        </template>

        <div class="py-4 space-y-4">
          <div class="grid grid-cols-1 gap-4">
            <div class="space-y-1">
              <div class="text-sm text-gray-500">配置名称</div>
              <div class="text-gray-900">{{ viewData.configName }}</div>
            </div>
            <div class="space-y-1">
              <div class="text-sm text-gray-500">数据库类型</div>
              <div>
                <UBadge
                  :color="getDbTypeColor(viewData.type)"
                  variant="subtle"
                  size="sm"
                >
                  {{ viewData.type }}
                </UBadge>
              </div>
            </div>
            <div class="space-y-1">
              <div class="text-sm text-gray-500">数据库URL</div>
              <div class="text-gray-900 break-all">{{ viewData.url }}</div>
            </div>
            <div class="space-y-1">
              <div class="text-sm text-gray-500">驱动类名</div>
              <div class="text-gray-900">{{ viewData.driverClassName }}</div>
            </div>
            <div class="space-y-1">
              <div class="text-sm text-gray-500">Schema</div>
              <div class="text-gray-900">{{ viewData.schema || '-' }}</div>
            </div>
            <div class="space-y-1">
              <div class="text-sm text-gray-500">用户名</div>
              <div class="text-gray-900">{{ viewData.username }}</div>
            </div>
            <div class="space-y-1">
              <div class="text-sm text-gray-500">创建时间</div>
              <div class="text-gray-900">{{ formatDateTime(viewData.createdAt) }}</div>
            </div>
            <div class="space-y-1">
              <div class="text-sm text-gray-500">更新时间</div>
              <div class="text-gray-900">{{ formatDateTime(viewData.updatedAt) }}</div>
            </div>
            <div class="space-y-1">
              <div class="text-sm text-gray-500">最后修改人</div>
              <div class="text-gray-900">{{ viewData.lastModifiedBy || '-' }}</div>
            </div>
          </div>
        </div>

        <template #footer>
          <div class="flex justify-end">
            <UButton
              color="gray"
              variant="soft"
              @click="showViewModal = false"
            >
              关闭
            </UButton>
          </div>
        </template>
      </UCard>
    </UModal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useApi } from '~/composables/useApi'
import { format } from 'date-fns'
import type { BadgeColor, DropdownItem } from '#ui/types'

const api = useApi()

// 类型定义
interface DbConfig {
  configName: string
  type: string
  url: string
  username: string
  password: string
  driverClassName: string
  schema: string
  createdAt?: string
  updatedAt?: string
  lastModifiedBy?: string
}

interface DbConfigAnalyzeResult {
  type: string
  driverClassName: string
  schemas: string[]
}

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const testing = ref(false)
const showModal = ref(false)
const showDeleteModal = ref(false)
const showViewModal = ref(false)
const modalTitle = ref('')
const originalData = ref<DbConfig[]>([])
const tableData = ref<DbConfig[]>([])
const currentRow = ref<DbConfig | null>(null)
const viewData = reactive<DbConfig>({
  configName: '',
  url: '',
  type: '',
  driverClassName: '',
  schema: '',
  username: '',
  password: ''
})

// 搜索表单
const searchForm = reactive({
  configName: '',
  url: '',
  username: ''
})

// 编辑表单
const formData = reactive<DbConfig>({
  configName: '',
  url: '',
  type: '',
  driverClassName: '',
  schema: '',
  username: '',
  password: ''
})

// 表格列定义
const columns = [
  { 
    key: 'configName', 
    label: '配置名称'
  },
  { 
    key: 'type', 
    label: '数据库类型'
  },
  {
    key: 'url',
    label: '数据库URL',
    class: 'max-w-xs'
  },
  { 
    key: 'username', 
    label: '用户名'
  },
  { 
    key: 'schema', 
    label: 'Schema'
  },
  { 
    key: 'actions', 
    label: '操作',
    class: 'w-24 text-right'
  }
]

// 表单验证
const validateForm = (state: DbConfig) => {
  const errors: Array<{ path: keyof DbConfig, message: string }> = []
  if (!state.configName) {
    errors.push({
      path: 'configName',
      message: '配置名称不能为空'
    })
  }
  if (!state.url) {
    errors.push({
      path: 'url',
      message: '数据库URL不能为空'
    })
  }
  if (!state.username) {
    errors.push({
      path: 'username',
      message: '用户名不能为空'
    })
  }
  if (!state.password) {
    errors.push({
      path: 'password',
      message: '密码不能为空'
    })
  }
  return errors
}

// 获取数据库配置列表
const fetchConfigs = async () => {
  loading.value = true
  try {
    const response = await api.post<DbConfig[]>('/api/database/config/search', {})
    originalData.value = response || []
    filterData()
  } catch (error: any) {
    useToast().add({
      title: '获取数据失败',
      description: error.message,
      color: 'red'
    })
    originalData.value = []
    tableData.value = []
  } finally {
    loading.value = false
  }
}

// 本地筛选数据
const filterData = () => {
  tableData.value = originalData.value.filter(item => {
    const configNameMatch = !searchForm.configName || 
      item.configName.toLowerCase().includes(searchForm.configName.toLowerCase())
    const urlMatch = !searchForm.url || 
      item.url.toLowerCase().includes(searchForm.url.toLowerCase())
    const usernameMatch = !searchForm.username || 
      item.username.toLowerCase().includes(searchForm.username.toLowerCase())
    return configNameMatch && urlMatch && usernameMatch
  })
}

const handleAdd = () => {
  modalTitle.value = '新增数据库配置'
  const emptyForm: DbConfig = {
    configName: '',
    url: '',
    type: '',
    driverClassName: '',
    schema: '',
    username: '',
    password: ''
  }
  Object.assign(formData, emptyForm)
  showModal.value = true
}

const handleEdit = (row: DbConfig) => {
  modalTitle.value = '编辑数据库配置'
  Object.assign(formData, row)
  showModal.value = true
}

const handleCopy = (row: DbConfig) => {
  modalTitle.value = '复制数据库配置'
  Object.assign(formData, row)
  formData.configName = `${formData.configName}_copy`
  showModal.value = true
}

const handleDelete = (row: DbConfig) => {
  currentRow.value = row
  showDeleteModal.value = true
}

const handleTestConnection = async () => {
  if (testing.value) return
  
  testing.value = true
  try {
    await api.post('/api/database/config/test', formData)
    useToast().add({
      title: '连接成功',
      description: ``,
      icon: 'i-heroicons-check-circle',
      color: 'green',
      timeout: 3000,
      closeButton: {
        icon: 'i-heroicons-x-mark',
        color: 'gray',
        variant: 'ghost',
        size: 'sm'
      }
    })
  } catch (error: any) {
    useToast().add({
      title: '连接失败',
      description: error.message,
      icon: 'i-heroicons-exclamation-circle',
      color: 'red',
      timeout: 5000,
      closeButton: {
        icon: 'i-heroicons-x-mark',
        color: 'gray',
        variant: 'ghost',
        size: 'sm'
      }
    })
  } finally {
    testing.value = false
  }
}

const handleSubmit = async (event: Event) => {
  if (submitting.value) return
  
  // 获取表单验证结果
  const errors = validateForm(formData)
  if (errors.length > 0) {
    useToast().add({
      title: '表单验证失败',
      description: errors[0].message,
      color: 'red'
    })
    return
  }
  
  submitting.value = true
  try {
    await api.post('/api/database/config/save', formData)
    showModal.value = false
    fetchConfigs()
    useToast().add({
      title: '保存成功',
      color: 'green'
    })
  } catch (error: any) {
    useToast().add({
      title: '保存失败',
      description: error.message,
      color: 'red'
    })
  } finally {
    submitting.value = false
  }
}

const confirmDelete = async () => {
  if (submitting.value || !currentRow.value) return
  
  submitting.value = true
  try {
    await api.delete(`/api/database/config/delete/${currentRow.value.configName}`)
    showDeleteModal.value = false
    fetchConfigs()
    useToast().add({
      title: '删除成功',
      color: 'green'
    })
  } catch (error: any) {
    useToast().add({
      title: '删除失败',
      description: error.message,
      color: 'red'
    })
  } finally {
    submitting.value = false
  }
}

// 监听URL变化，自动识别数据库类型和驱动
watch(() => formData.url, async (newUrl) => {
  if (newUrl) {
    try {
      // 分析数据库类型和驱动
      if (newUrl.includes('jdbc:mysql')) {
        formData.type = 'MySQL'
        formData.driverClassName = 'com.mysql.cj.jdbc.Driver'
      } else if (newUrl.includes('jdbc:postgresql')) {
        formData.type = 'PostgreSQL'
        formData.driverClassName = 'org.postgresql.Driver'
      } else if (newUrl.includes('jdbc:oracle')) {
        formData.type = 'Oracle'
        formData.driverClassName = 'oracle.jdbc.OracleDriver'
      } else if (newUrl.includes('jdbc:sqlserver')) {
        formData.type = 'SQL Server'
        formData.driverClassName = 'com.microsoft.sqlserver.jdbc.SQLServerDriver'
      } else if (newUrl.includes('jdbc:h2')) {
        formData.type = 'H2'
        formData.driverClassName = 'org.h2.Driver'
      } else if (newUrl.includes('jdbc:sqlite')) {
        formData.type = 'SQLite3'
        formData.driverClassName = 'org.sqlite.JDBC'
      } else {
        formData.type = ''
        formData.driverClassName = ''
        useToast().add({
          title: '不支持的数据库类型',
          description: '支持的数据库类型：MySQL、PostgreSQL、Oracle、SQL Server、H2、SQLite3',
          color: 'red'
        })
      }
    } catch (error: any) {
      console.error('URL解析失败:', error)
      formData.type = ''
      formData.driverClassName = ''
    }
  } else {
    formData.type = ''
    formData.driverClassName = ''
  }
})

// 监听搜索表单变化
watch(
  () => searchForm,
  () => {
    filterData()
  },
  { deep: true }
)

// 获取数据库类型对应的颜色
const getDbTypeColor = (type: string): BadgeColor => {
  const colorMap: Record<string, BadgeColor> = {
    'MySQL': 'orange',
    'PostgreSQL': 'blue',
    'Oracle': 'red',
    'SQL Server': 'purple',
    'H2': 'green',
    'SQLite3': 'gray'
  }
  return colorMap[type] || 'gray'
}

// 获取操作菜单项（用于移动端）
const getActionItems = (row: DbConfig): DropdownItem[][] => [[
  {
    label: '查看',
    icon: 'i-heroicons-eye',
    click: () => handleView(row),
    class: 'text-primary-500'
  },
  {
    label: '编辑',
    icon: 'i-heroicons-pencil-square',
    click: () => handleEdit(row),
    class: 'text-green-500'
  },
  {
    label: '复制',
    icon: 'i-heroicons-document-duplicate',
    click: () => handleCopy(row),
    class: 'text-blue-500'
  },
  {
    label: '删除',
    icon: 'i-heroicons-trash',
    click: () => handleDelete(row),
    class: 'text-red-500'
  }
]]

// 格式化日期时间
const formatDateTime = (dateStr?: string) => {
  if (!dateStr) return '-'
  try {
    return format(new Date(dateStr), 'yyyy-MM-dd HH:mm:ss')
  } catch {
    return '-'
  }
}

// 添加查看处理方法
const handleView = (row: DbConfig) => {
  Object.assign(viewData, row)
  showViewModal.value = true
}

onMounted(() => {
  fetchConfigs()
})
</script> 