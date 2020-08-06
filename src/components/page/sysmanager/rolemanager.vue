<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>角色管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container" v-loading="toggles.deleteContainerLoading">
      <div class="handle-box">
        <el-button type="primary" @click="toggles.addRoleVisible=true" v-bind:disabled="permission.createRole">新建角色
        </el-button>
        <el-button type="primary" :loading="toggles.saveButtonLoading" @click="patchMenusAndResources"
                   v-bind:disabled="permission.save">保存
        </el-button>
      </div>
      <div class="content">
        <!-- 角色列表-->
        <el-table
            :data="roles.data"
            border
            highlight-current-row
            class="roles"
            ref="multipleTable"
            max-height="425"
            @row-click="getMenus"
        >
          <el-table-column prop="roleName" label="角色名" width="177" align="center"></el-table-column>
          <el-table-column label="操作" width="120" align="center">
            <template slot-scope="scope">
              <el-button
                  type="text"
                  icon="el-icon-edit"
                  @click="patchRole(scope.$index, scope.row)"
                  v-bind:disabled="permission.edit"
              >编辑
              </el-button>
              <el-button
                  type="text"
                  icon="el-icon-delete"
                  class="red"
                  @click="deleteRole(scope.$index, scope.row)"
              >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <!-- 菜单树-->
        <div class="menus">
          <h6>菜单</h6>
          <el-tree
              :data="menus.data"
              show-checkbox
              default-expand-all
              @node-click="getResourceList"
              node-key="name"
              ref="menu"
              @check-change="clearResourcesChecked"
              :props="defaultProps">
          </el-tree>
        </div>
        <!-- 资源树-->
        <div class="resources">
          <h6>资源</h6>
          <el-tree
              :data="resources.data"
              default-expand-all
              check-on-click-node
              show-checkbox
              node-key="name"
              ref="resource"
              :default-checked-keys="resources.checked"
              @check-change="setResourceMp"
              :props="defaultProps">
          </el-tree>
        </div>

      </div>
      <!-- 新增modal-->
      <el-dialog title="新建角色" :visible.sync="toggles.addRoleVisible" ref="addUserForm" v-dialogDrag width="300">
        <el-form :model="roleForm" :rules="roleFormRule">
          <el-form-item label="角色名" prop="username">
            <el-input v-model="roleForm.roleName" autocomplete="off"></el-input>
          </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="toggles.addRoleVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveRole" :loading="toggles.postButtonLoading">确 定</el-button>
        </div>
      </el-dialog>
      <!-- 修改modal-->
      <el-dialog
          title="修改信息"
          :visible.sync="toggles.editRoleVisible"
          ref="editUserForm"
          v-dialogDrag
      >
        <el-form :model="editRoleForm" :rules="editRoleRule" ref="editUserForm">
          <el-form-item label="角色名" :label-width="300" prop="username">
            <el-input v-model="editRoleForm.roleName" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="toggles.editRoleVisible=false">取 消</el-button>
          <el-button type="primary" @click="editRole" :loading="toggles.patchButtonLoading">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import axios from 'axios'
import domain from '../../../../public/domain'
import qs from 'qs'

