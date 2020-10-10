<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from '../index/mixins/resize'
import { queryBuildingDosage } from '../../../api/energy/report'

const animationDuration = 6000

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '380px'
    },
    buildingId: {
      type: Number,
      default: null
    }
  },
  data () {
    return {
      chart: null,
      queryParams: {
        buildingId: this.buildingId
      },
      qbuildingDosageData: [],
      qbuildingDosageData: [],
      qbuildingDosageData: []
    }
  },
  watch: {
    buildingId: {
      deep: true,
      handler (newValue) {
        this.queryParams.buildingId = newValue
        this.getBuildingDosage()
      }
    }
  },
  created () {
    this.getBuildingDosage()
  },
  mounted () {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy () {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    getBuildingDosage () {
      this.queryParams.buildingId && queryBuildingDosage(this.queryParams).then(res => {
        const data = res.data
        const dayDosage = data.dayDosage
        const monthDosage = data.monthDosage
        const yearDosage = data.yearDosage
        let airArray = []
        let elecArray = []
        let waterArray = []
        if (dayDosage) {
          const currentAirSumValue = dayDosage.currentAirSumValue.toFixed(2) || 0
          const currentElectricitySumValue =
            dayDosage.currentElectricitySumValue.toFixed(2) || 0
          const currentWaterSumValue = dayDosage.currentWaterSumValue.toFixed(2) || 0
          airArray[0] = currentAirSumValue
          elecArray[0] = currentElectricitySumValue
          waterArray[0] = currentWaterSumValue
        }
        if (monthDosage) {
          const currentAirSumValue = monthDosage.currentAirSumValue.toFixed(2) || 0
          const currentElectricitySumValue =
            monthDosage.currentElectricitySumValue.toFixed(2) || 0
          const currentWaterSumValue = monthDosage.currentWaterSumValue.toFixed(2) || 0
          airArray[1] = currentAirSumValue
          elecArray[1] = currentElectricitySumValue
          waterArray[1] = currentWaterSumValue
        }
        if (yearDosage) {
          const currentAirSumValue = yearDosage.currentAirSumValue.toFixed(2) || 0
          const currentElectricitySumValue =
            yearDosage.currentElectricitySumValue.toFixed(2) || 0
          const currentWaterSumValue = yearDosage.currentWaterSumValue.toFixed(2) || 0
          airArray[2] = currentAirSumValue
          elecArray[2] = currentElectricitySumValue
          waterArray[2] = currentWaterSumValue
        }
        this.qbuildingDosageData = airArray
        this.dbuildingDosageData = elecArray
        this.sbuildingDosageData = waterArray
        this.chart.setOption({
          series: [
            {
              data: this.qbuildingDosageData
            },
            {
              data: this.dbuildingDosageData
            },
            {
              data: this.sbuildingDosageData
            }
          ]
        })
      })
    },
    initChart () {
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          top: 40,
          left: '2%',
          right: '3%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            data: ['日累计用量', '月累计用量', '年累计用量'],
            axisTick: { show: false }
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        legend: {
          data: ['气', '电', '水'],
          left: 'right'
        },
        series: [
          {
            name: '气',
            type: 'bar',
            barGap: 0,
            data: this.qbuildingDosageData,
            animationDuration
          },
          {
            name: '电',
            type: 'bar',
            data: this.dbuildingDosageData,
            animationDuration
          },
          {
            name: '水',
            type: 'bar',
            data: this.sbuildingDosageData,
            animationDuration
          }
        ]
      })
    }
  }
}
</script>
