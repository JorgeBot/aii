<template>
  <div class="handle-box">
    <el-input v-model="queryLike"
              placeholder="名称搜索"
              class="handle-input mr10"
              clearable
    ></el-input>
    <el-button type="primary"
               icon="search"
               @click="query"
               :loading="toggles.queryLoading"
    >搜索</el-button>
    <el-button type="primary"
               @click="openCreateHTTPInterfaceDialog"
               :disabled="permission.create"
    >新建接口
    </el-button>
  </div>
</template>
<script>

import bus from "@/components/page/interfacemanagement/bus"

export default {
  props: {
    permission: {
      create: false,
    },
  },
  data() {
    return {
      queryLike: '',
      toggles: {
        queryLoading: false,
      },
    }
  },
  methods: {
    query() {
      this.toggles.queryLoading = true
      bus.$emit('query', this.queryLike, null)
    },

    openCreateHTTPInterfaceDialog() {
      bus.$emit('openCreateHTTPInterfaceDialog')
    },
  },
  mounted() {
  },
  created() {
    bus.$on('asyncQueryLoaded', () => {
      this.toggles.queryLoading = false
    })
  }
}
</script>


<style scoped>
.handle-box {
  margin-bottom: 20px;
  min-width: 700px;
}

.handle-input {
  width: 20%;
  min-width: 100px;
  display: inline-block;
}

.mr10 {
  margin-right: 10px;
}
</style>

