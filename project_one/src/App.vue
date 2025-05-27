<script setup>
import UserPaginationList from './components/UserPaginationList.vue'
</script>

<template>
  <div class="app-container">
    <h2>用户分页列表</h2>
    <UserPaginationList />
  </div>
</template>

<style scoped>
.app-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

h2 {
  text-align: center;
  color: #303133;
  margin-bottom: 30px;
}
</style>
