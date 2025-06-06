<template>
  <div id="app">
    <!-- 主内容区域 -->
    <router-view />
    
    <!-- 底部导航栏 -->
    <van-tabbar
      v-if="showTabbar"
      v-model="activeTab"
      @change="onTabChange"
      :placeholder="true"
      :safe-area-inset-bottom="true"
    >
      <van-tabbar-item 
        name="home" 
        icon="home-o"
        to="/"
      >
        首页
      </van-tabbar-item>
      
      <van-tabbar-item 
        name="category" 
        icon="apps-o"
        to="/category"
      >
        分类
      </van-tabbar-item>
      
      <van-tabbar-item 
        name="cart" 
        icon="shopping-cart-o"
        :badge="cartCount > 0 ? cartCount : null"
        to="/cart"
      >
        购物车
      </van-tabbar-item>
      
      <van-tabbar-item 
        name="message" 
        icon="chat-o"
        :dot="hasNewMessage"
        to="/message"
      >
        消息
      </van-tabbar-item>
      
      <van-tabbar-item 
        name="profile" 
        icon="user-o"
        to="/profile"
      >
        我的
      </van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const activeTab = ref('home')

// 是否显示底部导航栏
const showTabbar = computed(() => {
  return route.meta?.showTabbar || false
})

// 购物车商品数量
const cartCount = ref(0)

// 是否有新消息（模拟数据）
const hasNewMessage = ref(true)

// 获取购物车数量（从本地存储）
const getCartCount = () => {
  try {
    const cartItems = JSON.parse(localStorage.getItem('cartItems') || '[]')
    cartCount.value = cartItems.length
  } catch (error) {
    console.error('获取购物车数量失败:', error)
    cartCount.value = 0
  }
}

// 设置全局更新购物车数量的方法
window.updateCartCount = getCartCount

// 标签页切换事件
const onTabChange = (name) => {
  activeTab.value = name
  // 当切换到购物车时，更新数量
  if (name === 'cart') {
    getCartCount()
  }
}

// 监听路由变化，更新当前激活的标签
watch(() => route.path, (newPath) => {
  switch (newPath) {
    case '/':
      activeTab.value = 'home'
      break
    case '/category':
      activeTab.value = 'category'
      break
    case '/cart':
      activeTab.value = 'cart'
      getCartCount() // 进入购物车页面时更新数量
      break
    case '/message':
      activeTab.value = 'message'
      break
    case '/profile':
      activeTab.value = 'profile'
      break
    default:
      // 其他页面不更改激活状态
      break
  }
}, { immediate: true })

// 监听localStorage变化
window.addEventListener('storage', (e) => {
  if (e.key === 'cartItems') {
    getCartCount()
  }
})

onMounted(() => {
  // 页面加载时获取购物车数量
  getCartCount()
  
  // 定期更新购物车数量
  setInterval(getCartCount, 1000)
})
</script>

<style>
#app {
  font-family: -apple-system, BlinkMacSystemFont, 'Helvetica Neue', Helvetica, Segoe UI, Arial, Roboto, 'PingFang SC', 'miui', 'Hiragino Sans GB', 'Microsoft Yahei', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

/* 全局样式 */
* {
  box-sizing: border-box;
}

body {
  margin: 0;
  padding: 0;
  background-color: #f7f8fa;
}

/* 底部导航栏样式 */
.van-tabbar {
  background-color: white;
  border-top: 1px solid #eee;
  box-shadow: 0 -2px 8px rgba(0,0,0,0.08);
  z-index: 999;
  position: fixed;
}

.van-tabbar-item {
  color: #646566;
  transition: color 0.2s ease;
}

.van-tabbar-item--active {
  color: #1989fa;
}

.van-tabbar-item__icon {
  font-size: 22px;
  margin-bottom: 2px;
}

.van-tabbar-item__text {
  font-size: 12px;
  margin-top: 2px;
  font-weight: 500;
}
</style> 