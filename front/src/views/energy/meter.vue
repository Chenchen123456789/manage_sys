<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['energy:meter:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['energy:meter:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['energy:meter:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['energy:meter:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="meterList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" :index="indexMethod" width="50" />
      <el-table-column label="公司名称" align="center" prop="companyName" />
      <el-table-column label="工厂名称" align="center" prop="plantName" />
      <el-table-column label="建筑名称" align="center" prop="buildingName" />
      <el-table-column label="设备名称" align="center" prop="deviceName" />
      <el-table-column label="仪表编号" align="center" prop="meterCode" />
      <el-table-column label="仪表名称" align="center" prop="meterName" />
      <el-table-column label="仪表厂家" align="center" prop="meterCompany" />
      <el-table-column label="仪表规格" align="center" prop="meterSpec" />
      <el-table-column label="安装地点" align="center" prop="location" />
      <el-table-column label="仪表参数" align="center" prop="meterParam" />
      <el-table-column label="描述" align="center" prop="meterDescription" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['energy:meter:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['energy:meter:remove']"
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

    <!-- 添加或修改仪表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="公司" prop="companyId">
          <el-select v-model="form.companyId" placeholder="请选择">
            <el-option
              v-for="item in companyOptions"
              :key="item.id"
              :label="item.companyName"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="工厂" prop="plantId">
          <el-select v-model="form.plantId" placeholder="请选择">
            <el-option
              v-for="item in plantOptions"
              :key="item.id"
              :label="item.plantName"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="建筑" prop="buildingId">
          <el-select v-model="form.buildingId" placeholder="请选择">
            <el-option
              v-for="item in buildingOptions"
              :key="item.id"
              :label="item.buildingName"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="设备" prop="deviceId">
          <el-select v-model="form.deviceId" placeholder="请选择">
            <el-option
              v-for="item in deviceOptions"
              :key="item.id"
              :label="item.deviceName"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="仪表编号" prop="meterCode">
          <el-input v-model="form.meterCode" placeholder="请输入仪表编号" />
        </el-form-item>
        <el-form-item label="仪表名称" prop="meterName">
          <el-input v-model="form.meterName" placeholder="请输入仪表名称" />
        </el-form-item>
        <el-form-item label="仪表厂家" prop="meterCompany">
          <el-input v-model="form.meterCompany" placeholder="请输入仪表厂家" />
        </el-form-item>
        <el-form-item label="仪表规格" prop="meterSpec">
          <el-input v-model="form.meterSpec" placeholder="请输入仪表规格" />
        </el-form-item>
        <el-form-item label="安装地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入安装地点" />
        </el-form-item>
        <el-form-item label="仪表参数" prop="meterParam">
          <el-input v-model="form.commParam" placeholder="请输入通讯参数" />
        </el-form-item>
        <el-form-item label="描述" prop="meterDescription">
          <el-input v-model="form.meterDescription" type="textarea" placeholder="请输入内容" />
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
import { listMeter, getMeter, delMeter, addMeter, updateMeter, exportMeter } from "@/api/energy/meter";
import { listCompany } from "@/api/energy/company";
import { listPlant } from "@/api/energy/plant";
import { listBuilding } from "@/api/energy/building";
import { listDevice } from "@/api/energy/device";

export default {
  name: "Meter",
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
      // 仪表表格数据
      meterList: [],
      companyOptions: [],
      plantOptions: [],
      buildingOptions: [],
      deviceOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        companyId: [
          { required: true, message: "公司不能为空", trigger: "blur" }
        ],
        plantId: [
          { required: true, message: "工厂不能为空", trigger: "blur" }
        ],
        buildingId: [
          { required: true, message: "建筑不能为空", trigger: "blur" }
        ],
        deviceId: [
          { required: true, message: "设备不能为空", trigger: "blur" }
        ],
        meterName: [
          { required: true, message: "仪表名称不能为空", trigger: "blur" }
        ],
        meterCode: [
          { required: true, message: "仪表编号不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created () {
    this.getList()
    this.getCompanyOptions()
    this.getPlantOptions()
    this.getBuildingOptions()
    this.getDeviceOptions()
  },
  methods: {
    getCompanyOptions () {
      listCompany().then(res => {
        this.companyOptions = res.rows
      })
    },
    getPlantOptions () {
      listPlant().then(res => {
        this.plantOptions = res.rows
      })
    },
    getBuildingOptions () {
      listBuilding().then(res => {
        this.buildingOptions = res.rows
      })
    },
    getDeviceOptions () {
      listDevice().then(res => {
        this.deviceOptions = res.rows
      })
    },
    indexMethod (index) {
      return ((this.queryParams.pageNum - 1) * this.queryParams.pageSize) + index + 1
    },
    /** 查询仪表列表 */
    getList () {
      this.loading = true;
      listMeter(this.queryParams).then(response => {
        this.meterList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel () {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset () {
      this.form = {
        id: undefined,
        meterCode: undefined,
        meterName: undefined,
        meterDescription: undefined,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange (selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd () {
      this.reset();
      this.open = true;
      this.title = "添加仪表";
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.reset();
      const id = row.id
      getMeter(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改仪表";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateMeter(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addMeter(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除,删除后不能恢复?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delMeter(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () { });
    },
    /** 导出按钮操作 */
    handleExport () {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有仪表数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportMeter(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(function () { });
    }
  }
};
</script>