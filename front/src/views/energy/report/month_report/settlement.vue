<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="单位名称" prop="plantId">
        <el-select v-model="queryParams.plantId" placeholder="请选择" clearable size="small">
          <el-option
            v-for="item in queryPlantOptions"
            :key="item.id"
            :label="item.plantName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="查询时间" prop="queryTime">
        <el-date-picker
          v-model="queryParams.queryTime"
          type="month"
          placeholder="选择月"
          clearable
          size="small"
          @change="changeQueryTime"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="info" icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-divider></el-divider>
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
        <el-button type="success" icon="el-icon-edit" size="mini" @click="handleUpdate">修改</el-button>
      </el-col>
    </el-row>

    <el-dialog title="修改" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="水单价" prop="waterPrice">
          <el-input-number v-model="form.waterPrice" placeholder="请输入水单价" />
        </el-form-item>
        <el-form-item label="电单价" prop="electricityPrice">
          <el-input-number v-model="form.electricityPrice" placeholder="请输入电单价" />
        </el-form-item>
        <el-form-item label="空气单价" prop="airPrice">
          <el-input-number v-model="form.airPrice" placeholder="请输入空气单价" />
        </el-form-item>
        <el-form-item label="蒸汽单价" prop="steamPrice">
          <el-input-number v-model="form.steamPrice" placeholder="请输入蒸汽单价" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-divider></el-divider>

    <el-row :gutter="10" class="mb8">
      <el-popover placement="bottom" trigger="click">
        <div style="text-align: right; margin: 0">
          <el-button
            icon="el-icon-download"
            type="warning"
            size="mini"
            @click="handleExport(0)"
          >导出全部数据</el-button>
          <el-button
            icon="el-icon-download"
            type="warning"
            size="mini"
            @click="handleExport(1)"
          >导出当前页数据</el-button>
        </div>
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          v-hasPermi="['energy:report_monthSettlement:export']"
          slot="reference"
        >导出</el-button>
      </el-popover>
    </el-row>

    <el-table v-loading="loading" :data="settlementList">
      <el-table-column label="序号" type="index" :index="indexMethod" width="50" />
      <el-table-column label="单位" align="center" prop="plantName" />
      <el-table-column label="水" align="center">
        <el-table-column label="水数量" align="center" prop="currentWaterDosage" />
        <el-table-column label="单价" align="center">
          <span>{{waterPrice}}</span>
        </el-table-column>
        <el-table-column label="金额" align="center" prop="waterAmount">
          <template slot-scope="scope">{{(waterPrice * scope.row.currentWaterDosage).toFixed(2)}}</template>
        </el-table-column>
        <el-table-column label="上月累计" align="center" prop="preWaterSumValue" />
        <el-table-column label="耗水累计" align="center" prop="currentWaterSumValue" />
      </el-table-column>
      <el-table-column label="空气" align="center">
        <el-table-column label="空气数量" align="center" prop="currentAirDosage" />
        <el-table-column label="单价" align="center" prop="airPrice">
          <span>{{airPrice}}</span>
        </el-table-column>
        <el-table-column label="金额" align="center" prop="airAmount">
          <template slot-scope="scope">{{(airPrice * scope.row.currentAirDosage).toFixed(2)}}</template>
        </el-table-column>
        <el-table-column label="上月累计" align="center" prop="preAirSumValue" />
        <el-table-column label="空气累计" align="center" prop="currentAirSumValue" />
      </el-table-column>
      <el-table-column label="电" align="center">
        <el-table-column label="电数量" align="center" prop="currentElectricityDosage" />
        <el-table-column label="单价" align="center" prop="electricityPrice">
          <span>{{electricityPrice}}</span>
        </el-table-column>
        <el-table-column label="金额" align="center" prop="electricityAmount">
          <template slot-scope="scope">{{(electricityPrice * scope.row.currentElectricityDosage).toFixed(2)}}</template>
        </el-table-column>
        <el-table-column label="上月累计" align="center" prop="preElectricitySumValue" />
        <el-table-column label="耗电累计" align="center" prop="currentElectricitySumValue" />
      </el-table-column>
      <el-table-column label="蒸汽" align="center">
        <el-table-column label="蒸汽数量" align="center" prop="currentSteamDosage" />
        <el-table-column label="单价" align="center" prop="steamPrice">
          <span>{{steamPrice}}</span>
        </el-table-column>
        <el-table-column label="金额" align="center" prop="steamAmount">
          <template slot-scope="scope">{{(steamPrice * scope.row.currentSteamDosage).toFixed(2)}}</template>
        </el-table-column>
        <el-table-column label="上月累计" align="center" prop="preSteamSumValue" />
        <el-table-column label="蒸汽累计" align="center" prop="currentSteamSumValue" />
      </el-table-column>
      <el-table-column label="金额合计" align="center" prop="totalAmount">
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
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { listMonthSettlement, exportMonthSettlement } from '@/api/energy/report'
import { listPlant } from '@/api/energy/plant'
import { listUnitPrice, updateUnitPrice } from '@/api/energy/unitPrice'

