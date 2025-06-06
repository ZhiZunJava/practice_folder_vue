<template>
  <div class="login-container">
    <div class="login-form">
      <div class="text-center mb-20">
        <h2>微商城后台管理系统</h2>
      </div>
      
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        auto-complete="on"
        label-position="left"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            :prefix-icon="User"
            size="large"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            :type="passwordType"
            placeholder="密码"
            :prefix-icon="Lock"
            size="large"
            @keyup.enter="handleLogin"
          >
            <template #suffix>
              <el-icon @click="showPwd" style="cursor: pointer;">
                <View v-if="passwordType === 'password'" />
                <Hide v-else />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-button
          :loading="loading"
          type="primary"
          size="large"
          style="width: 100%; margin-bottom: 20px;"
          @click="handleLogin"
        >
          登录
        </el-button>
        
        <el-button
          size="large"
          style="width: 100%;"
          @click="handleReset"
        >
          重置
        </el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, View, Hide } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref()
const loading = ref(false)
const passwordType = ref('password')

const loginForm = reactive({
  username: 'admin',
  password: 'admin123'
})

const loginRules = {
  username: [
    { required: true, trigger: 'blur', message: '请输入用户名' }
  ],
  password: [
    { required: true, trigger: 'blur', message: '请输入密码' }
  ]
}

const showPwd = () => {
  passwordType.value = passwordType.value === 'password' ? '' : 'password'
}

const handleLogin = () => {
  loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 调用真实的登录API
        await userStore.login(loginForm)
        ElMessage.success('登录成功')
        router.push('/')
      } catch (error) {
        ElMessage.error('登录失败: ' + (error.message || error))
      } finally {
        loading.value = false
      }
    }
  })
}

const handleReset = () => {
  loginForm.username = 'admin'
  loginForm.password = 'admin123'
}
</script> 