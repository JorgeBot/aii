<!--配置角色-->
<template>
  <div class="handle-box">
    <el-input v-model="queryLike"
              placeholder="名称搜索"
              clearable
              class="handle-input mr10"
    ></el-input>
    <el-button type="primary"
               icon="search"
               @click="query"
               :loading="toggles.queryLoading"
    >搜索
    </el-button>
    <el-button type="primary"
               @click="openCreateProjectDialog"
               :disabled="permission.create"
    >新建项目
    </el-button>
    <el-button type="primary"
               @click="openConfigRoleDialog"
               :disabled="permission.relatedUser"
    >增设人员
    </el-button>
  </div>
</template>
<script>

import bus from "@/components/page/sysmanager/projectmanagement/bus"

export default {
  props: {
    permission: {
      create: false,
      relatedUser: false,
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
      bus.$emit('query', this.queryLike)
    },

    openCreateProjectDialog() {
      bus.$emit('openCreateProjectDialog')
    },

    openConfigRoleDialog() {
      bus.$emit('openConfigRoleDialog')
    },

    patchMenusAndResources() {
      bus.$emit('patchMenusAndResources')
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
.handle-input {
  width: 20%;
  min-width: 100px;
  display: inline-block;
}

.mr10 {
  margin-right: 10px;
}
</style>

