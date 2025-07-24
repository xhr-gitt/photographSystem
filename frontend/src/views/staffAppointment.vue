<template>
    <div class="staff-detail">
        <h2>摄影师详情</h2>

        <div v-if="staff">
            <div class="detail-item">
                <label>ID:</label>
                <span>{{ staff.id }}</span>
            </div>
            <div class="detail-item">
                <label>姓名:</label>
                <span>{{ staff.realName || staff.username }}</span>
            </div>
            <div class="detail-item">
                <label>简介:</label>
                <span>{{ staff.staffInformation || '-' }}</span>
            </div>

            <!-- 新增预约表单 -->
            <div class="appointment-form">
                <h3>预约摄影师</h3>
                <form @submit.prevent="createAppointment">
                    <div class="form-group">
                        <label>预约时间:</label>
                        <input
                                type="datetime-local"
                                v-model="appointmentForm.appointmentTime"
                                required
                        >
                    </div>
                    <div class="form-actions">
                        <button type="button" @click="$router.go(-1)" class="btn-back">返回</button>
                        <button type="submit" class="btn-submit">提交预约</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<script>
    import api from '@/api/auth';
    import { useAuthStore } from '@/stores/auth.js';
    console.log('authStore内容:', JSON.parse(JSON.stringify(useAuthStore())));

    export default {
        data() {
            return {
                staff: null,
                appointmentForm: {
                    appointmentTime: ''
                }
            }
        },
        mounted() {
            this.fetchStaffDetail();
        },
        methods: {
            async fetchStaffDetail() {
                try {
                    const username = this.$route.params.username;
                    const response = await api.get(`/api/staff/${username}`);
                    this.staff = response.data;
                } catch (error) {
                    console.error('获取员工详情失败:', error);
                    alert('获取员工详情失败');
                }
            },
            async createAppointment() {
                try {
                    const authStore = useAuthStore();
                    // 直接从 localStorage 获取（确保和登录时存储方式一致）
                    // 方法1：直接从localStorage获取（确保和登录时存储方式一致）
                    const rawUserId = localStorage.getItem('userId');
                    console.log('从localStorage读取的原始userId:', rawUserId);

                    if (!authStore.userId) {
                        throw new Error('无法获取当前用户ID，请重新登录');
                    }
                    // 终极验证
                    const userId = Number(rawUserId || authStore.userId);
                    if (isNaN(userId)) {
                        console.error('无效的用户ID:', {
                            localStorage: rawUserId,
                            pinia: authStore.userId,
                            converted: userId
                        });
                        throw new Error('用户未登录，请重新登录');
                    }

                    // 准备请求数据
                    const requestData = {
                        staffId: Number(this.staff.id),
                        userId: userId, // 确保转换为数字
                        appointmentTime: new Date(this.appointmentForm.appointmentTime).toISOString()
                    };

                    console.log('提交的预约数据:', requestData);

                    const response = await api.post('/api/appointment', requestData);

                    if (response.status === 200 || response.status === 201) {
                        alert('预约成功！');
                        this.$router.push('/appointments');
                    } else {
                        throw new Error(response.data?.message || '预约失败');
                    }
                } catch (error) {
                    console.error('预约失败:', error);
                    alert(`预约失败: ${error.response?.data?.message || error.message}`);
                }
            }
        }
    }
</script>

<style scoped>
    .staff-detail {
        padding: 20px;
        max-width: 600px;
        margin: 0 auto;
    }

    .detail-item {
        margin-bottom: 15px;
        padding: 10px;
        background: #f9f9f9;
        border-radius: 4px;
    }

    .detail-item label {
        font-weight: bold;
        margin-right: 10px;
        color: #333;
    }

    .appointment-form {
        margin-top: 30px;
        padding: 20px;
        background: #f5f5f5;
        border-radius: 8px;
    }

    .appointment-form h3 {
        margin-top: 0;
        color: #2c3e50;
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
        border: 1px solid #ddd;
        border-radius: 4px;
        box-sizing: border-box;
    }

    .form-actions {
        display: flex;
        justify-content: space-between;
        margin-top: 20px;
    }

    .btn-back {
        padding: 8px 16px;
        background: #e0e0e0;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    .btn-submit {
        padding: 8px 16px;
        background: #4CAF50;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    .btn-submit:hover {
        background: #45a049;
    }

    input[type="datetime-local"] {
        padding: 8px;
    }
</style>