<template>
  <UContainer>
    <UCard class="min-h-screen bg-gradient-to-br from-gray-50 to-blue-50">
      <!-- 头部区域 -->
      <template #header>
        <div class="flex justify-between items-center">
          <div>
            <h2 class="text-3xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">批次管理</h2>
            <p class="mt-2 text-gray-600">管理产品批次信息</p>
          </div>
          <UButton
            icon="i-heroicons-plus"
            color="primary"
            variant="solid"
            @click="handleAdd"
            class="transform transition-all duration-200 hover:scale-105"
          >
            新增批次
          </UButton>
        </div>
      </template>

      <!-- 搜索筛选 -->
      <div class="mb-8">
        <UForm :state="searchForm">
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <UFormGroup>
              <UInput
                v-model="searchForm.batchNo"
                placeholder="搜索批次号"
                icon="i-heroicons-magnifying-glass"
                trailing
                :trailing-icon="searchForm.batchNo ? 'i-heroicons-x-mark' : ''"
                @trailing-click="searchForm.batchNo = ''"
                color="primary"
              />
            </UFormGroup>
            <UFormGroup label="">
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
            <UFormGroup label="">
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

      <!-- 批次列表 -->
      <UTable
        :rows="filteredBatches"
        :columns="columns"
        :loading="loading"
        :empty-state="{ icon: 'i-heroicons-list-bullet', label: '暂无数据' }"
      >
        <template #actions-data="{ row }">
          <UButtonGroup>
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
    </UCard>

    <!-- 批次表单对话框 -->
    <UModal v-model="showDialog" :ui="{ width: 'md' }">
      <UCard :ui="{ divide: 'divide-y divide-gray-100 dark:divide-gray-800' }">
        <template #header>
          <div class="flex items-center justify-between">
            <h3 class="text-xl font-bold text-gray-900">{{ isEdit ? '编辑批次' : '新增批次' }}</h3>
            <UButton
              icon="i-heroicons-x-mark"
              color="gray"
              variant="ghost"
              @click="showDialog = false"
            />
          </div>
        </template>

        <UForm :state="form" class="space-y-6 py-4" @submit="handleSubmit">
          <UFormGroup label="批次号" required>
            <UInput
              v-model="form.batchNo"
              :disabled="isEdit"
              color="primary"
            />
          </UFormGroup>
          <UFormGroup label="开始日期" required>
            <DatePicker
              v-model="form.startDate"
              :max-date="form.endDate"
              placeholder="选择开始日期"
              color="primary"
            />
          </UFormGroup>
          <UFormGroup label="结束日期" required>
            <DatePicker
              v-model="form.endDate"
              :min-date="form.startDate"
              placeholder="选择结束日期"
              color="primary"
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
  </UContainer>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { format } from 'date-fns'
import { useApi } from '~/composables/useApi'
import DatePicker from '~/components/DatePicker.vue'

const api = useApi()
const batches = ref([])
const showDialog = ref(false)
const isEdit = ref(false)
const loading = ref(false)
const submitting = ref(false)

// 搜索表单
const searchForm = ref({
  batchNo: '',
  startDate: '',
  endDate: ''
})

// 编辑表单
const form = ref({
  batchNo: '',
  startDate: '',
  endDate: ''
})

