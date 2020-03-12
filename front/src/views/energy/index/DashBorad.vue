<template>
  <div :class="className" :style="{height:height,width:width,'min-width':'230px'}"></div>
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
      default: '225px'
    },
    dashboardData: {
      type: Object,
      default: {}
    }
  },
  data() {
    return {
      chart: null
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
        const max = Math.max(...resItem.map(item => item.tagValue))
        resData = resItem.map(item => {
          return { name: item.description, value: item.tagValue }
        })
        this.chart.setOption({
          series: [
            {
              data: resData,
              // min: 0,
              // max: max,
              // center:['50%','40%'],
              axisLine: {
                // 坐标轴线
                lineStyle: {
                  // 属性lineStyle控制线条样式
                  width: 5
                }
              },
              splitLine: {
                // 分隔线
                length: 10, // 属性length控制线长
                lineStyle: {
                  // 属性lineStyle（详见lineStyle）控制线条样式
                  color: 'auto'
                }
              },
              axisTick: {
                // 坐标轴小标记
                // length: 15,        // 属性length控制线长
                lineStyle: {
                  // 属性lineStyle控制线条样式
                  color: 'auto'
                }
              },
              detail: {
                fontSize: 20
              }
            }
          ]
        })
      }
    },
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption({
         tooltip: {
          trigger: 'item',
        },
        series: [
          {
            // name: '读数',
            type: 'gauge',
            data: [{ value: 0 }]
          }
        ]
      })
    }
  }
}
</script>
