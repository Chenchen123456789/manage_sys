<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" ref="queryForm">
      <el-form-item label="测点名" prop="tagName">
        <el-input placeholder="请输入" size="small" v-model="queryParams.tagName"></el-input>
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
      <el-form-item label="能耗建筑划分" prop="buildingId">
        <el-select clearable size="small" v-model="queryParams.buildingId">
          <el-option
            :key="item.id"
            :label="item.buildingName"
            :value="item.id"
            v-for="item in buildingOptions"
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
        <el-button
          @click="handleAdd"
          icon="el-icon-plus"
          size="mini"
          type="primary"
          v-hasPermi="['energy:consumptionGroup:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :disabled="single"
          @click="handleUpdate"
          icon="el-icon-edit"
          size="mini"
          type="success"
          v-hasPermi="['energy:consumptionGroup:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :disabled="multiple"
          @click="handleDelete"
          icon="el-icon-delete"
          size="mini"
          type="danger"
          v-hasPermi="['energy:consumptionGroup:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          @click="handleImport"
          icon="el-icon-upload2"
          size="mini"
          type="info"
          v-hasPermi="['energy:consumptionGroup:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
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
            v-hasPermi="['energy:consumptionGroup:export']"
          >导出</el-button>
        </el-popover>
      </el-col>
    </el-row>

    <el-table
      :data="consumptionGroupList"
      @selection-change="handleSelectionChange"
      size="mini"
      v-loading="loading"
    >
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column :index="indexMethod" align="center" label="序号" type="index" width="60" />
      <el-table-column align="center" label="能耗划分建筑" prop="energyBuildingName" />
      <el-table-column align="center" label="测点名" prop="tagName" />
       <el-table-column align="center" label="测点描述" width="300" prop="tagNameDesc" />
      <el-table-column align="center" label="计量等级" prop="measureLevel" />
      <el-table-column align="center" label="大功率" prop="deviceType">
        <template slot-scope="scope">{{scope.row.deviceType ==1 ? '大功率电器':'未分类'}}</template>
      </el-table-column>
      <el-table-column align="center" label="能源类型" prop="energyTypeName" />
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            @click="handleUpdate(scope.row)"
            icon="el-icon-edit"
            size="mini"
            type="text"
            v-hasPermi="['energy:consumptionGroup:edit']"
          >修改</el-button>
          <el-button
            @click="handleDelete(scope.row)"
            icon="el-icon-delete"
            size="mini"
            type="text"
            v-hasPermi="['energy:consumptionGroup:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
      v-show="total>0"
    />

    <!-- 添加或修改能耗分类对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px">
      <el-form :model="form" :rules="rules" label-width="110px" ref="form">
        <el-form-item label="测点名" prop="tagName">
          <el-select
            :loading="loading1"
            :remote-method="remoteMethod1"
            @change="changeFormTagName"
            collapse-tags
            filterable
            placeholder="请输入关键词"
            remote
            reserve-keyword
            size="small"
            style="width: 250px"
            v-model="form.tagName"
          >
            <el-option
              :key="item.label + index"
              :label="item.label"
              :value="item.value"
              v-for="(item, index) in options1"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="工厂名称" prop="plantId">
          <el-select
            :clearable="false"
            @change="changeQueryPlantOptions"
            placeholder="请选择工厂"
            size="small"
            style="width: 250px"
            v-model="form.plantId"
          >
            <el-option
              :key="item.id"
              :label="item.plantName"
              :value="item.id"
              v-for="item in queryPlantOptions"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="能耗划分建筑" prop="energyBuildingId">
          <el-select
            :clearable="false"
            @change="changeQueryBuildingOptions"
            collapse-tags
            placeholder="请选择建筑"
            size="small"
            style="width: 250px"
            v-model="form.energyBuildingId"
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
          <el-input-number :controls="false" style="width: 250px" v-model="form.measureLevel" />
        </el-form-item>
        <el-form-item label="大功率" prop="deviceType">
          <el-select clearable style="width: 250px" v-model="form.deviceType">
            <el-option
              :key="item.id"
              :label="item.label"
              :value="item.id"
              v-for="item in [{id: 1, label:'大功率电器' },{id: 0, label :'未分类'}]"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div class="dialog-footer" slot="footer">
        <el-button @click="submitForm" type="primary">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <ImportData :importData="{importType:'consumptionGroup', upload}"></ImportData>
  </div>
</template>

