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
               :loading="toggles.queryLoading"
               @click="query"
    >搜索
    </el-button>
    <el-button type="primary"
               @click="openCreateUserDialog"
               :disabled="permission.create"
    >新建用户
    </el-button>
    <el-button type="primary"
               @click="openConfigRoleDialog"
               :disabled="permission.operate"
    >配置角色
    </el-button>
  </div>
</template>
<script>

import bus from '@/components/page/sysmanager/usermanagement/bus'

export default {
  props: {
    permission: {
      create: false,
      operate: false,
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
    openCreateUserDialog() {
      bus.$emit('openCreateUserDialog')
    },
    openConfigRoleDialog() {
      this.$emit('openConfigRoleDialog')
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

