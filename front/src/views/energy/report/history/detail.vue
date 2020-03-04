<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="查询类型" prop="queryTypeId">
        <el-select v-model="queryParams.queryTypeId" placeholder="请选择" size="small">
          <el-option
            v-for="item in queryTypeOptions"
            :key="item.id"
            :label="item.queryTypeName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="查询时间" prop="queryTime">
        <el-date-picker
          v-show="queryParams.queryTypeId == 1"
          v-model="queryParams.queryTime"
          type="year"
          placeholder="选择年"
          clearable
          size="small"
        ></el-date-picker>
        <el-date-picker
          v-show="queryParams.queryTypeId == 2"
          v-model="queryParams.queryTime"
          type="month"
          placeholder="选择月"
          clearable
          size="small"
        ></el-date-picker>
        <el-date-picker
          v-show="queryParams.queryTypeId == 3"
          v-model="queryParams.queryTime"
          type="date"
          placeholder="选择日期"
          clearable
          size="small"
        ></el-date-picker>
        <el-date-picker
          v-show="queryParams.queryTypeId == 4"
          v-model="queryParams.queryTime"
          type="datetime"
          @change="changeQueryTimeOfHour"
          align="left"
          placeholder="选择时间"
          value-format="yyyy-MM-dd HH"
          clearable
          size="small"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="公司名称" prop="companyId">
        <el-select
          clearable
          @change="changeQueryCompanyOptions"
          v-model="queryParams.companyId"
          placeholder="请选择公司"
          size="small"
        >
          <el-option
            v-for="item in queryCompanyOptions"
            :key="item.id"
            :label="item.companyName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
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
      <el-form-item label="系统名称" prop="systemId">
        <el-select clearable v-model="queryParams.systemId" placeholder="请选择系统" size="small">
          <el-option
            v-for="item in querySystemOptions"
            :key="item.systemId"
            :label="item.systemName"
            :value="item.systemId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="能源类型" prop="energyTypeId">
        <el-select clearable v-model="queryParams.energyTypeId" placeholder="请选择能源" size="small">
          <el-option
            v-for="item in queryEnergyTypeOptions"
            :key="item.energyTypeId"
            :label="item.energyTypeName"
            :value="item.energyTypeId"
          />
        </el-select>
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
          v-hasPermi="['enery:report_historyDetail:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table border style="width:100%" v-loading="loading" :data="historyDetailList">
      <el-table-column label="序号" type="index" :index="indexMethod" width="50" />
      <el-table-column label="时间" align="center" prop="dataTime" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dataTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="工厂名" align="center" prop="plantName" />
      <el-table-column label="建筑名" align="center" prop="buildingName" />
      <el-table-column label="系统名" align="center" prop="systemName" />
      <el-table-column label="能源类型" align="center" prop="energyTypeName" />
      <el-table-column label="设备名" align="center" prop="deviceName" />
      <el-table-column label="仪表编号" align="center" prop="deviceId" />
      <el-table-column label="数据位号" align="center" prop="tagName" />
      <el-table-column label="数据值" align="center" prop="tagValue" />
      <el-table-column label="数据点描述" align="center" prop="description" />
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
  listHistoryDetail,
  exportHistoryDetail,
  listClass,
  listEnergyType,
  listSignalType,
  listSystem
} from '@/api/energy/report'
import { listCompany } from '@/api/energy/company'
import { listPlant } from '@/api/energy/plant'
import { listBuilding } from '@/api/energy/building'
import { listDevice } from '@/api/energy/device'
import { listMeter } from '@/api/energy/meter'

