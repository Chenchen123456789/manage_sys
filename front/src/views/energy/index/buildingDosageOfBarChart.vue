<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'

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
      default: '300px'
    },
    chartData: {
      type: Array,
      default: null
    }
  },
  data() {
    return {
      chart: null
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
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setChartData(val)
      }
    }
  },
  methods: {
    setChartData(chartData) {
      if (chartData && chartData.length > 0) {
        const xAxisData = chartData.map(item => item.buildingName)
        const seriesData = chartData.map(item => item.sumValue)
        this.chart.setOption({
          xAxis: {
            data: xAxisData
          },
          series: [
            {
              data: seriesData
            }
          ]
        })
      }
    },
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')

      this.chart.setOption({
        title: {
          text: '日用量'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          top: 40,
          left: '2%',
          right: '2%',
          bottom: '2%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            data: [],
            axisTick: { show: false }
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        legend: {
          left: 'right',
          data: []
        },
        series: [
          {
            name: '日用量',
            type: 'bar',
            data: [],
            animationDuration
          }
        ]
      })
    }
  }
}
</script>
