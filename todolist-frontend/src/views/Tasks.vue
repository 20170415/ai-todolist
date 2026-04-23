<template>
  <div class="tasks-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <el-icon size="24" color="#409eff"><List /></el-icon>
            <div>
              <span class="stat-label">待办</span>
              <span class="stat-value">{{ stats.todoCount }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <el-icon size="24" color="#e6a23c"><Loading /></el-icon>
            <div>
              <span class="stat-label">进行中</span>
              <span class="stat-value">{{ stats.inProgressCount }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <el-icon size="24" color="#67c23a"><CircleCheck /></el-icon>
            <div>
              <span class="stat-label">已完成</span>
              <span class="stat-value">{{ stats.completedCount }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <el-icon size="24" color="#909399"><Clock /></el-icon>
            <div>
              <span class="stat-label">搁置</span>
              <span class="stat-value">{{ stats.shelvedCount }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="task-card">
      <template #header>
        <div class="card-header">
          <span>任务列表</span>
          <el-button type="primary" @click="showAddDialog">新增任务</el-button>
        </div>
      </template>

      <div class="filter-row">
        <el-form inline>
          <el-form-item label="状态">
            <el-select v-model="filter.status" clearable placeholder="全部" style="width: 120px">
              <el-option label="待办" :value="1" />
              <el-option label="进行中" :value="2" />
              <el-option label="已完成" :value="3" />
            </el-select>
          </el-form-item>
          <el-form-item label="分组">
            <el-select v-model="filter.groupid" clearable placeholder="全部" style="width: 150px">
              <el-option v-for="g in groups" :key="g.id" :label="g.name" :value="g.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="优先级">
            <el-select v-model="filter.priority" clearable placeholder="全部" style="width: 120px">
              <el-option label="紧急" :value="4" />
              <el-option label="高" :value="3" />
              <el-option label="中" :value="2" />
              <el-option label="低" :value="1" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
          </el-form-item>
          <el-form-item>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
        <el-button type="warning" class="batch-btn" @click="batchChangeStatus">批量修改状态</el-button>
      </div>

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
        <el-table-column prop="statusDesc" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.statusDesc }}</el-tag>
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
        <el-table-column prop="updatetime" label="更新时间" width="150">
          <template #default="{ row }">
            {{ formatDate(row.updatetime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" @click="editTask(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDeleteTask(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑任务' : '新增任务'" width="500px">
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
            <el-option label="搁置" :value="4" />
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
import { getTaskList, createTask, updateTask, deleteTask as deleteTaskApi, batchUpdateStatus, getTaskStats } from '../api/task'
import { getGroupList } from '../api/group'

const tasks = ref([])
const groups = ref([])
const stats = ref({
  todoCount: 0,
  inProgressCount: 0,
  completedCount: 0,
  shelvedCount: 0,
  totalCount: 0
})

const filter = reactive({
  status: null,
  groupid: null,
  priority: null
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const selectedTasks = ref([])
const dialogVisible = ref(false)
const batchDialogVisible = ref(false)
const batchStatus = ref(1)
const isEdit = ref(false)
const editId = ref(null)
const taskFormRef = ref()

const taskForm = reactive({
  title: '',
  description: '',
  groupid: null,
  priority: 2,
  status: 1,
  duedate: null
})

const taskRules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }]
}

const fetchTasks = async () => {
  const params = {
    page: pagination.page,
    size: pagination.size,
    status: filter.status,
    groupid: filter.groupid,
    priority: filter.priority
  }
  const res = await getTaskList(params)
  tasks.value = res.data.records
  pagination.total = res.data.total
}

const fetchGroups = async () => {
  const res = await getGroupList()
  groups.value = res.data
}

const fetchStats = async () => {
  const res = await getTaskStats()
  stats.value = res.data
}

const handleSearch = () => {
  pagination.page = 1
  fetchTasks()
}

const handleReset = () => {
  filter.status = null
  filter.groupid = null
  filter.priority = null
  pagination.page = 1
  fetchTasks()
}

const handleSizeChange = () => {
  pagination.page = 1
  fetchTasks()
}

const handleCurrentChange = () => {
  fetchTasks()
}

const handleSelectionChange = (selection) => {
  selectedTasks.value = selection
}

const showAddDialog = () => {
  isEdit.value = false
  editId.value = null
  taskForm.title = ''
  taskForm.description = ''
  taskForm.groupid = null
  taskForm.priority = 2
  taskForm.status = 1
  taskForm.duedate = null
  dialogVisible.value = true
}

const editTask = (task) => {
  isEdit.value = true
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
  if (isEdit.value) {
    await updateTask(editId.value, taskForm)
    ElMessage.success('更新成功')
  } else {
    await createTask(taskForm)
    ElMessage.success('创建成功')
  }
  dialogVisible.value = false
  fetchTasks()
  fetchStats()
}

const handleDeleteTask = async (task) => {
  await ElMessageBox.confirm('确定删除该任务？', '提示', { type: 'warning' })
  await deleteTaskApi(task.id)
  ElMessage.success('删除成功')
  fetchTasks()
  fetchStats()
}

const batchChangeStatus = () => {
  if (selectedTasks.value.length === 0) {
    ElMessage.warning('请先选择任务')
    return
  }
  batchStatus.value = 1
  batchDialogVisible.value = true
}

const submitBatchStatus = async () => {
  const taskIds = selectedTasks.value.map(t => t.id)
  await batchUpdateStatus({ taskIds, status: batchStatus.value })
  ElMessage.success('批量更新成功')
  batchDialogVisible.value = false
  fetchTasks()
  fetchStats()
}

const getPriorityType = (priority) => {
  const types = { 1: '', 2: 'warning', 3: 'danger', 4: 'danger' }
  return types[priority] || ''
}

const getStatusType = (status) => {
  const types = { 1: 'info', 2: 'warning', 3: 'success', 4: '' }
  return types[status] || ''
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

const isOverdue = (task) => {
  if (!task.duedate || task.status === 3) return false
  return new Date(task.duedate) < new Date()
}

onMounted(() => {
  fetchTasks()
  fetchGroups()
  fetchStats()
})
</script>

<style scoped>
.tasks-container {
  padding: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
}

.task-card {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.filter-row .el-form {
  flex: 1;
}

.batch-btn {
  margin-top: 4px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.overdue {
  color: #f56c6c;
}
</style>