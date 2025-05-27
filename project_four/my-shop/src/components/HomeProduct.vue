<template>
  <div class="home-product">
    <ul class="product-list">
      <li v-for="item in brandList" :key="item.id" class="product-item">
        <img :src="item.pic_url" alt="" class="product-image">
        <h4 class="product-name">{{ item.name }}</h4>
      </li>
    </ul>
  </div>
</template>

<script setup>

const brandList = [
  { id: 1, name: '直播', pic_url: '/images/product1.png' },
  { id: 2, name: '推荐', pic_url: '/images/product2.png' },
  { id: 3, name: '补贴', pic_url: '/images/product3.png' },
  { id: 4, name: '分享', pic_url: '/images/product4.png' }
] 
</script>

<style lang="less" scoped>
.home-product {
  padding: 10px 0;
}
.product-list {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  list-style: none;
  padding: 0;
  margin: 0;
}

.product-item {
  width: 49%;
  margin-bottom: 10px;
  position: relative;
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.product-image {
  width: 100%;
  display: block;
}

.product-name {
  font-size: 13px;
  position: absolute;
  left: 8px;
  top: 8px;
  background-color: rgba(0, 0, 0, 0.5);
  color: #fff;
  border-radius: 4px;
  padding: 3px 6px;
  font-weight: normal;
}
</style>