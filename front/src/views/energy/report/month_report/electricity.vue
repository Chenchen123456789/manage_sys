<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="工厂名称" prop="plantId">
        <el-select
          clearable
          @change="changeQueryPlantOptions"
          v-model="queryParams.plantId"
          placeholder="请选择工厂"
          size="small"
        >
          <el-option
            v-for="item in queryPlantOptions"
            :key="item.id"
            :label="item.plantName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="建筑名称" prop="buildingId">
        <el-select
          clearable
          @change="changeQueryBuildingOptions"
          v-model="queryParams.buildingId"
          placeholder="请选择建筑"
          :disabled="buildingDisabled"
          v-on:click.native="clickBuilding"
          size="small"
          multiple
          collapse-tags
        >
          <el-option
            v-for="item in queryBuildingOptions"
            :key="item.id"
            :label="item.buildingName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="设备名称" prop="deviceId">
        <el-select
          clearable
          @change="changeQueryDeviceOptions"
          v-model="queryParams.deviceId"
          placeholder="请选择设备"
          size="small"
          :disabled="deviceDisabled"
          v-on:click.native="clickDevice"
          multiple
          collapse-tags
        >
          <el-option
            v-for="item in queryDeviceOptions"
            :key="item.id"
            :label="item.deviceName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="仪表编号" prop="meterId">
        <el-select
          clearable
          @change="changeQueryMeterCodeOptions"
          v-model="queryParams.meterId"
          placeholder="请选择仪表编号"
          size="small"
          :disabled="meterDisabled"
          v-on:click.native="clickMeter"
          multiple
          collapse-tags
        >
          <el-option
            v-for="item in queryMeterOptions"
            :key="item.id"
            :label="item.meterCode"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="查询时间" prop="queryTime">
        <el-date-picker
          v-model="queryParams.queryTime"
          type="month"
          placeholder="选择月"
          :clearable="false"
          size="small"
          @change="changeQueryTime"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="info" icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

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
          v-hasPermi="['energy:report_monthDosageOfElectricity:export']"
          slot="reference"
        >导出</el-button>
      </el-popover>
    </el-row>

    <el-table size="mini" show-summary v-loading="loading" :data="monthDosageOfElectricityList">
      <el-table-column label="序号" type="index" :index="indexMethod" width="50" />
      <el-table-column label="单位名称" align="center" prop="plantName" />
      <el-table-column label="建筑名称" align="center" prop="buildingName" />
      <el-table-column label="设备名称" align="center" prop="deviceName" />
      <el-table-column label="表号" align="center" prop="meterCode" />
      <el-table-column label="装表地点" align="center" prop="meterLocation" />
      <el-table-column label="倍率" align="center" prop="meterSpec" />
      <el-table-column label="上月抄见数" align="center" prop="preMonthValue" />
      <el-table-column label="本月抄见数" align="center" prop="currentMonthValue" />
      <el-table-column label="本月用电数" align="center">
        <el-table-column label="峰" align="center" prop="fValue" />
        <el-table-column label="平" align="center" prop="pValue" />
        <el-table-column label="谷" align="center" prop="gValue" />
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
import {
  listMonthDosageOfElectricity,
  exportMonthDosageOfElectricity
} from '@/api/energy/report'
import { listPlant } from '@/api/energy/plant'
import { listBuilding } from '@/api/energy/building'
import { listDevice } from '@/api/energy/device'
import { listMeter } from '@/api/energy/meter'

