<template>
  <div class="message-page">
    <van-cell-group v-for="(item, index) in lists" :key="index" class="message-card">
      <van-cell center :icon="item.img" :title="item.title" :value="item.value" :label="item.label" class="message-cell" />
    </van-cell-group>
  </div>
</template>

<script setup>
const lists = [
  {
    img: '/images/avatar1.jpg',
    title: '食品旗舰店',
    value: '星期一',
    label: "您有一条店铺消息"
  },
  {
    img: '/images/avatar2.jpg',
    title: '水果旗舰店',
    value: '星期二',
    label: "亲爱的果粉："
  },
  {
    img: '/images/avatar3.png',
    title: '订阅号消息',
    value: '星期日',
    label: "水果旗舰店：【新到水果新品————粑粑柑、砂糖橘】"
  },
  {
    img: '/images/avatar4.png',
    title: '消息号内容',
    value: '星期一',
    label: "食品旗舰店：大量新品到货，速来选购"
  }
]
</script>

<style lang="less" scoped>
.message-page {
  padding: 10px;
  background-color: #f7f8fa; // 页面背景色
  min-height: calc(100vh - 50px); // 假设有底部导航栏，撑满屏幕
}

.message-card {
  margin-bottom: 10px;
  border-radius: 8px;
  overflow: hidden; // 确保内部元素遵循圆角
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);

  &:last-child {
    margin-bottom: 0;
  }
}

:deep(.message-cell) {
  .van-cell__left-icon {
    width: 45px; // 稍微增大头像尺寸
    height: 45px;
    margin-right: 12px; // 图标与标题的间距
    .van-icon__image {
      width: 100%;
      height: 100%;
      border-radius: 50%; // 圆形头像
      object-fit: cover; // 确保图片填满且不变形
    }
  }
  .van-cell__title {
    font-size: 15px;
    color: #333;
    .van-cell__label {
      white-space: nowrap;
      text-overflow: ellipsis;
      overflow: hidden;
      width: calc(100vw - 160px); // 根据实际情况调整宽度以正确显示省略号
      font-size: 13px;
      color: #777;
      margin-top: 4px;
    }
  }
  .van-cell__value {
    font-size: 12px;
    color: #999;
  }
}
</style>