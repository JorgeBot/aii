<template>
  <div class="login-wrap">
    <div class="ms-login">
      <div class="ms-title">Aii接口自动化</div>
      <el-form :model="ruleForm" :rules="rules" ref="ruleFormRef" label-width="0px" class="ms-content">
        <el-form-item prop="username">
          <el-input v-model="ruleForm.username"
                    placeholder="username"
                    @focus="toggles.submittable=false"
          >
            <el-button slot="prepend" icon="el-icon-lx-people"></el-button>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password"
                    placeholder="password"
                    v-model="ruleForm.password"
                    @keyup.enter.native="submitForm('ruleFormRef')"
                    @focus="toggles.submittable=false"
          >
            <el-button slot="prepend" icon="el-icon-lx-lock"></el-button>
          </el-input>
        </el-form-item>
        <p class="login-tips" v-if="toggles.showTip">{{ tip }}</p>

        <div class="login-btn">
          <el-button type="primary"
                     :disabled="toggles.submittable"
                     :loading="toggles.submitLoading"
                     @click="submitForm('ruleFormRef')"
          >登录
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import qs from 'qs'

export default {
  data: function () {
    return {
      tip: '@@@',
      ruleForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'}
        ]
      },
      toggles: {
        showTip: false,
        submittable: false,
        submitLoading: false,
      }

    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.toggles.submitLoading = true
          axios.post('http://localhost/login', qs.stringify(this.ruleForm)).then(res => {
            localStorage.setItem('ms_username', res.data.username);
            localStorage.setItem('ms_nickname', res.data.nickname);
            localStorage.setItem('ms_menus', JSON.stringify(res.data.menus));
            localStorage.setItem('ms_resources', JSON.stringify(res.data.resources));
            this.$router.push('/');
          }).catch(err => {
            if (err.response.status === 401) {
              this.tip = err.response.data
              this.toggles.showTip = true
              this.toggles.submittable = true
            } else {
              this.$message.error('登录失败: ' + err.response.data)
            }
          }).finally(() => {
            this.toggles.submitLoading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.login-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  background-image: url(../../assets/img/login-bg.jpg);
  background-size: 100%;
}

.ms-title {
  width: 100%;
  line-height: 50px;
  text-align: center;
  font-size: 20px;
  color: #000;
  border-bottom: 1px solid #ddd;
}

.ms-login {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 350px;
  margin: -190px 0 0 -175px;
  border-radius: 5px;
  background: rgba(255, 255, 255, 1);
  overflow: hidden;
}

.ms-content {
  padding: 30px 30px;
}

.login-btn {
  margin-top: 5px;
  text-align: center;
}

.login-btn button {
  width: 100%;
  height: 36px;
  margin-bottom: 10px;
}

.login-tips {
  font-size: 12px;
  color: red;
  margin-top: -10px;
}
</style>