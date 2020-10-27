<!--项目树-->
<template>
  <div>
    <el-table
        :data="projectTable.records"
        border
        show-overflow-tooltip
        class="table"
        ref="usersTableRef"
        height="455px"
        v-loading="toggles.tableLoading"
    >
      <el-table-column type="selection" min-width="55" align="center"></el-table-column>
      <el-table-column prop="orderNum" label="序号" type="index" min-width="70"></el-table-column>
      <el-table-column prop="projectName" label="项目名" min-width="120"></el-table-column>
      <el-table-column prop="createTime" :formatter="dateFormatter" label="创建时间" min-width="120"></el-table-column>
      <el-table-column prop="description" label="描述" min-width="200"></el-table-column>
      <el-table-column prop="status" label="状态" min-width="100"></el-table-column>
      <el-table-column label="操作" width="260" align="center">
        <template slot-scope="scope">
          <el-button
              type="text"
              icon="el-icon-lx-apps"
              class="yellow"
              @click="openModuleOperateDialog(scope.$index, scope.row)"
              :disabled="permission.module"
          >模块
          </el-button>
          <el-button
              type="text"
              icon="el-icon-lx-people"
              class="blue"
              @click="openRelatedUsersDialog(scope.$index, scope.row)"
              :disabled="permission.relatedUser"
          >人员
          </el-button>
          <el-button
              type="text"
              icon="el-icon-edit"
              @click="openEditOperateDialog(scope.$index, scope.row)"
              :disabled="permission.operate"
          >编辑
          </el-button>
          <el-button
              type="text"
              icon="el-icon-delete"
              class="red"
              @click="handleDelete(scope.$index, scope.row)"
              :disabled="permission.operate"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
          @current-change="currentChange"
          @size-change="sizeChange"
          :total="projectTable.total"
          :page-size="projectTable.size"
          :page-sizes="[10, 20, 50, 100]"
          :current-page="projectTable.current"
          layout="total, sizes, prev, pager, next"
          background
      ></el-pagination>
    </div>
  </div>
</template>
<script>

import axios from "@/api/api";
import bus from "@/components/page/sysmanager/projectmanagement/bus"

export default {
  props: {
    permission: {
      module: false,
      relatedUser: false,
      operate: false,
    },
  },
  data() {
    return {
      queryLike: '',
      projectTable: {
        records: [],
        total: 0,
        current: 1,
        size: 10,
      },
      toggles: {
        tableLoading: false,
      },
    }
  },
  methods: {
    flushTable() {
      let params = {
        projectNameLike: this.queryLike,
        current: this.projectTable.current,
        size: this.projectTable.size,
      };
      this.toggles.tableLoading = true
      axios.get(`projectPage`, {params: params}).then(res => {
        this.toggles.tableLoading = true;
        this.projectTable = res.data;
      }).catch(err => {
        this.$message.error(`加载出错:${err.response.data}`);
      }).finally(() => {
        this.asyncQueryLoaded()
        this.toggles.tableLoading = false
      })
    },

    asyncQueryLoaded() {
      bus.$emit('asyncQueryLoaded')
    },

    currentChange(val) {
      this.projectTable.current = val;
      this.flushTable()
    },

    sizeChange(val) {
      this.projectTable.size = val
      this.flushTable()
    },

    dateFormatter(row, column, cellValue, index) {
      return cellValue.split(" ")[0]
    },

    openModuleOperateDialog(index, row) {
      bus.$emit("openModuleOperateDialog", row.id)
    },

    openRelatedUsersDialog(index, row) {
      bus.$emit("openRelatedUsersDialog", row)
    },

    openEditOperateDialog(index, row) {
      bus.$emit("openEditOperateDialog", Object.assign({}, row))
    },

    handleDelete(index, row) {
      let params = {
        userid: row.userid
      };
      this.$confirm("删除项目会删除该项目下所以数据，确认删除？", "提示", {
        type: "warning"
      }).then(() => {
        axios.delete(`project/${row.id}`).then(() => {
          this.tableLoading = true
          this.$message({
            type: "success",
            message: "删除成功"
          });
          this.projectTable.current = 1
          this.flushTable();
        }).catch(err => {
          this.$message.error(`删除失败:${err.response.data}`);
        }).finally(() => {
          this.tableLoading = false
        })
      });
    },
  },
  mounted() {
    this.flushTable()
  },
  created() {
    bus.$on('query', queryLike => {
      if (queryLike !== undefined) {
        this.queryLike = queryLike
      }
      this.flushTable()
    })
  }
}
</script>

<style scoped>
.table {
  width: 100%;
  font-size: 14px;
}

.red {
  color: #ff0000;
}

.blue {
  color: #2d8cf0;
}

.yellow {
  color: #07c4a8;
}
</style>

