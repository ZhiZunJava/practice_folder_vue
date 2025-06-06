<template>
  <div class="profile-page">
    <!-- 用户信息区域 -->
    <div class="user-section">
      <div v-if="isLoggedIn" class="user-info">
        <div class="user-avatar">
          <van-image
            :src="userInfo.avatar || defaultAvatar"
            round
            width="60"
            height="60"
            fit="cover"
          />
        </div>
        <div class="user-details">
          <div class="user-name">{{ userInfo.nickname || '用户' }}</div>
          <div class="user-level">{{ userInfo.level || 'VIP会员' }}</div>
        </div>
        <div class="user-actions">
          <van-button size="mini" @click="editProfile">编辑</van-button>
        </div>
      </div>
      
      <div v-else class="login-section">
        <div class="login-avatar">
          <van-image :src="defaultAvatar" round width="60" height="60" />
        </div>
        <div class="login-prompt">
          <div class="login-text">登录享受更多权益</div>
          <van-button type="primary" size="small" @click="showLogin = true">
            登录 / 注册
          </van-button>
        </div>
      </div>
    </div>

    <!-- 会员权益 -->
    <div v-if="isLoggedIn" class="member-section">
      <van-grid :column-num="4" :gutter="0" :border="false">
        <van-grid-item>
          <div class="member-item">
            <div class="member-value">{{ userInfo.points || 0 }}</div>
            <div class="member-label">积分</div>
          </div>
        </van-grid-item>
        <van-grid-item>
          <div class="member-item">
            <div class="member-value">{{ userInfo.coupons || 0 }}</div>
            <div class="member-label">优惠券</div>
          </div>
        </van-grid-item>
        <van-grid-item>
          <div class="member-item">
            <div class="member-value">{{ userInfo.favorites || 0 }}</div>
            <div class="member-label">收藏</div>
          </div>
        </van-grid-item>
        <van-grid-item>
          <div class="member-item">
            <div class="member-value">{{ userInfo.footprints || 0 }}</div>
            <div class="member-label">足迹</div>
          </div>
        </van-grid-item>
      </van-grid>
    </div>

    <!-- 我的订单 -->
    <div class="order-section">
      <van-cell-group>
        <van-cell title="我的订单" is-link @click="$router.push('/orders')" value="查看全部订单">
          <template #icon>
            <van-icon name="orders-o" />
          </template>
        </van-cell>
      </van-cell-group>
      
      <van-grid :column-num="5" :gutter="0" class="order-grid">
        <van-grid-item
          v-for="item in orderTypes"
          :key="item.type"
          @click="goToOrders(item.type)"
        >
          <van-icon :name="item.icon" size="24" />
          <div class="order-text">{{ item.text }}</div>
          <van-badge v-if="item.count > 0" :content="item.count" :offset="[10, -10]" />
        </van-grid-item>
      </van-grid>
    </div>

    <!-- 功能菜单 -->
    <div class="menu-section">
      <van-cell-group>
        <van-cell
          v-for="item in menuItems"
          :key="item.id"
          :title="item.title"
          :value="item.value"
          is-link
          @click="onMenuClick(item)"
        >
          <template #icon>
            <van-icon :name="item.icon" color="#1989fa" />
          </template>
          <template #right-icon>
            <van-badge v-if="item.badge" :content="item.badge" />
          </template>
        </van-cell>
      </van-cell-group>
    </div>

    <!-- 设置选项 -->
    <div class="setting-section">
      <van-cell-group>
        <van-cell
          v-for="item in settingItems"
          :key="item.id"
          :title="item.title"
          is-link
          @click="onSettingClick(item)"
        >
          <template #icon>
            <van-icon :name="item.icon" color="#969799" />
          </template>
        </van-cell>
        
        <van-cell v-if="isLoggedIn" title="退出登录" @click="logout">
          <template #icon>
            <van-icon name="exit" color="#ee0a24" />
          </template>
        </van-cell>
      </van-cell-group>
    </div>

    <!-- 登录弹窗 -->
    <van-popup v-model:show="showLogin" position="bottom" :style="{ height: '60%' }">
      <div class="login-popup">
        <div class="login-header">
          <van-nav-bar title="登录" left-arrow @click-left="showLogin = false" />
        </div>
        
        <div class="login-form">
          <van-form @submit="handleLogin">
            <van-cell-group inset>
              <van-field
                v-model="loginForm.username"
                name="username"
                label="用户名"
                placeholder="请输入用户名"
                :rules="[{ required: true, message: '请输入用户名' }]"
              />
              <van-field
                v-model="loginForm.password"
                type="password"
                name="password"
                label="密码"
                placeholder="请输入密码"
                :rules="[{ required: true, message: '请输入密码' }]"
              />
            </van-cell-group>
            
            <div class="login-actions">
              <van-button
                round
                block
                type="primary"
                native-type="submit"
                :loading="loginLoading"
              >
                登录
              </van-button>
            </div>
          </van-form>
          
          <div class="login-tips">
            <p>登录即表示同意《用户协议》和《隐私政策》</p>
          </div>
        </div>
      </div>
    </van-popup>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { showToast, showConfirmDialog } from 'vant'