// 表格列定义
const columns = [
  {
    key: 'batchNo',
    label: '批次号',
  },
  {
    key: 'startDate',
    label: '开始日期',
    formatter: (value) => formatDate(value)
  },
  {
    key: 'endDate',
    label: '结束日期',
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

// 筛选后的批次列表
const filteredBatches = computed(() => {
  return batches.value.filter(batch => {
    const matchBatchNo = batch.batchNo.toLowerCase().includes(searchForm.value.batchNo.toLowerCase())
    
    // 日期筛选逻辑
    const batchStartDate = new Date(batch.startDate).setHours(0, 0, 0, 0)
    const batchEndDate = new Date(batch.endDate).setHours(0, 0, 0, 0)
    const searchStartDate = searchForm.value.startDate ? new Date(searchForm.value.startDate).setHours(0, 0, 0, 0) : null
    const searchEndDate = searchForm.value.endDate ? new Date(searchForm.value.endDate).setHours(0, 0, 0, 0) : null

    const matchStartDate = !searchStartDate || batchStartDate >= searchStartDate
    const matchEndDate = !searchEndDate || batchEndDate <= searchEndDate

    return matchBatchNo && matchStartDate && matchEndDate
  })
})

// 获取批次列表
const fetchBatches = async () => {
  loading.value = true
  try {
    const data = await api.get('/api/batches')
    batches.value = data
  } catch (error) {
    console.error('获取批次列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 格式化日期
const formatDate = (date) => {
  if (!date) return '-'
  return format(new Date(date), 'yyyy-MM-dd')
}

// 新增批次
const handleAdd = () => {
  isEdit.value = false
  form.value = {
    batchNo: '',
    startDate: '',
    endDate: ''
  }
  showDialog.value = true
}

// 编辑批次
const handleEdit = (batch) => {
  isEdit.value = true
  form.value = {
    batchNo: batch.batchNo,
    startDate: batch.startDate,
    endDate: batch.endDate
  }
  showDialog.value = true
}

// 删除批次
const handleDelete = async (batch) => {
  const confirmed = await useConfirmDialog({
    title: '确认删除',
    content: '确定要删除该批次吗？此操作不可恢复。',
    confirmLabel: '删除',
    cancelLabel: '取消',
    confirmButtonProps: {
      color: 'red',
      variant: 'soft'
    }
  })
  
  if (!confirmed) return
  
  try {
    await api.delete(`/api/batches/${batch.batchNo}`)
    await fetchBatches()
    useToast().add({
      title: '删除成功',
      color: 'green'
    })
  } catch (error) {
    console.error('删除批次失败:', error)
    useToast().add({
      title: '删除失败',
      description: error.message,
      color: 'red'
    })
  }
}

// 提交表单
const handleSubmit = async () => {
  // 表单验证
  if (!form.value.batchNo || !form.value.startDate || !form.value.endDate) {
    useToast().add({
      title: '请填写完整信息',
      description: '批次号、开始日期和结束日期都是必填项',
      color: 'red'
    })
    return
  }

  // 日期验证
  const startDate = new Date(form.value.startDate).setHours(0, 0, 0, 0)
  const endDate = new Date(form.value.endDate).setHours(0, 0, 0, 0)
  if (startDate > endDate) {
    useToast().add({
      title: '日期错误',
      description: '结束日期不能早于开始日期',
      color: 'red'
    })
    return
  }

  submitting.value = true
  try {
    const formData = {
      ...form.value,
      startDate: format(new Date(form.value.startDate), 'yyyy-MM-dd'),
      endDate: format(new Date(form.value.endDate), 'yyyy-MM-dd')
    }

    if (isEdit.value) {
      await api.put(`/api/batches/${formData.batchNo}`, formData)
    } else {
      await api.post('/api/batches', formData)
    }
    
    showDialog.value = false
    await fetchBatches()
    useToast().add({
      title: isEdit.value ? '更新成功' : '创建成功',
      color: 'green'
    })
  } catch (error) {
    console.error(isEdit.value ? '更新批次失败:' : '创建批次失败:', error)
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
  fetchBatches()
})
</script>

<style>
.vc-container {
  --vc-font-family: inherit;
  --vc-rounded-lg: 0.75rem;
  --vc-header-title-color: theme('colors.gray.900');
  --vc-header-arrow-color: theme('colors.gray.600');
  --vc-header-arrow-hover-color: theme('colors.gray.900');
  --vc-header-background: theme('colors.white');
  --vc-weekday-color: theme('colors.gray.500');
  --vc-weekday-background: theme('colors.gray.50');
  --vc-day-color: theme('colors.gray.700');
  --vc-day-border-color: theme('colors.gray.100');
  --vc-day-disabled-color: theme('colors.gray.300');
  --vc-day-hover-background: theme('colors.gray.50');
  --vc-day-current-color: theme('colors.indigo.600');
  --vc-day-selected-color: theme('colors.white');
  --vc-day-selected-background: theme('colors.indigo.600');
  --vc-day-selected-hover-background: theme('colors.indigo.700');
  border: 1px solid theme('colors.gray.200');
  border-radius: var(--vc-rounded-lg);
  box-shadow: theme('boxShadow.lg');
}

.vc-header {
  padding: 0.75rem 0.5rem;
  font-weight: 600;
}

.vc-weeks {
  padding: 0.5rem;
}

.vc-day {
  height: 2.5rem;
  transition-property: all;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 200ms;
}

.vc-day:not(.is-selected):hover {
  background-color: theme('colors.gray.50');
  border-radius: var(--vc-rounded-lg);
}

.vc-day.is-selected {
  background: linear-gradient(to right, theme('colors.indigo.600'), theme('colors.purple.600'));
  border-radius: var(--vc-rounded-lg);
  font-weight: 500;
}

.vc-day.is-today {
  font-weight: 600;
  color: theme('colors.indigo.600');
}
</style>