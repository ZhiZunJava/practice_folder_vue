<template>
  <div class="home-container">
    <van-search
      shape="round"
      v-model="value"
      placeholder="请输入搜索关键词"
      @search="onSearch"
      @cancel="onCancel"
    >
    </van-search>
    <!-- 轮播图 -->
    <home-swiper class="home-section"></home-swiper>
    <!-- 功能按钮区 -->
    <home-grid class="home-section"></home-grid>
    <!-- 商品信息展示区 -->
    <home-product class="home-section"></home-product>
    <home-new class="home-section"></home-new>
    <home-top class="home-section"></home-top>
  </div>
</template>

<script setup>
import HomeSwiper from '../components/HomeSwiper.vue'
import HomeGrid from '../components/HomeGrid.vue'
import HomeProduct from '../components/HomeProduct.vue'
import HomeNew from '../components/HomeNew.vue'
import HomeTop from '../components/HomeTop.vue'

import { ref } from 'vue'
import { showToast } from 'vant'

const value = ref('')
const onSearch = val=> showToast(val)
const onCancel = () => showToast('取消')
</script>

<style scoped>
.home-container {
  padding: 10px;
  background-color: #f7f8fa;
}

.home-section {
  margin-top: 5px;
}
</style>
