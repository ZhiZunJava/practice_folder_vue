<template>
  <PageContainer title="仪表盘" description="欢迎使用商城管理系统，这里是数据概览">
    <!-- 统计卡片 -->
    <el-row :gutter="24" class="stats-section">
      <el-col :xl="6" :lg="6" :md="6" :sm="6" :xs="24" v-for="item in statsCards" :key="item.title">
        <div class="stats-card">
          <div class="stats-content">
            <div class="stats-info">
              <div class="stats-number">{{ item.value.toLocaleString() }}</div>
              <div class="stats-title">{{ item.title }}</div>
              <div class="stats-trend" :class="item.trend > 0 ? 'trend-up' : 'trend-down'">
                <el-icon :size="12">
                  <ArrowUp v-if="item.trend > 0" />
                  <ArrowDown v-else />
                </el-icon>
                {{ Math.abs(item.trend) }}%
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="24" class="charts-section">
      <!-- 销售趋势图 -->
      <el-col :xl="16" :lg="16" :md="24" :sm="24" :xs="24">
        <div class="admin-card">
          <div class="admin-card-header">
            <div class="card-header-content">
              <h3>销售趋势</h3>
              <p>近30天销售数据统计</p>
            </div>
            <div class="card-header-extra">
              <el-radio-group v-model="salesPeriod" size="small">
                <el-radio-button value="7">近7天</el-radio-button>
                <el-radio-button value="30">近30天</el-radio-button>
                <el-radio-button value="90">近90天</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div class="admin-card-body">
            <div class="chart-container">
              <v-chart 
                class="chart" 
                :option="salesOption" 
                autoresize
              />
            </div>
          </div>
        </div>
      </el-col>
      
      <!-- 分类占比 -->
      <el-col :xl="8" :lg="8" :md="24" :sm="24" :xs="24">
        <div class="admin-card">
          <div class="admin-card-header">
            <div class="card-header-content">
              <h3>分类占比</h3>
              <p>商品分类销售占比</p>
            </div>
          </div>
          <div class="admin-card-body">
            <div class="chart-container">
              <v-chart 
                class="chart" 
                :option="categoryOption" 
                autoresize
              />
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 快捷操作 -->
    <el-row :gutter="24" class="quick-actions-section">
      <el-col :span="24">
        <div class="admin-card">
          <div class="admin-card-header">
            <div class="card-header-content">
              <h3>快捷操作</h3>
              <p>常用功能快速入口</p>
            </div>
          </div>
          <div class="admin-card-body">
            <div class="quick-actions">
              <div class="action-item" @click="$router.push('/products')">
                <div class="action-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%)">
                  <el-icon><Goods /></el-icon>
                </div>
                <div class="action-text">
                  <div class="action-title">添加商品</div>
                  <div class="action-desc">添加新的商品信息</div>
                </div>
              </div>
              
              <div class="action-item" @click="$router.push('/categories')">
                <div class="action-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%)">
                  <el-icon><Menu /></el-icon>
                </div>
                <div class="action-text">
                  <div class="action-title">分类管理</div>
                  <div class="action-desc">管理商品分类</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </PageContainer>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, PieChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
// 注册ECharts组件
use([
  CanvasRenderer,
  LineChart,
  PieChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

// 销售周期选择
const salesPeriod = ref('30')

// 统计卡片数据
const statsCards = ref([
  {
    title: '用户总数',
    value: 15420,
    icon: 'User',
    color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    trend: 12.5
  },
  {
    title: '商品总数',
    value: 3248,
    icon: 'Goods',
    color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    trend: 8.3
  },
  {
    title: '订单总数',
    value: 28670,
    icon: 'List',
    color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    trend: -2.1
  },
  {
    title: '销售额',
    value: 1586420,
    icon: 'Money',
    color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
    trend: 18.7
  }
])

// 销售趋势图配置
const salesOption = ref({
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'cross'
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: ['1日', '2日', '3日', '4日', '5日', '6日', '7日', '8日', '9日', '10日', '11日', '12日', '13日', '14日', '15日']
  },
  yAxis: {
    type: 'value',
    axisLabel: {
      formatter: '{value}'
    }
  },
  series: [
    {
      name: '销售额',
      type: 'line',
      smooth: true,
      symbol: 'circle',
      symbolSize: 6,
      lineStyle: {
        color: '#1890ff',
        width: 3
      },
      itemStyle: {
        color: '#1890ff'
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [{
            offset: 0, color: 'rgba(24, 144, 255, 0.3)'
          }, {
            offset: 1, color: 'rgba(24, 144, 255, 0.05)'
          }]
        }
      },
      data: [8200, 9320, 9010, 9340, 12900, 13300, 13200, 11800, 10500, 12100, 13400, 14200, 15100, 13800, 14500]
    }
  ]
})

