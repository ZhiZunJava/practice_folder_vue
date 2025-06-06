<template>
  <div class="product-detail">
    <van-nav-bar 
      title="商品详情" 
      left-arrow 
      @click-left="$router.go(-1)" 
    />
    
    <div v-if="product" class="product-info">
      <!-- 商品图片 -->
      <van-swipe :autoplay="3000" indicator-color="white">
        <van-swipe-item v-for="(image, index) in product.images" :key="index">
          <img :src="image" :alt="product.name" />
        </van-swipe-item>
      </van-swipe>
      
      <!-- 商品基本信息 -->
      <div class="product-basic">
        <div class="price">
          <span class="current-price">¥{{ product.price }}</span>
          <span v-if="product.originalPrice" class="original-price">
            ¥{{ product.originalPrice }}
          </span>
        </div>
        <h2 class="name">{{ product.name }}</h2>
        <p class="desc">{{ product.description }}</p>
        
        <!-- 销量和评价 -->
        <div class="sales-info">
          <span>销量 {{ product.sales || 0 }}</span>
          <span>评价 {{ product.reviews || 0 }}</span>
        </div>
      </div>
      
      <!-- 商品详情 -->
      <van-cell-group title="商品详情">
        <van-cell title="品牌" :value="product.brand || '暂无'" />
        <van-cell title="产地" :value="product.origin || '暂无'" />
        <van-cell title="规格" :value="product.specification || '暂无'" />
      </van-cell-group>
      
      <!-- 详情图片 -->
      <div class="detail-images">
        <h3>商品详情</h3>
        <img 
          v-for="(image, index) in product.detailImages" 
          :key="index"
          :src="image" 
          :alt="`详情图${index + 1}`" 
        />
      </div>
    </div>
    
    <van-loading v-else type="spinner" color="#1989fa">加载中...</van-loading>
    
    <!-- 底部操作栏 -->
    <div class="goods-action">
      <div class="goods-action-icons">
        <div class="action-icon" @click="goToCart">
          <van-badge :content="cartCount > 0 ? cartCount : null" :show-zero="false">
            <van-icon name="shopping-cart-o" />
          </van-badge>
          <span>购物车</span>
        </div>
        <div class="action-icon" @click="toggleFavorite">
          <van-icon 
            :name="isFavorite ? 'star' : 'star-o'" 
            :color="isFavorite ? '#ff5000' : '#323233'"
          />
          <span>{{ isFavorite ? '已收藏' : '收藏' }}</span>
        </div>
        <div class="action-icon" @click="showShare = true">
          <van-icon name="share-o" />
          <span>分享</span>
        </div>
      </div>
      <div class="goods-action-buttons">
        <van-button 
          type="warning" 
          size="large"
          round
          @click="addToCart"
          class="add-cart-btn"
        >
          加入购物车
        </van-button>
        <van-button 
          type="danger" 
          size="large"
          round
          @click="buyNow"
          class="buy-now-btn"
        >
          立即购买
        </van-button>
      </div>
    </div>
    
    <!-- 分享面板 -->
    <van-share-sheet
      v-model:show="showShare"
      title="立即分享给好友"
      :description="product?.name || '精选商品'"
      :options="shareOptions"
      @select="onSelectShare"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { showToast } from 'vant'
import { productApi } from '@/api'

const route = useRoute()
const router = useRouter()

// 响应式数据
const product = ref(null)
const isFavorite = ref(false)
const cartCount = ref(0)
const showShare = ref(false)

// 分享选项配置
const shareOptions = [
  [
    { name: '微信', icon: 'wechat' },
    { name: '朋友圈', icon: 'wechat-moments' },
    { name: '微博', icon: 'weibo' },
    { name: 'QQ', icon: 'qq' }
  ],
  [
    { name: '复制链接', icon: 'link' },
    { name: '分享海报', icon: 'poster' },
    { name: '二维码', icon: 'qrcode' }
  ]
]

// 获取购物车数量
const getCartCount = () => {
  try {
    const cartItems = JSON.parse(localStorage.getItem('cartItems') || '[]')
    cartCount.value = cartItems.length
  } catch (error) {
    cartCount.value = 0
  }
}

