<template>
  <div class="dashboard-editor-container">
    <!-- <panel-group1 /> -->
    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="12">
        <div style="border: solid 1px rgba(0,0,0,0.1);padding: 15px 10px 0px 10px;">
          <span class="title">昨日最大负荷（万kWh）</span>
          <el-divider></el-divider>
          <BarChart2></BarChart2>
        </div>

        <!-- <div style="border: solid 1px rgba(0,0,0,0.1);padding: 15px;">
          <span class="title">昨日最大负荷（万kWh）</span>
          <el-divider></el-divider>
          <div class="cls1">
            <div class="cls2">
              <div class=" icon-example_fuhe">
                <svg-icon icon-class="example_fuhe" class-name="card-panel-icon" />
              </div>
              <div class="number">
                <span class="number_name">一期</span>
                <span class="number_no">1100</span>
              </div>
            </div>
            <div class="cls2">
              <div class=" icon-example_fuhe">
                <svg-icon icon-class="example_fuhe" class-name="card-panel-icon" />
              </div>
              <div class="number">
                <span class="number_name">二期</span>
                <span class="number_no">1100</span>
              </div>
            </div>
            <div class="cls2">
              <div class=" icon-example_fuhe">
                <svg-icon icon-class="example_fuhe" class-name="card-panel-icon" />
              </div>
              <div class="number">
                <span class="number_name">扶贫</span>
                <span class="number_no">1100</span>
              </div>
            </div>
            <div class="cls2">
              <div class=" icon-example_fuhe">
                <svg-icon icon-class="example_fuhe" class-name="card-panel-icon" />
              </div>
              <div class="number">
                <span class="number_name">三期</span>
                <span class="number_no">1100</span>
              </div>
            </div>
            <div class="cls2">
              <div class=" icon-example_fuhe">
                <svg-icon icon-class="example_fuhe" class-name="card-panel-icon" />
              </div>
              <div class="number">
                <span class="number_name">谷前堡站</span>
                <span class="number_no">1100</span>
              </div>
            </div>
          </div>
        </div>-->
        <div style="border: solid 1px rgba(0,0,0,0.1);padding: 15px; margin-top: 20px">
          <span class="title">昨日发电量 / 昨日上网电量（万kWh）</span>
          <el-divider></el-divider>
          <div class="cls1">
            <!-- <div class="dian_icon">
              <svg-icon icon-class="example_dian" class-name="card-panel-icon" />
            </div>-->
            <div class="cls3">
              <!-- <div class=" ">
                <svg-icon icon-class="example_dian" class-name="card-panel-icon" />
              </div>-->
              <div class="number">
                <span class="number_name">一期</span>
                <span class="number_no">1100 / 500</span>
              </div>
            </div>
            <div class="cls3">
              <!-- <div class=" ">
                <svg-icon icon-class="example_dian" class-name="card-panel-icon" />
              </div>-->
              <div class="number">
                <span class="number_name">二期</span>
                <span class="number_no">800 / 902.5</span>
              </div>
            </div>
            <div class="cls3">
              <!-- <div class=" ">
                <svg-icon icon-class="example_dian" class-name="card-panel-icon" />
              </div>-->
              <div class="number">
                <span class="number_name">扶贫</span>
                <span class="number_no">800 / 902.5</span>
              </div>
            </div>
            <div class="cls3">
              <!-- <div class=" ">
                <svg-icon icon-class="example_dian" class-name="card-panel-icon" />
              </div>-->
              <div class="number">
                <span class="number_name">三期</span>
                <span class="number_no">800 / 902.5</span>
              </div>
            </div>
            <div class="cls3">
              <!-- <div class=" ">
                <svg-icon icon-class="example_dian" class-name="card-panel-icon" />
              </div>-->
              <div class="number">
                <span class="number_name">谷前堡站</span>
                <span class="number_no">800 / 902.5</span>
              </div>
            </div>
          </div>
        </div>
        <div style="margin-top:20px;border:solid 1px rgba(0,0,0,0.1);padding: 15px 15px 5px 15px;">
          <span class="title">竞赛指标统计</span>
          <el-divider></el-divider>
          <!-- <div class="jinsai_con"> 
             <div style="background: white;padding:5px;">
              <div
                style="display: flex;justify-content: space-between;font-size:20px;padding: 0 12px 0 12px;"
              >
                <div style="font-weight:400">一值</div>
                <div style="color:#A57278">上网电量(万kWh)：300</div>
              </div>
              <div style="display: flex;justify-content: space-between;">
                <div class="second_part">
                  <span class="second_part_number">100</span>
                  <span class="second_part_word">
                    双细则考
                    <br />核（万元）
                  </span>
                </div>
                <div class="second_part">
                  <span class="second_part_number">100</span>
                  <span class="second_part_word">
                    组串平均
                    <br />故障次数（次）
                  </span>
                </div>
                <div class="second_part">
                  <span class="second_part_number">100</span>
                  <span class="second_part_word">
                    故障平均
                    <br />处理时间（h）
                  </span>
                </div>
              </div>
              <div style="display:flex; justify-content:space-between;align-items: center">
                <span class="third_part_word">SVG/FC投入率（%）</span>
                <div style="width: 40%; margin-right:15px">
                  <el-progress
                    :text-inside="true"
                    :stroke-width="24"
                    :percentage="70"
                    status="success"
                  ></el-progress>
                </div>
              </div>
            </div>
            <div style="background: white; padding:5px;">
              <div
                style="display: flex;justify-content: space-between;font-size:20px;padding: 0 12px 0 12px;"
              >
                <div>一值</div>
                <div style="color:#A57278">上网电量(万kWh)：300</div>
              </div>
              <div style="display: flex;justify-content: space-between;">
                <div class="second_part">
                  <span class="second_part_number">100</span>
                  <span class="second_part_word">
                    双细则考
                    <br />核（万元）
                  </span>
                </div>
                <div class="second_part">
                  <span class="second_part_number">100</span>
                  <span class="second_part_word">
                    组串平均
                    <br />故障次数（次）
                  </span>
                </div>
                <div class="second_part">
                  <span class="second_part_number">100</span>
                  <span class="second_part_word">
                    故障平均
                    <br />处理时间（h）
                  </span>
                </div>
              </div>
              <div style="display:flex; justify-content:space-between;align-items: center">
                <span class="third_part_word">SVG/FC投入率（%）</span>
                <div style="width: 40%; margin-right:15px">
                  <el-progress
                    :text-inside="true"
                    :stroke-width="24"
                    :percentage="100"
                    status="success"
                  ></el-progress>
                </div>
              </div>
            </div> 
          </div>-->
          <div style="text-align: center;">
            <div style="background: rgba(144,147,153,0.3);padding: 10px 5px 10px 0;font-size:18px" class="jinsai">
              <div style="width: 30px"></div>
              <div>
                上网电量
                <br />(万kWh)
              </div>
              <div>
                双细则考核
                <br />（万元）
              </div>
              <div>
                组串平均故障次数
                <br />（次）
              </div>
              <div>
                故障平均处理时间
                <br />（h）
              </div>
              <div>
                SVG/FC投入率
                <br />（%）
              </div>
            </div>
            <div
              style="background: #FDF5E6;padding: 10px 0px; margin-top: 6px;color: #008acd; padding: 10px 5px;"
              class="jinsai"
            >
              <div>一值</div>
              <div style="width: 64px">333</div>
              <div style="width: 80px">333</div>
              <div style="width: 128px">222</div>
              <div style="width: 128px">55</div>
              <div style="width: 108px">123</div>
            </div>
            <div
              style="margin-top: 5px;background: #F0F9EB;padding: 10px 0px;color: #008acd;padding: 10px 5px;"
              class="jinsai"
            >
              <div>二值</div>
              <div style="width: 64px;">333</div>
              <div style="width: 80px">333</div>
              <div style="width: 128px">222</div>
              <div style="width: 128px">55</div>
              <div style="width: 108px">123</div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-wrapper" style="margin-bottom: 10px">
          <bar-chart1 />
        </div>
        <div class="chart-wrapper">
          <bar-chart3 />
        </div>
      </el-col>
    </el-row>
    <el-row style="margin-top: 0px" :gutter="32">
      <el-col :xs="24" :sm="24" :lg="24">
        <div class="chart-wrapper">
          <bar-chart5 />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import PanelGroup1 from './dashboard/PanelGroup1'
