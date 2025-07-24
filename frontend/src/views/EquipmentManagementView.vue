<template>
    <div class="equipment-management">
        <h2>设备管理</h2>
        <p><router-link to="/user-control">返回用户管理页面</router-link></p>

        <!-- 搜索区域 -->
        <div class="search-section">
            <div class="search-group">
                <label>设备名称:</label>
                <input v-model="searchParams.name" placeholder="输入设备名称">
            </div>
            <div class="search-group">
                <label>购买日期范围:</label>
                <input type="date" v-model="searchParams.startDate">
                <span>至</span>
                <input type="date" v-model="searchParams.endDate">
            </div>
            <div class="search-group">
                <label>状态:</label>
                <select v-model="searchParams.status">
                    <option value="">全部</option>
                    <option value="AVAILABLE">可用</option>
                    <option value="ASSIGNED">不可用</option>
                </select>
            </div>
            <button @click="searchEquipment">搜索</button>
            <button @click="resetSearch">重置</button>
        </div>

        <!-- 操作按钮 -->
        <div class="action-buttons" >
            <button @click="showCreateDialog = true">新增设备</button>
            <router-link :to="{ name: 'rentEquipment' }" class="text-xl font-semibold text-gray-900">
                设备租借管理
            </router-link>
        </div>

        <!-- 设备列表 -->
        <table class="equipment-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>设备名称</th>
                <th>购买日期</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="equipment in equipmentList" :key="equipment.id">
                <td>{{ equipment.id }}</td>
                <td>{{ equipment.name }}</td>
                <td>{{ formatDate(equipment.purchaseDate) }}</td>
                <td :class="getStatusClass(equipment.status)">
                    {{ getStatusText(equipment.status) }}
                </td>
                <td class="actions">
                    <button @click="prepareEditEquipment(equipment)">编辑</button>

                    <button class="delete-btn" @click="confirmDelete(equipment)">删除</button>
                    <button @click="showBorrowDialog = true">借用设备</button>
                </td>
            </tr>
            </tbody>
        </table>
        <div v-if="showBorrowDialog" class="dialog-overlay">
            <div class="dialog">
                <h3>借用设备</h3>
                <form @submit.prevent="borrowEquipment">
                    <div class="form-group">
                        <label>设备ID:</label>
                        <input v-model.number="borrowForm.equipmentId" type="number" required>
                    </div>
                    <div class="form-group">
                        <label>借用目的:</label>
                        <textarea v-model="borrowForm.purpose" required maxlength="500"></textarea>
                    </div>
                    <div class="dialog-buttons">
                        <button type="button" @click="showBorrowDialog = false">取消</button>
                        <button type="submit">确认借用</button>
                    </div>
                </form>
            </div>
        </div>
        <!-- 创建设备对话框 -->
        <div v-if="showCreateDialog" class="dialog-overlay">
            <div class="dialog">
                <h3>新增设备</h3>
                <form @submit.prevent="createEquipment">
                    <div class="form-group">
                        <label>设备名称:</label>
                        <input v-model="newEquipment.name" required>
                    </div>
                    <div class="form-group">
                        <label>购买日期:</label>
                        <input type="date" v-model="newEquipment.purchaseDate" required>
                    </div>
                    <div class="form-group">
                        <label>初始状态:</label>
                        <select v-model="newEquipment.status" required>
                            <option value="AVAILABLE">可用</option>
                            <option value="ASSIGNED">不可用</option>
                        </select>
                    </div>
                    <div class="dialog-buttons">
                        <button type="button" @click="showCreateDialog = false">取消</button>
                        <button type="submit">创建</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- 编辑设备对话框 -->
        <div v-if="showEditDialog" class="dialog-overlay">
            <div class="dialog">
                <h3>编辑设备</h3>
                <form @submit.prevent="updateEquipment">
                    <div class="form-group">
                        <label>ID:</label>
                        <span>{{ editForm.id }}</span>
                    </div>
                    <div class="form-group">
                        <label>设备名称:</label>
                        <input v-model="editForm.name" required>
                    </div>
                    <div class="form-group">
                        <label>购买日期:</label>
                        <input type="date" v-model="editForm.purchaseDate" required>
                    </div>
                    <div class="form-group">
                        <label>状态:</label>
                        <select v-model="editForm.status" required>
                            <option value="AVAILABLE">可用</option>
                            <option value="ASSIGNED">不可用</option>
                        </select>
                    </div>
                    <div class="dialog-buttons">
                        <button type="button" @click="showEditDialog = false">取消</button>
                        <button type="submit">保存</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- 修改状态对话框 -->
        <div v-if="showStatusDialog" class="dialog-overlay">
            <div class="dialog">
                <h3>修改设备状态</h3>
                <div class="form-group">
                    <label>设备名称:</label>
                    <span>{{ statusForm.name }}</span>
                </div>
                <div class="form-group">
                    <label>当前状态:</label>
                    <span>{{ getStatusText(statusForm.currentStatus) }}</span>
                </div>
                <div class="form-group">
                    <label>新状态:</label>
                    <select v-model="statusForm.newStatus">
                        <option value="AVAILABLE">可用</option>
                        <option value="ASSIGNED">不可用</option>
                    </select>
                </div>
                <div class="dialog-buttons">
                    <button type="button" @click="showStatusDialog = false">取消</button>
                    <button type="button" @click="changeEquipmentStatus">确认</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
    import { ref, onMounted } from 'vue'
    import { useRouter } from 'vue-router'
    import api from '@/api/auth'

    // 新增借用相关状态
    const showBorrowDialog = ref(false)
    const borrowForm = ref({
        equipmentId: null,
        purpose: ''
    })

    // 新增借用设备方法
    const borrowEquipment = async () => {
        try {
            await api.post('/api/equipment-borrow/borrow', {
                equipmentId: borrowForm.value.equipmentId,
                staffId: 1, // 实际应用中应从登录信息获取
                purpose: borrowForm.value.purpose
            })
            alert('设备借用成功')
            showBorrowDialog.value = false
            borrowForm.value = { equipmentId: null, purpose: '' }
            fetchEquipmentList() // 刷新设备列表
        } catch (error) {
            console.error('借用失败:', error)
            alert('借用失败: ' + (error.response?.data?.message || error.message))
        }
    }

    const router = useRouter()

    // 设备列表数据
    const equipmentList = ref([])

    // 搜索参数
    const searchParams = ref({
        name: '',
        startDate: '',
        endDate: '',
        status: ''
    })

    // 对话框控制
    const showCreateDialog = ref(false)
    const showEditDialog = ref(false)
    const showStatusDialog = ref(false)

    // 表单数据
    const newEquipment = ref({
        name: '',
        purchaseDate: '',
        status: 'AVAILABLE'
    })

    const editForm = ref({
        id: '',
        name: '',
        purchaseDate: ''
    })

    const statusForm = ref({
        id: '',
        name: '',
        currentStatus: '',
        newStatus: ''
    })

    // 初始化加载设备列表
    onMounted(() => {
        fetchEquipmentList()
    })

    // 获取设备列表
    const fetchEquipmentList = async () => {
        try {
            const response = await api.get('/api/equipment/GetAll')
            equipmentList.value = response.data
        } catch (error) {
            console.error('获取设备列表失败:', error)
            alert('获取设备列表失败')
        }
    }

    // 搜索设备
    const searchEquipment = async () => {
        try {
            let params = {}

            // 优先按名称搜索
            if (searchParams.value.name) {
                params.name = searchParams.value.name
            }
            // 如果没有名称但有日期范围，则按日期搜索
            else if (searchParams.value.startDate && searchParams.value.endDate) {
                params.startDate = searchParams.value.startDate
                params.endDate = searchParams.value.endDate
            }
            // 如果只有状态，则按状态搜索
            else if (searchParams.value.status) {
                params.status = searchParams.value.status
            }
            // 如果没有任何条件，则获取全部
            else {
                fetchEquipmentList()
                return
            }

            const response = await api.get('/api/equipment/LikeSearch', { params })
            equipmentList.value = response.data
        } catch (error) {
            console.error('搜索设备失败:', error)
            alert('搜索设备失败: ' + (error.response?.data?.message || '请检查搜索条件'))
        }
    }

    // 重置搜索
    const resetSearch = () => {
        searchParams.value = {
            name: '',
            startDate: '',
            endDate: '',
            status: ''
        }
        fetchEquipmentList()
    }

    // 创建设备
    const createEquipment = async () => {
        try {
            const equipmentData = {
                name: newEquipment.value.name,
                purchaseDate: newEquipment.value.purchaseDate,
                status: newEquipment.value.status
            }

            await api.post('/api/equipment/create', equipmentData)
            alert('创建成功')
            showCreateDialog.value = false
            newEquipment.value = { name: '', purchaseDate: '', status: 'AVAILABLE' }
            fetchEquipmentList()
        } catch (error) {
            console.error('创建设备失败:', error)
            alert('创建设备失败: ' + (error.response?.data?.message || error.message))
        }
    }

    // 准备编辑设备
    const prepareEditEquipment = (equipment) => {
        editForm.value = {
            id: equipment.id,
            name: equipment.name,
            purchaseDate: equipment.purchaseDate,
            status: equipment.status || 'AVAILABLE' // 确保状态有值
        }
        showEditDialog.value = true
    }

    // 更新设备
    const updateEquipment = async () => {
        try {
            const equipmentData = {
                name: editForm.value.name,
                purchaseDate: editForm.value.purchaseDate,
                status: editForm.value.status || 'AVAILABLE' // 确保状态有值
            }

            await api.put(`/api/equipment/updateInformationById/${editForm.value.id}`, equipmentData)
            alert('更新成功')
            showEditDialog.value = false
            fetchEquipmentList()
        } catch (error) {
            console.error('更新设备失败:', error)
            alert('更新设备失败: ' + (error.response?.data?.message || error.message))
        }
    }


    // 修改设备状态
    const changeEquipmentStatus = async () => {
        try {
            await api.patch(`/api/equipment/updateStatusByid/${statusForm.value.id}?newStatus=${statusForm.value.newStatus}`)
            alert('状态更新成功')
            showStatusDialog.value = false
            fetchEquipmentList()
        } catch (error) {
            console.error('更新状态失败:', error)
            alert('更新状态失败: ' + (error.response?.data?.message || error.message))
        }
    }

    // 确认删除
    const confirmDelete = (equipment) => {
        if (confirm(`确定要删除设备 "${equipment.name}" 吗？`)) {
            deleteEquipment(equipment.id)
        }
    }

    // 删除设备
    const deleteEquipment = async (id) => {
        try {
            await api.delete(`/api/equipment/DeleteById/${id}`)
            alert('删除成功')
            fetchEquipmentList()
        } catch (error) {
            console.error('删除设备失败:', error)
            alert('删除设备失败，该设备有未归还的借用记录，无法删除: ' + (error.response?.data?.message || error.message))
        }
    }

    // 查看设备详情
    const viewEquipment = (id) => {
        router.push(`/equipment/${id}`)
    }

    // 辅助函数：格式化日期
    const formatDate = (dateString) => {
        if (!dateString) return ''
        const date = new Date(dateString)
        return date.toLocaleDateString()
    }

    // 辅助函数：获取状态文本
    const getStatusText = (status) => {
        const statusMap = {
            'AVAILABLE': '可用',
            'ASSIGNED': '不可用'
        }
        return statusMap[status] || status
    }

    // 辅助函数：获取状态对应的CSS类
    const getStatusClass = (status) => {
        const classMap = {
            'AVAILABLE': 'status-available',
            'IN_USE': 'status-in-use',
            'MAINTENANCE': 'status-maintenance',
            'RETIRED': 'status-retired'
        }
        return classMap[status] || ''
    }
