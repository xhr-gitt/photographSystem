<script setup>
   import { useAuthStore } from '@/stores/auth.js'
   import { ref, onMounted } from 'vue'
   import { useRouter } from 'vue-router'
   import api from '@/api/auth'

   const authStore = useAuthStore()
   const router = useRouter()

   const staffList = ref([])

   // 获取员工列表（只获取摄影师）
   const fetchStaffList = async () => {
      try {
         const response = await api.get('/api/staff/sync-and-list')
         staffList.value = response.data
                 .filter(staff => staff.role === 'PHOTOGRAPHER')
                 .map(staff => ({
                    ...staff,
                    // 确保有id字段
                    id: staff.id || staff.username // 如果没有id则使用username作为后备
                 }))
      } catch (error) {
         console.error('获取摄影师列表失败:', error)
         alert('获取摄影师列表失败')
      }
   }

   // 查看员工详情
   const viewStaff = (id) => {
      router.push(`/staffAppointment/${id}`)
   }

   onMounted(() => {
      fetchStaffList()
   })
</script>

<template>
   <router-link :to="{ name: 'main' }" class="text-xl font-semibold text-gray-900">
      回到主页
   </router-link>
   <div class="staff-management">
      <h2>摄影师列表</h2>
      <div><router-link v-if="authStore.isAdmin" to="/appointments">查看预约</router-link></div>
      <p><router-link to="/userappointments">查看我的预约</router-link></p>
      <table class="staff-table">
         <thead>
         <tr>
            <th>姓名</th>
            <th>简介</th>
            <th>操作</th>
         </tr>
         </thead>
         <tbody>
         <tr v-for="staff in staffList" :key="staff.id">
            <td>{{ staff.realName || staff.username }}</td>
            <td>{{ staff.staffInformation || '-' }}</td>
            <td class="actions">
               <button @click="viewStaff(staff.id)">预约</button>
            </td>
         </tr>
         </tbody>
      </table>
   </div>
</template>

<style scoped>
   .staff-management {
      padding: 20px;
      max-width: 1000px;
      margin: 0 auto;
   }

   .staff-table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 20px;
   }

   .staff-table th, .staff-table td {
      border: 1px solid #ddd;
      padding: 12px 15px;
      text-align: left;
   }

   .staff-table th {
      background-color: #f2f2f2;
      font-weight: bold;
   }

   .actions {
      display: flex;
      gap: 10px;
   }

   .actions button {
      padding: 6px 12px;
      background-color: #4CAF50;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
   }

   .actions button:hover {
      background-color: #45a049;
   }
</style>