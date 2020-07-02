<template>
  <div class="app-container">
    <router-view />
    <el-form :inline="true" :model="queryParams" label-width="68px" ref="queryForm">
      <el-form-item label="查询类型" prop="queryTypeId">
        <el-select style="width: 100px" placeholder="请选择" size="small" v-model="queryParams.queryTypeId">
          <el-option
            :key="item.id"
            :label="item.queryTypeName"
            :value="item.id"
            v-for="item in queryTypeOptions"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="时间范围">
        <el-date-picker
          end-placeholder="结束日期"
          range-separator="至"
          size="small"
          start-placeholder="开始日期"
          type="datetimerange"
          v-model="timeRange"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="公司名称" prop="companyId">
        <el-select
          @change="changeQueryCompanyOptions"
          clearable
          placeholder="请选择公司"
          size="small"
          v-model="queryParams.companyId"
        >
          <el-option
            :key="item.id"
            :label="item.companyName"
            :value="item.id"
            v-for="item in queryCompanyOptions"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="工厂名称" prop="plantId">
        <el-select
          @change="changeQueryPlantOptions"
          clearable
          placeholder="请选择工厂"
          size="small"
          v-model="queryParams.plantId"
        >
          <el-option
            :key="item.id"
            :label="item.plantName"
            :value="item.id"
            v-for="item in queryPlantOptions"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="建筑名称" prop="buildingId">
        <el-select
          :disabled="buildingDisabled"
          @change="changeQueryBuildingOptions"
          clearable
          collapse-tags
          multiple
          placeholder="请选择建筑"
          size="small"
          v-model="queryParams.buildingId"
          v-on:click.native="clickBuilding"
        >
          <el-option
            :key="item.id"
            :label="item.buildingName"
            :value="item.id"
            v-for="item in queryBuildingOptions"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="设备名称" prop="deviceId">
        <el-select
          :disabled="deviceDisabled"
          @change="changeQueryDeviceOptions"
          clearable
          collapse-tags
          multiple
          placeholder="请选择设备"
          size="small"
          v-model="queryParams.deviceId"
          v-on:click.native="clickDevice"
        >
          <el-option
            :key="item.id"
            :label="item.deviceName"
            :value="item.id"
            v-for="item in queryDeviceOptions"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="仪表编号" prop="meterId">
        <el-select
          style="width: 150px"
          :disabled="meterDisabled"
          @change="changeQueryMeterCodeOptions"
          clearable
          collapse-tags
          multiple
          placeholder="请选择仪表编号"
          size="small"
          v-model="queryParams.meterId"
          v-on:click.native="clickMeter"
        >
          <el-option
            :key="item.id"
            :label="item.meterCode"
            :value="item.id"
            v-for="item in queryMeterOptions"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="系统名称" prop="systemId">
        <el-select style="width: 150px" clearable placeholder="请选择系统" size="small" v-model="queryParams.systemId">
          <el-option
            :key="item.systemId"
            :label="item.systemName"
            :value="item.systemId"
            v-for="item in querySystemOptions"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="能源类型" prop="energyTypeId">
        <el-select style="width: 150px" clearable placeholder="请选择能源" size="small" v-model="queryParams.energyTypeId">
          <el-option
            :key="item.energyTypeId"
            :label="item.energyTypeName"
            :value="item.energyTypeId"
            v-for="item in queryEnergyTypeOptions"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="大功率" prop="deviceType">
        <el-select style="width: 150px" clearable size="small" v-model="queryParams.deviceType">
          <el-option
            :key="item.id"
            :label="item.label"
            :value="item.id"
            v-for="item in [{id: 1, label:'大功率电器' },{ id:0, label: '未分类' }]"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="计量等级" prop="measureLevel">
        <el-select style="width: 150px" clearable size="small" v-model="queryParams.measureLevel">
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
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-popover placement="bottom" trigger="click">
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
          v-hasPermi="['energy:report_historyDetail:export']"
        >导出</el-button>
      </el-popover>
    </el-row>

    <el-table :data="historyDetailList" border size="mini" style="width:100%" v-loading="loading">
      <el-table-column :index="indexMethod" align="center" label="序号" type="index" width="60" />
      <el-table-column align="center" label="时间" prop="showTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.showTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="工厂名" prop="plantName" width="120" />
      <el-table-column align="center" label="建筑名" prop="buildingName" width="120" />
      <el-table-column align="center" label="能源类型" prop="energyTypeName" width="80" />
      <el-table-column align="center" label="设备名" prop="deviceName" width="150" />
      <el-table-column align="center" label="仪表编号" prop="meterCode" width="120" />
      <el-table-column align="center" label="数据位号" prop="tagName" width="150" />
      <el-table-column align="center" label="数据值" prop="tagValue" width="100" />
      <el-table-column align="center" label="系统名" prop="systemName" width="120" />
      <el-table-column align="center" label="数据点描述" prop="description" />
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
  data () {
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
        { id: 4, queryTypeName: '小时' },
        { id: 5, queryTypeName: '分钟' }
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
      timeRange: [undefined, new Date()],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        queryTypeId: 1,
        companyId: undefined,
        plantId: undefined,
        plantId: undefined,
        buildingId: [],
        deviceId: [],
        meterId: [],
        systemId: undefined,
        energyTypeId: undefined,
        deviceType: undefined
      }
    }
  },
  created () {
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
    'queryParams.plantId' (val) {
      if (val) {
        this.buildingDisabled = false
      } else {
        this.buildingDisabled = true
      }
    },
    'queryParams.buildingId' (val) {
      if (val && val.length > 0) {
        this.deviceDisabled = false
      } else {
        this.deviceDisabled = true
      }
    },
    'queryParams.deviceId' (val) {
      if (val && val.length > 0) {
        this.meterDisabled = false
      } else {
        this.meterDisabled = true
      }
    }
  },
  methods: {
    changeQueryCompanyOptions (value) {
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
    clickBuilding () {
      if (this.buildingDisabled) {
        this.$message.warning('请先选择工厂')
      }
    },
    clickDevice () {
      if (this.deviceDisabled) {
        this.$message.warning('请先选择建筑')
      }
    },
    clickMeter () {
      if (this.meterDisabled) {
        this.$message.warning('请先选择设备')
      }
    },
    changeQueryPlantOptions (value) {
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
    changeQueryBuildingOptions (value) {
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
    changeQueryDeviceOptions (value) {
      this.queryParams.meterId = []
      if (value.length === 0) {
        this.queryMeterOptions = this.meterOptions
      } else {
        this.queryMeterOptions = this.meterOptions.filter(item =>
          value.includes(item.deviceId)
        )
      }
    },
    changeQueryMeterCodeOptions (value) {
      if (value.length === 0) {
      } else {
      }
    },
    getCompanyOptions () {
      listCompany().then(res => {
        this.queryCompanyOptions = res.rows
      })
    },
    getPlantOptions () {
      listPlant().then(res => {
        this.queryPlantOptions = this.plantOptions = res.rows
      })
    },
    getBuildingOptions () {
      listBuilding().then(res => {
        this.queryBuildingOptions = this.buildingOptions = res.rows
      })
    },
    getDeviceOptions () {
      listDevice().then(res => {
        this.queryDeviceOptions = this.deviceOptions = res.rows
      })
    },
    getMeterCodeOptions () {
      listMeter().then(res => {
        this.queryMeterOptions = this.meterOptions = res.rows
      })
    },
    getClassOptions () {
      listClass().then(res => {
        this.queryClassOptions = res.rows
      })
    },
    getEnergyTypeOptions () {
      listEnergyType().then(res => {
        this.queryEnergyTypeOptions = res.rows
      })
    },
    getSignalTypeOptions () {
      listSignalType().then(res => {
        this.querySignalTypeOptions = res.rows
      })
    },
    getSystemOptions () {
      listSystem().then(res => {
        this.querySystemOptions = res.rows
      })
    },
    indexMethod (index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      )
    },
    /** 查询列表 */
    getList () {
      this.loading = true
      listHistoryDetail(this.addDateRange(this.queryParams, this.timeRange)).then(response => {
        this.historyDetailList = response.rows
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
      this.$confirm('是否确认导出历史详细数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function () {
          return exportHistoryDetail(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
        .catch(function () { })
    }
  }
}
</script>