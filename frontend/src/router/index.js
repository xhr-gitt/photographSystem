import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import LoginView from '@/views/LoginView.vue';
import HomeView from '@/views/HomeView.vue';
import LayoutView from '@/views/LayoutView.vue';
import Main from '@/views/Main.vue';

const routes = [
    {
        path: '/login',
        name: 'login',
        component: LoginView,
        meta: { requiresAuth: false },
    },
    {
        path: '/register',
        name: 'register',
        component: () => import('@/views/RegisterView.vue'),
        meta: { requiresAuth: false }
    },
    {
        path: '/',
        component: LayoutView,
        meta: { requiresAuth: true },
        children: [
            {
                path: '',
                name: 'home',
                component: HomeView,
            },
            {
                path: 'user-control',
                name: 'user-control',
                component: () => import('@/views/UserControlView.vue'),
                meta: {
                    hideNavbar: true,
                    requiresAuth: true,
                    requiresAdmin: true
                },
            },
            {
                path: 'staff-management',
                name: 'staff-management',
                component: () => import('@/views/StaffManagementView.vue'),
                meta: {
                    hideNavbar: true,
                    requiresAuth: true,
                    requiresAdmin: true
                },

            },{
                path: 'equipment-management',
                name: 'equipment-management',
                component: () => import('@/views/EquipmentManagementView.vue'),
                meta: {
                    hideNavbar: true,
                    requiresAuth: true,
                    requiresAdmin: true

                },

            },
            {path: 'rentEquipment',
                name: 'rentEquipment',
                component: () => import('@/views/RentEquipmentView.vue'),
                meta: {
                    hideNavbar: true,
                    requiresAuth: true,
                    requiresAdmin: true
                }
            },
            {
                path: 'staff/:username',
                name: 'staff-detail',
                component: () => import('@/views/StaffDetailView.vue'),
                meta: {
                    hideNavbar: true,
                    requiresAuth: true,
                    requiresAdmin: true
                },
                props: true
            },
            {
                path: 'staffAppointment/:username',
                name: 'staffAppointment',
                component: () => import('@/views/staffAppointment.vue'),
                meta: {
                    hideNavbar: true,
                    requiresAuth: true,
                },
                props: true
            },
            {
                path: 'appointment',
                name: 'appointment',
                component: () => import('@/views/appointment.vue'),
                meta: {
                    hideNavbar: true
                }
            },
            {
                path: 'appointments',
                name: 'appointments',
                component: () => import('@/views/Allappointments.vue'),
                meta: {
                    hideNavbar: true
                }
            }, {
                path: 'userappointments',
                name: 'userappointments',
                component: () => import('@/views/userappointments.vue'),
                meta: {
                    hideNavbar: true
                }
            },
            {
                path: 'get-involved',
                name: 'get-involved',
                component: () => import('@/views/GetInvolvedView.vue')
            },
            {
                path: 'contact',
                name: 'contact',
                component: () => import('@/views/ContactView.vue'),
                meta: {
                    hideNavbar: true,

                },
            }, {
                path: 'uploadPic',
                name: 'uploadPic',
                component: () => import('@/views/uploadPic.vue'),
                meta: {
                    hideNavbar: true,

                },
            },{
                path: 'commit',
                name: 'commit',
                component: () => import('@/views/commit.vue'),
                meta: {
                    hideNavbar: true,

                },
            },
            // {
            //     path: 'toMain',
            //     name: 'toMain',
            //     component: () => import('@/views/Main.vue'),
            //     meta: {
            //         hideNavbar: true,
            //
            //     },
            // },
        ],
    },
    {
        path: '/main',
        name: 'main',
        component: () => import('@/views/Main.vue'), // 改为动态导入
        meta: {
            requiresAuth: true,
            hideNavbar: true
        }
    },{
        path: '/ourPage',
        name: 'ourPage',
        component: () => import('@/views/ourPage.vue'), // 改为动态导入
        meta: {
            requiresAuth: true,
            hideNavbar: true
        }
    },

    {
        path: '/gallery',
        name: 'Gallery',
        component: () => import('@/views/Gallery.vue'),
        props: (route) => ({ query: route.query }) ,// 传递查询参数
        meta: {
            requiresAuth: true,
            hideNavbar: true
        }
    }
];

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes,
});

router.beforeEach((to, from, next) => {
    const authStore = useAuthStore();

    console.log('路由守卫检查:', {
        path: to.path,
        requiresAuth: to.meta.requiresAuth,
        requiresAdmin: to.meta.requiresAdmin,
        isAuthenticated: authStore.isAuthenticated,
        isAdmin: authStore.isAdmin,
        token: authStore.token
    });

    // 检查是否需要认证
    if (to.meta.requiresAuth && !authStore.isAuthenticated) {
        console.warn('路由拦截: 未认证用户访问受保护路由');
        return next('/login');
    }

    // 检查是否需要管理员权限
    if (to.meta.requiresAdmin && !authStore.isAdmin) {
        console.warn('路由拦截: 非管理员尝试访问管理员路由');
        return next('/'); // 重定向到首页或其他适当页面
    }

    next();
});

export default router;