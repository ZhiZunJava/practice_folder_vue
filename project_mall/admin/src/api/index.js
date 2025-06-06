import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建axios实例
const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('adminToken')
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
    const { code, message } = response.data
    if (code === 200) {
      return response.data
    } else {
      ElMessage.error(message || '请求失败')
      return Promise.reject(new Error(message))
    }
  },
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('adminToken')
      localStorage.removeItem('adminInfo')
      router.push('/login')
    }
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

// 管理员相关API
export const adminApi = {
  // 登录
  login: (data) => request.post('/admin/login', data),
  // 获取当前登录管理员信息
  getInfo: () => request.get('/admin/info'),
  // 更新管理员信息
  updateInfo: (data) => request.put('/admin/info', data),
  // 修改密码
  changePassword: (data) => request.put('/admin/password', data)
}

// 分类相关API
export const categoryApi = {
  // 获取分类树
  getTree: () => request.get('/admin/categories/tree'),
  // 添加分类
  add: (data) => request.post('/admin/categories', data),
  // 更新分类
  update: (id, data) => request.put(`/admin/categories/${id}`, data),
  // 删除分类
  delete: (id) => request.delete(`/admin/categories/${id}`),
  // 获取分类详情
  getById: (id) => request.get(`/admin/categories/${id}`)
}

// 商品相关API
export const productApi = {
  // 分页查询商品
  getPage: (params) => request.get('/admin/products', { params }),
  // 获取商品详情
  getById: (id) => request.get(`/admin/products/${id}`),
  // 添加商品
  add: (data) => request.post('/admin/products', data),
  // 更新商品
  update: (id, data) => request.put(`/admin/products/${id}`, data),
  // 删除商品
  delete: (id) => request.delete(`/admin/products/${id}`),
  // 更新商品状态
  updateStatus: (id, status) => request.put(`/admin/products/${id}/status`, { status })
}

// 用户相关API
export const userApi = {
  // 分页查询用户
  getPage: (params) => request.get('/admin/users', { params }),
  // 获取用户详情
  getById: (id) => request.get(`/admin/users/${id}`),
  // 更新用户状态
  updateStatus: (id, status) => request.put(`/admin/users/${id}/status`, { status }),
  // 更新用户信息
  update: (id, data) => request.put(`/admin/users/${id}`, data)
}

// 订单相关API
export const orderApi = {
  // 分页查询订单
  getPage: (params) => request.get('/admin/orders', { params }),
  // 获取订单详情
  getById: (id) => request.get(`/admin/orders/${id}`),
  // 更新订单状态
  updateStatus: (id, status) => request.put(`/admin/orders/${id}/status`, { status }),
  // 发货
  ship: (id, data) => request.put(`/admin/orders/${id}/ship`, data)
}

// 统计相关API
export const statsApi = {
  // 获取仪表盘数据
  getDashboard: () => request.get('/admin/stats/dashboard'),
  // 获取销售统计
  getSalesStats: (params) => request.get('/admin/stats/sales', { params }),
  // 获取用户统计
  getUserStats: (params) => request.get('/admin/stats/users', { params })
}

// 上传相关API
export const uploadApi = {
  // 上传图片
  uploadImage: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return request.post('/admin/upload/image', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  }
}

export default request 