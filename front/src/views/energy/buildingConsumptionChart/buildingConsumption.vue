<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" label-width="68px" ref="queryForm">
      <el-form-item label="工厂名称" prop="plantId">
        <el-select
          :clearable="false"
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
          :clearable="false"
          placeholder="请选择建筑"
          size="small"
          v-model="queryParams.buildingId"
        >
          <el-option
            :key="item.id"
            :label="item.buildingName"
            :value="item.id"
            v-for="item in queryBuildingOptions"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery" icon="el-icon-search" size="mini" type="primary">搜索</el-button>
        <el-button @click="resetQuery" icon="el-icon-refresh" size="mini" type="info">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="20">
      <el-col :span="12" :xs="24">
        <el-card class="box-card" id="bc_card1" style="height: 450px;">
          <div class="clearfix" slot="header">
            <span>能耗指标 {{this.selectedBuildingName}}</span>
          </div>
          <div>
            <div class="text-center">
              <BuildingDosageBarchart :buildingId="selectedBuildingId"></BuildingDosageBarchart>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12" :xs="24">
        <el-card class="box-card my-card" id="bc_card2" style="overflow: auto;height:450px">
          <div class="clearfix" slot="header">
            <span>建筑设备 {{this.selectedBuildingName}}</span>
          </div>
          <div>
            <div class="text-center">
              <el-table
                :data="filterDeviceList"
                border
                size="mini"
                style="width:100%"
                v-loading="loading"
              >
                <el-table-column align="center" label="序号" type="index" width="50" />
                <el-table-column align="center" label="建筑名称" prop="buildingName" width="150" />
                <el-table-column align="center" label="设备编号" prop="deviceCode" width="80" />
                <el-table-column align="center" label="设备名称" prop="deviceName" width="180"></el-table-column>
                <el-table-column align="center" label="设备描述" prop="deviceDescription" />
              </el-table>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="24" :xs="24">
        <el-card
          body-style="over-flow:auto"
          class="box-card my-card main-tag"
          style="min-height: 200px;margin-top:10px"
        >
          <div class="clearfix" slot="header">
            <span>建筑主要参数 {{this.selectedBuildingName}}</span>
          </div>
          <div>
            <div class="text-center" style="display:flex;">
              <BuildingMainTagValueChart
                :itemData="item"
                v-bind:key="index"
                v-for="(item,index) in mainTagValueList"
              ></BuildingMainTagValueChart>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import BuildingDosageBarchart from './buildingDosageBarchart'
import BuildingMainTagValueChart from './buildingMainTagValueChart'
import { listDevice } from '@/api/energy/device'
import { listBuildingMainMPValue } from '@/api/energy/report'
import { listBuilding } from '@/api/energy/building'
import { listPlant } from '@/api/energy/plant'

export default {
  components: {
    BuildingDosageBarchart,
    BuildingMainTagValueChart
  },
  data () {
    return {
      // 遮罩层
      loading: true,
      filterDeviceList: [],
      mainTagValueList: [],
      queryPlantOptions: [],
      queryBuildingOptions: [],
      buildingOptions: [],
      selectedBuildingName: '',
      selectedBuildingId: undefined,
      queryParams: {
        buildingId: undefined
      }
    }
  },
  created () {
    this.getPlantAndBuildingList()
  },
  watch: {
    'queryParams.plantId': {
      handler (value) {
        this.queryBuildingOptions = this.buildingOptions.filter(item => item.plantId == value)
        if (!this.queryBuildingOptions.find(item => item.id === this.queryParams.buildingId)) {
          this.queryParams.buildingId = undefined
        }
      }

    }
  },
  methods: {
    getPlantAndBuildingList () {
      listPlant().then(res => {
        this.queryPlantOptions = res.rows
      })

      listBuilding().then(res => {
        this.queryBuildingOptions = this.buildingOptions = res.rows
        const queryParams = { ...this.queryParams }
        queryParams.buildingId = res.rows[0].id
        queryParams.plantId = res.rows[0].plantId
        this.queryParams = queryParams
        this.selectedBuildingName = res.rows[0].buildingName + ''
        this.handleQuery()
      })
    },
    handleQuery () {
      if (!this.queryParams.buildingId) {
        this.$message.warning('请选择建筑')
        return
      }
      this.getDeviceList()
      this.getBuildingMainMPValueList()
      this.selectedBuildingId = new Number(this.queryParams.buildingId)
      this.selectedBuildingName = this.queryBuildingOptions.find(item => item.id == this.selectedBuildingId).buildingName
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    getBuildingMainMPValueList () {
      listBuildingMainMPValue(this.queryParams.buildingId).then(res => {
        this.mainTagValueList = res.data
      })
    },
    getDeviceList () {
      this.loading = true
      listDevice().then(res => {
        this.filterDeviceList = res.rows.filter(
          item => item.buildingId == this.queryParams.buildingId
        )
        this.loading = false
      })
    }
  }
}
</script>
<style lang="scss" >
.main-tag {
  .el-card__body {
    padding-bottom: 0px;
  }
}
</style>
