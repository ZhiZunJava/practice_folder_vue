import { createRouter, createWebHistory } from 'vue-router'
import NProgress from 'nprogress'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '管理员登录' }
  },
  {
    path: '/',
    redirect: '/dashboard',
    component: () => import('../layout/Layout.vue'),
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '仪表盘', icon: 'DataBoard' }
      },
      {
        path: 'categories',
        name: 'Categories',
        component: () => import('../views/CategoryManage.vue'),
        meta: { title: '分类管理', icon: 'Menu' }
      },
      {
        path: 'products',
        name: 'Products',
        component: () => import('../views/ProductManage.vue'),
        meta: { title: '商品管理', icon: 'Goods' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('../views/Profile.vue'),
        meta: { title: '个人资料', icon: 'UserFilled' }
      },
      {
        path: 'icon-test',
        name: 'IconTest',
        component: () => import('../views/IconTest.vue'),
        meta: { title: '图标测试', icon: 'Picture' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  NProgress.start()
  
  // 设置页面标题
  document.title = to.meta.title || '商城后台管理'
  
  // 检查登录状态
  const token = localStorage.getItem('adminToken')
  
  if (to.path === '/login') {
    if (token) {
      next('/')
    } else {
      next()
    }
  } else {
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})

export default router 