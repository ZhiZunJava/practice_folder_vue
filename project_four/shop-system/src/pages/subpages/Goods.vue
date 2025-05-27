<template>
  <div class="goods-management-page">
    <el-button type="primary" :icon="Plus" style="margin-bottom: 15px;" @click="addRow">新增商品</el-button>
    
    <el-dialog 
      v-model="dialogVisible" 
      :title="id ? '修改商品' : '新增商品'" 
      width="750px" 
      :before-close="handleBeforeClose"
      draggable
      destroy-on-close
    >
      <GoodsEdit ref="goodsForm" :id="id" @success="editSuccess" />
    </el-dialog>

    <el-table 
      :data="goodsList" 
      style="width: 100%; margin-bottom: 20px;" 
      row-key="id" 
      border 
      empty-text="暂无商品数据"
    >
      <el-table-column prop="id" label="商品编号" width="120" align="center" sortable />
      <el-table-column prop="name" label="商品名称" min-width="220" show-overflow-tooltip sortable />
      <el-table-column prop="category_name" label="所属分类" width="120" />
      <el-table-column prop="price" label="价格" width="100" align="center" sortable >
        <template #default="{ row }">
          ¥{{ parseFloat(row.price).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="100" align="center" sortable />
      <el-table-column prop="description" label="商品简介" min-width="250" show-overflow-tooltip />
      <el-table-column label="图片" width="100" align="center">
        <template #default="{ row }">
          <el-image 
            v-if="row.picture" 
            :src="row.picture" 
            fit="contain" 
            style="width: 60px; height: 60px; vertical-align: middle;"
            :preview-src-list="[row.picture]"
            preview-teleported
            hide-on-click-modal
          >
            <template #error>
              <div class="image-slot">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
            {{ row.status === 1 ? '上架' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="150" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" icon="Edit" @click="editRow(row)">编辑</el-button>
          <el-button link type="danger" size="small" icon="Delete" @click="delRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-if="total > 0"
      v-model:current-page="page"
      v-model:page-size="pagesize"
      :page-sizes="[5, 10, 20, 50]" 
      background 
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      style="margin-top: 20px; justify-content: flex-end;"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getGoodsList, delGoods } from '../../api';
import GoodsEdit from '../../components/GoodsEdit.vue';
import { ElMessageBox, ElMessage } from 'element-plus';
import { Picture, Edit, Delete, Plus } from '@element-plus/icons-vue';

const goodsList = ref([]);
const page = ref(1);
const pagesize = ref(10);
const total = ref(0);
const id = ref();
const dialogVisible = ref(false);
const goodsForm = ref();

onMounted(() => {
  loadGoodsList();
});

const loadGoodsList = async () => {
  const params = {
    page: page.value,
    pagesize: pagesize.value,
  };
  try {
    const data = await getGoodsList(params);
    goodsList.value = data.list.map(item => {
      item.status = item.status === undefined ? 1 : item.status;
      item.category_name = item.category_name || '-';
      item.description = removeHtmlTags(item.description);
      return item;
    });
    total.value = data.total;
  } catch (error) {
    ElMessage.error('获取商品列表失败');
    console.error('Error loading goods list:', error);
  }
};

const removeHtmlTags = str => {
  if (!str) return '';
  return str.replace(/<[^>]+>/g, '');
};

const addRow = () => {
  id.value = 0;
  dialogVisible.value = true;
};

const editRow = (row) => {
  id.value = row.id;
  dialogVisible.value = true;
};

const delRow = (row) => {
  ElMessageBox.confirm(`确定要删除商品 " ${row.name} " 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    draggable: true,
  }).then(async () => {
    try {
      const result = await delGoods({ id: row.id });
      if (result) {
        ElMessage.success('删除成功');
        loadGoodsList(); 
      }
    } catch (error) {
      console.error('Error deleting goods:', error);
    }
  }).catch(() => {
    ElMessage.info('已取消删除');
  });
};

const handleSizeChange = (val) => {
  pagesize.value = val;
  loadGoodsList();
};

const handleCurrentChange = (val) => {
  page.value = val;
  loadGoodsList();
};

const editSuccess = () => {
  loadGoodsList();
  dialogVisible.value = false;
  ElMessage.success(id.value === 0 ? '新增成功' : '修改成功');
};

const handleBeforeClose = (done) => {
  ElMessageBox.confirm('确定关闭对话框吗？未保存的更改将会丢失。','提示', {
    confirmButtonText: '确定关闭',
    cancelButtonText: '继续编辑',
    type: 'warning',
    draggable: true,
  }).then(() => {
    done();
  });
};

</script>

<style scoped>
.goods-management-page {
  min-height: calc(100vh - 80px);
}

.el-table :deep(th.el-table__cell) {
  background-color: #fafafa !important;
  color: #333;
  font-weight: 600;
}

.el-button + .el-button {
  margin-left: 8px;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #c0c4cc;
  font-size: 24px;
}

:deep(.el-dialog__body) {
  padding: 20px 25px;
}

.el-pagination {
  display: flex;
  justify-content: flex-end;
}
</style>