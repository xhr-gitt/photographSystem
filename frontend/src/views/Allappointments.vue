<template>
    <div class="appointment-management">
        <h2>预约管理</h2>
        <router-link :to="{ name: 'main' }" class="text-xl font-semibold text-gray-900">
        回到主页
    </router-link>

        <!-- 管理员专属功能区 -->
        <div v-if="authStore.isAdmin" class="admin-controls">
            <div class="pagination-controls">
                <button @click="prevPage" :disabled="currentPage === 1">上一页</button>
                <span>第 {{ currentPage }} 页 / 共 {{ totalPages }} 页</span>
                <button @click="nextPage" :disabled="currentPage === totalPages">下一页</button>
                <select v-model="pageSize" @change="fetchAppointments">
                    <option value="5">每页5条</option>
                    <option value="10">每页10条</option>
                    <option value="20">每页20条</option>
                </select>
                <button @click="showCreateDialog = true" class="create-btn">新建预约</button>
            </div>
        </div>

        <!-- 预约列表表格 -->
        <table class="appointment-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>摄影师ID</th>
                <th>用户ID</th>
                <th>预约时间</th>
                <th v-if="authStore.isAdmin">操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="appointment in appointments" :key="appointment.id">
                <td>{{ appointment.id }}</td>
                <td>{{ appointment.staffId }}</td>
                <td>{{ appointment.userId }}</td>
                <td>{{ formatDateTime(appointment.appointmentTime) }}</td>
                <td v-if="authStore.isAdmin" class="actions">
                    <button @click="prepareEdit(appointment)">编辑</button>
                    <button @click="confirmDelete(appointment.id)" class="delete-btn">删除</button>
                </td>
            </tr>
            </tbody>
        </table>

        <!-- 新建预约对话框 -->
        <div v-if="showCreateDialog" class="dialog-overlay">
            <div class="dialog">
                <h3>新建预约</h3>
                <form @submit.prevent="createAppointment">
                    <div class="form-group">
                        <label>摄影师ID:</label>
                        <input v-model="newAppointment.staffId" type="number" required>
                    </div>
                    <div class="form-group">
                        <label>用户ID:</label>
                        <input v-model="newAppointment.userId" type="number" required>
                    </div>
                    <div class="form-group">
                        <label>预约时间:</label>
                        <input v-model="newAppointment.appointmentTime" type="datetime-local" required>
                    </div>
                    <div class="dialog-buttons">
                        <button type="button" @click="showCreateDialog = false">取消</button>
                        <button type="submit">创建</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- 编辑预约对话框 -->
        <div v-if="showEditDialog" class="dialog-overlay">
            <div class="dialog">
                <h3>编辑预约</h3>
                <form @submit.prevent="updateAppointment">
                    <div class="form-group">
                        <label>预约ID:</label>
                        <span>{{ editForm.id }}</span>
                    </div>
                    <div class="form-group">
                        <label>摄影师ID:</label>
                        <input v-model="editForm.staffId" type="number" required>
                    </div>
                    <div class="form-group">
                        <label>用户ID:</label>
                        <input v-model="editForm.userId" type="number" required>
                    </div>
                    <div class="form-group">
                        <label>预约时间:</label>
                        <input v-model="editForm.appointmentTime" type="datetime-local" required>
                    </div>
                    <div class="dialog-buttons">
                        <button type="button" @click="showEditDialog = false">取消</button>
                        <button type="submit">保存</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<script setup>
    import { ref, onMounted, computed } from 'vue'
    import { useAuthStore } from '@/stores/auth'
    import api from '@/api/auth'
    import { useRouter } from 'vue-router'

    const authStore = useAuthStore()
    const router = useRouter()

    // 数据状态
    const appointments = ref([])
    const currentPage = ref(1)
    const pageSize = ref(10)
    const totalPages = ref(1)
    const showCreateDialog = ref(false)
    const showEditDialog = ref(false)

    // 表单数据
    const newAppointment = ref({
        staffId: null,
        userId: null,
        appointmentTime: ''
    })

    const editForm = ref({
        id: null,
        staffId: null,
        userId: null,
        appointmentTime: ''
    })

    // 获取预约列表
    const fetchAppointments = async () => {
        try {
            let response
            if (authStore.isAdmin) {
                // 管理员获取分页数据
                response = await api.get('/api/appointment/page', {
                    params: {
                        pageNum: currentPage.value,
                        pageSize: pageSize.value
                    }
                })
                appointments.value = response.data.content
                totalPages.value = response.data.totalPages
            } else {
                // 普通用户获取自己的预约
                response = await api.get('/api/appointment')
                appointments.value = response.data
            }
        } catch (error) {
            console.error('获取预约列表失败:', error)
            alert('获取预约列表失败')
        }
    }

    // 创建预约
    const createAppointment = async () => {
        try {
            const data = {
                ...newAppointment.value,
                appointmentTime: new Date(newAppointment.value.appointmentTime).toISOString()
            }
            await api.post('/api/appointment', data)
            alert('创建成功')
            showCreateDialog.value = false
            await fetchAppointments()
        } catch (error) {
            console.error('创建预约失败:', error)
            alert(`创建失败: ${error.response?.data?.message || error.message}`)
        }
    }

    // 准备编辑
    const prepareEdit = (appointment) => {
        editForm.value = {
            id: appointment.id,
            staffId: appointment.staffId,
            userId: appointment.userId,
            appointmentTime: formatDateTimeForInput(appointment.appointmentTime)
        }
        showEditDialog.value = true
    }

    // 更新预约
    const updateAppointment = async () => {
        try {
            const data = {
                staffId: editForm.value.staffId,
                userId: editForm.value.userId,
                appointmentTime: new Date(editForm.value.appointmentTime).toISOString()
            }
            await api.put(`/api/appointment/${editForm.value.id}`, data)
            alert('更新成功')
            showEditDialog.value = false
            await fetchAppointments()
        } catch (error) {
            console.error('更新预约失败:', error)
            alert(`更新失败: ${error.response?.data?.message || error.message}`)
        }
    }

    // 删除预约
    const confirmDelete = async (id) => {
        if (confirm('确定要删除此预约吗？')) {
            try {
                await api.delete(`/api/appointment/${id}`)
                alert('删除成功')
                await fetchAppointments()
            } catch (error) {
                console.error('删除预约失败:', error)
                alert(`删除失败: ${error.response?.data?.message || error.message}`)
            }
        }
    }

    // 分页控制
    const prevPage = () => {
        if (currentPage.value > 1) {
            currentPage.value--
            fetchAppointments()
        }
    }

    const nextPage = () => {
        if (currentPage.value < totalPages.value) {
            currentPage.value++
            fetchAppointments()
        }
    }

    // 辅助函数
    const formatDateTime = (dateTime) => {
        return new Date(dateTime).toLocaleString('zh-CN')
    }

    const formatDateTimeForInput = (dateTime) => {
        const date = new Date(dateTime)
        return date.toISOString().slice(0, 16)
    }

    onMounted(() => {
        fetchAppointments()
    })
