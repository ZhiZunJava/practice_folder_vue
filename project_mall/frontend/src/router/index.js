import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import Category from '@/views/Category.vue'
import ProductDetail from '@/views/ProductDetail.vue'
import ProductList from '@/views/ProductList.vue'
import Cart from '@/views/Cart.vue'
import Message from '@/views/Message.vue'
import Profile from '@/views/Profile.vue'
import Login from '@/views/Login.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: { 
      title: '首页',
      showTabbar: true
    }
  },
  {
    path: '/category',
    name: 'Category',
    component: Category,
    meta: { 
      title: '分类',
      showTabbar: true
    }
  },
  {
    path: '/category/:categoryId/products',
    name: 'ProductList',
    component: ProductList,
    meta: { 
      title: '商品列表'
    }
  },
  {
    path: '/cart',
    name: 'Cart',
    component: Cart,
    meta: { 
      title: '购物车',
      showTabbar: true
    }
  },
  {
    path: '/message',
    name: 'Message',
    component: Message,
    meta: { 
      title: '消息',
      showTabbar: true
    }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile,
    meta: { 
      title: '我的',
      showTabbar: true
    }
  },
  {
    path: '/product/:id',
    name: 'ProductDetail',
    component: ProductDetail,
    meta: { 
      title: '商品详情'
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { 
      title: '登录'
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})

export default router 