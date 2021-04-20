<template>
  <div :class="className" :style="{ height: height, width: width }" />
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
      default: 'chart',
    },
    width: {
      type: String,
      default: '100%',
    },
    height: {
      type: String,
      default: '300px',
    },
    chartData: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      chart: null,
      xAxisData: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setChartData(val)
      },
    },
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
    setChartData(val) {
      this.chart.setOption({
        title: {
          text: val.title,
        },
        series: [
          {
            data: val.yData,
          },
        ],
      })
    },
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      console.log(1111)

      this.chart.setOption({
        title: {
          text: '',
          left: 'center',
          align: 'right',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow', // 默认为直线，可选为：'line' | 'shadow'
          },
        },
        grid: {
          top: 29,
          left: '2%',
          right: '2%',
          bottom: '2',
          containLabel: true,
        },
        yAxis: [
          {
            type: 'category',
            name: '月',
            data: this.xAxisData,
            axisTick: {
              alignWithLabel: true,
            },
          },
        ],
        xAxis: [
          {
            type: 'value',
            axisTick: {
              show: false,
            },
          },
        ],
        series: [
          {
            name: '',
            type: 'bar',
            barWidth: '60%',
            data: [],
            animationDuration,
          },
        ],
      })
    },
  },
}
</script>
