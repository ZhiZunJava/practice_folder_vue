<template>
  <div class="layout">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '64px' : '220px'" class="sidebar">
        <div class="logo">
          <div class="logo-icon">
            <el-icon>
              <Shop />
            </el-icon>
          </div>
          <h2 v-if="!isCollapse" class="logo-text">商城管理系统</h2>
        </div>

        <el-menu :default-active="$route.name" :router="true" :collapse="isCollapse" background-color="transparent"
          text-color="rgba(255,255,255,0.8)" active-text-color="#ffffff" class="sidebar-menu">
          <el-menu-item index="dashboard" class="menu-item">
            <el-icon>
              <DataBoard />
            </el-icon>
            <span>仪表盘</span>
          </el-menu-item>

          <el-menu-item index="categories" class="menu-item">
            <el-icon>
              <Collection />
            </el-icon>
            <span>分类管理</span>
          </el-menu-item>
          <el-menu-item index="products" class="menu-item">
            <el-icon>
              <Goods />
            </el-icon>
            <span>商品管理</span>
          </el-menu-item>

          <el-menu-item index="profile" class="menu-item">
            <el-icon>
              <UserFilled />
            </el-icon>
            <span>个人资料</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容区 -->
      <el-container class="main-container">
        <!-- 头部 -->
        <el-header class="header">
          <div class="header-left">
            <el-button text @click="toggleCollapse" class="collapse-btn">
              <el-icon size="20">
                <Expand v-if="isCollapse" />
                <Fold v-else />
              </el-icon>
            </el-button>

            <!-- 面包屑导航 -->
            <el-breadcrumb separator="/" class="breadcrumb">
              <el-breadcrumb-item>{{ getBreadcrumbHome() }}</el-breadcrumb-item>
              <el-breadcrumb-item v-if="getBreadcrumbCurrent()">{{ getBreadcrumbCurrent() }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>

          <div class="header-right">
            <!-- 通知 -->
            <el-badge :value="3" class="notification">
              <el-button text circle>
                <el-icon size="18">
                  <Bell />
                </el-icon>
              </el-button>
            </el-badge>

            <!-- 用户下拉菜单 -->
            <el-dropdown @command="handleCommand" class="user-dropdown">
              <span class="el-dropdown-link">
                <el-avatar :src="userInfo.avatar || '/default-avatar.png'" :size="32" class="user-avatar" />
                <span class="username">{{ userInfo.username || '管理员' }}</span>
                <el-icon class="el-icon--right">
                  <ArrowDown />
                </el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <el-icon>
                      <User />
                    </el-icon>
                    个人资料
                  </el-dropdown-item>
                  <el-dropdown-item command="settings">
                    <el-icon>
                      <Setting />
                    </el-icon>
                    系统设置
                  </el-dropdown-item>
                  <el-dropdown-item command="logout" divided>
                    <el-icon>
                      <SwitchButton />
                    </el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <!-- 内容区 -->
        <el-main class="main-content">
          <div class="content-wrapper">
            <router-view />
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { adminApi } from '@/api'

const router = useRouter()
const route = useRoute()

// 响应式数据
const isCollapse = ref(false)
const userInfo = reactive({
  username: '',
  avatar: ''
})

// 菜单标题映射
const menuTitles = {
  dashboard: '仪表盘',
  categories: '分类管理',
  products: '商品管理',
  profile: '个人资料'
}

// 获取面包屑首页名称
const getBreadcrumbHome = () => {
  return '首页'
}

// 获取面包屑当前页面名称
const getBreadcrumbCurrent = () => {
  return menuTitles[route.name] || ''
}

// 切换侧边栏收缩状态
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

// 处理下拉菜单命令
const handleCommand = (command) => {
  if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'settings') {
    ElMessage.info('系统设置功能开发中')
  } else if (command === 'logout') {
    logout()
  }
}

// 退出登录
const logout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '退出提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    localStorage.removeItem('adminToken')
    localStorage.removeItem('adminInfo')

    ElMessage.success('退出成功')
    router.push('/login')
  } catch (error) {
    // 用户取消
  }
}

// 加载用户信息
const loadUserInfo = async () => {
  try {
    // 先从本地存储读取
    const localInfo = localStorage.getItem('adminInfo')
    if (localInfo) {
      Object.assign(userInfo, JSON.parse(localInfo))
    }

    // 从服务器获取最新信息
    const res = await adminApi.getInfo()
    Object.assign(userInfo, res.data)
    localStorage.setItem('adminInfo', JSON.stringify(res.data))
  } catch (error) {
    console.error('加载用户信息失败:', error)
  }
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.layout {
  height: 100vh;
  background: #f0f2f5;
}

.sidebar {
  background: linear-gradient(180deg, #1890ff 0%, #096dd9 100%);
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
  transition: width 0.3s ease;
  position: relative;
  z-index: 1001;
}

.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  margin-bottom: 16px;
}

.logo-icon {
  width: 32px;
  height: 32px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  margin-right: 12px;
}

.logo-text {
  color: white;
  font-size: 16px;
  font-weight: 600;
  margin: 0;
  white-space: nowrap;
}

.sidebar-menu {
  border-right: none;
  background: transparent !important;
}

.menu-item {
  margin: 4px 12px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.menu-item:hover {
  background: rgba(255, 255, 255, 0.1) !important;
}

.menu-item.is-active {
  background: rgba(255, 255, 255, 0.15) !important;
  border-left: 3px solid #ffffff;
}

.sub-menu-item {
  margin: 2px 8px;
  border-radius: 6px;
}

.main-container {
  background: #f0f2f5;
}

.header {
  background: #ffffff;
  border-bottom: 1px solid #e8e8e8;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  position: relative;
  z-index: 1000;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-btn {
  color: #666;
  transition: all 0.3s ease;
}

.collapse-btn:hover {
  color: #1890ff;
  background: #f0f9ff;
}

.breadcrumb {
  font-size: 14px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.notification {
  margin-right: 8px;
}

.user-dropdown {
  cursor: pointer;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #333;
  transition: all 0.3s ease;
}

.el-dropdown-link:hover {
  color: #1890ff;
}

.user-avatar {
  border: 2px solid #e8e8e8;
}

.username {
  font-size: 14px;
  font-weight: 500;
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.main-content {
  padding: 24px;
  background: #f0f2f5;
  min-height: calc(100vh - 64px);
}

.content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header {
    padding: 0 16px;
  }

  .main-content {
    padding: 16px;
  }

  .username {
    display: none;
  }
}

/* 滚动条美化 */
:deep(.el-menu) {
  overflow-y: auto;
  max-height: calc(100vh - 80px);
}

:deep(.el-menu::-webkit-scrollbar) {
  width: 4px;
}

:deep(.el-menu::-webkit-scrollbar-track) {
  background: rgba(255, 255, 255, 0.1);
}

:deep(.el-menu::-webkit-scrollbar-thumb) {
  background: rgba(255, 255, 255, 0.3);
  border-radius: 2px;
}

:deep(.el-menu::-webkit-scrollbar-thumb:hover) {
  background: rgba(255, 255, 255, 0.5);
}
</style>