<template>
  <div>
    <!-- 操作按钮 -->
    <div class="table-actions">
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增分类
      </el-button>
    </div>

    <!-- 分类表格 -->
    <el-table
      v-loading="loading"
      :data="categoryList"
      row-key="categoryId"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      border
    >
      <el-table-column prop="categoryName" label="分类名称" width="200">
        <template #default="{ row }">
          <div style="display: flex; align-items: center;">
            <el-image
              v-if="row.categoryIcon"
              :src="row.categoryIcon"
              style="width: 30px; height: 30px; margin-right: 10px;"
              fit="cover"
            />
            <span>{{ row.categoryName }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="categoryId" label="分类编号" width="100" />
      <el-table-column prop="sortOrder" label="排序" width="80" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === '0' ? 'success' : 'danger'">
            {{ row.status === '0' ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
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

    <!-- 新增/编辑分类对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="上级分类" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="categoryTreeOptions"
            :props="{ value: 'categoryId', label: 'categoryName', children: 'children' }"
            placeholder="选择上级分类"
            check-strictly
            :render-after-expand="false"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="form.categoryName" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="分类图标" prop="categoryIcon">
          <el-input v-model="form.categoryIcon" placeholder="请输入图标URL">
            <template #append>
              <el-button @click="selectImage">选择图片</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="分类描述" prop="categoryDesc">
          <el-input
            v-model="form.categoryDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入分类描述"
          />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" />
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
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getCategoryTree, addCategory, updateCategory, deleteCategory } from '@/api/mall'

const loading = ref(false)
const categoryList = ref([])
const categoryTreeOptions = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref()

const form = ref({
  categoryId: undefined,
  parentId: 0,
  categoryName: '',
  categoryIcon: '',
  categoryDesc: '',
  sortOrder: 0,
  status: '0'
})

const rules = {
  categoryName: [
    { required: true, message: '分类名称不能为空', trigger: 'blur' }
  ],
  sortOrder: [
    { required: true, message: '排序不能为空', trigger: 'blur' }
  ]
}

// 获取分类列表
const fetchCategoryList = async () => {
  loading.value = true
  try {
    const res = await getCategoryTree()
    categoryList.value = res.data || mockCategoryData
    
    // 构建树形选择器数据
    categoryTreeOptions.value = [
      { categoryId: 0, categoryName: '主分类', children: res.data || mockCategoryData }
    ]
  } catch (error) {
    console.error('获取分类列表失败:', error)
    // 使用模拟数据
    categoryList.value = mockCategoryData
    categoryTreeOptions.value = [
      { categoryId: 0, categoryName: '主分类', children: mockCategoryData }
    ]
  } finally {
    loading.value = false
  }
}

// 模拟数据
const mockCategoryData = [
  {
    categoryId: 1,
    categoryName: '潮流女装',
    categoryIcon: '',
    sortOrder: 1,
    status: '0',
    createTime: '2022-12-22 07:00:00',
    children: [
      {
        categoryId: 101,
        categoryName: '羽绒服',
        categoryIcon: '',
        sortOrder: 101,
        status: '0',
        createTime: '2022-12-22 07:00:00'
      },
      {
        categoryId: 102,
        categoryName: '毛呢大衣',
        categoryIcon: '',
        sortOrder: 102,
        status: '0',
        createTime: '2022-12-22 07:00:00'
      }
    ]
  },
  {
    categoryId: 2,
    categoryName: '食品',
    categoryIcon: '',
    sortOrder: 2,
    status: '0',
    createTime: '2022-12-22 07:00:00',
    children: [
      {
        categoryId: 201,
        categoryName: '休闲零食',
        categoryIcon: '',
        sortOrder: 201,
        status: '0',
        createTime: '2022-12-22 07:00:00'
      }
    ]
  }
]

// 新增分类
const handleAdd = () => {
  resetForm()
  dialogTitle.value = '新增分类'
  dialogVisible.value = true
}

// 编辑分类
const handleEdit = (row) => {
  resetForm()
  form.value = { ...row }
  dialogTitle.value = '编辑分类'
  dialogVisible.value = true
}

// 删除分类
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除分类"${row.categoryName}"吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteCategory(row.categoryId)
    ElMessage.success('删除成功')
    fetchCategoryList()
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
        if (form.value.categoryId) {
          await updateCategory(form.value)
          ElMessage.success('修改成功')
        } else {
          await addCategory(form.value)
          ElMessage.success('新增成功')
        }
        dialogVisible.value = false
        fetchCategoryList()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
  })
}

// 重置表单
const resetForm = () => {
  form.value = {
    categoryId: undefined,
    parentId: 0,
    categoryName: '',
    categoryIcon: '',
    categoryDesc: '',
    sortOrder: 0,
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
  fetchCategoryList()
})
</script>

<style scoped>
.table-actions {
  margin-bottom: 20px;
}
</style> 