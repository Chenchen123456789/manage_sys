<template>
  <div class="app-container">
    <el-form :inline="true">
      <el-form-item label="菜单名称">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入菜单名称"
          size="small"
          v-model="queryParams.menuName"
        />
      </el-form-item>
      <el-form-item label="状态">
        <el-select clearable placeholder="菜单状态" size="small" v-model="queryParams.visible">
          <el-option
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
            v-for="dict in visibleOptions"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery" icon="el-icon-search" size="mini" type="primary">搜索</el-button>
        <el-button
          @click="handleAdd"
          icon="el-icon-plus"
          size="mini"
          type="primary"
          v-hasPermi="['system:menu:add']"
        >新增</el-button>
      </el-form-item>
    </el-form>

    <el-table
      :data="menuList"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      row-key="menuId"
      v-loading="loading"
    >
      <el-table-column :show-overflow-tooltip="true" label="菜单名称" prop="menuName" width="160"></el-table-column>
      <el-table-column align="center" label="图标" prop="icon" width="100">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column label="排序" prop="orderNum" width="60"></el-table-column>
      <el-table-column :show-overflow-tooltip="true" label="权限标识" prop="perms"></el-table-column>
      <el-table-column :show-overflow-tooltip="true" label="组件路径" prop="component"></el-table-column>
      <el-table-column :formatter="visibleFormat" label="可见" prop="visible" width="80"></el-table-column>
      <el-table-column align="center" label="创建时间" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            @click="handleUpdate(scope.row)"
            icon="el-icon-edit"
            size="mini"
            type="text"
            v-hasPermi="['system:menu:edit']"
          >修改</el-button>
          <el-button
            @click="handleAdd(scope.row)"
            icon="el-icon-plus"
            size="mini"
            type="text"
            v-hasPermi="['system:menu:add']"
          >新增</el-button>
          <el-button
            @click="handleDelete(scope.row)"
            icon="el-icon-delete"
            size="mini"
            type="text"
            v-hasPermi="['system:menu:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px">
      <el-form :model="form" :rules="rules" label-width="80px" ref="form">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级菜单">
              <treeselect
                :options="menuOptions"
                :show-count="true"
                placeholder="选择上级菜单"
                v-model="form.parentId"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单类型" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio label="M">目录</el-radio>
                <el-radio label="C">菜单</el-radio>
                <el-radio label="F">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单图标" v-if="form.menuType != 'F'">
              <el-popover
                @show="$refs['iconSelect'].reset()"
                placement="bottom-start"
                trigger="click"
                width="460"
              >
                <IconSelect @selected="selected" ref="iconSelect" />
                <el-input placeholder="点击选择图标" readonly slot="reference" v-model="form.icon">
                  <svg-icon
                    :icon-class="form.icon"
                    class="el-input__icon"
                    slot="prefix"
                    style="height: 32px;width: 16px;"
                    v-if="form.icon"
                  />
                  <i class="el-icon-search el-input__icon" slot="prefix" v-else />
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="menuName">
              <el-input placeholder="请输入菜单名称" v-model="form.menuName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number :min="0" controls-position="right" v-model="form.orderNum" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否外链" prop="isFrame" v-if="form.menuType != 'F'">
              <el-radio-group v-model="form.isFrame">
                <el-radio label="0">是</el-radio>
                <el-radio label="1">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.isFrame === '1' && form.menuType != 'F'">
            <el-form-item label="路由地址" prop="path">
              <el-input placeholder="请输入路由地址" v-model="form.path" />
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="form.isFrame === '0' && form.menuType != 'F'">
            <el-form-item label="路由地址" prop="path">
              <el-input placeholder="请输入路由地址" v-model="form.path" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="form.isFrame === '0' && form.menuType != 'F'">
            <el-form-item label="链接地址" prop="frameSrc">
              <el-input placeholder="请输入链接地址" v-model="form.frameSrc" />
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="form.menuType == 'C' && form.isFrame == '1'">
            <el-form-item label="组件路径" prop="component">
              <el-input placeholder="请输入组件路径" v-model="form.component" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="权限标识" v-if="form.menuType != 'M'">
              <el-input maxlength="50" placeholder="请权限标识" v-model="form.perms" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单状态" v-if="form.menuType != 'F'">
              <el-radio-group v-model="form.visible">
                <el-radio
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                  v-for="dict in visibleOptions"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div class="dialog-footer" slot="footer">
        <el-button @click="submitForm" type="primary">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listMenu,
  getMenu,
  treeselect,
  delMenu,
  addMenu,
  updateMenu
} from '@/api/system/menu'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import IconSelect from '@/components/IconSelect'

