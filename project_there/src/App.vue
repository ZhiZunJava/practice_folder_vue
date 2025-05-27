<script setup>
import ProductManager from './components/ProductManager.vue'
</script>

<template>
  <div id="app">
    <ProductManager />
  </div>
</template>

<style>
#app {
  min-height: 100vh;
  background-color: #f5f5f5;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
}
</style>
