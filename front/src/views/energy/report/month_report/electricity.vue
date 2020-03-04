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
          size="small"
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
          clearable
          size="small"
          @change="changeQueryTime"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['energy:report_monthDosageOfElectricity:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table show-summary v-loading="loading" :data="monthDosageOfElectricityList">
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
        <el-table-column label="峰" align="center" prop="fvalue" />
        <el-table-column label="平" align="center" prop="pvalue" />
        <el-table-column label="谷" align="center" prop="gvalue" />
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
import { listMonthDosageOfElectricity, exportMonthDosageOfElectricity } from '@/api/energy/report'
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
      // 岗位表格数据
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
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        plantId: undefined,
        buildingId: undefined,
        deviceId: undefined,
        meterId: undefined,
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
  methods: {
    changeQueryPlantOptions(value) {
      if (value === '') {
        this.queryBuildingOptions = this.buildingOptions
        this.queryDeviceOptions = this.deviceOptions
        this.queryMeterOptions = this.meterOptions
      } else {
        const temp = this.queryParams
        temp.buildingId = temp.deviceId = temp.meterId = null
        const plant = this.plantOptions.find(item => item.id == value)
        this.queryParams = { ...temp }
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
      if (value === '') {
        this.queryDeviceOptions = this.deviceOptions
        this.queryMeterOptions = this.meterOptions
      } else {
        const building = this.buildingOptions.find(item => item.id == value)
        const plantId = building.plantId || null
        const temp = this.queryParams
        temp.plantId = plantId
        temp.deviceId = temp.meterId = null
        this.queryParams = { ...temp }
        this.queryDeviceOptions = this.deviceOptions.filter(
          item => item.buildingId == value
        )
        this.queryMeterOptions = this.meterOptions.filter(
          item => item.buildingId == value
        )
      }
    },
    changeQueryDeviceOptions(value) {
      if (value === '') {
        this.queryMeterOptions = this.meterOptions
      } else {
        const device = this.deviceOptions.find(item => item.id == value)
        const plantId = device.plantId || null
        const buildingId = device.buildingId || null
        const temp = this.queryParams
        temp.plantId = plantId
        temp.buildingId = buildingId
        temp.meterId = null
        this.queryParams = { ...temp }
        this.queryMeterOptions = this.meterOptions.filter(
          item => item.deviceId == value
        )
      }
    },
    changeQueryMeterCodeOptions(value) {
      if (value === '') {
      } else {
        const meter = this.meterOptions.find(item => item.id == value)
        const plantId = meter.plantId || null
        const buildingId = meter.buildingId || null
        const deviceId = meter.deviceId
        const temp = this.queryParams
        temp.plantId = plantId
        temp.buildingId = buildingId
        temp.deviceId = deviceId
        this.queryParams = { ...temp }
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
    /** 查询岗位列表 */
    getList() {
      this.loading = true
      listMonthDosageOfElectricity(this.queryParams).then(response => {
        this.MonthDosageOfElectricityList = response.rows
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
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm('是否确认导出所有岗位数据项?', '警告', {
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