export default {
  data() {
    return {
      permission: {
        createRole: false,
        save: false,
        edit: false,
        delete: false
      },
      roles: {
        data: [],
        clicked: '',
      },
      menus: {
        data: [],
      },
      resources: {
        data: [],
        checked: []
      },
      roleFormRule: {
        roleName: [
          {required: true, message: "请输入角色名", trigger: "blur"}
        ],
      },
      editRoleRule: {
        roleName: [
          {required: true, message: "请输入角色名", trigger: "blur"}
        ],
      },
      roleForm: {
        roleName: '',
      },
      editRoleForm: {
        roleName: '',
      },
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      toggles: {
        addRoleVisible: false,
        editRoleVisible: false,
        saveButtonLoading: false,
        postButtonLoading: false,
        patchButtonLoading: false,
        deleteContainerLoading: false,
      }
    };
  }
  ,
  methods: {
    getRoles() {
      axios.get('roles').then(res => {
        this.roles.data = res.data
      })
    },
    getMenus(row, col, env) {
      if (col.label === "角色名") {
        this.roles.clicked = row
        this.resources.data = []
        this.menus.data = domain.menu
        if (row.menus) {
          this.$refs.menu.setCheckedKeys(row.menus);
        } else {
          this.$refs.menu.setCheckedKeys([]);
        }
      }
    },
    getResourceList(obj, node, self) {
      this.resources.data = []
      // 获取资源
      axios.get(`/resources`, {params: {menuName: obj.name}}).then(res => {
        let data = res.data
        if (data) {
          data.forEach(e => {
            this.resources.data.push({name: e})
          })
        }
        return data
      }).then(res => {    // 获取选中资源
        if (res && node.checked) {
          let checkedNode = res.filter(f => this.roles.clicked.resources.some(e => e === f));
          this.$refs.resource.setCheckedKeys(checkedNode);
        }
      })
    },
    saveRole() {
      this.toggles.postButtonLoading = true
      axios.post('role', qs.stringify(this.roleForm)).then(res => {
        this.$message({
          message: '保存成功',
          type: 'success'
        });
        this.toggles.addRoleVisible = false
        this.getRoles()
      }).catch(err => {
        this.$message.error('保存失败')
      }).finally(() => {
        this.roleForm.roleName = ''
        this.toggles.postButtonLoading = false
      })
    },
    patchMenusAndResources() {
      let menus = this.$refs.menu.getCheckedKeys()
      let resources = this.roles.clicked.resources
      this.toggles.saveButtonLoading = true
      axios.patch(`role/${this.roles.clicked.id}`, qs.stringify({
        menus: menus,
        resources: resources
      })).then(res => {
        this.toggles.saveButtonLoading = false
        this.getRoles()
        this.$alert('保存成功', {confirmButtonText: '确定'});
      })
    },
    patchRole(index, row) {
      this.toggles.editRoleVisible = true;
      this.editRoleForm = Object.assign({}, row);
    },
    deleteRole(index, row) {
      this.toggles.deleteContainerLoading = true
      axios.delete(`role/${row.id}`).then(() => {
        this.$message({
          message: '删除成功',
          type: 'success'
        });
        this.getRoles()
      }).catch(() => {
        this.$message.error('删除失败')
      }).finally(() => {
        this.toggles.deleteContainerLoading = false
      })
    },
    editRole() {
      this.toggles.patchButtonLoading = true
      axios.patch(`role/${this.editRoleForm.id}`, qs.stringify(this.editRoleForm)).then(res => {
        this.$message({
          message: '修改成功',
          type: 'success'
        });
        this.getRoles()
      }).catch(err => {
        this.$message.error('修改失败')
      }).finally(() => {
        this.toggles.patchButtonLoading = false
        this.toggles.editRoleVisible = false
      })
    },
    setResourceMp(data, isAdd) {
      if (!new Set(this.$refs.menu.getCheckedKeys()).has(this.$refs.menu.getCurrentKey())) {
        this.$refs.resource.setCheckedKeys([])
      }
      let resourceSet = new Set(this.roles.clicked.resources);
      if (isAdd) {
        resourceSet.add(data.name)
      } else {
        resourceSet.delete(data.name)
      }
      this.roles.clicked.resources = Array.from(resourceSet)
    },
    /**
     * 菜单取消勾选时，同时取消资源的勾选
     * @param menu
     * @param checked
     */
    clearResourcesChecked(menu, checked) {
      if (!checked) {
        if (this.$refs.menu.getCurrentKey() === menu.name) {
          this.$refs.resource.setCheckedKeys([])
        }
      }
    },
    getResources() {
      let resources = new Set(JSON.parse(localStorage.getItem("ms_resources")))
      if (domain.permission) {
        this.permission.createRole = !resources.has("新建角色")
        this.permission.edit = !resources.has("编辑角色")
        this.permission.save = !resources.has("保存权限")
      }
    },
  },
  mounted() {
    this.getRoles();
    this.getResources()
  }
}
;
</script>
<style scoped>
.handle-box {
  margin-bottom: 20px;
}

.container {
  height: 500px;
}

.content {
  display: flex;
  justify-content: space-between;
  flex-wrap: nowrap;
  overflow-y: auto;
  overflow-x: hidden;
}

.roles {
  flex: 0 0 298px;
}

.menus {
  border-style: solid;
  border-width: 2px;
  border-color: #f1f3f8;
  height: 425px;
  flex: 0 0 300px;
}

.resources {
  border-style: solid;
  border-width: 2px;
  border-color: #f1f3f8;
  height: 425px;
  flex: 0 0 300px;
}

h6 {
  padding: 8px;
  font-size: 12px;
  color: #969ba6;
  width: 300px;
  border-bottom-style: solid;
  border-width: 2px;
  border-color: #f1f3f8;
  margin-bottom: 10px;
  text-align: center;
}

/deep/ .el-table__body-wrapper::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

/deep/ .el-table__body-wrapper::-webkit-scrollbar-thumb {
  background-color: #ddd;
  border-radius: 3px;
}
</style>

