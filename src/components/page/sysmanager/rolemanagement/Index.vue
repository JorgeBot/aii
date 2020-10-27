<template>
  <div>
    <el-container class="container">
      <el-main>
        <OperateBar :permission="permission"
        ></OperateBar>
        <MainContent :permission="permission"
        ></MainContent>
      </el-main>
      <CreateRoleDialog></CreateRoleDialog>
      <EditRoleDialog></EditRoleDialog>
    </el-container>
  </div>
</template>
<script>

import domain from '@/../public/domain'
import CreateRoleDialog from "@/components/page/sysmanager/rolemanagement/components/CreateRoleDialog"
import EditRoleDialog from "@/components/page/sysmanager/rolemanagement/components/EditRoleDialog"
import MainContent from "@/components/page/sysmanager/rolemanagement/components/MainContent"
import OperateBar from "@/components/page/sysmanager/rolemanagement/components/OperateBar"

export default {
  name: 'RoleManagement',

  components: {
    CreateRoleDialog,
    EditRoleDialog,
    MainContent,
    OperateBar,
  },

  data() {
    return {
      permission: {
        create: false,
        save: false,
        operate: false,
      },
    };
  },

  methods: {
    getResources() {
      let resources = new Set(JSON.parse(localStorage.getItem("ms_resources")))
      if (domain.permission) {
        this.permission.create = !resources.has("新建角色")
        this.permission.operate = !resources.has("角色操作")
        this.permission.save = !resources.has("资源配置")
      }
    },
  },

  mounted() {
    this.getResources()
  }
}
</script>

<style scoped>
</style>

