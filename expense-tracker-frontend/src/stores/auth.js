import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api/auth'
import { userApi } from '@/api/user'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
  const token = ref(localStorage.getItem('token') || '')

  const isLoggedIn = computed(() => !!token.value)

  async function login(credentials) {
    const { data } = await authApi.login(credentials)
    token.value = data.token
    user.value = {
      id: data.userId,
      email: data.email,
      username: data.username,
      role: data.role
    }
    localStorage.setItem('token', data.token)
    localStorage.setItem('user', JSON.stringify(user.value))
    return data
  }

  async function register(form) {
    const { data } = await authApi.register(form)
    token.value = data.token
    user.value = {
      id: data.userId,
      email: data.email,
      username: data.username,
      role: data.role
    }
    localStorage.setItem('token', data.token)
    localStorage.setItem('user', JSON.stringify(user.value))
    return data
  }

  function logout() {
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  async function fetchUser() {
    try {
      const { data } = await userApi.getMe()
      user.value = data
      localStorage.setItem('user', JSON.stringify(data))
    } catch {
      logout()
    }
  }

  return { user, token, isLoggedIn, login, register, logout, fetchUser }
})
