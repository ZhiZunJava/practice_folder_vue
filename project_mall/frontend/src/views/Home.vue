<template>
  <div class="home-page">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <van-search
        v-model="searchValue"
        placeholder="搜索商品"
        @search="onSearch"
        @click="$router.push('/search')"
        readonly
      >
        <template #left-icon>
          <van-icon name="search" />
        </template>
        <template #action>
          <van-icon name="scan" @click="onScan" />
        </template>
      </van-search>
    </div>

    <!-- 轮播图 -->
    <van-swipe class="banner-swipe" :autoplay="3000" indicator-color="white">
      <van-swipe-item v-for="item in banners" :key="item.id" @click="onBannerClick(item)">
        <van-image :src="item.image" fit="cover" class="banner-image">
          <template #loading>
            <van-loading type="spinner" size="20" />
          </template>
        </van-image>
      </van-swipe-item>
    </van-swipe>

    <!-- 功能菜单 -->
    <div class="menu-section">
      <van-grid :column-num="5" :gutter="10">
        <van-grid-item
          v-for="item in menuItems"
          :key="item.id"
          :icon="item.icon"
          :text="item.text"
          @click="onMenuClick(item)"
        />
      </van-grid>
    </div>

    <!-- 秒杀活动 -->
    <div class="seckill-section">
      <van-cell-group>
        <van-cell title="限时秒杀" :value="`${countdown.hours}:${countdown.minutes}:${countdown.seconds}`">
          <template #right-icon>
            <van-button type="danger" size="mini" @click="$router.push('/seckill')">
              更多
            </van-button>
          </template>
        </van-cell>
      </van-cell-group>
      
      <van-grid :column-num="3" :gutter="10" class="seckill-grid">
        <van-grid-item v-for="item in seckillProducts" :key="item.id" @click="goToProduct(item.id)">
          <van-image :src="item.image" fit="cover" class="seckill-image" />
          <div class="seckill-info">
            <div class="seckill-price">¥{{ item.seckillPrice }}</div>
            <div class="seckill-original">¥{{ item.originalPrice }}</div>
          </div>
        </van-grid-item>
      </van-grid>
    </div>

    <!-- 推荐商品 -->
    <div class="recommend-section">
      <van-divider>为你推荐</van-divider>
      <van-grid :column-num="2" :gutter="10">
        <van-grid-item v-for="item in recommendProducts" :key="item.id" @click="goToProduct(item.id)">
          <div class="product-card">
            <van-image :src="item.image" fit="cover" class="product-image" />
            <div class="product-info">
              <div class="product-title">{{ item.name }}</div>
              <div class="product-price">¥{{ item.price }}</div>
              <div class="product-sales">已售{{ item.sales }}</div>
            </div>
          </div>
        </van-grid-item>
      </van-grid>
    </div>

    <!-- 猜你喜欢 -->
    <div class="guess-section">
      <van-divider>猜你喜欢</van-divider>
      <div class="guess-list">
        <div
          v-for="item in guessProducts"
          :key="item.id"
          class="guess-item"
          @click="goToProduct(item.id)"
        >
          <van-image :src="item.image" fit="cover" class="guess-image" />
          <div class="guess-info">
            <div class="guess-title">{{ item.name }}</div>
            <div class="guess-desc">{{ item.desc }}</div>
            <div class="guess-price">¥{{ item.price }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import { productApi } from '@/api'

const router = useRouter()

// 搜索值
const searchValue = ref('')

// 倒计时
const countdown = reactive({
  hours: '02',
  minutes: '45',
  seconds: '30'
})

// 轮播图数据
const banners = ref([
  {
    id: 1,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg',
    title: '双12大促',
    url: '/promotion'
  },
  {
    id: 2,
    image: 'https://img01.yzcdn.cn/vant/ipad.jpeg',
    title: '新品首发',
    url: '/new-product'
  },
  {
    id: 3,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg',
    title: '限时秒杀',
    url: '/seckill'
  }
])

// 菜单项
const menuItems = ref([
  { id: 1, icon: 'apps-o', text: '分类', route: '/category' },
  { id: 2, icon: 'fire-o', text: '秒杀', route: '/seckill' },
  { id: 3, icon: 'gift-o', text: '优惠券', route: '/coupon' },
  { id: 4, icon: 'star-o', text: '签到', route: '/checkin' },
  { id: 5, icon: 'service-o', text: '客服', route: '/service' }
])

// 秒杀商品
const seckillProducts = ref([
  {
    id: 1,
    image: 'https://img01.yzcdn.cn/vant/ipad.jpeg',
    seckillPrice: 999,
    originalPrice: 1999
  },
  {
    id: 2,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg',
    seckillPrice: 199,
    originalPrice: 399
  },
  {
    id: 3,
    image: 'https://img01.yzcdn.cn/vant/ipad.jpeg',
    seckillPrice: 2999,
    originalPrice: 4999
  }
])

// 推荐商品
const recommendProducts = ref([])

// 猜你喜欢
const guessProducts = ref([])

// 模拟数据作为降级方案
const mockRecommendProducts = [
  {
    id: 4,
    name: 'iPhone 15 Pro',
    price: 7999,
    sales: 1234,
    image: 'https://img01.yzcdn.cn/vant/ipad.jpeg'
  },
  {
    id: 5,
    name: 'MacBook Pro',
    price: 12999,
    sales: 567,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg'
  },
  {
    id: 6,
    name: 'AirPods Pro',
    price: 1999,
    sales: 2345,
    image: 'https://img01.yzcdn.cn/vant/ipad.jpeg'
  },
  {
    id: 7,
    name: 'iPad Pro',
    price: 6999,
    sales: 890,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg'
  }
]

const mockGuessProducts = [
  {
    id: 8,
    name: 'Apple Watch Series 9',
    desc: '健康监测 运动追踪',
    price: 2999,
    image: 'https://img01.yzcdn.cn/vant/ipad.jpeg'
  },
  {
    id: 9,
    name: 'HomePod mini',
    desc: '智能音箱 Siri助手',
    price: 749,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg'
  },
  {
    id: 10,
    name: 'Magic Keyboard',
    desc: '无线键盘 妙控键盘',
    price: 1079,
    image: 'https://img01.yzcdn.cn/vant/ipad.jpeg'
  },
  {
    id: 11,
    name: 'AirTag',
    desc: '查找追踪器 防丢神器',
    price: 229,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg'
  }
]

let timer = null

// 加载热门商品数据
const loadHotProducts = async () => {
  try {
    const response = await productApi.getHotProducts(10)
    if (response.data && response.data.length > 0) {
      // 将API数据分配给推荐商品和猜你喜欢
      const products = response.data
      recommendProducts.value = products.slice(0, 4).map(item => ({
        id: item.id,
        name: item.name,
        price: item.price,
        sales: item.sales || Math.floor(Math.random() * 2000) + 100,
        image: item.image || 'https://img01.yzcdn.cn/vant/ipad.jpeg'
      }))
      
      guessProducts.value = products.slice(4, 8).map(item => ({
        id: item.id,
        name: item.name,
        desc: item.description || '精选商品',
        price: item.price,
        image: item.image || 'https://img.yzcdn.cn/vant/cat.jpeg'
      }))
    } else {
      // 使用模拟数据
      recommendProducts.value = mockRecommendProducts
      guessProducts.value = mockGuessProducts
    }
  } catch (error) {
    console.error('加载热门商品失败:', error)
    // 使用模拟数据作为降级方案
    recommendProducts.value = mockRecommendProducts
    guessProducts.value = mockGuessProducts
  }
}

// 搜索
const onSearch = (value) => {
  if (value.trim()) {
    router.push(`/search?keyword=${encodeURIComponent(value)}`)
  } else {
    showToast('请输入搜索关键词')
  }
}

// 扫码
const onScan = () => {
  showToast('扫码功能开发中...')
}

// 轮播图点击
const onBannerClick = (item) => {
  if (item.url) {
    router.push(item.url)
  }
}

// 菜单点击
const onMenuClick = (item) => {
  if (item.route) {
    router.push(item.route)
  } else {
    showToast(`${item.text}功能开发中...`)
  }
}

// 去商品详情
const goToProduct = (id) => {
  router.push(`/product/${id}`)
}

// 更新倒计时
const updateCountdown = () => {
  let hours = parseInt(countdown.hours)
  let minutes = parseInt(countdown.minutes)
  let seconds = parseInt(countdown.seconds)

  if (seconds > 0) {
    seconds--
  } else if (minutes > 0) {
    seconds = 59
    minutes--
  } else if (hours > 0) {
    seconds = 59
    minutes = 59
    hours--
  } else {
    // 重置倒计时
    hours = 2
    minutes = 45
    seconds = 30
  }

  countdown.hours = String(hours).padStart(2, '0')
  countdown.minutes = String(minutes).padStart(2, '0')
  countdown.seconds = String(seconds).padStart(2, '0')
}

onMounted(() => {
  // 启动倒计时
  timer = setInterval(updateCountdown, 1000)
  // 加载热门商品
  loadHotProducts()
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<style scoped>
.home-page {
  background-color: #f7f8fa;
  min-height: 100vh;
  padding-bottom: 50px;
}

.search-bar {
  padding: 10px 16px;
  background-color: white;
}

.banner-swipe {
  height: 200px;
  margin-bottom: 12px;
}

.banner-image {
  width: 100%;
  height: 100%;
}

.menu-section {
  background-color: white;
  padding: 16px;
  margin-bottom: 12px;
}

.seckill-section {
  background-color: white;
  margin-bottom: 12px;
}

.seckill-grid {
  padding: 0 16px 16px;
}

.seckill-image {
  width: 80px;
  height: 80px;
  border-radius: 8px;
}

.seckill-info {
  margin-top: 8px;
  text-align: center;
}

.seckill-price {
  color: #ee0a24;
  font-weight: bold;
  font-size: 14px;
}

.seckill-original {
  color: #969799;
  font-size: 12px;
  text-decoration: line-through;
}

.recommend-section {
  background-color: white;
  padding: 0 16px 16px;
  margin-bottom: 12px;
}

.product-card {
  text-align: left;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.product-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0,0,0,0.15);
}

.product-image {
  width: 100%;
  height: 150px;
  border-radius: 8px 8px 0 0;
  margin-bottom: 8px;
  object-fit: cover;
}

.product-info {
  padding: 8px 12px 12px;
  background: white;
}

.product-title {
  font-size: 15px;
  font-weight: 600;
  color: #323233;
  margin-bottom: 6px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.4;
}

.product-price {
  color: #ee0a24;
  font-weight: bold;
  font-size: 18px;
  margin-bottom: 4px;
}

.product-price::before {
  content: '¥';
  font-size: 14px;
}

.product-sales {
  color: #969799;
  font-size: 12px;
  background: #f7f8fa;
  padding: 2px 6px;
  border-radius: 4px;
  display: inline-block;
}

.guess-section {
  background-color: white;
  padding: 0 16px 16px;
}

.guess-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.guess-item {
  display: flex;
  padding: 16px;
  background-color: #fafafa;
  border-radius: 12px;
  border: 1px solid #f0f0f0;
  transition: all 0.2s ease;
}

.guess-item:active {
  background-color: #f0f0f0;
  transform: scale(0.98);
}

.guess-image {
  width: 90px;
  height: 90px;
  border-radius: 8px;
  margin-right: 16px;
  flex-shrink: 0;
  object-fit: cover;
  border: 1px solid #eee;
}

.guess-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.guess-title {
  font-size: 16px;
  color: #323233;
  font-weight: 600;
  line-height: 1.4;
  margin-bottom: 4px;
}

.guess-desc {
  color: #969799;
  font-size: 13px;
  margin: 6px 0;
  line-height: 1.3;
}

.guess-price {
  color: #ee0a24;
  font-weight: bold;
  font-size: 20px;
}

.guess-price::before {
  content: '¥';
  font-size: 16px;
}

:deep(.van-grid-item__content) {
  padding: 12px 8px;
}

:deep(.van-grid-item__icon) {
  font-size: 28px;
}

:deep(.van-grid-item__text) {
  font-size: 12px;
  margin-top: 8px;
}
</style> 