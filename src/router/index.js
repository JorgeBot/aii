import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            component: resolve => require(['../components/common/Home.vue'], resolve),
            meta: {title: '自述文件'},
            children: [
                {
                    path: '/',
                    component: resolve => require(['../components/page/Dashboard.vue'], resolve),
                    meta: {title: '系统首页'}
                },
                {
                    path: '/tabs',
                    component: resolve => require(['../components/page/Tabs.vue'], resolve),
                    meta: {title: 'tab选项卡'}
                },
                {
                    path: '/usermanager',
                    component: resolve => require(['../components/page/sysmanager/usermanagement/Index.vue'], resolve),
                    meta: {title: '人员管理'}
                },
                {
                    path: '/rolemanager',
                    component: resolve => require(['../components/page/sysmanager/rolemanagement/Index.vue'], resolve),
                    meta: {title: '角色管理'}
                },
                {
                    path: '/projectmanagement',
                    component: resolve => require(['../components/page/sysmanager/projectmanagement/Index.vue'], resolve),
                    meta: {title: '项目管理'}
                },
                {
                    path: '/http',
                    component: resolve => require(['../components/page/interfacemanagement/Index.vue'], resolve),
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
            component: resolve => require(['../components/page/404.vue'], resolve)
        },
    ]
})
