import { defineStore } from 'pinia'
import { login, logout, getUserInfo } from '../api/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: JSON.parse(localStorage.getItem('user') || '{}'),
    isLoggedIn: !!localStorage.getItem('token')
  }),

  actions: {
    async login(loginData) {
      const res = await login(loginData)
      this.token = res.data.token
      this.user = res.data.user
      this.isLoggedIn = true
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('user', JSON.stringify(res.data.user))
      return res
    },

    async logout() {
      await logout()
      this.token = ''
      this.user = {}
      this.isLoggedIn = false
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    },

    async getUserInfo() {
      const res = await getUserInfo()
      this.user = res.data
      localStorage.setItem('user', JSON.stringify(res.data))
      return res
    },

    isAdmin() {
      return this.user.role === 'admin'
    }
  }
})