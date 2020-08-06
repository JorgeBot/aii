<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>接口管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-input v-model="interface.interfaceNameLike" placeholder="名称搜索" class="handle-input mr10"></el-input>
        <el-button type="primary" icon="search" @click="getInterfaces">搜索</el-button>
        <el-button type="primary"
                   @click="toggles.createDialogVisible=true"
                   v-bind:disabled="permission.createInterface"
        >新建接口
        </el-button>
      </div>
      <el-table
          :data="interface.data"
          border
          class="table"
          ref="interfaceTable"
          v-loading="toggles.tableLoading"
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="orderNum" label="序号" type="index" width="120"></el-table-column>
        <el-table-column prop="interfaceName" label="接口名" width="180"></el-table-column>
        <el-table-column prop="url" label="URL" width="320"></el-table-column>
        <el-table-column prop="method" label="请求方式"></el-table-column>
        <el-table-column prop="protocol" label="协议"></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button
                type="text"
                icon="el-icon-edit"
                @click="editInterface(scope.$index, scope.row)"
                v-bind:disabled="permission.editInterface"
            >编辑
            </el-button>
            <el-button
                type="text"
                icon="el-icon-caret-right"
                v-bind:disabled="permission.runInterface"
                @click="runInterface(scope.$index, scope.row)"
            >运行
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
            background
            @current-change="currentChange"
            layout="prev, pager, next"
            :total="interface.total"
            :page-size="interface.size"
        ></el-pagination>
      </div>

      <!--新建接口-->
      <el-dialog
          title="新建接口"
          :visible.sync="toggles.createDialogVisible"
          width="50%">
        <div class="create-dialog">
          <el-form :model="interfaceModel" :rules="interfaceRules" ref="interfaceForm" label-width="130px"
                   class="demo-ruleForm">
            <el-form-item label="接口名" prop="interfaceName" class="input">
              <el-input v-model="interfaceModel.interfaceName"></el-input>
            </el-form-item>
            <el-form-item label="超时时间" prop="timeout" class="input">
              <el-input v-model="interfaceModel.timeout"
                        oninput="value=value.replace(/[^\d]/g,'')"
                        maxlength="2"
              >
                <template slot="append">秒</template>
              </el-input>
            </el-form-item>
            <div class="interfaceParams">
              <div>
                <h5>* 请求参数：</h5>
                <el-input
                    type="textarea"
                    :rows="15"
                    placeholder="请输入内容"
                    v-on:input="toggles.noteRequestParam=false"
                    v-model="interfaceModel.requestParams">
                </el-input>
                <div class="noteRequestParam" v-if="toggles.noteRequestParam">请输入请求参数</div>
              </div>
              <div>
                <h5>响应参数：</h5>
                <el-input
                    type="textarea"
                    :rows="15"
                    placeholder="请输入内容"
                    v-model="interfaceModel.responseParams">
                </el-input>
              </div>
            </div>
          </el-form>

        </div>
        <span slot="footer">
                    <el-button @click="toggles.createDialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="postInterface" v-loading="toggles.OKButton">确 定</el-button>
                </span>
      </el-dialog>
      <!--编辑接口-->
      <el-dialog
          title="新建接口"
          :visible.sync="toggles.editDialogVisible"
          width="50%">
        <div class="create-dialog">
          <el-form :model="editInterfaceModel" :rules="interfaceRules" ref="editInterfaceForm" label-width="130px"
                   class="demo-ruleForm">
            <el-form-item label="接口名" prop="interfaceName" class="input">
              <el-input v-model="editInterfaceModel.interfaceName"></el-input>
            </el-form-item>
            <el-form-item label="超时时间" prop="timeout" class="input">
              <el-input v-model="editInterfaceModel.timeout"
                        oninput="value=value.replace(/[^\d]/g,'')"
                        maxlength="2"
              >
                <template slot="append">秒</template>
              </el-input>
            </el-form-item>
            <div class="interfaceParams">
              <div>
                <h5>* 请求参数：</h5>
                <el-input
                    type="textarea"
                    :rows="15"
                    placeholder="请输入内容"
                    v-on:input="toggles.noteRequestParam=false"
                    v-model="editInterfaceModel.requestParams">
                </el-input>
                <div class="noteRequestParam" v-if="toggles.noteRequestParam">请输入请求参数</div>
              </div>
              <div>
                <h5>响应参数：</h5>
                <el-input
                    type="textarea"
                    :rows="15"
                    placeholder="请输入内容"
                    v-model="editInterfaceModel.responseParams">
                </el-input>
              </div>
            </div>
          </el-form>
        </div>
        <span slot="footer">
          <el-button @click="toggles.editDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="patchInterface" v-loading="toggles.OKButton">确 定</el-button>
        </span>
      </el-dialog>
      <!--运行结果-->
      <el-dialog
          title="运行结果"
          :visible.sync="toggles.runDialogVisible"
          width="50%">
        <div>
          <h5>响应状态：{{ runModel.status }}</h5>
          <h5>响应参数：</h5>
          <el-input
              type="textarea"
              :rows="15"
              v-model="runModel.responseParams">
          </el-input>
        </div>
        <span slot="footer">
                    <el-button type="info" @click="setResponse"
                               v-loading="toggles.setResponseButton">设为 RESPONSE</el-button>
                    <el-button type="primary" @click="toggles.runDialogVisible=false">确 定</el-button>
                </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import qs from 'qs'

