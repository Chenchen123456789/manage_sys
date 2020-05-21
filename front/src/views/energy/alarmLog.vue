<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
      <el-form-item label="报警时间范围">
        <el-date-picker
          v-model="alarmTimeRange"
          size="small"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="测点名" prop="queryTagName">
        <el-select
          clearable
          v-model="queryParams.queryTagName"
          placeholder="请选择"
          size="small"
          multiple
          collapse-tags
        >
          <el-option
            v-for="item in queryMeasuringPointOptions"
            :key="item.id"
            :label="item.tagName"
            :value="item.tagName"
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
          type="success"
          icon="el-icon-check"
          size="mini"
          :disabled="multiple"
          @click="handleUpdate"
          v-hasPermi="['energy:building:edit']"
        >确认</el-button>
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
            v-hasPermi="['energy:alarmLog:export']"
            slot="reference"
          >导出</el-button>
        </el-popover>
      </el-col>

      <el-col style="float:right" :span="1.5">
        <el-tooltip
          class="tooltip-alarm"
          effect="dark"
          content="黄色:LL/L/FALSE； 红色:H/HH/TRUE； 蓝色:确认后未恢复； 紫色:未确认自恢复； 绿色:确认后自恢复"
          placement="top"
        >
          <i style="font-size:24px; color:black" class="el-icon-question"></i>
        </el-tooltip>
      </el-col>
    </el-row>
    <el-table
      class="alarm-log"
      size="mini"
      v-loading="loading"
      :data="alarmLogList"
      @selection-change="handleSelectionChange"
      :row-class-name="tableRowClassName"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" :index="indexMethod" width="50" />
      <el-table-column v-if="false" label="id" prop="id" width="50" />
      <el-table-column label="测点名" width="150" align="center">
        <template slot-scope="scope">
          <el-link @click="handleClickTagName(scope.row)" style="color: white">{{scope.row.tagName}}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="测点描述" width="350" align="center" prop="description" />
      <el-table-column label="测点值" width="150" align="center" prop="tagValue" />
      <el-table-column label="报警时间" width="180" align="center" prop="alarmTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.alarmTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="确认时间" width="180" align="center" prop="ackTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.ackTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="恢复时间" width="180" align="center" prop="restoreTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.restoreTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="确认用户" width="150" align="center" prop="ackUser" />
      <el-table-column label="报警状态" width="100" align="center" prop="status" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.ackStatus == 0"
            size="small"
            icon="el-icon-check"
            type="success"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['energy:alarmLog:edit']"
          >确认</el-button>
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

    <el-dialog title="操作日志详细" :visible.sync="open" width="700px">
      <el-form ref="form" :model="form" label-width="115px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="测点名称：">{{ form.tagName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态：">{{ form.status }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="描述：">{{ form.description }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公司：">{{ form.companyName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工厂：">{{ form.plantName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="建筑：">{{ form.buildingName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备：">{{ form.deviceName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="仪表：">{{ form.meterName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="系统：">{{ form.systemName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="能源类型：">{{ form.energyTypeName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据分类：">{{ form.signalTypeName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总量点：">{{ form.totalFlag }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主参数：">{{ form.mainTag }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报警类型：">{{ form.alarmTag }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报警等级：">{{ form.alarmLevel }}</el-form-item>
          </el-col>
          <template v-if="form.alarmTag == '开关量'">
            <el-col :span="24">
              <el-form-item label="报警方式：">{{ form.alarmDigitalType }}</el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="为0报警说明：">{{ form.alarmDigitalZeroDescription }}</el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="为1报警说明：">{{ form.alarmDigitalOneDescription }}</el-form-item>
            </el-col>
          </template>
          <template v-if="form.alarmTag == '模拟量'">
            <el-col :span="12">
              <el-form-item label="报警LL值：">{{ form.alarmLLValue }}</el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="报警L值：">{{ form.alarmLValue }}</el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="报警H值：">{{ form.alarmHValue }}</el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="报警HH值：">{{ form.alarmHHValue }}</el-form-item>
            </el-col>
          </template>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listAlarmLog,
  getAlarmLog,
  updateAlarmLog,
  exportAlarmLog,
  listMeasuringPoint,
  getMeasuringPoint
} from '@/api/energy/alarmLog'

export default {
  name: 'AlarmLog',
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
      // 公司表格数据
      alarmLogList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      form: {},
      queryMeasuringPointOptions: [],
      // 查询参数
      alarmTimeRange: [undefined, undefined],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        queryTagName: []
      },
    }
  },
  created () {
    this.getList()
    this.getMeasuringPointList()
  },
  methods: {
    handleClickTagName (row) {
      const tagName = row.tagName
      this.open = true;
      getMeasuringPoint(tagName).then(res => {
        const resData = res.data
        console.log(resData)
        let alarmTag, alarmLevel, alarmDigitalType
        switch (resData.alarm_tag) {
          case 0:
            alarmTag = '不报警'
            break;
          case 1:
            alarmTag = '模拟量'
            break;
          case 2:
            alarmTag = '开关量'
            break;
          default:
            alarmTag = '-'
            break;
        }

        switch (resData.alarm_level) {
          case 0:
            alarmLevel = '普通'
            break;
          case 1:
            alarmLevel = '一般'
            break;
          case 2:
            alarmLevel = '紧急'
            break;
          case 3:
            alarmLevel = '特急'
            break;
          default:
            alarmLevel = '-'
            break;
        }
        switch (resData.alarm_digital_value) {
          case 0:
            alarmDigitalType = '为0报警'
            break;
          case 1:
            alarmDigitalType = '为1报警'
            break;
          case 2:
            alarmDigitalType = '0、1都报警'
            break;
          default:
            alarmDigitalType = '-'
            break;
        }

        const formData = {
          tagName: resData.tag_name,
          status: resData.disable == 0 ? '可用' : '不可用',
          description: resData.description,
          companyName: resData.company_name,
          plantName: resData.plant_name,
          buildingName: resData.building_name,
          deviceName: resData.device_name,
          meterName: resData.meter_name,
          systemName: resData.system_name,
          energyTypeName: resData.energy_type_name,
          signalTypeName: resData.signal_type_name,
          mainTag: resData.main_tag == 0 ? '是' : '否',
          totalFlag: resData.total_flag == 0 ? '是' : '否',
          alarmTag,
          alarmLevel,
          alarmLLValue: resData.alarm_ll_value,
          alarmLValue: resData.alarm_l_value,
          alarmHValue: resData.alarm_h_value,
          alarmHHValue: resData.alarm_hh_value,
          alarmDigitalType,
          alarmDigitalZeroDescription: resData.alarm_digital_zero_description,
          alarmDigitalOneDescription: resData.alarm_digital_one_description
        }
        this.form = formData
      })
    },
    getMeasuringPointList () {
      listMeasuringPoint().then(res => {
        this.queryMeasuringPointOptions = res.rows
      })
    },
    handleUpdate (row) {
      const ids = row.id || this.ids
      return updateAlarmLog(ids)
    },
    tableRowClassName ({ row, rowIndex }) {
      if (row.bgColor === "yellow") {
        return 'yellow-row';
      } else if (row.bgColor === "red") {
        return 'red-row';
      } else if (row.bgColor === "blue") {
        return 'blue-row';
      } else if (row.bgColor === "purple") {
        return 'purple-row';
      } else if (row.bgColor === "green") {
        return 'green-row';
      }
      return '';
    },
    indexMethod (index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      )
    },
    getList () {
      this.loading = true

      listAlarmLog(this.addDateRange(this.queryParams, this.alarmTimeRange)).then(response => {
        this.alarmLogList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.alarmTimeRange = [undefined, undefined]
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange (selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      const ids = row.id || this.ids
      updateAlarmLog(ids).then(response => {
        this.getList()
      })
    },
    /** 导出按钮操作 */
    handleExport (type) {
      const queryParams = { ...this.queryParams }
      if (type === 0) {
        queryParams.pageNum = null
      }
      const _this = this
      this.$confirm('是否确认导出报警数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function () {
          return exportAlarmLog(_this.addDateRange(queryParams, _this.alarmTimeRange))
        })
        .then(response => {
          this.download(response.msg)
        })
        .catch(function () { })
    },
  }
}
</script>

<style lang="scss">
.el-table .yellow-row {
  background: #e6a23c;
}

.el-table .red-row {
  background: #f56c6c;
}

.el-table .blue-row {
  background: #409eff;
}

.el-table .purple-row {
  background: #ba55d3;
}

.el-table .green-row {
  background: #67c23a;
}
.alarm-log {
  .el-table__body {
    color: white;
  }
  tr {
    height: 45px;
  }
  tbody tr:hover > td {
    background-color: #909399 !important;
  }
}
</style>