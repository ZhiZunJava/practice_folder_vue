# 电商系统部署指南

## 快速部署步骤

### 1. 环境准备
确保您的系统已安装以下软件：
- Node.js 16+ 
- Java 17+
- Maven 3.6+
- MySQL 8.0+

### 2. 数据库初始化

1. 启动MySQL服务
2. 执行数据库初始化脚本：
```sql
mysql -u root -p < backend/src/main/resources/mall.sql
```

### 3. 后端配置

编辑 `backend/src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mall?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: your_mysql_username
    password: your_mysql_password
```

### 4. 一键启动

**Windows系统**：
```bash
# 双击运行启动脚本
start-all.bat
```

**手动启动**：
```bash
# 终端1 - 启动前端
cd frontend
npm install
npm run dev

# 终端2 - 启动后台管理
cd admin
npm install
npm run dev

# 终端3 - 启动后端
cd backend
mvn spring-boot:run
```

### 5. 访问系统

- **用户前端**: http://localhost:3000
- **后台管理**: http://localhost:3001  
- **后端API**: http://localhost:8080

### 6. 默认账户

**管理员登录** (后台管理系统):
- 用户名: `admin`
- 密码: `123456`

**普通用户** (前端系统):
- 用户名: `user`
- 密码: `123456`

## 生产环境部署

### 前端打包部署

```bash
# 打包前端
cd frontend
npm run build

# 打包后台管理
cd admin
npm run build
```

生成的 `dist` 目录可部署到Nginx或其他Web服务器。

### 后端打包部署

```bash
cd backend
mvn clean package -DskipTests

# 运行jar包
java -jar target/mall-backend-1.0.0.jar
```

### Nginx配置示例

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    # 前端
    location / {
        root /path/to/frontend/dist;
        try_files $uri $uri/ /index.html;
    }
    
    # 后台管理
    location /admin {
        root /path/to/admin/dist;
        try_files $uri $uri/ /admin/index.html;
    }
    
    # API代理
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## Docker部署

### 创建Docker Compose

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: mall
    ports:
      - "3306:3306"
    volumes:
      - ./backend/src/main/resources/mall.sql:/docker-entrypoint-initdb.d/init.sql

  backend:
    build: ./backend
    ports:
      - "8080:8080"
    depends_on:
      - mysql
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/mall

  frontend:
    build: ./frontend
    ports:
      - "3000:80"

  admin:
    build: ./admin
    ports:
      - "3001:80"
```

### 启动Docker环境

```bash
docker-compose up -d
```

## 常见问题

### 1. 端口冲突
如果端口被占用，可以修改配置文件中的端口：
- 前端: `frontend/vite.config.js`
- 后台: `admin/vite.config.js`
- 后端: `backend/src/main/resources/application.yml`

### 2. 数据库连接失败
- 检查MySQL服务是否启动
- 确认数据库用户名密码正确
- 检查防火墙设置

### 3. 跨域问题
开发环境已配置代理，生产环境需要在Nginx中配置CORS或使用代理。

### 4. 文件上传路径
确保后端配置的文件上传路径存在并有写入权限：
```yaml
file:
  upload-path: /path/to/upload/directory
```

## 性能优化建议

### 前端优化
- 开启Gzip压缩
- 使用CDN加速静态资源
- 图片懒加载
- 路由懒加载

### 后端优化
- 数据库连接池配置
- Redis缓存热点数据
- 分页查询优化
- 接口防刷限流

### 数据库优化
- 创建合适的索引
- 定期清理无用数据
- 读写分离
- 分库分表

## 监控和日志

### 应用监控
- 集成Spring Boot Actuator
- 使用Prometheus + Grafana监控
- 接入APM工具

### 日志管理
- 使用ELK Stack收集日志
- 配置日志级别和轮转
- 错误日志告警

## 备份策略

### 数据库备份
```bash
# 每日自动备份
mysqldump -u root -p mall > backup_$(date +%Y%m%d).sql
```

### 文件备份
- 定期备份上传的文件
- 使用对象存储服务
- 异地备份重要数据 