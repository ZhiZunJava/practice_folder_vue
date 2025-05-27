<template>
  <div class="home-top">
    <h3 class="top-title">人气推荐</h3>
    <div class="content">
      <van-card
        v-for="item in GoodsList"
        :key="item.id"
        :tag="item.tag"
        :price="item.retail_price"
        :origin-price="item.origin_price"
        :desc="item.goods_brief"
        :title="item.name"
        :thumb="item.list_pic_url"
        class="top-product-card"
      >
      </van-card>
    </div>
  </div>
</template>

<script setup>
const GoodsList = [
  {
    retail_price: '299.00',
    name: '蚕丝被 正品桑蚕丝',
    goods_brief: '一级桑蚕丝，轻盈、透气、柔软',
    list_pic_url: '/images/top1.jpg',
    tag: 'TOP1'
  },
  {
    retail_price: '88.00',
    origin_price: '98.00',
    name: '儿童摇摇马',
    goods_brief: '安全、不会侧翻、爸妈放心',
    list_pic_url: '/images/top2.jpg',
    tag: 'TOP2'
  },
  {
    retail_price: '128.00',
    origin_price: '168.00',
    name: '可躺可睡休闲懒人沙发',
    goods_brief: '轻松看书、社交、办公、舒适放松',
    list_pic_url: '/images/top3.jpg',
    tag: 'TOP3'
  },
  {
    retail_price: '199.00',
    origin_price: '205.00',
    name: '儿童积木 拼装玩具',
    goods_brief: '大颗粒 家长更放心 不易吞咽、安全性高',
    list_pic_url: '/images/top4.jpg',
    tag: 'TOP4'
  },
  {
    retail_price: '89.00',
    origin_price: '99.00',
    name: '扭扭车 1——3岁男女宝宝',
    goods_brief: '儿童扭扭车万向轮 防侧翻大人新款摇摆扭扭车',
    list_pic_url: '/images/top5.jpg',
    tag: 'TOP5'
  }
]
</script>

<style lang="less" scoped>
.home-top {
  background-color: #fff;
  padding: 15px 10px;
  margin-top: 10px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

  .top-title {
    font-size: 18px;
    font-weight: 600;
    color: #333;
    text-align: center;
    margin-bottom: 15px;
    position: relative;
    display: inline-block;
    left: 50%;
    transform: translateX(-50%);
    padding-bottom: 5px;

    &::after {
      content: '';
      position: absolute;
      left: 50%;
      transform: translateX(-50%);
      bottom: 0;
      width: 50px;
      height: 3px;
      background-color: #e60012;
      border-radius: 2px;
    }
  }

  .content {
    --van-tag-danger-color: #e60012;
    --van-card-price-color: #e60012;
    --van-card-price-font-size: 18px;
    --van-card-thumb-size: 90px;
    background: transparent;

    .top-product-card {
      background-color: #f9f9f9;
      border-radius: 6px;
      margin-top: 0;
      margin-bottom: 10px;
      padding: 5px;

      &:last-child {
        margin-bottom: 0;
      }

      :deep(.van-card__title) {
        font-size: 15px;
        font-weight: bold;
        line-height: 1.4;
        margin-top: 5px;
        padding: 0;
      }
      :deep(.van-card__desc) {
        font-size: 12px;
        color: #666;
        padding-top: 3px;
      }
      :deep(.van-card__price-currency) {
        
      }
      :deep(.van-tag--danger) {
        background-color: var(--van-tag-danger-color);
      }
      :deep(.van-card__tag) {
        
      }
    }
  }
}
</style>