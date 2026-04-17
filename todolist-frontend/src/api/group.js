import request from '../utils/request'

export function getGroupList() {
  return request.get('/groups')
}

export function getGroupDetail(id) {
  return request.get(`/groups/${id}`)
}

export function createGroup(data) {
  return request.post('/groups', data)
}

export function updateGroup(id, data) {
  return request.put(`/groups/${id}`, data)
}

export function deleteGroup(id) {
  return request.delete(`/groups/${id}`)
}