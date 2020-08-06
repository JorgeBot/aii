<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>人员管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-input v-model="userTable.query" placeholder="名称搜索" class="handle-input mr10"></el-input>
        <el-button type="primary" icon="search" @click="getUsers">搜索</el-button>
        <el-button type="primary" @click="toggles.addUserVisible=true" v-bind:disabled="permission.createUser">
          新建用户
        </el-button>
        <el-button type="primary" @click="showConfigRole" v-bind:disabled="permission.configRole">配置角色
        </el-button>
      </div>
      <el-table
          :data="userTable.data"
          border
          class="table"
          ref="usersTable"
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
                @click="handleEdit(scope.$index, scope.row)"
                v-bind:disabled="permission.editUser"
            >编辑
            </el-button>
            <el-button
                type="text"
                icon="el-icon-delete"
                class="red"
                @click="handleDelete(scope.$index, scope.row)"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
            background
            @current-change="currentChange"
            layout="prev, pager, next"
            :total="userTable.total"
            :page-size="userTable.pageSize"
        ></el-pagination>
      </div>
      <!-- 编辑 -->
      <el-dialog
          title="修改信息"
          :visible.sync="toggles.editUserVisible"
          ref="editUserForm"
          v-dialogDrag
          width="30%"
      >
        <el-form :model="userForm" :rules="editRule" ref="editUserForm">
          <el-form-item label="昵称" :label-width="formLabelWidth" prop="nickname">
            <el-input class="nicknameEdit" v-model="userForm.nickname" width="20"
                      autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="状态" :label-width="formLabelWidth">
            <el-radio v-model="userForm.status" label="正常">正常</el-radio>
            <el-radio v-model="userForm.status" label="禁用">禁用</el-radio>
          </el-form-item>
          <el-form-item label="角色" :label-width="formLabelWidth">
          </el-form-item>
          <el-table
              ref="rolesEditTable"
              :data="roleTable"
              tooltip-effect="dark"
              :show-header="false"
              max-height="300"
              class="editRole"
          >
            <el-table-column
                type="selection"
                width="55">
            </el-table-column>
            <el-table-column
                prop="roleName"
                width="145">
            </el-table-column>
          </el-table>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="clearRoleTable">取 消</el-button>
          <el-button type="primary" @click.native="editUser">确 定</el-button>
        </div>
      </el-dialog>
      <!-- 新建用户 -->
      <el-dialog title="新建用户" :visible.sync="toggles.addUserVisible" ref="addUserForm" v-dialogDrag>
        <el-form :model="addUserForm" :rules="addUserRule">
          <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
            <el-input v-model="addUserForm.username" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="昵称" :label-width="formLabelWidth" prop="nickname">
            <el-input v-model="addUserForm.nickname" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="密码" :label-width="formLabelWidth" prop="password">
            <el-input v-model="addUserForm.password" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="性别" :label-width="formLabelWidth">
            <el-radio v-model="addUserForm.sex" label="男">男</el-radio>
            <el-radio v-model="addUserForm.sex" label="女">女</el-radio>
          </el-form-item>
          <el-form-item label="状态" :label-width="formLabelWidth">
            <el-radio v-model="addUserForm.status" label="正常">正常</el-radio>
            <el-radio v-model="addUserForm.status" label="禁用">禁用</el-radio>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="toggles.addUserVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveUser">确 定</el-button>
        </div>
      </el-dialog>
      <!--配置角色-->
      <el-dialog
          title="配置角色"
          :visible.sync="toggles.relateUseAndRoleVisible"
          v-dialogDrag
          width="15%"
      >
        <el-table
            ref="rolesTable"
            :data="roleTable"
            tooltip-effect="dark"
            max-height="400"
        >
          <el-table-column
              type="selection"
              width="55">
          </el-table-column>
          <el-table-column
              prop="roleName"
              label="角色名:"
              width="120">
          </el-table-column>
        </el-table>
        <div slot="footer" class="dialog-footer">
          <el-button @click="toggles.relateUseAndRoleVisible=false">取 消</el-button>
          <el-button type="primary" @click.native="relateUsersAndRoles">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import {
  getDeleteOne,
} from "@/api/api";
import axios from 'axios'
import qs from 'qs'
import domain from '@/../public/domain'

