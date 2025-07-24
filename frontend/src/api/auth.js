import axios from 'axios'
import { useAuthStore } from '@/stores/auth'

// 创建新实例避免污染全局axios
const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}` // 确保此处生效
    },
    withCredentials: true // 关键配置
})

// axios示例（Vue/React）
// axios.get('/api/protected/data', {
//     headers: {
//         'Authorization': `Bearer ${localStorage.getItem('token')}` // 确保此处与存储的key一致
//     }
// }).catch(error => {
//     console.log('完整错误详情:', {
//         status: error.response.status,
//         headers: error.response.headers,
//         data: error.response.data
//     });
// });

// 简化登录方法
// auth.js 中的 login 方法修改
export const login = async (credentials) => {
    try {
        const freshAxios = axios.create({
            baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
            withCredentials: true
        })

        const response = await freshAxios.post('/api/auth/login', credentials)

        if (!response) throw new Error('未收到响应')

        const token = response.data?.token ||
            response.headers?.['authorization']?.replace('Bearer ', '')

        if (!token) throw new Error('响应中未包含token')

        // 解析JWT token获取角色
        const decodedToken = parseJwt(token)
        const role = decodedToken.role || response.data?.role

        return {
            success: true,
            token: token,
            role: role, // 添加角色信息
            data: response.data
        }
    } catch (error) {
        console.error('原始登录错误:', error)
        throw new Error(error.response?.data?.message || '登录失败')
    }
}

// JWT解析工具函数
function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1]
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
        return JSON.parse(atob(base64))
    } catch (e) {
        return null
    }
}

// 新增注册方法（不携带token的纯净请求）
export const register = async (userData) => {
    try {
        // 创建不携带token的新实例
        const noAuthAxios = axios.create({
            baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
            withCredentials: true
        })

        const response = await noAuthAxios.post('/api/user/zhuceusers', {
            username: userData.username,
            password: userData.password,
            phone: userData.phone,
            role: 'USER' // 前端固定角色
        })

        return {
            success: true,
            data: response.data
        }
    } catch (error) {
        console.error('注册错误:', error)
        throw new Error(error.response?.data?.message || '注册失败')
    }
}

// 仅保留通用请求拦截器
api.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    // 跳过登录、注册和特定API
    const shouldSkipAuth = [
        '/api/auth/login',
        '/api/user/zhuceusers'
    ].some(path => config.url.includes(path))

    if (token && !shouldSkipAuth) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})

export default api