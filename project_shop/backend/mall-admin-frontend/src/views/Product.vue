<template>
  <div>
    <!-- 搜索表单 -->
    <el-card class="mb-20">
      <el-form :model="queryParams" ref="queryRef" :inline="true">
        <el-form-item label="商品名称" prop="productName">
          <el-input
            v-model="queryParams.productName"
            placeholder="请输入商品名称"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="商品分类" prop="categoryId">
          <el-select v-model="queryParams.categoryId" placeholder="请选择分类" clearable style="width: 200px">
            <el-option
              v-for="category in categoryOptions"
              :key="category.categoryId"
              :label="category.categoryName"
              :value="category.categoryId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="商品状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 200px">
            <el-option label="正常" value="0" />
            <el-option label="停用" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="resetQuery">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <div class="table-actions">
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增商品
      </el-button>
    </div>

    <!-- 商品表格 -->
    <el-table v-loading="loading" :data="productList" border>
      <el-table-column prop="productId" label="商品编号" width="100" />
      <el-table-column prop="productName" label="商品名称" width="150" />
      <el-table-column prop="productImage" label="商品图片" width="120">
        <template #default="{ row }">
          <el-image
            v-if="row.productImage"
            :src="row.productImage"
            style="width: 50px; height: 50px;"
            fit="cover"
            :preview-src-list="[row.productImage]"
          />
        </template>
      </el-table-column>
      <el-table-column prop="categoryName" label="商品分类" width="120" />
      <el-table-column prop="currentPrice" label="现价" width="100">
        <template #default="{ row }">
          <span style="color: #f56c6c; font-weight: bold;">¥{{ row.currentPrice }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="80" />
      <el-table-column prop="sales" label="销量" width="80" />
      <el-table-column label="标签" width="120">
        <template #default="{ row }">
          <el-tag v-if="row.isRecommend === '1'" type="danger" size="small">推荐</el-tag>
          <el-tag v-if="row.isNew === '1'" type="success" size="small">新品</el-tag>
          <el-tag v-if="row.isHot === '1'" type="warning" size="small">热销</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === '0' ? 'success' : 'danger'">
            {{ row.status === '0' ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="handleEdit(row)">
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
          <el-button type="danger" link size="small" @click="handleDelete(row)">
            <el-icon><Delete /></el-icon>
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div style="margin-top: 20px; text-align: center;">
      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="getList"
        @current-change="getList"
      />
    </div>

    <!-- 新增/编辑商品对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="商品名称" prop="productName">
              <el-input v-model="form.productName" placeholder="请输入商品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%">
                <el-option
                  v-for="category in categoryOptions"
                  :key="category.categoryId"
                  :label="category.categoryName"
                  :value="category.categoryId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="原价" prop="originalPrice">
              <el-input-number v-model="form.originalPrice" :precision="2" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="现价" prop="currentPrice">
              <el-input-number v-model="form.currentPrice" :precision="2" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="库存" prop="stock">
              <el-input-number v-model="form.stock" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="销量" prop="sales">
              <el-input-number v-model="form.sales" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="商品图片" prop="productImage">
          <el-input v-model="form.productImage" placeholder="请输入图片URL">
            <template #append>
              <el-button @click="selectImage">选择图片</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="商品描述" prop="productDesc">
          <el-input
            v-model="form.productDesc"
            type="textarea"
            :rows="4"
            placeholder="请输入商品描述"
          />
        </el-form-item>
        <el-form-item label="商品标签">
          <el-checkbox v-model="form.isRecommend" true-label="1" false-label="0">推荐</el-checkbox>
          <el-checkbox v-model="form.isNew" true-label="1" false-label="0">新品</el-checkbox>
          <el-checkbox v-model="form.isHot" true-label="1" false-label="0">热销</el-checkbox>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getProductList, addProduct, updateProduct, deleteProduct, getCategoryList } from '@/api/mall'

const loading = ref(false)
const productList = ref([])
const categoryOptions = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref()
const queryRef = ref()

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  productName: '',
  categoryId: '',
  status: ''
})

