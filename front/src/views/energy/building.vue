<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['energy:building:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['energy:building:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['energy:building:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['energy:building:import']"
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
            v-hasPermi="['energy:building:export']"
            slot="reference"
          >导出</el-button>
        </el-popover>
      </el-col>
    </el-row>

    <el-table size="mini" v-loading="loading" :data="buildingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" :index="indexMethod" width="50" />
      <el-table-column label="公司名称" align="center" prop="companyName" />
      <el-table-column label="工厂名称" align="center" prop="plantName" />
      <el-table-column label="建筑编号" align="center" prop="buildingCode" />
      <el-table-column label="建筑名称" align="center" prop="buildingName" />
      <el-table-column label="描述" align="center" prop="buildingDescription" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['energy:building:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['energy:building:remove']"
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

    <!-- 添加或修改建筑对话框 -->
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
        <el-form-item label="建筑编号" prop="buildingCode">
          <el-input v-model="form.buildingCode" placeholder="请输入建筑编号" />
        </el-form-item>
        <el-form-item label="建筑名称" prop="buildingName">
          <el-input v-model="form.buildingName" placeholder="请输入建筑名称" />
        </el-form-item>
        <el-form-item label="描述" prop="buildingDescription">
          <el-input v-model="form.buildingDescription" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <ImportData :importData="{importType:'building', upload}"></ImportData>
  </div>
</template>

<script>
import {
  listBuilding,
  getBuilding,
  delBuilding,
  addBuilding,
  updateBuilding,
  exportBuilding
} from '@/api/energy/building'
import { listCompany } from '@/api/energy/company'
import { listPlant } from '@/api/energy/plant'
import ImportData from '../components/importData'

export default {
  name: 'Building',
  components:{
    ImportData
  },
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
      // 建筑表格数据
      buildingList: [],
      companyOptions: [],
      plantOptions: [],
      formCompanyOptions: [],
      formPlantOptions: [],
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
        companyId: [
          { required: false, message: '公司不能为空', trigger: 'blur' }
        ],
        plantId: [{ required: true, message: '工厂不能为空', trigger: 'blur' }],
        buildingName: [
          { required: true, message: '建筑名称不能为空', trigger: 'blur' }
        ],
        buildingCode: [
          { required: true, message: '建筑编号不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getCompanyOptions()
    this.getPlantOptions()
  },
  methods: {
    changeFormCompanyOptions(value) {
      if (value === '') {
        this.formPlantOptions = this.plantOptions
      } else {
        const temp = this.form
        temp.plantId = null
        this.form = { ...temp }
        this.formPlantOptions = this.plantOptions.filter(
          item => item.companyId == value
        )
      }
    },
    changeFormPlantOptions(value) {
      if (value === '') {
      } else {
        const plant = this.plantOptions.find(item => item.id == value)
        const companyId = plant.companyId || null
        const temp = this.form
        temp.companyId = companyId
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
    indexMethod(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      )
    },
    /** 查询建筑列表 */
    getList() {
      this.loading = true
      listBuilding(this.queryParams).then(response => {
        this.buildingList = response.rows
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
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        buildingCode: undefined,
        buildingName: undefined,
        buildingDescription: undefined
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
      this.title = '添加建筑'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id
      getBuilding(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改建筑'
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateBuilding(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              } else {
                this.msgError(response.msg)
              }
            })
          } else {
            addBuilding(this.form).then(response => {
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
          return delBuilding(ids)
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
      this.$confirm('是否确认导出建筑数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function() {
          return exportBuilding(queryParams)
        })
        .then(response => {
          this.download(response.msg)
        })
        .catch(function() {})
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload = {
        title: '建筑导入',
        open: true
      }
    }
  }
}
</script>