export default {
  name: 'Detail',
  data() {
    return {
      // 遮罩层
      loading: true,
      total: 0,
      historyDetailList: [],
      companyOptions: [],
      queryTypeOptions: [
        { id: 1, queryTypeName: '年' },
        { id: 2, queryTypeName: '月' },
        { id: 3, queryTypeName: '日' },
        { id: 4, queryTypeName: '小时' }
      ],
      queryCompanyOptions: [],
      queryPlantOptions: [],
      queryBuildingOptions: [],
      queryDeviceOptions: [],
      queryMeterOptions: [],
      plantOptions: [],
      buildingOptions: [],
      deviceOptions: [],
      meterOptions: [],
      queryClassOptions: [],
      queryEnergyTypeOptions: [],
      querySignalTypeOptions: [],
      querySystemOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        queryTypeId: 1,
        queryTime: new Date(new Date().getFullYear() + '-01-01 00:00:00'),
        companyId: undefined,
        plantId: undefined,
        companyId: undefined,
        plantId: undefined,
        buildingId: undefined,
        deviceId: undefined,
        meterId: undefined,
        systemId: undefined,
        energyTypeId: undefined
      }
    }
  },
  created() {
    this.getList()
    this.getCompanyOptions()
    this.getPlantOptions()
    this.getBuildingOptions()
    this.getDeviceOptions()
    this.getMeterCodeOptions()
    this.getClassOptions()
    this.getEnergyTypeOptions()
    this.getSignalTypeOptions()
    this.getSystemOptions()
  },
  methods: {
    changeQueryTimeOfHour(value) {
      if (value) {
        this.queryParams.queryTime = new Date(value + ':00:00')
      }
    },
    changeQueryCompanyOptions(value) {
      if (value === '') {
        this.queryPlantOptions = this.plantOptions
        this.queryBuildingOptions = this.buildingOptions
        this.queryDeviceOptions = this.deviceOptions
        this.queryMeterOptions = this.meterOptions
      } else {
        const temp = this.queryParams
        temp.plantId = temp.buildingId = temp.deviceId = temp.meterId = null
        this.queryParams = { ...temp }
        this.queryPlantOptions = this.plantOptions.filter(
          item => item.companyId == value
        )
        this.queryBuildingOptions = this.buildingOptions.filter(
          item => item.companyId == value
        )
        this.queryDeviceOptions = this.deviceOptions.filter(
          item => item.companyId == value
        )
        this.queryMeterOptions = this.meterOptions.filter(
          item => item.companyId == value
        )
      }
    },
    changeQueryPlantOptions(value) {
      if (value === '') {
        this.queryBuildingOptions = this.buildingOptions
        this.queryDeviceOptions = this.deviceOptions
        this.queryMeterOptions = this.meterOptions
      } else {
        const temp = this.queryParams
        temp.buildingId = temp.deviceId = temp.meterId = null
        const plant = this.plantOptions.find(item => item.id == value)
        const companyId = plant.companyId
        temp.companyId = companyId
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
        const companyId = building.companyId
        const plantId = building.plantId || null
        const temp = this.queryParams
        temp.companyId = companyId
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
        const companyId = device.companyId
        const plantId = device.plantId || null
        const buildingId = device.buildingId || null
        const temp = this.queryParams
        temp.companyId = companyId
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
        const companyId = meter.companyId
        const plantId = meter.plantId || null
        const buildingId = meter.buildingId || null
        const deviceId = meter.deviceId
        const temp = this.queryParams
        temp.companyId = companyId
        temp.plantId = plantId
        temp.buildingId = buildingId
        temp.deviceId = deviceId
        this.queryParams = { ...temp }
      }
    },
    getCompanyOptions() {
      listCompany().then(res => {
        this.queryCompanyOptions = res.rows
      })
    },
    getPlantOptions() {
      listPlant().then(res => {
        this.queryPlantOptions = this.plantOptions = res.rows
      })
    },
    getBuildingOptions() {
      listBuilding().then(res => {
        this.queryBuildingOptions = this.buildingOptions = res.rows
      })
    },
    getDeviceOptions() {
      listDevice().then(res => {
        this.queryDeviceOptions = this.deviceOptions = res.rows
      })
    },
    getMeterCodeOptions() {
      listMeter().then(res => {
        this.queryMeterOptions = this.meterOptions = res.rows
      })
    },
    getClassOptions() {
      listClass().then(res => {
        this.queryClassOptions = res.rows
      })
    },
    getEnergyTypeOptions() {
      listEnergyType().then(res => {
        this.queryEnergyTypeOptions = res.rows
      })
    },
    getSignalTypeOptions() {
      listSignalType().then(res => {
        this.querySignalTypeOptions = res.rows
      })
    },
    getSystemOptions() {
      listSystem().then(res => {
        this.querySystemOptions = res.rows
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
      listHistoryDetail(this.queryParams).then(response => {
        this.historyDetailList = response.rows
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
          return exportHistoryDetail(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
        .catch(function() {})
    }
  }
}
</script>