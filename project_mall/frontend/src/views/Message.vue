<template>
  <div class="message-page">
    <!-- 导航栏 -->
    <van-nav-bar title="消息" left-arrow @click-left="$router.back()">
      <template #right>
        <van-icon name="ellipsis" @click="showMoreActions" />
      </template>
    </van-nav-bar>

    <!-- 消息列表 -->
    <div class="message-content">
      <!-- 系统消息 -->
      <van-cell-group title="系统消息" inset>
        <van-cell
          v-for="item in systemMessages"
          :key="item.id"
          :title="item.title"
          :label="item.content"
          :value="formatTime(item.time)"
          is-link
          @click="openMessage(item)"
        >
          <template #icon>
            <van-badge :dot="!item.read" :offset="[0, 5]">
              <van-icon name="volume-o" size="20" color="#ee0a24" />
            </van-badge>
          </template>
        </van-cell>
      </van-cell-group>

      <!-- 订单消息 -->
      <van-cell-group title="订单消息" inset>
        <van-cell
          v-for="item in orderMessages"
          :key="item.id"
          :title="item.title"
          :label="item.content"
          :value="formatTime(item.time)"
          is-link
          @click="openMessage(item)"
        >
          <template #icon>
            <van-badge :dot="!item.read" :offset="[0, 5]">
              <van-icon name="shopping-cart-o" size="20" color="#1989fa" />
            </van-badge>
          </template>
        </van-cell>
      </van-cell-group>

      <!-- 促销活动 -->
      <van-cell-group title="促销活动" inset>
        <van-cell
          v-for="item in promotionMessages"
          :key="item.id"
          :title="item.title"
          :label="item.content"
          :value="formatTime(item.time)"
          is-link
          @click="openMessage(item)"
        >
          <template #icon>
            <van-badge :dot="!item.read" :offset="[0, 5]">
              <van-icon name="gift-o" size="20" color="#ff976a" />
            </van-badge>
          </template>
        </van-cell>
      </van-cell-group>

      <!-- 空状态 -->
      <van-empty 
        v-if="allMessages.length === 0"
        image="https://fastly.jsdelivr.net/npm/@vant/assets/custom-empty-image.png"
        description="暂无消息"
      />
    </div>

    <!-- 消息详情弹窗 -->
    <van-popup v-model:show="showMessageDetail" position="right" class="message-detail-popup">
      <div class="message-detail">
        <van-nav-bar
          :title="currentMessage?.title"
          left-arrow
          @click-left="showMessageDetail = false"
        />
        <div class="message-detail-content">
          <div class="message-header">
            <div class="message-time">{{ formatFullTime(currentMessage?.time) }}</div>
          </div>
          <div class="message-body">
            <p>{{ currentMessage?.content }}</p>
            <div v-if="currentMessage?.detail" class="message-extra">
              {{ currentMessage.detail }}
            </div>
          </div>
        </div>
      </div>
    </van-popup>

    <!-- 操作菜单 -->
    <van-action-sheet 
      v-model:show="showActions" 
      :actions="actionOptions" 
      @select="handleAction"
      cancel-text="取消"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
import { useRouter } from 'vue-router'

const router = useRouter()

// 响应式数据
const showMessageDetail = ref(false)
const showActions = ref(false)
const currentMessage = ref(null)

// 系统消息
const systemMessages = ref([
  {
    id: 1,
    title: '系统升级通知',
    content: '系统将于今晚22:00-24:00进行升级维护',
    time: new Date('2024-11-28 10:00:00'),
    read: false,
    detail: '亲爱的用户，为了给您提供更好的服务体验，我们将在今晚进行系统升级维护。维护期间可能会影响部分功能的使用，给您带来的不便敬请谅解。'
  },
  {
    id: 2,
    title: '新功能上线',
    content: '购物车分享功能已上线，快来体验吧！',
    time: new Date('2024-11-27 15:30:00'),
    read: true,
    detail: '全新的购物车分享功能让您可以轻松与朋友分享心仪的商品清单。点击购物车页面的分享按钮即可使用。'
  }
])

