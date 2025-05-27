<template>
  <div class="user-list-container">
    <!-- 用户列表表格 -->
    <el-table 
      :data="currentPageData" 
      stripe 
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column 
        prop="id" 
        label="ID" 
        width="80"
        align="center"
      />
      <el-table-column 
        prop="username" 
        label="用户名" 
        width="200"
        align="center"
      />
      <el-table-column 
        prop="email" 
        label="邮箱" 
        width="250"
        align="center"
      />
      <el-table-column 
        prop="registerDate" 
        label="注册时间" 
        width="180"
        align="center"
      />
      <el-table-column 
        prop="level" 
        label="等级" 
        width="120"
        align="center"
      >
        <template #default="scope">
          <el-tag 
            :type="getLevelTagType(scope.row.level)"
            size="small"
          >
            {{ getLevelText(scope.row.level) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column 
        prop="status" 
        label="状态" 
        width="100"
        align="center"
      >
        <template #default="scope">
          <el-tag 
            :type="scope.row.status === 'active' ? 'success' : 'danger'"
            size="small"
          >
            {{ scope.row.status === 'active' ? '活跃' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :small="false"
        :disabled="false"
        :background="true"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

// 响应式数据
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(114)

// 模拟用户数据
const userData = ref([])

// 生成模拟数据
const generateUserData = () => {
  const data = []
  const levels = [1, 2, 3, 4, 5]
  const statuses = ['active', 'inactive']
  
  for (let i = 1; i <= 114; i++) {
    data.push({
      id: i,
      username: `用户${i.toString().padStart(3, '0')}`,
      email: `user${i}@example.com`,
      registerDate: new Date(2020 + Math.floor(Math.random() * 4), 
                            Math.floor(Math.random() * 12), 
                            Math.floor(Math.random() * 28) + 1)
                    .toLocaleDateString('zh-CN'),
      level: levels[Math.floor(Math.random() * levels.length)],
      status: statuses[Math.floor(Math.random() * statuses.length)]
    })
  }
  return data
}

// 计算当前页数据
const currentPageData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return userData.value.slice(start, end)
})

// 获取等级标签类型
const getLevelTagType = (level) => {
  const typeMap = {
    1: '',
    2: 'success',
    3: 'info',
    4: 'warning',
    5: 'danger'
  }
  return typeMap[level] || ''
}

// 获取等级文本
const getLevelText = (level) => {
  const textMap = {
    1: '新手',
    2: '初级',
    3: '中级',
    4: '高级',
    5: 'VIP'
  }
  return textMap[level] || '未知'
}

// 分页大小改变处理
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
}

// 当前页改变处理
const handleCurrentChange = (val) => {
  currentPage.value = val
}

// 组件挂载时初始化数据
onMounted(() => {
  loading.value = true
  // 模拟数据加载延迟
  setTimeout(() => {
    userData.value = generateUserData()
    loading.value = false
  }, 500)
})
</script>

<style scoped>
.user-list-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table__header) {
  background-color: #f5f7fa;
}

:deep(.el-table th) {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 600;
}

:deep(.el-table tr) {
  background-color: #fff;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  background-color: #fafafa;
}

:deep(.el-table td) {
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-pagination) {
  justify-content: center;
}
</style> 