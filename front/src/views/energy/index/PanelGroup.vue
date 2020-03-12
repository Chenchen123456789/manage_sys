<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :xs="12" :sm="12" :lg="4" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-people">
          <svg-icon icon-class="peoples" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            年总电量
            <br />（kWh）
          </div>
          <count-to
            :start-val="0"
            :end-val="yearSumValueOfElectricity"
            :duration="2600"
            class="card-panel-num"
          />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="4" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-message">
          <svg-icon icon-class="message" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            年总水量
            <br />&nbsp;&nbsp;&nbsp;（t）
          </div>
          <count-to
            :start-val="0"
            :end-val="yearSumValueOfWater"
            :duration="3000"
            class="card-panel-num"
          />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="4" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-money">
          <svg-icon icon-class="money" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            年总气量
            <br />&nbsp;（m³）
          </div>
          <count-to
            :start-val="0"
            :end-val="yearSumValueOfAir"
            :duration="3200"
            class="card-panel-num"
          />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="4" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-shopping">
          <svg-icon icon-class="shopping" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            生活水压力
            <br />&nbsp;&nbsp;（MPa）
          </div>
          <count-to
            :start-val="0"
            :end-val="lifeWaterPressure"
            :duration="3600"
            class="card-panel-num"
          />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="4" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-shopping">
          <svg-icon icon-class="shopping" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            消防水压力
            <br />&nbsp;&nbsp;（MPa）
          </div>
          <count-to
            :start-val="0"
            :end-val="fireWaterPressure"
            :duration="3600"
            class="card-panel-num"
          />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="4" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-shopping">
          <svg-icon icon-class="shopping" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            空压机压力
            <br />&nbsp;&nbsp;（MPa）
          </div>
          <count-to
            :start-val="0"
            :end-val="airCompressorPressure"
            :duration="3600"
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
    panelGroupData: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      yearSumValueOfElectricity: 0,
      yearSumValueOfWater: 0,
      yearSumValueOfAir: 0,
      airCompressorPressure: 0,
      fireWaterPressure: 0,
      lifeWaterPressure: 0
    }
  },
  watch: {
    panelGroupData: {
      deep: true,
      handler(val) {
        this.setValues(val)
      }
    }
  },
  methods: {
    setValues(panelGroupData) {
      const homePageChartSettingList = panelGroupData.homePageChartSettingList
      if (homePageChartSettingList.length > 0) {
        this.lifeWaterPressure = homePageChartSettingList[0].tagValue
        this.fireWaterPressure = homePageChartSettingList[1].tagValue
        this.airCompressorPressure = homePageChartSettingList[2].tagValue
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
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
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
    cursor: pointer;
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

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3;
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3;
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
