<template>
  <div class="category-page">
    <div class="menu-left">
      <ul>
        <li class="menu-item" v-for="(menu, index) in menus" :key="index" :class="{ current: index === currentIndex }" @click="clickList(index)" ref="menuList">
          <p class="text">{{ menu.name }}</p>
        </li>
      </ul>
    </div>
    <div class="menu-right" ref="itemList">
      <ul>
        <li class="cate" v-for="(menu, index1) in menus" :key="index1">
          <h4 class="cate-title"><span>{{ menu.name }}</span></h4>
          <ul class="cate-item-list">
            <li class="cate-item" v-for="(item, index2) in menu.children" :key="index2">
              <router-link class="cate-item-wrapper" :to="{name: 'goodslist', params: { category_id: item.id } }">
                <div class="cate-item-img-container">
                  <img :src="item.picture" alt="" class="cate-item-img">
                </div>
                <span class="cate-item-name">{{ item.name }}</span>
              </router-link>
            </li>
          </ul>
        </li>
        <li class="cate-bottom"></li>
      </ul>
    </div>
  </div>
</template>

<script setup>

import { onMounted, watch, nextTick, ref, computed } from 'vue'
import { getCategoryList } from '../api'

import BScroll from '@better-scroll/core'

const menus = ref([])
const itemList = ref()
const rightLiTops = ref([])
const menuList = ref()
const scrollY = ref(0)

let leftBscroll = null
let rightBscroll = null

onMounted(() => {
  loadCategoryList()
  RightHeightFix()
})

// 监听
watch(menus, () => {
  nextTick(() => {
    initBScroll()
    initRightHeight()
  })
})

// 获取分类数据
const loadCategoryList = async () => {
  let data = await getCategoryList()
  // 将一维数组数据转换为树形结构
  const treeData = convertToTree(data)
  // 将转换后的数据赋值给menus
  menus.value = treeData
}

// 将一维数组转换成树形结构的方法
const convertToTree = data => {
  const treeData = []
  const map = {}
  // 遍历一维数组数据，建立节点映射表
  for (const item of data) {
    map[item.id] = { ...item, children: [] }
  }
  // 遍历映射表，将节点添加到父节点的children中
  for (const item of data) {
    const node = map[item.id]
    if (item.pid === 0) {
      treeData.push(node)
    } else {
      const parent = map[item.pid]
      parent.children.push(node)
    }
  }
  return treeData
}

// 单击左侧菜单项
const clickList = index => {
  scrollY.value = rightLiTops.value[index]
  rightBscroll.scrollTo(0, -scrollY.value)
}

// 初始化BScroll
const initBScroll = () => {
  // 初始化左菜单
  leftBscroll = new BScroll('.menu-left', {
    click: true,
    mouseWheel: true
  })
  // 初始化右菜单
  rightBscroll = new BScroll('.menu-right', {
    click: true,
    mouseWheel: true,
    probeType: 3 // 实时派发scroll事件
  })
  rightBscroll.on('scroll', pos => {
    scrollY.value = Math.abs(pos.y)
  })
}

// 初始化右边菜单的高度
const initRightHeight = () => {
  const itemArray = []
  let top = 0
  itemArray.push(top)
  const allList = itemList.value.getElementsByClassName('cate')
  Array.prototype.slice.call(allList).forEach(li => {
    top += li.clientHeight
    itemArray.push(top)
  })
  rightLiTops.value = itemArray
}

const RightHeightFix = () => {
  let bottom = itemList.value.getElementsByClassName('cate-bottom')[0]
  bottom.style.height = itemList.value.clientHeight / 1.2 + 'px'
}

// 右菜单滚动时，左菜单联动
const initLeftScroll = index => {
  const menu = menuList.value
  let el = menu[index]
  leftBscroll.scrollToElement(el, 300, 0, -100)
}

// 监听数据的到达，并在回调函数中更新
const currentIndex = computed(() => {
  return rightLiTops.value.findIndex((top, index) => {
    if (index === rightLiTops.value.length - 2) {
      return true
    }
    if (scrollY.value >= top && scrollY.value < rightLiTops.value[index + 1]) {
      initLeftScroll(index)
      return true
    }
  })
})
</script>

<style lang="less" scoped>
ul {
  margin: 0;
  padding: 0;
}
li {
  list-style: none;
}

.category-page {
  display: flex;
  position: absolute;
  top: 46px;
  bottom: 50px;
  width: 100%;
  overflow: hidden;
  background-color: #f7f8fa;

  .menu-left {
    flex: 0 0 85px;
    width: 85px;
    background: #fff;
    overflow-y: auto;
    overflow-x: hidden;
    .menu-item {
      height: 50px; // 菜单项高度
      line-height: 50px; // 垂直居中文本
      width: 100%;
      text-align: center;
      font-size: 14px;
      color: #333;
      border-bottom: 1px solid #eee; // 分隔线
      transition: background-color 0.3s, color 0.3s; // 过渡效果

      &:last-child {
        border-bottom: none;
      }

      .text {
        margin: 0;
        padding: 0 5px; // 文本内边距
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
    .current {
      background: #f7f8fa; // 选中项背景色
      color: #e54d42; // 主题红色
      font-weight: bold;
      border-left: 3px solid #e54d42; // 左侧选中指示条
      .text {
        color: #e54d42;
      }
    }
  }

  .menu-right {
    flex: 1;
    background: #f7f8fa; // 右侧内容背景色
    padding: 10px;
    overflow-y: auto; // (BScroll会处理)

    .cate {
      margin-bottom: 15px;
      background-color: #fff; // 每个分类块的背景
      border-radius: 8px;
      padding: 10px;
      box-shadow: 0 1px 3px rgba(0,0,0,0.04);

      .cate-title {
        margin: 0 0 12px 0;
        text-align: left;
        font-size: 15px;
        color: #333;
        font-weight: 600; // 加粗
        position: relative;
        padding-left: 10px;
        // 标题装饰线
        &::before {
          content: '';
          position: absolute;
          left: 0;
          top: 50%;
          transform: translateY(-50%);
          width: 4px;
          height: 16px;
          background-color: #e54d42;
          border-radius: 2px;
        }
      }
      .cate-item-list {
        display: flex;
        flex-wrap: wrap;
        // padding: 5px 0 0; // 移除，由cate-item的margin控制
      }

      .cate-item {
        width: 33.333%; // 每行3个
        text-align: center;
        margin-bottom: 10px;

        .cate-item-wrapper {
          display: flex;
          flex-direction: column;
          align-items: center;
          text-decoration: none;
          color: #333;

          .cate-item-img-container {
            width: 65px; // 图片容器宽度
            height: 65px; // 图片容器高度
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 8px;
            background-color: #fff; // 图片背景，如果图片透明
            border-radius: 6px; // 图片容器圆角
            // border: 1px solid #eee; // 可选：图片边框
            overflow: hidden; // 确保图片不超出容器
          }
          .cate-item-img {
            max-width: 100%;
            max-height: 100%;
            object-fit: contain; // 图片适应方式
          }
          .cate-item-name {
            display: block;
            font-size: 12px;
            color: #555;
            line-height: 1.4;
            // 最多显示两行，超出省略
            height: (12px * 1.4 * 2); // 计算两行的高度
            overflow: hidden;
            text-overflow: ellipsis;
          }
        }
      }
    }
  }
}
</style>
