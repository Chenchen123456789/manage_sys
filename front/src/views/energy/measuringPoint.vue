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
        <el-button type="info" icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
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
          type="info"
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['energy:measuringPoint:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
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
            v-hasPermi="['energy:measuringPoint:export']"
            slot="reference"
          >导出</el-button>
        </el-popover>
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
      <el-table-column label="数据分类" align="center" prop="signalTypeName" />
      <el-table-column label="系统名称" align="center" prop="systemName" />
      <el-table-column label="测点描述" align="center" prop="description" />
      <el-table-column label="总量点" align="center" prop="totalFlag" />
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
    <el-dialog :title="title" @close="handleDialogClose" :visible.sync="open" width="800px">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item style="width:318px" label="测点名称" prop="tagName">
              <el-input v-model="form.tagName" placeholder="请输入测点名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="disable">
              <el-select clearable v-model="form.disable" placeholder="请选择">
                <el-option :key="0" label="可用" :value="0"></el-option>
                <el-option :key="1" label="不可用" :value="1"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="22">
            <el-form-item label="描述" prop="description">
              <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
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
          </el-col>
          <el-col :span="12">
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
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
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
          </el-col>
          <el-col :span="12">
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
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
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
          </el-col>
          <el-col :span="12">
            <el-form-item label="系统" prop="systemId">
              <el-select clearable v-model="form.systemId" placeholder="请选择">
                <el-option
                  v-for="item in formSystemOptions"
                  :key="item.systemId"
                  :label="item.systemName"
                  :value="item.systemId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="能源类型" prop="energyTypeId">
              <el-select clearable v-model="form.energyTypeId" placeholder="请选择">
                <el-option
                  v-for="item in formEnergyTypeOptions"
                  :key="item.energyTypeId"
                  :label="item.energyTypeName"
                  :value="item.energyTypeId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据分类" prop="signalTypeId">
              <el-select clearable v-model="form.signalTypeId" placeholder="请选择">
                <el-option
                  v-for="item in formSignalTypeOptions"
                  :key="item.signalTypeId"
                  :label="item.signalTypeName"
                  :value="item.signalTypeId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="总量点" prop="totalFlag">
              <el-select clearable v-model="form.totalFlag" placeholder="请选择">
                <el-option :key="0" label="是" :value="0"></el-option>
                <el-option :key="1" label="否" :value="1"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主参数" prop="mainTag">
              <el-select clearable v-model="form.mainTag" placeholder="请选择">
                <el-option :key="0" label="是" :value="0"></el-option>
                <el-option :key="1" label="否" :value="1"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="报警类型" prop="alarmTag">
              <el-select clearable v-model="form.alarmTag" placeholder="请选择">
                <el-option :key="0" label="不报警" :value="0"></el-option>
                <el-option :key="1" label="模拟量" :value="1"></el-option>
                <el-option :key="2" label="开关量" :value="2"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报警等级" prop="alarmLevel">
              <el-select clearable v-model="form.alarmLevel" placeholder="请选择">
                <el-option :key="0" label="普通" :value="1"></el-option>
                <el-option :key="1" label="一般" :value="2"></el-option>
                <el-option :key="2" label="紧急" :value="3"></el-option>
                <el-option :key="3" label="特急" :value="4"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item v-if="form.alarmTag == 1" label="报警LL值" prop="alarmLLValue">
              <el-input-number :precision="4" v-model="form.alarmLLValue" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.alarmTag == 1" label="报警L值" prop="alarmLValue">
              <el-input-number :precision="4" v-model="form.alarmLValue" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item v-if="form.alarmTag == 1" label="报警H值" prop="alarmHValue">
              <el-input-number :precision="4" v-model="form.alarmHValue" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.alarmTag == 1" label="报警HH值" prop="alarmHHValue">
              <el-input-number :precision="4" v-model="form.alarmHHValue" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item v-if="form.alarmTag == 2" label="报警方式" prop="alarmDigitalValue">
              <el-select clearable v-model="form.alarmDigitalValue" placeholder="请选择">
                <el-option :key="0" label="0报警" :value="0"></el-option>
                <el-option :key="1" label="1报警" :value="1"></el-option>
                <el-option :key="2" label="0、1都报警" :value="2"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
           <el-col :span="12">
            <el-form-item
              v-if="form.alarmTag == 2"
              label="为0报警说明"
              prop="alarmDigitalZeroDescription"
              style="width:318px"
            >
              <el-input v-model="form.alarmDigitalZeroDescription" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              v-if="form.alarmTag == 2"
              label="为1报警说明"
              prop="alarmDigitalOneDescription"
              style="width:318px"
            >
              <el-input v-model="form.alarmDigitalOneDescription" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <ImportData :importData="{importType:'measuringPoint', upload}"></ImportData>
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
import ImportData from '../components/importData'

import {
  listClass,
  listEnergyType,
  listSignalType,
  listSystem
} from '@/api/energy/report'

export default {
  name: 'MeasuringPoint',
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
      upload: {
        title: '',
        open: false
      },
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
      formClassOptions: [],
      formSignalTypeOptions: [],
      formSystemOptions: [],
      formEnergyTypeOptions: [],
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
      form: {
        alarmDigitalValue: 0,
        alarmHHValue: null
      },
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
        ]
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
    this.getSignalTypeOptions()
    this.getSystemOptions()
    this.getEnergyTypeOptions()
  },
  methods: {
    getClassOptions () {
      listClass().then(res => {
        this.formClassOptions = res.rows
      })
    },
    getSignalTypeOptions () {
      listSignalType().then(res => {
        this.formSignalTypeOptions = res.rows
      })
    },
    getSystemOptions () {
      listSystem().then(res => {
        this.formSystemOptions = res.rows
      })
    },
    getEnergyTypeOptions () {
      listEnergyType().then(res => {
        this.formEnergyTypeOptions = res.rows
      })
    },
    changeQueryPlantOptions (value) {
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
    changeQueryBuildingOptions (value) {
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
    changeQueryDeviceOptions (value) {
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
    changeQueryMeterCodeOptions (value) {
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
    changeFormCompanyOptions (value) {
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
    changeFormPlantOptions (value) {
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
    changeFormBuildingOptions (value) {
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
    changeFormDeviceOptions (value) {
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
    changeFormMeterOptions (value) {
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
    getCompanyOptions () {
      listCompany().then(res => {
        this.companyOptions = this.formCompanyOptions = res.rows
      })
    },
    getMeterCodeOptions () {
      listMeter().then(res => {
        this.queryMeterOptions = this.formMeterOptions = this.meterOptions =
          res.rows
      })
    },
    getPlantOptions () {
      listPlant().then(res => {
        this.plantOptions = this.queryPlantOptions = this.formPlantOptions =
          res.rows
      })
    },
    getBuildingOptions () {
      listBuilding().then(res => {
        this.buildingOptions = this.queryBuildingOptions = this.formBuildingOptions =
          res.rows
      })
    },
    getDeviceOptions () {
      listDevice().then(res => {
        this.deviceOptions = this.queryDeviceOptions = this.formDeviceOptions =
          res.rows
      })
    },
    indexMethod (index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      )
    },
    /** 查询测点列表 */
    getList () {
      this.loading = true
      listMeasuringPoint(this.queryParams).then(response => {
        const list = response.rows
        for (const index in list) {
          let disable = list[index].disable
          let totalFlag = list[index].totalFlag
          let mainTag = list[index].mainTag
          if (disable == 0) {
            disable = '可用'
          } else {
            disable = '不可用'
          }

          if (totalFlag == 0) {
            totalFlag = '是'
          } else {
            totalFlag = '否'
          }

          if (mainTag == 0) {
            mainTag = '是'
          } else {
            mainTag = '否'
          }
          list[index].disable = disable
          list[index].totalFlag = totalFlag
          list[index].mainTag = mainTag
        }
        this.measuringPointList = list
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel () {
      this.open = false
      this.reset()
    },
    handleDialogClose () {
      this.formCompanyOptions = this.companyOptions
      this.formPlantOptions = this.plantOptions
      this.formBuildingOptions = this.buildingOptions
      this.formDeviceOptions = this.deviceOptions
      this.formMeterOptions = this.meterOptions
    },
    // 表单重置
    reset () {
      this.form = {
        id: undefined,
        tagName: undefined,
        description: undefined
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
      this.title = '添加测点'
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.reset()
      const id = row.id
      getMeasuringPoint(id).then(response => {
        const data = response.data
        if (data.totalFlag != null) {
          data.totalFlag = Number(data.totalFlag)
        }
        if (data.mainTag != null) {
          data.mainTag = Number(data.mainTag)
        }
        this.form = data
        this.open = true
        this.title = '修改测点'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
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
    handleDelete (row) {
      const ids = row.id || this.ids
      this.$confirm('是否确认删除,删除后不能恢复?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function () {
          return delMeasuringPoint(ids)
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
      this.$confirm('是否确认导出所有测点数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function () {
          return exportMeasuringPoint(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
        .catch(function () { })
    },
    /** 导入按钮操作 */
    handleImport () {
      this.upload = {
        title: '测点导入',
        open: true
      }
    }
  }
}
</script>