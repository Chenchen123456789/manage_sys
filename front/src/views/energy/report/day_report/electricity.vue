<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" label-width="68px" ref="queryForm">
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
      <el-form-item label="查询时间" prop="queryTime">
        <el-date-picker
          :clearable="false"
          placeholder="选择日期"
          size="small"
          type="date"
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
          v-hasPermi="['energy:report_dayDosageOfElectricity:export']"
        >导出</el-button>
      </el-popover>
    </el-row>

    <el-table :data="dayDosageOfElectricityList" show-summary size="mini" v-loading="loading">
      <el-table-column :index="indexMethod" label="序号" type="index" width="50" />
      <el-table-column align="center" label="单位名称" prop="plantName" />
      <el-table-column align="center" label="建筑名称" prop="buildingName" />
      <el-table-column align="center" label="设备名称" prop="deviceName" />
      <el-table-column align="center" label="表号" prop="meterCode" />
      <el-table-column align="center" label="测点名称" prop="tagName" />
      <el-table-column align="center" label="装表地点" prop="meterLocation" />
      <el-table-column align="center" label="倍率" prop="meterParam" />
      <el-table-column align="center" label="昨天抄见数" prop="preTimeValue" />
      <el-table-column align="center" label="今天抄见数" prop="currentTimeValue" />
      <el-table-column :label="dataTime" align="center">
        <el-table-column align="center" label="峰" prop="fValue" />
        <el-table-column align="center" label="平" prop="pValue" />
        <el-table-column align="center" label="谷" prop="gValue" />
        <el-table-column align="center" label="总量" prop="totalValue"></el-table-column>
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
import {
  listDayDosageOfElectricity,
  exportDayDosageOfElectricity
} from '@/api/energy/report'
import { listPlant } from '@/api/energy/plant'
import { listBuilding } from '@/api/energy/building'
import { listDevice } from '@/api/energy/device'
import { listMeter } from '@/api/energy/meter'

export default {
  name: 'Electricity',
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 表格数据
      dayDosageOfElectricityList: [],
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
        queryTime: new Date()
      }
    }
  },
  created () {
    this.getList()
    this.getPlantOptions()
    this.getBuildingOptions()
    this.getDeviceOptions()
    this.getMeterCodeOptions()

    const year = new Date().getFullYear()
    const month = new Date().getMonth() + 1
    const day = new Date().getDate()
    this.dataTime = '数据时间：' + year + '年' + month + '月' + day + '日'
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
    changeQueryTime (value) {
      const year = value.getFullYear()
      const month = value.getMonth() + 1
      const day = value.getDate()
      this.dataTime = '数据时间：' + year + '年' + month + '月' + day + '日'
    },
    getPlantOptions () {
      listPlant().then(res => {
        this.plantOptions = this.queryPlantOptions = res.rows
      })
    },
    getBuildingOptions () {
      listBuilding().then(res => {
        this.buildingOptions = this.queryBuildingOptions = res.rows
      })
    },
    getDeviceOptions () {
      listDevice().then(res => {
        this.deviceOptions = this.queryDeviceOptions = res.rows
      })
    },
    getMeterCodeOptions () {
      listMeter().then(res => {
        this.meterOptions = this.queryMeterOptions = res.rows
      })
    },
    indexMethod (index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      )
    },
    /** 查询列表 */
    getList () {
      this.changeQueryTime(this.queryParams.queryTime)

      this.loading = true
      listDayDosageOfElectricity(this.queryParams).then(response => {
        const rows = response.rows
        for (let index in rows) {
          const preTimeValue = rows[index].preTimeValue.toFixed(2)
          const currentTimeValue = rows[index].currentTimeValue.toFixed(2)
          const fValue = rows[index].fValue.toFixed(2)
          const gValue = rows[index].gValue.toFixed(2)
          const pValue = rows[index].pValue.toFixed(2)
          const totalValue = (currentTimeValue - preTimeValue).toFixed(2)

          rows[index].preTimeValue = preTimeValue
          rows[index].currentTimeValue = currentTimeValue
          rows[index].fValue = fValue
          rows[index].gValue = gValue
          rows[index].pValue = pValue
          rows[index].totalValue = totalValue
        }
        this.dayDosageOfElectricityList = rows
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
      this.$confirm('是否确认导出电量日报数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function () {
          return exportDayDosageOfElectricity(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
        .catch(function () { })
    }
  }
}
</script>