// 订单消息
const orderMessages = ref([
  {
    id: 3,
    title: '订单已发货',
    content: '您的订单 #202411280001 已发货',
    time: new Date('2024-11-28 14:20:00'),
    read: false,
    detail: '您的订单已通过顺丰快递发货，运单号：SF1234567890123。预计2-3个工作日内送达，请保持电话畅通。'
  },
  {
    id: 4,
    title: '订单待付款',
    content: '您有一笔订单待付款，请及时处理',
    time: new Date('2024-11-28 09:15:00'),
    read: true,
    detail: '订单 #202411280002 还有23小时58分钟自动取消，请及时完成支付。'
  },
  {
    id: 5,
    title: '订单已完成',
    content: '订单 #202411270001 已完成，欢迎评价',
    time: new Date('2024-11-27 16:45:00'),
    read: true,
    detail: '感谢您的购买！您的订单已完成，快来给商品评价吧，您的反馈对我们很重要。'
  }
])

// 促销活动
const promotionMessages = ref([
  {
    id: 6,
    title: '双12大促开始啦！',
    content: '全场8折起，优惠券免费领取',
    time: new Date('2024-11-28 08:00:00'),
    read: false,
    detail: '双12购物狂欢节正式开启！全场商品8折起，新用户专享299-50优惠券，老用户也有999-100大额券等你来抢！活动时间：12月1日-12月12日。'
  },
  {
    id: 7,
    title: '会员专享福利',
    content: '成为会员即享9.5折优惠',
    time: new Date('2024-11-26 12:00:00'),
    read: true,
    detail: '升级为VIP会员，即可享受全场9.5折优惠，还有生日礼品、积分翻倍等多重福利等你解锁！'
  }
])

// 所有消息
const allMessages = computed(() => [
  ...systemMessages.value,
  ...orderMessages.value,
  ...promotionMessages.value
])

// 操作选项
const actionOptions = [
  { name: '全部标记为已读', value: 'markAllRead' },
  { name: '清空消息', value: 'clearAll' }
]

// 格式化时间
const formatTime = (time) => {
  const now = new Date()
  const messageTime = new Date(time)
  const diff = now - messageTime

  if (diff < 60000) {
    return '刚刚'
  } else if (diff < 3600000) {
    return `${Math.floor(diff / 60000)}分钟前`
  } else if (diff < 86400000) {
    return `${Math.floor(diff / 3600000)}小时前`
  } else if (diff < 172800000) {
    return '昨天'
  } else {
    return `${messageTime.getMonth() + 1}-${messageTime.getDate()}`
  }
}

// 格式化完整时间
const formatFullTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

// 打开消息详情
const openMessage = (message) => {
  currentMessage.value = message
  message.read = true
  showMessageDetail.value = true
}

// 显示更多操作
const showMoreActions = () => {
  showActions.value = true
}

// 处理操作
const handleAction = async (action) => {
  switch (action.value) {
    case 'markAllRead':
      allMessages.value.forEach(msg => msg.read = true)
      showToast('已全部标记为已读')
      break
    case 'clearAll':
      try {
        await showConfirmDialog({
          title: '确认清空',
          message: '确定要清空所有消息吗？此操作不可恢复'
        })
        systemMessages.value = []
        orderMessages.value = []
        promotionMessages.value = []
        showToast('消息已清空')
      } catch {
        // 用户取消
      }
      break
  }
}
</script>

<style scoped>
.message-page {
  background-color: #f7f8fa;
  min-height: 100vh;
  padding-bottom: 60px; /* 为底部导航栏留出空间 */
}

.message-content {
  padding: 12px 0 20px 0;
}

.van-cell-group {
  margin-bottom: 12px;
}

.message-detail-popup {
  width: 100%;
  height: 100%;
}

.message-detail {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.message-detail-content {
  flex: 1;
  padding: 16px;
}

.message-header {
  margin-bottom: 16px;
}

.message-time {
  color: #969799;
  font-size: 12px;
}

.message-body p {
  font-size: 16px;
  line-height: 24px;
  margin: 0 0 12px 0;
  color: #323233;
}

.message-extra {
  background-color: #f7f8fa;
  padding: 12px;
  border-radius: 8px;
  font-size: 14px;
  line-height: 20px;
  color: #646566;
}

/* 修复图标对齐问题 */
:deep(.van-cell) {
  align-items: center;
}

:deep(.van-cell__left-icon) {
  margin-right: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
}

:deep(.van-badge) {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

:deep(.van-icon) {
  display: flex;
  align-items: center;
  justify-content: center;
}

:deep(.van-cell__title) {
  font-weight: 500;
  color: #323233;
}

:deep(.van-cell__label) {
  color: #969799;
  margin-top: 4px;
  line-height: 1.4;
}

:deep(.van-cell__value) {
  color: #969799;
  font-size: 12px;
}

/* 图标样式优化 */
.message-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: rgba(25, 137, 250, 0.1);
}

.message-icon .van-icon {
  font-size: 18px;
}
</style> 