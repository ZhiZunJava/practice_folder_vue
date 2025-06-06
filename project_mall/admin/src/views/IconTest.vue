<template>
  <PageContainer title="图标测试" description="测试ElementPlus图标是否正常显示">
    <div class="icon-test">
      <el-row :gutter="24">
        <el-col :span="8" v-for="icon in iconList" :key="icon.name">
          <div class="icon-item">
            <el-icon :size="24">
              <component :is="icon.component" />
            </el-icon>
            <span class="icon-name">{{ icon.name }}</span>
          </div>
        </el-col>
      </el-row>
      
      <el-divider />
      
      <h3>常用图标测试</h3>
      <div class="common-icons">
        <el-button type="primary">
          <el-icon><Plus /></el-icon>
          添加
        </el-button>
        
        <el-button type="success">
          <el-icon><Check /></el-icon>
          确认
        </el-button>
        
        <el-button type="warning">
          <el-icon><Edit /></el-icon>
          编辑
        </el-button>
        
        <el-button type="danger">
          <el-icon><Delete /></el-icon>
          删除
        </el-button>
        
        <el-button type="info">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
      </div>
      
      <el-divider />
      
      <h3>菜单图标测试</h3>
      <div class="menu-icons">
        <div class="menu-icon-item">
          <el-icon size="20"><Shop /></el-icon>
          <span>Shop</span>
        </div>
        <div class="menu-icon-item">
          <el-icon size="20"><DataBoard /></el-icon>
          <span>DataBoard</span>
        </div>
        <div class="menu-icon-item">
          <el-icon size="20"><Files /></el-icon>
          <span>Files</span>
        </div>
        <div class="menu-icon-item">
          <el-icon size="20"><Collection /></el-icon>
          <span>Collection</span>
        </div>
        <div class="menu-icon-item">
          <el-icon size="20"><Goods /></el-icon>
          <span>Goods</span>
        </div>
        <div class="menu-icon-item">
          <el-icon size="20"><User /></el-icon>
          <span>User</span>
        </div>
        <div class="menu-icon-item">
          <el-icon size="20"><Document /></el-icon>
          <span>Document</span>
        </div>
        <div class="menu-icon-item">
          <el-icon size="20"><UserFilled /></el-icon>
          <span>UserFilled</span>
        </div>
      </div>
    </div>
  </PageContainer>
</template>

<script setup>
import { ref } from 'vue'

const iconList = ref([
  { name: 'Plus', component: 'Plus' },
  { name: 'Edit', component: 'Edit' },
  { name: 'Delete', component: 'Delete' },
  { name: 'Search', component: 'Search' },
  { name: 'Check', component: 'Check' },
  { name: 'Close', component: 'Close' },
  { name: 'ArrowUp', component: 'ArrowUp' },
  { name: 'ArrowDown', component: 'ArrowDown' },
  { name: 'Expand', component: 'Expand' },
  { name: 'Fold', component: 'Fold' },
  { name: 'Refresh', component: 'Refresh' },
  { name: 'Setting', component: 'Setting' }
])
</script>

<style scoped>
.icon-test {
  padding: 20px;
}

.icon-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  margin-bottom: 16px;
  border: 1px solid var(--border-lighter);
  border-radius: 8px;
  background: white;
  transition: all 0.3s ease;
}

.icon-item:hover {
  box-shadow: var(--shadow-light);
  border-color: var(--primary-color);
}

.icon-name {
  font-size: 14px;
  color: var(--text-secondary);
}

.common-icons {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
  margin: 20px 0;
}

.menu-icons {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 16px;
  margin: 20px 0;
}

.menu-icon-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px;
  border: 1px solid var(--border-lighter);
  border-radius: 6px;
  background: white;
}

h3 {
  margin: 20px 0 16px 0;
  color: var(--text-primary);
  font-size: 16px;
  font-weight: 600;
}
</style> 