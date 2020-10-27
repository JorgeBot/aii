<!-- 编辑modal窗口-->
<template>
  <el-dialog title="修改信息"
             :visible.sync="visible"
             v-dialogDrag
             @close="close"
  >
    <el-form :model="editRoleForm" :rules="editRoleRule">
      <el-form-item label="角色名" prop="username">
        <el-input v-model="editRoleForm.roleName" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="editRole" :loading="toggles.submitButtonLoading">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>

import axios from "@/api/api";
import qs from 'qs'
import bus from '@/components/page/sysmanager/rolemanagement/bus'

export default {
  props: {},
  data() {
    return {
      visible: false,
      editRoleForm: {
        roleName: '',
      },
      editRoleRule: {
        roleName: [
          {required: true, message: "请输入角色名", trigger: "blur"}
        ],
      },
      toggles: {
        submitButtonLoading: false,
      },
    };
  },
  methods: {
    editRole() {
      this.toggles.submitButtonLoading = true
      axios.patch(`role/${this.editRoleForm.id}`, qs.stringify(this.editRoleForm)).then(() => {
        this.$message({
          message: '修改成功',
          type: 'success'
        })
        this.close()
        this.flushRoles()
      }).catch(err => {
        this.$message.error(`修改失败: ${err.response.data}`)
      }).finally(() => {
        this.toggles.submitButtonLoading = false
      })
    },
    flushRoles() {
      this.$emit('flushRoles')
    },
    close() {
      this.visible = false
    },
  },
  mounted() {
  },
  created() {
    bus.$on('openEditRoleDialog', editRoleSource => {
      this.visible = true
      this.editRoleForm = editRoleSource
    })
  }
}
</script>

<style scoped>
</style>

