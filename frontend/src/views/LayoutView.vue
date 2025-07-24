<template>
    <div class="min-h-screen bg-gray-100">
        <nav v-if="!$route.meta.hideNavbar" class="bg-white shadow-sm">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div class="flex justify-between h-16">
                    <div class="flex items-center">
                        <router-link :to="{ name: 'main' }" class="text-xl font-semibold text-gray-900">
                            进入大庆影楼管理系统
                        </router-link>
                    </div>
                    <div class="flex items-center">
                        <button
                                @click="handleLogout"
                                class="px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:text-gray-900 hover:bg-gray-50"
                        >
                            退出登录
                        </button>
                    </div>
                </div>
            </div>
        </nav>



        <main class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
            <router-view />
        </main>
    </div>
</template>


<style scoped>
    /* 电脑端专属优化 - 保持原有class名称 */



    /* 半透明遮罩层 - 电脑端更明显的遮罩 */
    .min-h-screen::before {
        content: "";
        position: fixed; /* 改为fixed确保全屏覆盖 */
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: linear-gradient(180deg, rgba(15, 23, 42, 0) 0%, rgba(15, 23, 42, 0) 100%);
        z-index: 0;
        pointer-events: none;
    }



    /* 系统标题 - 电脑端更大更醒目 */
    .text-xl {
        color: #6fff22 !important;
        text-shadow:
                0 2px 1px rgba(0, 0, 0, 0.4),
                0 4px 2px rgba(0, 0, 0, 0.3);
        font-size: 2.5rem !important; /* 电脑端增大字号 */
        font-weight: 700 !important;
        letter-spacing: 1px;
        position: relative;
        z-index: 1;

    }



    /* 退出按钮 - 电脑端更明显的交互效果 */
    .px-3 {
        padding: 0.75rem 1.25rem !important;
        position: relative; /* 添加定位上下文 */
        z-index: 20 !important; /* 确保按钮在最上层 */
        background: rgba(255, 255, 255, 0.15) !important; /* 添加轻微背景 */
        backdrop-filter: blur(8px); /* 毛玻璃效果 */
        border-radius: 8px !important; /* 圆角边框 */
        transition: all 0.3s ease;
        border: 1px solid rgba(255, 255, 255, 0.2); /* 添加边框增强立体感 */
    }

    .text-gray-700 {
        color: #e0e0e0 !important;
        text-shadow: 0 1px 3px rgba(0, 0, 0, 0.4);
        font-weight: 500 !important;
        transition: all 0.2s ease;
    }

    .hover\:text-gray-900:hover {
        color: #ffffff !important;
        transform: translateY(-1px); /* 悬停微动效果 */
    }

    .hover\:bg-gray-50:hover {
        background-color: rgba(255, 255, 255, 0.2) !important;
    }




</style>


<script setup>
    import { useAuthStore } from '@/stores/auth'

    const authStore = useAuthStore()

    const handleLogout = () => {
        authStore.logoutUser()
    }
</script>