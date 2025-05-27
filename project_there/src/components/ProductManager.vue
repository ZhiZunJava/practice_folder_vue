<template>
  <div class="product-manager">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>商品管理</span>
          <el-button type="primary" @click="handleAdd">添加商品</el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-input
          v-model="searchQuery"
          placeholder="搜索商品名称或分类"
          class="search-input"
          clearable
          @input="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>

      <!-- 商品列表表格 -->
      <el-table
        :data="filteredProducts"
        style="width: 100%"
        stripe
        border
        v-loading="loading"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="商品名称" min-width="150" />
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="price" label="价格" width="100">
          <template #default="{ row }">
            <span class="price">¥{{ row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" />
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === '上架' ? 'success' : 'danger'">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 添加/编辑商品对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加商品' : '编辑商品'"
      width="600px"
      :before-close="handleClose"
    >
      <el-form
        ref="productForm"
        :model="productFormData"
        :rules="formRules"
        label-width="80px"
      >
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="productFormData.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="productFormData.category" placeholder="请选择分类" style="width: 100%">
            <el-option label="电子产品" value="电子产品" />
            <el-option label="服装" value="服装" />
            <el-option label="食品" value="食品" />
            <el-option label="图书" value="图书" />
            <el-option label="家居用品" value="家居用品" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number
            v-model="productFormData.price"
            :min="0"
            :precision="2"
            controls-position="right"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input-number
            v-model="productFormData.stock"
            :min="0"
            controls-position="right"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="productFormData.status">
            <el-radio label="上架">上架</el-radio>
            <el-radio label="下架">下架</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="productFormData.description"
            type="textarea"
            :rows="4"
            placeholder="请输入商品描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            {{ dialogType === 'add' ? '添加' : '更新' }}
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogType = ref('add') // 'add' 或 'edit'
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const productForm = ref(null)

// 商品列表数据
const products = ref([
  {
    id: 1,
    name: 'iPhone 14 Pro',
    category: '电子产品',
    price: 7999.00,
    stock: 50,
    status: '上架',
    description: '最新款iPhone，配备A16仿生芯片'
  },
  {
    id: 2,
    name: '阿迪达斯运动鞋',
    category: '服装',
    price: 699.00,
    stock: 100,
    status: '上架',
    description: '舒适透气的运动鞋，适合日常运动'
  },
  {
    id: 3,
    name: '有机苹果',
    category: '食品',
    price: 25.00,
    stock: 200,
    status: '上架',
    description: '新鲜有机苹果，富含维生素'
  },
  {
    id: 4,
    name: 'Vue.js实战指南',
    category: '图书',
    price: 89.00,
    stock: 30,
    status: '下架',
    description: 'Vue.js从入门到精通的实战教程'
  },
  {
    id: 5,
    name: '智能台灯',
    category: '家居用品',
    price: 299.00,
    stock: 75,
    status: '上架',
    description: '可调光护眼台灯，支持手机App控制'
  }
])

// 表单数据
const productFormData = reactive({
  id: null,
  name: '',
  category: '',
  price: 0,
  stock: 0,
  status: '上架',
  description: ''
})

// 表单验证规则
const formRules = {
  name: [
    { required: true, message: '请输入商品名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请选择商品分类', trigger: 'change' }
  ],
  price: [
    { required: true, message: '请输入商品价格', trigger: 'blur' },
    { type: 'number', min: 0, message: '价格必须大于等于0', trigger: 'blur' }
  ],
  stock: [
    { required: true, message: '请输入库存数量', trigger: 'blur' },
    { type: 'number', min: 0, message: '库存必须大于等于0', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择商品状态', trigger: 'change' }
  ]
}

// 计算属性
const filteredProducts = computed(() => {
  if (!searchQuery.value) {
    return products.value
  }
  return products.value.filter(product => 
    product.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
    product.category.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
})

const total = computed(() => filteredProducts.value.length)

// 生成新ID
const generateId = () => {
  return Math.max(...products.value.map(p => p.id), 0) + 1
}

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
}

const handleCurrentChange = (val) => {
  currentPage.value = val
}

// 添加商品
const handleAdd = () => {
  dialogType.value = 'add'
  resetForm()
  dialogVisible.value = true
}

// 编辑商品
const handleEdit = (row) => {
  dialogType.value = 'edit'
  Object.assign(productFormData, { ...row })
  dialogVisible.value = true
}

// 删除商品
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除商品 "${row.name}" 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    
    const index = products.value.findIndex(p => p.id === row.id)
    if (index > -1) {
      products.value.splice(index, 1)
      ElMessage.success('删除成功')
    }
  } catch {
    ElMessage.info('已取消删除')
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!productForm.value) return
  
  try {
    await productForm.value.validate()
    submitLoading.value = true
    
    // 模拟API调用延迟
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    if (dialogType.value === 'add') {
      // 添加商品
      const newProduct = {
        ...productFormData,
        id: generateId()
      }
      products.value.push(newProduct)
      ElMessage.success('添加成功')
    } else {
      // 编辑商品
      const index = products.value.findIndex(p => p.id === productFormData.id)
      if (index > -1) {
        products.value[index] = { ...productFormData }
        ElMessage.success('更新成功')
      }
    }
    
    handleClose()
  } catch (error) {
    console.error('表单验证失败:', error)
  } finally {
    submitLoading.value = false
  }
}

// 关闭对话框
const handleClose = () => {
  dialogVisible.value = false
  resetForm()
}

// 重置表单
const resetForm = () => {
  Object.assign(productFormData, {
    id: null,
    name: '',
    category: '',
    price: 0,
    stock: 0,
    status: '上架',
    description: ''
  })
  if (productForm.value) {
    productForm.value.clearValidate()
  }
}

// 组件挂载时的初始化
onMounted(() => {
  // 这里可以添加初始化逻辑，比如从API获取商品数据
  console.log('商品管理组件已挂载')
})
</script>

<style scoped>
.product-manager {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  margin-bottom: 20px;
}

.search-input {
  width: 300px;
}

.price {
  color: #e74c3c;
  font-weight: bold;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .product-manager {
    padding: 10px;
  }
  
  .search-input {
    width: 100%;
  }
  
  .card-header {
    flex-direction: column;
    gap: 10px;
    align-items: stretch;
  }
}
</style> 