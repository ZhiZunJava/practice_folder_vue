<template>
  <PageContainer 
    title="分类管理" 
    description="管理商品分类，支持多级分类结构"
  >
    <template #extra>
      <el-button type="primary" @click="handleAdd" class="add-btn">
        <el-icon><Plus /></el-icon>
        添加分类
      </el-button>
    </template>

    <!-- 工具栏 -->
    <div class="admin-toolbar">
      <div class="admin-toolbar-left">
        <el-input
          v-model="searchText"
          placeholder="搜索分类名称"
          style="width: 250px"
          clearable
          prefix-icon="Search"
          @input="handleSearch"
        />
        <el-button @click="handleExpandAll">
          <el-icon><Expand /></el-icon>
          展开全部
        </el-button>
        <el-button @click="handleCollapseAll">
          <el-icon><Fold /></el-icon>
          收起全部
        </el-button>
      </div>
      
      <div class="admin-toolbar-right">
        <el-button @click="loadCategories">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <!-- 分类树表格 -->
    <div class="table-container">
      <el-table
        ref="tableRef"
        :data="filteredCategoryTree"
        style="width: 100%;"
        row-key="id"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        :default-expand-all="expandAll"
        v-loading="loading"
      >
        <el-table-column prop="name" label="分类名称" min-width="200">
          <template #default="scope">
            <div class="category-name">
              <el-image
                v-if="scope.row.icon"
                :src="scope.row.icon"
                class="category-icon"
                fit="cover"
              />
              <span class="name-text">{{ scope.row.name }}</span>
              <el-tag 
                v-if="scope.row.level === 0" 
                size="small" 
                type="primary"
                class="level-tag"
              >
                顶级
              </el-tag>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="sort" label="排序" width="100" align="center">
          <template #default="scope">
            <el-tag type="info" size="small">{{ scope.row.sort }}</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="productCount" label="商品数量" width="120" align="center">
          <template #default="scope">
            <el-link type="primary" @click="handleViewProducts(scope.row)">
              {{ scope.row.productCount || 0 }} 个
            </el-link>
          </template>
        </el-table-column>
        
        <el-table-column prop="status" label="状态" width="120" align="center">
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              :loading="scope.row.statusLoading"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        
        <el-table-column prop="createTime" label="创建时间" width="180" align="center">
          <template #default="scope">
            <div class="time-info">
              <div>{{ formatDateTime(scope.row.createTime) }}</div>
              <div class="time-ago">{{ getTimeAgo(scope.row.createTime) }}</div>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="240" align="center" fixed="right">
          <template #default="scope">
            <div class="action-buttons">
              <el-button
                type="primary"
                size="small"
                @click="handleEdit(scope.row)"
                plain
              >
                <el-icon><Edit /></el-icon>
              </el-button>
              
              <el-button
                type="success"
                size="small"
                @click="handleAddChild(scope.row)"
                plain
              >
                <el-icon><Plus /></el-icon>
              </el-button>
              
              <el-button
                type="warning"
                size="small"
                @click="handleMove(scope.row)"
                plain
              >
                <el-icon><Sort /></el-icon>
              </el-button>
              
              <el-popconfirm
                title="确定删除此分类吗？删除后无法恢复！"
                confirm-button-text="确定删除"
                cancel-button-text="取消"
                confirm-button-type="danger"
                @confirm="handleDelete(scope.row)"
              >
                <template #reference>
                  <el-button 
                    type="danger" 
                    size="small"
                    plain
                    :disabled="scope.row.children && scope.row.children.length > 0"
                  >
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </template>
              </el-popconfirm>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 添加/编辑分类对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        class="category-form"
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="分类名称" prop="name">
              <el-input 
                v-model="form.name" 
                placeholder="请输入分类名称"
                clearable
                maxlength="50"
                show-word-limit
              />
            </el-form-item>
          </el-col>
          
          <el-col :span="24">
            <el-form-item label="父分类" prop="parentId">
              <el-cascader
                v-model="parentPath"
                :options="categoryOptions"
                :props="cascaderProps"
                placeholder="请选择父分类，不选择为顶级分类"
                style="width: 100%"
                clearable
                @change="handleParentChange"
              />
            </el-form-item>
          </el-col>
          
          <el-col :span="12">
            <el-form-item label="分类图标" prop="icon">
              <div class="upload-container">
                <el-upload
                  :action="uploadUrl"
                  :headers="uploadHeaders"
                  :show-file-list="false"
                  :on-success="handleIconSuccess"
                  :before-upload="beforeUpload"
                  accept="image/*"
                  class="icon-upload"
                >
                  <div class="upload-area">
                    <el-image
                      v-if="form.icon"
                      :src="form.icon"
                      class="uploaded-icon"
                      fit="cover"
                    />
                    <div v-else class="upload-placeholder">
                      <el-icon size="24"><Picture /></el-icon>
                      <div>上传图标</div>
                    </div>
                  </div>
                </el-upload>
                <div class="upload-tips">
                  <p>建议尺寸：64x64px</p>
                  <p>支持 JPG、PNG 格式</p>
                </div>
              </div>
            </el-form-item>
          </el-col>
          
          <el-col :span="12">
            <el-form-item label="排序值" prop="sort">
              <el-input-number
                v-model="form.sort"
                :min="0"
                :max="9999"
                placeholder="数值越小越靠前"
                style="width: 100%"
              />
            </el-form-item>
            
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          
          <el-col :span="24">
            <el-form-item label="分类描述" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                :rows="3"
                placeholder="请输入分类描述（可选）"
                maxlength="200"
                show-word-limit
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            {{ isEdit ? '更新' : '创建' }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 移动分类对话框 -->
    <el-dialog
      v-model="moveDialogVisible"
      title="移动分类"
      width="500px"
    >
      <el-form label-width="100px">
        <el-form-item label="当前分类">
          <el-input :value="currentMoveCategory?.name" disabled />
        </el-form-item>
        <el-form-item label="目标父分类">
          <el-cascader
            v-model="targetParentPath"
            :options="categoryOptions"
            :props="cascaderProps"
            placeholder="选择目标父分类"
            style="width: 100%"
            clearable
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="moveDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleMoveConfirm">确定移动</el-button>
        </div>
      </template>
    </el-dialog>
  </PageContainer>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { categoryApi, uploadApi } from '@/api'

// 响应式数据
const categoryTree = ref([])
const filteredCategoryTree = ref([])
const dialogVisible = ref(false)
const moveDialogVisible = ref(false)
const isEdit = ref(false)
const loading = ref(false)
const submitLoading = ref(false)
const expandAll = ref(false)
const searchText = ref('')
const formRef = ref()
const tableRef = ref()
const currentMoveCategory = ref(null)
const parentPath = ref([])
const targetParentPath = ref([])

// 表单数据
const form = reactive({
  id: null,
  name: '',
  parentId: 0,
  icon: '',
  sort: 0,
  status: 1,
  description: ''
})

// 对话框标题
const dialogTitle = computed(() => {
  if (isEdit.value) {
    return '编辑分类'
  }
  return form.parentId > 0 ? '添加子分类' : '添加分类'
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入分类名称', trigger: 'blur' },
    { min: 1, max: 50, message: '分类名称长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  sort: [
    { type: 'number', message: '排序值必须为数字', trigger: 'blur' }
  ]
}

// 上传配置
const uploadUrl = '/api/admin/upload/image'
const uploadHeaders = computed(() => ({
  Authorization: `Bearer ${localStorage.getItem('adminToken')}`
}))

// 级联选择器配置
const cascaderProps = {
  value: 'id',
  label: 'name',
  children: 'children',
  checkStrictly: true,
  emitPath: false
}

// 分类选项（用于级联选择器）
const categoryOptions = computed(() => {
  const buildOptions = (categories) => {
    return categories.map(category => ({
      id: category.id,
      name: category.name,
      children: category.children && category.children.length > 0 ? buildOptions(category.children) : undefined
    }))
  }
  return buildOptions(categoryTree.value)
})

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString()
}

// 获取时间距离现在的描述
const getTimeAgo = (dateTime) => {
  if (!dateTime) return ''
  const now = new Date()
  const date = new Date(dateTime)
  const diff = now - date
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))
  if (days === 0) return '今天'
  if (days === 1) return '昨天'
  if (days < 30) return `${days}天前`
  return ''
}

