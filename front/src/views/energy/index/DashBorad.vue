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
      if (dashboardDataList.length > 0) {
        try {
          var resItem = dashboardDataList.find(
            item => item.placeholderId === placeholderId
          ).item
        } catch (e) {
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
        const colorArray = ['green', 'purple', '#FF6347', '#FFD700', 'red']

        resData = resItem.map((item, index) => {
          return { value: item.tagValue, name: item.tagName, itemStyle: { color: colorArray[index] } }
        })
        const seriesItem = {
          type: 'gauge',
          max: maxValue,
          center: ['50%', '46%'],
          data: resData,
          title: {
            show: false
          },
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
          detail: {
            offsetCenter: ['0%', '70%'],
            formatter: function () {
              const data = resData.map((item, index) => {
                return `{${index}|${item.value}}`
              })
              return data.join('\n')
            },
            rich: {
              0: {
                color: 'green',
                fontSize: 16,
              },
              1: {
                color: 'purple',
                fontSize: 16,
              },
              2: {
                color: '#FF6347',
                fontSize: 16,
              },
              3: {
                color: '#FFD700',
                fontSize: 16,
                height: 20
              },
              4: {
                color: 'red',
                fontSize: 16,
              }
            }
          }
        }

        this.chart.setOption({
          series: [seriesItem]
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
            type: 'gauge',
            data: [{ value: 0 }],
          }
        ]
      })
    }
  }
}
</script>
