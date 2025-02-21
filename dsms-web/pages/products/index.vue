<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-bold text-gray-900">产品管理</h2>
      <button
        @click="handleAdd"
        class="px-4 py-2 text-sm font-medium text-white bg-indigo-600 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
      >
        新增产品
      </button>
    </div>

    <!-- 产品列表 -->
    <div class="bg-white shadow-md rounded-lg overflow-hidden">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">英文名称</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">中文名称</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">描述</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">创建时间</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr v-for="product in products" :key="product.product_name_en">
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ product.product_name_en }}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ product.product_name_cn }}</td>
            <td class="px-6 py-4 text-sm text-gray-900">{{ product.description }}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ formatDate(product.created_at) }}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
              <button
                @click="handleEdit(product)"
                class="text-indigo-600 hover:text-indigo-900"
              >
                编辑
              </button>
              <button
                @click="handleDelete(product)"
                class="text-red-600 hover:text-red-900"
              >
                删除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 产品表单对话框 -->
    <div v-if="showDialog" class="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center">
      <div class="bg-white rounded-lg p-6 max-w-md w-full">
        <h3 class="text-lg font-medium text-gray-900 mb-4">{{ isEdit ? '编辑产品' : '新增产品' }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">英文名称</label>
              <input
                v-model="form.product_name_en"
                type="text"
                :disabled="isEdit"
                required
                class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">中文名称</label>
              <input
                v-model="form.product_name_cn"
                type="text"
                required
                class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">描述</label>
              <textarea
                v-model="form.description"
                rows="3"
                class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
              ></textarea>
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
import { ref, onMounted } from 'vue'

const products = ref([])
const showDialog = ref(false)
const isEdit = ref(false)
const form = ref({
  product_name_en: '',
  product_name_cn: '',
  description: ''
})

// 获取产品列表
const fetchProducts = async () => {
  try {
    const token = localStorage.getItem('token')
    const response = await fetch('/api/products', {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
    if (!response.ok) throw new Error('获取产品列表失败')
    products.value = await response.json()
  } catch (error) {
    console.error('获取产品列表失败:', error)
  }
}

// 格式化日期
const formatDate = (date) => {
  return new Date(date).toLocaleString()
}

// 新增产品
const handleAdd = () => {
  isEdit.value = false
  form.value = {
    product_name_en: '',
    product_name_cn: '',
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
  if (!confirm('确定要删除该产品吗？')) return
  
  try {
    const token = localStorage.getItem('token')
    const response = await fetch(`/api/products/${product.product_name_en}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
    if (!response.ok) throw new Error('删除产品失败')
    await fetchProducts()
  } catch (error) {
    console.error('删除产品失败:', error)
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    const url = isEdit.value
      ? `/api/products/${form.value.product_name_en}`
      : '/api/products'
    const method = isEdit.value ? 'PUT' : 'POST'
    const token = localStorage.getItem('token')
    
    const response = await fetch(url, {
      method,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(form.value)
    })
    
    if (!response.ok) throw new Error(isEdit.value ? '更新产品失败' : '新增产品失败')
    
    showDialog.value = false
    await fetchProducts()
  } catch (error) {
    console.error(isEdit.value ? '更新产品失败:' : '新增产品失败:', error)
  }
}

onMounted(() => {
  fetchProducts()
})
</script>