<template>
  <div class="profile-page">
    <el-card class="profile-card">
      <template #header>
        <h3>个人中心</h3>
      </template>
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        style="max-width: 500px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input :model-value="form.email" disabled />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="角色">
          <el-tag>{{ form.role === 'ADMIN' ? '管理员' : '普通用户' }}</el-tag>
        </el-form-item>
        <el-form-item label="新密码" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            show-password
            placeholder="留空则不修改密码"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleUpdate">
            保存修改
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { userApi } from '@/api/user'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

const auth = useAuthStore()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  email: '',
  phone: '',
  password: '',
  role: ''
})

const rules = {
  username: [{ required: true, message: '用户名不能为空', trigger: 'blur' }]
}

onMounted(async () => {
  try {
    const { data } = await userApi.getMe()
    form.username = data.username
    form.email = data.email
    form.phone = data.phone || ''
    form.role = data.role
  } catch {}
})

async function handleUpdate() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const payload = {
      username: form.username,
      phone: form.phone || null
    }
    if (form.password) {
      payload.password = form.password
    }
    await userApi.updateMe(payload)
    ElMessage.success('更新成功')
    form.password = ''
    await auth.fetchUser()
  } catch {} finally {
    loading.value = false
  }
}
</script>

<style scoped>
.profile-card {
  max-width: 600px;
}

.profile-card h3 {
  font-size: 18px;
}
</style>
