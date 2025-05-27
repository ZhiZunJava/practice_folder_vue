# Vue3 + Element Plus 商品管理系统

这是一个基于Vue3和Element Plus构建的完整商品增删改查(CRUD)系统。

## 功能特性

### ✨ 核心功能
- **商品列表展示** - 使用表格形式展示商品信息
- **商品搜索** - 支持按商品名称和分类搜索
- **商品添加** - 通过弹窗表单添加新商品
- **商品编辑** - 修改现有商品信息
- **商品删除** - 支持单个删除和批量删除
- **分页功能** - 支持分页显示和每页条数调整

### 🎨 UI特性
- **响应式设计** - 适配移动端和桌面端
- **现代化界面** - 使用Element Plus组件库
- **加载状态** - 操作时显示加载动画
- **消息提示** - 操作成功/失败消息提示
- **确认对话框** - 删除操作需要用户确认

### 📊 数据字段
每个商品包含以下信息：
- ID - 商品唯一标识
- 商品名称 - 必填，2-50个字符
- 分类 - 必选，支持多个预设分类
- 价格 - 必填，支持小数点后两位
- 库存 - 必填，整数
- 状态 - 上架/下架
- 描述 - 商品详细描述

## 项目结构

```
src/
├── components/
│   └── ProductManager.vue     # 主要的商品管理组件
├── api/
│   └── productApi.js         # 商品API服务（模拟数据）
├── App.vue                   # 根组件
└── main.js                   # 应用入口
```

## 安装和运行

### 安装依赖
```bash
npm install
```

### 启动开发服务器
```bash
npm run dev
```

### 构建生产版本
```bash
npm run build
```

## 组件使用

### 在其他组件中使用ProductManager

```vue
<template>
  <div>
    <ProductManager />
  </div>
</template>

<script setup>
import ProductManager from './components/ProductManager.vue'
</script>
```

## API接口说明

项目中的`src/api/productApi.js`提供了完整的API接口模拟，包括：

### 商品相关接口
- `getProducts(params)` - 获取商品列表
- `getProduct(id)` - 获取单个商品详情
- `createProduct(productData)` - 创建新商品
- `updateProduct(id, productData)` - 更新商品信息
- `deleteProduct(id)` - 删除商品
- `batchDeleteProducts(ids)` - 批量删除商品
- `searchProducts(keyword, filters)` - 搜索商品
- `getCategories()` - 获取商品分类列表

### 接口返回格式
```javascript
{
  success: true,        // 是否成功
  data: {},            // 返回数据
  message: "操作成功", // 提示信息
  timestamp: "2024-01-01T00:00:00.000Z" // 时间戳
}
```

## 连接真实后端

要连接真实的后端API，只需要修改`src/api/productApi.js`文件：

1. 更新`API_BASE_URL`为你的后端服务地址
2. 取消注释真实的fetch调用代码
3. 删除或注释模拟数据部分

示例：
```javascript
// 修改API基础URL
const API_BASE_URL = 'https://your-api.com/api'

// 在getProducts方法中使用真实API
async getProducts(params = {}) {
  const response = await fetch(`${API_BASE_URL}/products?${new URLSearchParams(params)}`)
  return response.json()
}
```

## 自定义配置

### 修改商品分类
在`ProductManager.vue`组件中的`el-select`部分修改分类选项：

```vue
<el-select v-model="productFormData.category" placeholder="请选择分类">
  <el-option label="你的分类1" value="你的分类1" />
  <el-option label="你的分类2" value="你的分类2" />
  <!-- 添加更多分类... -->
</el-select>
```

### 修改表单验证规则
在`ProductManager.vue`组件中的`formRules`对象中修改验证规则：

```javascript
const formRules = {
  name: [
    { required: true, message: '请输入商品名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  // 添加更多验证规则...
}
```

### 修改表格列
在`ProductManager.vue`组件的`el-table`部分添加或修改列：

```vue
<el-table-column prop="newField" label="新字段" width="120" />
```

## 技术栈

- **Vue 3** - 渐进式JavaScript框架
- **Element Plus** - Vue 3的组件库
- **Vite** - 前端构建工具
- **JavaScript ES6+** - 现代JavaScript语法

## 浏览器支持

- Chrome >= 87
- Firefox >= 78
- Safari >= 14
- Edge >= 88

## 许可证

MIT License

## 贡献

欢迎提交Issue和Pull Request来改进这个项目！

## 更新日志

### v1.0.0 (2024-01-20)
- 初始版本发布
- 实现基本的商品CRUD功能
- 添加搜索和分页功能
- 响应式设计支持 