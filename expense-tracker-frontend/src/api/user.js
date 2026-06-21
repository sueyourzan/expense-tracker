import api from './index'

export const userApi = {
  getMe() {
    return api.get('/users/me')
  },
  updateMe(data) {
    return api.put('/users/me', data)
  }
}