const form = ref({
  productId: undefined,
  categoryId: '',
  productName: '',
  productDesc: '',
  productImage: '',
  originalPrice: 0,
  currentPrice: 0,
  stock: 0,
  sales: 0,
  isRecommend: '0',
  isNew: '0',
  isHot: '0',
  status: '0'
})

const rules = {
  productName: [
    { required: true, message: '商品名称不能为空', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择商品分类', trigger: 'change' }
  ],
  currentPrice: [
    { required: true, message: '现价不能为空', trigger: 'blur' }
  ]
}

// 模拟商品数据
const mockProductData = [
  {
    productId: 1,
    productName: '蜜柑',
    categoryName: '生鲜果蔬',
    productImage: 'https://img.yzcdn.cn/vant/cat.jpeg',
    originalPrice: 12,
    currentPrice: 10,
    stock: 10,
    sales: 20,
    isRecommend: '1',
    isNew: '0',
    isHot: '0',
    status: '0',
    productDesc: '蜜柑脐橙含多种维生素，味酸甜、性温，对人身有益。'
  },
  {
    productId: 2,
    productName: '蓝莓',
    categoryName: '生鲜果蔬',
    productImage: 'https://img.yzcdn.cn/vant/cat.jpeg',
    originalPrice: 15,
    currentPrice: 10,
    stock: 20,
    sales: 30,
    isRecommend: '0',
    isNew: '1',
    isHot: '0',
    status: '0',
    productDesc: '蓝莓营养价值高，含有丰富的花青素，对眼睛有益。'
  }
]

// 获取商品列表
const getList = async () => {
  loading.value = true
  try {
    const res = await getProductList(queryParams.value)
    productList.value = res.rows || mockProductData
    total.value = res.total || mockProductData.length
  } catch (error) {
    console.error('获取商品列表失败:', error)
    productList.value = mockProductData
    total.value = mockProductData.length
  } finally {
    loading.value = false
  }
}

// 获取分类选项
const getCategoryOptions = async () => {
  try {
    const res = await getCategoryList()
    categoryOptions.value = res.data || [
      { categoryId: 1, categoryName: '生鲜果蔬' },
      { categoryId: 2, categoryName: '休闲零食' }
    ]
  } catch (error) {
    categoryOptions.value = [
      { categoryId: 1, categoryName: '生鲜果蔬' },
      { categoryId: 2, categoryName: '休闲零食' }
    ]
  }
}

// 搜索
const handleQuery = () => {
  queryParams.value.pageNum = 1
  getList()
}

// 重置搜索
const resetQuery = () => {
  queryRef.value.resetFields()
  handleQuery()
}

// 新增商品
const handleAdd = () => {
  resetForm()
  dialogTitle.value = '新增商品'
  dialogVisible.value = true
}

// 编辑商品
const handleEdit = (row) => {
  resetForm()
  form.value = { ...row }
  dialogTitle.value = '编辑商品'
  dialogVisible.value = true
}

// 删除商品
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除商品"${row.productName}"吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteProduct(row.productId)
    ElMessage.success('删除成功')
    getList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 提交表单
const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (form.value.productId) {
          await updateProduct(form.value)
          ElMessage.success('修改成功')
        } else {
          await addProduct(form.value)
          ElMessage.success('新增成功')
        }
        dialogVisible.value = false
        getList()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
  })
}

// 重置表单
const resetForm = () => {
  form.value = {
    productId: undefined,
    categoryId: '',
    productName: '',
    productDesc: '',
    productImage: '',
    originalPrice: 0,
    currentPrice: 0,
    stock: 0,
    sales: 0,
    isRecommend: '0',
    isNew: '0',
    isHot: '0',
    status: '0'
  }
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

// 选择图片（模拟）
const selectImage = () => {
  ElMessage.info('请输入图片URL或上传图片功能待开发')
}

onMounted(() => {
  getList()
  getCategoryOptions()
})
</script>

<style scoped>
.table-actions {
  margin-bottom: 20px;
}

.el-checkbox {
  margin-right: 20px;
}
</style> 