<template>
  <div class="profile">
    <el-row :gutter="20">
      <!-- 个人信息卡片 -->
      <el-col :span="8">
        <el-card class="profile-card">
          <template #header>
            <span>个人资料</span>
          </template>
          
          <div class="profile-info">
            <div class="avatar-section">
              <el-upload
                :action="uploadUrl"
                :headers="uploadHeaders"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeUpload"
                accept="image/*"
              >
                <el-avatar
                  :src="adminInfo.avatar || '/default-avatar.png'"
                  :size="80"
                  class="avatar"
                />
                <div class="avatar-overlay">
                  <el-icon><Camera /></el-icon>
                </div>
              </el-upload>
            </div>
            
            <div class="info-item">
              <label>用户名:</label>
              <span>{{ adminInfo.username }}</span>
            </div>
            
            <div class="info-item">
              <label>姓名:</label>
              <span>{{ adminInfo.nickname || '未设置' }}</span>
            </div>
            
            <div class="info-item">
              <label>邮箱:</label>
              <span>{{ adminInfo.email || '未设置' }}</span>
            </div>
            
            <div class="info-item">
              <label>手机号:</label>
              <span>{{ adminInfo.phone || '未设置' }}</span>
            </div>
            
            <div class="info-item">
              <label>最后登录:</label>
              <span>{{ formatDateTime(adminInfo.lastLoginTime) || '从未登录' }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <!-- 编辑表单 -->
      <el-col :span="16">
        <el-card>
          <template #header>
            <span>编辑资料</span>
          </template>
          
          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            label-width="100px"
            style="max-width: 600px"
          >
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" disabled />
            </el-form-item>
            
            <el-form-item label="真实姓名" prop="nickName">
              <el-input
                v-model="form.nickname"
                placeholder="请输入姓名"
                clearable
              />
            </el-form-item>
            
            <el-form-item label="邮箱" prop="email">
              <el-input
                v-model="form.email"
                placeholder="请输入邮箱"
                clearable
              />
            </el-form-item>
            
            <el-form-item label="手机号" prop="phone">
              <el-input
                v-model="form.phone"
                placeholder="请输入手机号"
                clearable
              />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
                保存修改
              </el-button>
              <el-button @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
        
        <!-- 修改密码 -->
        <el-card style="margin-top: 20px;">
          <template #header>
            <span>修改密码</span>
          </template>
          
          <el-form
            ref="passwordFormRef"
            :model="passwordForm"
            :rules="passwordRules"
            label-width="100px"
            style="max-width: 600px"
          >
            <el-form-item label="当前密码" prop="currentPassword">
              <el-input
                v-model="passwordForm.currentPassword"
                type="password"
                placeholder="请输入当前密码"
                show-password
                clearable
              />
            </el-form-item>
            
            <el-form-item label="新密码" prop="newPassword">
              <el-input
                v-model="passwordForm.newPassword"
                type="password"
                placeholder="请输入新密码"
                show-password
                clearable
              />
            </el-form-item>
            
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input
                v-model="passwordForm.confirmPassword"
                type="password"
                placeholder="请再次输入新密码"
                show-password
                clearable
              />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="handlePasswordSubmit" :loading="passwordLoading">
                修改密码
              </el-button>
              <el-button @click="resetPasswordForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { adminApi } from '@/api'

// 响应式数据
const formRef = ref()
const passwordFormRef = ref()
const submitLoading = ref(false)
const passwordLoading = ref(false)

// 管理员信息
const adminInfo = reactive({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  avatar: '',
  lastLoginTime: ''
})

// 基本信息表单
const form = reactive({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  avatar: ''
})

// 密码表单
const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 基本信息验证规则
const rules = {
  nickname: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

// 密码验证规则
const passwordRules = {
  currentPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 上传配置
const uploadUrl = '/api/admin/upload/image'
const uploadHeaders = computed(() => ({
  Authorization: `Bearer ${localStorage.getItem('adminToken')}`
}))

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  return new Date(dateTime).toLocaleString()
}

// 加载管理员信息
const loadAdminInfo = async () => {
  try {
    // 从本地存储获取用户名
    const adminInfoStr = localStorage.getItem('adminInfo')
    if (adminInfoStr) {
      const localInfo = JSON.parse(adminInfoStr)
      Object.assign(adminInfo, localInfo)
      Object.assign(form, localInfo)
    }
    
    // 从服务器获取最新信息
    const res = await adminApi.getInfo()
    Object.assign(adminInfo, res.data)
    Object.assign(form, res.data)
  } catch (error) {
    console.error('加载管理员信息失败:', error)
    ElMessage.error('加载信息失败：' + (error.message || '网络错误'))
  }
}

// 头像上传成功
const handleAvatarSuccess = async (response) => {
  if (response.code === 200) {
    try {
      // 更新表单中的头像URL
      form.avatar = response.data.url
      
      // 立即保存头像到服务器
      await adminApi.updateInfo(form)
      
      // 更新本地显示
      adminInfo.avatar = response.data.url
      localStorage.setItem('adminInfo', JSON.stringify(adminInfo))
      
      ElMessage.success('头像上传并保存成功')
    } catch (error) {
      console.error('保存头像失败:', error)
      ElMessage.error('头像上传成功但保存失败，请手动保存')
    }
  } else {
    ElMessage.error(response.message || '上传失败')
  }
}

// 上传前检查
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

// 提交基本信息
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    const res = await adminApi.updateInfo(form)
    
    // 更新本地信息
    Object.assign(adminInfo, res.data || form)
    localStorage.setItem('adminInfo', JSON.stringify(adminInfo))
    
    ElMessage.success('保存成功')
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败：' + (error.message || '网络错误'))
  } finally {
    submitLoading.value = false
  }
}

// 重置基本信息表单
const resetForm = () => {
  Object.assign(form, adminInfo)
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// 提交密码修改
const handlePasswordSubmit = async () => {
  if (!passwordFormRef.value) return
  
  try {
    await passwordFormRef.value.validate()
    passwordLoading.value = true
    
    await adminApi.changePassword({
      currentPassword: passwordForm.currentPassword,
      newPassword: passwordForm.newPassword
    })
    
    ElMessage.success('密码修改成功')
    resetPasswordForm()
  } catch (error) {
    console.error('密码修改失败:', error)
    ElMessage.error('密码修改失败：' + (error.message || '网络错误'))
  } finally {
    passwordLoading.value = false
  }
}

// 重置密码表单
const resetPasswordForm = () => {
  Object.assign(passwordForm, {
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  })
  
  if (passwordFormRef.value) {
    passwordFormRef.value.clearValidate()
  }
}

onMounted(() => {
  loadAdminInfo()
})
</script>

<style scoped>
.profile {
  padding: 0;
}

.profile-card {
  height: fit-content;
}

.profile-info {
  text-align: center;
}

.avatar-section {
  position: relative;
  display: inline-block;
  margin-bottom: 20px;
}

.avatar {
  cursor: pointer;
  border: 2px solid #dcdfe6;
  transition: border-color 0.3s;
}

.avatar:hover {
  border-color: #409eff;
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  opacity: 0;
  transition: opacity 0.3s;
  cursor: pointer;
  color: white;
}

.avatar-section:hover .avatar-overlay {
  opacity: 1;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.info-item label {
  font-weight: 500;
  color: #606266;
}

.info-item span {
  color: #303133;
}
</style> 