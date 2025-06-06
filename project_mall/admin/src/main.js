import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// 引入ElementPlus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// 引入ECharts
import VChart from 'vue-echarts'

// 引入进度条
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

// 导入全局样式
import './styles/global.css'

// 导入全局组件
import PageContainer from './components/PageContainer.vue'

const app = createApp(App)

// 注册ElementPlus
app.use(ElementPlus)

// 注册ElementPlus所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 注册路由
app.use(router)

// 注册ECharts
app.component('VChart', VChart)

// 注册全局组件
app.component('PageContainer', PageContainer)

// 配置进度条
NProgress.configure({ showSpinner: false })

app.mount('#app') 