// 加载商品详情
const loadProductDetail = async () => {
  try {
    const productId = route.params.id
    
    // 先尝试调用API
    try {
      const response = await productApi.getDetail(productId)
      if (response.data) {
        product.value = {
          ...response.data,
          images: response.data.subImages ? response.data.subImages.split(',') : [response.data.mainImage || 'https://img01.yzcdn.cn/vant/ipad.jpeg'],
          detailImages: [response.data.mainImage || 'https://img01.yzcdn.cn/vant/ipad.jpeg'],
          description: response.data.detail || response.data.subtitle || '精选商品，品质保证'
        }
        // 检查收藏状态
        checkFavoriteStatus()
        return
      }
    } catch (apiError) {
      console.log('API调用失败，使用模拟数据:', apiError.message)
    }
    
    // API失败时使用模拟数据
    const mockProducts = {
      1: {
        id: 1,
        name: 'iPhone 15 Pro',
        price: 7999,
        originalPrice: 8999,
        description: '全新A17 Pro芯片，钛金属材质，专业级摄像系统',
        brand: 'Apple',
        origin: '中国',
        specification: '256GB',
        sales: 1234,
        reviews: 567,
        images: [
          'https://img01.yzcdn.cn/vant/ipad.jpeg',
          'https://img.yzcdn.cn/vant/cat.jpeg',
          'https://img01.yzcdn.cn/vant/ipad.jpeg'
        ],
        detailImages: [
          'https://img01.yzcdn.cn/vant/ipad.jpeg',
          'https://img.yzcdn.cn/vant/cat.jpeg'
        ]
      },
      2: {
        id: 2,
        name: 'MacBook Pro',
        price: 12999,
        originalPrice: 14999,
        description: 'M3芯片，16英寸液晶显示屏，专业性能',
        brand: 'Apple',
        origin: '中国',
        specification: '16英寸 M3芯片',
        sales: 567,
        reviews: 234,
        images: [
          'https://img.yzcdn.cn/vant/cat.jpeg',
          'https://img01.yzcdn.cn/vant/ipad.jpeg'
        ],
        detailImages: [
          'https://img.yzcdn.cn/vant/cat.jpeg'
        ]
      },
      3: {
        id: 3,
        name: 'AirPods Pro',
        price: 1999,
        description: '主动降噪，空间音频，无线充电',
        brand: 'Apple',
        origin: '中国',
        specification: '第三代',
        sales: 2345,
        reviews: 891,
        images: [
          'https://img01.yzcdn.cn/vant/ipad.jpeg'
        ],
        detailImages: [
          'https://img01.yzcdn.cn/vant/ipad.jpeg'
        ]
      }
    }
    
    product.value = mockProducts[productId] || {
      id: productId,
      name: '精选商品',
      price: 999,
      description: '优质商品，值得信赖',
      brand: '知名品牌',
      origin: '中国',
      specification: '标准规格',
      sales: 100,
      reviews: 50,
      images: ['https://img01.yzcdn.cn/vant/ipad.jpeg'],
      detailImages: ['https://img01.yzcdn.cn/vant/ipad.jpeg']
    }
    
    // 检查收藏状态
    checkFavoriteStatus()
    
  } catch (error) {
    console.error('加载商品详情失败:', error)
    showToast('加载失败')
  }
}

// 检查收藏状态
const checkFavoriteStatus = () => {
  if (!product.value) return
  
  try {
    const favorites = JSON.parse(localStorage.getItem('favoriteProducts') || '[]')
    isFavorite.value = favorites.some(item => item.id == product.value.id)
  } catch (error) {
    isFavorite.value = false
  }
}

// 切换收藏状态
const toggleFavorite = () => {
  if (!product.value) return
  
  try {
    const favorites = JSON.parse(localStorage.getItem('favoriteProducts') || '[]')
    
    if (isFavorite.value) {
      // 取消收藏
      const updatedFavorites = favorites.filter(item => item.id != product.value.id)
      localStorage.setItem('favoriteProducts', JSON.stringify(updatedFavorites))
      isFavorite.value = false
      showToast('已取消收藏')
    } else {
      // 添加收藏
      const favoriteItem = {
        id: product.value.id,
        name: product.value.name,
        price: product.value.price,
        image: product.value.images[0],
        addTime: new Date().toISOString()
      }
      favorites.push(favoriteItem)
      localStorage.setItem('favoriteProducts', JSON.stringify(favorites))
      isFavorite.value = true
      showToast('已添加到收藏')
    }
  } catch (error) {
    console.error('收藏操作失败:', error)
    showToast('操作失败')
  }
}

// 联系客服
const contactService = () => {
  showToast('客服功能开发中...')
}

