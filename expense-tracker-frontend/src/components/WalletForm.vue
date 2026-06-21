<template>
  <el-dialog
    :model-value="visible"
    :title="isEdit ? '编辑钱包' : '新建钱包'"
    width="500px"
    @update:model-value="$emit('update:visible', $event)"
    @close="resetForm"
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="钱包名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入钱包名称" />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input v-model="form.description" type="textarea" placeholder="可选" />
      </el-form-item>
      <el-form-item label="初始余额" prop="initialBalance">
        <el-input-number
          v-model="form.initialBalance"
          :precision="2"
          :min="0"
          style="width: 100%"
          placeholder="0.00"
        />
      </el-form-item>
      <el-form-item label="货币" prop="currencyCode">
        <el-select v-model="form.currencyCode" style="width: 100%">
          <el-option label="人民币 (CNY)" value="CNY" />
          <el-option label="美元 (USD)" value="USD" />
          <el-option label="欧元 (EUR)" value="EUR" />
          <el-option label="日元 (JPY)" value="JPY" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="$emit('update:visible', false)">取消</el-button>
      <el-button type="primary" :loading="loading" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  visible: Boolean,
  wallet: { type: Object, default: null }
})

const emit = defineEmits(['update:visible', 'submit'])

const isEdit = ref(false)
const loading = ref(false)
const formRef = ref(null)

const form = reactive({
  name: '',
  description: '',
  initialBalance: 0,
  currencyCode: 'CNY'
})

const rules = {
  name: [{ required: true, message: '请输入钱包名称', trigger: 'blur' }],
  initialBalance: [{ required: true, message: '请输入初始余额', trigger: 'blur' }],
  currencyCode: [{ required: true, message: '请选择货币', trigger: 'change' }]
}

watch(() => props.visible, (val) => {
  if (val && props.wallet) {
    isEdit.value = true
    form.name = props.wallet.name
    form.description = props.wallet.description || ''
    form.initialBalance = props.wallet.balance
    form.currencyCode = props.wallet.currencyCode || 'CNY'
  } else if (val) {
    isEdit.value = false
    resetForm()
  }
})

function resetForm() {
  form.name = ''
  form.description = ''
  form.initialBalance = 0
  form.currencyCode = 'CNY'
  isEdit.value = false
  formRef.value?.resetFields()
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    emit('submit', { ...form })
    emit('update:visible', false)
    ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
    resetForm()
  } finally {
    loading.value = false
  }
}
</script>
