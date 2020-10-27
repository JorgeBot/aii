<!--配置角色-->
<template>
  <div class="handle-box">
    <el-button type="primary"
               @click="openCreateRoleDialog"
               :disabled="permission.create"
    >新建角色
    </el-button>
    <el-button type="primary"
               :loading="toggles.saveLoading"
               @click="patchMenusAndResources"
               :disabled="permission.save">保存
    </el-button>
  </div>
</template>
<script>

import bus from '@/components/page/sysmanager/rolemanagement/bus'

export default {
  props: {
    permission: {
      create: false,
      save: false,
    },
  },
  data() {
    return {
      toggles: {
        saveLoading: false,
      },
    }
  },
  methods: {
    openCreateRoleDialog() {
      bus.$emit('openCreateRoleDialog')
    },

    patchMenusAndResources() {
      this.toggles.saveLoading = true
      bus.$emit('patchMenusAndResources')
    },
  },
  mounted() {
  },
  created() {
    bus.$on('asyncSaveLoaded', () => {
      this.toggles.saveLoading = false
    })
  }
}
</script>

<style scoped>

</style>

