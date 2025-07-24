<template>
    <div class="min-h-screen flex items-center justify-center bg-gray-50">
        <div class="w-full max-w-md p-8 space-y-6 bg-white rounded-lg shadow-md">
            <h2 class="text-2xl font-bold text-center text-gray-900">用户注册</h2>

            <form @submit.prevent="handleRegister" class="space-y-4">
                <div>
                    <input
                            v-model="form.username"
                            placeholder="用户名"
                            required
                            class="w-full px-3 py-2 border border-gray-300 rounded-md"
                    />
                </div>
                <div>
                    <input
                            v-model="form.password"
                            type="password"
                            placeholder="密码"
                            required
                            class="w-full px-3 py-2 border border-gray-300 rounded-md"
                    />
                </div>
                <div>
                    <input
                            v-model="form.phone"
                            placeholder="手机号"
                            required
                            class="w-full px-3 py-2 border border-gray-300 rounded-md"
                    />
                </div>

                <button
                        type="submit"
                        class="w-full py-2 px-4 bg-indigo-600 text-white rounded-md hover:bg-indigo-700"
                >
                    注册
                </button>
            </form>

            <div class="text-center">
                <router-link to="/login" class="text-sm text-indigo-600">已有账号？去登录</router-link>
            </div>
        </div>
    </div>
</template>

<script setup>
    import { ref } from 'vue'
    import { useRouter } from 'vue-router'
    import axios from '@/api/auth'

    const router = useRouter()
    const form = ref({
        username: '',
        password: '',
        phone: ''
    })

    const handleRegister = async () => {
        try {
            await axios.post('/api/user/zhuceusers', {
                ...form.value,
                role: 'USER' // 前端固定传USER
            })
            alert('注册成功')
            router.push('/login')
        } catch (error) {
            alert(error.response?.data?.message || '注册失败')
        }
    }
</script>