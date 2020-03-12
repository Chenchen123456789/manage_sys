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
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
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
          v-hasPermi="['energy:monthDosageOfWater:export']"
          slot="reference"
        >导出</el-button>
      </el-popover>
    </el-row>

    <el-table show-summary v-loading="loading" :data="monthDosageOfWaterList">
      <el-table-column label="序号" type="index" :index="indexMethod" width="50" />
      <el-table-column label="单位" align="center" prop="plantName" />
      <el-table-column label="测点名" align="center" prop="tagName" />
      <el-table-column label="安装地点" align="center" prop="buildingName" />
      <el-table-column label="水表规格" align="center" prop="spe" />
      <el-table-column :label="dataTime" align="center">
        <el-table-column label="上月抄见" align="center" prop="preMonthValue" />
        <el-table-column label="本月抄见" align="center" prop="currentMonthValue" />
        <el-table-column label="实际用量" align="center" prop="realDosage" />
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
  listMonthDosageOfWater,
  exportMonthDosageOfWater
} from '@/api/energy/report'
import { listPlant } from '@/api/energy/plant'

export default {
  name: 'Water',
  data() {
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
  created() {
    this.getList()
    this.getPlantOptions()
  },
  methods: {
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
      listMonthDosageOfWater(this.queryParams).then(response => {
        const list = response.rows
        for (const index in list) {
          const preMonthValue = list[index].preMonthValue || 0
          const currentMonthValue = list[index].currentMonthValue || 0
          const realDosage = currentMonthValue - preMonthValue
          list[index].preMonthValue = preMonthValue
          list[index].currentMonthValue = currentMonthValue
          list[index].realDosage = realDosage
        }
        this.monthDosageOfWaterList = list
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
      this.$confirm('是否确认导出水量月报数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function() {
          return exportMonthDosageOfWater(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
        .catch(function() {})
    }
  }
}
</script>