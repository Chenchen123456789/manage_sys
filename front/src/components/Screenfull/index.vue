<template>
  <div>
    <svg-icon :icon-class="isFullscreen?'exit-fullscreen':'fullscreen'" @click="click" />
  </div>
</template>

<script>
import screenfull from 'screenfull'

export default {
  name: 'Screenfull',
  data () {
    return {
      isFullscreen: false
    }
  },
  mounted () {
    this.init()
  },
  beforeDestroy () {
    this.destroy()
  },
  methods: {
    click () {
      if (!screenfull.enabled) {
        this.$message({
          message: 'you browser can not work',
          type: 'warning'
        })
        return false
      }
      let side_container = document.getElementsByClassName('sidebar-container')[0]
      let main_container = document.getElementsByClassName('main-container')[0]
      let header = document.getElementById('header')
      let app_main = document.getElementsByClassName('app-main')[0]
      if (screenfull.isFullscreen) {
        screenfull.exit()
      } else {
        screenfull.request()
        side_container.setAttribute('style', 'display: none')
        main_container.setAttribute('style', 'margin-left: 0px !important')
        header.setAttribute('style', 'display:none')
        app_main.setAttribute('style', 'min-height: 100vh')
      }
    },
    change () {
      this.isFullscreen = screenfull.isFullscreen
      // 全屏下监控是否按键了ESC
      // 全屏下按键esc后要执行的动作
      let side_container = document.getElementsByClassName('sidebar-container')[0]
      let main_container = document.getElementsByClassName('main-container')[0]
      let header = document.getElementById('header')
      let app_main = document.getElementsByClassName('app-main')[0]
      if (!screenfull.isFullscreen) {
        side_container.setAttribute('style', 'display: block')
        main_container.setAttribute('style', 'margin-left: 200px')
        header.setAttribute('style', 'display:block')
        app_main.setAttribute('style', 'min-height: calc(100vh-50px)')
      }
    },
    init () {
      if (screenfull.enabled) {
        screenfull.on('change', this.change)
      }
    },
    destroy () {
      if (screenfull.enabled) {
        screenfull.off('change', this.change)
      }
    }
  }
}
</script>

<style scoped>
.screenfull-svg {
  display: inline-block;
  cursor: pointer;
  fill: #5a5e66;
  width: 20px;
  height: 20px;
  vertical-align: 10px;
}
</style>
