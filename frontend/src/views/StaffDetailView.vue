<template>
    <div class="staff-detail">
        <h2>员工详情</h2>

        <div v-if="staff">
            <div class="detail-item">
                <label>ID:</label>
                <span>{{ staff.id }}</span>
            </div>

            <div class="detail-item">
                <label>用户名:</label>
                <span>{{ staff.username }}</span>
            </div>
            <div class="detail-item">
                <label>真实姓名:</label>
                <span>{{ staff.realName }}</span>
            </div>
            <div class="detail-item">
                <label>角色:</label>
                <span>{{ staff.role }}</span>
            </div>

            <div class="detail-item">
                <label>职位介绍</label>
                <span>{{ staff.staffInformation || '-' }}</span>
            </div>
        </div>

        <button @click="$router.go(-1)">返回</button>
    </div>
</template>

<script>
    import api from '@/api/auth';

    export default {
        data() {
            return {
                staff: null
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
            }
        }
    }
</script>

<style scoped>
    .staff-detail {
        padding: 20px;
    }

    .detail-item {
        margin-bottom: 15px;
    }

    .detail-item label {
        font-weight: bold;
        margin-right: 10px;
    }
</style>