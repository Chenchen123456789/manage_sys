<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" label-width="68px" ref="queryForm">
      <el-form-item label="查询时间" prop="queryTime">
        <el-date-picker
          :clearable="false"
          placeholder="选择月"
          size="small"
          type="month"
          v-model="queryParams.queryTime"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="工厂名称" prop="plantId">
        <el-select
          :clearable="false"
          @change="changeQueryPlantOptions"
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
          @change="changeQueryBuildingOptions"
          collapse-tags
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
      <el-form-item label="计量等级" prop="measureLevel">
        <el-select clearable size="small" style="width: 150px" v-model="queryParams.measureLevel">
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
      <el-col :span="1.5">
        <el-button @click="handleEdit" icon="el-icon-edit" size="mini" type="primary">编辑</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button @click="handleCancelEdit" icon="el-icon-refresh-left" size="mini" type="danger">取消编辑</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          @click="handleSave"
          icon="el-icon-check"
          size="mini"
          type="success"
          v-hasPermi="['energy:transferNotice:add']"
        >保存</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          @click="handleExport(0)"
          icon="el-icon-download"
          size="mini"
          type="warning"
          v-hasPermi="['energy:transferNotice:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button class="print-button" icon="el-icon-printer" size="mini" v-print="printObj">打印</el-button>
      </el-col>
    </el-row>

    <el-divider class="first-divider"></el-divider>
    <el-row
      :gutter="10"
      style="display:flex; align-items:center; justify-content:stretch;margin-left: 0px"
    >
      <el-col :span="3">
        <span>水单价：{{waterPrice }} 元/m³</span>
      </el-col>
      <el-col :span="3">
        <span>电-峰单价：{{electricityFPrice }} 元/kWh</span>
      </el-col>
      <el-col :span="3">
        <span>电-平单价：{{electricityPPrice }} 元/kWh</span>
      </el-col>
      <el-col :span="3">
        <span>电-谷单价：{{electricityGPrice }} 元/kWh</span>
      </el-col>
      <el-col :span="3">
        <span>电-综合单价：{{electricityPrice }} 元/kWh</span>
      </el-col>
      <el-col :span="3">
        <span>空气单价：{{airPrice }} 元/m³</span>
      </el-col>
      <el-col :span="3">
        <span>蒸汽单价：{{steamPrice }} 元/m³</span>
      </el-col>
      <el-col :span="1.5">
        <el-button @click="handleUpdate" icon="el-icon-edit" size="mini" type="success">修改</el-button>
      </el-col>
    </el-row>

    <el-dialog :visible.sync="open" title="修改" width="500px">
      <el-form :model="form" label-width="90px" ref="form">
        <el-form-item label="水单价" prop="waterPrice">
          <el-input-number placeholder="请输入水单价" v-model="form.waterPrice" />
        </el-form-item>
        <el-form-item label="电-峰单价" prop="electricityFPrice">
          <el-input-number placeholder="请输入电-峰单价" v-model="form.electricityFPrice" />
        </el-form-item>
        <el-form-item label="电-平单价" prop="electricityPPrice">
          <el-input-number placeholder="请输入电-平单价" v-model="form.electricityPPrice" />
        </el-form-item>
        <el-form-item label="电-谷单价" prop="electricityGPrice">
          <el-input-number placeholder="请输入电-谷单价" v-model="form.electricityGPrice" />
        </el-form-item>
        <el-form-item label="电-综合单价" prop="electricityPrice">
          <el-input-number placeholder="请输入电-总计单价" v-model="form.electricityPrice" />
        </el-form-item>
        <el-form-item label="空气单价" prop="airPrice">
          <el-input-number placeholder="请输入空气单价" v-model="form.airPrice" />
        </el-form-item>
        <el-form-item label="蒸汽单价" prop="steamPrice">
          <el-input-number placeholder="请输入蒸汽单价" v-model="form.steamPrice" />
        </el-form-item>
      </el-form>
      <div class="dialog-footer" slot="footer">
        <el-button @click="submitForm" type="primary">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-divider></el-divider>

    <el-row class="table-notice" id="printArea" ref="printArea">
      <el-col :span="24" class="notice-title">费用转账通知单</el-col>
      <el-col :span="16" class="in-department">转入单位：{{tableData.inDepartment}}</el-col>
      <el-col :span="8" class="query-time">{{formatTime(queryParams.queryTime)}}</el-col>

      <el-table :data="transferNoticeList" size="medium" style="width:100%" v-loading="loading">
        <el-table-column align="center" label="费用项目" prop="energyTypeName" width="150" />
        <el-table-column align="center" label="数量" prop="amount">
          <template slot-scope="scope">
            <span v-show="!showEdit">{{scope.row.amount}}</span>
            <el-input-number
              :controls="false"
              :precision="2"
              placeholder="请输入"
              v-if="scope.row.amount!='-'"
              v-model="scope.row.amount"
              v-show="showEdit"
            ></el-input-number>
          </template>
        </el-table-column>
        <el-table-column align="center" label="单价（元）" prop="price">
          <template slot-scope="scope">
            <span v-show="!showEdit">{{scope.row.price}}</span>
            <el-input-number
              :controls="false"
              :precision="2"
              placeholder="请输入"
              v-if="scope.row.price!='-'"
              v-model="scope.row.price"
              v-show="showEdit"
            ></el-input-number>
          </template>
        </el-table-column>
        <el-table-column align="center" label="金额（元）" prop="totalPrice">
          <template slot-scope="scope">
            <span v-show="!showEdit">{{scope.row.totalPrice}}</span>
            <span
              v-if="scope.row.price !='-' && showEdit"
            >{{ (scope.row.amount *scope.row.price).toFixed(2) }}</span>
            <span v-if="scope.row.price =='-' && showEdit">{{sumTotalPrice }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="补充说明" prop="note">
          <template slot-scope="scope">
            <span v-show="!showEdit">{{scope.row.note}}</span>
            <el-input
              placeholder="请输入"
              v-if="scope.row.note!='-'"
              v-model="scope.row.note"
              v-show="showEdit"
            ></el-input>
          </template>
        </el-table-column>
      </el-table>
      <div class="notice-footer">
        <el-col :span="10" class="out-department">
          转出单位：
          <span v-show="!showEdit">{{tableData.outDepartment}}</span>
          <el-input
            placeholder="请输入"
            style="width: 250px"
            v-model="tableData.outDepartment"
            v-show="showEdit"
          ></el-input>
        </el-col>
        <el-col :span="8" class="modify-user">
          制表人：
          <span v-show="!showEdit">{{tableData.modifyUser}}</span>
          <el-input
            placeholder="请输入"
            style="width: 250px;"
            v-model="tableData.modifyUser"
            v-show="showEdit"
          ></el-input>
        </el-col>
      </div>
    </el-row>
  </div>
</template>

<script>
import {
  listTransferNotice,
  exportTransferNotice,
  addTransferNotice
} from '@/api/energy/transferNotice'
import { listPlant } from '@/api/energy/plant'
import { listBuilding } from '@/api/energy/building'
import { listUnitPrice, updateUnitPrice } from '@/api/energy/unitPrice'

export default {
  name: 'TransferNotice',
  data () {
    return {
      printObj: {
        id: "#printArea",
        extraHead: '<meta http-equiv="Content-Language"content="zh-cn"/>'
      },
      // 遮罩层
      loading: false,
      total: 0,
      transferNoticeList: [],
      queryPlantOptions: [],
      queryBuildingOptions: [],
      plantOptions: [],
      buildingOptions: [],
      queryParams: {
        queryTime: new Date(),
        plantId: undefined,
        buildingId: undefined,
      },
      tableData: {
        outDepartment: '动力站'
      },
      open: false,
      form: {},
      waterPrice: 0,
      airPrice: 0,
      electricityPrice: 0,
      steamPrice: 0,
      electricityFPrice: 0,
      electricityPPrice: 0,
      electricityGPrice: 0,
      showEdit: false,
      sumTotalPrice: 0,
    }
  },
  created () {
    this.getUnitPriceList()
    this.getPlantOptions()
    this.getBuildingOptions()
    this.tableData.modifyUser = this.$store.state.user.name
  },
  watch: {
    transferNoticeList: {
      deep: true,
      handler (val) {
        if (val) {
          this.calTotalPrice(val)
        }
      }
    }
  },
  methods: {
    handleCancelEdit(){
      this.showEdit = false
      this.handleQuery()
    },
    calTotalPrice (val) {
      let sumTotalPrice = 0
      for (const iterator of [...val].splice(0, 6)) {
        sumTotalPrice += Number(iterator.amount) * Number(iterator.price)
      }
      this.sumTotalPrice = sumTotalPrice.toFixed(2)
    },
    handleEdit () {
      this.showEdit = true
    },
    getUnitPriceList () {
      listUnitPrice().then(res => {
        this.unitPriceList = res.data
        this.waterPrice =
          res.data.find(item => item.energyType == '水').price || 0
        this.electricityPrice =
          res.data.find(item => item.energyType == '电').price || 0

        this.electricityFPrice =
          res.data.find(item => item.energyType == '电-峰').price || 0
        this.electricityPPrice =
          res.data.find(item => item.energyType == '电-平').price || 0
        this.electricityGPrice =
          res.data.find(item => item.energyType == '电-谷').price || 0

        this.airPrice =
          res.data.find(item => item.energyType == '空气').price || 0
        this.steamPrice =
          res.data.find(item => item.energyType == '蒸汽').price || 0
      })
    },
    // 取消按钮
    cancel () {
      this.open = false
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs['form'].validate(valid => {
        if (valid) {
          updateUnitPrice(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess('修改成功')
              this.open = false
              this.getUnitPriceList()
            } else {
              this.msgError(response.msg)
            }
          })
        }
      })
    },
    handleUpdate () {
      this.open = true
      this.form.waterPrice = this.waterPrice
      this.form.electricityPrice = this.electricityPrice

      this.form.electricityFPrice = this.electricityFPrice
      this.form.electricityPPrice = this.electricityPPrice
      this.form.electricityGPrice = this.electricityGPrice

      this.form.airPrice = this.airPrice
      this.form.steamPrice = this.steamPrice
    },
    handleSave () {
      if (!this.showEdit) {
        return
      }
      const transferNoticeList = [...this.transferNoticeList].splice(0, 6)
      const tableData = { ...this.tableData }
      for (let i = 0; i < transferNoticeList.length; i++) {
        const item = transferNoticeList[i]
        item.buildingId = this.queryParams.buildingId
        item.dataTime = this.queryParams.queryTime
        item.outDepartment = tableData.outDepartment
        item.modifyUser = tableData.modifyUser
        item.createTime = this.parseTime(new Date())
        item.createBy = this.$store.state.user.name
      }
      addTransferNotice(transferNoticeList).then(response => {
        if (response.code === 200) {
          this.msgSuccess('修改成功')
          this.open = false
          this.handleQuery()
        } else {
          this.msgError(response.msg)
        }
        this.showEdit = false
      })
    },
    formatTime (time) {
      if (time) {
        const year = time.getFullYear();
        const month = time.getMonth() + 1
        return year + '年' + month + '月'
      }
    },
    changeQueryPlantOptions (value) {
      this.queryParams.buildingId = undefined
      if (value === '') {
        this.queryParams.plantId = undefined
        this.queryBuildingOptions = this.buildingOptions
      } else {
        this.queryBuildingOptions = this.buildingOptions.filter(
          item => item.plantId == value
        )
      }
    },
    changeQueryBuildingOptions (value) {
      if (value === '') {
      } else {
        const building = this.buildingOptions.find(item => item.id == value)
        const plantId = building.plantId || null
        this.queryParams.plantId = plantId
      }
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
    /** 查询列表 */
    getList () {
      this.loading = true
      listTransferNotice(this.queryParams).then(response => {
        const rows = response.rows
        const FPGList = rows[0].FPGList
        const settlementList = rows[0].settlementList
        if (FPGList || settlementList) {
          if (FPGList.length > 0 || settlementList.length > 0) {
            const FPGItem = FPGList[0]
            const settlemenItem = settlementList[0]
            const resList = []
            let waterDosage = 0, electricityDosage = 0, steamDosage = 0, airDosage = 0, electricityFDosage = 0, electricityPDosage = 0, electricityGDosage = 0
            if (settlemenItem) {
              waterDosage = (settlemenItem.currentWaterSumValue - settlemenItem.preWaterSumValue).toFixed(2)
              electricityDosage = (settlemenItem.currentElectricitySumValue - settlemenItem.preElectricitySumValue).toFixed(2)
              steamDosage = (settlemenItem.currentSteamSumValue - settlemenItem.preSteamSumValue).toFixed(2)
              airDosage = (settlemenItem.currentAirSumValue - settlemenItem.preAirSumValue).toFixed(2)
            }
            if (FPGItem) {
              electricityFDosage = (FPGItem.curSumFValue ).toFixed(2)
              electricityPDosage = (FPGItem.curSumPValue ).toFixed(2)
              electricityGDosage = (FPGItem.curSumGValue).toFixed(2)
            }
            resList.push(
              {
                energyType: 1,
                energyTypeName: '水（m³）',
                amount: waterDosage,
                price: this.waterPrice,
                totalPrice: (this.waterPrice * waterDosage).toFixed(2),
                note: ''
              })
            resList.push(
              {
                energyType: 2,
                energyTypeName: '电-峰（KWh）',
                amount: electricityFDosage,
                price: this.electricityFPrice,
                totalPrice: (this.electricityFPrice * electricityFDosage).toFixed(2),
                note: ''
              })
            resList.push(
              {
                energyType: 3,
                energyTypeName: '电-平（KWh）',
                amount: electricityPDosage,
                price: this.electricityPPrice,
                totalPrice: (this.electricityPPrice * electricityPDosage).toFixed(2),
                note: ''
              })
            resList.push(
              {
                energyType: 4,
                energyTypeName: '电-谷（KWh）',
                amount: electricityGDosage,
                price: this.electricityGPrice,
                totalPrice: (this.electricityGPrice * electricityGDosage).toFixed(2),
                note: ''
              })
            resList.push(
              {
                energyType: 5,
                energyTypeName: '电-总计（KWh）',
                amount: electricityDosage,
                price: this.electricityPrice,
                totalPrice: (this.electricityPrice * electricityDosage).toFixed(2),
                note: ''
              })
            resList.push(
              {
                energyType: 6,
                energyTypeName: '空气（m³）',
                amount: airDosage,
                price: this.airPrice,
                totalPrice: (this.airPrice * airDosage).toFixed(2),
                note: ''
              })
            resList.push(
              {
                energyType: 7,
                energyTypeName: '蒸汽（m³）',
                amount: steamDosage,
                price: this.steamPrice,
                totalPrice: (this.steamPrice * steamDosage).toFixed(2),
                note: ''
              })
            let sumTotalPrice = 0
            for (const iterator of resList) {
              Number(iterator.totalPrice) && (sumTotalPrice += Number(iterator.totalPrice))
            }
            resList.push(
              {
                energyType: '',
                energyTypeName: '合计',
                amount: '-',
                price: '-',
                totalPrice: sumTotalPrice.toFixed(2),
                note: '-'
              })

            this.transferNoticeList = resList
          }
        }
        else {
          const energyTypeOptions = ['', '水（m³）', '电-峰（KWh）', '电-平（KWh）', '电-谷（KWh）', '电-总计（KWh）', '空气（m³）', '蒸汽（m³）']
          for (const index in rows) {
            let item = rows[index]
            item.energyTypeName = energyTypeOptions[item.energyType]
          }
          let sumTotalPrice = 0
          for (const iterator of rows) {
            Number(iterator.totalPrice) && (sumTotalPrice += Number(iterator.totalPrice))
          }
          rows.push(
            {
              energyType: '',
              energyTypeName: '合计',
              amount: '-',
              price: '-',
              totalPrice: sumTotalPrice.toFixed(2),
              note: '-'
            })
          this.transferNoticeList = rows
        }
        this.total = response.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery () {
      if (this.queryParams.buildingId) {
        const plantName = this.plantOptions.find(item => item.id == this.queryParams.plantId).plantName
        const buildingName = this.buildingOptions.find(item => item.id == this.queryParams.buildingId).buildingName
        this.tableData.inDepartment = plantName + buildingName
        this.getList()
      } else {
        this.$message.warning('请选择建筑')
        return
      }
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm('queryForm')
    },
    /** 导出按钮操作 */
    handleExport (type) {
      if (this.queryParams.buildingId) {
      } else {
        this.$message.warning('请选择建筑')
        return
      }
      const queryParams = { ...this.queryParams }
      this.$confirm('是否确认导出通知单数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function () {
          return exportTransferNotice(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
        .catch(function () { })
    }
  }
}
</script>

<style lang="scss" scoped>
.el-divider--horizontal {
  margin: 12px 0;
}

.first-divider {
  margin: 0px 0 12px 0;
}
.notice-title {
  display: none;
  text-align: center;
}
.table-notice {
  margin-top: 16px;
  .in-department {
    font-size: 16px;
    padding: 0 0 5px 11px;
  }
  .query-time {
    font-size: 16px;
    text-align: right;
    padding: 0 2px 5px 0;
  }
  .modify-user {
    font-size: 16px;
    text-align: right;
    padding: 10px 2px 0px 0;
  }
  .out-department {
    font-size: 16px;
    padding: 10px 0 0 10px;
  }
}
</style>
