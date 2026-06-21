<template>
  <div class="transactions-page">
    <div class="page-header">
      <h3>交易记录</h3>
      <el-button type="primary" @click="openCreate">
        <el-icon><Plus /></el-icon> 记一笔
      </el-button>
    </div>

    <!-- 筛选 -->
    <el-card class="filter-card">
      <el-form inline>
        <el-form-item label="钱包">
          <el-select v-model="filters.walletId" clearable placeholder="全部" @change="fetchTransactions">
            <el-option v-for="w in wallets" :key="w.id" :label="w.name" :value="w.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期范围">
          <el-date-picker
            v-model="filters.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始"
            end-placeholder="结束"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            @change="fetchTransactions"
          />
        </el-form-item>
        <el-form-item>
          <el-button @click="clearFilters">清除筛选</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 交易列表 -->
    <el-card>
      <el-table :data="transactions" stripe style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column label="类型" width="80">
          <template #default="{ row }">
            <el-tag :type="row.type === 'INCOME' ? 'success' : 'danger'" size="small">
              {{ row.type === 'INCOME' ? '收入' : '支出' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="金额" width="140">
          <template #default="{ row }">
            <span :style="{ color: row.type === 'INCOME' ? '#67c23a' : '#f56c6c', fontWeight: 'bold' }">
              {{ row.type === 'INCOME' ? '+' : '-' }}¥{{ row.amount?.toFixed(2) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="100" />
        <el-table-column label="钱包" width="120">
          <template #default="{ row }">
            {{ row.wallet?.name }}
          </template>
        </el-table-column>
        <el-table-column prop="description" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column label="日期" width="170">
          <template #default="{ row }">
            {{ formatDate(row.transactionDate) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80" fixed="right">
          <template #default="{ row }">
            <el-popconfirm title="删除后将恢复钱包余额，确定删除？" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button size="small" type="danger" text>删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <TransactionForm
      :visible="dialogVisible"
      :wallets="wallets"
      @update:visible="dialogVisible = $event"
      @submit="handleCreate"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { transactionApi } from '@/api/transaction'
import { walletApi } from '@/api/wallet'
import { ElMessage } from 'element-plus'
import TransactionForm from '@/components/TransactionForm.vue'

const transactions = ref([])
const wallets = ref([])
const loading = ref(false)
const dialogVisible = ref(false)

const filters = reactive({
  walletId: null,
  dateRange: null
})

onMounted(async () => {
  await fetchWallets()
  await fetchTransactions()
})

async function fetchWallets() {
  try {
    const { data } = await walletApi.list()
    wallets.value = data
  } catch {}
}

async function fetchTransactions() {
  loading.value = true
  try {
    const params = {}
    if (filters.walletId) params.walletId = filters.walletId
    if (filters.dateRange?.length === 2) {
      params.startDate = filters.dateRange[0]
      params.endDate = filters.dateRange[1]
    }
    const { data } = await transactionApi.list(params)
    transactions.value = data
  } catch {} finally {
    loading.value = false
  }
}

function clearFilters() {
  filters.walletId = null
  filters.dateRange = null
  fetchTransactions()
}

function openCreate() {
  if (!wallets.value.length) {
    ElMessage.warning('请先创建钱包')
    return
  }
  dialogVisible.value = true
}

async function handleCreate(form) {
  try {
    await transactionApi.create(form)
    ElMessage.success('记录成功')
    await fetchTransactions()
  } catch {}
}

async function handleDelete(id) {
  try {
    await transactionApi.remove(id)
    ElMessage.success('删除成功')
    await fetchTransactions()
  } catch {}
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  const pad = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`
}
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h3 {
  font-size: 18px;
}

.filter-card {
  margin-bottom: 20px;
}
</style>
