import request from '../utils/request'

export function login(data) {
  return request.post('/auth/login', data)
}

export function logout() {
  return request.post('/auth/logout')
}

export function refreshToken() {
  return request.post('/auth/refresh')
}

export function getUserInfo() {
  return request.get('/user/info')
}

export function changePassword(data) {
  return request.post('/user/change-password', data)
}