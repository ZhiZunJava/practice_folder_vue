<template>
  <div class="dashboard-page">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="box-card user-card">
          <template #header>
            <div class="card-header">
              <el-avatar class="avatar" :src="admin.avatar" shape="square" :size="48" />
              <span class="username">{{ admin.username }}</span>
            </div>
          </template>
          <div class="info">
            <p><el-icon><Memo /></el-icon> 登录时间：{{ loginInfo.loginTime }}</p>
            <p><el-icon><Memo /></el-icon> 登录地点：{{ loginInfo.loginPlace }}</p>
          </div>
        </el-card>
      </el-col>
      <!-- 单月统计信息展示 -->
      <el-col :span="18">
        <el-card class="box-card stat-card">
          <template #header>
            <div class="card-header stat-title">
              <el-icon style="margin-right: 8px;"><Memo /></el-icon>6月统计信息
            </div>
          </template>
          <div class="info">
            <el-row :gutter="24">
              <!-- 商品数量 -->
              <el-col :span="8">
                <div class="stat-item">
                  <div class="stat-icon" style="background-color: #EEAD0E;">
                    <el-icon :size="48" color="#fff"><Memo /></el-icon>
                  </div>
                  <div class="stat-content">
                    <p class="number">500</p>
                    <p class="desc">商品数量(个)</p>
                  </div>
                </div>
              </el-col>
              <!-- 商品分类数量 -->
              <el-col :span="8">
                <div class="stat-item">
                  <div class="stat-icon" style="background-color: #AB82FF;">
                    <el-icon :size="48" color="#fff"><Memo /></el-icon>
                  </div>
                  <div class="stat-content">
                    <p class="number">20</p>
                    <p class="desc">商品分类数量(个)</p>
                  </div>
                </div>
              </el-col>
              <!-- 用户访问次数 -->
              <el-col :span="8">
                <div class="stat-item">
                  <div class="stat-icon" style="background-color: #63B8FF;">
                    <el-icon :size="48" color="#fff"><Memo /></el-icon>
                  </div>
                  <div class="stat-content">
                    <p class="number">121</p>
                    <p class="desc">用户访问次数(次)</p>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 图表区域 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="box-card chart-card">
          <div id="salesVolume" class="chart-area"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card chart-card">
          <div id="orderQuantity" class="chart-area"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { getAdmin } from '../../api'
import useAdmin from '../../stores/admin'
import { Memo } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

const { admin, updateAdmin } = useAdmin()

onMounted(() => {
  loadAdmin()
  initCharts1()
  initCharts2()
})

const loadAdmin = async () => {
  let data = await getAdmin()
  updateAdmin({
    username: data.username,
    avatar: data.avatar
  })
}

// 用户登录信息（模拟数据）
const loginInfo = reactive({
  loginTime: '2023-07-22 09:00:00',
  loginPlace: '北京'
})

// 图表1：月度销售额
const initCharts1 = () => {
  const myChart = echarts.init(document.getElementById('salesVolume'))
  myChart.setOption({
    color: ['#1493fa'],
    title: { text: '2022年月度销售额' },
    xAxis: {
      data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
      name: '月份',
      axisLabel: {
        interval: 0
      },
    },
    yAxis: {
      name: '单位（千万元）',
    },
    grid: {
      left: '7%',
      right: '8%',
      bottom: '5%',
      containLabel: true,
    },
    legend: {},
    series: [
      {
        data: [6, 7, 8.5, 8, 9, 10, 13, 12, 10, 16, 15, 14],
        type: 'line',
        name: '销售额',
        smooth: true,
        label: {
          show: true,
          position: 'top',
        }
      }
    ]
  })
  // 图表自适应大小
  window.onresize = () => {
    myChart.resize()
  }
}

// 图表2：2022年订单数量
const initCharts2 = () => {
  const myChart = echarts.init(document.getElementById('orderQuantity'))
  myChart.setOption({
    title: { text: '2022年订单数量' },
    color: ['#1493fa'],
    grid: {
      left: '5%',
      right: '8%',
      bottom: '3%',
      containLabel: true,
    },
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
      name: '月份',
      // 类目轴中在 boundaryGap 为 true 的时候有效，可以保证刻度线和标签对齐
      axisTick: {
        alignWithLabel: true,
      },
      axisLabel: {
        interval: 0,rotate: 45 // 设置刻度标签旋转角度为45度
      },
    },
    legend: {},
    yAxis: {
      name: '单位（个）',
    },
    series: [
      {
        data: [400, 450, 300, 230, 250, 300, 400, 350, 160, 350, 380, 400],
        type: 'bar',
        barWidth: '60%',
        name: '订单总数',
        label: {
          show: true,
          position: 'top',
        }
      }
    ]
  })
  // 图表自适应大小
  window.onresize = () => {
    myChart.resize()
  }
}

</script>

<style lang="scss" scoped>
.dashboard-page {
  min-height: 100vh;
}

.el-row {
  margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
}

.box-card {
  border-radius: 14px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.04);
  transition: box-shadow 0.2s;
  &:hover {
    box-shadow: 0 4px 24px 0 rgba(0,0,0,0.08);
  }
}

.user-card {
  .card-header {
    display: flex;
    align-items: center;
    .avatar {
      margin-right: 16px;
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.08);
    }
    .username {
      font-size: 22px;
      font-weight: 600;
      color: #333;
    }
  }
  .info {
    font-size: 15px;
    color: #666;
    p {
      margin: 8px 0;
      display: flex;
      align-items: center;
      el-icon {
        margin-right: 6px;
        color: #b3b3b3;
      }
    }
  }
}

.stat-card {
  .stat-title {
    font-size: 18px;
    font-weight: 600;
    color: #444;
    display: flex;
    align-items: center;
  }
  .info {
    padding: 10px 0 0 0;
  }
}

.stat-item {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 1px 6px 0 rgba(0,0,0,0.04);
  padding: 18px 18px 18px 0;
  margin-bottom: 10px;
  transition: box-shadow 0.2s, transform 0.2s;
  &:hover {
    box-shadow: 0 4px 16px 0 rgba(0,0,0,0.10);
    transform: translateY(-2px) scale(1.02);
  }
  .stat-icon {
    width: 64px;
    height: 64px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 18px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.08);
    margin-left: 20px;
  }
  .stat-content {
    .number {
      font-size: 28px;
      font-weight: bold;
      color: #222;
      margin-bottom: 4px;
    }
    .desc {
      font-size: 14px;
      color: #888;
    }
  }
}

.chart-card {
  padding: 0 0 10px 0;
  .chart-area {
    width: 100%;
    height: 400px;
    margin-top: 10px;
  }
}
</style>