// 分类占比图配置
const categoryOption = ref({
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}: {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 10,
    top: 20,
    bottom: 20,
    itemGap: 10
  },
  series: [
    {
      name: '分类销售',
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['65%', '50%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 4,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '16',
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: [
        { value: 1048, name: '电子产品', itemStyle: { color: '#1890ff' } },
        { value: 735, name: '服装鞋帽', itemStyle: { color: '#52c41a' } },
        { value: 580, name: '家居用品', itemStyle: { color: '#faad14' } },
        { value: 484, name: '食品饮料', itemStyle: { color: '#f5222d' } },
        { value: 300, name: '其他', itemStyle: { color: '#722ed1' } }
      ]
    }
  ]
})

// 最新订单数据
const recentOrders = ref([
  {
    orderNo: 'ORD202401001',
    username: '张三',
    productName: 'iPhone 15 Pro Max 1TB 深空黑色',
    amount: 12999,
    status: 1,
    createTime: '2024-01-15 14:30:25'
  },
  {
    orderNo: 'ORD202401002',
    username: '李四',
    productName: 'MacBook Pro 16英寸 M3 Max芯片',
    amount: 28999,
    status: 2,
    createTime: '2024-01-15 13:45:10'
  },
  {
    orderNo: 'ORD202401003',
    username: '王五',
    productName: 'AirPods Pro (第3代)',
    amount: 1899,
    status: 0,
    createTime: '2024-01-15 12:20:33'
  },
  {
    orderNo: 'ORD202401004',
    username: '赵六',
    productName: 'Apple Watch Series 9 GPS + 蜂窝网络',
    amount: 3999,
    status: 3,
    createTime: '2024-01-15 11:15:42'
  },
  {
    orderNo: 'ORD202401005',
    username: '钱七',
    productName: 'iPad Air 5 Wi-Fi 256GB 深空灰色',
    amount: 5499,
    status: 1,
    createTime: '2024-01-15 10:30:18'
  }
])

// 获取状态样式类
const getStatusClass = (status) => {
  const classes = {
    0: 'status-tag status-tag--info',
    1: 'status-tag status-tag--warning',
    2: 'status-tag status-tag--success',
    3: 'status-tag status-tag--error'
  }
  return classes[status] || 'status-tag status-tag--info'
}

// 获取状态文本
const getStatusText = (status) => {
  const texts = {
    0: '待付款',
    1: '待发货',
    2: '已发货',
    3: '已取消'
  }
  return texts[status] || '未知'
}

// 处理查看订单
const handleViewOrders = () => {
  ElMessage.info('订单管理功能开发中')
}

// 处理查看全部订单
const handleViewAllOrders = () => {
  ElMessage.info('跳转到订单列表页面')
}
</script>

<style scoped>
.stats-section {
  margin-bottom: 24px;
}

.charts-section {
  margin-bottom: 24px;
}

.quick-actions-section {
  margin-bottom: 24px;
}

.orders-section {
  margin-bottom: 0;
}

.card-header-content {
  flex: 1;
}

.card-header-content h3 {
  margin: 0 0 4px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.card-header-content p {
  margin: 0;
  font-size: 14px;
  color: var(--text-tertiary);
}

.card-header-extra {
  flex-shrink: 0;
}

.admin-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.chart-container {
  height: 300px;
}

.chart {
  width: 100%;
  height: 100%;
}

.stats-trend {
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 2px;
  margin-top: 4px;
}

.trend-up {
  color: var(--success-color);
}

.trend-down {
  color: var(--error-color);
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  border-radius: 8px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-lighter);
  cursor: pointer;
  transition: all 0.3s ease;
}

.action-item:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-medium);
  border-color: var(--primary-color);
}

.action-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
}

.action-text {
  flex: 1;
}

.action-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 2px;
}

.action-desc {
  font-size: 12px;
  color: var(--text-tertiary);
}

.amount-text {
  font-weight: 600;
  color: var(--success-color);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .chart-container {
    height: 250px;
  }
  
  .quick-actions {
    grid-template-columns: 1fr;
  }
  
  .admin-card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}
</style> 