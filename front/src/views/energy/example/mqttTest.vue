<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <!-- <el-form style="width: 400px" ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="主题" prop="topic">
          <el-input v-model="form.topic" placeholder="请输入建筑编号" />
        </el-form-item>
        <el-form-item label="建筑名称" prop="buildingName">
          <el-input v-model="form.buildingName" placeholder="请输入建筑名称" />
        </el-form-item>
        <el-form-item label="描述" prop="buildingDescription">
          <el-input v-model="form.buildingDescription" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>-->
      <el-col :span="1.5">
        <el-button type="primary" size="mini" @click="handleTest">测试</el-button>
      </el-col>
    </el-row>

    <el-table size="mini" border v-loading="loading" :data="dataList">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" :index="indexMethod" width="70" />
      <el-table-column label="测点名" align="center" prop="tagname" />
      <el-table-column label="测点值" align="center" prop="tagvalue" />
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { testRTableList } from '@/api/energy/mqttTest'

export default {
  name: 'Building',
  data () {
    return {
      // 总条数
      total: 0,
      dataList: [],
      // 遮罩层
      loading: true,
      form: {},
      rules: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
    }
  },
  created () {
    this.getList()
  },
  methods: {
    handleTest () {
      testRTableList(this.queryParams).then(res => {
        this.dataList = res.rows
        this.total = res.total
      })
    },
    getList () {
      this.loading = false
    },
    indexMethod (index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      )
    }
  }
}
</script>