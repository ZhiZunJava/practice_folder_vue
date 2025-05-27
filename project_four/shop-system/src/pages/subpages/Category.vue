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
import { Picture, Edit, Delete, Plus } from '@element-plus/icons-vue'; // 引入图标

const tableData = ref([]);
const dialogVisible = ref(false);
const id = ref(0);
const categoryForm = ref();
const tableRef = ref(); // 用于操作表格实例，例如清空选择等

onMounted(() => {
  loadCategoryList();
});

const loadCategoryList = async () => {
  const data = await getCategoryList();
  // 假设API返回的数据可能包含status字段，1为启用，0为禁用
  // 此处为示例，如果实际数据没有status，可以移除或根据实际情况调整
  const processedData = data.map(item => ({ ...item, status: item.status === undefined ? 1 : item.status }));
  tableData.value = convertToTree(processedData);
};

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
      if (parent) { //确保父节点存在
        parent.children.push(node)
      }
    }
  }
  return treeData
}

const addRow = () => {
  id.value = 0;
  // dialogVisible.value = true; // CategoryEdit 内部可能会重置表单，先打开dialog再调用resetForm更稳妥
  // if (categoryForm.value) { // 确保 categoryForm 存在，因为 dialog 内容是惰性渲染的
  //   categoryForm.value.resetForm(0);
  // }
  // 更改为先打开对话框，再在nextTick中调用resetForm
  dialogVisible.value = true;
  // nextTick(() => { //  如果CategoryEdit内部没有自动重置逻辑，可以在这里调用
  //   if (categoryForm.value) {
  //     categoryForm.value.resetForm(0);
  //   }
  // });
};

const editRow = (row) => {
  id.value = row.id;
  dialogVisible.value = true;
  // nextTick(() => { // 同上，如果CategoryEdit内部没有自动重置逻辑，可以在这里调用
  //   if (categoryForm.value) {
  //    categoryForm.value.resetForm(row.id);
  //   }
  // });
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
      // 假设 delCategory 返回一个包含 success 属性的对象或直接是布尔值
      if (result && (result.success === undefined || result.success)) { 
        ElMessage.success('删除成功');
        loadCategoryList();
      } else {
        // ElMessage.error(result.message || '删除失败'); // 如果API返回错误信息
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
  // 检查 CategoryEdit 组件内部是否有isDirty之类的状态来判断表单是否被修改过
  // if (categoryForm.value && categoryForm.value.isFormDirty()) { // 假设有 isFormDirty 方法
  //   ElMessageBox.confirm('表单内容已修改，确定关闭对话框吗？未保存的更改将会丢失。', '提示', {
  //     confirmButtonText: '确定关闭',
  //     cancelButtonText: '继续编辑',
  //     type: 'warning',
  //     draggable: true,
  //   }).then(() => {
  //     done(); 
  //   }).catch(() => {});
  // } else {
  //   done();
  // }
  // 简化版：总是提示，如果需要更智能的提示，需要 CategoryEdit 组件配合
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
