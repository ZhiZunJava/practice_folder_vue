<template>
  <div class="login">
    <van-nav-bar title="用户登录" left-arrow @click-left="$router.go(-1)" />
    
    <div class="login-container">
      <div class="logo">
        <h1>商城</h1>
      </div>
      
      <van-form @submit="onSubmit">
        <van-cell-group inset>
          <van-field
            v-model="form.username"
            name="username"
            label="用户名"
            placeholder="请输入用户名"
            :rules="[{ required: true, message: '请填写用户名' }]"
          />
          <van-field
            v-model="form.password"
            type="password"
            name="password"
            label="密码"
            placeholder="请输入密码"
            :rules="[{ required: true, message: '请填写密码' }]"
          />
        </van-cell-group>
        
        <div class="login-btn">
          <van-button 
            round 
            block 
            type="primary" 
            native-type="submit"
            :loading="loading"
          >
            登录
          </van-button>
        </div>
      </van-form>
      
      <div class="register-link">
        <van-button type="default" size="small" @click="goToRegister">
          还没有账号？立即注册
        </van-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import { userApi } from '@/api'

const router = useRouter()

// 表单数据
const form = ref({
  username: '',
  password: ''
})

const loading = ref(false)

// 提交登录
const onSubmit = async (values) => {
  loading.value = true
  
  try {
    const res = await userApi.login(values)
    
    if (res.code === 200) {
      // 保存token
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('userInfo', JSON.stringify(res.data.userInfo))
      
      showToast('登录成功')
      
      // 跳转到首页
      router.push('/')
    } else {
      showToast(res.message || '登录失败')
    }
  } catch (error) {
    console.error('登录失败:', error)
    showToast('登录失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}

// 跳转到注册页面
const goToRegister = () => {
  // 这里可以添加注册页面的路由
  showToast('注册功能暂未开放')
}
</script>

<style scoped>
.login {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-container {
  padding: 40px 20px;
}

.logo {
  text-align: center;
  margin-bottom: 40px;
}

.logo h1 {
  font-size: 48px;
  color: white;
  font-weight: bold;
  text-shadow: 0 2px 4px rgba(0,0,0,0.3);
}

.van-cell-group {
  margin-bottom: 20px;
}

.login-btn {
  padding: 0 16px;
  margin-bottom: 20px;
}

.register-link {
  text-align: center;
}

.register-link .van-button {
  background: transparent;
  border: 1px solid rgba(255,255,255,0.3);
  color: white;
}
</style> 