export default {
  name: 'Electricity',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 表格数据
      monthDosageOfElectricityList: [],
      plantOptions: [],
      buildingOptions: [],
      deviceOptions: [],
      meterOptions: [],
      queryPlantOptions: [],
      queryBuildingOptions: [],
      queryDeviceOptions: [],
      queryMeterOptions: [],
      dataTime: '数据时间',
      buildingDisabled: true,
      deviceDisabled: true,
      meterDisabled: true,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        plantId: undefined,
        buildingId: [],
        deviceId: [],
        meterId: [],
        queryTime: new Date(
          new Date().getFullYear() +
            '-' +
            (new Date().getMonth() + 1) +
            '-01 00:00:00'
        )
      }
    }
  },
  created() {
    this.getList()
    this.getPlantOptions()
    this.getBuildingOptions()
    this.getDeviceOptions()
    this.getMeterCodeOptions()
  },
  watch: {
    'queryParams.plantId'(val) {
      if (val) {
        this.buildingDisabled = false
      } else {
        this.buildingDisabled = true
      }
    },
    'queryParams.buildingId'(val) {
      if (val && val.length > 0) {
        this.deviceDisabled = false
      } else {
        this.deviceDisabled = true
      }
    },
    'queryParams.deviceId'(val) {
      if (val && val.length > 0) {
        this.meterDisabled = false
      } else {
        this.meterDisabled = true
      }
    }
  },
  methods: {
    clickBuilding() {
      if (this.buildingDisabled) {
        this.$message.warning('请先选择工厂')
      }
    },
    clickDevice() {
      if (this.deviceDisabled) {
        this.$message.warning('请先选择建筑')
      }
    },
    clickMeter() {
      if (this.meterDisabled) {
        this.$message.warning('请先选择设备')
      }
    },
    changeQueryPlantOptions(value) {
      this.queryParams.buildingId = this.queryParams.deviceId = this.queryParams.meterId = []
      if (value === '') {
        this.queryParams.plantId = undefined
        this.queryBuildingOptions = this.buildingOptions
        this.queryDeviceOptions = this.deviceOptions
        this.queryMeterOptions = this.meterOptions
      } else {
        this.queryBuildingOptions = this.buildingOptions.filter(
          item => item.plantId == value
        )
        this.queryDeviceOptions = this.deviceOptions.filter(
          item => item.plantId == value
        )
        this.queryMeterOptions = this.meterOptions.filter(
          item => item.plantId == value
        )
      }
    },
    changeQueryBuildingOptions(value) {
      this.queryParams.deviceId = this.queryParams.meterId = []
      if (value.length === 0) {
        this.queryDeviceOptions = this.deviceOptions
        this.queryMeterOptions = this.meterOptions
      } else {
        this.queryDeviceOptions = this.deviceOptions.filter(item =>
          value.includes(item.buildingId)
        )
        this.queryMeterOptions = this.meterOptions.filter(item =>
          value.includes(item.buildingId)
        )
      }
    },
    changeQueryDeviceOptions(value) {
      this.queryParams.meterId = []
      if (value.length === 0) {
        this.queryMeterOptions = this.meterOptions
      } else {
        this.queryMeterOptions = this.meterOptions.filter(item =>
          value.includes(item.deviceId)
        )
      }
    },
    changeQueryMeterCodeOptions(value) {
      if (value.length === 0) {
      } else {
      }
    },
    changeQueryTime(value) {
      const year = value.getFullYear()
      const month = value.getMonth() + 1
      this.dataTime = '数据时间：' + year + '年' + month + '月'
    },
    getPlantOptions() {
      listPlant().then(res => {
        this.plantOptions = this.queryPlantOptions = res.rows
      })
    },
    getBuildingOptions() {
      listBuilding().then(res => {
        this.buildingOptions = this.queryBuildingOptions = res.rows
      })
    },
    getDeviceOptions() {
      listDevice().then(res => {
        this.deviceOptions = this.queryDeviceOptions = res.rows
      })
    },
    getMeterCodeOptions() {
      listMeter().then(res => {
        this.meterOptions = this.queryMeterOptions = res.rows
      })
    },
    indexMethod(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      )
    },
    /** 查询列表 */
    getList() {
      this.loading = true
      listMonthDosageOfElectricity(this.queryParams).then(response => {
        this.monthDosageOfElectricityList = response.rows
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
      this.$confirm('是否确认导出电量月报数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function() {
          return exportMonthDosageOfElectricity(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
        .catch(function() {})
    }
  }
}
</script>