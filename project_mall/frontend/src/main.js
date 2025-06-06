import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// 引入Vant组件库
import Vant from 'vant'
import 'vant/lib/index.css'

// 移动端适配
import '@vant/touch-emulator'

const app = createApp(App)

app.use(router)
app.use(Vant)

app.mount('#app') 