// 搜索功能
const handleSearch = () => {
  if (!searchText.value.trim()) {
    filteredCategoryTree.value = categoryTree.value
    return
  }
  
  const filterTree = (categories) => {
    return categories.filter(category => {
      const matchName = category.name.toLowerCase().includes(searchText.value.toLowerCase())
      const hasMatchChildren = category.children && filterTree(category.children).length > 0
      
      if (matchName || hasMatchChildren) {
        return {
          ...category,
          children: hasMatchChildren ? filterTree(category.children) : category.children
        }
      }
      return false
    })
  }
  
  filteredCategoryTree.value = filterTree(categoryTree.value)
}

// 展开全部
const handleExpandAll = () => {
  expandAll.value = true
  if (tableRef.value) {
    const expandRows = (data) => {
      data.forEach(row => {
        tableRef.value.toggleRowExpansion(row, true)
        if (row.children && row.children.length > 0) {
          expandRows(row.children)
        }
      })
    }
    expandRows(filteredCategoryTree.value)
  }
}

// 收起全部
const handleCollapseAll = () => {
  expandAll.value = false
  if (tableRef.value) {
    const collapseRows = (data) => {
      data.forEach(row => {
        tableRef.value.toggleRowExpansion(row, false)
        if (row.children && row.children.length > 0) {
          collapseRows(row.children)
        }
      })
    }
    collapseRows(filteredCategoryTree.value)
  }
}

// 查看商品
const handleViewProducts = (category) => {
  ElMessage.info(`查看分类"${category.name}"下的商品`)
  // 这里可以跳转到商品列表页面，并传递分类ID作为筛选条件
}

// 处理父分类变化
const handleParentChange = (value) => {
  form.parentId = value || 0
}

// 添加分类
const handleAdd = () => {
  resetForm()
  isEdit.value = false
  dialogVisible.value = true
}

