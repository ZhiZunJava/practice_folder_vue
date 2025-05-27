<template>
  <div class="home-new">
    <div class="home-new-title">
      <h3>每周上新</h3>
    </div>
    <ul class="new-product-list">
      <li v-for="item in newList" :key="item.id" class="new-product-item">
        <img :src="item.list_pic_url" alt="" class="new-product-image" />
        <p class="new-product-name">{{ item.name }}</p>
        <p class="new-product-price"><span>￥</span>{{ item.retail_price }}</p>
      </li>
    </ul>
  </div>
</template>

<script setup>
import new1 from '/images/new1.jpg'
import new2 from '/images/new2.jpg'
import new3 from '/images/new3.jpg'
import new4 from '/images/new4.jpg'

const newList = [
  { name: '懒人小沙发', list_pic_url: new1, retail_price: '128.00' },
  { name: '减压弹力球', list_pic_url: new2, retail_price: '89.00' },
  { name: '简约一字夹发夹', list_pic_url: new3, retail_price: '12.8' },
  { name: '毛线小兔子耳朵发夹', list_pic_url: new4, retail_price: '9.9' }
]
</script>

<style lang="less" scoped>
.home-new {
  background-color: #fff;
  padding: 15px 10px;
  margin-top: 10px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

  .home-new-title {
    text-align: center;
    margin-bottom: 15px;
    h3 {
      font-size: 18px;
      font-weight: 600;
      color: #333;
      position: relative;
      display: inline-block;
      padding-bottom: 5px;
      &::after {
        content: '';
        position: absolute;
        left: 50%;
        transform: translateX(-50%);
        bottom: 0;
        width: 50px;
        height: 3px;
        background-color: #FF8000;
        border-radius: 2px;
      }
    }
  }
  .new-product-list {
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
    list-style: none;
    padding: 0;
    margin: 0;
  }
  .new-product-item {
    width: 48.5%;
    margin-bottom: 15px;
    background-color: #f9f9f9;
    border-radius: 6px;
    overflow: hidden;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
    padding-bottom: 10px;

    .new-product-image {
      width: 100%;
      height: 150px;
      object-fit: cover;
      display: block;
    }
    .new-product-name {
      font-size: 14px;
      color: #333;
      margin: 8px 10px;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      text-align: left;
    }
    .new-product-price {
      font-size: 16px;
      color: #FF8000;
      font-weight: bold;
      margin: 0 10px;
      text-align: left;
      span {
        font-size: 12px;
      }
    }
  }
}
</style>