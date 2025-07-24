<template>

    <div class="min-h-screen bg-gray-50">
        <!-- 顶部导航栏 -->
        <nav class="bg-white shadow-lg sticky top-0 z-50">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div class="flex justify-between h-16">
                    <div class="flex items-center">
                        <span class="text-2xl font-bold text-indigo-700 font-serif">臻影云图摄影影楼</span>
                        <span class="ml-2 text-xs bg-indigo-100 text-indigo-800 px-2 py-1 rounded-full">专业摄影</span>
                    </div>
                    <router-link to="/main" class="text-xl font-semibold text-gray-900">
                        返回首页
                    </router-link>
                </div>
            </div>
        </nav>

        <!-- 英雄区域 -->
        <div class="relative bg-gradient-to-r from-indigo-900 to-purple-900 py-20">
            <div class="max-w-7xl mx-auto px-4 text-center text-white">
                <h1 class="text-4xl md:text-5xl font-bold mb-6 font-serif">
                    定格精彩瞬间 · 铭记永恒时光
                </h1>
                <p class="text-xl text-indigo-100 mb-8 max-w-3xl mx-auto">
                    臻影云图摄影，专业提供亲子、写真、旅拍、情侣、幼童等摄影服务，
                    用镜头捕捉生活美好，用光影诠释情感故事。
                </p>

            </div>
        </div>

        <!-- 服务套系展示 -->
        <div class="max-w-7xl mx-auto px-4 py-16">
            <div class="text-center mb-12">
                <h2 class="text-3xl font-bold text-gray-900 mb-4">精选摄影套系</h2>
                <div class="w-24 h-1 bg-indigo-600 mx-auto"></div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-6">
                <div
                        v-for="(category, index) in categories"
                        :key="index"
                        class="group"
                >
                    <div class="relative overflow-hidden rounded-xl shadow-lg hover:shadow-xl transition-shadow">
                        <div class="aspect-w-3 aspect-h-4">
                            <img
                                    :src="images[index]"
                                    :alt="category.name"
                                    class="w-full h-full object-cover transform group-hover:scale-105 transition-transform duration-300"
                            />
                        </div>
                        <div class="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent opacity-0 group-hover:opacity-100 transition-opacity">
                            <div class="absolute bottom-0 left-0 right-0 p-4">
                                <h3 class="text-white font-semibold text-lg">{{ category.name }}</h3>
                                <p class="text-indigo-100 text-sm mt-1 hidden md:block">{{ category.desc }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="mt-3 text-center">
                        <h3 class="font-medium text-gray-900">{{ category.name }}摄影</h3>
                        <p class="text-sm text-gray-600 mt-1">{{ category.price }}</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- 关于我们 -->
        <div class="bg-white py-16">
            <div class="max-w-7xl mx-auto px-4">
                <div class="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
                    <div>
                        <img
                                src="https://images.unsplash.com/photo-1518199266791-5375a83190b7?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"
                                alt="关于我们"
                                class="rounded-xl shadow-lg w-full"
                        />
                    </div>
                    <div>
                        <span class="text-indigo-600 font-medium">十年品质</span>
                        <h2 class="text-3xl font-bold text-gray-900 mt-2 mb-4">关于影楼</h2>
                        <p class="text-gray-600 mb-4">
                            臻影云图摄影成立于2013年，是一家专注于人像摄影的专业机构。
                            我们拥有顶尖的摄影团队和后期制作团队，致力于为每位客户打造独一无二的摄影作品。
                        </p>
                        <p class="text-gray-600 mb-6">
                            多年来，我们服务超过5000组家庭，客户满意度高达98.7%。
                            坚持用艺术眼光捕捉生活瞬间，用专业态度诠释每份情感。
                        </p>

                    </div>
                </div>
            </div>
        </div>



        <!-- 页脚 -->
        <footer class="bg-gray-900 text-gray-300 py-12">
            <div class="max-w-7xl mx-auto px-4">
                <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
                    <div>
                        <h3 class="text-white text-lg font-medium mb-4">臻影云图摄影影楼</h3>
                        <p class="text-gray-400 text-sm">
                            用镜头记录生活，用光影诠释艺术。我们致力于为每位客户打造专属的摄影体验。
                        </p>
                    </div>
                    <!--                    <div>-->
                    <!--                        <h4 class="text-white text-sm font-medium mb-4">快速链接</h4>-->
                    <!--                        <ul class="space-y-2 text-sm">-->
                    <!--                            <li><a href="#" class="hover:text-white transition-colors">首页</a></li>-->
                    <!--                            <li><a href="#" class="hover:text-white transition-colors">客片展示</a></li>-->
                    <!--                            <li><a href="#" class="hover:text-white transition-colors">服务套系</a></li>-->
                    <!--                            <li><a href="#" class="hover:text-white transition-colors">关于我们</a></li>-->
                    <!--                            <li><a href="#" class="hover:text-white transition-colors">预约咨询</a></li>-->
                    <!--                        </ul>-->
                    <!--                    </div>-->
                    <div>
                        <h4 class="text-white text-sm font-medium mb-4">联系我们</h4>

                    </div>
                    <div>
                        <h4 class="text-white text-sm font-medium mb-4">关注我们</h4>

                    </div>
                </div>
                <div class="border-t border-gray-800 mt-8 pt-8 text-center text-sm text-gray-400">
                    © 2023 臻影云图摄影影楼 版权所有 | 黑ICP备12345678号
                </div>
            </div>
        </footer>
    </div>
</template>

<script setup>
    import { ref } from 'vue'

    // 分类数据
    const categories = ref([
        { name: '亲子', desc: '记录温馨亲子时光', price: '¥1299起' },
        { name: '写真', desc: '展现个性自我风采', price: '¥899起' },
        { name: '旅拍', desc: '边走边拍浪漫旅程', price: '¥2999起' },
        { name: '情侣', desc: '定格甜蜜爱情瞬间', price: '¥1599起' },
        { name: '幼童', desc: '捕捉童真可爱模样', price: '¥999起' }
    ])

    // 客户评价
    const reviews = ref([
        {
            name: '张女士',
            avatar: 'https://randomuser.me/api/portraits/women/44.jpg',
            rating: 5,
            comment: '摄影师非常专业，很会引导表情，成品效果超出预期！特别是儿童摄影那块，拍得特别自然生动。',
            date: '2023-05-15',
            service: '亲子摄影'
        },
        {
            name: '李先生',
            avatar: 'https://randomuser.me/api/portraits/men/32.jpg',
            rating: 5,
            comment: '服务态度很好，从前期沟通到后期选片都很耐心。旅拍套餐性价比很高，风景和人像结合得特别美。',
            date: '2023-06-20',
            service: '旅拍'
        },
        {
            name: '王小姐',
            avatar: 'https://randomuser.me/api/portraits/women/68.jpg',
            rating: 4,
            comment: '写真拍摄体验很棒，化妆师技术很好，服装选择也多。就是等待时间稍长，但成品很满意。',
            date: '2023-07-10',
            service: '个人写真'
        }
    ])

    // 图片数据（使用您提供的5张图片）
    const images = ref([
        'https://xhrbucket1.oss-cn-hangzhou.aliyuncs.com/uploads/%E5%A4%A7%E5%BA%86%E5%BD%B1%E6%A5%BC%E8%BD%AE%E6%92%AD6.png',
        'https://xhrbucket1.oss-cn-hangzhou.aliyuncs.com/uploads/%E5%A4%A7%E5%BA%86%E5%BD%B1%E6%A5%BC%E8%BD%AE%E6%92%AD7.png',
        'https://xhrbucket1.oss-cn-hangzhou.aliyuncs.com/uploads/%E5%A4%A7%E5%BA%86%E5%BD%B1%E6%A5%BC%E8%BD%AE%E6%92%AD3.png',
        'https://xhrbucket1.oss-cn-hangzhou.aliyuncs.com/uploads/%E5%A4%A7%E5%BA%86%E5%BD%B1%E6%A5%BC%E8%BD%AE%E6%92%AD4.png',
        'https://xhrbucket1.oss-cn-hangzhou.aliyuncs.com/uploads/%E5%A4%A7%E5%BA%86%E5%BD%B1%E6%A5%BC%E8%BD%AE%E6%92%AD5.png'
    ])
</script>

<style scoped>
    /* 自定义样式 */
    .font-serif {
        font-family: Georgia, Cambria, "Times New Roman", Times, serif;
    }

    /* 渐变按钮效果 */
    .bg-gradient-to-r {
        background-image: linear-gradient(to right, var(--tw-gradient-stops));
    }

    /* 响应式调整 */
    @media (max-width: 640px) {
        .hero-title {
            font-size: 2.25rem;
        }
    }
    .text-2xl{
        font-size: xx-large;
    }
</style>