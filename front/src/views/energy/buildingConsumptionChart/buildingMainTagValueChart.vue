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
      default: '210px'
    },
    itemData: {
      type: Object,
      default: null
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
      const itemData = this.itemData
      this.chart.setOption({
        tooltip: {

        },
        series: [
          {
            name: '测点值',
            type: 'gauge',
            pointer: {
              width: 5
            },
            min: 0,
            max: 500,
            radius: '85%',
            center: ['50%', '46%'],
            axisLine: {            // 坐标轴线
              lineStyle: {       // 属性lineStyle控制线条样式
                color: [[0.2, '#ff4500'], [0.8, '#1e90ff'], [1, '#2EC7C9']],
                width: 5
              }
            },
            axisTick: {            // 坐标轴小标记
              length: 10,        // 属性length控制线长
              lineStyle: {       // 属性lineStyle控制线条样式
                color: 'auto'
              }
            },
            splitLine: {           // 分隔线
              length: 13,         // 属性length控制线长
              lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
                color: 'auto'
              }
            },
            title: {
              offsetCenter: [0, '100%'],
            },
            detail: {
              // 其余属性默认使用全局文本样式，详见TEXTSTYLE
              fontSize: 22,
              offsetCenter: ['0', '70%']
            },
            data: [{ value: itemData.tagValue || 0, name: itemData.tagName || '' }]
          }
        ]
      })
    }
  }
}
</script>
