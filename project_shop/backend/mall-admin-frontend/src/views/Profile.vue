<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <!-- 用户信息卡片 -->
        <el-card class="profile-card">
          <div class="profile-header">
            <el-avatar :size="80" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
            <div class="profile-info">
              <h3>{{ userInfo.userName || 'admin' }}</h3>
              <p class="profile-desc">{{ userInfo.email || 'admin@example.com' }}</p>
            </div>
          </div>
          <el-divider />
          <div class="profile-stats">
            <div class="stat-item">
              <div class="stat-value">{{ userInfo.loginCount || 89 }}</div>
              <div class="stat-label">登录次数</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ formatDate(userInfo.loginDate) || '2025-01-15' }}</div>
              <div class="stat-label">最后登录</div>
            </div>
          </div>
        </el-card>

        <!-- 快捷操作 -->
        <el-card class="mt-20">
          <template #header>
            <span>快捷操作</span>
          </template>
          <div class="quick-actions">
            <el-button type="primary" @click="$router.push('/product')">
              <el-icon><Box /></el-icon>
              商品管理
            </el-button>
            <el-button type="success" @click="$router.push('/category')">
              <el-icon><Grid /></el-icon>
              分类管理
            </el-button>
            <el-button type="warning" @click="$router.push('/dashboard')">
              <el-icon><Odometer /></el-icon>
              数据统计
            </el-button>
          </div>
        </el-card>
      </el-col>

      <el-col :span="16">
        <!-- 个人信息表单 -->
        <el-card>
          <template #header>
            <span>个人信息</span>
          </template>
          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            label-width="100px"
          >
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="用户名" prop="userName">
                  <el-input v-model="form.userName" placeholder="请输入用户名" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="昵称" prop="nickName">
                  <el-input v-model="form.nickName" placeholder="请输入昵称" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="form.email" type="email" placeholder="请输入邮箱" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="手机号" prop="phonenumber">
                  <el-input v-model="form.phonenumber" placeholder="请输入手机号" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="性别" prop="sex">
                  <el-select v-model="form.sex" placeholder="请选择性别" style="width: 100%">
                    <el-option label="男" value="0" />
                    <el-option label="女" value="1" />
                    <el-option label="未知" value="2" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="状态" prop="status">
                  <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                    <el-option label="正常" value="0" />
                    <el-option label="停用" value="1" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                :rows="4"
                placeholder="请输入备注"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm">
                <el-icon><Check /></el-icon>
                保存修改
              </el-button>
              <el-button @click="resetForm">
                <el-icon><Refresh /></el-icon>
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <!-- 修改密码 -->
        <el-card class="mt-20">
          <template #header>
            <span>修改密码</span>
          </template>
          <el-form
            ref="passwordFormRef"
            :model="passwordForm"
            :rules="passwordRules"
            label-width="100px"
          >
            <el-form-item label="旧密码" prop="oldPassword">
              <el-input
                v-model="passwordForm.oldPassword"
                type="password"
                placeholder="请输入旧密码"
                show-password
              />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input
                v-model="passwordForm.newPassword"
                type="password"
                placeholder="请输入新密码"
                show-password
              />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input
                v-model="passwordForm.confirmPassword"
                type="password"
                placeholder="请确认新密码"
                show-password
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitPasswordForm">
                <el-icon><Lock /></el-icon>
                修改密码
              </el-button>
              <el-button @click="resetPasswordForm">
                <el-icon><Refresh /></el-icon>
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Box, Grid, Odometer, Check, Refresh, Lock } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const formRef = ref()
const passwordFormRef = ref()

const userInfo = ref({
  userName: 'admin',
  email: 'admin@example.com',
  loginCount: 89,
  loginDate: '2025-01-15 10:30:00'
})

const form = ref({
  userId: 1,
  userName: 'admin',
  nickName: '管理员',
  email: 'admin@example.com',
  phonenumber: '15888888888',
  sex: '0',
  status: '0',
  remark: '管理员账号'
})

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const rules = {
  userName: [
    { required: true, message: '用户名不能为空', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度必须介于 2 和 20 之间', trigger: 'blur' }
  ],
  nickName: [
    { required: true, message: '昵称不能为空', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '邮箱地址不能为空', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
  ],
  phonenumber: [
    { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

const passwordRules = {
  oldPassword: [
    { required: true, message: '旧密码不能为空', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '新密码不能为空', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度必须介于 6 和 20 之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '确认密码不能为空', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 格式化日期
const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString()
}

// 提交个人信息表单
const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 这里应该调用更新用户信息的API
        // await updateUserProfile(form.value)
        ElMessage.success('个人信息修改成功')
      } catch (error) {
        ElMessage.error('修改失败')
      }
    }
  })
}

// 重置个人信息表单
const resetForm = () => {
  form.value = {
    userId: 1,
    userName: 'admin',
    nickName: '管理员',
    email: 'admin@example.com',
    phonenumber: '15888888888',
    sex: '0',
    status: '0',
    remark: '管理员账号'
  }
}

// 提交修改密码表单
const submitPasswordForm = () => {
  passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 这里应该调用修改密码的API
        // await updatePassword(passwordForm.value)
        ElMessage.success('密码修改成功')
        resetPasswordForm()
      } catch (error) {
        ElMessage.error('密码修改失败')
      }
    }
  })
}

// 重置密码表单
const resetPasswordForm = () => {
  passwordForm.value = {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  }
  if (passwordFormRef.value) {
    passwordFormRef.value.resetFields()
  }
}

onMounted(() => {
  // 获取用户信息
  if (userStore.userInfo) {
    Object.assign(form.value, userStore.userInfo)
    Object.assign(userInfo.value, userStore.userInfo)
  }
})
</script>

<style scoped>
.profile-card {
  .profile-header {
    display: flex;
    align-items: center;
    
    .profile-info {
      margin-left: 20px;
      
      h3 {
        margin: 0 0 8px 0;
        color: #303133;
      }
      
      .profile-desc {
        margin: 0;
        color: #909399;
        font-size: 14px;
      }
    }
  }
  
  .profile-stats {
    display: flex;
    justify-content: space-around;
    
    .stat-item {
      text-align: center;
      
      .stat-value {
        font-size: 20px;
        font-weight: bold;
        color: #409EFF;
        margin-bottom: 5px;
      }
      
      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.quick-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
  
  .el-button {
    justify-content: flex-start;
  }
}

.mt-20 {
  margin-top: 20px;
}
</style> 