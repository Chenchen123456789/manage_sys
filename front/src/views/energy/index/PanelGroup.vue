<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :lg="4" :sm="12" :xs="12" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-electricity">
          <svg-icon class-name="card-panel-icon" icon-class="electricity" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            年总电量
            <br />（kWh）
          </div>
          <count-to
            :duration="2600"
            :end-val="yearSumValueOfElectricity"
            :start-val="0"
            class="card-panel-num"
          />
        </div>
      </div>
    </el-col>
    <el-col :lg="4" :sm="12" :xs="12" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-water">
          <svg-icon class-name="card-panel-icon" icon-class="water" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            年总水量
            <br />&nbsp;&nbsp;&nbsp;（t）
          </div>
          <count-to
            :duration="3000"
            :end-val="yearSumValueOfWater"
            :start-val="0"
            class="card-panel-num"
          />
        </div>
      </div>
    </el-col>
    <el-col :lg="4" :sm="12" :xs="12" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-air">
          <svg-icon class-name="card-panel-icon" icon-class="air" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            年总气量
            <br />&nbsp;（m³）
          </div>
          <count-to
            :duration="3200"
            :end-val="yearSumValueOfAir"
            :start-val="0"
            class="card-panel-num"
          />
        </div>
      </div>
    </el-col>
    <el-col :lg="4" :sm="12" :xs="12" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-lifeWater">
          <svg-icon class-name="card-panel-icon" icon-class="lifeWater" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            生活水压力
            <br />&nbsp;&nbsp;（MPa）
          </div>
          <count-to
            :decimals="2"
            :duration="3600"
            :end-val="lifeWaterPressure"
            :start-val="0"
            class="card-panel-num"
          />
        </div>
      </div>
    </el-col>
    <el-col :lg="4" :sm="12" :xs="12" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-fireWater">
          <svg-icon class-name="card-panel-icon" icon-class="fireWater" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            消防水压力
            <br />&nbsp;&nbsp;（MPa）
          </div>
          <count-to
            :decimals="2"
            :duration="3600"
            :end-val="fireWaterPressure"
            :start-val="0"
            class="card-panel-num"
          />
        </div>
      </div>
    </el-col>
    <el-col :lg="4" :sm="12" :xs="12" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-airPressure">
          <svg-icon class-name="card-panel-icon" icon-class="airPressure" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            空压机压力
            <br />&nbsp;&nbsp;（MPa）
          </div>
          <count-to
            :decimals="2"
            :duration="3600"
            :end-val="airCompressorPressure"
            :start-val="0"
            class="card-panel-num"
          />
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'

export default {
  components: {
    CountTo
  },
  props: {
    yearTotal: {
      type: Object,
      default: null
    },
    homePageChartSettingList: {
      type: Array,
      default: null
    }
  },
  data () {
    return {
      yearSumValueOfElectricity: 0,
      yearSumValueOfWater: 0,
      yearSumValueOfAir: 0,
      airCompressorPressure: 0,
      fireWaterPressure: 0,
      lifeWaterPressure: 0
    }
  },
  computed: {
    panelGroupData () {
      return {
        yearTotal: this.yearTotal,
        homePageChartSettingList: this.homePageChartSettingList
      }
    }
  },
  watch: {
    panelGroupData: {
      deep: true,
      handler (val) {
        this.setValues(val)
      }
    }
  },
  methods: {
    setValues (panelGroupData) {
      const homePageChartSettingList = panelGroupData.homePageChartSettingList
      if (homePageChartSettingList.length > 0) {
        this.lifeWaterPressure = homePageChartSettingList.find(item => item.placeholderId == 1).tagValue
        this.fireWaterPressure = homePageChartSettingList.find(item => item.placeholderId == 2).tagValue
        this.airCompressorPressure = homePageChartSettingList.find(item => item.placeholderId == 3).tagValue
      }

      const yearTotal = panelGroupData.yearTotal
      this.yearSumValueOfElectricity = yearTotal.currentElectricityDosage || 0
      this.yearSumValueOfWater = yearTotal.currentWaterDosage || 0
      this.yearSumValueOfAir = yearTotal.currentAirDosage || 0
    }
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: -2px;

  .card-panel-col {
    margin-bottom: 18px;
    padding-left: 8px !important;
    padding-right: 8px !important;
  }
  .card-panel-col:first-child {
    padding-left: 20px !important;
  }
  .card-panel-col:last-child {
    padding-right: 20px !important;
  }

  .card-panel {
    height: 108px;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, 0.05);
    border-color: rgba(0, 0, 0, 0.05);
    display: flex;
    align-items: center;
    justify-content: space-evenly;

    .icon-electricity {
      color: #40c9c6;
    }

    .card-panel-icon-wrapper {
      // float: left;
      // margin: 14px 0 0 14px;
      // padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      // float: right;
      font-weight: bold;
      // margin: 26px;
      // margin-left: 0px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width: 550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
