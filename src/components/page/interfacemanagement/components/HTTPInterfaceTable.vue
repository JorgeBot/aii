<!--项目树-->
<template>
  <div>
    <el-table
        :data="httpInterfaceTable.records"
        border
        class="table"
        height="455px"
        ref="interfaceTableRef"
        v-loading="toggles.tableLoading"
    >
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="orderNum" label="序号" type="index" width="120"></el-table-column>
      <el-table-column prop="interfaceName" label="接口名" min-width="180"></el-table-column>
      <el-table-column prop="host" label="HOST" min-width="320"></el-table-column>
      <el-table-column prop="port" label="端口号"></el-table-column>
      <el-table-column label="操作" width="150px" align="center">
        <template slot-scope="scope">
          <el-button
              type="text"
              icon="el-icon-edit"
              @click="editHTTPInterface(scope.$index, scope.row)"
              :disabled="permission.operate"
          >编辑
          </el-button>
          <el-button
              type="text"
              icon="el-icon-delete"
              :disabled="permission.operate"
              @click="handleDelete(scope.$index, scope.row)"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
          @current-change="currentChange"
          @size-change="sizeChange"
          :total="httpInterfaceTable.total"
          :page-size="httpInterfaceTable.size"
          :page-sizes="[10, 20, 50, 100]"
          :current-page="httpInterfaceTable.current"
          layout="total, sizes, prev, pager, next"
          background
      ></el-pagination>
    </div>
  </div>
</template>
<script>

import axios from "@/api/api";
import bus from "@/components/page/interfacemanagement/bus"

export default {
  props: {
    permission: {
      operate: false,
    },
  },
  data() {
    return {
      queryLike: '',
      moduleId: 0,
      httpInterfaceTable: {
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
        queryLike: this.queryLike,
        moduleId: this.moduleId,
        current: this.httpInterfaceTable.current,
        size: this.httpInterfaceTable.size,
      };
      this.toggles.tableLoading = true
      axios.get(`/httpInterfacePage`, {params: params}).then(res => {
        this.toggles.tableLoading = true;
        this.httpInterfaceTable = res.data;
      }).catch(err => {
        this.$message.error(`加载出错:${err.response.data}`);
      }).finally(() => {
        this.asyncQueryLoaded()
        this.toggles.tableLoading = false
      })
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

    editHTTPInterface(index, row) {
      bus.$emit('openEditHTTPInterfaceDialog', row)
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
  },
  mounted() {
    this.flushTable()
  },
  created() {
    bus.$on('query', (...args) => {
      let queryLike = args[0]
      let moduleId = args[1]
      if (queryLike) {
        this.queryLike = queryLike
      }
      if (moduleId) {
        this.moduleId = moduleId
      }
      this.flushTable();
    })
  }
}
</script>

<style scoped>
.table {
  width: 100%;
  font-size: 14px;
}

</style>

