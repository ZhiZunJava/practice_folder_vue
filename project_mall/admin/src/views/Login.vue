<template>
  <div class="login-container">
    <div class="login-background">
      <div class="bg-shape bg-shape-1"></div>
      <div class="bg-shape bg-shape-2"></div>
      <div class="bg-shape bg-shape-3"></div>
    </div>
    
    <div class="login-content">
      <div class="login-card">
        <!-- 登录头部 -->
        <div class="login-header">
          <div class="logo">
            <el-icon size="32" class="logo-icon"><Shop /></el-icon>
            <h1 class="logo-text">商城管理系统</h1>
          </div>
          <p class="login-subtitle">欢迎回来，请登录您的账户</p>
        </div>
        
        <!-- 登录表单 -->
        <el-form 
          ref="loginFormRef" 
          :model="loginForm" 
          :rules="loginRules" 
          class="login-form"
          @keyup.enter="handleLogin"
        >
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              size="large"
              clearable
              prefix-icon="User"
              class="login-input"
            />
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              size="large"
              clearable
              show-password
              prefix-icon="Lock"
              class="login-input"
            />
          </el-form-item>
          
          <el-form-item>
            <div class="login-options">
              <el-checkbox v-model="rememberMe">记住我</el-checkbox>
              <el-button type="text" class="forget-password">忘记密码？</el-button>
            </div>
          </el-form-item>
          
          <el-form-item>
            <el-button
              type="primary"
              size="large"
              :loading="loading"
              @click="handleLogin"
              class="login-button"
            >
              <span v-if="!loading">登录</span>
              <span v-else>登录中...</span>
            </el-button>
          </el-form-item>
        </el-form>
        
        <!-- 登录底部 -->
        <div class="login-footer">
          <div class="demo-accounts">
            <p class="demo-title">演示账号</p>
            <div class="demo-buttons">
              <el-button 
                size="small" 
                @click="fillDemoAccount('admin')"
                class="demo-btn"
              >
                管理员
              </el-button>
              <el-button 
                size="small" 
                @click="fillDemoAccount('test')"
                class="demo-btn"
              >
                测试账号
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 版权信息 -->
    <div class="copyright">
      <p>&copy; 2024 商城管理系统. 版权所有</p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { adminApi } from '@/api'

const router = useRouter()

// 表单引用
const loginFormRef = ref()

// 表单数据
const loginForm = reactive({
  username: '',
  password: ''
})

// 表单验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度为 2 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度为 6 到 20 个字符', trigger: 'blur' }
  ]
}

// 其他状态
const loading = ref(false)
const rememberMe = ref(false)

// 处理登录
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
    
    loading.value = true
    
    const response = await adminApi.login({
      username: loginForm.username,
      password: loginForm.password
    })
    
    // 保存token和用户信息
    localStorage.setItem('adminToken', response.data.token)
    localStorage.setItem('adminInfo', JSON.stringify(response.data.adminInfo))
    
    // 记住我功能
    if (rememberMe.value) {
      localStorage.setItem('rememberedUsername', loginForm.username)
    } else {
      localStorage.removeItem('rememberedUsername')
    }
    
    ElMessage.success('登录成功')
    
    // 跳转到仪表盘
    router.push('/dashboard')
    
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error(error.message || '登录失败，请检查用户名和密码')
  } finally {
    loading.value = false
  }
}

// 填充演示账号
const fillDemoAccount = (type) => {
  if (type === 'admin') {
    loginForm.username = 'admin'
    loginForm.password = '123456'
  } else if (type === 'test') {
    loginForm.username = 'test'
    loginForm.password = '123456'
  }
}

// 初始化
const init = () => {
  // 如果已经登录，直接跳转
  const token = localStorage.getItem('adminToken')
  if (token) {
    router.push('/dashboard')
    return
  }
  
  // 记住我功能
  const rememberedUsername = localStorage.getItem('rememberedUsername')
  if (rememberedUsername) {
    loginForm.username = rememberedUsername
    rememberMe.value = true
  }
}

init()
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 1;
}

.bg-shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.bg-shape-1 {
  width: 200px;
  height: 200px;
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.bg-shape-2 {
  width: 150px;
  height: 150px;
  top: 60%;
  right: 15%;
  animation-delay: 2s;
}

.bg-shape-3 {
  width: 100px;
  height: 100px;
  bottom: 20%;
  left: 20%;
  animation-delay: 4s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

.login-content {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  position: relative;
  z-index: 2;
}

.login-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 48px;
  width: 100%;
  max-width: 420px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 16px;
}

.logo-icon {
  color: var(--primary-color);
}

.logo-text {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary);
  background: linear-gradient(135deg, var(--primary-color), #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.login-subtitle {
  margin: 0;
  font-size: 14px;
  color: var(--text-secondary);
  line-height: 1.5;
}

.login-form {
  margin-bottom: 24px;
}

.login-input {
  margin-bottom: 8px;
}

.login-input :deep(.el-input__wrapper) {
  padding: 12px 16px;
  border-radius: 8px;
  border: 2px solid transparent;
  background: var(--bg-secondary);
  transition: all 0.3s ease;
}

.login-input :deep(.el-input__wrapper):hover {
  border-color: var(--primary-hover);
  background: #ffffff;
}

.login-input :deep(.el-input__wrapper.is-focus) {
  border-color: var(--primary-color);
  background: #ffffff;
  box-shadow: 0 0 0 4px rgba(24, 144, 255, 0.1);
}

.login-options {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  margin: 16px 0;
}

.forget-password {
  color: var(--primary-color);
  padding: 0;
  font-size: 14px;
}

.forget-password:hover {
  color: var(--primary-hover);
}

.login-button {
  width: 100%;
  height: 48px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, var(--primary-color), #764ba2);
  border: none;
  transition: all 0.3s ease;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(24, 144, 255, 0.3);
}

.login-button:active {
  transform: translateY(0);
}

.login-footer {
  text-align: center;
  border-top: 1px solid var(--border-lighter);
  padding-top: 24px;
}

.demo-title {
  margin: 0 0 16px 0;
  font-size: 14px;
  color: var(--text-tertiary);
}

.demo-buttons {
  display: flex;
  gap: 12px;
  justify-content: center;
}

.demo-btn {
  border-radius: 6px;
  font-size: 13px;
  padding: 8px 16px;
  border: 1px solid var(--border-color);
  color: var(--text-secondary);
  background: var(--bg-secondary);
  transition: all 0.3s ease;
}

.demo-btn:hover {
  border-color: var(--primary-color);
  color: var(--primary-color);
  background: rgba(24, 144, 255, 0.05);
}

.copyright {
  text-align: center;
  padding: 20px;
  position: relative;
  z-index: 2;
}

.copyright p {
  margin: 0;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.8);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-card {
    padding: 32px 24px;
    margin: 16px;
    border-radius: 12px;
  }
  
  .logo-text {
    font-size: 20px;
  }
  
  .demo-buttons {
    flex-direction: column;
  }
  
  .bg-shape-1,
  .bg-shape-2,
  .bg-shape-3 {
    opacity: 0.5;
  }
}

@media (max-width: 480px) {
  .login-card {
    padding: 24px 16px;
  }
  
  .login-options {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }
}

/* 深色模式适配 */
@media (prefers-color-scheme: dark) {
  .login-card {
    background: rgba(26, 32, 44, 0.95);
    border: 1px solid rgba(255, 255, 255, 0.1);
  }
}
</style> 