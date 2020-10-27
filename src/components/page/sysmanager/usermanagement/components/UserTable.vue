<!--配置角色-->
<template>
  <div>
    <el-table
        :data="userTable.records"
        border
        class="table"
        height="455px"
        ref="usersTableRef"
        v-loading="toggles.tableLoading"
    >
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="orderNum" label="序号" type="index" width="120"></el-table-column>
      <el-table-column prop="username" label="用户名" width="120"></el-table-column>
      <el-table-column prop="nickname" label="角色名称"></el-table-column>
      <el-table-column prop="status" label="状态"></el-table-column>
      <el-table-column prop="sex" label="性别"></el-table-column>
      <el-table-column prop="lastLoginDatetime" label="登录时间"></el-table-column>
      <el-table-column label="操作" width="180" align="center">
        <template slot-scope="scope">
          <el-button
              type="text"
              icon="el-icon-edit"
              @click="openEditUserDialog(scope.$index, scope.row)"
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
          :total="userTable.total"
          :page-size="userTable.size"
          :page-sizes="[10, 20, 50, 100]"
          :current-page="userTable.current"
          layout="total, sizes, prev, pager, next"
          background
      ></el-pagination>
    </div>
  </div>
</template>
<script>

import axios from "@/api/api";
import bus from '@/components/page/sysmanager/usermanagement/bus'

export default {
  props: {
    permission: {
      operate: false,
    },
  },
  data() {
    return {
      queryLike: '',
      userTable: {
        records: [],
        total: 0,
        current: 1,
        size: 10,
      },
      toggles: {
        submitButtonLoading: false,
        tableLoading: false,
      },
    }
  },
  methods: {
    flushTable() {
      let params = {
        queryNameLike: this.queryLike,
        current: this.userTable.current,
        size: this.userTable.size,
      }
      this.toggles.tableLoading = true
      axios.get(`userPage`, {params: params}).then(res => {
        this.userTable = res.data
      }).catch(err => {
        this.$message.error(`获取角色列表失败: ${err.response.data}`)
      }).finally(() => {
        this.toggles.tableLoading = false
        this.asyncQueryLoaded()
      })
    },
    currentChange(val) {
      this.userTable.current = val
      this.flushTable()
    },
    sizeChange(val) {
      this.userTable.size = val
      this.flushTable()
    },
    asyncQueryLoaded() {
      bus.$emit('asyncQueryLoaded')
    },
    openEditUserDialog(index, row) {
      let param = Object.assign({},
          {
            id: row.id,
            nickname: row.nickname,
            roleIds: row.roleIds,
            relatedProjectIdArray: row.relatedProjectIdArray,
            sex: row.sex,
            status: row.status,
          })
      bus.$emit('openEditUserDialog', param)
    },
    getCheckedUserIds() {
      return this.$refs.usersTableRef.selection.map(m => m.id)
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
</style>

