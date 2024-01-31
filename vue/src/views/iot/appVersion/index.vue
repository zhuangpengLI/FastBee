<template>
  <div class="app-container">
    <!-- <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="apk下载链接" prop="apkUrl">
        <el-input
          v-model="queryParams.apkUrl"
          placeholder="请输入apk下载链接"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="版本号" prop="versionCode">
        <el-input
          v-model="queryParams.versionCode"
          placeholder="请输入版本号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="版本名称" prop="versionName">
        <el-input
          v-model="queryParams.versionName"
          placeholder="请输入版本名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否强制更新" prop="isForce">
        <el-select v-model="queryParams.isForce" placeholder="请选择是否强制更新" clearable size="small">
          <el-option
            v-for="dict in dict.type.iot_yes_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form> -->

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['iot:appVersion:add']"
        >新增</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['iot:appVersion:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['iot:appVersion:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['iot:appVersion:export']"
        >导出</el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="appVersionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="apkId" />
      <!-- <el-table-column label="apk类型1客户端" align="center" prop="apkType" /> -->
       <!-- 1 IOS  2 Android -->
      <el-table-column label="app平台" align="center" prop="platform" />
      <el-table-column label="apk下载链接" align="center" prop="apkUrl" />
      <el-table-column label="版本号" align="center" prop="versionCode" />
      <el-table-column label="版本名称" align="center" prop="versionName" />
      <el-table-column label="更新内容" align="center" prop="updateContent" />
      <el-table-column label="是否强制更新" align="center" prop="isForce">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.iot_yes_no" :value="scope.row.isForce"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['iot:appVersion:edit']"
          >修改</el-button> -->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['iot:appVersion:reverse']"
          >回退</el-button>
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

    <!-- 添加或修改升级管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="apk下载链接" prop="apkUrl">
          <el-input v-model="form.apkUrl" placeholder="请输入apk下载链接" />
        </el-form-item>
        <el-form-item label="版本号" prop="versionCode">
          <el-input v-model="form.versionCode" placeholder="请输入版本号" />
        </el-form-item>
        <el-form-item label="版本名称" prop="versionName">
          <el-input v-model="form.versionName" placeholder="请输入版本名称" />
        </el-form-item>
        <el-form-item label="更新内容" prop="updateContent">
          <el-input v-model="form.updateContent" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="是否强制更新" prop="isForce">
          <el-select v-model="form.isForce" placeholder="请选择是否强制更新">
            <el-option
              v-for="dict in dict.type.iot_yes_no"
              :key="dict.value"
              :label="dict.label"
:value="dict.value"
            ></el-option>
          </el-select>
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
import { listAppVersion, getAppVersion, delAppVersion, addAppVersion, updateAppVersion } from "@/api/iot/appVersion";

export default {
  name: "AppVersion",
  dicts: ['iot_yes_no'],
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
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 升级管理表格数据
      appVersionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        apkType: null,
        platform: null,
        apkUrl: null,
        versionCode: null,
        versionName: null,
        updateContent: null,
        isForce: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询升级管理列表 */
    getList() {
      this.loading = true;
      listAppVersion(this.queryParams).then(response => {
        this.appVersionList = response.rows;
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
        apkId: null,
        apkType: null,
        platform: null,
        apkUrl: null,
        versionCode: null,
        versionName: null,
        updateContent: null,
        isForce: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.apkId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加升级管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const apkId = row.apkId || this.ids
      getAppVersion(apkId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改升级管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.apkId != null) {
            updateAppVersion(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAppVersion(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const apkIds = row.apkId;
      this.$modal.confirm('是否确认回退到升级管理编号为"' + apkIds + '"的数据项？').then(function() {
        return delAppVersion(apkIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("回退成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('iot/appVersion/export', {
        ...this.queryParams
      }, `appVersion_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
