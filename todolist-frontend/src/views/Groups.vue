<template>
  <div class="groups-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>分组管理</span>
          <el-button type="primary" @click="showAddDialog">新增分组</el-button>
        </div>
      </template>

      <el-table :data="groups">
        <el-table-column prop="name" label="分组名称" min-width="150" />
        <el-table-column prop="color" label="颜色" width="100">
          <template #default="{ row }">
            <el-tag :color="row.color" effect="dark">{{ row.name }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createtime" label="创建时间" width="150">
          <template #default="{ row }">
            {{ formatDate(row.createtime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" @click="editGroup(row)" :disabled="!row.userid">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDeleteGroup(row)" :disabled="!row.userid">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑分组' : '新增分组'" width="400px">
      <el-form ref="groupFormRef" :model="groupForm" :rules="groupRules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="groupForm.name" />
        </el-form-item>
        <el-form-item label="颜色">
          <el-color-picker v-model="groupForm.color" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitGroup">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getGroupList, createGroup, updateGroup, deleteGroup as deleteGroupApi } from '../api/group'

const groups = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const groupFormRef = ref()

const groupForm = reactive({
  name: '',
  color: '#409EFF'
})

const groupRules = {
  name: [{ required: true, message: '请输入分组名称', trigger: 'blur' }]
}

const fetchGroups = async () => {
  const res = await getGroupList()
  groups.value = res.data
}

const showAddDialog = () => {
  isEdit.value = false
  editId.value = null
  groupForm.name = ''
  groupForm.color = '#409EFF'
  dialogVisible.value = true
}

const editGroup = (group) => {
  isEdit.value = true
  editId.value = group.id
  groupForm.name = group.name
  groupForm.color = group.color
  dialogVisible.value = true
}

const submitGroup = async () => {
  await groupFormRef.value.validate()
  if (isEdit.value) {
    await updateGroup(editId.value, groupForm)
    ElMessage.success('更新成功')
  } else {
    await createGroup(groupForm)
    ElMessage.success('创建成功')
  }
  dialogVisible.value = false
  fetchGroups()
}

const handleDeleteGroup = async (group) => {
  await ElMessageBox.confirm('确定删除该分组？', '提示', { type: 'warning' })
  await deleteGroupApi(group.id)
  ElMessage.success('删除成功')
  fetchGroups()
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

onMounted(() => {
  fetchGroups()
})
</script>

<style scoped>
.groups-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>