export default {
  name: "HTTPManagement",
  data() {
    return {
      permission: {
        editInterface: false,
        runInterface: false,
        createInterface: false,
      },

      interface: {
        data: [
          {
            interfaceName: '',
            url: '',
            method: '',
            result: '',
          }
        ],
        total: 0,
        current: 1,
        size: 10,
        interfaceNameLike: '',
      },
      URLParams: {
        data: [
          {
            url: "/getPerson",
            value: "1"
          }
        ]
      },
      toggles: {
        createDialogVisible: false,
        editDialogVisible: false,
        runDialogVisible: false,
        noteRequestParam: false,
        OKButton: false,
        setResponseButton: false,
      },
      interfaceModel: {
        timeout: '',
        interfaceName: '',
        requestParams: '',
        responseParams: '',
      },
      editInterfaceModel: {
        id: '',
        timeout: '',
        interfaceName: '',
        requestParams: '',
        responseParams: '',
      },
      runModel: {
        runId: '',
        status: '',
        responseParams: '',
      },
      interfaceRules: {
        interfaceName: [
          {required: true, message: '请输入URL', trigger: 'blur'},
        ],
        timeout: [
          {required: true, message: '请输入超时时间', trigger: 'blur'},
        ]
      },

    }
  },
  methods: {
    getInterfaces() {
      axios.get(`http://localhost/interfacePage?interfaceNameLike=${this.interface.interfaceNameLike}`).then(res => {
        this.interface.data = res.data.records
        this.interface.current = res.data.current
        this.interface.size = res.data.size
        this.interface.total = res.data.total
      })
    },
    currentChange(val) {
      this.interface.current = val;
      this.getInterfaces();
    },
    postInterface() {
      this.$refs['interfaceForm'].validate((valid) => {
        if (valid) {
          if (this.interfaceModel.requestParams === '') {
            this.toggles.noteRequestParam = true
            return
          }
          this.toggles.OKButton = true
          axios.post('http://localhost/interface', qs.stringify(this.interfaceModel)).then(() => {
            this.$message({
              type: "success",
              message: '保存成功'
            });
          }).catch(err => {
            this.$message({
              type: "warning",
              message: `保存失败：${err}`
            });
          }).finally(() => {
            this.toggles.OKButton = false
            this.toggles.createDialogVisible = false;
          })
        }
      })
    },
    editInterface(index, row) {
      this.toggles.editDialogVisible = true
      this.editInterfaceModel.requestParams = row.requestParams
      this.editInterfaceModel.responseParams = row.responseParams
      this.editInterfaceModel.interfaceName = row.interfaceName
      this.editInterfaceModel.timeout = row.timeout
      this.editInterfaceModel.id = row.id
    },
    patchInterface() {
      this.$refs['editInterfaceForm'].validate((valid) => {
        if (valid) {
          if (this.editInterfaceModel.requestParams === '') {
            this.toggles.noteRequestParam = true
            return
          }
          this.toggles.OKButton = true
          axios.patch(`http://localhost/interface/${this.editInterfaceModel.id}`, qs.stringify(this.editInterfaceModel)).then(() => {
            this.$message({
              type: "success",
              message: '保存成功'
            })
          }).catch(err => {
            this.$message({
              type: "warning",
              message: `保存失败：${err}`
            })
          }).finally(() => {
            this.toggles.OKButton = false
            this.toggles.editDialogVisible = false
          })
        }
      })
    },
    runInterface(index, row) {
      this.toggles.runDialogVisible = true
      axios.get(`http://localhost/interface/run/${row.id}`).then(res => {
        this.runModel.responseParams = res.data.responseParams
        this.runModel.status = res.data.status
        this.runModel.runId = row.id
      }).catch(err => {
        console.log(err.response.data)
        this.runModel.responseParams = err.response.data
        this.runModel.status = err.response.status
        this.runModel.runId = row.id
      })
    },
    setResponse() {
      this.toggles.setResponseButton = true
      axios.patch(`http://localhost/interface/${this.runModel.runId}`, qs.stringify({responseParams: this.runModel.responseParams})).then(res => {
        this.$message({
          type: "success",
          message: '设置成功'
        })
      }).finally(() => {
        this.toggles.runDialogVisible = false
        this.toggles.setResponseButton = false
      })
    }
  },
  mounted() {
    this.getInterfaces()
  }
}
</script>

<style scoped>
.container {
  height: 500px;
}

.table {
  width: 100%;
  font-size: 14px;
}

.handle-input {
  width: 300px;
  display: inline-block;
}

.mr10 {
  margin-right: 10px;
}

.handle-box {
  margin-bottom: 20px;
}

.create-dialog {
  height: 60vh;
  overflow: auto;
}

.input {
  width: 50%;
}

.interfaceParams {
  display: flex;
  justify-content: space-around;
}

.interfaceParams > div {
  width: 40%;
}

.noteRequestParam {
  font-size: 12px;
  color: #f56c6c;
}
</style>