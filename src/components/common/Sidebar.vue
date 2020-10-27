<template>
    <div class="sidebar">
        <el-menu
                class="sidebar-el-menu"
                :default-active="onRoutes"
                :collapse="collapse"
                background-color="#20222A"
                text-color="rgba(255,255,255,.7)"
                active-text-color="#fff"
                unique-opened
                router
        >
            <template v-for="item in items">
                <template v-if="item.children">
                    <el-submenu :index="item.index" :key="item.index">
                        <template slot="title">
                            <i :class="item.icon"></i>
                            <span slot="title">{{ item.name }}</span>
                        </template>
                        <template v-for="subItem in item.children">
                            <el-submenu v-if="subItem.children" :index="subItem.index" :key="subItem.index">
                                <template slot="title">{{ subItem.name }}</template>
                                <el-menu-item
                                        v-for="(threeItem,i) in subItem.children"
                                        :key="i"
                                        :index="threeItem.index"
                                >{{ threeItem.name }}
                                </el-menu-item>
                            </el-submenu>
                            <el-menu-item v-else :index="subItem.index" :key="subItem.index">{{ subItem.name }}
                            </el-menu-item>
                        </template>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index" :key="item.index">
                        <i :class="item.icon"></i>
                        <span slot="title">{{ item.name }}</span>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
    import bus from ".//bus";
    import domain from '@/../public/domain'

    export default {
        data() {
            return {
                collapse: false,
                menuItems: [],
                items: []
            };
        },
        methods: {
            getMenuData() {
                let personMenus = new Set(JSON.parse(localStorage.getItem('ms_menus')))
                let menus = new Set(JSON.parse(JSON.stringify(domain.menu)))
                // 获取子
                if (domain.permission) {
                    menus.forEach(e => {
                        let children = []
                        e.children.forEach(c => {
                            if (personMenus.has(c.name)) {
                                children.push(c)
                            }
                        })
                        e.children = children
                        if (e.children.length === 0) {
                            menus.delete(e)
                        }
                    });
                }
                this.items = Array.from(menus)
            },
        },
        computed: {
            onRoutes() {
                return this.$route.path.replace("/", "");
            }
        }
        ,
        created() {
            // 通过 Event Bus 进行组件间通信，来折叠侧边栏
            bus.$on("collapse", msg => {
                this.collapse = msg;
            });
            this.getMenuData();
        }
    }
    ;
</script>

<style scoped>
    .sidebar {
        display: block;
        position: absolute;
        left: 0;
        top: 70px;
        bottom: 0;
        overflow-y: scroll;
    }

    .sidebar::-webkit-scrollbar {
        width: 0;
    }

    .sidebar-el-menu:not(.el-menu--collapse) {
        width: 250px;
    }

    .sidebar > ul {
        height: 100%;
    }
</style>
