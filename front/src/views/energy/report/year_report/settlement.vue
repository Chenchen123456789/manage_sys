<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" label-width="68px" ref="queryForm">
      <el-form-item label="单位名称" prop="plantId">
        <el-select clearable placeholder="请选择" size="small" v-model="queryParams.plantId">
          <el-option
            :key="item.id"
            :label="item.plantName"
            :value="item.id"
            v-for="item in queryPlantOptions"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="查询时间" prop="queryTime">
        <el-date-picker
          @change="changeQueryTime"
          clearable
          placeholder="选择年"
          size="small"
          type="year"
          v-model="queryParams.queryTime"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="大功率" prop="deviceType">
        <el-select clearable size="small" v-model="queryParams.deviceType">
          <el-option
            :key="item.id"
            :label="item.label"
            :value="item.id"
            v-for="item in [{id: 1, label:'大功率电器' },{ id:0, label: '未分类' }]"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="计量等级" prop="measureLevel">
        <el-select clearable size="small" v-model="queryParams.measureLevel">
          <el-option
            :key="item.id"
            :label="item.label"
            :value="item.id"
            v-for="item in [{id: 1, label:'一级' },{id: 2, label:'二级' },{id: 3, label:'三级' }]"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery" icon="el-icon-search" size="mini" type="primary">搜索</el-button>
        <el-button @click="resetQuery" icon="el-icon-refresh" size="mini" type="info">重置</el-button>
        <el-popover placement="bottom" style="margin-left:10px" trigger="click">
          <div style="text-align: right; margin: 0">
            <el-button
              @click="handleExport(0)"
              icon="el-icon-download"
              size="mini"
              type="warning"
            >导出全部数据</el-button>
            <el-button
              @click="handleExport(1)"
              icon="el-icon-download"
              size="mini"
              type="warning"
            >导出当前页数据</el-button>
          </div>
          <el-button
            icon="el-icon-download"
            size="mini"
            slot="reference"
            type="warning"
            v-hasPermi="['energy:report_daySettlement:export']"
          >导出</el-button>
        </el-popover>
      </el-form-item>
    </el-form>
    <el-divider class="first-divider"></el-divider>
    <el-row
      :gutter="10"
      style="display:flex; align-items:center; justify-content:stretch;margin-left: 0px"
    >
      <el-col :span="4">
        <span>水单价：{{waterPrice }} 元/m³</span>
      </el-col>
      <el-col :span="4">
        <span>电单价：{{electricityPrice }} 元/kWh</span>
      </el-col>
      <el-col :span="4">
        <span>空气单价：{{airPrice }} 元/m³</span>
      </el-col>
      <el-col :span="4">
        <span>蒸汽单价：{{steamPrice }} 元/m³</span>
      </el-col>
      <el-col :span="1.5">
        <el-button @click="handleUpdate" icon="el-icon-edit" size="mini" type="success">修改</el-button>
      </el-col>
    </el-row>

    <el-dialog :visible.sync="open" title="修改" width="500px">
      <el-form :model="form" label-width="80px" ref="form">
        <el-form-item label="水单价" prop="waterPrice">
          <el-input-number placeholder="请输入水单价" v-model="form.waterPrice" />
        </el-form-item>
        <el-form-item label="电单价" prop="electricityPrice">
          <el-input-number placeholder="请输入电单价" v-model="form.electricityPrice" />
        </el-form-item>
        <el-form-item label="空气单价" prop="airPrice">
          <el-input-number placeholder="请输入空气单价" v-model="form.airPrice" />
        </el-form-item>
        <el-form-item label="蒸汽单价" prop="steamPrice">
          <el-input-number placeholder="请输入蒸汽单价" v-model="form.steamPrice" />
        </el-form-item>
      </el-form>
      <div class="dialog-footer" slot="footer">
        <el-button @click="submitForm" type="primary">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-divider></el-divider>

    <el-table :data="settlementList" size="mini" v-loading="loading">
      <el-table-column :index="indexMethod" label="序号" type="index" width="50" />
      <el-table-column align="center" label="单位" prop="plantName" />
      <el-table-column align="center" label="建筑" prop="buildingName" width="120" />
      <el-table-column align="center" label="水">
        <el-table-column align="center" label="水数量" prop="currentWaterDosage" width="120" />
        <el-table-column align="center" label="单价">
          <span>{{waterPrice}}</span>
        </el-table-column>
        <el-table-column align="center" label="金额" prop="waterAmount" width="120">
          <template slot-scope="scope">{{(waterPrice * scope.row.currentWaterDosage).toFixed(2)}}</template>
        </el-table-column>
        <el-table-column align="center" label="去年累计" prop="preWaterSumValue" width="120" />
        <el-table-column align="center" label="耗水累计" prop="currentWaterSumValue" width="120" />
      </el-table-column>
      <el-table-column align="center" label="空气">
        <el-table-column align="center" label="空气数量" prop="currentAirDosage" width="120" />
        <el-table-column align="center" label="单价" prop="airPrice">
          <span>{{airPrice}}</span>
        </el-table-column>
        <el-table-column align="center" label="金额" prop="airAmount" width="120">
          <template slot-scope="scope">{{(airPrice * scope.row.currentAirDosage).toFixed(2)}}</template>
        </el-table-column>
        <el-table-column align="center" label="去年累计" prop="preAirSumValue" width="120" />
        <el-table-column align="center" label="空气累计" prop="currentAirSumValue" width="120" />
      </el-table-column>
      <el-table-column align="center" label="电">
        <el-table-column align="center" label="电数量" prop="currentElectricityDosage" width="120" />
        <el-table-column align="center" label="单价" prop="electricityPrice">
          <span>{{electricityPrice}}</span>
        </el-table-column>
        <el-table-column align="center" label="金额" prop="electricityAmount" width="120">
          <template
            slot-scope="scope"
          >{{(electricityPrice * scope.row.currentElectricityDosage).toFixed(2)}}</template>
        </el-table-column>
        <el-table-column align="center" label="去年累计" prop="preElectricitySumValue" width="120" />
        <el-table-column align="center" label="耗电累计" prop="currentElectricitySumValue" width="120" />
      </el-table-column>
      <el-table-column align="center" label="蒸汽">
        <el-table-column align="center" label="蒸汽数量" prop="currentSteamDosage" width="120" />
        <el-table-column align="center" label="单价" prop="steamPrice">
          <span>{{steamPrice}}</span>
        </el-table-column>
        <el-table-column align="center" label="金额" prop="steamAmount" width="120">
          <template slot-scope="scope">{{(steamPrice * scope.row.currentSteamDosage).toFixed(2)}}</template>
        </el-table-column>
        <el-table-column align="center" label="去年累计" prop="preSteamSumValue" width="120" />
        <el-table-column align="center" label="蒸汽累计" prop="currentSteamSumValue" width="120" />
      </el-table-column>
      <el-table-column align="center" label="金额合计" prop="totalAmount" width="120">
        <template slot-scope="scope">
          {{
          (waterPrice * scope.row.currentWaterDosage +
          airPrice * scope.row.currentAirDosage +
          electricityPrice * scope.row.currentElectricityDosage +
          steamPrice * scope.row.currentSteamDosage).toFixed(2)
          }}
        </template>
      </el-table-column>
    </el-table>

    <pagination
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
      v-show="total>0"
    />
  </div>