import PanelGroup2 from './dashboard/PanelGroup2'
import BarChart1 from './dashboard/BarChart1'
import BarChart2 from './dashboard/BarChart2'
import BarChart3 from './dashboard/BarChart3'
import BarChart4 from './dashboard/BarChart4'
import BarChart5 from './dashboard/BarChart5'

export default {
  name: 'Index',
  components: {
    PanelGroup1,
    PanelGroup2,
    BarChart1,
    BarChart2,
    BarChart3,
    BarChart4,
    BarChart5
  },
  data () {
    return {
      tableData: [{ prop1: '', prop2: '一值', prop3: '二值' },
      { prop1: '上网电量(万kWh)', prop2: 201.17, prop3: 120.44 },
      { prop1: '双细则考核（万元）', prop2: 201.17, prop3: 120.44 },
      { prop1: '组串平均故障次数（次）', prop2: 201.17, prop3: 120.44 },
      { prop1: '故障平均处理时间（h）', prop2: 201.17, prop3: 120.44 },
      { prop1: 'SVG/FC投入率（%）', prop2: 201.17, prop3: 120.44 }
      ]
    }
  },
  methods: {
  }
}
</script>

<style lang="scss" scoped>
.jinsai_con {
  display: flex;
  line-height: 1.5;
  justify-content: space-between;

  .second_part {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding: 3px 8px 5px 8px;
    text-align: center;

    .second_part_number {
      font-size: 22px;
      color: #289feb;
    }
    .second_part_word {
      display: inline-block;
      font-size: 16px;
    }
  }

  .third_part_word {
    margin-left: 5px;
    text-align: left;
    font-size: 18px;
  }
  .third_part_number {
    margin-left: 15px;
    font-size: 18px;
    color: #a57278;
  }
}

