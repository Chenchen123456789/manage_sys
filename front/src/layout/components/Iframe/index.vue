<template>
  <div
    id="myIframeDiv"
    :class="{'iframe-fullscreen': isFullscreen}"
    v-loading="iframeLoading"
    element-loading-text="加载中"
  >
    <iframe :id="myIframeId" style="border:none;width:100%;height:100%" :src="src" frameborder="no"></iframe>
  </div>
</template>
<script>
import Vue from "vue";
import screenfull from "screenfull";
export default {
  props: {
    'src': {
      type: String,
      default: '/'
    },
    'myIframeId': {
      type: String
    }
  },
  data () {
    return {
      iframeLoading: false,
      isFullscreen: false
    };
  },
  mounted () {
    var _this = this
    this.init()

    const iframeId = ("#" + this.myIframeId).replace(/\//g, '\\/')
    const iframe = document.querySelector(iframeId)
    // 处理兼容问题
    if (iframe.attachEvent) {
      this.iframeLoading = true
      iframe.attachEvent('onload', function () {
        // iframe加载完毕以后执行操作
        _this.iframeLoading = false
      });
    } else {
      this.iframeLoading = true
      iframe.onload = function () {
        // iframe加载完毕以后执行操作
        _this.iframeLoading = false
      };
    }
  },
  methods: {
    init () {
      if (screenfull.enabled) {
        screenfull.on('change', this.change)
      }
    },
    change () {
      this.isFullscreen = screenfull.isFullscreen
    }
  },
}
</script>
<style lang="scss" scope>
#myIframeDiv {
  height: calc(100vh - 98px);
}
.iframe-fullscreen {
  height: 100vh !important;
}
</style>