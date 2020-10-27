<!--模块操作-->
<template>
  <div>
    <el-dialog title="模块操作"
               :visible.sync="toggles.visible"
               v-dialogDrag
               width="500px"
               @close="close"
    >
      <el-tree
          :data="moduleTree.data"
          :props="treeProps"
          node-key="id"
          default-expand-all
          :expand-on-click-node="false"
          v-loading="toggles.moduleTreeLoading"
      >
            <span class="custom-tree-node" slot-scope="{ node, data }">
              <span>{{ node.label }}</span>
              <span>
                <el-button
                    type="text"
                    size="mini"
                    @click="() => append(data)"
                >添加
                </el-button>
                <el-button
                    type="text"
                    size="mini"
                    @click="() => edit(data)"
                >修改
                </el-button>
                <el-button
                    type="text"
                    size="mini"
                    @click="() => remove(node, data)"
                >删除
                </el-button>
              </span>
            </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="toggles.visible=false">取 消</el-button>
        <el-button type="primary" @click="close">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="新增模块"
               :visible.sync="toggles.createVisible"
               v-dialogDrag
               width="350px"
               class="in-dialog"
               height="30px"
    >
      <el-input class="in-input"
                placeholder="模块名称"
                v-model="moduleForm.moduleName"
      ></el-input>
      <el-button type="primary" @click.native="createModule">确定</el-button>
    </el-dialog>
    <el-dialog title="编辑模块"
               :visible.sync="toggles.editVisible"
               v-dialogDrag
               width="350px"
               class="in-dialog"
               height="30px"
    >
      <el-input class="in-input"
                placeholder="模块名称"
                v-model="moduleForm.moduleName"
      ></el-input>
      <el-button type="primary" @click.native="editModule">确定</el-button>
    </el-dialog>
  </div>
</template>
<script>

import axios from "@/api/api";
import qs from 'qs'
import bus from "@/components/page/sysmanager/projectmanagement/bus"

export default {
  props: {},
  data() {
    return {
      moduleTree: {
        data: [],
        init: function () {
          this.data = []
        }
      },
      moduleForm: {
        projectId: 0,
        moduleName: '',
        editForm: {
          id: 0,
        },
        createForm: {
          parentId: 0,
        },
        init: function () {
          this.moduleName = ''
          this.editForm.id = 0
          this.createForm.parentId = 0
        }
      },
      projectRule: {
        projectName: [
          {required: true, message: "请输入项目名", trigger: "blur"},
        ],
      },
      toggles: {
        visible: false,
        createVisible: false,
        editVisible: false,
        createButtonLoading: false,
        editButtonLoading: false,
        deleteButtonLoading: false,
        moduleTreeLoading: false,
      },
      treeProps: {
        label: 'name',
        children: 'children',
      },
      formLabelWidth: "120px",
    }
  },
  methods: {
    flushTable() {
      this.$emit('flushTable')
    },

    append(data) {
      this.toggles.createVisible = true
      this.moduleForm.createForm.parentId = data.id
    },

    createModule() {
      this.toggles.createButtonLoading = true
      let param = {
        projectId: this.moduleForm.projectId,
        moduleName: this.moduleForm.moduleName,
        parentId: this.moduleForm.createForm.parentId,
      }
      axios.post(`project/module`, qs.stringify(param)).then(res => {
        this.toggles.createVisible = false
        this.flushTree(this.moduleForm.projectId)
        this.moduleForm.init()
      }).catch(err => {
        this.$message.error(`创建模块失败: ${err.response.data}`)
      }).finally(() => {
        this.toggles.createButtonLoading = false
      })
    },

    edit(data) {
      this.toggles.editVisible = true
      this.moduleForm.editForm.id = data.id
      this.moduleForm.moduleName = data.moduleName
    },

    editModule() {
      this.toggles.editButtonLoading = true
      let param = {
        id: this.moduleForm.editForm.id,
        moduleName: this.moduleForm.moduleName,
      }
      axios.patch('project/module', qs.stringify(param)).then(res => {
        this.toggles.editVisible = false
        this.flushTree(this.moduleForm.projectId)
        this.moduleForm.init()
      }).catch(err => {
        this.$message.error(`编辑模块失败: ${err.response.data}`)
      }).finally(() => {
        this.toggles.editButtonLoading = false
      })
    },

    close() {
      this.moduleTree.init()
    },

    flushTree(projectId) {
      this.toggles.moduleTreeLoading = true
      axios.get(`project/${projectId}/modulesTree`).then(res => {
        this.moduleTree.data = res.data
      }).catch(err => {
        this.$message.error(`获取模块列表失败: ${err.response.data}`)
      }).finally(() => {
        this.toggles.moduleTreeLoading = false
      })
    }
  },

  mounted() {
  },
  created() {
    bus.$on('openModuleOperateDialog', projectId => {
      this.toggles.visible = true
      this.moduleForm.projectId = projectId
      this.flushTree(projectId)
    })
  }
}
</script>

<style scoped>
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.in-dialog {
  margin-top: 50px;
  display: inline-block;
  white-space: nowrap;
}

div {
  padding: 0;
}

.in-input {
  width: 75%;
  min-width: 20%;


  margin-right: 20px;
}
</style>