export default {
  data() {
    return {
      permission: {
        createUser: false,
        configRole: false,
        editUser: false,
      },
      userTable: {
        query: "",
        data: [],
        total: 0,
        pageNo: 1,
        pageSize: 10,
      },
      userForm: {
        nickname: '',
        status: '',
        roleIds: ''
      }, //编辑数据
      addUserForm: {
        username: "",
        nickname: "",
        password: "123456aA",
        status: "正常",
        sex: "男"
      }, //添加用户数据
      roleTable: [
        {
          id: '',
          roleName: '',
        }
      ],
      addUserRule: {
        username: [
          {required: true, message: "请输入用户名", trigger: "blur"}
        ],
        nickname: [
          {
            required: true,
            message: "请输入昵称",
            trigger: "blur"
          }
        ],
        password: [
          {
            required: true,
            message: "请输入密码",
            trigger: "blur"
          }
        ],
      },
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
        editUserVisible: false, //是否显示编辑
        addUserVisible: false, //新建用户框
        relateUseAndRoleVisible: false, // 配置角色
        tableLoading: false,
      },
      formLabelWidth: "120px",
    };
  },
  methods: {
    getUsers() {
      this.toggles.tableLoading = true;
      let params = {
        queryNameLike: this.userTable.query.trim(),
        page: this.userTable.pageNo
      };
      axios.get(`userPage`, {params: params}).then(res => {
        this.userTable.data = res.data.records;
        this.userTable.total = res.data.total;
        this.toggles.tableLoading = false;
      });
    },
    currentChange(val) {
      this.userTable.pageNo = val;
      this.getUsers();
    },
    saveUser() {
      let params = Object.assign({}, this.addUserForm);
      params.username = params.username.trim();
      axios.post('/user', qs.stringify(params)).then(() => {
        this.$message({
          message: "添加成功",
          type: "success"
        });
        this.toggles.addUserVisible = false;
        this.addUserForm = {};
        this.getUsers();
      })
    },
    handleEdit(index, row) {
      axios.get('roles').then(res => {
        this.roleTable = res.data
      }).then(() => {
        let t = new Set(row.roleIds)
        this.roleTable.filter(f => t.has(f.id)).forEach(e => {
          this.$refs.rolesEditTable.toggleRowSelection(e)
        })
      })
      this.toggles.editUserVisible = true;
      this.userForm = Object.assign({}, {nickname: row.nickname, status: row.status, id: row.id});
    },
    handleDelete(index, row) {
      let params = {
        userid: row.userid
      };
      this.$confirm("确认删除该用户？", "提示", {
        type: "warning"
      }).then(() => {
        getDeleteOne(params).then(() => {
          this.$message({
            type: "success",
            message: "删除成功"
          });
          this.getUsers();
        });
      });
    },
    editUser() {
      this.$refs['editUserForm'].validate((valid) => {
        if (valid) {
          this.userForm.roleIds = this.$refs.rolesEditTable.selection.map(m => m.id)
          let params = this.userForm;
          axios.patch(`/user/${params.id}`, qs.stringify(params)).then(() => {
            this.$message({
              type: "success",
              message: '更新成功'
            });
            this.getUsers();
            this.toggles.editUserVisible = false;
            this.userForm = {};
          })
        }
      });
    },
    showConfigRole() {
      if (this.$refs.usersTable.selection.length === 0) {
        this.$message({
          type: "warning",
          message: '请先选中用户'
        });
        return;
      }

      this.toggles.relateUseAndRoleVisible = true;
      axios.get('roles').then(res => {
        this.roleTable = res.data
      })
    },
    relateUsersAndRoles() {
      let userIds = this.$refs.usersTable.selection.map(m => m.id)
      let roleIds = this.$refs.rolesTable.selection.map(m => m.id)
      axios.post('relationUserRole', qs.stringify({userIds: userIds, roleIds: roleIds})).then(() => {
        this.getUsers()
        this.$message({
          type: "success",
          message: "配置成功"
        });
        this.toggles.relateUseAndRoleVisible = false
      })
    },
    clearRoleTable() {
      this.toggles.editUserVisible = false
      let _this = this
      setTimeout(function () {
        _this.roleTable = []
      }, 200)
    },
    getResources() {
      let resources = new Set(JSON.parse(localStorage.getItem("ms_resources")))
      if (domain.permission) {
        this.permission.configRole = !resources.has("配置角色")
        this.permission.createUser = !resources.has("新建用户")
        this.permission.editUser = !resources.has("编辑用户")
      }
    }
  },
  mounted() {
    this.getUsers();
    this.getResources()
  }
};
</script>
<style scoped>

.container {
  height: 500px;
}

.handle-box {
  margin-bottom: 20px;
}

.handle-select {
  width: 120px;
}

.handle-input {
  width: 300px;
  display: inline-block;
}

.del-dialog-cnt {
  font-size: 16px;
  text-align: center;
}

.table {
  width: 100%;
  font-size: 14px;
}

.red {
  color: #ff0000;
}

.mr10 {
  margin-right: 10px;
}

.editRole {
  margin-left: 20%;
  width: 210px;

}

.nicknameEdit {
  width: 60%;
}

.has-gutter {
  display: none;
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

