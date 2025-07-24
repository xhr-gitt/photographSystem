<script setup>
    import { ref, onMounted, nextTick } from 'vue';
    import { useAuthStore } from '@/stores/auth.js';
    import { Swiper, Pagination, Navigation, Autoplay } from 'swiper';
    import 'swiper/css';
    import 'swiper/css/pagination';
    import 'swiper/css/navigation';
    import 'swiper/css/autoplay';

    const authStore = useAuthStore();
    const photoKeys = ['photo1', 'photo2', 'photo3', 'photo4'];
    const photoMap = ref(null);
    const loading = ref(true);
    const error = ref(null);
    const imageLoadError = ref({});
    let swiperInstance = null;

    const initSwiper = () => {
        Swiper.use([Pagination, Navigation, Autoplay]);
        swiperInstance = new Swiper('.swiper', {
            modules: [Pagination, Navigation, Autoplay],
            slidesPerView: 1,
            spaceBetween: 30,
            loop: true,
            centeredSlides: true,
            autoplay: {
                delay: 3000,
                disableOnInteraction: false,
            },
            pagination: {
                el: '.swiper-pagination',
                clickable: true,
            },
            navigation: {
                nextEl: '.swiper-button-next',
                prevEl: '.swiper-button-prev',
            },
            breakpoints: {
                768: {
                    slidesPerView: 2,
                },
                1024: {
                    slidesPerView: 3,
                },
            }
        });
    };

    const handleImageError = (key) => {
        imageLoadError.value = { ...imageLoadError.value, [key]: true };
    };

    const handleImageLoad = (e) => {
        e.target.style.opacity = 1;
    };

    async function fetchPhotos() {
        try {
            loading.value = true;
            error.value = null;
            imageLoadError.value = {};

            const apiUrl = import.meta.env.VITE_API_BASE_URL
                ? `${import.meta.env.VITE_API_BASE_URL}/api/oss/photos`
                : 'http://localhost:8080/api/oss/photos';

            const response = await fetch(`${apiUrl}?${photoKeys.map(k => `keys=${encodeURIComponent(k)}`).join('&')}`, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authStore.token || ''}`
                }
            });

            if (!response.ok) throw new Error(`HTTP错误: ${response.status}`);

            const responseData = await response.json();

            const formattedData = {};
            for (let i = 0; i < responseData.length; i += 2) {
                formattedData[responseData[i]] = responseData[i + 1];
            }

            if (formattedData.data && typeof formattedData.data === 'object') {
                photoMap.value = formattedData.data;
            } else {
                throw new Error('API返回数据结构异常');
            }
        } catch (err) {
            error.value = `请求失败: ${err.message}`;
            photoMap.value = null;
        } finally {
            loading.value = false;
        }
    }

    onMounted(async () => {
        await fetchPhotos();
        nextTick(() => {
            if (photoMap.value) {
                initSwiper();
            }
        });
    });

</script>

<template>
    <div class="page1" id="top">
        <!-- 头部组件 -->
        <header>

            <div class="container_12">
                <div class="grid_12">
                    <h1>
                        <a href="index.html">
                            <img src="@/assets/images/logo.png" alt="您的幸福家庭">
                        </a>
                    </h1>
                    <div class="menu_block">
                        <router-link v-if="authStore.isAdmin" to="/uploadPic" class="donate">上传图片</router-link>
                        <div class="clear"></div>

                        <nav class="horizontal-nav full-width horizontalNav-notprocessed">
                            <ul class="sf-menu">
                                <li class="current">
                                    <router-link
                                            to="/"
                                            :class="{
                         'text-indigo-700 bg-indigo-100': $route.path === '/',
                         'text-gray-600 hover:text-indigo-700 hover:bg-indigo-50': $route.path !== '/'
                                 }">
                                        返回上一页
                                    </router-link>
                                </li>
                                <li v-if="authStore.isAdmin">
                                    <router-link to="/user-control">系统管理</router-link>
                                </li>

                                <li class="dropdown">
                                    <a href="#" class="dropbtn">查看作品</a>
                                    <div class="dropdown-content">
                                        <router-link to="/gallery">所有作品</router-link>
                                        <router-link to="/gallery?category=婚纱照">婚纱照</router-link>
                                        <router-link to="/gallery?category=写真">写真</router-link>
                                        <router-link to="/gallery?category=旅拍">旅拍</router-link>
                                        <router-link to="/gallery?category=亲子">亲子</router-link>
                                        <router-link to="/gallery?category=幼童">幼童</router-link>
                                    </div>
                                </li>
                                <li><router-link to="/appointment">预约拍摄</router-link></li>
                                <li><router-link to="/commit">参与品论</router-link></li>
                                <li><router-link to="/contact">关于我们</router-link></li>
                            </ul>
                        </nav>
                        <div class="clear"></div>
                    </div>
                </div>
            </div>
        </header>

        <!-- 幻灯片组件 -->
        <!--        <div class="fluidHeight container_12">-->
        <!--            <div class="sliderContainer">-->
        <!--                <div class="iosSlider">-->
        <!--                    <div class="slider">-->
        <!--                        <div v-for="(slide, index) in slides" :key="index" :class="['item', 'item'+(index+1)]">-->
        <!--                            <div class="inner">-->
        <!--                                <div class="text1"><span v-html="slide.text"></span></div>-->
        <!--                            </div>-->
        <!--                        </div>-->
        <!--                    </div>-->
        <!--                </div>-->
        <!--                <div class="slideSelectors">-->
        <!--                    <div v-for="(slide, index) in slides"-->
        <!--                         :key="index"-->
        <!--                         class="item"-->
        <!--                         :class="{ selected: currentSlide === index }"-->
        <!--                         @click="goToSlide(index)"></div>-->
        <!--                </div>-->
        <!--            </div>-->
        <!--        </div>-->

        <div class="gallery-container">
            <h1 class="gallery-title">臻影云图作品展示</h1>

            <div v-if="loading" class="loading">
                <i class="fas fa-spinner fa-spin"></i>
                <p>正在加载作品...</p>
            </div>

            <div v-else-if="error" class="error">
                <i class="fas fa-exclamation-triangle"></i>
                <p>{{ error }}</p>
                <button @click="fetchPhotos" class="retry-btn">
                    <i class="fas fa-sync-alt"></i> 重试
                </button>
            </div>

            <div v-else class="carousel-wrapper">
                <!-- 轮播图容器 -->
                <div class="swiper-container">
                    <div class="swiper">
                        <div class="swiper-wrapper">
                            <div v-for="(photoUrl, key) in photoMap" :key="key" class="swiper-slide">
                                <div class="slide-content">
                                    <div class="image-frame">
                                        <div class="image-wrapper">
                                            <img
                                                    :src="photoUrl"
                                                    :alt="key"
                                                    class="gallery-image"
                                                    @error="handleImageError(key)"
                                                    @load="handleImageLoad"
                                            />
                                            <div v-if="imageLoadError[key]" class="img-placeholder">
                                                <i class="fas fa-image"></i>
                                                <span>图片加载失败</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- 分页器 -->
                        <div class="swiper-pagination"></div>
                        <!-- 导航按钮 -->
                        <div class="swiper-button-prev"></div>
                        <div class="swiper-button-next"></div>
                    </div>
                </div>

                <!-- 底部文字 -->
                <div class="carousel-caption">
                    <div class="caption-line"></div>
                    <p>臻影云图作品展示</p>
                    <div class="caption-line"></div>
                </div>
            </div>
        </div>

        <!-- 内容组件 -->
        <div class="content">

            <div class="container_12">
                <div class="grid_6">
                    <h2>认识我们的团队</h2>
                    <img src="https://xhrbucket1.oss-cn-hangzhou.aliyuncs.com/uploads/%E5%A4%A7%E5%BA%86%E5%BD%B1%E6%A5%BC%E8%BD%AE%E6%92%AD3.png" alt="" class="img_inner fleft">
                    <div class="extra_wrapper">
                        <p class="col2"><a href="#"> 臻影云图摄影成立于2013年，是一家专注于人像摄影的专业机构。
                            我们拥有顶尖的摄影团队和后期制作团队，致力于为每位客户打造独一无二的摄影作品。 </a></p>
                        多年来，我们服务超过5000组家庭，客户满意度高达98.7%。
                        <br>
                        <a href="#" class="btn">了解更多</a>
                    </div>
                    <div class="clear"></div>
                </div>
                <div class="grid_5 prefix_1">
                    <h2>我们的使命</h2>
                    <div class="rel1">
                        <p>用镜头记录生活，用光影诠释艺术。我们致力于为每位客户打造专属的摄影体验。</p>
                    </div>
                    <a href="#" class="btn">了解更多</a>
                </div>
            </div>
            <div class="hor"></div>
            <div class="container_12">
                <div class="grid_3">
                    <h2>最新消息</h2>
                    <ul class="list">
                        <li v-for="(news, index) in latestNews" :key="index">
                            <time :datetime="news.date">{{ news.day }}<span>{{ news.month }}</span></time>
                            <div class="extra_wrapper">
                                <div class="title col2"><a href="#">{{ news.title }}</a></div>
                                {{ news.description }}
                            </div>
                        </li>
                    </ul>
                    <blockquote class="bq1">
                        <div class="title">客户评价</div>
                        <p>普遍评价，摄影师水平好，服务满意 </p>
                        <div class="col2">--来自政府调查问卷</div>
                    </blockquote>
                </div>
                <div class="grid_9">
                    <h2>我们的照片</h2>
                    <div>asfadsfasfa</div>
                    <section>
                        <ul id="da-thumbs" class="da-thumbs">
                            <li v-for="(campaign, index) in campaigns" :key="index">
                                <a href="#">
                                    <img :src="'@/assets/images/' + campaign.image" :alt="campaign.title" />
                                    <div><span>{{ campaign.title }}</span></div>
                                </a>
                            </li>
                        </ul>
                    </section>
                </div>
            </div>
        </div>

        <!-- 底部区块组件 -->
        <div class="bottom_block">
            <div class="container_12">
                <div class="grid_4">
                    <h3>保持关注</h3>
                    <div class="text1">订阅我们的新闻通讯</div>
                    <form id="newsletter" @submit.prevent="submitNewsletter">
                        <div class="rel">
                            <div class="success" v-if="newsletterSuccess">您的订阅请求已发送！</div>
                            <label class="email">
                                <input type="email" v-model="email" placeholder="输入您的邮箱">
                                <span class="error" v-if="emailError">*邮箱地址无效。</span>
                            </label>
                        </div>
                        <button type="submit" class="btn">提交</button>
                    </form>
                </div>
                <div class="grid_5 prefix_3">
                    <h3>保持联系</h3>
                    <div class="text1">在社交媒体上关注我们</div>
                    或者给我们发邮箱，我们提供您优质的拍摄体验
                    <div class="socials">
                        <a v-for="(social, index) in socialMedia" :key="index" :href="social.link">
                            <div :class="'fa fa-' + social.icon"></div>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- 页脚组件 -->
        <footer>
            <div class="container_12">
                <div class="grid_12">
                    <div class="copy">
                        臻影云图摄影 &copy; 2014 | <a href="#">隐私政策</a> <br> 更多模板 <a href="https://www.downdemo.com" target="_blank" title="网站模板">网站模板</a>
                    </div>
                </div>
            </div>
        </footer>
    </div>
</template>

<script>


    export default {
        name: 'HomePage',
        data() {
            return {
                currentSlide: 0,
                slides: [
                    {text: "加入我们的活动，为<br>无家可归者带来希望"},
                    {text: "做出正确的选择！<br>帮助那些需要帮助的人。"},
                    {text: "我们的使命是让更多人参与进来，<br>为需要帮助的人创造更好的生活"}
                ],
                latestNews: [
                    {date: "2025-06-20", day: "20", month: "6月", title: "市最美影楼", description: "根据大众投票，荣获最美影楼奖"},
                    {date: "2014-06-25", day: "25", month: "6月", title: "市最美人像", description: "根据大众投票，荣获最美人像奖"},
                    {date: "2014-07-1", day: "1", month: "7月", title: "市最佳服务影楼", description: "根据大众投票，荣获最佳服务奖"},
                    {date: "2014-07-05", day: "05", month: "7月", title: "市优秀企业奖", description: "根据政府选凭，荣获优秀企业奖"}
                ],

                socialMedia: [
                    {icon: "twitter", link: "#"},
                    {icon: "facebook", link: "#"},
                    {icon: "pinterest-square", link: "#"},
                    {icon: "google-plus", link: "#"},
                    {icon: "instagram", link: "#"}
                ],
                email: "",
                emailError: false,
                newsletterSuccess: false
            }
        }}
</script>

<style scoped>
    @import "@/assets/css/form.css";
    @import "@/assets/css/thumbs.css";
    @import "@/assets/css/slider.css";
    @import "@/assets/css/style.css";
    /* 下拉菜单容器 */
    .dropdown {
        color: #ff7319;
        position: relative;
        display: inline-block;
    }

    /* 下拉按钮样式 */
    .dropbtn {
        font-size: larger;
        display: inline-block;
        color: #bffff0;
        text-align: center;
        padding: 12px 16px;
        text-decoration: none;
    }

    /* 下拉内容 (默认隐藏) */
    .dropdown-content {
        display: none;
        position: absolute;
        background-color: #f9f9f9;
        min-width: 160px;
        box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
        z-index: 1;
    }

    /* 下拉菜单中的链接 */
    .dropdown-content a {
        color: black;
        padding: 12px 16px;
        text-decoration: none;
        display: block;
        text-align: left;
    }

    /* 鼠标悬停时改变下拉菜单链接颜色 */
    .dropdown-content a:hover {
        background-color: #f1f1f1;
    }

    /* 鼠标悬停时显示下拉菜单 */
    .dropdown:hover .dropdown-content {
        display: block;
    }

    /* 当前选中的分类样式 */
    .dropdown-content a.active {
        background-color: #4CAF50;
        color: white;
    }

    .gallery-container {
        max-width: 1500px;
        margin: 0 auto;
        padding: 40px 20px;
    }

    .gallery-title {
        text-align: center;
        font-size: 2.5rem;
        color: #333;
        margin-bottom: 40px;
        position: relative;
    }

    .gallery-title:after {
        content: "";
        display: block;
        width: 80px;
        height: 3px;
        background: #d4af37;
        margin: 15px auto 0;
    }

    .carousel-wrapper {
        background: white;
        border-radius: 15px;
        box-shadow: 0 15px 40px rgba(0,0,0,0.08);
        overflow: hidden;
        padding-bottom: 30px;
    }

    .swiper-container {
        padding: 30px;
    }

    .image-frame {
        padding: 15px;
        background: white;
        border-radius: 10px;
        box-shadow:
                0 5px 15px rgba(0,0,0,0.05),
                inset 0 0 0 1px rgba(0,0,0,0.03);
        margin: 0 10px;
        transition: transform 0.3s ease;
    }

    .image-frame:hover {
        transform: translateY(-5px);
    }

    .image-wrapper {
        width: 100%;
        height: 400px;
        display: flex;
        justify-content: center;
        align-items: center;
        overflow: hidden;
        border-radius: 8px;
        background: #f9f9f9;
    }

    .gallery-image {
        max-width: 100%;
        max-height: 100%;
        object-fit: contain;
        transition: all 0.5s ease;
        opacity: 0;
    }

    .swiper-slide-active .gallery-image {
        opacity: 1;
    }

    .img-placeholder {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        color: #999;
        background: #f5f5f5;
    }

    .img-placeholder i {
        font-size: 3rem;
        margin-bottom: 15px;
        color: #ddd;
    }

    .carousel-caption {
        display: flex;
        align-items: center;
        justify-content: center;
        margin-top: 20px;
        padding: 0 40px;
    }

    .carousel-caption p {
        margin: 0 25px;
        font-size: 1.8rem;
        color: #333;
        font-weight: 300;
        letter-spacing: 3px;
        text-align: center;
    }

    .caption-line {
        flex: 1;
        height: 1px;
        background: linear-gradient(90deg, transparent, #d4af37, transparent);
    }

    /* 分页器样式 */
    :deep(.swiper-pagination-bullet) {
        width: 12px;
        height: 12px;
        background: #ddd;
        opacity: 1;
    }

    :deep(.swiper-pagination-bullet-active) {
        background: #d4af37;
        width: 35px;
        border-radius: 6px;
    }

    /* 导航按钮 */
    :deep(.swiper-button-prev),
    :deep(.swiper-button-next) {
        color: white;
        background: rgba(212, 175, 55, 0.9);
        width: 50px;
        height: 50px;
        border-radius: 50%;
        box-shadow: 0 3px 15px rgba(0,0,0,0.2);
        transition: all 0.3s;
    }

    :deep(.swiper-button-prev:hover),
    :deep(.swiper-button-next:hover) {
        background: rgba(212, 175, 55, 1);
        transform: scale(1.1);
    }

    :deep(.swiper-button-prev:after),
    :deep(.swiper-button-next:after) {
        font-size: 1.5rem;
        font-weight: bold;
    }

    /* 响应式设计 */
    @media (max-width: 768px) {
        .gallery-title {
            font-size: 2rem;
        }

        .image-wrapper {
            height: 280px;
        }

        .carousel-caption p {
            font-size: 1.4rem;
        }

        :deep(.swiper-button-prev),
        :deep(.swiper-button-next) {
            width: 40px;
            height: 40px;
        }
    }

    .loading, .error {
        text-align: center;
        padding: 60px 0;
        color: #666;
    }

    .error {
        color: #e74c3c;
    }

    .retry-btn {
        margin-top: 20px;
        padding: 10px 25px;
        background: #d4af37;
        color: white;
        border: none;
        border-radius: 30px;
        cursor: pointer;
        transition: all 0.3s;
        font-size: 1rem;
    }

    .retry-btn:hover {
        background: #c19b2e;
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(0,0,0,0.1);
    }

    .fa-spinner {
        font-size: 2.5rem;
        margin-bottom: 20px;
        color: #d4af37;
    }

    .fa-exclamation-triangle {
        font-size: 2.5rem;
        margin-bottom: 20px;
        color: #e74c3c;
    }

</style>