</script>

<style scoped>
    .equipment-management {
        padding: 20px;
    }

    .search-section {
        background-color: #f5f5f5;
        padding: 15px;
        border-radius: 5px;
        margin-bottom: 20px;
        display: flex;
        flex-wrap: wrap;
        gap: 15px;
        align-items: center;
    }

    .search-group {
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .search-group label {
        font-weight: bold;
        min-width: 80px;
    }

    .action-buttons {
        margin-bottom: 15px;
    }

    .equipment-table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 15px;
    }

    .equipment-table th, .equipment-table td {
        border: 1px solid #ddd;
        padding: 8px 12px;
        text-align: left;
    }

    .equipment-table th {
        background-color: #f2f2f2;
        font-weight: bold;
    }

    .actions {
        display: flex;
        gap: 5px;
    }

    .actions button {
        padding: 4px 8px;
        font-size: 0.9em;
    }

    .delete-btn {
        background-color: #ff4444;
        color: white;
    }

    .delete-btn:hover {
        background-color: #cc0000;
    }

    /* 状态样式 */
    .status-available {
        color: #28a745;
        font-weight: bold;
    }

    .status-in-use {
        color: #007bff;
        font-weight: bold;
    }

    .status-maintenance {
        color: #ffc107;
        font-weight: bold;
    }

    .status-retired {
        color: #6c757d;
        font-weight: bold;
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

    .dialog-buttons button[type="submit"],
    .dialog-buttons button[type="button"]:last-child {
        background-color: #4CAF50;
        color: white;
    }

    .dialog-buttons button[type="submit"]:hover,
    .dialog-buttons button[type="button"]:last-child:hover {
        background-color: #45a049;
    }
</style>