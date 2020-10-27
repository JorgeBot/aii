export default {
    baseUrl: "http://localhost",
    permission: false,
    menu: [
        {
            icon: "el-icon-setting",
            name: "系统管理",
            type: "trunk",
            index: "manager",
            children: [
                {
                    index: "usermanager",
                    name: "人员管理",
                    type: "leaf"
                },
                {
                    index: "rolemanager",
                    name: "角色管理",
                    type: "leaf"

                },
                {
                    index: "projectmanagement",
                    name: "项目管理",
                    type: "leaf"
                },
            ]
        },
        {
            icon: "el-icon-lx-copy",
            name: "接口管理",
            index: "interface",
            type: "trunk",
            children: [
                {
                    index: "http",
                    name: "HTTP接口",
                    type: "leaf"
                },
            ]
        }
    ]
}