// 分享选择处理
const onSelectShare = async (option) => {
  showShare.value = false
  
  switch (option.name) {
    case '复制链接':
      // 复制当前页面链接
      const url = window.location.href
      if (navigator.clipboard) {
        try {
          await navigator.clipboard.writeText(url)
          showToast('链接已复制')
        } catch (error) {
          showToast('复制失败')
        }
      } else {
        // 降级方案
        const textArea = document.createElement('textarea')
        textArea.value = url
        document.body.appendChild(textArea)
        textArea.select()
        try {
          document.execCommand('copy')
          showToast('链接已复制')
        } catch (error) {
          showToast('复制失败')
        }
        document.body.removeChild(textArea)
      }
      break
    case '分享海报':
      showToast('海报生成功能开发中...')
      break
    case '二维码':
      showToast('二维码生成功能开发中...')
      break
    case '微信':
    case '朋友圈':
    case '微博':
    case 'QQ':
      showToast(`分享到${option.name}功能开发中...`)
      break
    default:
      showToast('分享功能开发中...')
  }
}

// 去购物车
const goToCart = () => {
  router.push('/cart')
}

// 加入购物车
const addToCart = () => {
  if (!product.value) {
    showToast('商品信息加载中...')
    return
  }
  
  // 使用本地存储添加购物车
  try {
    const cartItems = JSON.parse(localStorage.getItem('cartItems') || '[]')
    const existingItem = cartItems.find(item => item.productId == product.value.id)
    
    if (existingItem) {
      existingItem.quantity += 1
    } else {
      const newItem = {
        id: Date.now(),
        productId: product.value.id,
        productName: product.value.name,
        productImage: product.value.images[0],
        price: product.value.price,
        quantity: 1
      }
      cartItems.push(newItem)
    }
    
    localStorage.setItem('cartItems', JSON.stringify(cartItems))
    getCartCount() // 更新购物车数量
    showToast('已加入购物车')
    
    // 如果有全局的购物车更新方法，调用它
    if (typeof window.updateCartCount === 'function') {
      window.updateCartCount()
    }
    
  } catch (error) {
    console.error('加入购物车失败:', error)
    showToast('加入购物车失败')
  }
}

// 立即购买
const buyNow = () => {
  addToCart()
  showToast('功能开发中')
}

onMounted(() => {
  loadProductDetail()
  getCartCount()
  
  // 监听购物车变化
  const handleStorageChange = (e) => {
    if (e.key === 'cartItems') {
      getCartCount()
    }
  }
  
  window.addEventListener('storage', handleStorageChange)
  
  // 组件卸载时移除监听器
  onUnmounted(() => {
    window.removeEventListener('storage', handleStorageChange)
  })
})
</script>

<style scoped>
.product-detail {
  padding-bottom: 70px; /* 为底部操作栏留出空间 */
  background-color: #f7f8fa;
  min-height: 100vh;
}

.van-swipe {
  height: 300px;
}

.van-swipe img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-basic {
  padding: 16px;
  background: white;
  margin-bottom: 10px;
}

.price {
  margin-bottom: 8px;
}

.current-price {
  font-size: 24px;
  color: #ff6b35;
  font-weight: bold;
}

.original-price {
  font-size: 14px;
  color: #999;
  text-decoration: line-through;
  margin-left: 8px;
}

.name {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 8px;
  line-height: 1.4;
}

.desc {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 12px;
}

.sales-info {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #999;
}

.detail-images {
  padding: 16px;
  background: white;
  margin-top: 10px;
  margin-bottom: 20px;
}

.detail-images h3 {
  margin-bottom: 16px;
  font-size: 16px;
}

.detail-images img {
  width: 100%;
  margin-bottom: 10px;
}

.van-loading {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
}

/* 自定义底部操作栏样式 */
.goods-action {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: white;
  border-top: 1px solid #eee;
  box-shadow: 0 -2px 8px rgba(0,0,0,0.1);
  padding: 8px 12px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.goods-action-icons {
  display: flex;
  gap: 8px;
}

.action-icon {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4px 8px;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.2s;
  min-width: 50px;
}

.action-icon:active {
  background-color: #f7f8fa;
}

.action-icon .van-icon {
  font-size: 20px;
  margin-bottom: 2px;
}

.action-icon span {
  font-size: 11px;
  color: #666;
  white-space: nowrap;
}

.action-icon .van-badge {
  display: flex;
  justify-content: center;
}

.goods-action-buttons {
  display: flex;
  gap: 8px;
  flex: 1;
  margin-left: 8px;
}

.add-cart-btn, .buy-now-btn {
  flex: 1;
  height: 40px;
  font-size: 14px;
  font-weight: 600;
}

.add-cart-btn {
  background: linear-gradient(135deg, #ff9a56 0%, #ff7643 100%);
  border: none;
}

.buy-now-btn {
  background: linear-gradient(135deg, #ee0a24 0%, #d71345 100%);
  border: none;
}
</style> 