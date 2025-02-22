<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-bold text-gray-900">批次管理</h2>
      <button
        @click="handleAdd"
        class="px-4 py-2 text-sm font-medium text-white bg-indigo-600 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 flex items-center"
        title="新增批次"
      >
        <Icon name="material-symbols:add" class="h-5 w-5" />
        <span class="ml-1"></span>
      </button>
    </div>

    <!-- 搜索筛选 -->
    <div class="mb-6 flex space-x-4">
      <div class="flex-1">
        <div class="relative">
          <input
            v-model="searchBatchNo"
            type="text"
            placeholder="请输入批次号"
            class="block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 pr-8"
          />
          <button
            v-if="searchBatchNo"
            @click="searchBatchNo = ''"
            class="absolute inset-y-0 right-0 pr-3 flex items-center text-gray-400 hover:text-gray-600"
          >
            <Icon name="material-symbols:close" class="h-5 w-5" />
          </button>
        </div>
      </div>
      <div class="flex-1">
        <div class="relative">
          <input
            v-model="startDateFilter"
            type="date"
            placeholder="开始日期"
            class="block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
          />
        </div>
      </div>
      <div class="flex-1">
        <div class="relative">
          <input
            v-model="endDateFilter"
            type="date"
            placeholder="结束日期"
            class="block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
          />
        </div>
      </div>
    </div>

    <!-- 批次列表 -->
    <div class="bg-white shadow-md rounded-lg overflow-hidden">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">批次号</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">开始日期</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">结束日期</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">创建时间</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr v-for="batch in filteredBatches" :key="batch.batchNo">
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ batch.batchNo }}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ formatDate(batch.startDate) }}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ formatDate(batch.endDate) }}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ formatDate(batch.createdAt) }}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
              <button
                @click="handleEdit(batch)"
                class="text-indigo-600 hover:text-indigo-900 p-1 rounded-full hover:bg-indigo-50"
                title="编辑"
              >
                <Icon name="material-symbols:edit" class="h-5 w-5" />
              </button>
              <button
                @click="handleDelete(batch)"
                class="text-red-600 hover:text-red-900 p-1 rounded-full hover:bg-red-50"
                title="删除"
              >
                <Icon name="material-symbols:delete" class="h-5 w-5" />
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 批次表单对话框 -->
    <div v-if="showDialog" class="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center">
      <div class="bg-white rounded-lg p-6 max-w-md w-full">
        <h3 class="text-lg font-medium text-gray-900 mb-4">{{ isEdit ? '编辑批次' : '新增批次' }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">批次号</label>
              <input
                v-model="form.batchNo"
                type="text"
                :disabled="isEdit"
                required
                class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">开始日期</label>
              <input
                v-model="form.startDate"
                type="date"
                required
                class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">结束日期</label>
              <input
                v-model="form.endDate"
                type="date"
                required
                class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
              />
            </div>
          </div>
          <div class="mt-6 flex justify-end space-x-3">
            <button
              type="button"
              @click="showDialog = false"
              class="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500"
            >
              取消
            </button>
            <button
              type="submit"
              class="px-4 py-2 text-sm font-medium text-white bg-indigo-600 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              确定
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'

const batches = ref([])
const showDialog = ref(false)
const isEdit = ref(false)
const searchBatchNo = ref('')
const startDateFilter = ref('')
const endDateFilter = ref('')

const form = ref({
  batchNo: '',
  startDate: '',
  endDate: ''
})

// 筛选后的批次列表
const filteredBatches = computed(() => {
  return batches.value.filter(batch => {
    const matchBatchNo = batch.batchNo.toLowerCase().includes(searchBatchNo.value.toLowerCase())
    const matchStartDate = !startDateFilter.value || batch.startDate >= startDateFilter.value
    const matchEndDate = !endDateFilter.value || batch.endDate <= endDateFilter.value
    return matchBatchNo && matchStartDate && matchEndDate
  })
})

// 获取批次列表
const fetchBatches = async () => {
  try {
    const token = localStorage.getItem('token')
    const response = await fetch('/api/batches', {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
    if (!response.ok) throw new Error('获取批次列表失败')
    batches.value = await response.json()
  } catch (error) {
    console.error('获取批次列表失败:', error)
  }
}

// 格式化日期
const formatDate = (date) => {
  return new Date(date).toLocaleDateString()
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
  if (!confirm('确定要删除该批次吗？')) return
  
  try {
    const token = localStorage.getItem('token')
    const response = await fetch(`/api/batches/${batch.batchNo}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
    if (!response.ok) throw new Error('删除批次失败')
    await fetchBatches()
  } catch (error) {
    console.error('删除批次失败:', error)
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    const token = localStorage.getItem('token')
    const url = isEdit.value ? `/api/batches/${form.value.batchNo}` : '/api/batches'
    const method = isEdit.value ? 'PUT' : 'POST'
    
    const response = await fetch(url, {
      method,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(form.value)
    })
    
    if (!response.ok) throw new Error(isEdit.value ? '更新批次失败' : '创建批次失败')
    
    showDialog.value = false
    await fetchBatches()
  } catch (error) {
    console.error(isEdit.value ? '更新批次失败:' : '创建批次失败:', error)
  }
}

onMounted(() => {
  fetchBatches()
})
</script>