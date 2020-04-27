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
      default: '280px'
    }
  },
  data () {
    return {
      chart: null
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
    initChart () {
      this.chart = echarts.init(this.$el, 'macarons')

      this.chart.setOption({
        title: {
          text: '2020年3月累计发电量/上网电量',
          subtext: '单位：万kwh',
          subtextStyle: {
            color: 'black'
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        legend: {
          data: ['发电量', '上网电量']
        },
        grid: {
          top: 60,
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          data: ['一期', '二期', '扶贫', '三期', '谷前堡站'],
          axisTick: {
            alignWithLabel: true
          }
        }],
        yAxis: [{
          type: 'value',
          axisTick: {
            show: false
          }
        }],
        series: [{
          name: '发电量',
          type: 'bar',
           label: {
            show: true,
            position: 'top'
          },
          // barWidth: '60%',
          data: [214.5, 206.6, 120.0, 160.6, 701.7],
          animationDuration
        },
        {
          name: '上网电量',
          type: 'bar',
          // barWidth: '60%',
          label: {
            show: true,
            position: 'top'
          },
          data: [211.8, 203.8, 117.8, 159.0, 692.4],
          animationDuration
        }]
      })
    }
  }
}
</script>
