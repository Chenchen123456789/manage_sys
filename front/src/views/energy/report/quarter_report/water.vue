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
          placeholder="选择年"
          size="small"
          type="year"
          v-model="queryParams.queryTime"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="选择季度" prop="queryTimeQuarter">
        <el-select size="small" :clearable="false" v-model="queryParams.queryTimeQuarter">
          <el-option
            :key="item.id"
            :label="item.label"
            :value="item.id"
            v-for="item in quarterOptions"
          ></el-option>
        </el-select>
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
          v-hasPermi="['energy:report_quarterDosageOfWater:export']"
        >导出</el-button>
      </el-popover>
    </el-row>

    <el-table :data="quarterDosageOfWaterList" show-summary size="mini" v-loading="loading">
      <el-table-column :index="indexMethod" label="序号" type="index" width="50" />
      <el-table-column align="center" label="单位" prop="plantName" />
      <el-table-column align="center" label="测点名" prop="tagName" />
      <el-table-column align="center" label="安装地点" prop="buildingName" />
      <el-table-column align="center" label="水表规格" prop="meterParam" />
      <el-table-column :label="dataTime" align="center">
        <el-table-column align="center" label="上季度抄见" prop="preTimeValue" />
        <el-table-column align="center" label="本季度抄见" prop="currentTimeValue" />
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
  listQuarterDosageOfWater,
  exportQuarterDosageOfWater
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
      quarterDosageOfWaterList: [],
      queryPlantOptions: [],
      dataTime:
        '数据时间：' +
        new Date().getFullYear() +
        '年' + ' 第一季度',
      // 查询参数
      quarterOptions: [{ id: 1, label: '第一季度' }, { id: 2, label: '第二季度' }, { id: 3, label: '第三季度' }, { id: 4, label: '第四季度' }],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        queryTime: new Date(),
        queryTimeQuarter: 1,
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
    changeQueryTime () {
      const year = this.queryParams.queryTime.getFullYear()
      const queryTimeQuarter = this.queryParams.queryTimeQuarter
      let queryTimeQuarterStr = this.quarterOptions.find(item => item.id == queryTimeQuarter).label
      this.dataTime = '数据时间：' + year + '年 ' + queryTimeQuarterStr
    },
    indexMethod (index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      )
    },
    /** 查询列表 */
    getList () {
      this.changeQueryTime()
      this.loading = true
      const queryParams = { ...this.queryParams }
      const queryTimeQuarter = queryParams.queryTimeQuarter
      const queryTimeYear = queryParams.queryTime.getFullYear()
      queryParams.queryTimeYear = queryTimeYear
      queryParams.queryTimeQuarter = queryTimeQuarter
      queryParams.preQueryTimeYear = queryTimeYear
      queryParams.preQueryTimeQuarter = queryTimeQuarter - 1
      if (queryTimeQuarter == 1) {
        queryParams.preQueryTimeYear = queryTimeYear - 1
        queryParams.preQueryTimeQuarter = 4
      }
      listQuarterDosageOfWater(queryParams).then(response => {
        const list = response.rows
        for (const index in list) {
         const preTimeValue = list[index].preTimeValue.toFixed(2)
          const currentTimeValue = list[index].currentTimeValue.toFixed(2)
          const realDosage = (currentTimeValue - preTimeValue).toFixed(2)
          list[index].preTimeValue = preTimeValue
          list[index].currentTimeValue = currentTimeValue
          list[index].realDosage = realDosage
        }
        this.quarterDosageOfWaterList = list
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
      const queryTimeQuarter = queryParams.queryTimeQuarter
      const queryTimeYear = queryParams.queryTime.getFullYear()
      queryParams.queryTimeYear = queryTimeYear
      queryParams.queryTimeQuarter = queryTimeQuarter
      queryParams.preQueryTimeYear = queryTimeYear
      queryParams.preQueryTimeQuarter = queryTimeQuarter - 1
      if (queryTimeQuarter == 1) {
        queryParams.preQueryTimeYear = queryTimeYear - 1
        queryParams.preQueryTimeQuarter = 4
      }
      if (type === 0) {
        queryParams.pageNum = null
      }
      this.$confirm('是否确认导出水量季度报数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function () {
          return exportQuarterDosageOfWater(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
        .catch(function () { })
    }
  }
}
</script>