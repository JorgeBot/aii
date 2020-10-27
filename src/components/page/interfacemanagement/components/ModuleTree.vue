<template>
 <div>
   <el-input
       placeholder="输入关键字进行过滤"
       v-model="queryFilter"
       clearable
   ></el-input>
   <el-tree :data="moduleTree.data"
            :props="moduleTree.defaultProps"
            :filter-node-method="filterNode"
            @node-click="query"
            v-loading="toggles.treeLoading"
            class="tree"
            ref="moduleTreeRef"
   ></el-tree>
 </div>
</template>
<script>


import axios from "@/api/api"
import bus from "@/components/page/interfacemanagement/bus"

export default {
  data() {
    return {
      queryFilter: '',
      moduleTree: {
        data: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        },
      },
      toggles: {
        treeLoading: false
      }
    }
  },
  methods: {
    query(data) {
      bus.$emit('query', null, data.id)
    },
    loadModuleTree() {
      this.toggles.treeLoading = true
      axios.get(`/moduleTree`).then(res => {
        this.moduleTree.data = res.data
      }).catch(err => {
        this.$message.error(`加载模块树出错: ${err.response.data}`)
      }).finally(() => {
        this.toggles.treeLoading = false
      })
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    }
  },

  watch: {
    queryFilter(val) {
      this.$refs.moduleTreeRef.filter(val)
    },
    moduleTree: {
      handler: function () {
        this.$nextTick(() => {
          document.querySelector('.el-tree-node__content').click()
        })
      },
      deep: true,
    }
  },

  mounted() {
    this.loadModuleTree()
  },

  created() {
  }
}
</script>


<style scoped>
.tree {
  margin-top: 15px;
}
</style>

