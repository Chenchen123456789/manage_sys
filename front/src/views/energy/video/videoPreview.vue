<template>
  <div class="app-container">
    <video id="videoId" controls="true" height="500" width="auto"></video>
  </div>
</template>

<script>
import flvjs from 'flv.js'
import { videoPreview } from '@/api/energy/video'

export default {
  name: 'videoPreview.vue',
  data () {
    return {}
  },
  mounted () {
    this.init()
    if (flvjs.isSupported()) {
      var videoElement = document.getElementById('videoId');
      var flvPlayer = flvjs.createPlayer({
        type: 'flv',
        url: 'http://localhost:85/live/livestream.flv'
      });
      flvPlayer.attachMediaElement(videoElement);
      flvPlayer.load();
      //   flvPlayer.play();
    }
  },
  methods: {
    init(){
      videoPreview().then(res=>{
        console.log(111)
      })
    }
  }
}
</script>