# 电商系统项目

这是一个完整的电商系统，包含用户前端、后台管理和后端API三个子项目。

## 项目结构

```
project_mall/
├── frontend/          # 用户前端 (Vue3 + Vant)
├── admin/            # 后台管理 (Vue3 + ElementPlus)
├── backend/          # 后端API (SpringBoot3)
├── start-all.bat     # 启动脚本
└── README.md
```

## 技术栈

### 前端 (frontend)
- **框架**: Vue3 + Vite
- **UI库**: Vant (移动端UI组件库)
- **路由**: Vue Router
- **HTTP**: Axios
- **端口**: 3000

### 后台管理 (admin)
- **框架**: Vue3 + Vite
- **UI库**: ElementPlus (桌面端UI组件库)
- **图表**: ECharts
- **路由**: Vue Router
- **HTTP**: Axios
- **端口**: 3001

### 后端 (backend)
- **框架**: SpringBoot 3.2.0
- **数据库**: MySQL 8.0
- **ORM**: MyBatis Plus
- **认证**: JWT
- **JSON**: FastJSON
- **端口**: 8080

## 功能特性

### 用户前端
- 商品浏览和搜索
- 分类导航
- 商品详情
- 用户登录注册
- 个人中心
- 购物车功能

### 后台管理
- 管理员登录
- 数据统计看板
- 分类管理
- 商品管理
- 用户管理
- 订单管理

### 后端API
- JWT身份认证
- 用户/管理员分离认证
- RESTful API设计
- 文件上传功能
- 数据库CRUD操作

## 快速开始

### 环境要求
- Node.js 16+
- Java 17+
- Maven 3.6+
- MySQL 8.0+

### 启动方式

#### 方式一：使用启动脚本（推荐）
```bash
# 双击运行启动脚本
start-all.bat
```

#### 方式二：手动启动

1. **启动前端**
```bash
cd frontend
npm install
npm run dev
```

2. **启动后台管理**
```bash
cd admin
npm install
npm run dev
```

3. **启动后端**
```bash
cd backend
# 首先配置数据库连接 src/main/resources/application.yml
mvn spring-boot:run
```

### 访问地址
- 用户前端: http://localhost:3000
- 后台管理: http://localhost:3001
- 后端API: http://localhost:8080

## 数据库配置

在 `backend/src/main/resources/application.yml` 中配置数据库连接：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

## 默认账户

### 管理员账户
- 用户名: admin
- 密码: 123456

### 测试用户
- 用户名: user
- 密码: 123456

## 项目特点

1. **前后端分离**: 采用前后端分离架构，便于开发和部署
2. **移动端优先**: 前端使用Vant组件库，完美适配移动端
3. **现代化技术栈**: 使用最新的Vue3、SpringBoot3等技术
4. **完整权限控制**: JWT token认证，用户和管理员分离
5. **响应式设计**: 支持多端访问
6. **可扩展性强**: 模块化设计，易于功能扩展

## 开发说明

### 前端开发
- 使用 Vue3 Composition API
- Vant组件库提供丰富的移动端组件
- Axios拦截器统一处理API请求
- Vue Router进行页面路由管理

### 后台开发
- ElementPlus提供完整的后台UI组件
- ECharts实现数据可视化
- 自动导入ElementPlus组件，提升开发效率

### 后端开发
- SpringBoot3 + MyBatis Plus
- JWT实现无状态身份认证
- 统一异常处理和响应格式
- 支持文件上传和图片处理

## 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交代码
4. 创建Pull Request

## 许可证

MIT License 