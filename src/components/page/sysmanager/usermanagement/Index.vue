<template>
  <div>
    <el-container class="container">
     <el-main>
       <OperateBar :permission="permission"
                   @openConfigRoleDialog="openConfigRoleDialog"
       ></OperateBar>
       <UserTable ref="userTableRef"
                  :permission="permission"
       ></UserTable>
     </el-main>
    </el-container>
    <CreateUserDialog></CreateUserDialog>
    <EditUserDialog></EditUserDialog>
    <ConfigRoleDialog ref="configRoleDialogRef"
    ></ConfigRoleDialog>
  </div>
</template>
<script>

import domain from '@/../public/domain'
import CreateUserDialog from "@/components/page/sysmanager/usermanagement/components/CreateUserDialog";
import EditUserDialog from "@/components/page/sysmanager/usermanagement/components/EditUserDialog";
import ConfigRoleDialog from "@/components/page/sysmanager/usermanagement/components/ConfigRoleDialog";
import OperateBar from "@/components/page/sysmanager/usermanagement/components/OperateBar";
import UserTable from "@/components/page/sysmanager/usermanagement/components/UserTable";

export default {
  name: 'UserManagement',
  components: {
    CreateUserDialog,
    EditUserDialog,
    ConfigRoleDialog,
    OperateBar,
    UserTable,
  },
  data() {
    return {
      queryNameLike: '',
      permission: {
        createUser: false,
        configRole: false,
        editUser: false,
      },
    }
  },
  methods: {
    handleDelete(index, row) {
      let params = {
        userid: row.userid
      };
      this.$confirm("确认删除该用户？", "提示", {
        type: "warning"
      }).then(() => {
        getDeleteOne(params).then(() => {
          this.$message({
            type: "success",
            message: "删除成功"
          });
        });
      });
    },

    openConfigRoleDialog() {
      let checkedUserIds = this.$refs.userTableRef.getCheckedUserIds()
      this.$refs.configRoleDialogRef.openConfigRoleDialog(checkedUserIds)
    },

    getResources() {
      let resources = new Set(JSON.parse(localStorage.getItem("ms_resources")))
      if (domain.permission) {
        this.permission.create = !resources.has("新建用户")
        this.permission.operate = !resources.has("用户操作")
      }
    }
  },
  mounted() {
    this.getResources()
  }
};
</script>
<style scoped>

</style>

