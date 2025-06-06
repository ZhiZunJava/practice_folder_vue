# 项目启动说明

## 环境要求
- Java 17+
- Maven 3.6+
- Node.js 16+
- MySQL 8.0+

## 后端启动步骤

1. 确保MySQL服务运行，创建数据库：
```sql
CREATE DATABASE mall_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改数据库配置（backend/src/main/resources/application.yml）：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mall_db?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8mb4
    username: root
    password: your_password
```

3. 启动后端服务：
```bash
cd backend
mvn spring-boot:run
```

## 前端启动步骤

1. 安装依赖：
```bash
cd frontend
npm install
```

2. 启动开发服务器：
```bash
npm run dev
```

3. 访问：http://localhost:5173

## API接口修复完成

### 主要修复内容：

1. **API基础配置**
   - 修正baseURL为 `http://localhost:8080/api`
   - 完善请求和响应拦截器
   - 添加用户ID获取辅助函数

2. **购物车功能**
   - 清空默认模拟数据
   - 添加登录状态检查
   - 实现真实API调用（getList, add, updateQuantity, remove等）
   - 修复字段映射（productName, productImage等）

3. **用户登录**
   - 实现真实登录API调用
   - 保存token和用户信息到localStorage
   - 更新用户状态管理

4. **商品数据**
   - 首页调用热门商品API
   - 分类页面调用分类和商品API
   - 保留模拟数据作为降级方案

5. **导航栏**
   - 动态获取真实购物车数量
   - 监听登录状态变化

### API接口对应关系：

| 功能 | 前端调用 | 后端接口 | 状态 |
|------|----------|----------|------|
| 用户登录 | userApi.login | POST /api/user/login | ✅ |
| 获取分类树 | categoryApi.getTree | GET /api/categories/tree | ✅ |
| 热门商品 | productApi.getHotProducts | GET /api/products/hot | ✅ |
| 商品分类查询 | productApi.getByCategory | GET /api/products/category/{id} | ✅ |
| 购物车列表 | cartApi.getList | GET /api/cart?userId={userId} | ✅ |
| 添加购物车 | cartApi.add | POST /api/cart | ✅ |
| 更新数量 | cartApi.updateQuantity | PUT /api/cart/{id} | ✅ |
| 删除商品 | cartApi.remove | DELETE /api/cart/{id} | ✅ |

### 测试步骤：

1. 启动后端服务
2. 启动前端服务  
3. 在"我的"页面登录（如果有测试用户数据）
4. 测试各个页面的API调用
5. 检查购物车功能

## 注意事项

- 首次启动需要确保数据库中有基础数据（分类、商品、用户等）
- 可以通过后台管理系统添加初始数据
- 前端有降级处理，即使API调用失败也会显示模拟数据
- 所有需要登录的功能都会检查token状态 