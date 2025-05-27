<template>
  <div class="container">
    <div class="form-wrapper">
      <h2 class="title">系统登录</h2>
      
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="rules"
        label-width="100px"
        class="login-form"
      >
        <el-form-item label="账号" prop="username" required>
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
          />
        </el-form-item>
        
        <el-form-item label="密码" prop="password" required>
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>
        
        <el-button 
          type="primary" 
          class="login-btn"
          @click="handleLogin"
          :loading="loading"
        >
          登录
        </el-button>
        
        <div class="register-link">
          <router-link to="/register">还没有账号？去注册</router-link>
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
const loginFormRef = ref()
const loading = ref(false)

// 表单数据
const loginForm = reactive({
  username: '',
  password: ''
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

// 验证规则
const rules = {
  username: [{ validator: validateUsername, trigger: 'blur' }],
  password: [{ validator: validatePassword, trigger: 'blur' }]
}

// 模拟登录接口
const mockLoginApi = (userData) => {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      console.log('登录数据:', userData)
      // 模拟登录验证
      if (userData.username && userData.password) {
        resolve({ 
          success: true, 
          message: '登录成功',
          token: 'mock-token-' + Date.now(),
          userInfo: {
            username: userData.username,
            id: Math.floor(Math.random() * 1000)
          }
        })
      } else {
        reject(new Error('用户名或密码错误'))
      }
    }, 1000)
  })
}

// 处理登录
const handleLogin = () => {
  loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const response = await mockLoginApi({
          username: loginForm.username,
          password: loginForm.password
        })
        
        // 保存登录信息到本地存储
        localStorage.setItem('token', response.token)
        localStorage.setItem('userInfo', JSON.stringify(response.userInfo))
        
        await ElMessageBox.alert('登录成功！', '提示', {
          confirmButtonText: '确定',
          type: 'success'
        })
        
        // 这里可以跳转到主页面，暂时显示成功信息
        ElMessage.success('登录成功，欢迎回来！')
        
      } catch (error) {
        ElMessage.error(error.message || '登录失败，请重试')
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

.login-form {
  margin-top: 20px;
}

.login-btn {
  width: 100%;
  height: 40px;
  font-size: 14px;
  margin-top: 20px;
  background: #409eff;
  border: none;
}

.login-btn:hover {
  background: #66b1ff;
}

.register-link {
  text-align: center;
  margin-top: 20px;
}

.register-link a {
  color: #409eff;
  text-decoration: none;
  font-size: 14px;
}

.register-link a:hover {
  text-decoration: underline;
}
</style> 