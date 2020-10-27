<!-- 新建 -->
<template>
  <el-dialog title="新建项目" :visible.sync="toggles.visible" width="500px" v-dialogDrag>
    <el-form :model="createForm" :rules="projectRule">
      <el-form-item label="项目名" :label-width="formLabelWidth" prop="username">
        <el-input class="project-input" v-model="createForm.projectName" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="状态" :label-width="formLabelWidth">
        <el-radio v-model="createForm.status" label="正常">正常</el-radio>
        <el-radio v-model="createForm.status" label="禁用">禁用</el-radio>
      </el-form-item>
      <el-form-item class="project-input" label="描述" :label-width="formLabelWidth" prop="username">
        <el-input type="textarea" :rows="3" v-model="createForm.description" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary"
                 @click="saveProject"
                 :loading="toggles.submitButtonLoading"
      >确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>

import axios from "@/api/api";
import qs from 'qs'
import bus from "@/components/page/sysmanager/projectmanagement/bus"

export default {
  props: {
  },
  data() {
    return {
      createForm: {
        projectName: "",
        description: "",
        status: "正常",
        init: function () {
          this.projectName = ""
          this.description = ""
          this.status = "正常"
        }
      },
      projectRule: {
        projectName: [
          {required: true, message: "请输入项目名", trigger: "blur"}
        ],
      },
      toggles: {
        submitButtonLoading: false,
        createModuleVisible: false,
        visible: false,
      },
      formLabelWidth: "120px",
    }
  },
  methods: {
    saveProject() {
      let params = Object.assign({}, this.createForm);
      params.username = params.projectName.trim()
      this.toggles.submitButtonLoading = true
      axios.post(`project`, qs.stringify(params)).then(() => {
        this.$message({
          message: "添加成功",
          type: "success"
        })
        this.createForm.init()
        this.flushTable()
        this.close()
      }).catch(err => {
        this.$message.error(`创建项目失败；${err.response.data}`)
      }).finally(() => {
        this.toggles.submitButtonLoading = false
      })
    },
    
    flushTable() {
      bus.$emit('query')
    },

    close() {
      this.toggles.visible = false
    }
  },
  mounted() {
  },
  created() {
    bus.$on('openCreateProjectDialog', () => {
      this.toggles.visible = true
    })
  }
}
</script>

<style scoped>
.project-input {
  width: 70%;
}
</style>

