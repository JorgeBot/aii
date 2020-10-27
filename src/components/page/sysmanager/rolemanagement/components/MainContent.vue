<!--页面展示内容-->
<template>
  <div class="content">
    <!-- 角色列表-->
    <el-table
        :data="rolesTable.records"
        v-loading="toggles.roleLoading"
        border
        highlight-current-row
        class="roles"
        max-height="425"
        @row-click="getMenusTable"
    >
      <el-table-column prop="roleName" label="角色名" width="177" align="center"></el-table-column>
      <el-table-column label="操作" width="120" align="center">
        <template slot-scope="scope">
          <el-button
              type="text"
              icon="el-icon-edit"
              @click="openEditRoleDialog(scope.$index, scope.row)"
              :disabled="permission.operate"
          >编辑
          </el-button>
          <el-button
              type="text"
              icon="el-icon-delete"
              class="red"
              @click="deleteRole(scope.$index, scope.row)"
              :disabled="permission.operate"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 菜单树-->
    <div class="menus">
      <h6>菜单</h6>
      <el-tree
          :data="menusTable.records"
          show-checkbox
          default-expand-all
          @node-click="getResourceList"
          node-key="name"
          ref="menuTreeRef"
          @check-change="clearResourcesChecked"
          :props="defaultProps">
      </el-tree>
    </div>
    <!-- 资源树-->
    <div class="resources">
      <h6>资源</h6>
      <el-tree
          :data="resourcesTable.records"
          default-expand-all
          check-on-click-node
          show-checkbox
          node-key="name"
          ref="resourceTreeRef"
          :default-checked-keys="resourcesTable.checked"
          @check-change="setResourceMp"
          :props="defaultProps">
      </el-tree>
    </div>

  </div>
</template>
<script>

import axios from "@/api/api";
import qs from 'qs'
import domain from "@/../public/domain";
import bus from '@/components/page/sysmanager/rolemanagement/bus'

export default {
  props: {
    permission: {
      operate: false,
    },
  },
  data() {
    return {
      menusTable: {
        records: [],
      },
      rolesTable: {
        records: [],
        clicked: [],
      },
      resourcesTable: {
        records: [],
        checked: [],
        init: function () {
          this.records = []
          this.checked = []
        }
      },
      toggles: {
        roleLoading: false,
      },
      defaultProps: {
        children: 'children',
        label: 'name',
      },
    }
  },
  methods: {
    openEditRoleDialog(index, row) {
      bus.$emit('openEditRoleDialog', row)
    },

    deleteRole(index, row) {
      this.toggles.roleLoading = true
      axios.delete(`role/${row.id}`).then(() => {
        this.$message({
          message: '删除成功',
          type: 'success'
        });
        this.getRolesTable()
      }).catch(() => {
        this.$message.error('删除失败')
      }).finally(() => {
        this.toggles.roleLoading = false
      })
    },

    getRolesTable() {
      this.toggles.roleLoading = true
      axios.get(`roles`).then(res => {
        this.rolesTable.records = res.data
      }).catch(err => {
        this.$message.error(`获取角色列表失败: ${err.response.data}`)
      }).finally(() => {
        this.toggles.roleLoading = false

      });
    },

    getMenusTable(row, col, env) {
      if (col.label === "角色名") {
        this.rolesTable.clicked = row
        this.resourcesTable.init()
        this.menusTable.records = domain.menu
        if (row.menus) {
          this.$refs.menuTreeRef.setCheckedKeys(row.menus);
        } else {
          this.$refs.menuTreeRef.setCheckedKeys([]);
        }
      }
    },

    getResourceList(obj, node, self) {
      this.resourcesTable.init()
      // 获取资源
      axios.get(`resources`, {params: {menuName: obj.name}}).then(res => {
        let data = res.data
        if (data) {
          data.forEach(e => {
            this.resourcesTable.records.push({name: e})
          })
        }
        return data
      }).then(res => {    // 获取选中资源
        if (res && node.checked) {
          let checkedNode = res.filter(f => this.rolesTable.clicked.resources.some(e => e === f));
          this.$refs.resourceTreeRef.setCheckedKeys(checkedNode);
        }
      })
    },

    /**
     * 菜单取消勾选时，同时取消资源的勾选
     * @param menu
     * @param checked
     */
    clearResourcesChecked(menu, checked) {
      if (!checked) {
        if (this.$refs.menuTreeRef.getCurrentKey() === menu.name) {
          this.$refs.resourceTreeRef.setCheckedKeys([])
        }
      }
    },

    setResourceMp(data, isAdd) {
      if (!new Set(this.$refs.menuTreeRef.getCheckedKeys()).has(this.$refs.menuTreeRef.getCurrentKey())) {
        this.$refs.resourceTreeRef.setCheckedKeys([])
      }
      let resourceSet = new Set(this.rolesTable.clicked.resources);
      if (isAdd) {
        resourceSet.add(data.name)
      } else {
        resourceSet.delete(data.name)
      }
      this.rolesTable.clicked.resources = Array.from(resourceSet)
    },

    patchMenusAndResources() {
      let menus = this.$refs.menuTreeRef.getCheckedKeys()
      let resources = this.rolesTable.clicked.resources
      axios.patch(`role/${this.rolesTable.clicked.id}`, qs.stringify({
        menus: menus,
        resources: resources
      })).then(res => {
        this.asyncSaveLoaded()
        this.getRolesTable()
        this.$alert('保存成功', {confirmButtonText: '确定'});
      })
    },

    asyncSaveLoaded() {
      bus.$emit('asyncSaveLoaded')
    }
  },
  mounted() {
    this.getRolesTable()
  },
  created() {
    bus.$on('patchMenusAndResources', () => {
      this.patchMenusAndResources()
    })
    bus.$on('flushRoles', () => {
      this.getRolesTable()
    })
  }
}
</script>

<style scoped>
.content {
  display: flex;
  justify-content: space-between;
  height: 455px;
}
.content > div {
  margin-right: 20px;
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
</style>

