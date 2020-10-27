<template>
  <div class="wrapper">
    <VHead></VHead>
    <VSidebar></VSidebar>
    <div class="content-box" :class="{'content-collapse':collapse}">
      <VTags></VTags>
      <div class="content">
        <transition name="move" mode="out-in">
          <keep-alive :include="tagsList">
            <router-view></router-view>
          </keep-alive>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
import VHead from "./Header.vue";
import VSidebar from "./Sidebar.vue";
import VTags from "./Tags.vue";
import bus from "./bus";

export default {
  data() {
    return {
      tagsList: [],
      collapse: false,
    };
  },
  computed: {
  },
  methods: {
  },
  components: {
    VHead,
    VSidebar,
    VTags
  },
  created() {
    bus.$on("collapse", msg => {
      this.collapse = msg;
    });

    // 只有在标签页列表里的页面才使用keep-alive，即关闭标签之后就不保存到内存中了。
    bus.$on("tags", msg => {
      let arr = [];
      for (let i = 0, len = msg.length; i < len; i++) {
        msg[i].name && arr.push(msg[i].name);
      }
      this.tagsList = arr;
    });
  }
};
</script>
