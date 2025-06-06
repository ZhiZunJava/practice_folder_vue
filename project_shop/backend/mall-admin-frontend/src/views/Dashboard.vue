<template>
  <div>
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="stat-card" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white;">
          <div class="stat-number">{{ statistics.productCount || 500 }}</div>
          <div class="stat-label">商品数量(个)</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); color: white;">
          <div class="stat-number">{{ statistics.categoryCount || 20 }}</div>
          <div class="stat-label">商品分类(个)</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); color: white;">
          <div class="stat-number">{{ statistics.userCount || 121 }}</div>
          <div class="stat-label">用户总数(个)</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); color: white;">
          <div class="stat-number">¥{{ statistics.todaySales || '1280.50' }}</div>
          <div class="stat-label">今日销售额</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="dashboard-card">
          <template #header>
            <div style="display: flex; align-items: center;">
              <el-icon style="margin-right: 8px;"><TrendCharts /></el-icon>
              <span>2022年度成交额</span>
            </div>
          </template>
          <div ref="salesChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="dashboard-card">
          <template #header>
            <div style="display: flex; align-items: center;">
              <el-icon style="margin-right: 8px;"><Histogram /></el-icon>
              <span>2022年订单数量</span>
            </div>
          </template>
          <div ref="orderChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getStatistics, getChartData } from '@/api/mall'
import { TrendCharts, Histogram } from '@element-plus/icons-vue'

const statistics = ref({})
const chartData = ref({})
const salesChartRef = ref()
const orderChartRef = ref()

let salesChart = null
let orderChart = null

// 获取统计数据
const fetchStatistics = async () => {
  try {
    const res = await getStatistics()
    statistics.value = res.data
  } catch (error) {
    console.error('获取统计数据失败:', error)
    // 使用模拟数据
    statistics.value = {
      productCount: 500,
      categoryCount: 20,
      userCount: 121,
      todaySales: '1280.50'
    }
  }
}

// 获取图表数据
const fetchChartData = async () => {
  try {
    const res = await getChartData()
    chartData.value = res.data
  } catch (error) {
    console.error('获取图表数据失败:', error)
    // 使用模拟数据
    chartData.value = {
      months: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
      salesData: [8.2, 9.5, 10.8, 11.2, 12.5, 13.8, 14.2, 15.5, 13.8, 12.5, 13.2, 12.8],
      orderData: [450, 520, 350, 280, 380, 420, 350, 380, 450, 200, 350, 480]
    }
  }
}

// 初始化销售额图表
const initSalesChart = () => {
  salesChart = echarts.init(salesChartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis'
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
      data: chartData.value.months || ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '{value}万'
      }
    },
    series: [
      {
        name: '成交额',
        type: 'line',
        stack: 'Total',
        smooth: true,
        lineStyle: {
          color: '#667eea'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(102, 126, 234, 0.6)' },
            { offset: 1, color: 'rgba(102, 126, 234, 0.1)' }
          ])
        },
        data: chartData.value.salesData || [8.2, 9.5, 10.8, 11.2, 12.5, 13.8, 14.2, 15.5, 13.8, 12.5, 13.2, 12.8]
      }
    ]
  }
  salesChart.setOption(option)
}

// 初始化订单数量图表
const initOrderChart = () => {
  orderChart = echarts.init(orderChartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
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
      data: chartData.value.months || ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '订单数量',
        type: 'bar',
        data: chartData.value.orderData || [450, 520, 350, 280, 380, 420, 350, 380, 450, 200, 350, 480],
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#4facfe' },
            { offset: 1, color: '#00f2fe' }
          ])
        }
      }
    ]
  }
  orderChart.setOption(option)
}

// 窗口大小改变时重新调整图表
const handleResize = () => {
  if (salesChart) {
    salesChart.resize()
  }
  if (orderChart) {
    orderChart.resize()
  }
}

onMounted(async () => {
  await fetchStatistics()
  await fetchChartData()
  
  await nextTick()
  initSalesChart()
  initOrderChart()
  
  window.addEventListener('resize', handleResize)
})

// 组件卸载时清理
onBeforeUnmount(() => {
  if (salesChart) {
    salesChart.dispose()
  }
  if (orderChart) {
    orderChart.dispose()
  }
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.stat-card {
  text-align: center;
  
  .stat-number {
    font-size: 32px;
    font-weight: bold;
    margin-bottom: 10px;
  }
  
  .stat-label {
    font-size: 14px;
    opacity: 0.8;
  }
}

.dashboard-card {
  margin-bottom: 20px;
}
</style> 