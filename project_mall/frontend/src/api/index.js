import axios from 'axios'
import { showFailToast } from 'vant'
import router from '@/router'

// 创建axios实例
const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('userToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const { code, message, data } = response.data
    if (code === 200) {
      return { data, message }
    } else {
      showFailToast(message || '请求失败')
      return Promise.reject(new Error(message))
    }
  },
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('userToken')
      localStorage.removeItem('userInfo')
      router.push('/login')
    }
    showFailToast(error.message || '网络错误')
    return Promise.reject(error)
  }
)

// 获取当前用户ID的辅助函数
const getCurrentUserId = () => {
  const userInfo = localStorage.getItem('userInfo')
  if (userInfo) {
    try {
      const user = JSON.parse(userInfo)
      return user.id
    } catch (error) {
      console.error('解析用户信息失败:', error)
    }
  }
  return null
}

// 用户相关API
export const userApi = {
  // 用户登录
  login: (data) => request.post('/user/login', data),
  // 用户注册
  register: (data) => request.post('/user/register', data),
  // 获取用户信息
  getInfo: () => request.get('/user/info'),
  // 更新用户信息
  updateInfo: (data) => request.put('/user/info', data),
  // 修改密码
  changePassword: (data) => request.put('/user/password', data)
}

// 商品相关API
export const productApi = {
  // 获取热门商品
  getHotProducts: (limit = 10) => request.get('/products/hot', { params: { limit } }),
  // 根据分类查询商品
  getByCategory: (categoryId) => request.get(`/products/category/${categoryId}`),
  // 搜索商品
  search: (keyword) => request.get('/products/search', { params: { keyword } }),
  // 获取商品详情
  getDetail: (id) => request.get(`/products/${id}`)
}

// 分类相关API
export const categoryApi = {
  // 获取分类列表
  getList: () => request.get('/categories'),
  // 获取分类树
  getTree: () => request.get('/categories/tree')
}

// 购物车相关API
export const cartApi = {
  // 获取购物车列表
  getList: () => {
    const userId = getCurrentUserId()
    if (!userId) {
      return Promise.reject(new Error('未登录'))
    }
    return request.get('/cart', { params: { userId } })
  },
  // 添加到购物车
  add: (productId, quantity = 1) => {
    const userId = getCurrentUserId()
    if (!userId) {
      return Promise.reject(new Error('未登录'))
    }
    return request.post('/cart', { userId, productId, quantity })
  },
  // 更新购物车商品数量
  updateQuantity: (id, quantity) => request.put(`/cart/${id}`, { quantity }),
  // 删除购物车商品
  remove: (id) => request.delete(`/cart/${id}`),
  // 清空购物车
  clear: () => {
    const userId = getCurrentUserId()
    if (!userId) {
      return Promise.reject(new Error('未登录'))
    }
    return request.delete('/cart/clear', { params: { userId } })
  },
  // 选中/取消选中商品
  updateChecked: (id, checked) => request.put(`/cart/${id}/checked`, { checked }),
  // 全选/取消全选
  checkAll: (checked) => {
    const userId = getCurrentUserId()
    if (!userId) {
      return Promise.reject(new Error('未登录'))
    }
    return request.put('/cart/check-all', { userId, checked })
  }
}

// 订单相关API
export const orderApi = {
  // 创建订单
  create: (receiverName, receiverPhone, receiverAddress) => {
    const userId = getCurrentUserId()
    if (!userId) {
      return Promise.reject(new Error('未登录'))
    }
    return request.post('/orders', { userId, receiverName, receiverPhone, receiverAddress })
  },
  // 获取订单列表
  getList: (params = {}) => {
    const userId = getCurrentUserId()
    if (!userId) {
      return Promise.reject(new Error('未登录'))
    }
    return request.get('/orders', { params: { userId, ...params } })
  },
  // 获取订单详情
  getDetail: (id) => request.get(`/orders/${id}`),
  // 取消订单
  cancel: (id) => request.put(`/orders/${id}/cancel`),
  // 确认收货
  confirm: (id) => request.put(`/orders/${id}/confirm`),
  // 删除订单
  delete: (id) => request.delete(`/orders/${id}`)
}

// 支付相关API
export const paymentApi = {
  // 创建支付订单
  create: (orderNo, paymentType) => request.post('/payment/create', { orderNo, paymentType }),
  // 查询支付状态
  getStatus: (orderNo) => request.get(`/payment/status/${orderNo}`)
}

// 地址相关API
export const addressApi = {
  // 获取地址列表
  getList: () => request.get('/addresses'),
  // 添加地址
  add: (data) => request.post('/addresses', data),
  // 更新地址
  update: (id, data) => request.put(`/addresses/${id}`, data),
  // 删除地址
  delete: (id) => request.delete(`/addresses/${id}`),
  // 设置默认地址
  setDefault: (id) => request.put(`/addresses/${id}/default`)
}

export default request 