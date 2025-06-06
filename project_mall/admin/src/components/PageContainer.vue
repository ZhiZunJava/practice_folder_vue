<template>
  <div class="page-container">
    <!-- 页面头部 -->
    <div class="page-header" v-if="title || $slots.header">
      <div class="page-title">
        <slot name="header">
          <h1>{{ title }}</h1>
          <p v-if="description" class="page-description">{{ description }}</p>
        </slot>
      </div>
      <div class="page-extra" v-if="$slots.extra">
        <slot name="extra"></slot>
      </div>
    </div>
    
    <!-- 页面内容 -->
    <div class="page-content">
      <slot></slot>
    </div>
  </div>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    default: ''
  },
  description: {
    type: String,
    default: ''
  }
})
</script>

<style scoped>
.page-container {
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.page-header {
  padding: 24px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fafafa;
}

.page-title h1 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #262626;
  line-height: 1.4;
}

.page-description {
  margin: 4px 0 0 0;
  font-size: 14px;
  color: #8c8c8c;
  line-height: 1.5;
}

.page-extra {
  flex-shrink: 0;
}

.page-content {
  padding: 24px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    padding: 16px;
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .page-content {
    padding: 16px;
  }
  
  .page-title h1 {
    font-size: 18px;
  }
}
</style> 