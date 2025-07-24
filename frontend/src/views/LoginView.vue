<template>
    <div class="min-h-screen flex items-center justify-center bg-gray-50">
        <div class="w-full max-w-md p-8 space-y-8 bg-white rounded-lg shadow-md">
            <div class="text-center">
                <h2 class="text-3xl font-extrabold text-gray-900">大庆影楼管理系统</h2>
                <p class="mt-2 text-sm text-gray-600">请登录您的账户</p>
            </div>

            <form class="mt-8 space-y-6" @submit.prevent="handleLogin">
                <div class="rounded-md shadow-sm space-y-4">
                    <div>
                        <label for="username" class="sr-only">用户名</label>
                        <input
                                id="username"
                                v-model="form.username"
                                name="username"
                                type="text"
                                required
                                class="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                                placeholder="用户名"
                        />
                    </div>
                    <div>
                        <label for="password" class="sr-only">密码</label>
                        <input
                                id="password"
                                v-model="form.password"
                                name="password"
                                type="password"
                                required
                                class="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                                placeholder="密码"
                        />
                    </div>
                </div>

                <div class="flex items-center justify-between">
                    <div class="flex items-center">
                        <input
                                id="remember-me"
                                v-model="form.rememberMe"
                                name="remember-me"
                                type="checkbox"
                                class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                        />
                        <label for="remember-me" class="ml-2 block text-sm text-gray-900"> 记住我 </label>
                    </div>
                </div>

                <div>
                    <button
                        type="submit"
                        :disabled="loading"
                        class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                        :class="{ 'opacity-50 cursor-not-allowed': loading }"
                >
                    <span v-if="!loading">登录</span>
                    <span v-else>登录中...</span>
                </button>
                    <router-link
                            to="/register"
                            class="w-1/2 ml-2 inline-flex justify-center py-2 px-4 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                    >
                        注册
                    </router-link>
                </div>


                <div v-if="error" class="text-red-500 text-sm text-center">
                    {{ error }}
                </div>
            </form>
        </div>
    </div>
</template>

<style scoped>
    /* 添加背景层 */
    .min-h-screen::before {
        content: "";
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-image: url('https://xhrbucket1.oss-cn-hangzhou.aliyuncs.com/uploads/e70e6b94-a461-40b2-aa13-aa8930fc55f1.png');
        background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
        z-index: 0;
        opacity: 0.9; /* 调整背景透明度 */
    }

    /* 增强表单可读性 */
    .bg-white {
        background-color: rgba(255, 255, 255, 0.22);
        backdrop-filter: blur(2px); /* 可选：添加轻微模糊效果 */
    }

    /* 确保输入框在透明背景上清晰可见 */
    .border-gray-300 {
        border-color: rgba(209, 213, 219, 0.7);
    }


    .mt-8{
        width: 100%;
        max-width: 400px;
        padding: 2.5rem;
        background: rgba(255, 255, 255, 0.3)!important; /* 调整透明度为30% */
        backdrop-filter: blur(10px);
        -webkit-backdrop-filter: blur(10px);
        border-radius: 16px;
        border: 1px solid rgba(255, 255, 255, 0.4)!important; /* 更透明的边框 */
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    }

</style>

<script setup>
    import { ref } from 'vue'
    import { useAuthStore } from '@/stores/auth'

    const authStore = useAuthStore()
    const form = ref({
        username: '',
        password: '',
        rememberMe: false
    })
    const loading = ref(false)
    const error = ref('')

    const handleLogin = async () => {
        try {
            loading.value = true
            error.value = ''

            await authStore.loginUser({
                username: form.value.username,
                password: form.value.password
            })

        } catch (err) {
            error.value = '用户名或密码错误'
            console.error('登录异常:', {
                message: err.message,
                stack: err.stack
            })
        } finally {
            loading.value = false
        }
    }
</script>