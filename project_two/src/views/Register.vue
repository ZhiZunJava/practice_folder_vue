<template>
  <div class="container">
    <div class="form-wrapper">
      <h2 class="title">系统注册</h2>
      
      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="rules"
        label-width="100px"
        class="register-form"
      >
        <el-form-item label="账号" prop="username" required>
          <el-input
            v-model="registerForm.username"
            placeholder="请输入用户名"
          />
        </el-form-item>
        
        <el-form-item label="密码" prop="password" required>
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword" required>
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请确认密码"
            show-password
          />
        </el-form-item>
        
        <el-button 
          type="primary" 
          class="register-btn"
          @click="handleRegister"
          :loading="loading"
        >
          注册
        </el-button>
        
        <div class="login-link">
          <router-link to="/login">已有账号？去登录</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const registerFormRef = ref()
const loading = ref(false)

// 表单数据
const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: ''
})

// 自定义验证规则
const validateUsername = (rule, value, callback) => {
  if (!value) {
    return callback(new Error('用户名不能为空'))
  }
  const usernameRegex = /^[a-zA-Z][a-zA-Z0-9_-]{3,31}$/
  if (!usernameRegex.test(value)) {
    return callback(new Error('用户名格式不正确，需要4-32位字符'))
  }
  callback()
}

const validatePassword = (rule, value, callback) => {
  if (!value) {
    return callback(new Error('密码不能为空'))
  }
  if (value.length < 6) {
    return callback(new Error('密码至少6位'))
  }
  callback()
}

const validateConfirmPassword = (rule, value, callback) => {
  if (!value) {
    return callback(new Error('确认密码不能为空'))
  }
  if (value !== registerForm.password) {
    return callback(new Error('两次密码不一致'))
  }
  callback()
}

// 验证规则
const rules = {
  username: [{ validator: validateUsername, trigger: 'blur' }],
  password: [{ validator: validatePassword, trigger: 'blur' }],
  confirmPassword: [{ validator: validateConfirmPassword, trigger: 'blur' }]
}

// 模拟注册接口
const mockRegisterApi = (userData) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      console.log('注册数据:', userData)
      resolve({ success: true, message: '注册成功' })
    }, 1000)
  })
}

// 处理注册
const handleRegister = () => {
  registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await mockRegisterApi({
          username: registerForm.username,
          password: registerForm.password
        })
        
        await ElMessageBox.alert('注册成功！', '提示', {
          confirmButtonText: '确定',
          type: 'success'
        })
        
        // 跳转到登录页面
        router.push('/login')
      } catch (error) {
        ElMessage.error('注册失败，请重试')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.container {
  width: 100vw;
  height: 100vh;
  background: #f5f5f5;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  margin: 0;
  overflow: hidden;
}

.form-wrapper {
  background: white;
  padding: 40px;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

.title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 20px;
  font-weight: 500;
}

.register-form {
  margin-top: 20px;
}

.register-form :deep(.el-form-item) {
  margin-bottom: 20px;
}

.register-btn {
  width: 100%;
  height: 40px;
  font-size: 14px;
  margin-top: 20px;
  background: #409eff;
  border: none;
}

.register-btn:hover {
  background: #66b1ff;
}

.login-link {
  text-align: center;
  margin-top: 20px;
}

.login-link a {
  color: #409eff;
  text-decoration: none;
  font-size: 14px;
}

.login-link a:hover {
  text-decoration: underline;
}
</style> 