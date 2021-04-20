<template>
  <div class="dashboard-container">
    <el-row class="container">
      <el-col :span="24">
        <CountPanel></CountPanel>
      </el-col>
      <el-row class="middle-part">
        <el-col :span="6">
          <BarChart className="barchart1" :chartData="chartData1"></BarChart>
        </el-col>
        <el-col :span="12">
          <div class="homepage-background">
            <el-image fit="contain" class="image" :src="homepageBackground"></el-image>
            <div @click="handleClickBox(1)" class="box box1">
              <div class="front"></div>
              <div class="back"></div>
              <div class="left"></div>
              <div class="right"></div>
              <div class="top"></div>
              <div class="bottom"></div>
            </div>
            <div @click="handleClickBox(2)" class="box box2">
              <div class="front"></div>
              <div class="back"></div>
              <div class="left"></div>
              <div class="right"></div>
              <div class="top"></div>
              <div class="bottom"></div>
            </div>
            <div @click="handleClickBox(3)" class="box box3">
              <div class="front"></div>
              <div class="back"></div>
              <div class="left"></div>
              <div class="right"></div>
              <div class="top"></div>
              <div class="bottom"></div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <BarChart className="barchart1" :chartData="chartData2"></BarChart>
        </el-col>
      </el-row>
      <el-col :span="12">
        <LineChart className="chart2" :chartData="chartData3"></LineChart>
      </el-col>
      <el-col :span="12">
        <LineChart className="chart2" :chartData="chartData4"></LineChart>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import homepageBackground from '../../../assets/image/homepage-background.jpg'
import BarChart from './BarChart'
import resize from '../../dashboard/mixins/resize'
import CountPanel from './countPanel'
import LineChart from './LineChart'

export default {
  name: 'Index',
  components: {
    CountPanel,
    BarChart,
    LineChart,
  },
  mixins: [resize],
  data() {
    return {
      homepageBackground,
      chartData1: {},
      chartData2: {},
      chartData3: {},
      chartData4: {},
    }
  },
  created() {},
  mounted() {
    setTimeout(() => {
      this.chartData1 = {
        title: '各月总能耗',
        yData: [10, 20, 30, 50, 20, 30, 11, 50, 16, 15, 55, 5],
      }
      this.chartData2 = {
        title: '各月空调',
        yData: [10, 20, 30, 50, 20, 30, 11, 50, 16, 15, 55, 5],
      }
      this.chartData3 = {
        title: '各月冷水机组',
        yData: [10, 20, 30, 50, 20, 70, 19, 30, 16, 15, 55, 5],
      }
      this.chartData4 = {
        title: '各月空压机',
        yData: [10, 20, 30, 55, 20, 20, 71, 50, 16, 15, 55, 5],
      }
    }, 1000)
  },
  methods: {
    handleClickBox(target) {
      console.log(target)
      switch (target) {
        case 1:
          this.chartData1.yData = [10, 20, 30, 50, 20, 30, 11, 50, 16, 15, 55, 5].map(item => Math.ceil(item/2))
          this.chartData2.yData = [10, 20, 30, 50, 20, 30, 11, 50, 16, 15, 55, 5].map(item => Math.ceil(item/2))
          this.chartData3.yData = [10, 20, 30, 50, 20, 30, 11, 50, 16, 15, 55, 5].map(item => Math.ceil(item/2))
          this.chartData4.yData = [10, 20, 30, 50, 20, 30, 11, 50, 16, 15, 55, 5].map(item => Math.ceil(item/2))
          break
        case 2: 
          this.chartData1.yData = [10, 20, 30, 50, 20, 30, 11, 50, 16, 15, 55, 5].map(item => Math.ceil(item/3))
          this.chartData2.yData = [10, 20, 30, 50, 20, 30, 11, 50, 16, 15, 55, 5].map(item => Math.ceil(item/3))
          this.chartData3.yData = [10, 20, 30, 50, 20, 30, 11, 50, 16, 15, 55, 5].map(item => Math.ceil(item/3))
          this.chartData4.yData = [10, 20, 30, 50, 20, 30, 11, 50, 16, 15, 55, 5].map(item => Math.ceil(item/3))
          break
        case 3:
          this.chartData1.yData = [10, 20, 30, 50, 20, 30, 11, 50, 16, 15, 55, 5].map(item => Math.ceil(item/4))
          this.chartData2.yData = [10, 20, 30, 50, 20, 30, 11, 50, 16, 15, 55, 5].map(item => Math.ceil(item/4))
          this.chartData3.yData = [10, 20, 30, 50, 20, 30, 11, 50, 16, 15, 55, 5].map(item => Math.ceil(item/4))
          this.chartData4.yData = [10, 20, 30, 50, 20, 30, 11, 50, 16, 15, 55, 5].map(item => Math.ceil(item/4))
          break
        default:
          break
      }

      
    },
  },
}
</script>

