// 导入 Vue 的核心函数
import { createApp } from 'vue';
import { createPinia } from 'pinia'
// 导入全局样式（可选）
import './style.css';
// 导入根组件（App.vue）
import App from './App.vue';
// 导入路由实例（从 router/index.js）
import router from './router';
import './assets/main.css'


// 创建 Vue 应用实例
const app = createApp(App);

// 挂载路由到 Vue 应用
app.use(router);
app.use(createPinia())
// 将应用挂载到 DOM 的 #app 节点
// 将 Vue 应用挂载到 HTML 中 id 为 app 的节点
// （通常是 index.html 中的 <div id="app"></div>）。
app.mount('#app');
