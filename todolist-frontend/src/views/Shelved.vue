<template>
  <div class="shelved-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>搁置任务列表</span>
        </div>
      </template>

      <el-table :data="tasks" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="groupname" label="分组" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.groupname" :color="row.groupcolor" effect="dark">
              {{ row.groupname }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priorityDesc" label="优先级" width="80">
          <template #default="{ row }">
            <el-tag :type="getPriorityType(row.priority)">{{ row.priorityDesc }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="duedate" label="截止日期" width="150">
          <template #default="{ row }">
            <span :class="{ overdue: isOverdue(row) }">
              {{ formatDate(row.duedate) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createtime" label="创建时间" width="150">
          <template #default="{ row }">
            {{ formatDate(row.createtime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" @click="editTask(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDeleteTask(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="batch-actions" v-if="selectedTasks.length > 0">
        <el-button size="small" type="primary" @click="batchChangeStatus">批量修改状态</el-button>
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" title="编辑任务" width="500px">
      <el-form ref="taskFormRef" :model="taskForm" :rules="taskRules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="taskForm.title" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="taskForm.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="分组">
          <el-select v-model="taskForm.groupid" clearable placeholder="选择分组">
            <el-option v-for="g in groups" :key="g.id" :label="g.name" :value="g.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="taskForm.priority">
            <el-option label="紧急" :value="4" />
            <el-option label="高" :value="3" />
            <el-option label="中" :value="2" />
            <el-option label="低" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="taskForm.status">
            <el-option label="待办" :value="1" />
            <el-option label="进行中" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="搁置" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="截止日期">
          <el-date-picker v-model="taskForm.duedate" type="datetime" placeholder="选择截止日期" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitTask">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="batchDialogVisible" title="批量修改状态" width="300px">
      <el-form label-width="80px">
        <el-form-item label="新状态">
          <el-select v-model="batchStatus">
            <el-option label="待办" :value="1" />
            <el-option label="进行中" :value="2" />
            <el-option label="已完成" :value="3" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="batchDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBatchStatus">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getShelvedTaskList, updateTask, deleteTask as deleteTaskApi, batchUpdateStatus } from '../api/task'
import { getGroupList } from '../api/group'

const tasks = ref([])
const groups = ref([])
const selectedTasks = ref([])
const dialogVisible = ref(false)
const batchDialogVisible = ref(false)
const batchStatus = ref(1)
const editId = ref(null)
const taskFormRef = ref()

const taskForm = reactive({
  title: '',
  description: '',
  groupid: null,
  priority: 2,
  status: 4,
  duedate: null
})

const taskRules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }]
}

const fetchTasks = async () => {
  const res = await getShelvedTaskList()
  tasks.value = res.data
}

const fetchGroups = async () => {
  const res = await getGroupList()
  groups.value = res.data
}

const handleSelectionChange = (selection) => {
  selectedTasks.value = selection
}

const editTask = (task) => {
  editId.value = task.id
  taskForm.title = task.title
  taskForm.description = task.description
  taskForm.groupid = task.groupid
  taskForm.priority = task.priority
  taskForm.status = task.status
  taskForm.duedate = task.duedate
  dialogVisible.value = true
}

const submitTask = async () => {
  await taskFormRef.value.validate()
  await updateTask(editId.value, taskForm)
  ElMessage.success('更新成功')
  dialogVisible.value = false
  fetchTasks()
}

const handleDeleteTask = async (task) => {
  await ElMessageBox.confirm('确定删除该任务？', '提示', { type: 'warning' })
  await deleteTaskApi(task.id)
  ElMessage.success('删除成功')
  fetchTasks()
}

const batchChangeStatus = () => {
  batchStatus.value = 1
  batchDialogVisible.value = true
}

const submitBatchStatus = async () => {
  const taskIds = selectedTasks.value.map(t => t.id)
  await batchUpdateStatus({ taskIds, status: batchStatus.value })
  ElMessage.success('批量更新成功')
  batchDialogVisible.value = false
  fetchTasks()
}

const getPriorityType = (priority) => {
  const types = { 1: '', 2: 'warning', 3: 'danger', 4: 'danger' }
  return types[priority] || ''
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

const isOverdue = (task) => {
  if (!task.duedate) return false
  return new Date(task.duedate) < new Date()
}

onMounted(() => {
  fetchTasks()
  fetchGroups()
})
</script>

<style scoped>
.shelved-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.batch-actions {
  margin-top: 10px;
}

.overdue {
  color: #f56c6c;
}
</style>