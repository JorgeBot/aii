<template>
  <div class="header">
    <!-- 折叠按钮 -->
    <div class="collapse-btn" @click="collapseChange">
      <i class="el-icon-menu"></i>
    </div>
    <div class="logo">测试系统</div>
    <div class="header-right">
      <div class="header-user-con">
        <!-- 消息中心 -->
        <div class="btn-bell">
          <el-tooltip effect="dark" :content="message?`有${message}条未读消息`:`消息中心`" placement="bottom">
            <router-link to="/tabs">
              <i class="el-icon-bell"></i>
            </router-link>
          </el-tooltip>
          <span class="btn-bell-badge" v-if="message"></span>
        </div>
        <!-- 用户头像 -->
        <div class="user-avator">
          <img src="../../assets/img/img.jpg">
        </div>
        <!-- 用户名下拉菜单 -->
        <el-dropdown class="user-name" trigger="click" @command="handleCommand">
          <span class="el-dropdown-link">
            {{ nickname }}
            <i class="el-icon-caret-bottom"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <router-link to="/">
              <el-dropdown-item>我的任务</el-dropdown-item>
            </router-link>
            <el-dropdown-item command="editPassword">修改密码</el-dropdown-item>
            <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <el-dialog title="密码修改" :visible.sync="toggles.visible" v-dialogDrag>
      <el-form :model="form">
        <el-form-item label="原密码" :label-width="formLabelWidth">
          <el-input v-model="form.oldPassword" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码" :label-width="formLabelWidth">
          <el-input show-password v-model="form.newPassword" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" :label-width="formLabelWidth">
          <el-input show-password v-model="form.againPassword" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="editPassword">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>


import bus from "./bus";
import axios from "@/api/api";
import qs from 'qs'

export default {
  data() {
    return {
      collapse: false,
      fullscreen: false,
      message: 2,
      form: {
        oldPassword: '',
        newPassword: '',
        againPassword: '',
      },
      toggles: {
        visible: false,
      },
      formLabelWidth: '120px',
    };
  },
  computed: {
    nickname() {
      return localStorage.getItem("ms_nickname");
    },
    username() {
      return localStorage.getItem("ms_username");
    },
  },
  methods: {
    // 用户名下拉菜单选择事件
    handleCommand(command) {
      if (command === "logout") {
        localStorage.removeItem("ms_username")
        this.$router.push("/login")
      } else if (command === "editPassword") {
        this.toggles.visible = true
      }
    },
    // 侧边栏折叠
    collapseChange() {
      this.collapse = !this.collapse;
      bus.$emit("collapse", this.collapse);
    },
    editPassword() {
      if (this.form.newPassword !== this.form.againPassword) {
        this.$message.error("两次输入的密码不一致，请重新确认")
        return
      }
      let param = {
        oldPassword: this.form.oldPassword,
        newPassword: this.form.newPassword,
        username: this.username,
      }
      axios.post(`editPassword`, qs.stringify(param)).then(res => {

      })
    },
    close() {
      this.toggles.visible = false
    },
  },
  mounted() {
    if (document.body.clientWidth < 1500) {
      this.collapseChange();
    }
  },
  created() {
  }
};
</script>


<style scoped>
.header {
  background-color: #23262e !important;
  position: relative;
  box-sizing: border-box;
  width: 100%;
  height: 70px;
  font-size: 22px;
  color: #fff;
}

.collapse-btn {
  float: left;
  padding: 0 21px;
  cursor: pointer;
  line-height: 70px;
}

.header .logo {
  float: left;
  width: 250px;
  line-height: 70px;
}

.header-right {
  float: right;
  padding-right: 50px;
}

.header-user-con {
  display: flex;
  height: 70px;
  align-items: center;
}

.btn-fullscreen {
  transform: rotate(45deg);
  margin-right: 5px;
  font-size: 24px;
}

.btn-bell,
.btn-fullscreen {
  position: relative;
  width: 30px;
  height: 30px;
  text-align: center;
  border-radius: 15px;
  cursor: pointer;
}

.btn-bell-badge {
  position: absolute;
  right: 0;
  top: -2px;
  width: 8px;
  height: 8px;
  border-radius: 4px;
  background: #f56c6c;
  color: #fff;
}

.btn-bell .el-icon-bell {
  color: #fff;
}

.user-name {
  margin-left: 10px;
}

.user-avator {
  margin-left: 20px;
}

.user-avator img {
  display: block;
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.el-dropdown-link {
  color: #fff;
  cursor: pointer;
}

.el-dropdown-menu__item {
  text-align: center;
}

.collapse-btn:hover {
  background-color: #009688 !important;
}
</style>
