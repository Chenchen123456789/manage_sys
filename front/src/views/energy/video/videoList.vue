<template>
  <div class="app-container">
    <el-table size="mini" v-loading="loading" :data="buildingList">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" :index="indexMethod" width="50" />
      <el-table-column label="摄像头名" align="center" prop="cameraName" />
      <el-table-column label="描述" align="center" prop="cameraDescription" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
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
  </div>
</template>

<script>

export default {
  name: 'VideoList',
  data () {
    return {
      // 遮罩层
      loading: true,
      total: 0,
      // 建筑表格数据
      buildingList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
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
    handleView(){
      let id = 1
      this.$router.push(`/video/videoPreview/${id}`)
    },
    indexMethod (index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      )
    },
    /** 查询建筑列表 */
    getList () {
      this.loading = true
      // listBuilding(this.queryParams).then(response => {
      //   this.buildingList = response.rows
      //   this.total = response.total
      //   this.loading = false
      // })
      this.buildingList = [{ cameraName: 'test', cameraDescription: '测试用' }]
      this.total = 1
      this.loading = false
    }
  }
}
</script>