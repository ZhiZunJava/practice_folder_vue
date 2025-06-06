import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi, logout as logoutApi, getInfo } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref({})
  
  // 登录
  const login = async (loginForm) => {
    try {
      const response = await loginApi(loginForm)
      if (response.code === 200) {
        token.value = response.token
        localStorage.setItem('token', response.token)
        return Promise.resolve(response)
      } else {
        return Promise.reject(response.msg)
      }
    } catch (error) {
      // 如果后端未启动，使用模拟登录作为降级方案
      if (error.code === 'ERR_NETWORK' || error.message.includes('Network Error')) {
        console.warn('后端服务未启动，使用模拟登录')
        const mockToken = 'mock-token-' + Date.now()
        token.value = mockToken
        localStorage.setItem('token', mockToken)
        userInfo.value = {
          userId: 1,
          userName: loginForm.username,
          nickName: '管理员',
          email: 'admin@example.com'
        }
        return Promise.resolve({ code: 200, token: mockToken, msg: '模拟登录成功' })
      }
      return Promise.reject(error)
    }
  }
  
  // 获取用户信息
  const getUserInfo = async () => {
    try {
      const response = await getInfo()
      if (response.code === 200) {
        userInfo.value = response.user
        return Promise.resolve(response)
      }
    } catch (error) {
      return Promise.reject(error)
    }
  }
  
  // 登出
  const logout = async () => {
    try {
      await logoutApi()
    } catch (error) {
      console.error('Logout error:', error)
    } finally {
      token.value = ''
      userInfo.value = {}
      localStorage.removeItem('token')
    }
  }
  
  return {
    token,
    userInfo,
    login,
    getUserInfo,
    logout
  }
}) 