// 添加子分类
const handleAddChild = (row) => {
  resetForm()
  form.parentId = row.id
  parentPath.value = [row.id]
  isEdit.value = false
  dialogVisible.value = true
}

// 编辑分类
const handleEdit = (row) => {
  resetForm()
  Object.assign(form, { ...row })
  
  // 设置父分类路径
  if (row.parentId > 0) {
    parentPath.value = [row.parentId]
  } else {
    parentPath.value = []
  }
  
  isEdit.value = true
  dialogVisible.value = true
}

// 移动分类
const handleMove = (row) => {
  currentMoveCategory.value = row
  targetParentPath.value = row.parentId > 0 ? [row.parentId] : []
  moveDialogVisible.value = true
}

// 确认移动
const handleMoveConfirm = async () => {
  try {
    const targetParentId = targetParentPath.value[targetParentPath.value.length - 1] || 0
    
    await categoryApi.update(currentMoveCategory.value.id, {
      ...currentMoveCategory.value,
      parentId: targetParentId
    })
    
    ElMessage.success('分类移动成功')
    moveDialogVisible.value = false
    loadCategories()
  } catch (error) {
    ElMessage.error('移动失败：' + error.message)
  }
}

// 删除分类
const handleDelete = async (row) => {
  try {
    await categoryApi.delete(row.id)
    ElMessage.success('删除成功')
    loadCategories()
  } catch (error) {
    ElMessage.error('删除失败：' + error.message)
  }
}

// 状态变更
const handleStatusChange = async (row) => {
  row.statusLoading = true
  try {
    await categoryApi.update(row.id, { ...row, status: row.status })
    ElMessage.success('状态更新成功')
  } catch (error) {
    ElMessage.error('状态更新失败：' + error.message)
    // 回滚状态
    row.status = row.status === 1 ? 0 : 1
  } finally {
    row.statusLoading = false
  }
}

// 图标上传成功
const handleIconSuccess = (response) => {
  if (response.code === 200) {
    form.icon = response.data.url
    ElMessage.success('图标上传成功')
  } else {
    ElMessage.error(response.message || '上传失败')
  }
}

// 上传前验证
const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (isEdit.value) {
      await categoryApi.update(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await categoryApi.add(form)
      ElMessage.success('添加成功')
    }
    
    dialogVisible.value = false
    loadCategories()
  } catch (error) {
    if (error.message) {
      ElMessage.error('操作失败：' + error.message)
    }
  } finally {
    submitLoading.value = false
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    id: null,
    name: '',
    parentId: 0,
    icon: '',
    sort: 0,
    status: 1,
    description: ''
  })
  parentPath.value = []
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// 加载分类数据
const loadCategories = async () => {
  loading.value = true
  try {
    const response = await categoryApi.getTree()
    categoryTree.value = response.data || []
    filteredCategoryTree.value = categoryTree.value
  } catch (error) {
    ElMessage.error('加载分类数据失败：' + error.message)
    categoryTree.value = []
    filteredCategoryTree.value = []
  } finally {
    loading.value = false
  }
}

// 初始化
onMounted(() => {
  loadCategories()
})
</script>

<style scoped>
.add-btn {
  box-shadow: 0 2px 4px rgba(24, 144, 255, 0.3);
}

.table-container {
  background: white;
  border-radius: 8px;
  box-shadow: var(--shadow-light);
  overflow: hidden;
}

.category-name {
  display: flex;
  align-items: center;
  gap: 8px;
}

.category-icon {
  width: 32px;
  height: 32px;
  border-radius: 4px;
  border: 1px solid var(--border-lighter);
}

.name-text {
  font-weight: 500;
}

.level-tag {
  margin-left: 8px;
}

.time-info {
  text-align: center;
}

.time-ago {
  font-size: 12px;
  color: var(--text-tertiary);
  margin-top: 2px;
}

.action-buttons {
  display: flex;
  gap: 4px;
  justify-content: center;
}

.category-form {
  margin-top: 16px;
}

.upload-container {
  display: flex;
  gap: 16px;
  align-items: flex-start;
}

.icon-upload {
  flex-shrink: 0;
}

.upload-area {
  width: 80px;
  height: 80px;
  border: 2px dashed var(--border-color);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.upload-area:hover {
  border-color: var(--primary-color);
  background: rgba(24, 144, 255, 0.05);
}

.uploaded-icon {
  width: 76px;
  height: 76px;
  border-radius: 6px;
}

.upload-placeholder {
  text-align: center;
  color: var(--text-tertiary);
}

.upload-placeholder div {
  font-size: 12px;
  margin-top: 4px;
}

.upload-tips {
  flex: 1;
}

.upload-tips p {
  margin: 0 0 4px 0;
  font-size: 12px;
  color: var(--text-tertiary);
}

.dialog-footer {
  text-align: right;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .action-buttons {
    flex-direction: column;
    gap: 2px;
  }
  
  .upload-container {
    flex-direction: column;
    align-items: center;
  }
  
  :deep(.el-table .el-table__cell) {
    padding: 8px 4px;
  }
}
</style> 