<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in">
      <!-- <keep-alive :include="cachedViews"> -->
      <keep-alive>
        <router-view v-if="!$route.meta.iframeSrc" :key="key" />
      </keep-alive>
    </transition>
    <transition-group name="fade-transform" mode="out-in">
      <iframe-component
        v-for="(item, index) in iframeViews"
        :key="item.path"
        :myIframeId="'iframe' + index"
        v-show="$route.path === item.path"
        :src="item.meta.iframeSrc"
      ></iframe-component>
    </transition-group>
  </section>
</template>

<script>
import IframeComponent from './Iframe/index'

export default {
  name: 'AppMain',
  components: {
    IframeComponent
  },
  computed: {
    cachedViews() {
      return this.$store.state.tagsView.cachedViews
    },
    key() {
      return this.$route.path
    },
    iframeViews() {
      return this.$store.state.tagsView.iframeViews
    }
  }
}
</script>

<style lang="scss" scoped>
.app-main {
  /* 50= navbar  50  */
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;
}

.fixed-header + .app-main {
  padding-top: 50px;
}

.hasTagsView {
  .app-main {
    /* 98 = header width */
    min-height: calc(100vh - 98px);
  }

  .fixed-header + .app-main {
    padding-top: 84px;
  }
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 15px;
  }
}
</style>