.title {
  color: #008acd;
  font-size: 17px;
}
.jinsai {
  display: flex;
  justify-content: space-between;
  text-align: center;
  font-size: 20px;
}
.el-divider {
  margin: 12px 0;
}
.cls1 {
  display: flex;
  justify-content: space-between;
  padding-top: 5px;

  .dian_icon {
    display: flex;
    align-items: center;
    // margin-right: 10px;

    .card-panel-icon {
      font-size: 70px;
    }
  }
  .cls3 {
    display: flex;
    flex-direction: column;
    align-items: center;
    background-color: #fff;
    // height: 114px;
    padding: 10px 15px 15px 15px;
    width: 150px;
    justify-content: space-between;

    .card-panel-icon {
      font-size: 40px;
    }

    .number {
      float: right;
      margin: 10px 0 0 0;
      display: flex;
      flex-direction: column;
      text-align: center;

      .number_name {
        font-size: 24px;
        margin-bottom: 8px;
      }
      .number_no {
        display: inline-block;
        margin-top: 5px;
        font-size: 22px;
        color: #008acd;
      }
    }
  }

  .cls2 {
    display: flex;
    flex-direction: row;
    align-items: center;
    padding-left: 15px;
    background-color: #fff;
    height: 90px;
    width: 150px;
    justify-content: space-between;

    .card-panel-icon {
      font-size: 40px;
    }

    .number {
      margin-right: 15px;
      float: right;
      display: flex;
      flex-direction: column;
      text-align: center;

      .number_name {
        font-size: 14px;
        opacity: 0.8;
      }
      .number_no {
        display: inline-block;
        margin-top: 5px;
        font-size: 26px;
        color: #008acd;
      }
    }
  }
}
.dashboard-editor-container {
  min-height: 100vh;
  max-height: 100%;
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 15px;
  }
}

@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
