<template>
  <div class="category-page">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <van-search
        v-model="searchValue"
        placeholder="搜索商品"
        @search="onSearch"
        show-action
      >
        <template #action>
          <div @click="onSearch(searchValue)">搜索</div>
        </template>
      </van-search>
    </div>

    <!-- 分类内容 -->
    <div class="category-content">
      <!-- 左侧分类菜单 -->
      <div class="category-sidebar">
        <van-sidebar v-model="activeCategory" @change="onCategoryChange">
          <van-sidebar-item
            v-for="category in categories"
            :key="category.id"
            :title="category.name"
            :badge="category.badge"
          />
        </van-sidebar>
      </div>

      <!-- 右侧内容区域 -->
      <div class="category-main">
        <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
          <!-- 子分类 -->
          <div v-if="currentCategory?.children?.length" class="subcategory-section">
            <div class="section-title">{{ currentCategory.name }}</div>
            <van-grid :column-num="3" :gutter="8" class="subcategory-grid">
              <van-grid-item
                v-for="subcat in currentCategory.children"
                :key="subcat.id"
                @click="goToProductList(subcat)"
                class="subcategory-item"
              >
                <van-image 
                  :src="subcat.icon" 
                  fit="cover" 
                  class="subcategory-image"
                  loading-icon="photo"
                  error-icon="photo"
                />
                <div class="subcategory-name">{{ subcat.name }}</div>
              </van-grid-item>
            </van-grid>
          </div>

          <!-- 空状态 -->
          <div v-else class="empty-category">
            <van-empty 
              image="https://fastly.jsdelivr.net/npm/@vant/assets/custom-empty-image.png"
              description="该分类暂无子分类"
            />
          </div>
        </van-pull-refresh>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import { categoryApi } from '@/api'

const router = useRouter()

// 响应式数据
const searchValue = ref('')
const activeCategory = ref(0)
const refreshing = ref(false)

// 分类数据
const categories = ref([
  {
    id: 1,
    name: '数码电器',
    badge: 'Hot',
    children: [
      { id: 11, name: '手机', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 12, name: '电脑', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 13, name: '平板', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 14, name: '耳机', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 15, name: '音响', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 16, name: '智能手表', image: 'https://img.yzcdn.cn/vant/cat.jpeg' }
    ]
  },
  {
    id: 2,
    name: '服装鞋帽',
    children: [
      { id: 21, name: '男装', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 22, name: '女装', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 23, name: '童装', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 24, name: '运动鞋', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 25, name: '皮鞋', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 26, name: '帽子', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' }
    ]
  },
  {
    id: 3,
    name: '美妆护肤',
    badge: 'New',
    children: [
      { id: 31, name: '彩妆', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 32, name: '护肤', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 33, name: '香水', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 34, name: '洗护', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 35, name: '美容工具', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 36, name: '男士护理', image: 'https://img.yzcdn.cn/vant/cat.jpeg' }
    ]
  },
  {
    id: 4,
    name: '家居生活',
    children: [
      { id: 41, name: '家具', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 42, name: '家电', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 43, name: '厨具', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 44, name: '家纺', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 45, name: '装饰', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 46, name: '收纳', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' }
    ]
  },
  {
    id: 5,
    name: '食品饮料',
    children: [
      { id: 51, name: '零食', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 52, name: '饮料', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 53, name: '茶叶', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 54, name: '咖啡', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 55, name: '保健品', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 56, name: '生鲜', image: 'https://img.yzcdn.cn/vant/cat.jpeg' }
    ]
  },
  {
    id: 6,
    name: '运动户外',
    children: [
      { id: 61, name: '运动服装', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 62, name: '运动器材', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 63, name: '户外装备', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 64, name: '健身器材', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 65, name: '球类', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 66, name: '游泳用品', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' }
    ]
  },
  {
    id: 7,
    name: '母婴玩具',
    children: [
      { id: 71, name: '奶粉', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 72, name: '纸尿裤', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 73, name: '玩具', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 74, name: '童车', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 75, name: '童床', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 76, name: '孕妇用品', image: 'https://img.yzcdn.cn/vant/cat.jpeg' }
    ]
  },
  {
    id: 8,
    name: '图书文具',
    children: [
      { id: 81, name: '图书', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 82, name: '文具', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 83, name: '办公用品', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 84, name: '教学用品', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' },
      { id: 85, name: '艺术用品', image: 'https://img.yzcdn.cn/vant/cat.jpeg' },
      { id: 86, name: '电子书', image: 'https://img01.yzcdn.cn/vant/ipad.jpeg' }
    ]
  }
])

// 当前选中的分类
const currentCategory = computed(() => {
  return categories.value[activeCategory.value]
})

// 分类切换
const onCategoryChange = (index) => {
  activeCategory.value = index
}

// 刷新
const onRefresh = async () => {
  refreshing.value = true
  
  try {
    // 尝试从API加载分类数据
    const response = await categoryApi.getTree()
    if (response.data && response.data.length > 0) {
      categories.value = response.data
    }
  } catch (error) {
    console.log('API调用失败，使用本地数据:', error.message)
  } finally {
    refreshing.value = false
  }
}

// 搜索
const onSearch = (value) => {
  if (!value.trim()) {
    showToast('请输入搜索内容')
    return
  }
  
  router.push({
    path: '/search',
    query: { keyword: value.trim() }
  })
}

// 跳转到商品列表
const goToProductList = (subcategory) => {
  router.push({
    path: `/category/${subcategory.id}/products`,
    query: {
      categoryName: subcategory.name
    }
  })
}

// 页面初始化
onMounted(() => {
  onRefresh()
})
</script>

<style scoped>
.category-page {
  background-color: #f7f8fa;
  min-height: 100vh;
  padding-bottom: 60px; /* 为底部导航栏留出空间 */
}

.search-bar {
  background: white;
  padding: 8px 16px;
  border-bottom: 1px solid #eee;
}

.category-content {
  display: flex;
  height: calc(100vh - 120px); /* 减去搜索栏和底部导航栏的高度 */
}

.category-sidebar {
  width: 90px;
  background: white;
  border-right: 1px solid #eee;
}

.category-main {
  flex: 1;
  background: #f7f8fa;
  overflow-y: auto;
}

.subcategory-section {
  padding: 16px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #323233;
  margin-bottom: 16px;
  padding-left: 4px;
}

.subcategory-grid {
  background: transparent;
}

.subcategory-item {
  transition: all 0.2s ease;
}

.subcategory-item:active {
  transform: scale(0.95);
}

.subcategory-image {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  margin-bottom: 8px;
  border: 1px solid #f0f0f0;
}

.subcategory-name {
  font-size: 12px;
  color: #646566;
  text-align: center;
  line-height: 1.2;
}

.empty-category {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  padding: 40px 20px;
}

:deep(.van-sidebar) {
  height: 100%;
}

:deep(.van-sidebar-item) {
  padding: 20px 8px;
  font-size: 12px;
  line-height: 1.4;
  text-align: center;
}

:deep(.van-sidebar-item--select) {
  background: #f7f8fa;
  color: #1989fa;
  font-weight: 600;
}

:deep(.van-grid-item__content) {
  padding: 12px 8px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

:deep(.van-grid-item__content:active) {
  background: #f7f8fa;
}

:deep(.van-search__action) {
  color: #1989fa;
  font-size: 14px;
}
</style> 