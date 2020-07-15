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
    dashboardDataList: {
      type: Array,
      default: null
    },
    placeholderId: {
      type: Number,
      default: null
    },
  },
  data () {
    return {
      chart: null
    }
  },
  computed: {
    dashboardData () {
      return {
        placeholderId: this.placeholderId,
        dashboardDataList: this.dashboardDataList
      }
    }
  },
  watch: {
    dashboardData: {
      deep: true,
      handler (val) {
        this.setChartData(val)
      }
    }
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
    setChartData (dashboardData) {
      const placeholderId = dashboardData.placeholderId
      const dashboardDataList = dashboardData.dashboardDataList
      console.log(dashboardData)
      if (dashboardDataList.length > 0) {
        try {
          var resItem = dashboardDataList.find(
            item => item.placeholderId === placeholderId
          ).item
        } catch(e){
          console.log(e)
          return
        }
        let resData = []
        let maxValue = Math.max(...resItem.map(item => item.tagValue))
        if (maxValue <= 1) {
          maxValue = 1
        } else if (maxValue > 1 && maxValue <= 10) {
          maxValue = 10
        } else if (maxValue > 10 && maxValue <= 100) {
          maxValue = 100
        } else if (maxValue > 100) {
          let number2 = Math.ceil(maxValue / 100);
          maxValue = number2 * 100
        }
        resData = resItem.map(item => {
          return { value: item.tagValue, name: item.tagName }
        })
        const seriesData = []
        let index = 3
        let colorIndex = 0
        const colorArray = ['green', 'purple', '#FF6347', '#FFD700', 'red']
        for (const item of resData) {
          const seriesItem = {}
          seriesItem = {
            type: 'gauge',
            name: item.name,
            max: maxValue,
            center: ['50%', '46%'],
            // splitNumber: 10,
            data: [{ value: item.value }],
            axisLine: {
              lineStyle: {
                width: 5
              }
            },
            splitLine: {
              length: 13,
              lineStyle: {
                color: 'auto'
              }
            },
            axisTick: {
              length: 10,
              lineStyle: {
                color: 'auto'
              }
            },
            itemStyle: { color: colorArray[colorIndex] },
            detail: {
              offsetCenter: ['0%', `${index * 10}%`],
              fontSize: 16,
              color: colorArray[colorIndex]
            }
          }
          seriesData.push(seriesItem)
          index = index + 2
          colorIndex++
        }
        this.chart.setOption({
          series: seriesData
        })
      }
    },
    initChart () {
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption({
        tooltip: {
          trigger: 'item'
        },
        series: [
          {
            // name: '读数',
            type: 'gauge',
            data: [{ value: 0 }],
            // min: 0,
            // max: maxValue,
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
  }
}
</script>
