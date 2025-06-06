@echo off
echo ===========================
echo 启动电商系统所有服务
echo ===========================

echo 正在安装前端依赖...
cd frontend
call npm install
if %errorlevel% neq 0 (
    echo 前端依赖安装失败
    pause
    exit /b 1
)

echo 正在安装后台管理依赖...
cd ../admin
call npm install
if %errorlevel% neq 0 (
    echo 后台管理依赖安装失败
    pause
    exit /b 1
)

echo 正在启动前端服务 (端口3000)...
cd ../frontend
start cmd /k "npm run dev"

echo 正在启动后台管理服务 (端口3001)...
cd ../admin
start cmd /k "npm run dev"

echo 正在启动后端服务 (端口8080)...
cd ../backend
start cmd /k "mvn spring-boot:run"

echo ===========================
echo 所有服务启动完成！
echo 前端访问地址: http://localhost:3000
echo 后台管理访问地址: http://localhost:3001
echo 后端API地址: http://localhost:8080
echo ===========================

pause 