<script>
import {
  listConsumptionGroup,
  getConsumptionGroup,
  delConsumptionGroup,
  addConsumptionGroup,
  updateConsumptionGroup,
  exportConsumptionGroup
} from '@/api/energy/consumptionGroup'
import ImportData from '../components/importData'
import { listMeasuringPoint } from '@/api/energy/measuringPoint'
import { listBuilding } from '@/api/energy/building'
import { listEnergyType } from '@/api/energy/report'
import { listPlant } from '@/api/energy/plant'

export default {
  name: 'ConsumptionGroup',
  components: {
    ImportData
  },
  data () {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 能耗分类表格数据
      consumptionGroupList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      upload: {
        title: '',
        open: false
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        tagName: [
          { required: true, message: '测点名不能为空', trigger: 'blur' },
        ],
        energyBuildingId: [
          { required: true, message: '建筑不能为空', trigger: 'blur' },
        ],
      },
      options1: [],
      loading1: false,
      optionList1: [],
      energyTypeOptions: [],
      queryPlantOptions: [],
      queryBuildingOptions: [],
      plantOptions: [],
      buildingOptions: [],
      measuringPointList: []
    }
  },
  created () {
    this.getList()
    this.getMeasuringPointList()
    this.getEnergyTypeOptions()
    this.getPlantOptions()
    this.getBuildingOptions()
  },
  methods: {
    changeFormTagName (value) {
      if (value) {
        const measuringPoint = this.measuringPointList.find(item => item.tagName == value)
        if (measuringPoint) {
          this.form.plantId = measuringPoint.plantId
          this.form.energyBuildingId = measuringPoint.buildingId
        }
      }
    },
    changeQueryPlantOptions (value) {
      this.form.energyBuildingId = undefined
      if (value === '') {
        this.form.plantId = undefined
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
        this.form.plantId = plantId
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
    getEnergyTypeOptions () {
      listEnergyType().then(res => {
        this.energyTypeOptions = res.rows
      })
    },
    getMeasuringPointList () {
      listMeasuringPoint({}).then(res => {
        const list = res.rows.filter(item => item.disable == 0)
        this.measuringPointList = res.rows
        for (const item of list) {
          this.optionList1.push({ value: item.tagName, label: item.tagName })
        }
      })
    },
    remoteMethod1 (query) {
      if (query !== '') {
        this.loading1 = true
        setTimeout(() => {
          this.loading1 = false
          this.options1 = this.optionList1.filter(item => {
            return item.label.toLowerCase()
              .indexOf(query.toLowerCase()) > -1
          })
        }, 200)
      } else {
        this.options1 = []
      }
    },
    indexMethod (index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      )
    },
    /** 查询能耗分类列表 */
    getList () {
      this.loading = true
      listConsumptionGroup(this.queryParams).then(response => {
        this.consumptionGroupList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel () {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset () {
      this.form = {
        id: undefined,
        consumptionGroupCode: undefined,
        consumptionGroupName: undefined,
        consumptionGroupDescription: 0,
        energyBuildingId: undefined,
        plantId: undefined
      }
      this.resetForm('form')
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
    // 多选框选中数据
    handleSelectionChange (selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd () {
      this.reset()
      this.open = true
      this.title = '添加能耗分类'
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.reset()
      const id = row.id || this.ids
      getConsumptionGroup(id).then(response => {
        const { energyBuildingId } = response.data
        this.form = response.data
        this.form.plantId = this.buildingOptions.find(item => item.id == energyBuildingId).plantId
        this.open = true
        this.title = '修改能耗分类'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs['form'].validate(valid => {
        if (valid) {
          const form = { ...this.form }
          form.deviceType = form.deviceType || 0
          if (this.form.id != undefined) {
            updateConsumptionGroup(form).then(response => {
              if (response.code === 200) {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              } else {
                this.msgError(response.msg)
              }
            })
          } else {
            addConsumptionGroup(form).then(response => {
              if (response.code === 200) {
                this.msgSuccess('新增成功')
                this.open = false
                this.getList()
              } else {
                this.msgError(response.msg)
              }
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      const ids = row.id || this.ids
      this.$confirm('是否确认删除,删除后不能恢复?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function () {
          return delConsumptionGroup(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        })
        .catch(function () { })
    },
    /** 导出按钮操作 */
    handleExport (type) {
      const queryParams = { ...this.queryParams }
      if (type === 0) {
        queryParams.pageNum = null
      }
      this.$confirm('是否确认导出能耗分类数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function () {
          return exportConsumptionGroup(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
        .catch(function () { })
    },
    /** 导入按钮操作 */
    handleImport () {
      this.upload = {
        title: '能耗分类导入',
        open: true
      }
    }
  }
}
</script>