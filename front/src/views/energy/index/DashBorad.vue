<template>
  <div :class="className" :style="{height:height,width:width}"></div>
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from '../../dashboard/mixins/resize'

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
      default: '180px'
    },
    dashboardData: {
      type: Object,
      default: {}
    }
  },
  data() {
    return {
      chart: null,
    }
  },
  watch: {
    dashboardData: {
      deep: true,
      handler(val) {
        this.setChartData(val)
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    setChartData(dashboardData) {
      const placeholderId = dashboardData.placeholderId
      const dashboardDataList = dashboardData.dashboardDataList
      if (dashboardDataList.length > 0) {
        const resItem = dashboardDataList.find(
          item => item.placeholderId === placeholderId
        ).item
        let resData = []
        resData = resItem.map(item => {
          return { value: item.tagValue }
        })
        this.chart.setOption({
          series: [
            {
              type: 'gauge',
              data: resData
            }
          ]
        })
      }
    },
    initChart() {
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
          top: 10,
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        series: [
          {
            type: 'gauge',
            data: [{value: 0}]
          }
        ]
      })
    }
  }
}
</script>
