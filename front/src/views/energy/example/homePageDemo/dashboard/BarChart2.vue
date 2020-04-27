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
      default: '180px'
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
      const maxValue = 500
      this.chart.setOption({
        tooltip: {
          formatter: "{a} <br/>{c} {b}"
        },
        series: [
          {
            name: '昨日最大负荷',
            type: 'gauge',
            center: ['10%', '40%'],    // 默认全局居中
            radius: '80%',
            min: 0,
            max: maxValue,
            // endAngle: 45,
            splitNumber: 5,
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
            pointer: {
              width: 5
            },
            title: {
              offsetCenter: [0, '125%'],       // x, y，单位px
            },
            detail: {
              // 其余属性默认使用全局文本样式，详见TEXTSTYLE
              fontWeight: 'bolder',
              fontSize: 24,
              offsetCenter: ['0', '80%']
            },
            data: [{ value: 59.3, name: '一期' },]
          }, {
            name: '昨日最大负荷',
            type: 'gauge',
            center: ['30%', '40%'],    // 默认全局居中
            radius: '80%',
            min: 0,
            max: maxValue,
            // endAngle: 45,
            splitNumber: 5,
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
            pointer: {
              width: 5
            },
            title: {
              offsetCenter: [0, '125%'],       // x, y，单位px
            },
            detail: {
              // 其余属性默认使用全局文本样式，详见TEXTSTYLE
              fontWeight: 'bolder',
              fontSize: 24,
              offsetCenter: ['0', '80%']
            },
            data: [{ value: 58.3, name: '二期' },]
          },
          {
            name: '昨日最大负荷',
            type: 'gauge',
            center: ['50%', '40%'],    // 默认全局居中
            radius: '80%',
            min: 0,
            max: maxValue,
            // endAngle: 45,
            splitNumber: 5,
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
            pointer: {
              width: 5
            },
            title: {
              offsetCenter: [0, '125%'],       // x, y，单位px
            },
            detail: {
              // 其余属性默认使用全局文本样式，详见TEXTSTYLE
              fontWeight: 'bolder',
              fontSize: 24,
              offsetCenter: ['0', '80%']
            },
            data: [{ value: 34.1, name: '扶贫' },]
          },
          {
            name: '昨日最大负荷',
            type: 'gauge',
            center: ['70%', '40%'],    // 默认全局居中
            radius: '80%',
            min: 0,
            max: maxValue,
            // endAngle: 45,
            splitNumber: 5,
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
            pointer: {
              width: 5
            },
            title: {
              offsetCenter: [0, '125%'],       // x, y，单位px
            },
            detail: {
              // 其余属性默认使用全局文本样式，详见TEXTSTYLE
              fontWeight: 'bolder',
              fontSize: 24,
              offsetCenter: ['0', '80%']
            },
            data: [{ value: 55.7, name: '三期' },]
          },
          {
            name: '负荷值',
            type: 'gauge',
            center: ['90%', '40%'],    // 默认全局居中
            radius: '80%',
            min: 0,
            max: maxValue,
            // endAngle: 45,
            splitNumber: 5,
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
            pointer: {
              width: 5
            },
            title: {
              offsetCenter: [0, '125%'],       // x, y，单位px
            },
            detail: {
              // 其余属性默认使用全局文本样式，详见TEXTSTYLE
              fontWeight: 'bolder',
              fontSize: 24,
              offsetCenter: ['0', '80%'],
            },
            data: [{ value: 233.29, name: '谷前堡站' },]
          }
        ]
      })
    }
  }
}
</script>
