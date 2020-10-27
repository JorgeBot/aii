<!-- 新增modal窗口-->
<template>
  <el-dialog title="新建角色"
             :visible.sync="visible"
             v-dialogDrag
             width="300"
             @close="close"
  >
    <el-form :model="roleForm" :rules="roleFormRule">
      <el-form-item label="角色名" prop="username">
        <el-input v-model="roleForm.roleName" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary"
                 @click="saveRole"
                 :loading="toggles.submitButtonLoading"
      >确 定
      </el-button>
    </div>
  </el-dialog>
</template>
<script>

import axios from "@/api/api";
import qs from 'qs'
import bus from '@/components/page/sysmanager/rolemanagement/bus'

export default {
  props: {

  },
  data() {
    return {
      visible: false,
      roleFormRule: {
        roleName: [
          {required: true, message: "请输入角色名", trigger: "blur"}
        ],
      },
      roleForm: {
        roleName: '',
        init: function () {
          this.roleName = ''
        }
      },
      toggles: {
        submitButtonLoading: false,
      }
    };
  },
  methods: {
    saveRole() {
      this.toggles.submitButtonLoading = true
      axios.post('role', qs.stringify(this.roleForm)).then(() => {
        this.$message({
          message: '保存成功',
          type: 'success'
        })
        this.close()
        this.flushRoles()
      }).catch(err => {
        this.$message1
      }).finally(() => {
        this.roleForm.init()
        this.toggles.submitButtonLoading = false
      })
    },
    close() {
      this.visible = false
    },
    flushRoles() {
      bus.$emit('flushRoles')
    }
  },
  mounted() {
  },
  created() {
    bus.$on('openCreateRoleDialog', () => {
      this.visible = true
    })
  }
}
</script>

<style scoped>
</style>

