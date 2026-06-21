import api from './index'

export const walletApi = {
  list() {
    return api.get('/wallets')
  },
  get(id) {
    return api.get(`/wallets/${id}`)
  },
  create(data) {
    return api.post('/wallets', data)
  },
  update(id, data) {
    return api.put(`/wallets/${id}`, data)
  },
  remove(id) {
    return api.delete(`/wallets/${id}`)
  }
}