</script>

<style scoped>
    .appointment-management {
        padding: 20px;
        max-width: 1200px;
        margin: 0 auto;
    }

    .admin-controls {
        margin-bottom: 20px;
    }

    .pagination-controls {
        display: flex;
        align-items: center;
        gap: 10px;
        margin-bottom: 15px;
    }

    .create-btn {
        background-color: #4CAF50;
        color: white;
        padding: 8px 16px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    .appointment-table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
    }

    .appointment-table th, .appointment-table td {
        border: 1px solid #ddd;
        padding: 10px;
        text-align: left;
    }

    .appointment-table th {
        background-color: #f2f2f2;
        font-weight: bold;
    }

    .actions {
        display: flex;
        gap: 5px;
    }

    .delete-btn {
        background-color: #ff4444;
        color: white;
    }

    /* 对话框样式 */
    .dialog-overlay {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: rgba(0, 0, 0, 0.5);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 1000;
    }

    .dialog {
        background-color: white;
        padding: 25px;
        border-radius: 8px;
        width: 500px;
        max-width: 90%;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    }

    .form-group {
        margin-bottom: 15px;
    }

    .form-group label {
        display: block;
        margin-bottom: 5px;
        font-weight: bold;
    }

    .form-group input {
        width: 100%;
        padding: 8px;
        box-sizing: border-box;
        border: 1px solid #ddd;
        border-radius: 4px;
    }

    .dialog-buttons {
        display: flex;
        justify-content: flex-end;
        gap: 10px;
        margin-top: 20px;
    }

    .dialog-buttons button {
        padding: 8px 16px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    .dialog-buttons button[type="button"] {
        background-color: #f0f0f0;
    }

    .dialog-buttons button[type="submit"] {
        background-color: #4CAF50;
        color: white;
    }
</style>