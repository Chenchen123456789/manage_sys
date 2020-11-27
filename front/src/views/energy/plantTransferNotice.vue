<template>
  <div class="app-container">
    <el-form
      :inline="true"
      :model="queryParams"
      label-width="68px"
      ref="queryForm"
    >
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
      <el-form-item label="计量等级" prop="measureLevel">
        <el-select
          clearable
          size="small"
          style="width: 150px"
          v-model="queryParams.measureLevel"
        >
          <el-option
            :key="item.id"
            :label="item.label"
            :value="item.id"
            v-for="item in [
              { id: 1, label: '一级' },
              { id: 2, label: '二级' },
              { id: 3, label: '三级' },
            ]"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          @click="handleQuery"
          icon="el-icon-search"
          size="mini"
          type="primary"
          >搜索</el-button
        >
        <el-button
          @click="handleQueryHistory"
          icon="el-icon-search"
          size="mini"
          type="success"
          >历史搜索</el-button
        >
        <el-button
          @click="resetQuery"
          icon="el-icon-refresh"
          size="mini"
          type="info"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          @click="handleEdit"
          icon="el-icon-edit"
          size="mini"
          type="primary"
          >编辑</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          @click="handleCancelEdit"
          icon="el-icon-refresh-left"
          size="mini"
          type="danger"
          >取消编辑</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          @click="handleSave"
          icon="el-icon-check"
          size="mini"
          type="success"
          v-hasPermi="['energy:plantTransferNotice:add']"
          >保存</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          @click="downloadExcel"
          icon="el-icon-download"
          size="mini"
          type="warning"
          v-hasPermi="['energy:plantTransferNotice:export']"
          >导出</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          class="print-button"
          icon="el-icon-printer"
          size="mini"
          v-print="printObj"
          >打印</el-button
        >
      </el-col>
    </el-row>

    <el-divider class="first-divider"></el-divider>
    <el-row
      :gutter="10"
      style="
        display: flex;
        align-items: center;
        justify-content: stretch;
        margin-left: 0px;
      "
    >
      <el-col :span="3">
        <span>水单价：{{ waterPrice }} 元/m³</span>
      </el-col>
      <el-col :span="3">
        <span>电-峰单价：{{ electricityFPrice }} 元/kWh</span>
      </el-col>
      <el-col :span="3">
        <span>电-平单价：{{ electricityPPrice }} 元/kWh</span>
      </el-col>
      <el-col :span="3">
        <span>电-谷单价：{{ electricityGPrice }} 元/kWh</span>
      </el-col>
      <el-col :span="3">
        <span>电-综合单价：{{ electricityPrice }} 元/kWh</span>
      </el-col>
      <el-col :span="3">
        <span>空气单价：{{ airPrice }} 元/m³</span>
      </el-col>
      <el-col :span="3">
        <span>蒸汽单价：{{ steamPrice }} 元/m³</span>
      </el-col>
      <el-col :span="1.5">
        <el-button
          @click="handleUpdate"
          icon="el-icon-edit"
          size="mini"
          type="success"
          >修改</el-button
        >
      </el-col>
    </el-row>

    <el-dialog :visible.sync="open" title="修改" width="500px">
      <el-form :model="form" label-width="90px" ref="form">
        <el-form-item label="水单价" prop="waterPrice">
          <el-input-number
            placeholder="请输入水单价"
            v-model="form.waterPrice"
          />
        </el-form-item>
        <el-form-item label="电-峰单价" prop="electricityFPrice">
          <el-input-number
            placeholder="请输入电-峰单价"
            v-model="form.electricityFPrice"
          />
        </el-form-item>
        <el-form-item label="电-平单价" prop="electricityPPrice">
          <el-input-number
            placeholder="请输入电-平单价"
            v-model="form.electricityPPrice"
          />
        </el-form-item>
        <el-form-item label="电-谷单价" prop="electricityGPrice">
          <el-input-number
            placeholder="请输入电-谷单价"
            v-model="form.electricityGPrice"
          />
        </el-form-item>
        <el-form-item label="电-综合单价" prop="electricityPrice">
          <el-input-number
            placeholder="请输入电-总计单价"
            v-model="form.electricityPrice"
          />
        </el-form-item>
        <el-form-item label="空气单价" prop="airPrice">
          <el-input-number
            placeholder="请输入空气单价"
            v-model="form.airPrice"
          />
        </el-form-item>
        <el-form-item label="蒸汽单价" prop="steamPrice">
          <el-input-number
            placeholder="请输入蒸汽单价"
            v-model="form.steamPrice"
          />
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
      <tr class="block-table">
        <td>
          <el-col :span="16" class="in-department"
            >转入单位：{{ tableData.inDepartment }}</el-col
          >
        </td>
        <td v-for="item in 20" :key="item"></td>
        <td>
          <el-col :span="8" class="query-time">{{
            formatTime(queryParams.queryTime)
          }}</el-col>
        </td>
      </tr>
      <el-table
        :data="plantTransferNoticeList"
        size="medium"
        style="width: 100%"
        v-loading="loading"
        border
      >
        <el-table-column
          align="center"
          label="建筑名"
          width="120"
          prop="buildingName"
        />
        <el-table-column align="center" label="水">
          <el-table-column align="center" label="数量" prop="waterAmount">
            <template slot-scope="scope">
              <span v-show="!showEdit">{{ scope.row.waterAmount }}</span>
              <el-input-number
                :controls="false"
                :precision="2"
                placeholder="请输入"
                v-if="scope.row.waterAmount != '-'"
                v-model="scope.row.waterAmount"
                v-show="showEdit"
              ></el-input-number>
            </template>
          </el-table-column>

          <el-table-column align="center" label="单价（元）" prop="waterPrice">
            <template slot-scope="scope">
              <span v-show="!showEdit">{{ scope.row.waterPrice }}</span>
              <el-input-number
                :controls="false"
                :precision="2"
                placeholder="请输入"
                v-if="scope.row.waterPrice != '-'"
                v-model="scope.row.waterPrice"
                v-show="showEdit"
              ></el-input-number>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="金额（元）"
            width="120"
            prop="waterTotalPrice"
          >
            <template slot-scope="scope">
              <span v-show="!showEdit">{{ scope.row.waterTotalPrice }}</span>
              <span v-if="scope.row.waterPrice != '-' && showEdit">{{
                (scope.row.waterAmount * scope.row.waterPrice).toFixed(2)
              }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column align="center" label="电-总计">
          <el-table-column align="center" label="数量" prop="electricityAmount">
            <template slot-scope="scope">
              <span v-show="!showEdit">{{ scope.row.electricityAmount }}</span>
              <el-input-number
                :controls="false"
                :precision="2"
                placeholder="请输入"
                v-if="scope.row.electricityAmount != '-'"
                v-model="scope.row.electricityAmount"
                v-show="showEdit"
              ></el-input-number>
            </template>
          </el-table-column>

          <el-table-column align="center" label="单价（元）">
            <template slot-scope="scope">
              <span v-show="!showEdit">{{ scope.row.electricityPrice }}</span>
              <el-input-number
                :controls="false"
                :precision="2"
                placeholder="请输入"
                v-if="scope.row.electricityPrice != '-'"
                v-model="scope.row.electricityPrice"
                v-show="showEdit"
              ></el-input-number>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="金额（元）"
            width="120"
            prop="electricityTotalPrice"
          >
            <template slot-scope="scope">
              <span v-show="!showEdit">{{
                scope.row.electricityTotalPrice
              }}</span>
              <span v-if="scope.row.electricityPrice != '-' && showEdit">{{
                (
                  scope.row.electricityAmount * scope.row.electricityPrice
                ).toFixed(2)
              }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column align="center" label="电-峰">
          <el-table-column
            align="center"
            label="数量"
            prop="electricityFAmount"
          >
            <template slot-scope="scope">
              <span v-show="!showEdit">{{ scope.row.electricityFAmount }}</span>
              <el-input-number
                :controls="false"
                :precision="2"
                placeholder="请输入"
                v-if="scope.row.electricityFAmount != '-'"
                v-model="scope.row.electricityFAmount"
                v-show="showEdit"
              ></el-input-number>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="单价（元）"
            prop="electricityFPrice"
          >
            <template slot-scope="scope">
              <span v-show="!showEdit">{{ scope.row.electricityFPrice }}</span>
              <el-input-number
                :controls="false"
                :precision="2"
                placeholder="请输入"
                v-if="scope.row.electricityFPrice != '-'"
                v-model="scope.row.electricityFPrice"
                v-show="showEdit"
              ></el-input-number>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="金额（元）"
            width="120"
            prop="electricityFTotalPrice"
          >
            <template slot-scope="scope">
              <span v-show="!showEdit">{{
                scope.row.electricityFTotalPrice
              }}</span>
              <span v-if="scope.row.electricityFPrice != '-' && showEdit">{{
                (
                  scope.row.electricityFAmount * scope.row.electricityFPrice
                ).toFixed(2)
              }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column align="center" label="电-平">
          <el-table-column
            align="center"
            label="数量"
            prop="electricityPAmount"
          >
            <template slot-scope="scope">
              <span v-show="!showEdit">{{ scope.row.electricityPAmount }}</span>
              <el-input-number
                :controls="false"
                :precision="2"
                placeholder="请输入"
                v-if="scope.row.electricityPAmount != '-'"
                v-model="scope.row.electricityPAmount"
                v-show="showEdit"
              ></el-input-number>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="单价（元）"
            prop="electricityPPrice"
          >
            <template slot-scope="scope">
              <span v-show="!showEdit">{{ scope.row.electricityPPrice }}</span>
              <el-input-number
                :controls="false"
                :precision="2"
                placeholder="请输入"
                v-if="scope.row.electricityPPrice != '-'"
                v-model="scope.row.electricityPPrice"
                v-show="showEdit"
              ></el-input-number>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="金额（元）"
            width="120"
            prop="electricityPTotalPrice"
          >
            <template slot-scope="scope">
              <span v-show="!showEdit">{{
                scope.row.electricityPTotalPrice
              }}</span>
              <span v-if="scope.row.electricityPPrice != '-' && showEdit">{{
                (
                  scope.row.electricityPAmount * scope.row.electricityPPrice
                ).toFixed(2)
              }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column align="center" label="电-谷">
          <el-table-column
            align="center"
            label="数量"
            prop="electricityGAmount"
          >
            <template slot-scope="scope">
              <span v-show="!showEdit">{{ scope.row.electricityGAmount }}</span>
              <el-input-number
                :controls="false"
                :precision="2"
                placeholder="请输入"
                v-if="scope.row.electricityGAmount != '-'"
                v-model="scope.row.electricityGAmount"
                v-show="showEdit"
              ></el-input-number>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="单价（元）"
            prop="electricityGPrice"
          >
            <template slot-scope="scope">
              <span v-show="!showEdit">{{ scope.row.electricityGPrice }}</span>
              <el-input-number
                :controls="false"
                :precision="2"
                placeholder="请输入"
                v-if="scope.row.electricityGPrice != '-'"
                v-model="scope.row.electricityGPrice"
                v-show="showEdit"
              ></el-input-number>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="金额（元）"
            width="120"
            prop="electricityGTotalPrice"
          >
            <template slot-scope="scope">
              <span v-show="!showEdit">{{
                scope.row.electricityGTotalPrice
              }}</span>
              <span v-if="scope.row.electricityGPrice != '-' && showEdit">{{
                (
                  scope.row.electricityGAmount * scope.row.electricityGPrice
                ).toFixed(2)
              }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column align="center" label="蒸汽">
          <el-table-column align="center" label="数量" prop="steamAmount">
            <template slot-scope="scope">
              <span v-show="!showEdit">{{ scope.row.steamAmount }}</span>
              <el-input-number
                :controls="false"
                :precision="2"
                placeholder="请输入"
                v-if="scope.row.steamAmount != '-'"
                v-model="scope.row.steamAmount"
                v-show="showEdit"
              ></el-input-number>
            </template>
          </el-table-column>
          <el-table-column align="center" label="单价（元）" prop="steamPrice">
            <template slot-scope="scope">
              <span v-show="!showEdit">{{ scope.row.steamPrice }}</span>
              <el-input-number
                :controls="false"
                :precision="2"
                placeholder="请输入"
                v-if="scope.row.steamPrice != '-'"
                v-model="scope.row.steamPrice"
                v-show="showEdit"
              ></el-input-number>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="金额（元）"
            width="120"
            prop="steamTotalPrice"
          >
            <template slot-scope="scope">
              <span v-show="!showEdit">{{ scope.row.steamTotalPrice }}</span>
              <span v-if="scope.row.steamPrice != '-' && showEdit">{{
                (scope.row.steamAmount * scope.row.steamPrice).toFixed(2)
              }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column align="center" label="空气">
          <el-table-column align="center" label="数量" prop="airAmount">
            <template slot-scope="scope">
              <span v-show="!showEdit">{{ scope.row.airAmount }}</span>
              <el-input-number
                :controls="false"
                :precision="2"
                placeholder="请输入"
                v-if="scope.row.airAmount != '-'"
                v-model="scope.row.airAmount"
                v-show="showEdit"
              ></el-input-number>
            </template>
          </el-table-column>
          <el-table-column align="center" label="单价（元）" prop="airPrice">
            <template slot-scope="scope">
              <span v-show="!showEdit">{{ scope.row.airPrice }}</span>
              <el-input-number
                :controls="false"
                :precision="2"
                placeholder="请输入"
                v-if="scope.row.airPrice != '-'"
                v-model="scope.row.airPrice"
                v-show="showEdit"
              ></el-input-number>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="金额（元）"
            prop="airTotalPrice"
            width="120"
          >
            <template slot-scope="scope">
              <span v-show="!showEdit">{{ scope.row.airTotalPrice }}</span>
              <span v-if="scope.row.airPrice != '-' && showEdit">{{
                (scope.row.airAmount * scope.row.airPrice).toFixed(2)
              }}</span>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table>
      <div class="notice-footer">
        <tr class="block-table">
          <td>
            <el-col :span="10" class="out-department">
              转出单位：
              <span v-show="!showEdit">{{ tableData.outDepartment }}</span>
              <el-input
                placeholder="请输入"
                style="width: 250px"
                v-model="tableData.outDepartment"
                v-show="showEdit"
              ></el-input>
            </el-col>
          </td>
          <td v-for="item in 20" :key="item"></td>
          <td>
            <el-col :span="14" class="modify-user">
              制表人：
              <span v-show="!showEdit">{{ tableData.modifyUser }}</span>
              <el-input
                placeholder="请输入"
                style="width: 250px"
                v-model="tableData.modifyUser"
                v-show="showEdit"
              ></el-input>
            </el-col>
          </td>
        </tr>
      </div>
    </el-row>
  </div>
</template>

<script>
import {
  listPlantTransferNotice,
  exportPlantTransferNotice,
  addPlantTransferNotice,
  listPlantTransferNoticeHistory,
} from '@/api/energy/transferNotice'
import { listPlant } from '@/api/energy/plant'
import { listBuilding } from '@/api/energy/building'
import { listUnitPrice, updateUnitPrice } from '@/api/energy/unitPrice'
import XLSX from 'xlsx'
import XLSXS from 'yxg-xlsx-style'
import XSU from '@/utils/xsu'
import { saveAs } from 'file-saver'

export default {
  name: 'PlantTransferNotice',
  data() {
    return {
      printObj: {
        id: '#printArea',
        extraHead: '<meta http-equiv="Content-Language"content="zh-cn"/>',
      },
      // 遮罩层
      loading: false,
      total: 0,
      plantTransferNoticeList: [],
      queryPlantOptions: [],
      queryBuildingOptions: [],
      plantOptions: [],
      buildingOptions: [],
      queryParams: {
        queryTime: new Date(),
        plantId: undefined,
      },
      tableData: {
        outDepartment: '动力站',
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
    }
  },
  created() {
    this.getUnitPriceList()
    this.getPlantOptions()
    this.tableData.modifyUser = this.$store.state.user.name
  },
  methods: {
    downloadExcel() {
      var elt = document.getElementById('printArea')
      //workbook例子
      var wb = XLSX.utils.table_to_book(elt, { sheet: 'sheet1' })
      var sheetName = wb.SheetNames[0]

      XSU.setColWidth(wb, sheetName, [{ wch: 15 }])
      XSU.setFontVertAlignAll(wb, sheetName, true)
      XSU.setAlignmentVerticalAll(wb, sheetName, 'center')
      XSU.setAlignmentHorizontalAll(wb, sheetName, 'center') //垂直居中
      XSU.setBorderDefaultAll(wb, sheetName)
      XSU.setTitleStylesDefault(wb, sheetName)
      XSU.setEvenRowColorGrey(wb, sheetName)

      const wopts = { bookType: 'xlsx', bookSST: false, type: 'binary' }
      const wbout = XLSXS.write(wb, wopts)
      //保存，使用FileSaver.js
      return saveAs(
        new Blob([XSU.s2ab(wbout)], { type: '' }),
        '费用转账通知单汇总.xlsx'
      )
    },
    handleCancelEdit() {
      this.showEdit = false
      this.handleQuery()
    },
    handleEdit() {
      const obj = [...this.plantTransferNoticeList]
      this.plantTransferNoticeList = obj.splice(
        0,
        this.plantTransferNoticeList.length - 1
      )
      this.showEdit = true
    },
    getUnitPriceList() {
      listUnitPrice().then((res) => {
        this.unitPriceList = res.data
        this.waterPrice =
          res.data.find((item) => item.energyType == '水').price || 0
        this.electricityPrice =
          res.data.find((item) => item.energyType == '电').price || 0

        this.electricityFPrice =
          res.data.find((item) => item.energyType == '电-峰').price || 0
        this.electricityPPrice =
          res.data.find((item) => item.energyType == '电-平').price || 0
        this.electricityGPrice =
          res.data.find((item) => item.energyType == '电-谷').price || 0

        this.airPrice =
          res.data.find((item) => item.energyType == '空气').price || 0
        this.steamPrice =
          res.data.find((item) => item.energyType == '蒸汽').price || 0
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          updateUnitPrice(this.form).then((response) => {
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
    handleUpdate() {
      this.open = true
      this.form.waterPrice = this.waterPrice
      this.form.electricityPrice = this.electricityPrice

      this.form.electricityFPrice = this.electricityFPrice
      this.form.electricityPPrice = this.electricityPPrice
      this.form.electricityGPrice = this.electricityGPrice

      this.form.airPrice = this.airPrice
      this.form.steamPrice = this.steamPrice
    },
    handleSave() {
      if (!this.showEdit) {
        return
      }
      const tableData = { ...this.tableData }
      const plantTransferNoticeList = [...this.plantTransferNoticeList].splice(
        0,
        this.plantTransferNoticeList.length - 1
      )
      for (let i = 0; i < plantTransferNoticeList.length; i++) {
        const item = plantTransferNoticeList[i]
        item.plantId = this.queryParams.plantId
        item.dataTime = this.queryParams.queryTime
        item.outDepartment = tableData.outDepartment
        item.inDepartment = tableData.inDepartment
        item.modifyUser = tableData.modifyUser
        // item.createTime = this.parseTime(new Date())
        item.createBy = this.$store.state.user.name
      }
      addPlantTransferNotice(plantTransferNoticeList).then((response) => {
        if (response.code === 200) {
          this.msgSuccess('修改成功')
          this.open = false
          // this.handleQuery()
        } else {
          this.msgError(response.msg)
        }
        this.showEdit = false
      })
    },
    formatTime(time) {
      if (time) {
        const year = time.getFullYear()
        const month = time.getMonth() + 1
        return year + '年' + month + '月'
      }
    },
    getPlantOptions() {
      listPlant().then((res) => {
        this.queryPlantOptions = this.plantOptions = res.rows
      })
    },
    /** 查询列表 */
    getList() {
      this.loading = true
      listPlantTransferNotice(this.queryParams).then((response) => {
        console.log(1)
        const rows = response.rows
        const FPGList = rows[0].FPGList
        const settlementList = rows[0].settlementList
        let sumAirTotalPrice = 0,
          sumWaterTotalPrice = 0,
          sumSteamTotalPrice = 0,
          sumElectricityTotalPrice = 0,
          sumElectricityFTotalPrice = 0,
          sumElectricityPTotalPrice = 0,
          sumElectricityGTotalPrice = 0

        if (FPGList || settlementList) {
          if (FPGList.length > 0 || settlementList.length > 0) {
            for (let index in settlementList) {
              const settlement = settlementList[index]
              let waterAmount = 0,
                electricityAmount = 0,
                airAmount = 0,
                steamAmount = 0,
                electricityFAmount = 0,
                electricityPAmount = 0,
                electricityGAmount = 0
              waterAmount = (
                settlement.currentWaterSumValue - settlement.preWaterSumValue
              ).toFixed(2)
              electricityAmount = (
                settlement.currentElectricitySumValue -
                settlement.preElectricitySumValue
              ).toFixed(2)
              steamAmount = (
                settlement.currentSteamSumValue - settlement.preSteamSumValue
              ).toFixed(2)
              airAmount = (
                settlement.currentAirSumValue - settlement.preAirSumValue
              ).toFixed(2)
              const FPGItem = FPGList.find(
                (item) => item.buildingId == settlement.buildingId
              )
              if (FPGItem) {
                electricityFAmount = FPGItem.curSumFValue.toFixed(2)
                electricityPAmount = FPGItem.curSumPValue.toFixed(2)
                electricityGAmount = FPGItem.curSumGValue.toFixed(2)
              }
              settlement.electricityAmount = electricityAmount

              settlement.waterPrice = this.waterPrice
              settlement.electricityPrice = this.electricityPrice
              settlement.electricityFPrice = this.electricityFPrice
              settlement.electricityPPrice = this.electricityPPrice
              settlement.electricityGPrice = this.electricityGPrice
              settlement.airPrice = this.airPrice
              settlement.steamPrice = this.steamPrice

              settlement.waterAmount = waterAmount
              settlement.airAmount = airAmount
              settlement.steamAmount = steamAmount
              settlement.electricityAmount = electricityAmount
              settlement.electricityFAmount = electricityFAmount
              settlement.electricityPAmount = electricityPAmount
              settlement.electricityGAmount = electricityGAmount

              settlement.waterTotalPrice = (
                waterAmount * this.waterPrice
              ).toFixed(2)
              settlement.airTotalPrice = (airAmount * this.airPrice).toFixed(2)
              settlement.steamTotalPrice = (
                steamAmount * this.steamPrice
              ).toFixed(2)
              settlement.electricityTotalPrice = (
                electricityAmount * this.electricityPrice
              ).toFixed(2)
              settlement.electricityFTotalPrice = (
                electricityFAmount * this.electricityFPrice
              ).toFixed(2)
              settlement.electricityPTotalPrice = (
                electricityPAmount * this.electricityPPrice
              ).toFixed(2)
              settlement.electricityGTotalPrice = (
                electricityGAmount * this.electricityGPrice
              ).toFixed(2)

              sumAirTotalPrice += Number(settlement.airTotalPrice)
              sumWaterTotalPrice += Number(settlement.waterTotalPrice)
              sumSteamTotalPrice += Number(settlement.steamTotalPrice)
              sumElectricityTotalPrice += Number(
                settlement.electricityTotalPrice
              )
              sumElectricityFTotalPrice += Number(
                settlement.electricityFTotalPrice
              )
              sumElectricityPTotalPrice += Number(
                settlement.electricityPTotalPrice
              )
              sumElectricityGTotalPrice += Number(
                settlement.electricityGTotalPrice
              )
            }

            const sum = {
              buildingName: '合计',
              airTotalPrice: sumAirTotalPrice,
              waterTotalPrice: sumWaterTotalPrice,
              steamTotalPrice: sumSteamTotalPrice,
              electricityTotalPrice: sumElectricityTotalPrice,
              electricityFTotalPrice: sumElectricityFTotalPrice.toFixed(2),
              electricityPTotalPrice: sumElectricityPTotalPrice.toFixed(2),
              electricityGTotalPrice: sumElectricityGTotalPrice.toFixed(2),
            }
            settlementList.push(sum)

            this.plantTransferNoticeList = settlementList
          }
        } else {
          let sumAirTotalPrice = 0,
            sumWaterTotalPrice = 0,
            sumSteamTotalPrice = 0,
            sumElectricityTotalPrice = 0,
            sumElectricityFTotalPrice = 0,
            sumElectricityPTotalPrice = 0,
            sumElectricityGTotalPrice = 0

          for (const index in rows) {
            const item = rows[index]
            item.waterTotalPrice = (item.waterPrice * item.waterAmount).toFixed(
              2
            )
            item.airTotalPrice = (item.airPrice * item.airAmount).toFixed(2)
            item.steamTotalPrice = (item.steamPrice * item.steamAmount).toFixed(
              2
            )
            item.electricityTotalPrice = (
              item.electricityPrice * item.electricityAmount
            ).toFixed(2)
            item.electricityFTotalPrice = (
              item.electricityFPrice * item.electricityFAmount
            ).toFixed(2)
            item.electricityPTotalPrice = (
              item.electricityPPrice * item.electricityPAmount
            ).toFixed(2)
            item.electricityGTotalPrice = (
              item.electricityGPrice * item.electricityGAmount
            ).toFixed(2)

            sumAirTotalPrice += Number(item.airTotalPrice)
            sumWaterTotalPrice += Number(item.waterTotalPrice)
            sumSteamTotalPrice += Number(item.steamTotalPrice)
            sumElectricityTotalPrice += Number(item.electricityTotalPrice)
            sumElectricityFTotalPrice += Number(item.electricityFTotalPrice)
            sumElectricityPTotalPrice += Number(item.electricityPTotalPrice)
            sumElectricityGTotalPrice += Number(item.electricityGTotalPrice)
          }
          const sum = {
            buildingName: '合计',
            airTotalPrice: sumAirTotalPrice,
            waterTotalPrice: sumWaterTotalPrice,
            steamTotalPrice: sumSteamTotalPrice,
            electricityTotalPrice: sumElectricityTotalPrice,
            electricityFTotalPrice: sumElectricityFTotalPrice,
            electricityPTotalPrice: sumElectricityPTotalPrice,
            electricityGTotalPrice: sumElectricityGTotalPrice,
          }
          rows.push(sum)
          this.plantTransferNoticeList = rows
        }
        this.total = response.total
        this.loading = false
        console.log(2)
      })
    },
    getHistoryList() {
      this.loading = true
      listPlantTransferNoticeHistory(this.queryParams).then((response) => {
        const rows = response.rows
        if (rows.length > 0) {
          let sumAirTotalPrice = 0,
            sumWaterTotalPrice = 0,
            sumSteamTotalPrice = 0,
            sumElectricityTotalPrice = 0,
            sumElectricityFTotalPrice = 0,
            sumElectricityPTotalPrice = 0,
            sumElectricityGTotalPrice = 0

          for (const index in rows) {
            const item = rows[index]
            item.waterTotalPrice = (item.waterPrice * item.waterAmount).toFixed(
              2
            )
            item.airTotalPrice = (item.airPrice * item.airAmount).toFixed(2)
            item.steamTotalPrice = (item.steamPrice * item.steamAmount).toFixed(
              2
            )
            item.electricityTotalPrice = (
              item.electricityPrice * item.electricityAmount
            ).toFixed(2)
            item.electricityFTotalPrice = (
              item.electricityFPrice * item.electricityFAmount
            ).toFixed(2)
            item.electricityPTotalPrice = (
              item.electricityPPrice * item.electricityPAmount
            ).toFixed(2)
            item.electricityGTotalPrice = (
              item.electricityGPrice * item.electricityGAmount
            ).toFixed(2)

            sumAirTotalPrice += Number(item.airTotalPrice)
            sumWaterTotalPrice += Number(item.waterTotalPrice)
            sumSteamTotalPrice += Number(item.steamTotalPrice)
            sumElectricityTotalPrice += Number(item.electricityTotalPrice)
            sumElectricityFTotalPrice += Number(item.electricityFTotalPrice)
            sumElectricityPTotalPrice += Number(item.electricityPTotalPrice)
            sumElectricityGTotalPrice += Number(item.electricityGTotalPrice)
          }
          const sum = {
            buildingName: '合计',
            airTotalPrice: sumAirTotalPrice,
            waterTotalPrice: sumWaterTotalPrice,
            steamTotalPrice: sumSteamTotalPrice,
            electricityTotalPrice: sumElectricityTotalPrice,
            electricityFTotalPrice: sumElectricityFTotalPrice,
            electricityPTotalPrice: sumElectricityPTotalPrice,
            electricityGTotalPrice: sumElectricityGTotalPrice,
          }
          rows.push(sum)
        }
        this.plantTransferNoticeList = rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      if (this.queryParams.plantId) {
        const plantName = this.plantOptions.find(
          (item) => item.id == this.queryParams.plantId
        ).plantName
        this.tableData.inDepartment = plantName
        this.getList()
      } else {
        this.$message.warning('请选择工厂')
        return
      }
    },
    /** 搜索按钮操作 */
    handleQueryHistory() {
      if (this.queryParams.plantId) {
        const plantName = this.plantOptions.find(
          (item) => item.id == this.queryParams.plantId
        ).plantName
        this.tableData.inDepartment = plantName
        this.getHistoryList()
      } else {
        this.$message.warning('请选择工厂')
        return
      }
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
    },
    /** 导出按钮操作 */
    handleExport(type) {
      if (this.queryParams.plantId) {
      } else {
        this.$message.warning('请选择工厂')
        return
      }
      const queryParams = { ...this.queryParams }
      this.$confirm('是否确认导出通知单数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(function () {
          return exportPlantTransferNotice(queryParams)
        })
        .then((response) => {
          this.download(response.msg)
        })
        .catch(function () {})
    },
  },
}
</script>

<style lang="scss" scoped>
/deep/.cell {
  display: flex;
  text-align: center;
  justify-content: center;
}

/deep/.el-table {
  overflow: auto;
}
// 我的并集选择器不知道为什么没成功只能一个一个设置了
// 取消table头部滚动隐藏
/deep/ .el-table .el-table__header-wrapper {
  overflow: visible;
}
// 取消tablebody部滚动隐藏
/deep/ .el-table .el-table__body-wrapper {
  overflow: visible;
}
// 取消table底部滚动隐藏
/deep/ .el-table .el-table__footer-wrapper {
  overflow: visible;
}
// 合计好像会多条线进行隐藏
/deep/.el-table::after {
  position: relative !important;
}

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
.block-table {
  display: block;
  td {
    display: block;
  }
}
</style>
