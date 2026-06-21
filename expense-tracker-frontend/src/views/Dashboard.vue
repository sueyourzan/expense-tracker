<template>
  <div class="dashboard">
    <!-- 日期范围选择 -->
    <el-card class="filter-card">
      <el-form inline>
        <el-form-item label="日期范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            @change="fetchStats"
          />
        </el-form-item>
        <el-form-item>
          <el-button @click="setQuickRange('month')">本月</el-button>
          <el-button @click="setQuickRange('year')">今年</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="8">
        <el-card class="stat-card income-card">
          <div class="stat-label">总收入</div>
          <div class="stat-value income">¥{{ stats.totalIncome?.toFixed(2) || '0.00' }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card expense-card">
          <div class="stat-label">总支出</div>
          <div class="stat-value expense">¥{{ stats.totalExpense?.toFixed(2) || '0.00' }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card balance-card">
          <div class="stat-label">结余</div>
          <div class="stat-value" :class="stats.balance >= 0 ? 'income' : 'expense'">
            ¥{{ stats.balance?.toFixed(2) || '0.00' }}
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分类统计 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header>收入分类</template>
          <div v-if="stats.incomeByCategory?.length">
            <div
              v-for="item in stats.incomeByCategory"
              :key="item.category"
              class="category-item"
            >
              <span>{{ item.category }}</span>
              <span class="income">¥{{ (typeof item.total === 'number' ? item.total : 0).toFixed(2) }}</span>
            </div>
          </div>
          <el-empty v-else description="暂无数据" :image-size="80" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>支出分类</template>
          <div v-if="stats.expenseByCategory?.length">
            <div
              v-for="item in stats.expenseByCategory"
              :key="item.category"
              class="category-item"
            >
              <span>{{ item.category }}</span>
              <span class="expense">¥{{ (typeof item.total === 'number' ? item.total : 0).toFixed(2) }}</span>
            </div>
          </div>
          <el-empty v-else description="暂无数据" :image-size="80" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { transactionApi } from '@/api/transaction'

const dateRange = ref([])
const stats = reactive({
  totalIncome: 0,
  totalExpense: 0,
  balance: 0,
  incomeByCategory: [],
  expenseByCategory: []
})

onMounted(() => {
  setQuickRange('month')
})

function setQuickRange(range) {
  const now = new Date()
  const start = new Date()
  if (range === 'month') {
    start.setDate(1)
  } else if (range === 'year') {
    start.setMonth(0, 1)
  }
  dateRange.value = [
    start.toISOString().slice(0, 10),
    now.toISOString().slice(0, 10)
  ]
  fetchStats()
}

async function fetchStats() {
  if (!dateRange.value || dateRange.value.length !== 2) return
  try {
    const { data } = await transactionApi.stats({
      startDate: dateRange.value[0],
      endDate: dateRange.value[1]
    })
    Object.assign(stats, data)
  } catch {}
}
</script>

<style scoped>
.filter-card {
  margin-bottom: 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
}

.income {
  color: #67c23a;
}

.expense {
  color: #f56c6c;
}

.category-item {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #ebeef5;
}

.category-item:last-child {
  border-bottom: none;
}
</style>
