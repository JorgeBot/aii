<!-- 新建HTTP接口 -->
<template>
  <el-dialog
      title="新建接口"
      style="top: -10%"
      :visible.sync="toggles.visible"
      v-dialogDrag
      width="860px">
    <div class="create-dialog">
      <el-form :model="httpInterface"
               :rules="httpInterfaceRules"
               ref="httpInterfaceRef"
               label-width="130px"
               class="demo-ruleForm"
      >
        <el-form-item label="接口名" prop="interfaceName" class="input">
          <el-input v-model="httpInterface.interfaceName"></el-input>
        </el-form-item>
        <el-form-item label="HOST" prop="host" class="input">
          <el-input v-model="httpInterface.host"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="端口号" prop="port" class="input">
          <el-input v-model="httpInterface.port"
                    oninput="value=value.replace(/[^\d]/g,'')"
                    maxlength="5"
          >
          </el-input>
        </el-form-item>
        <div class="interfaceParams">
          <div>
            <h5>* 请求参数：</h5>
            <el-input
                type="textarea"
                :rows="15"
                placeholder="请输入内容"
                @input="toggles.noteRequestParam=false"
                v-model="httpInterface.request"
            >
            </el-input>
            <div class="noteRequestParam" v-if="toggles.noteRequestParam">请输入请求参数</div>
          </div>
          <div>
            <h5>响应参数：</h5>
            <el-input
                type="textarea"
                :rows="15"
                placeholder="请输入内容"
                @input="toggles.noteResponseParam=false"
                v-model="httpInterface.response"
            >
            </el-input>
            <div class="noteRequestParam" v-if="toggles.noteResponseParam">请输入响应参数</div>
          </div>
        </div>
      </el-form>

    </div>
    <span slot="footer">
      <el-button type="warning"
                 icon="el-icon-caret-right"
                 class="run-button"
                 @click="runHttpInterface"
                 :loading="toggles.runButtonLoading"
      >运&nbsp;&nbsp;&nbsp;行</el-button>
      <el-button @click="close"
      >取 消</el-button>
      <el-button type="primary"
                 :loading="toggles.submitButtonLoading"
                 @click="saveHTTPInterface"
      >确 定</el-button>
    </span>
  </el-dialog>
</template>
<script>

import axios from "@/api/api";
import qs from 'qs'
import bus from "@/components/page/interfacemanagement/bus"

export default {
  props: {},
  data() {
    return {
      httpInterface: {
        interfaceName: '',
        host: '',
        port: '',
        request: '',
        response: '',
        init: function () {
          this.interfaceName = ''
          this.host = ''
          this.port = ''
          this.request = ''
          this.response = ''
        }
      },
      httpInterfaceRules: {
        interfaceName: [
          {required: true, message: "请输入项目名", trigger: "blur"}
        ],
        host: [
          {required: true, message: "请输入HOST", trigger: "blur"}
        ],
        port: [
          {required: true, message: "请输入端口号", trigger: "blur"}
        ],
      },
      toggles: {
        submitButtonLoading: false,
        runButtonLoading: false,
        noteRequestParam: false,
        noteResponseParam: false,
        visible: false,
      },
    }
  },
  methods: {
    runHttpInterface() {
      this.$refs['httpInterfaceRef'].validate((valid) => {
        if (valid) {
          if (this.httpInterface.request === '') {
            this.toggles.noteRequestParam = true
            return
          }
          this.toggles.runButtonLoading = true;
          axios.post('/httpInterface/run', qs.stringify(this.httpInterface)).then(res => {
            this.httpInterface = res.data
          }).catch(err => {
            this.$message.error(`运行失败: ${err.response.data}`)
          }).finally(() => {
            this.toggles.runButtonLoading = false
          })
        }
      })
    },

    saveHTTPInterface() {
      this.$refs['httpInterfaceRef'].validate((valid) => {
        if (valid) {
          if (this.httpInterface.request === '') {
            this.toggles.noteRequestParam = true
            return
          }
          if (this.httpInterface.response === '') {
            this.toggles.noteResponseParam = true
            return
          }
          this.toggles.submitButtonLoading = true
          axios.post('/httpInterface', qs.stringify(this.httpInterface)).then(() => {
            this.$message({
              message: '保存成功',
              type: 'success'
            })
            this.toggles.visible = false
          }).catch(err => {
            this.$message.error(`保存出错: ${err.response.data}`)
          }).finally(() => {
            this.toggles.submitButtonLoading = false
          })
        }
      })
    },

    flushTable() {
      // bus.$emit('query')
    }
    ,

    close() {
      this.toggles.visible = false
    }
  }
  ,
  mounted() {
  }
  ,
  created() {
    bus.$on('openCreateHTTPInterfaceDialog', () => {
      this.toggles.visible = true
      this.httpInterface.init()
    })
  }
}
</script>

<style scoped>
.create-dialog {
  height: 66vh;
  overflow: auto;
}

.noteRequestParam {
  font-size: 12px;
  color: #f56c6c;
}

.interfaceParams {
  display: flex;
  justify-content: space-around;
}

.interfaceParams > div {
  width: 40%;
}

.input {
  width: 50%;
}

.run-button {
  padding-left: 5px;
  margin-right: 20px;
  width: 80px;
}

</style>

