<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :lg="8" :sm="12" :xs="12" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-electricity">
          <svg-icon class-name="card-panel-icon" icon-class="electricity" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            年总电量
            <br />（kWh）
          </div>
          <count-to
            :duration="2600"
            :end-val="10"
            :start-val="0"
            class="card-panel-num"
          />
        </div>
      </div>
    </el-col>
    <el-col :lg="8" :sm="12" :xs="12" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-water">
          <svg-icon class-name="card-panel-icon" icon-class="water" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            年总水量
            <br />&nbsp;&nbsp;&nbsp;（t）
          </div>
          <count-to
            :duration="3000"
            :end-val="20"
            :start-val="0"
            class="card-panel-num"
          />
        </div>
      </div>
    </el-col>
    <el-col :lg="8" :sm="12" :xs="12" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-air">
          <svg-icon class-name="card-panel-icon" icon-class="air" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            年总气量
            <br />&nbsp;（m³）
          </div>
          <count-to
            :duration="3200"
            :end-val="30"
            :start-val="0"
            class="card-panel-num"
          />
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'

export default {
  components: {
    CountTo,
  },
  props: {},
  data() {
    return {}
  },
  computed: {},
  watch: {},
  methods: {
    setValues(panelGroupData) {},
  },
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: -2px;

  .card-panel-col {
    margin-bottom: 18px;
    padding-left: 8px !important;
    padding-right: 8px !important;
  }
  .card-panel-col:first-child {
    padding-left: 20px !important;
  }
  .card-panel-col:last-child {
    padding-right: 20px !important;
  }

  .card-panel {
    height: 108px;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, 0.05);
    border-color: rgba(0, 0, 0, 0.05);
    display: flex;
    align-items: center;
    justify-content: space-evenly;

    .icon-electricity {
      color: #40c9c6;
    }

    .card-panel-icon-wrapper {
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      font-weight: bold;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width: 550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
