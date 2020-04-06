<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="12" :xs="24">
        <el-card id="bc_card1" style="height: 375px;" class="box-card">
          <div slot="header" class="clearfix">
            <span>能耗指标 {{this.selectedBuildingName}}</span>
          </div>
          <div>
            <div class="text-center">
              <BuildingDosageBarchart :buildingId="selectedBuildingId"></BuildingDosageBarchart>
            </div>
          </div>
        </el-card>
        <el-card id="bc_card2" style="overflow: auto;height:274px" class="box-card my-card">
          <div slot="header" class="clearfix">
            <span>建筑设备 {{this.selectedBuildingName}}</span>
          </div>
          <div>
            <div class="text-center">
              <el-table border style="width:100%" v-loading="loading" :data="filterDeviceList">
                <el-table-column label="序号" align="center" type="index" width="50" />
                <el-table-column label="建筑名称" align="center" prop="buildingName" />
                <el-table-column label="设备编号" align="center" prop="deviceCode" width="50" />
                <el-table-column label="设备名称" align="center" prop="deviceName"></el-table-column>
                <el-table-column label="设备描述" align="center" prop="deviceDescription" />
              </el-table>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12" :xs="24">
        <el-card
          id="bc_card3"
          class="img-card"
          body-style="min-height: 554px;
                      display: flex;
                      align-items: center;
                      justify-content: center;"
        >
          <div slot="header" class="clearfix">
            <span>建筑分布 生产三厂</span>
          </div>
          <div @click="handelClickBuilding" class="buildings-container">
            <el-image :src="imgSrc">
              <div slot="placeholder" class="image-slot">
                加载中
                <span class="dot">...</span>
              </div>
            </el-image>
            <el-tooltip effect="light" :content="buildingNameList[0]" placement="bottom-end">
              <div id="b1" class="buildings b1"></div>
            </el-tooltip>
            <el-tooltip effect="light" :content="buildingNameList[1]" placement="bottom-end">
              <div id="b2" class="buildings b2"></div>
            </el-tooltip>
            <el-tooltip effect="light" :content="buildingNameList[2]" placement="bottom-end">
              <div id="b3" class="buildings b3"></div>
            </el-tooltip>
            <el-tooltip effect="light" :content="buildingNameList[3]" placement="bottom-end">
              <div id="b4" class="buildings b4"></div>
            </el-tooltip>
            <el-tooltip effect="light" :content="buildingNameList[4]" placement="bottom-end">
              <div id="b5" class="buildings b5"></div>
            </el-tooltip>
            <el-tooltip effect="light" :content="buildingNameList[5]" placement="bottom-end">
              <div id="b6" class="buildings b6"></div>
            </el-tooltip>
            <el-tooltip effect="light" :content="buildingNameList[6]" placement="bottom-end">
              <div id="b7" class="buildings b67"></div>
            </el-tooltip>
            <el-tooltip effect="light" :content="buildingNameList[7]" placement="bottom-end">
              <div id="b8" class="buildings b8"></div>
            </el-tooltip>
            <el-tooltip effect="light" :content="buildingNameList[8]" placement="bottom-end">
              <div id="b9" class="buildings b9"></div>
            </el-tooltip>
            <el-tooltip effect="light" :content="buildingNameList[9]" placement="bottom-end">
              <div id="b10" class="buildings b10"></div>
            </el-tooltip>
            <el-tooltip effect="light" :content="buildingNameList[10]" placement="bottom-end">
              <div id="b11" class="buildings b11"></div>
            </el-tooltip>
            <el-tooltip effect="light" :content="buildingNameList[11]" placement="bottom-end">
              <div id="b12" class="buildings b12"></div>
            </el-tooltip>
            <el-tooltip effect="light" :content="buildingNameList[12]" placement="bottom-end">
              <div id="b13" class="buildings b13"></div>
            </el-tooltip>
            <el-tooltip effect="light" :content="buildingNameList[13]" placement="bottom-end">
              <div id="b14" class="buildings b14"></div>
            </el-tooltip>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="24" :xs="24">
        <el-card class="box-card my-card" style="min-height: 200px">
          <div slot="header" class="clearfix">
            <span>设备主要参数 {{this.selectedBuildingName}}</span>
          </div>
          <div>
            <div class="text-center" style="display:flex">
              <BuildingMainTagValueChart
                v-for="(item,index) in mainTagValueList"
                v-bind:key="index"
                :itemData="item"
              ></BuildingMainTagValueChart>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import imgSrc from '../../../assets/image/building-consumption.jpg'
