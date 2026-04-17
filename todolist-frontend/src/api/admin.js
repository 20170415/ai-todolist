import request from '../utils/request'

export function getUserList() {
  return request.get('/admin/users')
}

export function getUserPage(params) {
  return request.get('/admin/users/page', { params })
}

export function createUser(data) {
  return request.post('/admin/users', data)
}

export function updateUser(id, data) {
  return request.put(`/admin/users/${id}`, data)
}

export function deleteUser(id) {
  return request.delete(`/admin/users/${id}`)
}

export function resetPassword(id) {
  return request.post(`/admin/users/${id}/reset-password`)
}

export function getStats() {
  return request.get('/admin/stats')
}

export function getTaskStats() {
  return request.get('/admin/stats/tasks')
}

export function getAllTasks() {
  return request.get('/admin/tasks')
}

export function getAllShelvedTasks() {
  return request.get('/admin/tasks/shelved')
}