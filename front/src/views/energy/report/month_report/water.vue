<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" label-width="68px" ref="queryForm">
      <el-form-item label="单位名称" prop="plantId">
        <el-select
          @change="changeQueryPlantOptions"
          clearable
          placeholder="请选择"
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
      <el-form-item label="查询时间" prop="queryTime">
        <el-date-picker
          :clearable="false"
          placeholder="选择月"
          size="small"
          type="month"
          v-model="queryParams.queryTime"
        ></el-date-picker>
      </el-form-item>
     
      <el-form-item label="计量等级" prop="measureLevel">
        <el-select size="small" clearable v-model="queryParams.measureLevel">
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
          v-hasPermi="['energy:report_monthDosageOfWater:export']"
        >导出</el-button>
      </el-popover>
    </el-row>

    <el-table :data="monthDosageOfWaterList" show-summary size="mini" v-loading="loading">
      <el-table-column :index="indexMethod" label="序号" type="index" width="50" />
      <el-table-column align="center" label="单位" prop="plantName" />
      <el-table-column align="center" label="测点名" prop="tagName" />
      <el-table-column align="center" label="测点描述" width="200" prop="tagNameDesc" />
      <el-table-column align="center" label="安装地点" prop="buildingName" />
      <el-table-column align="center" label="水表规格" prop="meterParam" />
      <el-table-column :label="dataTime" align="center">
        <el-table-column align="center" label="上月抄见" prop="preTimeValue" />
        <el-table-column align="center" label="本月抄见" prop="currentTimeValue" />
        <el-table-column align="center" label="实际用量" prop="realDosage" />
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
  listMonthDosageOfWater,
  exportMonthDosageOfWater
} from '@/api/energy/report'
import { listPlant } from '@/api/energy/plant'

export default {
  name: 'Water',
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 表格数据
      monthDosageOfWaterList: [],
      queryPlantOptions: [],
      dataTime:
        '数据时间：' +
        new Date().getFullYear() +
        '年' +
        (new Date().getMonth() + 1) +
        '月',
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
      }
    }
  },
  created () {
    this.getList()
    this.getPlantOptions()
  },
  methods: {
    changeQueryPlantOptions (value) {
      if (value == '') {
        this.queryParams.plantId = undefined
      }
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
      this.changeQueryTime(this.queryParams.queryTime)
      this.loading = true
      listMonthDosageOfWater(this.queryParams).then(response => {
        const list = response.rows
        for (const index in list) {
          const preTimeValue = list[index].preTimeValue.toFixed(2)
          const currentTimeValue = list[index].currentTimeValue.toFixed(2)
          const realDosage = (currentTimeValue - preTimeValue).toFixed(2)
          list[index].preTimeValue = preTimeValue
          list[index].currentTimeValue = currentTimeValue
          list[index].realDosage = realDosage
        }
        this.monthDosageOfWaterList = list
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
      this.$confirm('是否确认导出水量月报数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function () {
          return exportMonthDosageOfWater(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
        .catch(function () { })
    }
  }
}
</script>