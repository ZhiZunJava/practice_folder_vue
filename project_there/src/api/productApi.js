// 商品API服务
// 这里可以配置你的API基础URL
const API_BASE_URL = '/api'

// 模拟延迟
const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms))

// 模拟API响应
const mockResponse = (data, success = true, message = '') => ({
  success,
  data,
  message,
  timestamp: new Date().toISOString()
})

// 商品API类
class ProductApi {
  // 获取商品列表
  async getProducts(params = {}) {
    await delay(500) // 模拟网络延迟
    
    // 这里应该调用真实的API
    // const response = await fetch(`${API_BASE_URL}/products?${new URLSearchParams(params)}`)
    // return response.json()
    
    // 模拟数据
    const mockData = [
      {
        id: 1,
        name: 'iPhone 14 Pro',
        category: '电子产品',
        price: 7999.00,
        stock: 50,
        status: '上架',
        description: '最新款iPhone，配备A16仿生芯片',
        createTime: '2024-01-01 10:00:00',
        updateTime: '2024-01-15 14:30:00'
      },
      {
        id: 2,
        name: '阿迪达斯运动鞋',
        category: '服装',
        price: 699.00,
        stock: 100,
        status: '上架',
        description: '舒适透气的运动鞋，适合日常运动',
        createTime: '2024-01-02 09:15:00',
        updateTime: '2024-01-16 11:20:00'
      },
      {
        id: 3,
        name: '有机苹果',
        category: '食品',
        price: 25.00,
        stock: 200,
        status: '上架',
        description: '新鲜有机苹果，富含维生素',
        createTime: '2024-01-03 08:30:00',
        updateTime: '2024-01-17 16:45:00'
      },
      {
        id: 4,
        name: 'Vue.js实战指南',
        category: '图书',
        price: 89.00,
        stock: 30,
        status: '下架',
        description: 'Vue.js从入门到精通的实战教程',
        createTime: '2024-01-04 14:20:00',
        updateTime: '2024-01-18 10:10:00'
      },
      {
        id: 5,
        name: '智能台灯',
        category: '家居用品',
        price: 299.00,
        stock: 75,
        status: '上架',
        description: '可调光护眼台灯，支持手机App控制',
        createTime: '2024-01-05 16:40:00',
        updateTime: '2024-01-19 13:25:00'
      }
    ]
    
    return mockResponse(mockData, true, '获取商品列表成功')
  }

  // 获取单个商品详情
  async getProduct(id) {
    await delay(300)
    
    // 模拟根据ID查找商品
    const products = await this.getProducts()
    const product = products.data.find(p => p.id === parseInt(id))
    
    if (product) {
      return mockResponse(product, true, '获取商品详情成功')
    } else {
      return mockResponse(null, false, '商品不存在')
    }
  }

  // 创建商品
  async createProduct(productData) {
    await delay(800)
    
    // 这里应该调用真实的API
    // const response = await fetch(`${API_BASE_URL}/products`, {
    //   method: 'POST',
    //   headers: {
    //     'Content-Type': 'application/json'
    //   },
    //   body: JSON.stringify(productData)
    // })
    // return response.json()
    
    // 模拟创建商品
    const newProduct = {
      ...productData,
      id: Date.now(), // 模拟生成ID
      createTime: new Date().toISOString(),
      updateTime: new Date().toISOString()
    }
    
    return mockResponse(newProduct, true, '创建商品成功')
  }

  // 更新商品
  async updateProduct(id, productData) {
    await delay(800)
    
    // 这里应该调用真实的API
    // const response = await fetch(`${API_BASE_URL}/products/${id}`, {
    //   method: 'PUT',
    //   headers: {
    //     'Content-Type': 'application/json'
    //   },
    //   body: JSON.stringify(productData)
    // })
    // return response.json()
    
    // 模拟更新商品
    const updatedProduct = {
      ...productData,
      id: parseInt(id),
      updateTime: new Date().toISOString()
    }
    
    return mockResponse(updatedProduct, true, '更新商品成功')
  }

  // 删除商品
  async deleteProduct(id) {
    await delay(500)
    
    // 这里应该调用真实的API
    // const response = await fetch(`${API_BASE_URL}/products/${id}`, {
    //   method: 'DELETE'
    // })
    // return response.json()
    
    return mockResponse(null, true, '删除商品成功')
  }

  // 批量删除商品
  async batchDeleteProducts(ids) {
    await delay(1000)
    
    // 这里应该调用真实的API
    // const response = await fetch(`${API_BASE_URL}/products/batch`, {
    //   method: 'DELETE',
    //   headers: {
    //     'Content-Type': 'application/json'
    //   },
    //   body: JSON.stringify({ ids })
    // })
    // return response.json()
    
    return mockResponse(null, true, `批量删除${ids.length}个商品成功`)
  }

  // 搜索商品
  async searchProducts(keyword, filters = {}) {
    await delay(400)
    
    const products = await this.getProducts()
    const filteredProducts = products.data.filter(product => {
      const matchKeyword = !keyword || 
        product.name.toLowerCase().includes(keyword.toLowerCase()) ||
        product.category.toLowerCase().includes(keyword.toLowerCase()) ||
        product.description.toLowerCase().includes(keyword.toLowerCase())
      
      const matchCategory = !filters.category || product.category === filters.category
      const matchStatus = !filters.status || product.status === filters.status
      const matchPriceRange = (!filters.minPrice || product.price >= filters.minPrice) &&
                              (!filters.maxPrice || product.price <= filters.maxPrice)
      
      return matchKeyword && matchCategory && matchStatus && matchPriceRange
    })
    
    return mockResponse(filteredProducts, true, '搜索商品成功')
  }

  // 获取商品分类列表
  async getCategories() {
    await delay(200)
    
    const categories = [
      { value: '电子产品', label: '电子产品' },
      { value: '服装', label: '服装' },
      { value: '食品', label: '食品' },
      { value: '图书', label: '图书' },
      { value: '家居用品', label: '家居用品' },
      { value: '运动户外', label: '运动户外' },
      { value: '美妆护肤', label: '美妆护肤' },
      { value: '母婴用品', label: '母婴用品' }
    ]
    
    return mockResponse(categories, true, '获取分类列表成功')
  }
}

// 创建API实例
const productApi = new ProductApi()

export default productApi

// 导出具体的API方法（可选，便于直接导入使用）
export const {
  getProducts,
  getProduct,
  createProduct,
  updateProduct,
  deleteProduct,
  batchDeleteProducts,
  searchProducts,
  getCategories
} = productApi 