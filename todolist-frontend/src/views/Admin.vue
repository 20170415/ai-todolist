<template>
  <div class="admin-container">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="数据统计" name="stats">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card>
              <div class="stat-item">
                <el-icon size="24" color="#409eff"><User /></el-icon>
                <div>
                  <span class="stat-label">用户总数</span>
                  <span class="stat-value">{{ stats.userCount }}</span>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card>
              <div class="stat-item">
                <el-icon size="24" color="#67c23a"><List /></el-icon>
                <div>
                  <span class="stat-label">任务总数</span>
                  <span class="stat-value">{{ stats.totalTaskCount }}</span>
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
                <el-icon size="24" color="#e6a23c"><TrendCharts /></el-icon>
                <div>
                  <span class="stat-label">完成率</span>
                  <span class="stat-value">{{ stats.completionRate }}%</span>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-card class="task-stats-card">
          <template #header>
            <span>任务状态统计</span>
          </template>
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="task-stat">
                <span class="task-stat-label">待办</span>
                <span class="task-stat-value">{{ taskStats['待办Count'] || 0 }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="task-stat">
                <span class="task-stat-label">进行中</span>
                <span class="task-stat-value">{{ taskStats['进行中Count'] || 0 }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="task-stat">
                <span class="task-stat-label">已完成</span>
                <span class="task-stat-value">{{ taskStats['已完成Count'] || 0 }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="task-stat">
                <span class="task-stat-label">搁置</span>
                <span class="task-stat-value">{{ taskStats['搁置Count'] || 0 }}</span>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="用户管理" name="users">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>用户列表</span>
              <el-button type="primary" @click="showAddDialog">新增用户</el-button>
            </div>
          </template>

          <el-table :data="users">
            <el-table-column prop="username" label="用户名" min-width="100" />
            <el-table-column prop="nickname" label="昵称" min-width="100" />
            <el-table-column prop="role" label="角色" width="100">
              <template #default="{ row }">
                <el-tag :type="row.role === 'admin' ? 'danger' : 'info'">
                  {{ row.role === 'admin' ? '管理员' : '普通用户' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="enabled" label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="row.enabled ? 'success' : 'danger'">
                  {{ row.enabled ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createtime" label="创建时间" width="150">
              <template #default="{ row }">
                {{ formatDate(row.createtime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="280">
              <template #default="{ row }">
                <el-button size="small" @click="editUser(row)">编辑</el-button>
                <el-button size="small" type="warning" @click="handleResetPassword(row)">重置密码</el-button>
                <el-button size="small" type="danger" @click="handleDeleteUser(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑用户' : '新增用户'" width="400px">
          <el-form ref="userFormRef" :model="userForm" :rules="userRules" label-width="80px">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="userForm.username" />
            </el-form-item>
            <el-form-item label="密码" prop="password" v-if="!isEdit">
              <el-input v-model="userForm.password" type="password" show-password />
            </el-form-item>
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="userForm.nickname" />
            </el-form-item>
            <el-form-item label="角色" prop="role">
              <el-select v-model="userForm.role">
                <el-option label="管理员" value="admin" />
                <el-option label="普通用户" value="user" />
              </el-select>
            </el-form-item>
            <el-form-item label="状态">
              <el-switch v-model="userForm.enabled" />
            </el-form-item>
          </el-form>
          <template #footer>
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitUser">确定</el-button>
          </template>
        </el-dialog>
      </el-tab-pane>

      <el-tab-pane label="任务列表" name="tasks">
        <el-card>
          <template #header>
            <span>所有用户任务</span>
          </template>

          <el-table :data="tasks">
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
                {{ formatDate(row.duedate) }}
              </template>
            </el-table-column>
            <el-table-column prop="createtime" label="创建时间" width="150">
              <template #default="{ row }">
                {{ formatDate(row.createtime) }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="搁置任务" name="shelved">
        <el-card>
          <template #header>
            <span>所有用户搁置任务</span>
          </template>

          <el-table :data="shelvedTasks">
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
                {{ formatDate(row.duedate) }}
              </template>
            </el-table-column>
            <el-table-column prop="createtime" label="创建时间" width="150">
              <template #default="{ row }">
                {{ formatDate(row.createtime) }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, createUser, updateUser, deleteUser as deleteUserApi, resetPassword as resetPasswordApi, getStats, getTaskStats, getAllTasks, getAllShelvedTasks } from '../api/admin'

const activeTab = ref('stats')
const users = ref([])
const tasks = ref([])
const shelvedTasks = ref([])
const stats = ref({})
const taskStats = ref({})
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const userFormRef = ref()

const userForm = reactive({
  username: '',
  password: '',
  nickname: '',
  role: 'user',
  enabled: true
})

const userRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }]
}

const fetchStats = async () => {
  const res = await getStats()
  stats.value = res.data
}

const fetchTaskStats = async () => {
  const res = await getTaskStats()
  taskStats.value = res.data
}

const fetchUsers = async () => {
  const res = await getUserList()
  users.value = res.data
}

const fetchTasks = async () => {
  const res = await getAllTasks()
  tasks.value = res.data
}

const fetchShelvedTasks = async () => {
  const res = await getAllShelvedTasks()
  shelvedTasks.value = res.data
}

const showAddDialog = () => {
  isEdit.value = false
  editId.value = null
  userForm.username = ''
  userForm.password = ''
  userForm.nickname = ''
  userForm.role = 'user'
  userForm.enabled = true
  dialogVisible.value = true
}

const editUser = (user) => {
  isEdit.value = true
  editId.value = user.id
  userForm.username = user.username
  userForm.password = ''
  userForm.nickname = user.nickname
  userForm.role = user.role
  userForm.enabled = user.enabled
  dialogVisible.value = true
}

const submitUser = async () => {
  await userFormRef.value.validate()
  if (isEdit.value) {
    await updateUser(editId.value, userForm)
    ElMessage.success('更新成功')
  } else {
    await createUser(userForm)
    ElMessage.success('创建成功')
  }
  dialogVisible.value = false
  fetchUsers()
  fetchStats()
}

const handleDeleteUser = async (user) => {
  await ElMessageBox.confirm('确定删除该用户？', '提示', { type: 'warning' })
  await deleteUserApi(user.id)
  ElMessage.success('删除成功')
  fetchUsers()
  fetchStats()
}

const handleResetPassword = async (user) => {
  await ElMessageBox.confirm('确定重置该用户密码为123456？', '提示', { type: 'warning' })
  await resetPasswordApi(user.id)
  ElMessage.success('密码已重置')
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

onMounted(() => {
  fetchStats()
  fetchTaskStats()
  fetchUsers()
  fetchTasks()
  fetchShelvedTasks()
})
</script>

<style scoped>
.admin-container {
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

.task-stats-card {
  margin-top: 20px;
}

.task-stat {
  text-align: center;
}

.task-stat-label {
  font-size: 14px;
  color: #666;
}

.task-stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>