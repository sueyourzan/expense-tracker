<template>
  <div class="wallets-page">
    <div class="page-header">
      <h3>钱包管理</h3>
      <el-button type="primary" @click="openCreate">
        <el-icon><Plus /></el-icon> 新建钱包
      </el-button>
    </div>

    <el-row :gutter="20">
      <el-col v-for="wallet in wallets" :key="wallet.id" :span="8">
        <el-card class="wallet-card">
          <div class="wallet-header">
            <span class="wallet-name">{{ wallet.name }}</span>
            <span class="wallet-currency">{{ wallet.currencyCode }}</span>
          </div>
          <div class="wallet-balance">¥{{ wallet.balance?.toFixed(2) || '0.00' }}</div>
          <div class="wallet-desc" v-if="wallet.description">{{ wallet.description }}</div>
          <div class="wallet-actions">
            <el-button size="small" @click="openEdit(wallet)">编辑</el-button>
            <el-popconfirm
              title="删除钱包将同时删除关联的交易记录，确定删除？"
              @confirm="handleDelete(wallet.id)"
            >
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-empty v-if="!wallets.length" description="还没有钱包，点击上方按钮创建" />

    <WalletForm
      :visible="dialogVisible"
      :wallet="editingWallet"
      @update:visible="dialogVisible = $event"
      @submit="handleSubmit"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { walletApi } from '@/api/wallet'
import { ElMessage } from 'element-plus'
import WalletForm from '@/components/WalletForm.vue'

const wallets = ref([])
const dialogVisible = ref(false)
const editingWallet = ref(null)

onMounted(fetchWallets)

async function fetchWallets() {
  try {
    const { data } = await walletApi.list()
    wallets.value = data
  } catch {}
}

function openCreate() {
  editingWallet.value = null
  dialogVisible.value = true
}

function openEdit(wallet) {
  editingWallet.value = wallet
  dialogVisible.value = true
}

async function handleSubmit(form) {
  if (editingWallet.value) {
    await walletApi.update(editingWallet.value.id, form)
  } else {
    await walletApi.create(form)
  }
  await fetchWallets()
}

async function handleDelete(id) {
  try {
    await walletApi.remove(id)
    ElMessage.success('删除成功')
    await fetchWallets()
  } catch {}
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

.wallet-card {
  margin-bottom: 20px;
}

.wallet-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.wallet-name {
  font-size: 16px;
  font-weight: bold;
}

.wallet-currency {
  font-size: 12px;
  color: #909399;
  background: #f0f2f5;
  padding: 2px 8px;
  border-radius: 4px;
}

.wallet-balance {
  font-size: 26px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.wallet-desc {
  font-size: 13px;
  color: #909399;
  margin-bottom: 12px;
}

.wallet-actions {
  display: flex;
  gap: 10px;
}
</style>
