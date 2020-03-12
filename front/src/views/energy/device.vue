<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['energy:device:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['energy:device:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['energy:device:remove']"
        >删除</el-button>
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
            v-hasPermi="['energy:device:export']"
            slot="reference"
          >导出</el-button>
        </el-popover>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="deviceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" :index="indexMethod" width="50" />
      <el-table-column label="公司名称" align="center" prop="companyName" />
      <el-table-column label="工厂名称" align="center" prop="plantName" />
      <el-table-column label="建筑名称" align="center" prop="buildingName" />
      <el-table-column label="设备编号" align="center" prop="deviceCode" />
      <el-table-column label="设备名称" align="center" prop="deviceName" />
      <el-table-column label="设备厂家" align="center" prop="deviceCompany" />
      <el-table-column label="设备规格" align="center" prop="deviceSpec" />
      <el-table-column label="安装地点" align="center" prop="location" />
      <el-table-column label="通讯协议" align="center" prop="commProtocol" />
      <el-table-column label="通讯参数" align="center" prop="commParam" />
      <el-table-column label="通讯IP" align="center" prop="commLink" />
      <el-table-column label="描述" align="center" prop="deviceDescription" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['energy:device:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['energy:device:remove']"
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

    <!-- 添加或修改设备对话框 -->
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
        <el-form-item label="设备编号" prop="deviceCode">
          <el-input v-model="form.deviceCode" placeholder="请输入设备编号" />
        </el-form-item>
        <el-form-item label="设备名称" prop="deviceName">
          <el-input v-model="form.deviceName" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="设备厂家" prop="deviceCompany">
          <el-input v-model="form.deviceCompany" placeholder="请输入设备厂家" />
        </el-form-item>
        <el-form-item label="设备规格" prop="deviceSpec">
          <el-input v-model="form.deviceSpec" placeholder="请输入设备规格" />
        </el-form-item>
        <el-form-item label="安装地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入安装地点" />
        </el-form-item>
        <el-form-item label="通讯协议" prop="commProtocol">
          <el-input v-model="form.commProtocol" placeholder="请输入通讯协议" />
        </el-form-item>
        <el-form-item label="通讯参数" prop="commParam">
          <el-input v-model="form.commParam" placeholder="请输入通讯参数" />
        </el-form-item>
        <el-form-item label="通讯IP" prop="commLink">
          <el-input v-model="form.commLink" placeholder="请输入通讯IP" />
        </el-form-item>
        <el-form-item label="描述" prop="deviceDescription">
          <el-input v-model="form.deviceDescription" type="textarea" placeholder="请输入内容" />
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
  listDevice,
  getDevice,
  delDevice,
  addDevice,
  updateDevice,
  exportDevice
} from '@/api/energy/device'
import { listCompany } from '@/api/energy/company'
import { listPlant } from '@/api/energy/plant'
import { listBuilding } from '@/api/energy/building'

export default {
  name: 'Device',
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
      // 设备表格数据
      deviceList: [],
      companyOptions: [],
      plantOptions: [],
      buildingOptions: [],
      formCompanyOptions: [],
      formPlantOptions: [],
      formBuildingOptions: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
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
          { required: true, message: '建筑不能为空', trigger: 'blur' }
        ],
        deviceName: [
          { required: true, message: '设备名称不能为空', trigger: 'blur' }
        ],
        deviceCode: [
          { required: true, message: '设备编号不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getCompanyOptions()
    this.getPlantOptions()
    this.getBuildingOptions()
  },
  methods: {
    changeFormCompanyOptions(value) {
      if (value === '') {
        this.formPlantOptions = this.plantOptions
        this.formBuildingOptions = this.buildingOptions
      } else {
        const temp = this.form
        temp.plantId = temp.buildingId = null
        this.form = { ...temp }
        this.formPlantOptions = this.plantOptions.filter(
          item => item.companyId == value
        )
        this.formBuildingOptions = this.buildingOptions.filter(
          item => item.companyId == value
        )
      }
    },
    changeFormPlantOptions(value) {
      if (value === '') {
        this.formBuildingOptions = this.buildingOptions
      } else {
        const temp = this.form
        temp.buildingId = null
        this.form = { ...temp }
        const plant = this.plantOptions.find(item => item.id == value)
        const companyId = plant.companyId || null
        this.form.companyId = companyId
        this.formBuildingOptions = this.buildingOptions.filter(
          item => item.plantId == value
        )
      }
    },
    changeFormBuildingOptions(value) {
      if (value === '') {
      } else {
        const building = this.buildingOptions.find(item => item.id == value)
        const companyId = building.companyId || null
        const plantId = building.plantId || null
        const temp = this.form
        temp.companyId = companyId
        temp.plantId = plantId
        this.form = { ...temp }
      }
    },
    getCompanyOptions() {
      listCompany().then(res => {
        this.companyOptions = this.formCompanyOptions = res.rows
      })
    },
    getPlantOptions() {
      listPlant().then(res => {
        this.plantOptions = this.formPlantOptions = res.rows
      })
    },
    getBuildingOptions() {
      listBuilding().then(res => {
        this.buildingOptions = this.formBuildingOptions = res.rows
      })
    },
    indexMethod(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      )
    },
    /** 查询设备列表 */
    getList() {
      this.loading = true
      listDevice(this.queryParams).then(response => {
        this.deviceList = response.rows
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
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        deviceCode: undefined,
        deviceName: undefined,
        deviceDescription: undefined
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
      this.title = '添加设备'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id
      getDevice(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改设备'
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateDevice(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              } else {
                this.msgError(response.msg)
              }
            })
          } else {
            addDevice(this.form).then(response => {
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
          return delDevice(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        })
        .catch(function() {})
    },
    /** 导出按钮操作 */
    handleExport(type) {
      const queryParams = { ...this.queryParams }
      if (type === 0) {
        queryParams.pageNum = null
      }
      this.$confirm('是否确认导出设备数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function() {
          return exportDevice(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
        .catch(function() {})
    }
  }
}
</script>