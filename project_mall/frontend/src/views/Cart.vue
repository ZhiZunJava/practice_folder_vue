<template>
  <div class="cart-page">
    <!-- 导航栏 -->
    <van-nav-bar title="购物车" left-arrow @click-left="$router.back()" fixed />
    
    <!-- 购物车列表 -->
    <div class="cart-content">
      <van-loading v-if="loading" type="spinner" size="24px" class="loading-container">
        加载中...
      </van-loading>
      
      <div v-else-if="cartItems.length > 0" class="cart-list">
        <van-checkbox-group v-model="checkedItems" ref="checkboxGroup">
          <div v-for="item in cartItems" :key="item.id" class="cart-item">
            <van-swipe-cell>
              <div class="item-content">
                <div class="item-checkbox">
                  <van-checkbox 
                    :name="item.id" 
                    @click.stop
                    icon-size="20px"
                  />
                </div>
                
                <div class="item-image" @click="goToDetail(item.productId)">
                  <van-image
                    :src="item.productImage"
                    fit="cover"
                    width="80"
                    height="80"
                    radius="8"
                    loading-icon="photo"
                    error-icon="photo"
                  />
                </div>
                
                <div class="item-info" @click="goToDetail(item.productId)">
                  <div class="item-name">{{ item.productName }}</div>
                  <div class="item-spec">规格：默认</div>
                  <div class="item-price">¥{{ item.price }}</div>
                </div>
                
                <div class="item-action">
                  <van-stepper 
                    v-model="item.quantity" 
                    @change="updateQuantity(item.id, $event)"
                    min="1"
                    button-size="22"
                    input-width="32"
                  />
                </div>
              </div>
              
              <template #right>
                <van-button 
                  square 
                  type="danger" 
                  text="删除" 
                  @click="removeItem(item.id)"
                  class="delete-button"
                />
              </template>
            </van-swipe-cell>
          </div>
        </van-checkbox-group>
      </div>
      
      <!-- 空状态 -->
      <div v-else class="empty-cart">
        <van-empty 
          image="https://fastly.jsdelivr.net/npm/@vant/assets/custom-empty-image.png"
          description="购物车空空如也"
          class="empty-state"
        >
          <van-button 
            type="primary" 
            size="small"
            round
            @click="$router.push('/')"
          >
            去逛逛
          </van-button>
        </van-empty>
      </div>
    </div>
    
    <!-- 底部操作栏 -->
    <van-submit-bar
      v-if="cartItems.length > 0"
      :price="totalPrice * 100"
      button-text="结算"
      @submit="handleSubmit"
      class="submit-bar"
    >
      <template #default>
        <van-checkbox 
          v-model="checkedAll" 
          @change="onCheckAll"
          icon-size="20px"
        >
          全选
        </van-checkbox>
      </template>
      <template #tip>
        <span class="selected-info">
          已选择 <span class="selected-count">{{ checkedItems.length }}</span> 件商品
        </span>
      </template>
    </van-submit-bar>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
import { useRouter } from 'vue-router'

const router = useRouter()

// 响应式数据
const cartItems = ref([])
const checkedItems = ref([])
const checkboxGroup = ref()
const loading = ref(false)

// 全选状态
const checkedAll = computed({
  get() {
    return checkedItems.value.length === cartItems.value.length && cartItems.value.length > 0
  },
  set(val) {
    if (val) {
      checkedItems.value = cartItems.value.map(item => item.id)
    } else {
      checkedItems.value = []
    }
  }
})

// 总价
const totalPrice = computed(() => {
  return cartItems.value
    .filter(item => checkedItems.value.includes(item.id))
    .reduce((total, item) => total + item.price * item.quantity, 0)
})

// 加载购物车数据（从localStorage）
const loadCartData = () => {
  loading.value = true
  
  try {
    const savedCart = localStorage.getItem('cartItems')
    if (savedCart) {
      const parsedCart = JSON.parse(savedCart)
      if (Array.isArray(parsedCart) && parsedCart.length > 0) {
        cartItems.value = parsedCart
        // 默认全选
        checkedItems.value = cartItems.value.map(item => item.id)
        loading.value = false
        return
      }
    }
    
    // 如果没有保存的购物车数据，添加一些示例商品
    cartItems.value = [
      {
        id: 1,
        productId: 1,
        productName: 'iPhone 15 Pro',
        productImage: 'https://img01.yzcdn.cn/vant/ipad.jpeg',
        price: 7999,
        quantity: 1
      },
      {
        id: 2,
        productId: 2,
        productName: 'AirPods Pro',
        productImage: 'https://img.yzcdn.cn/vant/cat.jpeg',
        price: 1999,
        quantity: 2
      }
    ]
    
    // 默认全选
    checkedItems.value = cartItems.value.map(item => item.id)
    saveCartData()
    
  } catch (error) {
    console.error('解析购物车数据失败:', error)
    cartItems.value = []
  } finally {
    loading.value = false
  }
}