<style  lang="scss">
// px单位转成vw单位
@function px2vw($size: 14px, $width: 1800px) {
  @if (type-of($size) == 'number' and unit($size) == 'px') {
    @return $size / $width * 100vw;
  } @else {
    @return $size;
  }
}

@function px2vh($size: 14px, $height: 1146px) {
  @if (type-of($size) == 'number' and unit($size) == 'px') {
    @return $size / $height * 100vh;
  } @else {
    @return $size;
  }
}

.dashboard-container {
  position: relative;

  .container{
    height: 100vh;
    padding: 20px; 
    background-color: rgb(240, 242, 245);
  }

  .barchart1 {
    height: px2vw(576px) !important;
  }
  .chart2 {
    height: px2vw(280px) !important;
  }

  .middle-part {
    display: flex;
    width: 100%;
    align-items: center;
    padding-bottom: 20px;
  }

  .homepage-background {
    position: relative;
    // background: src('../../../assets/image/homepage-background.jpg');

    .image {
      max-height: px2vw(574px);
      img{
       max-height: px2vw(574px);
      }
    }
  }
  .box {
    // opacity: 0;
    position: absolute;
    cursor: pointer;
    // top: px2vw(34px);
    // right: px2vw(178px);
    transform: rotateX(31deg) rotateY(-26deg) rotateZ(-7deg);
    /*让子元素保留3d变换效果*/
    transform-style: preserve-3d;
    /*添加透视景深效果*/
    perspective: 100;
    /*设置透视的观察角度*/
    perspective-origin: 10% 10%;
    // -webkit-perspective: 2000; /* Safari 和 Chrome */
    // -webkit-perspective-origin: 20% 20%; /* Safari 和 Chrome */
  }
  .box1 {
    top: 16%;
    right: 55.9%;
  }
  .box2 {
    top: 21.5%;
    right: 52%;
  }

  .box3 {
    transform: rotateX(37deg) rotateY(-30deg) rotateZ(-7deg);
    top: 39.5%;
    right: 41.5%;
  }

  .box:hover {
    opacity: 1;
  }

  .box > div {
    height: px2vw(26px);
    width: px2vw(100px);
    position: absolute;
    opacity: 0.6;
    background-color: rgba(64, 158, 255, 0.5);
    border: solid 1px rgb(64, 158, 255);
  }
  .front {
    /*变化完毕后会回到原始状态*/
    transform: translateZ(px2vw(20px));
  }
  .back {
    transform: translateZ(px2vw(-20px)) rotateY(180deg);
  }
  .left {
    height: px2vw(26px) !important;
    width: px2vw(40px) !important;
    /*移动加旋转*/
    transform: translateX(px2vw(-20px)) rotateY(-90deg);
  }
  .right {
    height: px2vw(26px) !important;
    width: px2vw(40px) !important;
    transform: translateX(px2vw(80px)) rotateY(90deg);
  }
  .top {
    height: px2vw(40px) !important;
    transform: translateY(px2vw(-20px)) rotateX(90deg);
  }
  .bottom {
    height: px2vw(40px) !important;
    transform: translateY(px2vw(6px)) rotateX(-90deg);
  }
}
</style>
