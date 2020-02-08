<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="10" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>能耗指标</span>
          </div>
          <div>
            <div class="text-center"></div>
          </div>
        </el-card>
        <el-card class="box-card my-card">
          <div slot="header" class="clearfix">
            <span>建筑设备</span>
          </div>
          <div>
            <div class="text-center"></div>
          </div>
        </el-card>
        <el-card class="box-card my-card">
          <div slot="header" class="clearfix">
            <span>设备主要参数</span>
          </div>
          <div>
            <div class="text-center"></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="14" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>建筑分布 生产三厂</span>
          </div>
          <div class="buildings-container">
            <el-image :src="imgSrc">
              <div slot="placeholder" class="image-slot">
                加载中
                <span class="dot">...</span>
              </div>
            </el-image>
            <el-tooltip effect="light" content="29#建筑" placement="bottom-end">
              <div class="buildings b29"></div>
            </el-tooltip>
            <el-tooltip effect="light" content="30#建筑" placement="bottom-end">
              <div class="buildings b30"></div>
            </el-tooltip>
            <el-tooltip effect="light" content="31#建筑" placement="bottom-end">
              <div class="buildings b31"></div>
            </el-tooltip>
            <el-tooltip effect="light" content="32#建筑" placement="bottom-end">
              <div class="buildings b32"></div>
            </el-tooltip>
            <el-tooltip effect="light" content="33#建筑" placement="bottom-end">
              <div class="buildings b33"></div>
            </el-tooltip>
            <el-tooltip effect="light" content="34#建筑" placement="bottom-end">
              <div class="buildings b34"></div>
            </el-tooltip>
            <el-tooltip effect="light" content="35#建筑" placement="bottom-end">
              <div class="buildings b35"></div>
            </el-tooltip>
            <el-tooltip effect="light" content="36#建筑" placement="bottom-end">
              <div class="buildings b36"></div>
            </el-tooltip>
            <el-tooltip effect="light" content="37#建筑" placement="bottom-end">
              <div class="buildings b37"></div>
            </el-tooltip>
            <el-tooltip effect="light" content="38#建筑" placement="bottom-end">
              <div class="buildings b38"></div>
            </el-tooltip>
            <el-tooltip effect="light" content="39#建筑" placement="bottom-end">
              <div class="buildings b39"></div>
            </el-tooltip>
            <el-tooltip effect="light" content="40#建筑" placement="bottom-end">
              <div class="buildings b40"></div>
            </el-tooltip>
            <el-tooltip effect="light" content="41#建筑" placement="bottom-end">
              <div class="buildings b41"></div>
            </el-tooltip>
            <el-tooltip effect="light" content="42#建筑" placement="bottom-end">
              <div class="buildings b42"></div>
            </el-tooltip>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import imgSrc from "../../assets/image/building-consumption.jpg";
export default {
  data() {
    return {
      imgSrc,
    };
  },
  created() {},
  methods: {}
};
</script>
<style lang="scss" scoped>
.my-card {
  margin-top: 10px;
}
.buildings-container {
  position: relative;

  .buildings {
    position: absolute;
    border: 1px white solid;
    transform: rotate(59deg);
    background-color: rgba(170, 209, 252, 0.75);
    cursor: pointer;
  }
  .b29 {
    width: 5.6%;
    height: 5.5%;
    top: 50.5%;
    left: 19.4%;
    transform: rotate(56deg) skewX(-13deg);
  }
  .b30 {
    width: 6.7%;
    height: 6%;
    top: 59.9%;
    left: 23.7%;
    transform: rotate(55deg) skewX(-11deg);
  }
  .b31 {
    width: 6.6%;
    height: 5.3%;
    top: 54.3%;
    left: 35.1%;
    transform: rotate(48deg) skewX(-20deg);
  }
  .b32 {
    width: 5.6%;
    height: 5%;
    top: 45.8%;
    left: 30.2%;
    transform: rotate(50deg) skewX(-20deg);
  }
  .b33 {
    width: 2.6%;
    height: 8%;
    top: 42%;
    left: 44.3%;
    transform: rotate(69deg) skewY(-24deg);
  }
  .b34 {
    width: 2.7%;
    height: 8.3%;
    top: 47.1%;
    left: 47.6%;
    transform: rotate(68deg) skewY(-24deg);
  }
  .b35 {
    width: 2.4%;
    height: 7.3%;
    top: 38%;
    left: 52.8%;
    transform: rotate(69deg) skewY(-27deg);
  }
  .b36 {
    width: 2.6%;
    height: 7.6%;
    top: 43%;
    left: 56.4%;
    transform: rotate(69deg) skewY(-27deg);
  }
  .b37 {
    width: 9.8%;
    height: 5.2%;
    top: 61.2%;
    left: 49.9%;
    transform: rotate(45deg) skewX(-22deg);
  }
  .b38 {
    width: 9.6%;
    height: 5%;
    top: 57.2%;
    left: 56.5%;
    transform: rotate(43deg) skewX(-21deg);
  }
  .b39 {
    width: 9.5%;
    height: 5%;
    top: 53.4%;
    left: 62.8%;
    transform: rotate(41deg) skewX(-21deg);
  }
  .b40 {
    width: 3%;
    height: 9%;
    top: 37.2%;
    left: 66.2%;
    transform: rotate(40deg) skewX(-27deg);
  }
  .b41 {
    width: 3%;
    height: 9%;
    top: 41.7%;
    right: 26.5%;
    transform: rotate(41deg) skewX(-25deg);
  }
  .b42 {
    width: 3.2%;
    height: 9%;
    top: 46.3%;
    right: 21.6%;
    transform: rotate(38deg) skewX(-27deg);
  }
}
</style>
