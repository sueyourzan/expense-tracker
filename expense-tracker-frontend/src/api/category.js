import api from './index'

export const categoryApi = {
  list(type) {
    return api.get('/categories', { params: type ? { type } : {} })
  },
  create(data) {
    return api.post('/categories', data)
  },
  remove(id) {
    return api.delete(`/categories/${id}`)
  }
}
