<!--项目管理-->
<template>
  <div>
    <el-container class="container">
      <el-main>
        <OperateBar :permission="permission"
        ></OperateBar>
        <ProjectTable :permission="permission"
        ></ProjectTable>
      </el-main>
      <CreateProjectDialog></CreateProjectDialog>
      <EditOperateDialog></EditOperateDialog>
      <ModuleOperateDialog></ModuleOperateDialog>
      <RelatedUsersDialog></RelatedUsersDialog>
    </el-container>
  </div>
</template>
<script>

import domain from '@/../public/domain'
import CreateProjectDialog from "@/components/page/sysmanager/projectmanagement/components/CreateProjectDialog";
import ProjectTable from "@/components/page/sysmanager/projectmanagement/components/ProjectTable";
import EditOperateDialog from "@/components/page/sysmanager/projectmanagement/components/EditOperateDialog";
import OperateBar from "@/components/page/sysmanager/projectmanagement/components/OperateBar";
import ModuleOperateDialog from "@/components/page/sysmanager/projectmanagement/components/ModuleOperateDialog";
import RelatedUsersDialog from "@/components/page/sysmanager/projectmanagement/components/RelatedUsersDialog";


export default {
  name: 'ProjectManagement',

  components: {
    CreateProjectDialog,
    ProjectTable,
    EditOperateDialog,
    OperateBar,
    ModuleOperateDialog,
    RelatedUsersDialog,
  },

  data() {
    return {
      permission: {
        create: false,
        operate: false,
        module: false,
      },
    }
  },

  methods: {
    getResources() {
      let resources = new Set(JSON.parse(localStorage.getItem("ms_resources")))
      if (domain.permission) {
        this.permission.create = !resources.has("新建项目")
        this.permission.operate = !resources.has("项目操作")
        this.permission.relatedUser = !resources.has("关联人员")
        this.permission.module = !resources.has("模块操作")
      }
    },
  },

  mounted() {
    this.getResources()
  }
}
</script>

<style scoped>
.relation-user {
  display: flex;
  justify-content: center;
}
</style>

