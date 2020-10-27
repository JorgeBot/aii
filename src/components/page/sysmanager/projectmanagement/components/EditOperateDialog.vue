<!-- 新建 -->
<template>
  <!-- 编辑 -->
  <el-dialog title="修改信息" :visible.sync="toggles.visible" v-dialogDrag width="30%">
    <el-form :model="editForm" :rules="projectRule" ref="editProjectFormRef">
      <el-form-item label="项目名" :label-width="formLabelWidth" prop="username">
        <el-input class="project-input"
                  v-model="editForm.projectName"
                  autocomplete="off"
                  @change="toggles.submittable=false"
        ></el-input>
      </el-form-item>
      <el-form-item label="状态" :label-width="formLabelWidth">
        <el-radio v-model="editForm.status" label="正常" @change="toggles.submittable=false">正常</el-radio>
        <el-radio v-model="editForm.status" label="禁用" @change="toggles.submittable=false">禁用</el-radio>
      </el-form-item>
      <el-form-item class="project-input" label="描述" :label-width="formLabelWidth" prop="username">
        <el-input type="textarea"
                  :rows="3"
                  v-model="editForm.description"
                  autocomplete="off"
                  @change="toggles.submittable=false"
        ></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary"
                 @click="patchProject"
                 :disabled="toggles.submittable"
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
      editForm: {
        projectName: "",
        description: "",
        status: "",
        id: ""
      },
      projectRule: {
        projectName: [
          {required: true, message: "请输入项目名", trigger: "blur"}
        ],
      },
      toggles: {
        visible: false,
        submittable: true,
      },
      formLabelWidth: "120px",
    }
  },
  methods: {
    patchProject() {
      this.$refs['editProjectFormRef'].validate((valid) => {
        if (valid) {
          axios.patch(`project/${this.editForm.id}`, qs.stringify(this.editForm)).then(() => {
            this.flushTable()
            this.editForm = {};
            this.$message({
              type: "success",
              message: '更新成功'
            });
          }).catch(err => {
            this.$message.error(`更新失败: ${err.response.data}`)
          }).finally(() => {
            this.toggles.visible = false
          })
        }
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
    bus.$on('openEditOperateDialog', editSource => {
      this.toggles.visible = true
      this.toggles.submittable = true
      this.editForm = editSource
    })
  }
}
</script>

<style scoped>
.project-input {
  width: 70%;
}
</style>

