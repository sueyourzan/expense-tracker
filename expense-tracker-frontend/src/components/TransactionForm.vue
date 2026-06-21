<template>
  <el-dialog
    :model-value="visible"
    title="记一笔"
    width="500px"
    @update:model-value="$emit('update:visible', $event)"
    @close="resetForm"
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="类型" prop="type">
        <el-radio-group v-model="form.type">
          <el-radio-button value="EXPENSE">支出</el-radio-button>
          <el-radio-button value="INCOME">收入</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="金额" prop="amount">
        <el-input-number
          v-model="form.amount"
          :precision="2"
          :min="0.01"
          style="width: 100%"
          placeholder="0.00"
        />
      </el-form-item>
      <el-form-item label="分类" prop="category">
        <el-select v-model="form.category" style="width: 100%" filterable allow-create>
          <el-option
            v-for="cat in categories"
            :key="cat.id"
            :label="cat.name"
            :value="cat.name"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="钱包" prop="walletId">
        <el-select v-model="form.walletId" style="width: 100%">
          <el-option
            v-for="w in wallets"
            :key="w.id"
            :label="`${w.name} (${w.currencyCode} ${w.balance})`"
            :value="w.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="日期" prop="transactionDate">
        <el-date-picker
          v-model="form.transactionDate"
          type="datetime"
          placeholder="选择日期时间"
          style="width: 100%"
          format="YYYY-MM-DD HH:mm:ss"
          value-format="YYYY-MM-DDTHH:mm:ss"
        />
      </el-form-item>
      <el-form-item label="备注" prop="description">
        <el-input v-model="form.description" type="textarea" placeholder="可选" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="$emit('update:visible', false)">取消</el-button>
      <el-button type="primary" :loading="loading" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { categoryApi } from '@/api/category'

const props = defineProps({
  visible: Boolean,
  wallets: { type: Array, default: () => [] }
})

const emit = defineEmits(['update:visible', 'submit'])

const loading = ref(false)
const formRef = ref(null)
const categories = ref([])

const form = reactive({
  type: 'EXPENSE',
  amount: null,
  category: '',
  walletId: null,
  transactionDate: '',
  description: ''
})

const rules = {
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
  category: [{ required: true, message: '请选择或输入分类', trigger: 'change' }],
  walletId: [{ required: true, message: '请选择钱包', trigger: 'change' }]
}

watch(() => props.visible, async (val) => {
  if (val) {
    form.transactionDate = new Date().toISOString().slice(0, 19)
    form.walletId = props.wallets[0]?.id || null
    try {
      const { data } = await categoryApi.list(form.type)
      categories.value = data
    } catch {}
  }
})

watch(() => form.type, async (newType) => {
  try {
    const { data } = await categoryApi.list(newType)
    categories.value = data
  } catch {}
})

function resetForm() {
  form.type = 'EXPENSE'
  form.amount = null
  form.category = ''
  form.walletId = props.wallets[0]?.id || null
  form.transactionDate = ''
  form.description = ''
  formRef.value?.resetFields()
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    emit('submit', { ...form })
    emit('update:visible', false)
    ElMessage.success('记录成功')
    resetForm()
  } finally {
    loading.value = false
  }
}
</script>
