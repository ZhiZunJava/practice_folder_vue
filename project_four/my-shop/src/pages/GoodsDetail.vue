<template>
  <div class="goods-detail-page" v-if="!isNotFound">
    <van-swipe class="goods-swipe" :autoplay="3000">
      <van-swipe-item v-for="item in album" :key="item.id">
        <img :src="item.picture" >
      </van-swipe-item>
    </van-swipe>

    <van-cell-group class="info-section primary-info">
      <van-cell>
        <template #title>
          <van-tag type="danger" mark class="goods-new-tag" v-if="goods.is_new">新品</van-tag>
          <div class="goods-price">
            <span class="currency-symbol">￥</span>{{ goods.price }}
            <span class="goods-spec" v-if="goods.spec"> / {{ goods.spec }}</span>
          </div>
          <div class="goods-title">
            {{ goods.name }}
          </div>
        </template>
      </van-cell>
      <van-cell class="goods-express-info">
        <van-row gutter="20">
          <van-col span="12">运费：{{ goods.freight || '包邮' }}</van-col>
          <van-col span="12">剩余：{{ goods.stock }}</van-col>
        </van-row>
      </van-cell>
    </van-cell-group>

    <van-cell-group class="info-section service-info">
      <van-cell icon="location-o" title="发货" value="陕西宝鸡" is-link />
      <van-cell icon="shield-o" title="保障">
        <template #value>
          <span class="service-tags">坏单包赔</span>
          <span class="service-tags">假一赔四</span>
          <span class="service-tags">极速退款</span>
        </template>
      </van-cell>
      <van-cell icon="setting-o" title="参数" value="品牌：枝纯..." is-link />
    </van-cell-group>

    <div class="detail-title-bar">
      <span class="line"></span>
      <span class="text">宝贝详情</span>
      <span class="line"></span>
    </div>
    <div class="goods-description" v-html="goods.description"></div>

    <van-action-bar class="action-bar-placeholder">
      <van-action-bar-icon icon="chat-o" @click="sorry" text="客服" />
      <van-action-bar-icon icon="cart-o" :badge="cartCount() || ''" @click="onClickCart" text="购物车" />
      <van-action-bar-button type="warning" @click="addCart" text="加入购物车" />
      <van-action-bar-button type="danger" @click="sorry" text="立即购买" />
    </van-action-bar>
  </div>
  <div class="goods-not-found" v-else>
    <van-empty description="商品不存在或已下架" />
    <van-button type="primary" round block @click="$router.push('/')" style="width: 60%; margin: 20px auto;">去逛逛</van-button>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { getGoodsAlbum, getGoodsDetail } from '../api'
import useCart from '../stores/cart'

const { cartCount, addToCart } = useCart()

const props = defineProps({
  id: String
})

const goods = reactive({
  freight: 0,
  is_new: true
})
const album = ref([])
const isNotFound = ref(false)

onMounted(() => {
  loadGoodsDetail()
})

const loadGoodsDetail = async () => {
  const data1 = await getGoodsDetail({ id: props.id })
  if (!data1.id) {
    isNotFound.value = true
    return
  }
  const data2 = await getGoodsAlbum({ goods_id: props.id })
  if (data2.length === 0 && data1.picture) {
    data2.push({ id: 'default_album_id', picture: data1.picture })
  }
  Object.assign(goods, data1)
  album.value = data2
  if (goods.create_time) {
    const oneMonthAgo = new Date();
    oneMonthAgo.setMonth(oneMonthAgo.getMonth() - 1);
    goods.is_new = new Date(goods.create_time) > oneMonthAgo;
  } else {
    goods.is_new = Math.random() > 0.5;
  }
}

import { showToast } from 'vant'
import { useRouter } from 'vue-router'

const router = useRouter()

const sorry = () => {
  showToast('暂无后续逻辑~')
}

const onClickCart = () => {
  router.push({ name: 'cart' })
}

const addCart = () => {
  addToCart({ id: props.id, num: 1, checked: true })
  showToast({
    message: '添加成功'
  })
}
</script>

<style lang="less" scoped>
.goods-detail-page {
  background-color: #f7f8fa;
  padding-bottom: 60px;
  min-height: 100vh;
  box-sizing: border-box;
}

.goods-swipe {
  img {
    width: 100%;
    height: 375px;
    object-fit: cover;
    display: block;
  }
}

.info-section {
  margin: 10px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
   :deep(.van-cell) {
    padding-top: 12px;
    padding-bottom: 12px;
  }
}

.primary-info {
  .goods-new-tag {
    margin-bottom: 8px;
    font-size: 10px;
  }
  .goods-price {
    color: #e54d42;
    font-size: 24px;
    font-weight: bold;
    margin-bottom: 5px;
    line-height: 1.2;
    .currency-symbol {
      font-size: 14px;
      margin-right: 2px;
    }
    .goods-spec {
      font-size: 12px;
      color: #999;
      font-weight: normal;
      margin-left: 5px;
    }
  }
  .goods-title {
    font-size: 16px;
    color: #333;
    line-height: 1.5;
  }
}

.goods-express-info {
  color: #666;
  font-size: 13px;
  background-color: #fff;
   :deep(.van-cell__title){
    width: 100%;
  }
  :deep(.van-row){
    width: 100%;
  }
}

.service-info {
  .van-cell__value {
    .service-tags {
      display: inline-block;
      background-color: #fff5f5;
      color: #e54d42;
      font-size: 10px;
      padding: 2px 5px;
      border-radius: 3px;
      margin-right: 5px;
      &:last-child {
        margin-right: 0;
      }
    }
  }
   :deep(.van-cell__title) {
      flex: 0 0 80px;
   }
   :deep(.van-cell__value) {
     text-align: left;
   }
}

.detail-title-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 15px;
  color: #888;
  font-size: 14px;
  .line {
    flex-grow: 1;
    height: 1px;
    background-color: #ebedf0;
    margin: 0 10px;
  }
  .text {
    white-space: nowrap;
  }
}

.goods-description {
  padding: 0 10px 20px;
  font-size: 14px;
  line-height: 1.8;
  color: #333;
  background-color: #fff;
  border-radius: 8px;
  margin: 0 10px 10px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
  :deep(img) {
    max-width: 100%;
    height: auto;
    display: block;
    margin-top: 10px;
    margin-bottom: 10px;
    border-radius: 4px;
  }
  :deep(p) {
    margin-bottom: 10px;
  }
}

.action-bar-placeholder {
}

.goods-not-found {
  padding-top: 40px;
  text-align: center;
  background-color: #f7f8fa;
  min-height: calc(100vh - 46px);
  box-sizing: border-box;
  .van-empty {
    padding-bottom: 0;
  }
}
</style>
