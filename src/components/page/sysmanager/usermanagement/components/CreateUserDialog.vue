<!-- 新建用户Modal -->
<template>
  <el-dialog title="新建用户"
             :visible.sync="visible"
             ref="addUserFormRef"
             v-dialogDrag
             @close="close"
  >
    <el-form :model="createUserForm" :rules="createUserRule">
      <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
        <el-input v-model="createUserForm.username" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="昵称" :label-width="formLabelWidth" prop="nickname">
        <el-input v-model="createUserForm.nickname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="密码" :label-width="formLabelWidth" prop="password">
        <el-input v-model="createUserForm.password" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="性别" :label-width="formLabelWidth">
        <el-radio v-model="createUserForm.sex" label="男">男</el-radio>
        <el-radio v-model="createUserForm.sex" label="女">女</el-radio>
      </el-form-item>
      <el-form-item label="状态" :label-width="formLabelWidth">
        <el-radio v-model="createUserForm.status" label="正常">正常</el-radio>
        <el-radio v-model="createUserForm.status" label="禁用">禁用</el-radio>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary"
                 @click="createUser"
                 :loading="toggles.submitButtonLoading"
      >确 定
      </el-button>
    </div>
  </el-dialog>
</template>
<script>

import axios from "@/api/api";
import qs from 'qs'
import bus from '@/components/page/sysmanager/usermanagement/bus'

export default {
  props: {},
  data() {
    return {
      visible: false,
      createUserForm: {
        username: "",
        nickname: "",
        password: "123456aA",
        status: "正常",
        sex: "男"
      },
      createUserRule: {
        username: [
          {required: true, message: "请输入用户名", trigger: "blur"}
        ],
        nickname: [
          {
            required: true,
            message: "请输入昵称",
            trigger: "blur"
          }
        ],
        password: [
          {
            required: true,
            message: "请输入密码",
            trigger: "blur"
          }
        ],
      },
      toggles: {
        submitButtonLoading: false,
      },
      formLabelWidth: "120px",
    }
  },
  methods: {
    createUser() {
      let params = Object.assign({}, this.createUserForm)
      params.username = params.username.trim()
      this.toggles.submitButtonLoading = true
      axios.post(`user`, qs.stringify(params)).then(() => {
        this.$message({
          message: "添加成功",
          type: "success"
        })
        this.visible = false
        this.initCreateUserForm()
        this.flushTable()
      }).catch(err => {
        this.$message.error(`创建用户失败: ${err.response.data}`)
      }).finally(() => {
        this.toggles.submitButtonLoading = false
      })
    },
    initCreateUserForm() {
      this.createUserForm = {
        username: "",
        nickname: "",
        password: "123456aA",
        status: "正常",
        sex: "男",
      }
    },
    close() {
      this.visible = false
    },
    flushTable() {
      bus.$emit('query')
    }
  },
  mounted() {
  },
  created() {
    bus.$on('openCreateUserDialog', () => {
      this.visible = true
    })
  }
}
</script>

<style scoped>
</style>

