<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
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
          size="small"
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
        >
          <el-option
            v-for="item in queryMeterOptions"
            :key="item.id"
            :label="item.meterCode"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['energy:measuringPoint:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['energy:measuringPoint:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['energy:measuringPoint:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['energy:measuringPoint:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="measuringPointList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" :index="indexMethod" width="50" />
      <el-table-column label="公司名称" align="center" prop="companyName" />
      <el-table-column label="工厂名称" align="center" prop="plantName" />
      <el-table-column label="建筑名称" align="center" prop="buildingName" />
      <el-table-column label="设备名称" align="center" prop="deviceName" />
      <el-table-column label="仪表名称" align="center" prop="meterName" />
      <el-table-column label="测点名称" align="center" prop="tagName" />
      <el-table-column label="能源类型" align="center" prop="energyTypeName" />
      <el-table-column label="信号类型" align="center" prop="signalTypeName" />
      <el-table-column label="系统名称" align="center" prop="systemName" />
      <el-table-column label="测点描述" align="center" prop="description" />
      <el-table-column label="总量点" align="center" prop="total_flag" />
      <el-table-column label="主参数" align="center" prop="mainTag" />
      <el-table-column label="状态" align="center" prop="disable" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['energy:measuringPoint:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['energy:measuringPoint:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改测点对话框 -->
    <el-dialog :title="title" @close="handleDialogClose" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="公司" prop="companyId">
          <el-select
            clearable
            @change="changeFormCompanyOptions"
            v-model="form.companyId"
            placeholder="请选择"
          >
            <el-option
              v-for="item in formCompanyOptions"
              :key="item.id"
              :label="item.companyName"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="工厂" prop="plantId">
          <el-select
            clearable
            @change="changeFormPlantOptions"
            v-model="form.plantId"
            placeholder="请选择"
          >
            <el-option
              v-for="item in formPlantOptions"
              :key="item.id"
              :label="item.plantName"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="建筑" prop="buildingId">
          <el-select
            clearable
            @change="changeFormBuildingOptions"
            v-model="form.buildingId"
            placeholder="请选择"
          >
            <el-option
              v-for="item in formBuildingOptions"
              :key="item.id"
              :label="item.buildingName"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="设备" prop="deviceId">
          <el-select
            clearable
            @change="changeFormDeviceOptions"
            v-model="form.deviceId"
            placeholder="请选择"
          >
            <el-option
              v-for="item in formDeviceOptions"
              :key="item.id"
              :label="item.deviceName"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="仪表" prop="meterId">
          <el-select
            clearable
            @change="changeFormMeterOptions"
            v-model="form.meterId"
            placeholder="请选择"
          >
            <el-option
              v-for="item in formMeterOptions"
              :key="item.id"
              :label="item.meterCode"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="测点名称" prop="tagName">
          <el-input v-model="form.tagName" placeholder="请输入测点名称" />
        </el-form-item>
        <el-form-item label="测点厂家" prop="measuringPointCompany">
          <el-input v-model="form.measuringPointCompany" placeholder="请输入测点厂家" />
        </el-form-item>
        <el-form-item label="测点规格" prop="measuringPointSpec">
          <el-input v-model="form.measuringPointSpec" placeholder="请输入测点规格" />
        </el-form-item>
        <el-form-item label="安装地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入安装地点" />
        </el-form-item>
        <el-form-item label="测点参数" prop="measuringPointParam">
          <el-input v-model="form.commParam" placeholder="请输入通讯参数" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listMeasuringPoint,
  getMeasuringPoint,
  delMeasuringPoint,
  addMeasuringPoint,
  updateMeasuringPoint,
  exportMeasuringPoint
} from '@/api/energy/measuringPoint'
import { listCompany } from '@/api/energy/company'
import { listPlant } from '@/api/energy/plant'
import { listBuilding } from '@/api/energy/building'
import { listDevice } from '@/api/energy/device'
import { listMeter } from '@/api/energy/meter'

export default {
  name: 'MeasuringPoint',
  data() {
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
      // 测点表格数据
      measuringPointList: [],
      companyOptions: [],
      plantOptions: [],
      buildingOptions: [],
      deviceOptions: [],
      meterOptions: [],
      // queryCompanyOptions: [],
      queryPlantOptions: [],
      queryBuildingOptions: [],
      queryDeviceOptions: [],
      queryMeterOptions: [],
      formMeterOptions: [],
      formCompanyOptions: [],
      formPlantOptions: [],
      formBuildingOptions: [],
      formDeviceOptions: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        plantId: undefined,
        buildingId: undefined,
        deviceId: undefined,
        meterId: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        companyId: [
          { required: false, message: '公司不能为空', trigger: 'blur' }
        ],
        plantId: [
          { required: false, message: '工厂不能为空', trigger: 'blur' }
        ],
        buildingId: [
          { required: false, message: '建筑不能为空', trigger: 'blur' }
        ],
        deviceId: [
          { required: false, message: '设备不能为空', trigger: 'blur' }
        ],
        meterId: [{ required: true, message: '仪表不能为空', trigger: 'blur' }],
        tagName: [
          { required: true, message: '测点名称不能为空', trigger: 'blur' }
        ],
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
  },
  methods: {
    changeQueryPlantOptions(value) {
      if (value === '') {
        this.queryBuildingOptions = this.buildingOptions
        this.queryDeviceOptions = this.deviceOptions
        this.queryMeterOptions = this.meterOptions
      } else {
        const temp = this.queryParams
        temp.buildingId = temp.deviceId = temp.meterId = null
        const plant = this.plantOptions.find(item => item.id == value)
        this.queryParams = { ...temp }
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
      if (value === '') {
        this.queryDeviceOptions = this.deviceOptions
        this.queryMeterOptions = this.meterOptions
      } else {
        const building = this.buildingOptions.find(item => item.id == value)
        const plantId = building.plantId || null
        const temp = this.queryParams
        temp.plantId = plantId
        temp.deviceId = temp.meterId = null
        this.queryParams = { ...temp }
        this.queryDeviceOptions = this.deviceOptions.filter(
          item => item.buildingId == value
        )
        this.queryMeterOptions = this.meterOptions.filter(
          item => item.buildingId == value
        )
      }
    },
    changeQueryDeviceOptions(value) {
      if (value === '') {
        this.queryMeterOptions = this.meterOptions
      } else {
        const device = this.deviceOptions.find(item => item.id == value)
        const plantId = device.plantId || null
        const buildingId = device.buildingId || null
        const temp = this.queryParams
        temp.plantId = plantId
        temp.buildingId = buildingId
        temp.meterId = null
        this.queryParams = { ...temp }
        this.queryMeterOptions = this.meterOptions.filter(
          item => item.deviceId == value
        )
      }
    },
    changeQueryMeterCodeOptions(value) {
      if (value === '') {
      } else {
        const meter = this.meterOptions.find(item => item.id == value)
        const plantId = meter.plantId || null
        const buildingId = meter.buildingId || null
        const deviceId = meter.deviceId
        const temp = this.queryParams
        temp.plantId = plantId
        temp.buildingId = buildingId
        temp.deviceId = deviceId
        this.queryParams = { ...temp }
      }
    },
    changeFormCompanyOptions(value) {
      if (value === '') {
        this.formPlantOptions = this.plantOptions
        this.formBuildingOptions = this.buildingOptions
        this.formDeviceOptions = this.deviceOptions
        this.formMeterOptions = this.meterOptions
      } else {
        const temp = this.form
        temp.plantId = temp.buildingId = temp.deviceId = temp.meterId = null
        this.form = { ...temp }
        this.formPlantOptions = this.plantOptions.filter(
          item => item.companyId == value
        )
        this.formBuildingOptions = this.buildingOptions.filter(
          item => item.companyId == value
        )
        this.formDeviceOptions = this.deviceOptions.filter(
          item => item.companyId == value
        )
        this.formMeterOptions = this.meterOptions.filter(
          item => item.companyId == value
        )
      }
    },
    changeFormPlantOptions(value) {
      if (value === '') {
        this.formBuildingOptions = this.buildingOptions
        this.formDeviceOptions = this.deviceOptions
        this.formMeterOptions = this.meterOptions
      } else {
        const temp = this.form
        temp.buildingId = temp.deviceId = temp.meterId = null
        this.form = { ...temp }
        const plant = this.plantOptions.find(item => item.id == value)
        const companyId = plant.companyId || null
        this.form.companyId = companyId
        this.formBuildingOptions = this.buildingOptions.filter(
          item => item.plantId == value
        )
        this.formDeviceOptions = this.deviceOptions.filter(
          item => item.plantId == value
        )
        this.formMeterOptions = this.meterOptions.filter(
          item => item.plantId == value
        )
      }
    },
    changeFormBuildingOptions(value) {
      if (value === '') {
        this.formDeviceOptions = this.deviceOptions
        this.formMeterOptions = this.meterOptions
      } else {
        const building = this.buildingOptions.find(item => item.id == value)
        const companyId = building.companyId || null
        const plantId = building.plantId || null
        const temp = this.form
        temp.companyId = companyId
        temp.plantId = plantId
        this.form = { ...temp }
        this.form.deviceId = null
        this.form.meterId = null
        this.formDeviceOptions = this.deviceOptions.filter(
          item => item.buildingId == value
        )
        this.formMeterOptions = this.meterOptions.filter(
          item => item.buildingId == value
        )
      }
    },
    changeFormDeviceOptions(value) {
      if (value === '') {
        this.formMeterOptions = this.meterOptions
      } else {
        const device = this.deviceOptions.find(item => item.id == value)
        const companyId = device.companyId || null
        const plantId = device.plantId || null
        const buildingId = device.buildingId || null
        const temp = this.form
        temp.companyId = companyId
        temp.plantId = plantId
        temp.buildingId = buildingId
        this.form = { ...temp }
        this.form.meterId = null
      }
    },
    changeFormMeterOptions() {
      if (value === '') {
      } else {
        const meter = this.meterOptions.find(item => item.id == value)
        const companyId = meter.companyId || null
        const plantId = meter.plantId || null
        const buildingId = meter.buildingId || null
        const deviceId = meter.deviceId
        const temp = this.form
        temp.companyId = companyId
        temp.plantId = plantId
        temp.buildingId = buildingId
        temp.deviceId = deviceId
        this.form = { ...temp }
      }
    },
    getCompanyOptions() {
      listCompany().then(res => {
        this.companyOptions = this.formCompanyOptions = res.rows
      })
    },
    getMeterCodeOptions() {
      listMeter().then(res => {
        this.queryMeterOptions = this.formMeterOptions = this.meterOptions =
          res.rows
      })
    },
    getPlantOptions() {
      listPlant().then(res => {
        this.plantOptions = this.queryPlantOptions = this.formPlantOptions =
          res.rows
      })
    },
    getBuildingOptions() {
      listBuilding().then(res => {
        this.buildingOptions = this.queryBuildingOptions = this.formBuildingOptions =
          res.rows
      })
    },
    getDeviceOptions() {
      listDevice().then(res => {
        this.deviceOptions = this.queryDeviceOptions = this.formDeviceOptions =
          res.rows
      })
    },
    indexMethod(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      )
    },
    /** 查询测点列表 */
    getList() {
      this.loading = true
      listMeasuringPoint(this.queryParams).then(response => {
        this.measuringPointList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    handleDialogClose() {
      this.formCompanyOptions = this.companyOptions
      this.formPlantOptions = this.plantOptions
      this.formBuildingOptions = this.buildingOptions
      this.formDeviceOptions = this.deviceOptions
      this.formMeterOptions = this.meterOptions
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        tagName: undefined,
        description: undefined
      }
      this.resetForm('form')
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加测点'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id
      getMeasuringPoint(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改测点'
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateMeasuringPoint(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              } else {
                this.msgError(response.msg)
              }
            })
          } else {
            addMeasuringPoint(this.form).then(response => {
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
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$confirm('是否确认删除,删除后不能恢复?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function() {
          return delMeasuringPoint(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        })
        .catch(function() {})
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm('是否确认导出所有测点数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function() {
          return exportMeasuringPoint(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
        .catch(function() {})
    }
  }
}
</script>