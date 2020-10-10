<template>
  <div>
    <div class="alarm">
      <audio
        loop="loop"
        preload="auto"
        ref="alarm"
        src="./static/audio/alarm.mp3"
        style="display:none"
      ></audio>
      <i
        :class="hasUnAck?'el-icon-warning alarm-icon':'el-icon-warning alarm-icon-disable'"
        @click="handleClick"
      ></i>

      <svg-icon
        :icon-class="audioSwitchStatus?'alarmAudioOn':'alarmAudioOff'"
        @click="handleSwitchAudio"
        style="font-size: 22px; cursor:pointer"
      />
    </div>
  </div>
</template>

<script>
import { getUnAckAlarmLog } from '@/api/energy/alarmLog'
import { getRouters } from '@/api/menu'

export default {
  name: 'Alarm',
  data () {
    return {
      audioSrc: './static/audio/alarm.mp3',
      hasUnAck: false,
      customerAudioSwitchStatus: false,
      audioSwitchStatus: false,
      audioPlayStatus: false,
      componentName: "",
      failedTimes: 0
    }
  },
  mounted () {
    this.getInfo()

    getRouters().then(res => {
      const routers = res.data
      let componentName = ""
      function findAlarmLog (routersArray = []) {
        for (let router of routersArray) {
          if (router.children && router.children.length > 0) {
            findAlarmLog(router.children)
          } else {
            if (router.component == 'energy/alarmLog') {
              componentName = router.name
            }
          }
        }
      }
      findAlarmLog(routers)
      this.componentName = componentName
    })
  },
  methods: {
    handleSwitchAudio () {
      if (!this.audioPlayStatus) {
        this.$refs.alarm.play()
        this.audioPlayStatus = true
      } else {
        this.$refs.alarm.pause()
        this.audioPlayStatus = false
      }
      this.customerAudioSwitchStatus = !this.customerAudioSwitchStatus
      this.audioSwitchStatus = !this.audioSwitchStatus
    },
    getInfo () {
      getUnAckAlarmLog().then(res => {
        if (res.data > 0) {
          this.hasUnAck = true
          if (this.customerAudioSwitchStatus) {
            this.audioSwitchStatus = true
          }
        } else {
          this.hasUnAck = false
          this.audioSwitchStatus = false
        }
      }).catch(e => {
        this.failedTimes++
      }).finally(
        () => {
          if (this.failedTimes > 20) {
            return
          }
          setTimeout(() => this.getInfo(), 5 * 1000)
        }
      )
    },
    handleClick () {
      this.$route.name != this.componentName && this.$router.push({ name: this.componentName })
    }
  }
}
</script>

<style lang="scss" scoped>
/* 定义keyframe动画，命名为blink */
@keyframes blink {
  0% {
    opacity: 1;
  }
  100% {
    opacity: 0;
  }
}
/* 添加兼容性前缀 */
@-webkit-keyframes blink {
  0% {
    opacity: 1;
  }
  100% {
    opacity: 0;
  }
}
@-moz-keyframes blink {
  0% {
    opacity: 1;
  }
  100% {
    opacity: 0;
  }
}
@-ms-keyframes blink {
  0% {
    opacity: 1;
  }
  100% {
    opacity: 0;
  }
}
@-o-keyframes blink {
  0% {
    opacity: 1;
  }
  100% {
    opacity: 0;
  }
}
/* 定义blink类*/
.alarm {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;

  .alarm-icon {
    visibility: visible;
    animation: blink 1s linear infinite;
    /* 其它浏览器兼容性前缀 */
    -webkit-animation: blink 1s linear infinite;
    -moz-animation: blink 1s linear infinite;
    -ms-animation: blink 1s linear infinite;
    -o-animation: blink 1s linear infinite;
    font-size: 26px;
    color: red;
    padding-right: 16px;
    cursor: pointer;
  }

  .alarm-icon-disable {
    visibility: hidden;
  }
}
</style>
