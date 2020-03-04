<template>
  <div class="dashboard-editor-container">
    <PanelGroup :panelGroupData="homePageChartSettingList" />
    <el-row :gutter="32" class="second-part">
      <el-col
        style="display: flex; flex-direction: column; align-items: center;justify-content: center;position: relative;"
        :span="4"
        v-for="(item, index) in [3,4,5,6,7,8]"
        :xs="24"
      >
        <Dashboard
          :dashboardData="{dashboardDataList: groupedHomePageSettingData, placeholderId: item}"
        ></Dashboard>
        <div style="position: absolute;bottom: 0;">{{getDashBoardDesc(item)}}</div>
      </el-col>
    </el-row>

    <el-row :gutter="32" class="third-part">
      <el-col :span="8" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>电量统计</span>
          </div>
          <div>
            <div class="text-center">
              <el-carousel indicator-position="outside">
                <el-carousel-item>
                  <BuildingDosageOfBarChart :chartData="dayDosageOfElectricity"></BuildingDosageOfBarChart>
                </el-carousel-item>
                <el-carousel-item>
                  <BuildingDosageOfPieChart :chartData="monthDosageOfElectricity"></BuildingDosageOfPieChart>
                </el-carousel-item>
                <el-carousel-item>
                  <BuildingDosageOfPie2Chart :chartData="yearDosageOfElectricity"></BuildingDosageOfPie2Chart>
                </el-carousel-item>
              </el-carousel>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>水量统计</span>
          </div>
          <div>
            <div class="text-center">
              <el-carousel indicator-position="outside">
                <el-carousel-item>
                  <BuildingDosageOfBarChart :chartData="dayDosageOfWater"></BuildingDosageOfBarChart>
                </el-carousel-item>
                <el-carousel-item>
                  <BuildingDosageOfPieChart :chartData="monthDosageOfWater"></BuildingDosageOfPieChart>
                </el-carousel-item>
                <el-carousel-item>
                  <BuildingDosageOfPie2Chart :chartData="yearDosageOfWater"></BuildingDosageOfPie2Chart>
                </el-carousel-item>
              </el-carousel>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>气量统计</span>
          </div>
          <div>
            <div class="text-center">
              <el-carousel indicator-position="outside">
                <el-carousel-item>
                  <BuildingDosageOfBarChart :chartData="dayDosageOfAir"></BuildingDosageOfBarChart>
                </el-carousel-item>
                <el-carousel-item>
                  <BuildingDosageOfPieChart :chartData="monthDosageOfAir"></BuildingDosageOfPieChart>
                </el-carousel-item>
                <el-carousel-item>
                  <BuildingDosageOfPie2Chart :chartData="yearDosageOfAir"></BuildingDosageOfPie2Chart>
                </el-carousel-item>
              </el-carousel>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="32" class="fourth-part">
      <el-col :span="12" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>用电、水、气环比</span>
          </div>
          <div>
            <div class="text-center">
              <el-carousel indicator-position="outside">
                <el-carousel-item>
                  <BuildingSequentialDosageOfElectricity :chartData="seqDosageData"></BuildingSequentialDosageOfElectricity>
                </el-carousel-item>
                <el-carousel-item>
                  <BuildingSequentialDosageOfAir :chartData="seqDosageData"></BuildingSequentialDosageOfAir>
                </el-carousel-item>
                <el-carousel-item>
                  <BuildingSequentialDosageOfWater :chartData="seqDosageData"></BuildingSequentialDosageOfWater>
                </el-carousel-item>
              </el-carousel>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>电压棒图</span>
          </div>
          <div>
            <div class="text-center">
              <el-carousel indicator-position="outside">
                <el-carousel-item>
                  <VoltageStickFigure :chartData="groupedHomePageSettingData"></VoltageStickFigure>
                </el-carousel-item>
              </el-carousel>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import BuildingDosageOfPieChart from './buildingDosageOfPieChart'
import BuildingDosageOfBarChart from './buildingDosageOfBarChart'
import BuildingDosageOfPie2Chart from './buildingDosageOfPie2Chart'
import VoltageStickFigure from './voltageStickFigure'
import BuildingSequentialDosageOfAir from './buildingSequentialDosageOfAir'
import BuildingSequentialDosageOfElectricity from './buildingSequentialDosageOfElectricity'
import BuildingSequentialDosageOfWater from './buildingSequentialDosageOfWater'
import PanelGroup from './PanelGroup'
import Dashboard from './DashBorad'
import {
  listHomePageChartSetting,
  queryBuildingDosageforHomePage
} from '@/api/energy/report'