export default {
  name: 'Settlement',
  data() {
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
        queryTime: new Date(
          new Date().getFullYear() +
            '-' +
            (new Date().getMonth() + 1) +
            '-01 00:00:00'
        ),
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
  created() {
    this.getPlantOptions()
    this.getUnitPriceList()
  },
  mounted() {
    this.getList()
  },
  methods: {
    // 取消按钮
    cancel() {
      this.open = false
    },
    /** 提交按钮 */
    submitForm: function() {
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
    handleUpdate() {
      this.open = true
      this.form.waterPrice = this.waterPrice
      this.form.electricityPrice = this.electricityPrice
      this.form.airPrice = this.airPrice
      this.form.steamPrice = this.steamPrice
    },
    getUnitPriceList() {
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
    getPlantOptions() {
      listPlant().then(res => {
        this.queryPlantOptions = res.rows
      })
    },
    changeQueryTime(value) {
      const year = value.getFullYear()
      const month = value.getMonth() + 1
      this.dataTime = '数据时间：' + year + '年' + month + '月'
    },
    indexMethod(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      )
    },
    /** 查询列表 */
    getList() {
      this.loading = true
      listMonthSettlement(this.queryParams).then(response => {
        let list = response.rows
        for (const index in list) {
          const currentAirSumValue = list[index].currentAirSumValue || 0
          const preAirSumValue = list[index].preAirSumValue || 0
          const currentAirDosage = currentAirSumValue - preAirSumValue
          const currentElectricitySumValue =
            list[index].currentElectricitySumValue || 0
          const preElectricitySumValue = list[index].preElectricitySumValue || 0
          const currentElectricityDosage = Number.parseFloat(
            (currentElectricitySumValue - preElectricitySumValue).toFixed(2)
          )
          const currentWaterSumValue = list[index].currentWaterSumValue || 0
          const preWaterSumValue = list[index].preWaterSumValue || 0
          const currentWaterDosage = currentWaterSumValue - preWaterSumValue
          const currentSteamSumValue = list[index].currentSteamSumValue || 0
          const preSteamSumValue = list[index].preSteamSumValue || 0
          const currentSteamDosage = currentSteamSumValue - preSteamSumValue
          list[index].currentAirDosage = currentAirDosage
          list[index].currentElectricityDosage = currentElectricityDosage
          list[index].currentWaterDosage = currentWaterDosage
          list[index].currentSteamDosage = currentSteamDosage
        }
        this.settlementList = list
        this.total = response.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 导出按钮操作 */
    handleExport(type) {
      const queryParams = { ...this.queryParams }
      if (type === 0) {
        queryParams.pageNum = null
      }
      const { waterPrice, airPrice, electricityPrice, steamPrice } = this
      queryParams.waterPrice = waterPrice
      queryParams.airPrice = airPrice
      queryParams.electricityPrice = electricityPrice
      queryParams.steamPrice = steamPrice
      this.$confirm('是否确认导出月报结算数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function() {
          return exportMonthSettlement(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
        .catch(function() {})
    }
  }
}
</script>