// 保存购物车数据到localStorage
const saveCartData = () => {
  localStorage.setItem('cartItems', JSON.stringify(cartItems.value))
}

// 全选操作
const onCheckAll = (val) => {
  checkedAll.value = val
}

// 更新数量
const updateQuantity = (id, quantity) => {
  const item = cartItems.value.find(item => item.id === id)
  if (item) {
    item.quantity = quantity
    saveCartData()
    showToast(`已更新数量为${quantity}`)
  }
}

// 删除商品
const removeItem = async (id) => {
  try {
    await showConfirmDialog({
      title: '确认删除',
      message: '确定要删除这件商品吗？'
    })
    
    // 从本地数据中移除
    const index = cartItems.value.findIndex(item => item.id === id)
    if (index > -1) {
      cartItems.value.splice(index, 1)
      // 同时从选中项中移除
      const checkedIndex = checkedItems.value.indexOf(id)
      if (checkedIndex > -1) {
        checkedItems.value.splice(checkedIndex, 1)
      }
      saveCartData()
      showToast('删除成功')
    }
  } catch (error) {
    if (error.message !== 'cancel') {
      console.error('删除商品失败:', error)
    }
  }
}

// 去商品详情
const goToDetail = (productId) => {
  router.push(`/product/${productId}`)
}

// 结算
const handleSubmit = () => {
  if (checkedItems.value.length === 0) {
    showToast('请选择要结算的商品')
    return
  }
  
  showToast('功能开发中...')
}

// 添加商品到购物车（供其他页面调用）
const addToCart = (product) => {
  const existingItem = cartItems.value.find(item => item.productId === product.id)
  
  if (existingItem) {
    existingItem.quantity += 1
  } else {
    const newItem = {
      id: Date.now(), // 使用时间戳作为ID
      productId: product.id,
      productName: product.name,
      productImage: product.image || product.mainImage,
      price: product.price,
      quantity: 1
    }
    cartItems.value.push(newItem)
    checkedItems.value.push(newItem.id)
  }
  
  saveCartData()
  showToast('已加入购物车')
}

// 监听购物车变化，自动保存
watch(cartItems, saveCartData, { deep: true })

// 页面加载时获取数据
onMounted(() => {
  loadCartData()
})

// 导出addToCart方法供其他组件使用
window.addToCart = addToCart
</script>

<style scoped>
.cart-page {
  background-color: #f7f8fa;
  min-height: 100vh;
  padding-top: 46px; /* 为固定导航栏留出空间 */
  padding-bottom: 80px; /* 为底部操作栏留出空间 */
}

.cart-content {
  min-height: calc(100vh - 126px);
}

.loading-container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 100px 0;
  color: #969799;
}

.cart-list {
  padding: 12px;
}

.cart-item {
  margin-bottom: 12px;
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.item-content {
  display: flex;
  align-items: center;
  padding: 16px;
}

.item-checkbox {
  margin-right: 12px;
}

.item-image {
  margin-right: 12px;
  cursor: pointer;
}

.item-info {
  flex: 1;
  cursor: pointer;
}

.item-name {
  font-size: 16px;
  font-weight: 600;
  color: #323233;
  margin-bottom: 4px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.item-spec {
  font-size: 12px;
  color: #969799;
  margin-bottom: 8px;
}

.item-price {
  font-size: 18px;
  font-weight: bold;
  color: #ee0a24;
}

.item-price::before {
  content: '¥';
  font-size: 14px;
}

.item-action {
  margin-left: 12px;
}

.delete-button {
  height: 100%;
  width: 80px;
}

.empty-cart {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  padding: 40px 20px;
}

.empty-state {
  text-align: center;
}

:deep(.van-submit-bar) {
  position: fixed;
  bottom: 50px; /* 给底部导航栏留出空间 */
  left: 0;
  right: 0;
  z-index: 100;
  border-top: 1px solid #eee;
  box-shadow: 0 -2px 8px rgba(0,0,0,0.1);
}

:deep(.van-submit-bar__tip) {
  background: transparent;
  padding: 0;
}

.selected-info {
  font-size: 14px;
  color: #606266;
}

.selected-count {
  font-weight: bold;
  color: #ee0a24;
}

:deep(.van-checkbox) {
  margin-right: 0;
}

:deep(.van-stepper) {
  border-radius: 6px;
}
</style> 