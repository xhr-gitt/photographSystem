// stores/auth.js
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login } from '@/api/auth'
import router from '@/router'

export const useAuthStore = defineStore('auth', () => {
    // 状态初始化（添加类型转换）
    const token = ref(localStorage.getItem('token') || null)
    const role = ref(localStorage.getItem('role') || null)
    const userId = ref(
        localStorage.getItem('userId')
            ? Number(localStorage.getItem('userId'))
            : null
    )
    const isAuthenticated = computed(() => !!token.value)

    // 修改后的setToken方法（带严格验证）
    const setToken = (newToken, newRole, newUserId) => {
        if (!newToken) {
            console.error('Token不能为空')
            throw new Error('认证令牌无效')
        }

        // 严格验证userId
        const parsedUserId = Number(newUserId)
        // if (isNaN(parsedUserId) {
        //     console.error('非法的用户ID:', {
        //         received: newUserId,
        //         type: typeof newUserId
        //     })
        //     throw new Error('用户ID必须是有效数字')
        // }

        // 存储数据（确保类型正确）
        token.value = newToken
        role.value = newRole
        userId.value = parsedUserId

        localStorage.setItem('token', newToken)
        localStorage.setItem('role', newRole)
        localStorage.setItem('userId', String(parsedUserId))

        // 验证存储结果
        console.log('认证数据已更新:', {
            token: newToken,
            userId: parsedUserId,
            role: newRole,
            storage: {
                token: localStorage.getItem('token'),
                userId: localStorage.getItem('userId'),
                role: localStorage.getItem('role')
            }
        })
    }

    // 增强的loginUser方法
    const loginUser = async (credentials) => {
        try {
            console.log('发起登录请求...')
            const response = await login(credentials)

            // 调试：打印完整响应结构
            console.log('登录响应原始数据:', JSON.parse(JSON.stringify(response)))

            // 处理不同响应结构
            const result = response.data || response
            if (!result) throw new Error('空响应')

            // 关键修复：确保获取userId
            const effectiveUserId = result.userId ?? result.data?.id
            if (!effectiveUserId) {
                throw new Error(`响应缺少用户ID字段: ${JSON.stringify(result)}`)
            }

            // 转换并验证ID
            const userId = Number(effectiveUserId)
            if (isNaN(userId)) {
                throw new Error(`无效的用户ID格式: ${effectiveUserId}`)
            }

            setToken(result.token, result.role, userId)

            // 调试：登录后状态验证
            console.log('登录成功，当前状态:', {
                pinia: { userId: userId.value, token: token.value },
                localStorage: {
                    userId: localStorage.getItem('userId'),
                    token: localStorage.getItem('token')
                }
            })

            window.location.replace('/')
            return result
        } catch (error) {
            console.error('登录流程失败:', {
                error: error.message,
                stack: error.stack
            })
            logoutUser()
            throw new Error(`登录失败: ${error.message}`)
        }
    }

    const logoutUser = () => {
        console.log('执行登出，清除认证数据')
        token.value = null
        role.value = null
        userId.value = null

        localStorage.removeItem('token')
        localStorage.removeItem('role')
        localStorage.removeItem('userId')

        router.push('/login')
    }

    // 添加hydrate方法同步状态
    const hydrate = () => {
        userId.value = localStorage.getItem('userId')
            ? Number(localStorage.getItem('userId'))
            : null
        token.value = localStorage.getItem('token')
        role.value = localStorage.getItem('role')
    }

    return {
        token,
        userId,
        role,
        isAuthenticated,
        isAdmin: computed(() =>
            ['ADMIN', 'ROLE_ADMIN'].includes(role.value?.toUpperCase())
        ),
        isPhotographer: computed(() =>
            ['Photographer', 'ROLE_PHOTOGRAPHER'].includes(role.value?.toUpperCase())
        ),
        loginUser,
        logoutUser,
        hydrate
    }
})