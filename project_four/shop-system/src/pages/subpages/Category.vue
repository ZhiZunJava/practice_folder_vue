<template>
  <div class="category-management-page">
    <el-button type="primary" :icon="Plus" style="margin-bottom: 15px" @click="addRow">新增分类</el-button>
    
    <el-dialog 
      v-model="dialogVisible" 
      :title="id ? '修改分类' : '新增分类'" 
      width="600px" 
      :before-close="handleBeforeClose"
      draggable
      destroy-on-close
    >
      <CategoryEdit ref="categoryForm" :id="id" @success="editSuccess" />
    </el-dialog>

    <el-table 
      ref="tableRef" 
      :data="tableData" 
      style="width: 100%; margin-bottom: 20px;" 
      row-key="id"
      default-expand-all
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      empty-text="暂无分类数据"
    >
      <el-table-column prop="name" label="分类名称" sortable min-width="200" />
      <el-table-column label="分类级别" width="120" align="center">
        <template #default="{ row }">
          <el-tag :type="row.pid === 0 ? 'success' : 'info'" size="small">
            {{ row.pid === 0 ? '一级分类' : '二级分类' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="分类编号" width="100" align="center" />
      <el-table-column label="分类图片" width="120" align="center">
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
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="180" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" icon="Edit" @click="editRow(row)">编辑</el-button>
          <el-button link type="danger" size="small" icon="Delete" @click="delRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getCategoryList, delCategory } from '../../api';
import { ElMessageBox, ElMessage } from 'element-plus';
import CategoryEdit from '../../components/CategoryEdit.vue';
import { Picture, Edit, Delete, Plus } from '@element-plus/icons-vue';

const tableData = ref([]);
const dialogVisible = ref(false);
const id = ref(0);
const categoryForm = ref();
const tableRef = ref();

onMounted(() => {
  loadCategoryList();
});

const loadCategoryList = async () => {
  const data = await getCategoryList();
  
  const processedData = data.map(item => ({ ...item, status: item.status === undefined ? 1 : item.status }));
  tableData.value = convertToTree(processedData);
};

const convertToTree = data => {
  const treeData = []
  const map = {}
  
  for (const item of data) {
    map[item.id] = { ...item, children: [] }
  }
  
  for (const item of data) {
    const node = map[item.id]
    if (item.pid === 0) {
      treeData.push(node)
    } else {
      const parent = map[item.pid]
      if (parent) {
        parent.children.push(node)
      }
    }
  }
  return treeData
}

const addRow = () => {
  id.value = 0;
  dialogVisible.value = true;
};

const editRow = (row) => {
  id.value = row.id;
  dialogVisible.value = true;
};

const delRow = (row) => {
  if (row.pid === 0 && row.children && row.children.length > 0) {
    ElMessage({
      type: 'warning',
      message: '该分类下存在二级分类，请先删除二级分类再删除此分类',
    });
  } else {
    ElMessageBox.confirm(`确定要删除分类 " ${row.name} " 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
      draggable: true,
    }).then(async () => {
      const result = await delCategory({ id: row.id });
      if (result && (result.success === undefined || result.success)) { 
        ElMessage.success('删除成功');
        loadCategoryList();
      }
    }).catch(() => {
      ElMessage.info('已取消删除');
    });
  }
};

const editSuccess = () => {
  loadCategoryList();
  dialogVisible.value = false;
  ElMessage.success(id.value ? '修改成功' : '新增成功');
};

const handleBeforeClose = (done) => {
   ElMessageBox.confirm('确定关闭对话框吗？', '提示',{
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info',
      draggable: true,
    }).then(() => {
      done(); 
    }).catch(() => {});
};
</script>

<style scoped>
.category-management-page {
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
</style>
