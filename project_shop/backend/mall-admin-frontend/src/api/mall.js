import request from './request'

// 仪表盘统计数据
export function getStatistics() {
  return request({
    url: '/mall/dashboard/statistics',
    method: 'get'
  })
}

export function getChartData() {
  return request({
    url: '/mall/dashboard/charts',
    method: 'get'
  })
}

// 分类管理
export function getCategoryList(params) {
  return request({
    url: '/mall/category/list',
    method: 'get',
    params
  })
}

export function getCategoryTree(params) {
  return request({
    url: '/mall/category/treeList',
    method: 'get',
    params
  })
}

export function getCategory(id) {
  return request({
    url: `/mall/category/${id}`,
    method: 'get'
  })
}

export function addCategory(data) {
  return request({
    url: '/mall/category',
    method: 'post',
    data
  })
}

export function updateCategory(data) {
  return request({
    url: '/mall/category',
    method: 'put',
    data
  })
}

export function deleteCategory(ids) {
  return request({
    url: `/mall/category/${ids}`,
    method: 'delete'
  })
}

// 商品管理
export function getProductList(params) {
  return request({
    url: '/mall/product/list',
    method: 'get',
    params
  })
}

export function getProduct(id) {
  return request({
    url: `/mall/product/${id}`,
    method: 'get'
  })
}

export function addProduct(data) {
  return request({
    url: '/mall/product',
    method: 'post',
    data
  })
}

export function updateProduct(data) {
  return request({
    url: '/mall/product',
    method: 'put',
    data
  })
}

export function deleteProduct(ids) {
  return request({
    url: `/mall/product/${ids}`,
    method: 'delete'
  })
} 