import { userApi } from '@/api'

const router = useRouter()

// 响应式数据
const showLogin = ref(false)
const loginLoading = ref(false)
const defaultAvatar = 'https://img.yzcdn.cn/vant/cat.jpeg'

// 登录表单
const loginForm = reactive({
  username: '',
  password: ''
})

// 用户信息
const userInfo = ref({
  id: null,
  nickname: '',
  avatar: '',
  level: '',
  points: 0,
  coupons: 0,
  favorites: 0,
  footprints: 0
})

// 登录状态
const isLoggedIn = computed(() => {
  return !!localStorage.getItem('userToken') || !!userInfo.value.id
})

// 订单类型
const orderTypes = ref([
  { type: 'pending', text: '待付款', icon: 'pending-payment', count: 2 },
  { type: 'shipped', text: '待发货', icon: 'tosend', count: 0 },
  { type: 'delivery', text: '待收货', icon: 'logistics', count: 1 },
  { type: 'comment', text: '待评价', icon: 'comment-o', count: 3 },
  { type: 'return', text: '退换货', icon: 'after-sale', count: 0 }
])

// 功能菜单
const menuItems = ref([
  { id: 1, title: '我的收藏', icon: 'star-o', route: '/favorites' },
  { id: 2, title: '浏览足迹', icon: 'eye-o', route: '/footprints' },
  { id: 3, title: '收货地址', icon: 'location-o', route: '/address' },
  { id: 4, title: '优惠券', icon: 'coupon-o', route: '/coupons', badge: '3' },
  { id: 5, title: '积分商城', icon: 'gift-o', route: '/points-mall' },
  { id: 6, title: '邀请好友', icon: 'friends-o', route: '/invite' },
  { id: 7, title: '客服中心', icon: 'service-o', action: 'contact' }
])

// 设置选项
const settingItems = ref([
  { id: 1, title: '设置', icon: 'setting-o', route: '/settings' },
  { id: 2, title: '帮助中心', icon: 'question-o', route: '/help' },
  { id: 3, title: '关于我们', icon: 'info-o', route: '/about' }
])

// 处理登录
const handleLogin = async () => {
  if (!loginForm.username || !loginForm.password) {
    showToast('请填写完整信息')
    return
  }
  
  loginLoading.value = true
  
  try {
    // 调用真实登录API
    const response = await userApi.login({
      username: loginForm.username,
      password: loginForm.password
    })
    
    if (response.data) {
      const { token, userInfo: userData } = response.data
      
      // 保存token和用户信息
      localStorage.setItem('userToken', token)
      localStorage.setItem('userInfo', JSON.stringify(userData))
      
      // 更新本地用户信息
      userInfo.value = {
        id: userData.id,
        nickname: userData.nickname || userData.username,
        avatar: userData.avatar || '',
        level: 'VIP会员',
        points: userData.points || 0,
        coupons: userData.coupons || 0,
        favorites: userData.favorites || 0,
        footprints: userData.footprints || 0
      }
      
      showToast('登录成功')
      showLogin.value = false
      
      // 重置表单
      loginForm.username = ''
      loginForm.password = ''
    }
    
  } catch (error) {
    console.error('登录失败:', error)
    showToast(error.message || '登录失败，请重试')
  } finally {
    loginLoading.value = false
  }
}

