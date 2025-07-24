<template>
    <div class="borrow-management">

        <!-- 借用记录列表 -->
        <div class="record-list">
            <h3>设备借用记录</h3>
            <p><router-link :to="{ name: 'equipment-management' }" class="text-xl font-semibold text-gray-900">
                设备管理页面
            </router-link></p>
            <div class="filter-controls">
                <button @click="fetchActiveRecords">未归还记录</button>
                <button @click="fetchAllRecords">全部记录</button>
                <div class="search-group">
                    <input v-model.number="searchEquipmentId" placeholder="设备ID" type="number">
                    <button @click="fetchByEquipment">按设备查询</button>
                </div>
            </div>

            <table>
                <thead>
                <tr>
                    <th>设备名称</th>
                    <th>借用人</th>
                    <th>借用时间</th>
                    <th>归还时间</th>
                    <th>借用目的</th>
                    <th>状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="record in records" :key="record.id">
                    <td>{{ record.equipment?.name }}</td>
                    <td>{{ record.staff?.realName }}</td>
                    <td>{{ formatDate(record.borrowTime) }}</td>
                    <td>{{ record.returnTime ? formatDate(record.returnTime) : '-' }}</td>
                    <td>{{ record.purpose }}</td>
                    <td :class="'status-' + record.status.toLowerCase()">
                        {{ getStatusText(record.status) }}
                    </td>
                    <td>
                        <button
                                v-if="record.status === 'ACTIVE'"
                                @click="returnEquipment(record.id)"
                                class="return-btn"
                        >
                            归还
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script setup>
    import { ref, onMounted, watch } from 'vue'
    import api from '@/api/auth'

    const records = ref([])
    const borrowForm = ref({
        equipmentId: null,
        purpose: ''
    })
    const searchEquipmentId = ref(null)
    const isBorrowed = ref(false)

    // 获取所有记录
    const fetchAllRecords = async () => {
        try {
            // 使用staffId=0表示获取所有记录
            const response = await api.get('/api/equipment-borrow/staff/0')
            records.value = response.data
        } catch (error) {
            console.error('获取全部记录失败:', error)
            alert('获取全部记录失败')
        }
    }

    // 获取未归还记录
    const fetchActiveRecords = async () => {
        try {
            const response = await api.get('/api/equipment-borrow/active')
            records.value = response.data
        } catch (error) {
            console.error('获取未归还记录失败:', error)
            alert('获取未归还记录失败')
        }
    }

    // 按设备ID查询记录
    const fetchByEquipment = async () => {
        if (!searchEquipmentId.value) return
        try {
            const response = await api.get(`/api/equipment-borrow/equipment/${searchEquipmentId.value}`)
            records.value = response.data
        } catch (error) {
            console.error('按设备查询失败:', error)
            alert('按设备查询失败')
        }
    }

    // 检查设备是否被借用
    const checkBorrowStatus = async () => {
        if (!borrowForm.value.equipmentId) return
        try {
            const response = await api.get(`/api/equipment-borrow/is-borrowed/${borrowForm.value.equipmentId}`)
            isBorrowed.value = response.data
        } catch (error) {
            console.error('检查借用状态失败:', error)
        }
    }


    // 归还设备
    const returnEquipment = async (recordId) => {
        if (!confirm('确定要归还该设备吗？')) return
        try {
            await api.post(`/api/equipment-borrow/return/${recordId}`)
            alert('归还成功')
            fetchActiveRecords()
        } catch (error) {
            console.error('归还失败:', error)
            alert('归还失败: ' + (error.response?.data?.message || error.message))
        }
    }

    // 辅助函数
    const formatDate = (dateString) => {
        return dateString ? new Date(dateString).toLocaleString() : ''
    }

    const getStatusText = (status) => {
        const statusMap = {
            'ACTIVE': '借用中',
            'RETURNED': '已归还',
            'OVERDUE': '已超期'
        }
        return statusMap[status] || status
    }

    // 监听设备ID变化
    watch(() => borrowForm.value.equipmentId, checkBorrowStatus)

    // 初始化加载未归还记录
    onMounted(fetchActiveRecords)
</script>

<style scoped>
    .borrow-management {
        padding: 20px;
        max-width: 1200px;
        margin: 0 auto;
    }

    .borrow-form, .record-list {
        background: #f5f5f5;
        padding: 20px;
        border-radius: 8px;
        margin-bottom: 20px;
    }

    .form-group {
        margin-bottom: 15px;
    }

    .form-group label {
        display: block;
        margin-bottom: 5px;
        font-weight: bold;
    }

    .form-group input,
    .form-group textarea {
        width: 100%;
        padding: 8px;
        box-sizing: border-box;
    }

    .error-text {
        color: #f44336;
        font-size: 0.8em;
    }

    .filter-controls {
        display: flex;
        gap: 10px;
        margin-bottom: 15px;
        align-items: center;
    }

    .search-group {
        display: flex;
        gap: 5px;
        margin-left: auto;
    }

    .search-group input {
        width: 100px;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 15px;
    }

    th, td {
        border: 1px solid #ddd;
        padding: 8px;
        text-align: left;
    }

    th {
        background-color: #f2f2f2;
    }

    .status-active {
        color: #ff9800;
        font-weight: bold;
    }

    .status-returned {
        color: #4caf50;
        font-weight: bold;
    }

    .status-overdue {
        color: #f44336;
        font-weight: bold;
    }

    button {
        padding: 8px 12px;
        cursor: pointer;
    }

    .return-btn {
        background-color: #4caf50;
        color: white;
    }

    button:disabled {
        background-color: #cccccc;
        cursor: not-allowed;
    }
</style>