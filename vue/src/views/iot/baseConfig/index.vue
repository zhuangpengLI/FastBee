<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="基础设置" name="first"
        ><el-form
          ref="form"
          :model="baseConfigList"
          :rules="rules"
          label-width="180px"
        >
          <el-form-item
            class="el-form-item"
            label="单账号最多配置家庭数"
            prop="userCreateRoomMax"
          >
            <el-input
              v-model="baseConfigList.userCreateRoomMax"
              placeholder="请输入单账号最多配置家庭数"
            />
          </el-form-item>
          <el-form-item
            class="el-form-item"
            label="用户设备同时登录最大数"
            prop="userLoginDeviceMax"
          >
            <el-input
              v-model="baseConfigList.userLoginDeviceMax"
              placeholder="请输入用户设备同时登录最大数"
            />
          </el-form-item>
          <el-form-item
            class="el-form-item"
            label="客服手机号"
            prop="customerServicePhone"
          >
            <el-input
              v-model="baseConfigList.customerServicePhone"
              placeholder="请输入客服手机号"
            />
          </el-form-item>
          <el-form-item
            class="el-form-item"
            label="用户接受短信最大数量"
            prop="userReceiveSmsMax"
          >
            <el-input
              v-model="baseConfigList.userReceiveSmsMax"
              placeholder="请输入用户接受短信最大数量"
            />
          </el-form-item>
          <el-form-item
            class="el-form-item"
            label="用户设置场景最大数"
            prop="userSceneMax"
          >
            <el-input
              v-model="baseConfigList.userSceneMax"
              placeholder="请输入用户设置场景最大数"
            />
          </el-form-item>
          <el-form-item
            class="el-form-item"
            label="家庭成员最大数"
            prop="familUserMax"
          >
            <el-input
              v-model="baseConfigList.familUserMax"
              placeholder="请输入家庭成员最大数"
            />
          </el-form-item> </el-form
      ></el-tab-pane>
      <el-tab-pane label="其他参数" name="second"></el-tab-pane>
    </el-tabs>
    <div class="dialog-footer">
      <el-button type="primary" @click="submitForm">保存</el-button>
      <!-- <el-button @click="cancel">取 消</el-button> -->
    </div>
  </div>
</template>

<script>
import {
  listBaseConfig,
  addBaseConfig,
  updateBaseConfig,
} from "@/api/iot/baseConfig";

export default {
  name: "BaseConfig",
  data() {
    return {
       activeName: 'first',
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 系统参数表格数据
      baseConfigList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userCreateRoomMax: null,
        userLoginDeviceMax: null,
        customerServicePhone: null,
        userReceiveSmsMax: null,
        userSceneMax: null,
        familUserMax: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询系统参数列表 */
    getList() {
      this.loading = true;
      listBaseConfig(this.queryParams).then((response) => {
        this.baseConfigList = response.rows[0];
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        userCreateRoomMax: null,
        userLoginDeviceMax: null,
        customerServicePhone: null,
        userReceiveSmsMax: null,
        userSceneMax: null,
        familUserMax: null,
      };
      this.resetForm("form");
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.baseConfigList.id != null) {
            updateBaseConfig(this.baseConfigList).then((response) => {
              this.$modal.msgSuccess("修改成功");
              // this.open = false;
              this.getList();
            });
          } else {
            addBaseConfig(this.baseConfigList).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
  },
};
</script>
<style scoped>
.el-input {
  width: 350px;
}
.dialog-footer {
  position: fixed;
  bottom: 30px;
}
.el-form-item {
  margin-top: 40px;
}
</style>

