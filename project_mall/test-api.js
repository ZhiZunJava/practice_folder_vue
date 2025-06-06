const axios = require('axios');

const BASE_URL = 'http://localhost:8080/api';

async function testAPIs() {
  console.log('开始测试后端API...\n');

  // 测试1: 获取分类树
  try {
    console.log('1. 测试获取分类树...');
    const response = await axios.get(`${BASE_URL}/categories/tree`);
    console.log('✅ 分类树API正常:', response.data.code === 200 ? '成功' : '失败');
  } catch (error) {
    console.log('❌ 分类树API失败:', error.message);
  }

  // 测试2: 获取热门商品
  try {
    console.log('2. 测试获取热门商品...');
    const response = await axios.get(`${BASE_URL}/products/hot?limit=5`);
    console.log('✅ 热门商品API正常:', response.data.code === 200 ? '成功' : '失败');
  } catch (error) {
    console.log('❌ 热门商品API失败:', error.message);
  }

  // 测试3: 测试登录（需要用户数据）
  try {
    console.log('3. 测试用户登录...');
    const response = await axios.post(`${BASE_URL}/user/login`, {
      username: 'test',
      password: '123456'
    });
    console.log('✅ 登录API正常:', response.data.code === 200 ? '成功' : '失败');
  } catch (error) {
    console.log('❌ 登录API失败:', error.response?.data?.message || error.message);
  }

  console.log('\nAPI测试完成！');
}

testAPIs(); 