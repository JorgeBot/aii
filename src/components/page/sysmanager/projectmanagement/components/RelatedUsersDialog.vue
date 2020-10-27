<!--关联人员-->
<template>
  <el-dialog title="关联人员"
             :visible.sync="toggles.visible"

             v-dialogDrag
             width="550px"
  >
    <el-transfer v-model="relatedUsers.value"
                 v-loading="toggles.relatedUsersLoading"
                 :data="relatedUsers.data"
                 :props="customProps"
                 :titles="['未选人员', '已选人员']"
                 @change="toggles.submittable=false"
    ></el-transfer>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary"
                 @click="patchProject"
                 :loading="toggles.submitButtonLoading"
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
  props: {},
  data() {
    return {
      projectId: 0,
      relatedUsers: {
        data: [],
        value: [],
      },
      toggles: {
        visible: false,
        submitButtonLoading: false,
        relatedUsersLoading: false,
        submittable: true,
      },
      customProps: {
        key: 'id',
        label: 'nickname',
        value: 'id',
      }
    }
  },
  methods: {
    getAllUsers(row) {
      if (this.relatedUsers.data.length === 0) {
        this.toggles.relatedUsersLoading = true;
        axios.get(`/users`).then(res => {
          this.relatedUsers.data = res.data || []
        }).catch(err => {
          this.$message.error(`获取角色列表失败: ${err.response.data}`)
        }).finally(() => {
          this.toggles.relatedUsersLoading = false
        });
      }
      this.relatedUsers.value = row.relatedUserIdArray || []
    },
    patchProject() {
      let param = {
        relatedUserIdArray: this.relatedUsers.value,
      }
      this.toggles.submitButtonLoading = true
      axios.patch(`project/${this.projectId}/user/relation`, qs.stringify(param)).then(() => {
        this.$message({
          type: "success",
          message: "关联成功"
        })
        this.close()
        this.flushTable()
      }).catch(err => {
        this.$message.error(`关联失败: ${err.response.data}`)
      }).finally(() => {
        this.toggles.submitButtonLoading = false
      })
    },
    close() {
      this.toggles.visible = false
    },
    flushTable() {
      bus.$emit('query')
    }
  },
  mounted() {
  },
  created() {
    bus.$on('openRelatedUsersDialog', row => {
      this.toggles.visible = true
      this.toggles.submittable = true
      this.projectId = row.id
      this.getAllUsers(row)
    })
  }
}
</script>

<style scoped>
</style>

