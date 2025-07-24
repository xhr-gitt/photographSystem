<template>
    <div class="bg-white shadow rounded-lg p-6">
        <h1 class="text-2xl font-bold text-gray-900 mb-4">欢迎回来！</h1>
        <p class="text-gray-600">您已成功登录大庆影楼管理系统</p>
    </div>
</template>

<style scoped>
    .bg-white::before {
        content: "";
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-image: url('https://xhrbucket1.oss-cn-hangzhou.aliyuncs.com/uploads/031a68b5-25ad-4626-8053-2fe0b0383e03.png');
        background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
        z-index: 0;
        opacity: 0.9; /* 调整背景透明度 */
    }
    /* 主标题样式 */
    .text-2xl {
        position: relative;
        font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
        font-size: 2.5rem;
        font-weight: 700;
        color: #ffffff;
        text-shadow:
                0 4px 8px rgba(0, 0, 0, 0.4),
                0 8px 16px rgba(0, 0, 0, 0.3);
        letter-spacing: 1.5px;
        margin-bottom: 1.5rem;
        -webkit-text-stroke: 1px rgba(0, 0, 0, 0.15); /* 文字描边 */
        text-stroke: 1px rgba(0, 0, 0, 0.15);
    }

    /* 副标题样式 */
    .text-gray-600 {
        position: relative;
        font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
        font-size: 1.25rem;
        font-weight: 500;
        color: #f0f0f0;
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
        line-height: 1.8;
        opacity: 0.95;
        max-width: 600px;
    }




</style>