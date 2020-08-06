import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/dashboard'
        },
        {
            path: '/',
            component: resolve => require(['../components/common/Home.vue'], resolve),
            meta: {title: '自述文件'},
            children: [
                {
                    path: '/dashboard',
                    component: resolve => require(['../components/page/Dashboard.vue'], resolve),
                    meta: {title: '系统首页'}
                },
                {
                    path: '/icon',
                    component: resolve => require(['../components/page/Icon.vue'], resolve),
                    meta: {title: '自定义图标'}
                },
                {
                    path: '/tabs',
                    component: resolve => require(['../components/page/Tabs.vue'], resolve),
                    meta: {title: 'tab选项卡'}
                },
                {
                    path: '/404',
                    component: resolve => require(['../components/page/404.vue'], resolve),
                    meta: {title: '404'}
                },
                {
                    path: '/403',
                    component: resolve => require(['../components/page/403.vue'], resolve),
                    meta: {title: '403'}
                },
                {
                    path: '/admin',
                    component: resolve => require(['../components/page/admin.vue'], resolve),
                    meta: {title: '用户信息'}
                },
                {
                    path: '/usermanager',
                    component: resolve => require(['../components/page/sysmanager/usermanager.vue'], resolve),
                    meta: {title: '人员管理'}
                },
                {
                    path: '/rolemanager',
                    component: resolve => require(['../components/page/sysmanager/rolemanager.vue'], resolve),
                    meta: {title: '角色管理'}
                },
                {
                    path: '/http',
                    component: resolve => require(['../components/page/interface/HTTPManagement.vue'], resolve),
                    meta: {title: 'HTTP接口'}
                }
            ]
        },
        {
            path: '/login',
            component: resolve => require(['../components/page/Login.vue'], resolve)
        },
        {
            path: '*',
            redirect: '/404'
        }
    ]
})