import BuildingDosageBarchart from './buildingDosageBarchart'
import BuildingMainTagValueChart from './buildingMainTagValueChart'
import { listDevice } from '@/api/energy/device'
import {
  listBuildingDistribution,
  listBuildingMainMPValue
} from '@/api/energy/report'
import screenfull from 'screenfull'

export default {
  components: {
    BuildingDosageBarchart,
    BuildingMainTagValueChart
  },
  data() {
    return {
      imgSrc,
      selectedBuildingId: undefined,
      selectedBuildingName: '',
      // 遮罩层
      loading: true,
      deviceList: [],
      filterDeviceList: [],
      selectedDeviceName: '',
      buildingNameList: [],
      buildingIdList: [],
      mainTagValueList: []
    }
  },
  watch: {
    selectedBuildingId(newValue) {
      this.filterDeviceList = this.deviceList.filter(
        item => item.buildingId == newValue
      )
    }
  },
  created() {
    this.getDeviceList()
    this.getBuildingDistributionList()
  },
  mounted: function() {

    screenfull.on('change', function(){
      if (screenfull.isFullscreen) {
          const bc_card1 = document.getElementById('bc_card1')
          const bc_card2 = document.getElementById('bc_card2')
          const bc_card3 = document.getElementById('bc_card3')
          const bc_card1Height = bc_card1.clientHeight
          const bc_card2Height = bc_card2.clientHeight
          const bc_card3Height = bc_card3.clientHeight
          console.log(
            bc_card1Height + '=== ' + bc_card2Height + '===' + bc_card3Height
          )
      }
    })

    const that = this
    window.onresize = function resizeFunction() {
    }
  },
  methods: {
    getBuildingDistributionList() {
      listBuildingDistribution().then(res => {
        this.buildingNameList = res.data.map(item => item.buildingName)
        this.buildingIdList = res.data.map(item => item.buildingId)
        this.selectedBuildingId = this.buildingIdList[0]
        this.selectedBuildingName = this.buildingNameList[0]
        listBuildingMainMPValue(this.selectedBuildingId).then(res => {
          this.mainTagValueList = res.data
        })
      })
    },

    getDeviceList() {
      this.loading = true
      listDevice().then(res => {
        this.deviceList = res.rows
        this.filterDeviceList = this.deviceList.filter(
          item => item.buildingId == this.selectedBuildingId
        )
        this.loading = false
      })
    },
    handelClickBuilding(e) {
      var target = event.target
      if (!target.id) {
        return
      }
      switch (target.id) {
        case 'b1':
          this.selectedBuildingId = this.buildingIdList[0]
          this.selectedBuildingName = this.buildingNameList[0]
          break
        case 'b2':
          this.selectedBuildingId = this.buildingIdList[1]
          this.selectedBuildingName = this.buildingNameList[1]
          break
        case 'b3':
          this.selectedBuildingId = this.buildingIdList[2]
          this.selectedBuildingName = this.buildingNameList[2]
          break
        case 'b4':
          this.selectedBuildingId = this.buildingIdList[3]
          this.selectedBuildingName = this.buildingNameList[3]
          break
        case 'b5':
          this.selectedBuildingId = this.buildingIdList[4]
          this.selectedBuildingName = this.buildingNameList[4]
          break
        case 'b6':
          this.selectedBuildingId = this.buildingIdList[5]
          this.selectedBuildingName = this.buildingNameList[5]
          break
        case 'b7':
          this.selectedBuildingId = this.buildingIdList[6]
          this.selectedBuildingName = this.buildingNameList[6]
          break
        case 'b8':
          this.selectedBuildingId = this.buildingIdList[7]
          this.selectedBuildingName = this.buildingNameList[7]
          break
        case 'b9':
          this.selectedBuildingId = this.buildingIdList[8]
          this.selectedBuildingName = this.buildingNameList[8]
          break
        case 'b10':
          this.selectedBuildingId = this.buildingIdList[9]
          this.selectedBuildingName = this.buildingNameList[9]
          break
        case 'b11':
          this.selectedBuildingId = this.buildingIdList[10]
          this.selectedBuildingName = this.buildingNameList[10]
          break
        case 'b12':
          this.selectedBuildingId = this.buildingIdList[11]
          this.selectedBuildingName = this.buildingNameList[11]
          break
        case 'b13':
          this.selectedBuildingId = this.buildingIdList[12]
          this.selectedBuildingName = this.buildingNameList[12]
          break
        case 'b14':
          this.selectedBuildingId = this.buildingIdList[13]
          this.selectedBuildingName = this.buildingNameList[13]
          break
      }
    }
  }
}
</script>
<style lang="scss" >
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
  .b1 {
    width: 5.6%;
    height: 5.5%;
    top: 50.5%;
    left: 19.4%;
    transform: rotate(56deg) skewX(-13deg);
  }
  .b2 {
    width: 6.7%;
    height: 6%;
    top: 59.9%;
    left: 23.7%;
    transform: rotate(55deg) skewX(-11deg);
  }
  .b3 {
    width: 6.6%;
    height: 5.3%;
    top: 54.3%;
    left: 35.1%;
    transform: rotate(48deg) skewX(-20deg);
  }
  .b4 {
    width: 5.6%;
    height: 5%;
    top: 45.8%;
    left: 30.2%;
    transform: rotate(50deg) skewX(-20deg);
  }
  .b5 {
    width: 2.6%;
    height: 8%;
    top: 42%;
    left: 44.3%;
    transform: rotate(69deg) skewY(-24deg);
  }
  .b6 {
    width: 2.7%;
    height: 8.3%;
    top: 47.1%;
    left: 47.6%;
    transform: rotate(68deg) skewY(-24deg);
  }
  .b67 {
    width: 2.4%;
    height: 7.3%;
    top: 38%;
    left: 52.8%;
    transform: rotate(69deg) skewY(-27deg);
  }
  .b8 {
    width: 2.6%;
    height: 7.6%;
    top: 43%;
    left: 56.4%;
    transform: rotate(69deg) skewY(-27deg);
  }
  .b9 {
    width: 9.8%;
    height: 5.2%;
    top: 61.2%;
    left: 49.9%;
    transform: rotate(45deg) skewX(-22deg);
  }
  .b10 {
    width: 9.6%;
    height: 5%;
    top: 57.2%;
    left: 56.5%;
    transform: rotate(43deg) skewX(-21deg);
  }
  .b11 {
    width: 9.5%;
    height: 5%;
    top: 53.4%;
    left: 62.8%;
    transform: rotate(41deg) skewX(-21deg);
  }
  .b12 {
    width: 3%;
    height: 9%;
    top: 37.2%;
    left: 66.2%;
    transform: rotate(40deg) skewX(-27deg);
  }
  .b13 {
    width: 3%;
    height: 9%;
    top: 41.7%;
    right: 26.5%;
    transform: rotate(41deg) skewX(-25deg);
  }
  .b14 {
    width: 3.2%;
    height: 9%;
    top: 46.3%;
    right: 21.6%;
    transform: rotate(38deg) skewX(-27deg);
  }
}
</style>
