<script setup>
    import { useAuthStore } from '@/stores/auth.js'
    import { ref, onMounted } from 'vue'
    import { useRouter } from 'vue-router'
    import api from '@/api/auth'

    const authStore = useAuthStore()
    const router = useRouter()

    const staffList = ref([])
    const showCreateDialog = ref(false)
    const showEditDialog = ref(false)
    const currentEditStaff = ref(null)
    const newStaff = ref({
        username: '',
        realName: '',
        role: 'PHOTOGRAPHER',
        staffInformation: ''
    })
    const editForm = ref({
        username: '',
        realName: '',
        role: '',
        staffInformation: ''
    })

    // 获取员工列表
    const fetchStaffList = async () => {
        try {
            const response = await api.get('/api/staff/sync-and-list')
            staffList.value = response.data
        } catch (error) {
            console.error('获取员工列表失败:', error)
            alert('获取员工列表失败')
        }
    }

    // 创建员工
    const createStaff = async () => {
                try {
                    await api.post('/api/staff', newStaff.value)
                    alert('创建成功')
            showCreateDialog.value = false
            newStaff.value = { username: '', realName: '', role: 'PHOTOGRAPHER', staffInformation: '' }
            await fetchStaffList()
        } catch (error) {
            console.error('创建员工失败:', error)
            alert('创建员工失败: ' + (error.response?.data?.message || error.message))
        }
    }

    // 查看员工详情
    const viewStaff = (username) => {
        router.push(`/staff/${username}`)
    }

    // 准备编辑员工
    const prepareEditStaff = (staff) => {
        currentEditStaff.value = staff
        editForm.value = {
            username: staff.username,
            realName: staff.realName || '',
            role: staff.role,
            staffInformation: staff.staffInformation || ''
        }
        showEditDialog.value = true
    }

    // 更新员工信息
    const updateStaff = async () => {
        try {
            await api.put(`/api/staff/${editForm.value.username}`, {
                role: editForm.value.role,
                realName: editForm.value.realName,
                staffInformation: editForm.value.staffInformation
            })
            alert('更新成功')
            showEditDialog.value = false
            await fetchStaffList()
        } catch (error) {
            console.error('更新员工信息失败:', error)
            alert('更新员工信息失败: ' + (error.response?.data?.message || error.message))
        }
    }

    // 删除员工
    const confirmDelete = (staff) => {
        if (confirm(`确定要删除员工 ${staff.username} 吗？`)) {
            deleteStaff(staff.username)
        }
    }

    const deleteStaff = async (username) => {
        try {
            await api.delete(`/api/staff/${username}`)
            alert('删除成功')
            await fetchStaffList()
        } catch (error) {
            console.error('删除员工失败:', error)
            alert('删除员工失败: ' + (error.response?.data?.message || error.message))
        }
    }

    onMounted(() => {
        fetchStaffList()
    })
</script>

<template>
    <div class="staff-management">
        <h2>员工管理</h2>

        <div v-if="authStore.isAdmin" class="action-buttons">
            <button @click="showCreateDialog = true">新增员工</button>
            <router-link to="/user-control" class="button-link">返回用户管理</router-link>
        </div>

        <table class="staff-table">
            <thead>
            <tr>
                <th>用户名</th>
                <th>真实姓名</th>
                <th>角色</th>
                <th>员工信息</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="staff in staffList" :key="staff.username">
                <td>{{ staff.username }}</td>
                <td>{{ staff.realName || '-' }}</td>
                <td>{{ staff.role }}</td>
                <td>{{ staff.staffInformation || '-' }}</td>
                <td class="actions">
                    <button @click="viewStaff(staff.username)">查看</button>
                    <button @click="prepareEditStaff(staff)">编辑</button>
                    <button @click="confirmDelete(staff)" class="delete-btn">删除</button>
                </td>
            </tr>
            </tbody>
        </table>

        <!-- 创建员工对话框 -->
        <div v-if="showCreateDialog" class="dialog-overlay">
            <div class="dialog">
                <h3>创建新员工</h3>
                <form @submit.prevent="createStaff">
                    <div class="form-group">
                        <label>用户名:</label>
                        <input v-model="newStaff.username" required>
                    </div>
                    <div class="form-group">
                        <label>真实姓名:</label>
                        <input v-model="newStaff.realName" required>
                    </div>
                    <div class="form-group">
                        <label>角色:</label>
                        <select v-model="newStaff.role" required>
                            <option value="PHOTOGRAPHER">摄影师</option>
                            <option value="Makeup Artist">化妆师</option>
                            <option value="Manger">经理</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>员工信息:</label>
                        <textarea v-model="newStaff.staffInformation"></textarea>
                    </div>
                    <div class="dialog-buttons">
                        <button type="button" @click="showCreateDialog = false">取消</button>
                        <button type="submit">创建</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- 编辑员工对话框 -->
        <div v-if="showEditDialog" class="dialog-overlay">
            <div class="dialog">
                <h3>编辑员工信息</h3>
                <form @submit.prevent="updateStaff">
                    <div class="form-group">
                        <label>用户名:</label>
                        <span>{{ editForm.username }}</span>
                    </div>
                    <div class="form-group">
                        <label>真实姓名:</label>
                        <input v-model="editForm.realName">
                    </div>
                    <div class="form-group">
                        <label>角色:</label>
                        <select v-model="editForm.role" required>
                            <option value="PHOTOGRAPHER">摄影师</option>
                            <option value="Makeup Artist">化妆师</option>
                            <option value="Manger">经理</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>员工信息:</label>
                        <textarea v-model="editForm.staffInformation"></textarea>
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

<style scoped>
    .staff-management {
        padding: 20px;
    }

    .action-buttons {
        margin-bottom: 20px;
        display: flex;
        gap: 10px;
    }

    .button-link {
        display: inline-block;
        padding: 8px 16px;
        background-color: #f0f0f0;
        color: #333;
        text-decoration: none;
        border-radius: 4px;
        border: 1px solid #ddd;
    }

    .button-link:hover {
        background-color: #e0e0e0;
    }

    .staff-table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
    }

    .staff-table th, .staff-table td {
        border: 1px solid #ddd;
        padding: 8px 12px;
        text-align: left;
    }

    .staff-table th {
        background-color: #f2f2f2;
        font-weight: bold;
    }

    .actions {
        display: flex;
        gap: 5px;
    }

    .actions button {
        padding: 4px 8px;
    }

    .delete-btn {
        background-color: #ff4444;
        color: white;
    }

    .delete-btn:hover {
        background-color: #cc0000;
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
        padding: 20px;
        border-radius: 8px;
        width: 450px;
        max-width: 90%;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    }

    .dialog h3 {
        margin-top: 0;
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
    .form-group select,
    .form-group textarea {
        width: 100%;
        padding: 8px;
        box-sizing: border-box;
        border: 1px solid #ddd;
        border-radius: 4px;
    }

    .form-group textarea {
        min-height: 80px;
        resize: vertical;
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

    .dialog-buttons button[type="submit"]:hover {
        background-color: #45a049;
    }
</style>