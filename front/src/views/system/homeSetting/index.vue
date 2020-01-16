<template>
  <el-form
    class="homeSetting"
    size="medium"
    ref="form"
    :model="homeSetting"
    :rules="rules"
    label-width="80px"
  >
    <el-form-item label="id" hidden prop="id">
      <el-input v-model="homeSetting.id" />
    </el-form-item>
    <el-form-item label="名称" prop="menuName">
      <el-input v-model="homeSetting.menuName" placeholder="请输入名称" />
    </el-form-item>
    <el-form-item label="菜单图标">
      <el-popover
        placement="bottom-start"
        width="460"
        trigger="click"
        @show="$refs['iconSelect'].reset()"
      >
        <IconSelect ref="iconSelect" @selected="selected" />
        <el-input slot="reference" v-model="homeSetting.iconClass" placeholder="点击选择图标" readonly>
          <svg-icon
            v-if="homeSetting.icon"
            slot="prefix"
            :icon-class="homeSetting.icon"
            class="el-input__icon"
            style="height: 32px;width: 16px;"
          />
          <i v-else slot="prefix" class="el-icon-search el-input__icon" />
        </el-input>
      </el-popover>
    </el-form-item>
    <el-form-item label="是否外链" prop="isFrame">
      <el-radio-group v-model="homeSetting.isFrame">
        <el-radio :label="0">是</el-radio>
        <el-radio :label="1">否</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="路由地址" prop="router">
      <el-input v-model="homeSetting.router" placeholder="请输入路由地址" />
    </el-form-item>
    <el-form-item v-if="homeSetting.isFrame === 0" label="链接地址" prop="frameSrc">
      <el-input v-model="homeSetting.frameSrc" placeholder="请输入链接地址" />
    </el-form-item>
    <el-form-item v-if="homeSetting.isFrame === 1" label="组件路径" prop="component">
      <el-input v-model="homeSetting.component" placeholder="请输入组件路径" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">保存</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { getHomeSetting, updateHomeSetting } from "@/api/system/homeSetting";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import IconSelect from "@/components/IconSelect";

export default {
  components: {
    IconSelect
  },
  data () {
    return {
      homeSetting: {
        isFrame: 0
      },
      // 表单校验
      rules: {
        menuName: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
        isFrame: [
          { required: true, message: "请选择是否外链", trigger: "blur" },
        ],
        router: [
          { required: true, message: "路由地址不能为空", trigger: "blur" },
        ]
      }
    };
  },
  created () {
    this.getDetail()
  },
  methods: {
    getDetail () {
      getHomeSetting().then(res => {
        this.homeSetting = res.data
      })
    },
    // 选择图标
    selected (name) {
      this.homeSetting.iconClass = name;
    },
    submit () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateHomeSetting(this.homeSetting).then(
            response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
              } else {
                this.msgError(response.msg);
              }
            }
          );
        }
      });
    }
  }
};
</script>

<style lang="scss" scope>
.homeSetting {
  margin-top: 20px;
  width: 500px;
  margin-left: 20px;
}
</style>
