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
      default: '330px'
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
      let xAxisData = []
      for (let i = 1; i <= 31; i++) {
        const item = i + '日'
        xAxisData.push(item)
      }

      this.chart.setOption({
        title: {
          text: '2020年3月上网电量柱状图',
          subtext: '单位：万kwh',
          subtextStyle: {
            color: 'black'
          }
        },
        legend: {
          data: ['一值上网电量', '二值上网电量']
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
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
          data: xAxisData,
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
          name: '一值上网电量',
          type: 'bar',
          label: {
            show: true,
            position: 'top',
            rotate: -90,
            offset: [-8, -20]
          },
          data: [214.5, 206.6, 120.0, 160.6, 191.7, 100, 120, 130, 150, 89, 250, 300, 170.6, 200.7, 190, 140, 120, 130, 150, 89, 250, 300, 170.6, 200.7, 190, 140, 189, 350, 190, 140, 300],
          animationDuration
        },
        {
          name: '二值上网电量',
          type: 'bar',
          label: {
            show: true,
            position: 'top',
            rotate: -90,
            offset: [-8, -20]
          },
          data: [220.5, 106.6, 230.0, 170.6, 200.7, 190, 140, 100, 90, 189, 350, 200, 120.0, 160.6, 191.7, 100, 170.6, 200.7, 190, 140, 100, 90, 189, 350, 190, 140, 89, 250, 300, 170.6, 100],
          animationDuration
        }]
      })
    }
  }
}
</script>
