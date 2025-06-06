<template>
  <div class="product-list-page">
    <!-- 导航栏 -->
    <van-nav-bar 
      :title="categoryName || '商品列表'" 
      left-arrow 
      @click-left="$router.back()"
      fixed
    />
    
    <!-- 筛选栏 -->
    <div class="filter-bar">
      <van-dropdown-menu>
        <van-dropdown-item v-model="sortType" :options="sortOptions" @change="onSortChange" />
        <van-dropdown-item v-model="priceRange" :options="priceOptions" @change="onPriceChange" />
      </van-dropdown-menu>
    </div>
    
    <!-- 商品列表 -->
    <div class="product-content">
      <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
        <van-list
          v-model:loading="loading"
          :finished="finished"
          finished-text="没有更多商品了"
          @load="onLoadMore"
        >
          <div v-if="products.length > 0" class="product-grid">
            <div
              v-for="product in products"
              :key="product.id"
              class="product-item"
              @click="goToProduct(product.id)"
            >
              <van-image 
                :src="product.image || product.mainImage" 
                fit="cover" 
                class="product-image"
                loading-icon="photo"
                error-icon="photo"
              />
              <div class="product-info">
                <div class="product-name">{{ product.name }}</div>
                <div class="product-desc">{{ product.subtitle || product.description }}</div>
                <div class="product-price">¥{{ product.price }}</div>
                <div class="product-sales">已售{{ product.sales || 0 }}</div>
              </div>
            </div>
          </div>
          
          <!-- 空状态 -->
          <div v-else-if="!loading" class="empty-state">
            <van-empty 
              image="https://fastly.jsdelivr.net/npm/@vant/assets/custom-empty-image.png"
              description="暂无相关商品"
            >
              <van-button type="primary" size="small" @click="$router.back()">
                返回分类
              </van-button>
            </van-empty>
          </div>
        </van-list>
      </van-pull-refresh>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { showToast } from 'vant'
import { productApi, categoryApi } from '@/api'

const route = useRoute()
const router = useRouter()

// 响应式数据
const categoryId = ref(route.params.categoryId || route.query.categoryId)
const categoryName = ref(route.query.categoryName || '')
const products = ref([])
const loading = ref(false)
const finished = ref(false)
const refreshing = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)

// 筛选条件
const sortType = ref('default')
const priceRange = ref('all')

// 筛选选项
const sortOptions = [
  { text: '综合排序', value: 'default' },
  { text: '价格从低到高', value: 'price_asc' },
  { text: '价格从高到低', value: 'price_desc' },
  { text: '销量优先', value: 'sales_desc' }
]

const priceOptions = [
  { text: '全部价格', value: 'all' },
  { text: '0-100元', value: '0-100' },
  { text: '100-500元', value: '100-500' },
  { text: '500-1000元', value: '500-1000' },
  { text: '1000元以上', value: '1000+' }
]

// 模拟商品数据
const mockProducts = [
  {
    id: 1,
    name: 'iPhone 15 Pro',
    subtitle: '全新A17 Pro芯片，钛金属材质',
    price: 7999,
    sales: 1234,
    image: 'https://img01.yzcdn.cn/vant/ipad.jpeg'
  },
  {
    id: 2,
    name: 'MacBook Pro',
    subtitle: 'M3芯片，16英寸液晶显示屏',
    price: 12999,
    sales: 567,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg'
  },
  {
    id: 3,
    name: 'AirPods Pro',
    subtitle: '主动降噪，空间音频',
    price: 1999,
    sales: 2345,
    image: 'https://img01.yzcdn.cn/vant/ipad.jpeg'
  },
  {
    id: 4,
    name: 'iPad Pro',
    subtitle: 'M2芯片，12.9英寸显示屏',
    price: 6999,
    sales: 890,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg'
  },
  {
    id: 5,
    name: 'Apple Watch',
    subtitle: '健康监测，运动追踪',
    price: 2999,
    sales: 1456,
    image: 'https://img01.yzcdn.cn/vant/ipad.jpeg'
  },
  {
    id: 6,
    name: 'HomePod',
    subtitle: '智能音箱，Hi-Fi音质',
    price: 2299,
    sales: 678,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg'
  }
]

