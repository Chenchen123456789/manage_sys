<template>
  <div>
    <div class="alarm">
      <audio
        style="display:none"
        loop="loop"
        preload="auto"
        ref="alarm"
        src="./static/audio/alarm.mp3"
      ></audio>
      <i
        :class="hasUnAck?'el-icon-warning alarm-icon':'el-icon-warning alarm-icon-disable'"
        @click="handleClick"
      ></i>

      <svg-icon
        @click="handleSwitchAudio"
        style="font-size: 22px; cursor:pointer"
        :icon-class="audioSwitchStatus?'alarmAudioOn':'alarmAudioOff'"
      />
    </div>
  </div>
</template>

<script>
import { getUnAckAlarmLog } from '@/api/energy/alarmLog'

export default {
  name: 'Alarm',
  data () {
    return {
      audioSrc: './static/audio/alarm.mp3',
      hasUnAck: false,
      customerAudioSwitchStatus: false,
      audioSwitchStatus: false,
      audioPlayStatus: false
    }
  },
  mounted () {
    this.getInfo()
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
      setInterval(() => {
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
        })
      }, 2000)

    },
    handleClick () {
      if (this.$route.fullPath !== "/energy/alarmLog") {
        this.$router.push("/energy/alarmLog")
      }
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
