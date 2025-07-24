<template>

    <div class="gallery-page">
        <!-- 顶部装饰条 -->
        <div class="decoration-bar"></div>

        <!-- 页面标题 -->
        <div class="gallery-header">
            <h1>臻影云图</h1>
            <h2>{{ currentCategory || '全部作品' }}<span class="chinese-comma">，</span>光影艺术的完美呈现</h2>
        </div>

        <!-- 照片列表 -->
        <div class="photo-list">
            <div v-if="loading" class="loading">
                <div class="spinner"></div>
                <p>正在加载您的珍贵记忆...</p>
            </div>
            <div v-else-if="!photos || photos.length === 0" class="no-photos">
                <i class="icon-camera"></i>
                <p>暂无作品展示</p>
            </div>
            <div v-else class="photos-grid">
                <div v-for="(photo, index) in validPhotos" :key="index" class="photo-item">
                    <div class="photo-container" v-if="isValidUrl(photo.fileUrl)">
                        <img
                                :src="photo.fileUrl"
                                :alt="photo.category || '臻影云图作品'"
                                class="gallery-image"
                                loading="lazy"
                                @error="handleImageError"
                                @click="openLightbox(index)"
                        />
                        <div class="photo-overlay">
                            <div class="overlay-content">
                                <h3>{{ photo.title || '无题作品' }}</h3>
                                <p>{{ photo.description || '用光影讲述的故事' }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="photo-meta">
                        <span class="category-tag">{{ photo.category || '未分类' }}</span>
                        <span class="photo-date">{{ formatDate(photo.createdAt) }}</span>
                    </div>
                </div>
            </div>
        </div>

        <!-- 页脚 -->
        <footer class="gallery-footer">
            <p>臻影云图摄影管理系统 © 2023 - 用专业定格永恒</p>
        </footer>

        <!-- 灯箱展示 -->
        <Lightbox
                v-if="showLightbox"
                :images="validPhotos"
                :initialIndex="currentImageIndex"
                @close="closeLightbox"
        />
    </div>
</template>

<script setup>
    import { ref, watch, onMounted, computed } from 'vue'
    import { useRoute } from 'vue-router'
    import api from '@/api/auth'


    const route = useRoute()
    const photos = ref([])
    const loading = ref(false)
    const currentCategory = ref(route.query.category || '')
    const showLightbox = ref(false)
    const currentImageIndex = ref(0)

    // 计算属性过滤有效照片
    const validPhotos = computed(() => {
        return photos.value.filter(photo =>
            photo && photo.fileUrl && isValidUrl(photo.fileUrl)
        )
    })

    // 验证URL是否有效
    const isValidUrl = (url) => {
        try {
            new URL(url)
            return true
        } catch {
            return false
        }
    }

    // 图片加载错误处理
    const handleImageError = (e) => {
        console.error('图片加载失败:', e.target.src)
        e.target.style.display = 'none'
    }

    // 日期格式化
    const formatDate = (dateString) => {
        if (!dateString) return '日期未知'
        const options = { year: 'numeric', month: 'long', day: 'numeric' }
        return new Date(dateString).toLocaleDateString('zh-CN', options)
    }

    // 打开灯箱
    const openLightbox = (index) => {
        currentImageIndex.value = index
        showLightbox.value = true
    }

    // 关闭灯箱
    const closeLightbox = () => {
        showLightbox.value = false
    }

    const fetchPhotos = async () => {
        loading.value = true
        try {
            const url = currentCategory.value
                ? `/api/photos/category/${encodeURIComponent(currentCategory.value)}`
                : '/api/photos'

            const response = await api.get(url)
            photos.value = Array.isArray(response.data) ? response.data : []
        } catch (error) {
            console.error('获取照片失败:', error)
            photos.value = []
        } finally {
            loading.value = false
        }
    }

    // 监听路由变化
    watch(() => route.query.category, (newVal) => {
        currentCategory.value = newVal || ''
        fetchPhotos()
    })

    // 初始加载
    onMounted(() => {
        fetchPhotos()
    })
</script>

<style scoped>
    /* 基础样式 */
    .gallery-page {
        max-width: 1400px;
        margin: 0 auto;
        padding: 0 20px 60px;
        font-family: 'Microsoft YaHei', 'Noto Sans SC', sans-serif;
        background-color: #f9f5f0;
        /*background-image: url('@/assets/images/subtle-paper-texture.png');*/
        position: relative;
        min-height: 100vh;
    }

    /* 顶部装饰条 */
    .decoration-bar {
        height: 8px;
        background: linear-gradient(90deg, #d4af37, #f0e6d2, #d4af37);
        margin-bottom: 30px;
    }

    /* 页面标题样式 */
    .gallery-header {
        text-align: center;
        margin-bottom: 40px;
        padding: 20px 0;
        position: relative;
    }

    .gallery-header h1 {
        font-size: 3.5rem;
        color: #333;
        margin-bottom: 10px;
        font-weight: 300;
        letter-spacing: 5px;
        text-shadow: 1px 1px 3px rgba(0,0,0,0.1);
    }

    .gallery-header h2 {
        font-size: 1.5rem;
        color: #666;
        font-weight: 300;
        margin-top: 0;
    }

    .chinese-comma {
        color: #d4af37;
    }

    /* 照片网格布局 */
    .photos-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
        gap: 25px;
        padding: 10px;
    }

    .photo-item {
        background: white;
        border-radius: 4px;
        overflow: hidden;
        box-shadow: 0 5px 15px rgba(0,0,0,0.08);
        transition: all 0.3s ease;
    }

    .photo-item:hover {
        transform: translateY(-5px);
        box-shadow: 0 10px 25px rgba(0,0,0,0.12);
    }

    /* 照片容器 */
    .photo-container {
        height: 320px;
        overflow: hidden;
        position: relative;
        cursor: pointer;
    }

    .gallery-image {
        width: 100%;
        height: 100%;
        object-fit: cover;
        transition: transform 0.5s ease;
    }

    .photo-container:hover .gallery-image {
        transform: scale(1.05);
    }

    /* 照片悬停遮罩 */
    .photo-overlay {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(0,0,0,0.5);
        opacity: 0;
        transition: opacity 0.3s ease;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
    }

    .photo-container:hover .photo-overlay {
        opacity: 1;
    }

    .overlay-content {
        padding: 20px;
        text-align: center;
    }

    .overlay-content h3 {
        font-size: 1.3rem;
        margin-bottom: 10px;
        font-weight: 400;
    }

    .overlay-content p {
        font-size: 0.9rem;
        line-height: 1.5;
    }

    /* 照片元信息 */
    .photo-meta {
        padding: 15px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        background: white;
    }

    .category-tag {
        background: #f0e6d2;
        color: #8e6c4b;
        padding: 3px 10px;
        border-radius: 12px;
        font-size: 0.8rem;
    }

    .photo-date {
        color: #999;
        font-size: 0.8rem;
    }

    /* 加载状态 */
    .loading {
        text-align: center;
        padding: 60px 0;
        color: #666;
    }

    .spinner {
        border: 4px solid #f0e6d2;
        border-top: 4px solid #d4af37;
        border-radius: 50%;
        width: 40px;
        height: 40px;
        animation: spin 1s linear infinite;
        margin: 0 auto 20px;
    }

    @keyframes spin {
        0% { transform: rotate(0deg); }
        100% { transform: rotate(360deg); }
    }

    /* 无照片状态 */
    .no-photos {
        text-align: center;
        padding: 80px 0;
        color: #999;
    }

    .icon-camera {
        font-size: 3rem;
        display: block;
        margin-bottom: 20px;
        color: #ddd;
    }

    /* 页脚样式 */
    .gallery-footer {
        text-align: center;
        margin-top: 60px;
        padding: 20px 0;
        color: #999;
        font-size: 0.9rem;
        border-top: 1px solid #eee;
    }

    /* 响应式设计 */
    @media (max-width: 768px) {
        .photos-grid {
            grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
        }

        .gallery-header h1 {
            font-size: 2.5rem;
        }

        .gallery-header h2 {
            font-size: 1.2rem;
        }
    }
</style>