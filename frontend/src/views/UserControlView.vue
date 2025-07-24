<template>
    <div class="w-full max-w-md p-8 space-y-8 bg-white rounded-lg shadow-md">
    <div class="user-control">
        <h2>用户管理</h2>

        <!-- 搜索和筛选区域 -->
        <div class="search-filter">
            <div class="search-group">
                <input
                        type="text"
                        v-model="searchId"
                        placeholder="输入用户ID查询"
                        @keyup.enter="getUserById"
                >
                <button @click="getUserById">查询</button>
                <button @click="goToStaffManagement">员工管理</button>
                <button @click="goToEquipmentManager">设备管理</button>
                <router-link :to="{ name: 'main' }" class="text-xl font-semibold text-gray-900">
                    返回首页
                </router-link>
            </div>

            <div class="filter-group">
                <select v-model="selectedRole" @change="filterByRole">
                    <option value="">所有角色</option>
                    <option value="USER">普通用户</option>
                    <option value="ADMIN">管理员</option>
                    <option value="Makeup Artist">化妆师</option>
                    <option value="Photographer">摄影师</option>
                    <option value="Manger">经理</option>
                </select>
            </div>
        </div>

        <!-- 用户表格 -->
        <table class="user-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>用户名</th>
                <th>真实姓名</th>
                <th>手机号</th>
                <th>角色</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="user in users" :key="user.id">
                <td>{{ user.id }}</td>
                <td>{{ user.username }}</td>
                <td>{{ user.realName }}</td>
                <td>{{ user.phone }}</td>
                <td>{{ user.role }}</td>
                <td>
                    <button @click="openEditDialog(user)">编辑</button>
                    <button @click="deleteUser(user.id)">删除</button>
                </td>
            </tr>
            </tbody>
        </table>

        <!-- 分页控件 -->
        <div class="pagination">
            <button @click="prevPage" :disabled="currentPage === 1">上一页</button>
            <span>第 {{ currentPage }} 页 / 共 {{ totalPages }} 页</span>
            <button @click="nextPage" :disabled="currentPage === totalPages">下一页</button>
            <select v-model="pageSize" @change="handlePageSizeChange">
                <option value="5">每页5条</option>
                <option value="10">每页10条</option>
                <option value="20">每页20条</option>
                <option value="50">每页50条</option>
            </select>
        </div>

        <!-- 编辑对话框 -->
        <div v-if="showEditDialog" class="dialog-overlay">
            <div class="dialog">
                <h3>编辑用户</h3>
                <form @submit.prevent="submitEdit">
                    <div class="form-group">
                        <label>真实姓名:</label>
                        <input v-model="editForm.realName">
                    </div>
                    <div class="form-group">
                        <label>手机号:</label>
                        <input v-model="editForm.phone">
                    </div>
                    <div class="form-group">
                        <label>角色:</label>
                        <select v-model="editForm.role" required>
                            <option value="USER">普通用户</option>
                            <option value="ADMIN">管理员</option>
                            <option value="Makeup Artist">化妆师</option>
                            <option value="Photographer">摄影师</option>
                            <option value="Manger">经理</option>
                        </select>
                    </div>
                    <div class="dialog-buttons">
                        <button type="button" @click="closeEditDialog">取消</button>
                        <button type="submit">保存</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    </div>
</template>

