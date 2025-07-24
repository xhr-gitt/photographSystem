import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import path from 'path';

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'), // 确保 @ 指向 src 目录
    },
  },
  server: {
    proxy: {
      // 关键修改：匹配 /api 或特定路径
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '') // 保留此重写
      },
      // 可选：单独代理登录接口（如果路径不固定）
      '/auth/login': {
        target: 'http://localhost:8080/api/auth/login',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/auth/, '/api/auth')
      }
    }
  },

  optimizeDeps: {
    include: ['font-awesome']
  }
});
