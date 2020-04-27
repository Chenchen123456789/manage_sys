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
      const data = [
        { '三期': 820.2 }, { '扶贫': 1144.9 }, { '二期': 1964.8 }, { '一期': 2042.6 }, , { '谷前堡站': 5972.5 }
      ]
      //   data.sort((a, b) => {
      //     return a[Object.keys(a)[0]] - b[Object.keys(a)[0]]
      //   })

      //   console.log(data)

      this.chart.setOption({
        title: {
          text: '年累计上网电量'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          top: 50,
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          boundaryGap: [0, 0.01]
        },
        yAxis: {
          type: 'category',
          data: ['三期', '扶贫', '二期', '一期', '谷前堡站'],
        },
        // xAxis: [{
        //   type: 'category',
        //   data: ['一期', '二期', '扶贫', '三期', '谷前堡站'],
        //   axisTick: {
        //     alignWithLabel: true
        //   }
        // }],
        // yAxis: [{
        //   type: 'value',
        //   axisTick: {
        //     show: false
        //   }
        // }],
        series: [{
          name: '上网电量',
          type: 'bar',
          stack: 'vistors',
          barWidth: '60%',
          data: [812.5, 1125.7, 1941.7, 2018.6, 5898.5],
          animationDuration
        }]
      })
    }
  }
}
</script>