// 退出登录
const logout = async () => {
  try {
    await showConfirmDialog({
      title: '确认退出',
      message: '确定要退出登录吗？'
    })
    
    userInfo.value = {
      id: null,
      nickname: '',
      avatar: '',
      level: '',
      points: 0,
      coupons: 0,
      favorites: 0,
      footprints: 0
    }
    
    localStorage.removeItem('userToken')
    localStorage.removeItem('userInfo')
    
    showToast('已退出登录')
  } catch {
    // 用户取消
  }
}

// 编辑资料
const editProfile = () => {
  showToast('编辑资料功能开发中...')
}

// 去订单页面
const goToOrders = (type) => {
  if (!isLoggedIn.value) {
    showLogin.value = true
    return
  }
  router.push(`/orders?type=${type}`)
}

// 菜单点击
const onMenuClick = (item) => {
  if (!isLoggedIn.value && item.id !== 7) {
    showLogin.value = true
    return
  }
  
  if (item.action === 'contact') {
    showToast('客服功能开发中...')
  } else if (item.route) {
    router.push(item.route)
  }
}

// 设置点击
const onSettingClick = (item) => {
  if (item.route) {
    router.push(item.route)
  } else {
    showToast(`${item.title}功能开发中...`)
  }
}

// 初始化用户信息
const initUserInfo = () => {
  const savedUserInfo = localStorage.getItem('userInfo')
  if (savedUserInfo) {
    try {
      userInfo.value = JSON.parse(savedUserInfo)
    } catch (error) {
      console.error('解析用户信息失败:', error)
    }
  }
}

// 页面加载时初始化
initUserInfo()
</script>

<style scoped>
.profile-page {
  background-color: #f7f8fa;
  min-height: 100vh;
  padding-bottom: 60px; /* 为底部导航栏留出空间 */
}

.user-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px 16px;
  color: white;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-details {
  flex: 1;
}

.user-name {
  font-size: 18px;
  font-weight: 500;
  margin-bottom: 4px;
}

.user-level {
  font-size: 14px;
  opacity: 0.8;
}

.login-section {
  display: flex;
  align-items: center;
  gap: 16px;
}

.login-prompt {
  flex: 1;
}

.login-text {
  font-size: 16px;
  margin-bottom: 8px;
}

.member-section {
  background-color: white;
  margin: 12px 16px;
  border-radius: 8px;
  overflow: hidden;
}

.member-item {
  text-align: center;
  padding: 16px 0;
}

.member-value {
  font-size: 18px;
  font-weight: bold;
  color: #323233;
  margin-bottom: 4px;
}

.member-label {
  font-size: 12px;
  color: #969799;
}

.order-section {
  margin: 12px 16px;
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
}

.order-grid {
  border-top: 1px solid #f0f0f0;
}

.order-text {
  font-size: 12px;
  color: #646566;
  margin-top: 4px;
}

.menu-section,
.setting-section {
  margin: 12px 16px;
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
}

.login-popup {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.login-header {
  border-bottom: 1px solid #eee;
}

.login-form {
  flex: 1;
  padding: 24px;
}

.login-actions {
  margin: 24px 0;
}

.login-tips {
  text-align: center;
  margin-top: 20px;
}

.login-tips p {
  font-size: 12px;
  color: #969799;
  line-height: 1.5;
}

/* 修复图标对齐问题 */
:deep(.van-cell__left-icon) {
  margin-right: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
}

:deep(.van-grid-item__content) {
  padding: 12px 8px;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

:deep(.van-grid-item__icon) {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 4px;
}

:deep(.van-grid-item__text) {
  font-size: 12px;
  color: #646566;
  text-align: center;
}

:deep(.van-badge) {
  position: absolute;
  top: 8px;
  right: 8px;
  transform: none;
}

:deep(.van-cell) {
  display: flex;
  align-items: center;
  padding: 12px 16px;
}

:deep(.van-cell__title) {
  font-weight: 500;
  color: #323233;
}

:deep(.van-cell__value) {
  color: #969799;
  font-size: 14px;
}

:deep(.van-icon) {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style> 