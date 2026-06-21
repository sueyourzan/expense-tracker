import api from './index'

export const transactionApi = {
  list(params) {
    return api.get('/transactions', { params })
  },
  get(id) {
    return api.get(`/transactions/${id}`)
  },
  create(data) {
    return api.post('/transactions', data)
  },
  remove(id) {
    return api.delete(`/transactions/${id}`)
  },
  stats(params) {
    return api.get('/transactions/stats', { params })
  }
}