</template>

<script>
import { listYearSettlement, exportYearSettlement } from '@/api/energy/report'
import { listPlant } from '@/api/energy/plant'
import { listUnitPrice, updateUnitPrice } from '@/api/energy/unitPrice'

export default {
  name: 'Settlement',
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 表格数据
      settlementList: [],
      queryPlantOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        queryTime: new Date(),
        plantId: undefined
      },
      unitPriceList: [],
      form: {},
      open: false,
      waterPrice: 0,
      airPrice: 0,
      electricityPrice: 0,
      steamPrice: 0
    }
  },
  created () {
    this.getPlantOptions()
    this.getUnitPriceList()
  },
  mounted () {
    this.getList()
  },
  methods: {
    // 取消按钮
    cancel () {
      this.open = false
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs['form'].validate(valid => {
        if (valid) {
          updateUnitPrice(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess('修改成功')
              this.open = false
              this.getUnitPriceList()
            } else {
              this.msgError(response.msg)
            }
          })
        }
      })
    },
    handleUpdate () {
      this.open = true
      this.form.waterPrice = this.waterPrice
      this.form.electricityPrice = this.electricityPrice
      this.form.airPrice = this.airPrice
      this.form.steamPrice = this.steamPrice
    },
    getUnitPriceList () {
      listUnitPrice().then(res => {
        this.unitPriceList = res.data
        this.waterPrice =
          res.data.find(item => item.energyType == '水').price || 0
        this.electricityPrice =
          res.data.find(item => item.energyType == '电').price || 0
        this.airPrice =
          res.data.find(item => item.energyType == '空气').price || 0
        this.steamPrice =
          res.data.find(item => item.energyType == '蒸汽').price || 0
      })
    },
    getPlantOptions () {
      listPlant().then(res => {
        this.queryPlantOptions = res.rows
      })
    },
    changeQueryTime (value) {
      const year = value.getFullYear()
      const month = value.getMonth() + 1
      this.dataTime = '数据时间：' + year + '年' + month + '月'
    },
    indexMethod (index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      )
    },
    /** 查询列表 */
    getList () {
      this.loading = true
      listYearSettlement(this.queryParams).then(response => {
        let list = response.rows
        for (const index in list) {
          const currentAirSumValue = list[index].currentAirSumValue || 0
          const preAirSumValue = list[index].preAirSumValue || 0
          const currentAirDosage = currentAirSumValue - preAirSumValue
          const currentElectricitySumValue = list[index].currentElectricitySumValue || 0
          const preElectricitySumValue = list[index].preElectricitySumValue || 0
          const currentElectricityDosage = currentElectricitySumValue - preElectricitySumValue
          const currentWaterSumValue = list[index].currentWaterSumValue || 0
          const preWaterSumValue = list[index].preWaterSumValue || 0
          const currentWaterDosage = currentWaterSumValue - preWaterSumValue
          const currentSteamSumValue = list[index].currentSteamSumValue || 0
          const preSteamSumValue = list[index].preSteamSumValue || 0
          const currentSteamDosage = currentSteamSumValue - preSteamSumValue

          list[index].currentAirDosage = currentAirDosage.toFixed(2)
          list[index].currentElectricityDosage = currentElectricityDosage.toFixed(2)
          list[index].currentWaterDosage = currentWaterDosage.toFixed(2)
          list[index].currentSteamDosage = currentSteamDosage.toFixed(2)

          list[index].currentAirSumValue = currentAirSumValue.toFixed(2)
          list[index].currentElectricitySumValue = currentElectricitySumValue.toFixed(2)
          list[index].currentWaterSumValue = currentWaterSumValue.toFixed(2)
          list[index].currentSteamSumValue = currentSteamSumValue.toFixed(2)

          list[index].preAirSumValue = preAirSumValue.toFixed(2)
          list[index].preElectricitySumValue = preElectricitySumValue.toFixed(2)
          list[index].preWaterSumValue = preWaterSumValue.toFixed(2)
          list[index].preSteamSumValue = preSteamSumValue.toFixed(2)
        }
        this.settlementList = list
        this.total = response.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 导出按钮操作 */
    handleExport (type) {
      const queryParams = { ...this.queryParams }
      if (type === 0) {
        queryParams.pageNum = null
      }
      const { waterPrice, airPrice, electricityPrice, steamPrice } = this
      queryParams.waterPrice = waterPrice
      queryParams.airPrice = airPrice
      queryParams.electricityPrice = electricityPrice
      queryParams.steamPrice = steamPrice
      this.$confirm('是否确认导出年报结算数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function () {
          return exportYearSettlement(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
        .catch(function () { })
    }
  }
}
</script>

<style lang="scss" scoped>
.el-divider--horizontal {
  margin: 12px 0;
}

.first-divider {
  margin: 0px 0 12px 0;
}
</style>