export default {
  name: 'Index',
  components: {
    BuildingDosageOfPieChart,
    BuildingDosageOfBarChart,
    BuildingDosageOfPie2Chart,
    VoltageStickFigure,
    PanelGroup,
    Dashboard,
    BuildingSequentialDosageOfAir,
    BuildingSequentialDosageOfWater,
    BuildingSequentialDosageOfElectricity
  },
  data() {
    return {
      homePageChartSettingList: [],
      groupedHomePageSettingData: [],
      dayDosageOfElectricity: [],
      dayDosageOfAir: [],
      dayDosageOfWater: [],
      monthDosageOfElectricity: [],
      monthDosageOfAir: [],
      monthDosageOfWater: [],
      yearDosageOfElectricity: [],
      yearDosageOfAir: [],
      yearDosageOfWater: [],
      seqDosageData: {}
    }
  },
  created() {
    this.getHomePageChartSettingList()
    this.getBuildingDosage()
  },
  methods: {
    getBuildingDosage() {
      queryBuildingDosageforHomePage().then(res => {
        const buildingDosage = res.data
        this.seqDosageData = res.data
        if (buildingDosage) {
          const dayDosage = buildingDosage.dayDosage
          const monthDosage = buildingDosage.monthDosage
          const yearDosage = buildingDosage.yearDosage
          if (dayDosage) {
            this.dayDosageOfElectricity = dayDosage.map(item => {
              return {
                buildingName: item.buildingName,
                sumValue: item.currentElectricitySumValue
              }
            })

            this.dayDosageOfAir = dayDosage.map(item => {
              return {
                buildingName: item.buildingName,
                sumValue: item.currentAirSumValue
              }
            })

            this.dayDosageOfWater = dayDosage.map(item => {
              return {
                buildingName: item.buildingName,
                sumValue: item.currentWaterSumValue
              }
            })
          }

          if (monthDosage) {
            this.monthDosageOfElectricity = monthDosage.map(item => {
              return {
                buildingName: item.buildingName,
                sumValue: item.currentElectricitySumValue
              }
            })

            this.monthDosageOfAir = monthDosage.map(item => {
              return {
                buildingName: item.buildingName,
                sumValue: item.currentAirSumValue
              }
            })

            this.monthDosageOfWater = monthDosage.map(item => {
              return {
                buildingName: item.buildingName,
                sumValue: item.currentWaterSumValue
              }
            })
          }

          if (yearDosage) {
            this.yearDosageOfElectricity = yearDosage.map(item => {
              return {
                buildingName: item.buildingName,
                sumValue: item.currentElectricitySumValue
              }
            })
            this.yearDosageOfAir = yearDosage.map(item => {
              return {
                buildingName: item.buildingName,
                sumValue: item.currentAirSumValue
              }
            })
            this.yearDosageOfWater = yearDosage.map(item => {
              return {
                buildingName: item.buildingName,
                sumValue: item.currentWaterSumValue
              }
            })
          }
        }
      })
    },
    getDashBoardDesc(placeholderId) {
      const homePageChartSettingList = this.homePageChartSettingList
      let desc = ''
      if (homePageChartSettingList.length > 0) {
        const obj = homePageChartSettingList.find(
          item => item.placeholderId === placeholderId
        )
        desc = obj ? obj.description : ''
      }
      return desc
    },
    groupArray(arr) {
      let map = {},
        dest = []
      for (const i in arr) {
        let ai = arr[i]
        if (!map[ai.placeholderId]) {
          dest.push({
            placeholderId: ai.placeholderId,
            item: [ai]
          })
          map[ai.placeholderId] = ai
        } else {
          for (const j in dest) {
            let dj = dest[j]
            if (dj.placeholderId == ai.placeholderId) {
              dj.item.push(ai)
              break
            }
          }
        }
      }
      return dest
    },
    getHomePageChartSettingList() {
      listHomePageChartSetting().then(res => {
        this.homePageChartSettingList = res.data
        this.groupedHomePageSettingData = this.groupArray(res.data)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.second-part {
  background: white;
  margin-bottom: 30px;
  padding-bottom: 18px;
  margin-left: 0px !important;
  margin-right: 0px !important;
}
.third-part {
  margin-left: 0px !important;
  margin-right: 0px !important;
  margin-bottom: 30px;
  background: white;
  .el-col:first-child {
    padding-left: 0px !important;
    padding-right: 0px !important;
  }
  .el-col:last-child {
    padding-left: 0px !important;
    padding-right: 0px !important;
  }
}
.fourth-part {
  margin-left: 0px !important;
  margin-right: 0px !important;
  background: white;
  .el-col:first-child {
    padding-left: 0px !important;
    padding-right: 10px !important;
  }
  .el-col:last-child {
    padding-left: 10px !important;
    padding-right: 0px !important;
  }
}
.dashboard-editor-container {
  min-height: 100vh;
  max-height: 100%;
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
