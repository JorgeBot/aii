<template>
  <div>
    <el-container class="container">
      <el-aside width="270px" class="aside">
        <ModuleTree></ModuleTree>
      </el-aside>
      <el-main>
        <OperateBar :permission="permission"></OperateBar>
        <HTTPInterfaceTable :permission="permission"></HTTPInterfaceTable>
      </el-main>
    </el-container>

    <CreateHTTPInterfaceDialog></CreateHTTPInterfaceDialog>
    <EditHTTPInterfaceDialog></EditHTTPInterfaceDialog>
  </div>
</template>
<script>


import CreateHTTPInterfaceDialog from "@/components/page/interfacemanagement/components/CreateHTTPInterfaceDialog";
import OperateBar from "@/components/page/interfacemanagement/components/OperateBar";
import HTTPInterfaceTable from "@/components/page/interfacemanagement/components/HTTPInterfaceTable";
import EditHTTPInterfaceDialog from "@/components/page/interfacemanagement/components/EditHTTPInterfaceDialog";
import ModuleTree from "@/components/page/interfacemanagement/components/ModuleTree";

export default {
  name: "HTTPManagement",

  components: {
    CreateHTTPInterfaceDialog,
    OperateBar,
    HTTPInterfaceTable,
    EditHTTPInterfaceDialog,
    ModuleTree,
  },

  data() {
    return {
      permission: {
        create: false,
        operate: false,
      },
    }
  },
  methods: {

  },
  mounted() {

  }
}
</script>


<style scoped>
.aside {
  padding: 20px 0 0 20px;
}
</style>