<script>
    import api from '@/api/auth';

    export default {
        data() {
            return {
                users: [],
                currentPage: 1,
                pageSize: 10,
                totalPages: 1,
                totalElements: 0,
                showEditDialog: false,
                searchId: '',
                selectedRole: '',
                editForm: {
                    id: null,
                    username: '',
                    realName: '',
                    phone: '',
                    role: ''
                }
            };
        },
        mounted() {
            this.fetchUsers();
        },
        methods: {
            goToStaffManagement() {
                this.$router.push({ name: 'staff-management' }); // 使用路由名称而不是路径
            },
            goToEquipmentManager() {
                this.$router.push({ name: 'equipment-management' }); // 使用路由名称而不是路径
            },
            async fetchUsers() {
                try {
                    let url = `/api/user/users/page?pageNum=${this.currentPage}&pageSize=${this.pageSize}`;

                    // 如果有角色筛选，使用角色筛选接口
                    if (this.selectedRole) {
                        const response = await api.get(`/api/user/by-role/${this.selectedRole}`);
                        this.users = response.data;
                        this.totalPages = 1;
                        this.totalElements = this.users.length;
                        return;
                    }

                    // 否则使用分页查询
                    const response = await api.get(url);
                    const data = response.data;
                    this.users = data.content;
                    this.totalPages = data.totalPages;
                    this.totalElements = data.totalElements;
                } catch (error) {
                    console.error('获取用户列表失败:', error);
                    alert('登录令牌失效，请重新登陆');
                }
            },

            async getUserById() {
                if (!this.searchId) {
                    this.fetchUsers();
                    return;
                }

                try {
                    const response = await api.get(`/api/user/users/${this.searchId}`);
                    this.users = [response.data];
                    this.currentPage = 1;
                    this.totalPages = 1;
                    this.totalElements = 1;
                } catch (error) {
                    console.error('查询用户失败:', error);
                    alert('登录令牌失效，请重新登陆: ' + (error.response?.data?.message || error.message));
                    this.users = [];
                }
            },

            filterByRole() {
                this.currentPage = 1;
                this.fetchUsers();
            },

            handlePageSizeChange() {
                this.currentPage = 1;
                this.fetchUsers();
            },

            openEditDialog(user) {
                this.editForm = {
                    id: user.id,
                    username: user.username,
                    realName: user.realName || '',
                    phone: user.phone || '',
                    role: user.role
                };
                this.showEditDialog = true;
            },

            closeEditDialog() {
                this.showEditDialog = false;
            },

            async submitEdit() {
                try {
                    await api.put(`/api/user/users/${this.editForm.id}`, {
                        realName: this.editForm.realName,
                        phone: this.editForm.phone,
                        role: this.editForm.role
                    });
                    alert('修改成功');
                    this.closeEditDialog();
                    this.fetchUsers();
                } catch (error) {
                    console.error('修改用户失败:', error);
                    alert('修改用户失败: ' + (error.response?.data?.message || error.message));
                }
            },

            async deleteUser(userId) {
                if (confirm('确定要删除这个用户吗？')) {
                    try {
                        await api.delete(`/api/user/users/${userId}`);
                        alert('删除成功');
                        this.fetchUsers();
                    } catch (error) {
                        console.error('删除用户失败:', error);
                        alert('删除用户失败: ' + (error.response?.data?.message || error.message));
                    }
                }
            },

            prevPage() {
                if (this.currentPage > 1) {
                    this.currentPage--;
                    this.fetchUsers();
                }
            },

            nextPage() {
                if (this.currentPage < this.totalPages) {
                    this.currentPage++;
                    this.fetchUsers();
                }
            }
        }
    };
</script>

<style scoped>
    .w-full::before {
        content: "";
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-image: url('https://xhrbucket1.oss-cn-hangzhou.aliyuncs.com/uploads/%E5%A4%A7%E5%BA%86%E5%BD%B1%E6%A5%BC%E8%BD%AE%E6%92%AD6.png');
        background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
        z-index: 0;
        opacity: 0.6; /* 调整背景透明度 */
    }
    .user-control {
        padding: 20px;
        position: relative;


    }

    .search-filter {
        display: flex;
        justify-content: space-between;
        margin-bottom: 20px;
        gap: 15px;
    }

    .search-group {
        display: flex;
        gap: 10px;
    }

    .search-group input {
        padding: 8px;
        border: 1px solid #ddd;
        border-radius: 4px;
        flex-grow: 1;
    }

    .filter-group select {
        padding: 8px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }

    .user-table {
        width: 100%;
        border-collapse: collapse;
        margin-bottom: 20px;
    }

    .user-table th, .user-table td {
        border: 1px solid #ddd;
        padding: 8px;
        text-align: left;
    }

    .user-table th {
        background-color: #f2f2f2;
    }

    .pagination {
        margin-top: 20px;
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 10px;
    }

    .pagination select {
        padding: 5px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }

    button {
        padding: 5px 10px;
        cursor: pointer;
        margin: 0 2px;
        background-color: #78f03c;
        border: 1px solid #ccc;
        border-radius: 3px;
    }

    button:hover {
        background-color: #e0e0e0;
    }

    button:disabled {
        opacity: 0.5;
        cursor: not-allowed;
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
        color: #333;
    }

    .form-group {
        margin-bottom: 15px;
    }

    .form-group label {
        display: block;
        margin-bottom: 5px;
        font-weight: bold;
        color: #555;
    }

    .form-group input,
    .form-group select {
        width: 100%;
        padding: 8px;
        box-sizing: border-box;
        border: 1px solid #ddd;
        border-radius: 4px;
    }

    .form-group select {
        height: 36px;
    }

    .dialog-buttons {
        display: flex;
        justify-content: flex-end;
        gap: 10px;
        margin-top: 20px;
    }

    .dialog-buttons button {
        padding: 8px 16px;
    }

    .dialog-buttons button[type="submit"] {
        background-color: #4CAF50;
        color: white;
        border: none;
    }

    .dialog-buttons button[type="submit"]:hover {
        background-color: #45a049;
    }

    .dialog-buttons button[type="button"] {
        background-color: #f44336;
        color: white;
        border: none;
    }

    .dialog-buttons button[type="button"]:hover {
        background-color: #d32f2f;
    }
</style>