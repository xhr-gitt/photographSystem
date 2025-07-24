<script setup>
    import { ref, onMounted } from 'vue'
    import { useAuthStore } from '@/stores/auth.js'
    import api from '@/api/auth'

    const authStore = useAuthStore()
    const appointments = ref([])
    const loading = ref(true)
    const error = ref(null)

    // 获取当前用户的预约信息
    const fetchUserAppointments = async () => {
        try {
            loading.value = true
            error.value = null

            // 确保authStore中有当前用户ID
            if (!authStore.userId) {
                throw new Error('无法获取用户ID，请先登录')
            }

            // 使用用户ID作为参数调用接口
            const response = await api.get(`/api/appointment/user/${authStore.userId}`)

            // 处理响应数据
            if (response.data) {
                // 如果返回的是单个预约对象，转为数组
                appointments.value = Array.isArray(response.data)
                    ? response.data
                    : [response.data]
            } else {
                throw new Error('返回数据格式不正确')
            }

        } catch (err) {
            console.error('获取预约信息失败:', err)
            error.value = err.message || '获取预约信息失败，请稍后再试'
        } finally {
            loading.value = false
        }
    }

    // 格式化日期时间
    const formatDateTime = (dateString) => {
        if (!dateString) return '-'
        const date = new Date(dateString)
        return date.toLocaleString()
    }

    onMounted(() => {
        // 确保authStore状态是最新的
        authStore.hydrate()
        fetchUserAppointments()
    })
</script>

<template>
    <div class="user-appointments">
        <h2>我的预约</h2>
        <div v-if="loading" class="loading">加载中...</div>
        <div v-else>
            <div v-if="error" class="error">{{ error }}</div>
            <div v-if="appointments.length === 0" class="no-appointments">
                您还没有任何预约记录
            </div>
            <table v-else class="appointments-table">
                <thead>
                <tr>
                    <th>摄影师ID</th>
                    <th>预约时间</th>
                    <th>状态</th>
                    <th>创建时间</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="appointment in appointments" :key="appointment.id">
                    <td>{{appointment.staffId}}</td>
                    <td>{{ formatDateTime(appointment.appointmentTime) }}</td>
                    <td>{{ appointment.status || '成功' }}</td>
                    <td>{{ formatDateTime(appointment.createdTime) }}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<style scoped>
    .user-appointments {
        padding: 20px;
        max-width: 1000px;
        margin: 0 auto;
    }

    .loading, .error, .no-appointments {
        text-align: center;
        padding: 20px;
        font-size: 16px;
    }

    .error {
        color: #ff4444;
    }

    .appointments-table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
    }

    .appointments-table th, .appointments-table td {
        border: 1px solid #ddd;
        padding: 12px 15px;
        text-align: left;
    }

    .appointments-table th {
        background-color: #f2f2f2;
        font-weight: bold;
    }

    .appointments-table tr:nth-child(even) {
        background-color: #f9f9f9;
    }

    .appointments-table tr:hover {
        background-color: #f1f1f1;
    }
</style>