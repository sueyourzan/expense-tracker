import axios from 'axios'
import { ElMessage } from 'element-plus'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const message = error.response?.data?.message || '请求失败'
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    } else if (error.response?.status === 400 && error.response?.data) {
      const data = error.response.data
      if (typeof data === 'object' && !data.message) {
        const msgs = Object.values(data).join('；')
        ElMessage.error(msgs)
        return Promise.reject(error)
      }
    }
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

export default api
