<!--配置角色-->
<template>
  <el-dialog
      title="配置角色"
      :visible.sync="visible"
      v-dialogDrag
      width="250px"
  >
    <el-table
        ref="rolesTableRef"
        :data="roleTable"
        tooltip-effect="dark"
        max-height="400"
    >
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column
          prop="roleName"
          label="角色名:"
          width="120">
      </el-table-column>
    </el-table>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary"
                 @click.native="relateUsersAndRoles"
                 :loading="toggles.submitButtonLoading"
      >确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>

import axios from "@/api/api";
import qs from 'qs'
import bus from '@/components/page/sysmanager/usermanagement/bus'

export default {
  props: {
  },
  data() {
    return {
      checkUserIds: [],
      visible: false,
      roleTable: [
        {
          id: 0,
          roleName: '',
        }
      ],
      toggles: {
        submitButtonLoading: false,
      },
      formLabelWidth: "120px",
    }
  },
  methods: {
    relateUsersAndRoles() {
      let roleIds = this.$refs.rolesTableRef.selection.map(m => m.id)
      this.toggles.submitButtonLoading = true
      axios.post(`/relatedUserRole`, qs.stringify({userIds: this.checkUserIds, roleIds: roleIds})).then(() => {
        this.$message({
          type: "success",
          message: "配置成功"
        })
        this.close()
        this.flushTable()
      }).catch(err => {
        this.$message.error(`配置失败: ${err.response.data}`)
      }).finally(() => {
        this.toggles.submitButtonLoading = false
      })
    },
    close() {
      this.visible = false
    },
    getCheckedUserIds() {
      return bus.$emit('getCheckedUserIds', )
    },
    openConfigRoleDialog(checkedUserIds) {
      if (checkedUserIds.length === 0) {
        this.$message({
          type: "warning",
          message: '请先选中用户'
        })
        return
      }
      this.checkUserIds = checkedUserIds
      axios.get(`roles`).then(res => {
        this.roleTable = res.data
      })
      this.visible = true
    },
    flushTable() {
      bus.$emit('query')
    }
  },
  mounted() {
  },
  created() {
  }
}
</script>

<style scoped>
</style>

