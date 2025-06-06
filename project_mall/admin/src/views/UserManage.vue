<template>
  <div class="user-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
        </div>
      </template>

      <!-- 搜索筛选 -->
      <div class="search-box">
        <el-form :model="searchForm" :inline="true">
          <el-form-item label="用户名">
            <el-input
              v-model="searchForm.username"
              placeholder="请输入用户名"
              clearable
            />
          </el-form-item>
          <el-form-item label="手机号">
            <el-input
              v-model="searchForm.phone"
              placeholder="请输入手机号"
              clearable
            />
          </el-form-item>
          <el-form-item label="用户状态">
            <el-select
              v-model="searchForm.status"
              placeholder="请选择状态"
              clearable
            >
              <el-option label="正常" :value="1" />
              <el-option label="禁用" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchUsers">搜索</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 用户列表 -->
      <el-table
        v-loading="loading"
        :data="users"
        style="width: 100%"
        border
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="avatar" label="头像" width="80">
          <template #default="scope">
            <el-avatar
              :src="scope.row.avatar || '/default-avatar.png'"
              :size="40"
            />
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="email" label="邮箱" width="200" />
        <el-table-column prop="phone" label="手机号" width="150" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="scope">
            <span v-if="scope.row.gender === 1">男</span>
            <span v-else-if="scope.row.gender === 2">女</span>
            <span v-else>保密</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="lastLoginTime" label="最后登录" width="180" />
        <el-table-column prop="createTime" label="注册时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="handleView(scope.row)"
            >
              查看
            </el-button>
            <el-button
              type="warning"
              size="small"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 用户详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="用户详情"
      width="600px"
    >
      <div v-if="currentUser" class="user-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="头像">
            <el-avatar
              :src="currentUser.avatar || '/default-avatar.png'"
              :size="60"
            />
          </el-descriptions-item>
          <el-descriptions-item label="用户名">
            {{ currentUser.username }}
          </el-descriptions-item>
          <el-descriptions-item label="昵称">
            {{ currentUser.nickname || '未设置' }}
          </el-descriptions-item>
          <el-descriptions-item label="邮箱">
            {{ currentUser.email || '未设置' }}
          </el-descriptions-item>
          <el-descriptions-item label="手机号">
            {{ currentUser.phone || '未设置' }}
          </el-descriptions-item>
          <el-descriptions-item label="性别">
            <span v-if="currentUser.gender === 1">男</span>
            <span v-else-if="currentUser.gender === 2">女</span>
            <span v-else>保密</span>
          </el-descriptions-item>
          <el-descriptions-item label="生日">
            {{ currentUser.birthday || '未设置' }}
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="currentUser.status === 1 ? 'success' : 'danger'">
              {{ currentUser.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="最后登录">
            {{ currentUser.lastLoginTime || '从未登录' }}
          </el-descriptions-item>
          <el-descriptions-item label="注册时间">
            {{ currentUser.createTime }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- 编辑用户对话框 -->
    <el-dialog
      v-model="editVisible"
      title="编辑用户"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" disabled />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio :label="0">保密</el-radio>
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="form.status"
            :active-value="1"
            :inactive-value="0"
            active-text="正常"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { userApi } from '@/api'

// 响应式数据
const loading = ref(false)
const users = ref([])
const detailVisible = ref(false)
const editVisible = ref(false)
const currentUser = ref(null)
const formRef = ref()

// 搜索表单
const searchForm = reactive({
  username: '',
  phone: '',
  status: ''
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 编辑表单
const form = reactive({
  id: null,
  username: '',
  nickname: '',
  email: '',
  phone: '',
  gender: 0,
  status: 1
})

// 表单验证规则
const rules = {
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

// 加载用户列表
const loadUsers = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      ...searchForm
    }
    
    // 模拟数据
    const mockData = {
      code: 200,
      data: {
        records: [
          {
            id: 1,
            username: 'user001',
            nickname: '张三',
            email: 'zhangsan@example.com',
            phone: '13800138001',
            avatar: 'https://img.yzcdn.cn/vant/cat.jpeg',
            gender: 1,
            birthday: '1990-01-01',
            status: 1,
            lastLoginTime: '2024-11-28 14:30:00',
            createTime: '2024-01-15 10:20:00'
          },
          {
            id: 2,
            username: 'user002',
            nickname: '李四',
            email: 'lisi@example.com',
            phone: '13800138002',
            avatar: null,
            gender: 2,
            birthday: '1992-05-15',
            status: 1,
            lastLoginTime: '2024-11-27 16:45:00',
            createTime: '2024-02-20 14:30:00'
          },
          {
            id: 3,
            username: 'user003',
            nickname: '王五',
            email: 'wangwu@example.com',
            phone: '13800138003',
            avatar: null,
            gender: 1,
            birthday: null,
            status: 0,
            lastLoginTime: '2024-11-20 09:15:00',
            createTime: '2024-03-10 11:45:00'
          }
        ],
        total: 3,
        current: pagination.current,
        size: pagination.size
      }
    }
    
    users.value = mockData.data.records
    pagination.total = mockData.data.total
  } catch (error) {
    console.error('加载用户列表失败:', error)
    ElMessage.error('加载用户列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索用户
const searchUsers = () => {
  pagination.current = 1
  loadUsers()
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    username: '',
    phone: '',
    status: ''
  })
  searchUsers()
}

// 分页大小改变
const handleSizeChange = (val) => {
  pagination.size = val
  loadUsers()
}

// 当前页改变
const handleCurrentChange = (val) => {
  pagination.current = val
  loadUsers()
}

// 查看用户详情
const handleView = (row) => {
  currentUser.value = { ...row }
  detailVisible.value = true
}

// 编辑用户
const handleEdit = (row) => {
  Object.assign(form, row)
  editVisible.value = true
}

// 状态切换
const handleStatusChange = async (row) => {
  try {
    await userApi.updateStatus(row.id, row.status)
    ElMessage.success('状态更新成功')
  } catch (error) {
    console.error('状态更新失败:', error)
    // 恢复原状态
    row.status = row.status === 1 ? 0 : 1
    ElMessage.error('状态更新失败')
  }
}

// 保存编辑
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    // 这里应该调用实际的API
    // await userApi.update(form.id, form)
    
    ElMessage.success('保存成功')
    editVisible.value = false
    loadUsers()
  } catch (error) {
    console.error('保存失败:', error)
  }
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.user-manage {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-box {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.user-detail {
  padding: 20px 0;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 