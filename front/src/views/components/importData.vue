<template>
  <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px">
    <el-upload
      ref="upload"
      :limit="1"
      accept=".xlsx, .xls"
      :headers="upload.headers"
      :action="upload.url + '?updateSupport=' + upload.updateSupport"
      :disabled="upload.isUploading"
      :on-progress="handleFileUploadProgress"
      :on-success="handleFileSuccess"
      :auto-upload="false"
      drag
    >
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">
        将文件拖到此处，或
        <em>点击上传</em>
      </div>
      <div class="el-upload__tip" slot="tip">
        <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的数据
        <el-link type="info" style="font-size:12px" @click="importTemplate">下载模板</el-link>
      </div>
      <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“xls”或“xlsx”格式文件！</div>
    </el-upload>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitFileForm">确 定</el-button>
      <el-button @click="upload.open = false">取 消</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { getToken } from '@/utils/auth'
import { importCompanyTemplate } from '@/api/energy/company'
import { importPlantTemplate } from '@/api/energy/plant'
import { importBuildingTemplate } from '@/api/energy/building'
import { importDeviceTemplate } from '@/api/energy/device'
import { importMeterTemplate } from '@/api/energy/meter'
import { importMeasuringPointTemplate } from '@/api/energy/measuringPoint'

export default {
  name: 'ImportData',
  props: {
    importData: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      upload: {
        // 是否显示弹出层（导入）
        open: false,
        // 弹出层标题（导入）
        title: '',
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: 'Bearer ' + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + '/energy/company/importData'
      }
    }
  },
  watch: {
    importData: {
      deep: true,
      handler(val) {
        this.handleImportData(val)
      }
    }
  },
  methods: {
    handleImportData(importData) {
      const importType = importData.importType
      const upload = importData.upload
      upload.isUploading = false
      upload.updateSupport = 0
      upload.headers = { Authorization: 'Bearer ' + getToken() }
      upload.url = `${process.env.VUE_APP_BASE_API}/energy/${importType}/importData`
      this.upload = upload
    },
    /** 下载模板操作 */
    importTemplate() {
      const importType = this.importData ? this.importData.importType : null
      if (importType) {
        switch (importType) {
          case 'company':
            importCompanyTemplate().then(response => {
              this.download(response.msg)
            })
            break
          case 'plant':
            importPlantTemplate().then(response => {
              this.download(response.msg)
            })
            break
          case 'building':
            importBuildingTemplate().then(response => {
              this.download(response.msg)
            })
            break
          case 'device':
            importDeviceTemplate().then(response => {
              this.download(response.msg)
            })
            break
          case 'meter':
            importMeterTemplate().then(response => {
              this.download(response.msg)
            })
            break
          case 'measuringPoint':
            importMeasuringPointTemplate().then(response => {
              this.download(response.msg)
            })
            break
          default:
            break
        }
      }
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false
      this.upload.isUploading = false
      this.$refs.upload.clearFiles()
      this.$alert(response.msg, '导入结果', { dangerouslyUseHTMLString: true })
      this.$parent.getList()
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit()
    }
  }
}
</script>