export default {
  name: 'Menu',
  components: { Treeselect, IconSelect },
  data () {
    return {
      // 遮罩层
      loading: true,
      // 菜单表格树数据
      menuList: [],
      // 菜单树选项
      menuOptions: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 菜单状态数据字典
      visibleOptions: [],
      // 查询参数
      queryParams: {
        menuName: undefined,
        visible: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        menuName: [
          { required: true, message: '菜单名称不能为空', trigger: 'blur' }
        ],
        orderNum: [
          { required: true, message: '菜单顺序不能为空', trigger: 'blur' }
        ],
        path: [
          { required: true, message: "路由地址不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created () {
    this.getList()
    this.getDicts('sys_show_hide').then(response => {
      this.visibleOptions = response.data
    })
  },
  methods: {
    // 选择图标
    selected (name) {
      this.form.icon = name
    },
    /** 查询菜单列表 */
    getList () {
      this.loading = true
      listMenu(this.queryParams).then(response => {
        this.menuList = response.data
        this.loading = false
      })
    },
    /** 查询菜单下拉树结构 */
    getTreeselect () {
      treeselect().then(response => {
        this.menuOptions = []
        const menu = { id: 0, label: '主类目', children: [] }
        menu.children = response.data
        this.menuOptions.push(menu)
      })
    },
    // 菜单显示状态字典翻译
    visibleFormat (row, column) {
      if (row.menuType == 'F') {
        return ''
      }
      return this.selectDictLabel(this.visibleOptions, row.visible)
    },
    // 取消按钮
    cancel () {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset () {
      this.form = {
        menuId: undefined,
        parentId: 0,
        menuName: undefined,
        icon: undefined,
        menuType: 'M',
        orderNum: undefined,
        isFrame: '1',
        visible: '0',
        frameSrc: undefined
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.getList()
    },
    /** 新增按钮操作 */
    handleAdd (row) {
      this.reset()
      this.getTreeselect()
      if (row != null) {
        this.form.parentId = row.menuId
      }
      this.open = true
      this.title = '添加菜单'
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.reset()
      this.getTreeselect()
      getMenu(row.menuId).then(response => {
        let resDate = response.data
        if (resDate.isFrame === '0') {
          const path = resDate.path
          const index = path.indexOf('&iframe?url=')
          if (index > -1) {
            const routerPath = path.substring(0, index)
            const frameSrc = path.substring(index + 12)
            resDate.path = routerPath
            resDate.frameSrc = frameSrc
          }
        }
        this.form = resDate
        this.open = true
        this.title = '修改菜单'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs['form'].validate(valid => {
        if (valid) {
          const formData = { ...this.form }
          if (formData.isFrame === '0') {
            formData.path =
              formData.path + '&iframe?url=' + formData.frameSrc
          }
          if (formData.menuId != undefined) {
            updateMenu(formData).then(response => {
              if (response.code === 200) {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              } else {
                this.msgError(response.msg)
              }
            })
          } else {
            addMenu(formData).then(response => {
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
      this.$confirm(
        '是否确认删除名称为"' + row.menuName + '"的数据项?',
        '警告',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
        .then(function () {
          return delMenu(row.menuId)
        })
        .then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        })
        .catch(function () { })
    }
  }
}
</script>