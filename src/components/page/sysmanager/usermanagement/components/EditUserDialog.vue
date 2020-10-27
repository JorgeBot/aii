<!-- 编辑用户Modal -->
<template>
  <el-dialog
      title="修改信息"
      :visible.sync="visible"
      v-dialogDrag
      width="500px"
      @close="close"
  >
    <el-form :model="editUserForm"
             :rules="editRule"
             ref="editUserFormRef"
             v-loading="toggles.loading"
    >
      <el-form-item label="昵称" :label-width="formLabelWidth" prop="nickname">
        <el-input class="nicknameEdit"
                  v-model="editUserForm.nickname"
                  width="20"
                  autocomplete="off"
                  @change="toggles.submittable=false"
        >
        </el-input>
      </el-form-item>
      <el-form-item label="状态"
                    :label-width="formLabelWidth"
      >
        <el-radio v-model="editUserForm.status" label="正常" @change="toggles.submittable=false">正常</el-radio>
        <el-radio v-model="editUserForm.status" label="禁用" @change="toggles.submittable=false">禁用</el-radio>
      </el-form-item>
      <el-form-item label="角色" :label-width="formLabelWidth">
        <el-select
            v-model="selectedRoles"
            multiple
            filterable
            allow-create
            default-first-option
            placeholder="请添加角色"
            @change="toggles.submittable=false"
        >
          <el-option
              v-for="item in roleList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="项目" :label-width="formLabelWidth">
        <el-select
            v-model="selectedProjects"
            multiple
            filterable
            allow-create
            default-first-option
            placeholder="请添加项目"
            @change="toggles.submittable=false"
        >
          <el-option
              v-for="item in projectList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click.native="editUser" :disabled="toggles.submittable">确 定</el-button>
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
      editUserForm: {
        nickname: '',
        status: '',
        id: '',
        roleIds: [],
        relatedProjectIdArray: [],
      },
      roleList: [],
      projectList: [],
      selectedRoles: [],
      selectedProjects: [],
      editRule: {
        nickname: [
          {
            required: true,
            message: "请输入姓名",
            trigger: "blur"
          }
        ]
      },
      toggles: {
        submitButtonLoading: false,
        loading: false,
        getRoleListLoading: false,
        getProjectListLoading: false,
        submittable: true,
      },
      formLabelWidth: "120px",
    }
  },
  methods: {
    editUser() {
      this.$refs['editUserFormRef'].validate((valid) => {
        if (valid) {
          let params = this.editUserForm
          params.roleIds = this.selectedRoles
          params.relatedProjectIdArray = this.selectedProjects
          axios.patch(`user/${params.id}`, qs.stringify(params)).then(() => {
            this.$message({
              type: "success",
              message: '更新成功'
            })
            this.close()
            this.flushTable()
          })
        }
      });
    },

    getRoleList() {
      if (this.roleList.length === 0) {
        this.toggles.loading = true
        this.toggles.getRoleListLoading = true
        axios.get(`roles`).then(res => {
          res.data.forEach(e => {
            this.roleList.push({label: e.roleName, value: e.id})
          })
        }).catch(err => {
          this.$message.error(`获取角色列表失败: ${err.response.data}`)
        }).finally(() => {
          this.toggles.getRoleListLoading = false
          this.flushLoading()
        })
      }
      this.selectedRoles = this.editUserForm.roleIds || []
    },

    getProjectList() {
      if (this.projectList.length === 0) {
        this.toggles.loading = true
        this.toggles.getProjectListLoading = true
        axios.get(`projects`).then(res => {
          res.data.forEach(e => {
            this.projectList.push({label: e.projectName, value: e.id})
            this.selectedProjects = this.editUserForm.relatedProjectIdArray || []
          })
        }).catch(err => {
          this.$message.error(`获取项目列表失败: ${err.response.data}`)
        }).finally(() => {
          this.toggles.getProjectListLoading = false
          this.flushLoading()
        })
      }
      this.selectedProjects = this.editUserForm.relatedProjectIdArray || []
    },

    flushLoading() {
      this.toggles.loading = this.toggles.getProjectListLoading || this.toggles.getRoleListLoading
    },

    initEditUserForm() {
      this.editUserForm = {
        nickname: '',
        status: '',
        id: '',
        roleIds: [],
      }
    },

    close() {
      this.visible = false
      this.initEditUserForm()
    },

    flushTable() {
      bus.$emit('query')
    },
  },

  mounted() {
  },

  created() {
    bus.$on('openEditUserDialog', editUserSource => {
      this.visible = true
      this.toggles.submittable = true
      this.editUserForm = editUserSource
      this.getRoleList()
      this.getProjectList()
    })
  }
}
</script>

<style scoped>
.nicknameEdit {
  width: 60%;
}
</style>