// 加载商品数据
const loadProducts = async (isRefresh = false) => {
  if (loading.value && !isRefresh) return
  
  loading.value = true
  
  try {
    // 如果是刷新，重置页码
    if (isRefresh) {
      currentPage.value = 1
      products.value = []
      finished.value = false
    }
    
    // 先尝试调用API
    try {
      const response = await productApi.getByCategory(categoryId.value)
      if (response.data && response.data.length > 0) {
        const newProducts = response.data.slice(
          (currentPage.value - 1) * pageSize.value,
          currentPage.value * pageSize.value
        )
        
        if (isRefresh) {
          products.value = newProducts
        } else {
          products.value.push(...newProducts)
        }
        
        if (newProducts.length < pageSize.value) {
          finished.value = true
        } else {
          currentPage.value++
        }
        
        loading.value = false
        return
      }
    } catch (error) {
      console.log('API调用失败，使用模拟数据:', error.message)
    }
    
    // API失败时使用模拟数据
    const startIndex = (currentPage.value - 1) * pageSize.value
    const endIndex = startIndex + pageSize.value
    const newProducts = mockProducts.slice(startIndex, endIndex)
    
    if (isRefresh) {
      products.value = newProducts
    } else {
      products.value.push(...newProducts)
    }
    
    if (newProducts.length < pageSize.value || endIndex >= mockProducts.length) {
      finished.value = true
    } else {
      currentPage.value++
    }
    
  } catch (error) {
    console.error('加载商品失败:', error)
    showToast('加载失败')
  } finally {
    loading.value = false
    refreshing.value = false
  }
}

// 下拉刷新
const onRefresh = () => {
  refreshing.value = true
  loadProducts(true)
}

// 加载更多
const onLoadMore = () => {
  loadProducts()
}

// 排序变化
const onSortChange = (value) => {
  sortType.value = value
  // 根据排序重新加载数据
  onRefresh()
}

// 价格筛选变化
const onPriceChange = (value) => {
  priceRange.value = value
  // 根据价格筛选重新加载数据
  onRefresh()
}

// 去商品详情
const goToProduct = (id) => {
  router.push(`/product/${id}`)
}

// 加载分类名称
const loadCategoryName = async () => {
  if (categoryName.value) return
  
  try {
    const response = await categoryApi.getList()
    if (response.data) {
      const category = response.data.find(cat => cat.id == categoryId.value)
      if (category) {
        categoryName.value = category.name
      }
    }
  } catch (error) {
    console.error('获取分类名称失败:', error)
  }
}

onMounted(() => {
  if (!categoryId.value) {
    showToast('分类ID不能为空')
    router.back()
    return
  }
  
  loadCategoryName()
  loadProducts(true)
})
</script>

<style scoped>
.product-list-page {
  background-color: #f7f8fa;
  min-height: 100vh;
  padding-top: 46px; /* 为固定导航栏留出空间 */
  padding-bottom: 60px; /* 为底部导航栏留出空间 */
}

.filter-bar {
  background: white;
  border-bottom: 1px solid #eee;
}

.product-content {
  min-height: calc(100vh - 136px);
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  padding: 12px;
}

.product-item {
  background-color: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  transition: all 0.2s ease;
  border: 1px solid #f0f0f0;
}

.product-item:active {
  transform: scale(0.98);
  box-shadow: 0 4px 16px rgba(0,0,0,0.12);
}

.product-image {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-bottom: 1px solid #f5f5f5;
}

.product-info {
  padding: 12px;
}

.product-name {
  font-size: 14px;
  font-weight: 600;
  color: #323233;
  margin-bottom: 6px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.4;
}

.product-desc {
  font-size: 12px;
  color: #969799;
  margin-bottom: 8px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-price {
  font-size: 16px;
  color: #ee0a24;
  font-weight: bold;
  margin-bottom: 4px;
}

.product-price::before {
  content: '¥';
  font-size: 14px;
}

.product-sales {
  font-size: 12px;
  color: #969799;
  background: #f7f8fa;
  padding: 2px 6px;
  border-radius: 4px;
  display: inline-block;
}

.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  padding: 40px 20px;
}

:deep(.van-dropdown-menu) {
  box-shadow: none;
}

:deep(.van-dropdown-menu__bar) {
  background: white;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}
</style> 