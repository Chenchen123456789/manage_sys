<template>
  <div class="app-container">
    <router-view />
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
          v-hasPermi="['energy:report_historyDetail:export']"
          slot="reference"
        >导出</el-button>
      </el-popover>
    </el-row>

    <el-table border style="width:100%" v-loading="loading" :data="historyDetailList">
      <el-table-column label="序号" type="index" :index="indexMethod" width="50" />
      <el-table-column label="时间" align="center" prop="dataTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dataTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="工厂名" align="center" prop="plantName" />
      <el-table-column label="建筑名" align="center" prop="buildingName" />
      <el-table-column label="能源类型" align="center" prop="energyTypeName" width="80"  />
      <el-table-column label="设备名" align="center" prop="deviceName" />
      <el-table-column label="仪表编号" align="center" prop="meterCode" />
      <el-table-column label="数据位号" align="center" prop="tagName" />
      <el-table-column label="数据值" align="center" prop="tagValue" />
      <el-table-column label="系统名" align="center" prop="systemName" />
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
      buildingDisabled: true,
      deviceDisabled: true,
      meterDisabled: true,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        queryTypeId: 1,
        queryTime: new Date(new Date().getFullYear() + '-01-01 00:00:00'),
        companyId: undefined,
        plantId: undefined,
        plantId: undefined,
        buildingId: [],
        deviceId: [],
        meterId: [],
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
    changeQueryTimeOfHour(value) {
      if (value) {
        this.queryParams.queryTime = new Date(value + ':00:00')
      }
    },
    changeQueryCompanyOptions(value) {
      this.queryParams.plantId = null
      this.queryParams.buildingId = this.queryParams.deviceId = this.queryParams.meterId = []
      if (value === '') {
        this.queryPlantOptions = this.plantOptions
        this.queryBuildingOptions = this.buildingOptions
        this.queryDeviceOptions = this.deviceOptions
        this.queryMeterOptions = this.meterOptions
      } else {
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
    /** 查询列表 */
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
    handleExport(type) {
      const queryParams = { ...this.queryParams }
      if (type === 0) {
        queryParams.pageNum = null
      }
      this.$confirm('是否确认导出历史详细数据项?', '警告', {
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