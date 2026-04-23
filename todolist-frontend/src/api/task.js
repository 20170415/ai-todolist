import request from '../utils/request'

export function getTaskList(params) {
  return request.get('/tasks', { params })
}

export function getShelvedTaskList() {
  return request.get('/tasks/shelved')
}

export function getTaskDetail(id) {
  return request.get(`/tasks/${id}`)
}

export function createTask(data) {
  return request.post('/tasks', data)
}

export function updateTask(id, data) {
  return request.put(`/tasks/${id}`, data)
}

export function deleteTask(id) {
  return request.delete(`/tasks/${id}`)
}

export function batchUpdateStatus(data) {
  return request.put('/tasks/batch-status', data)
}

export function getTaskStats() {
  return request.get('/tasks/stats')
}