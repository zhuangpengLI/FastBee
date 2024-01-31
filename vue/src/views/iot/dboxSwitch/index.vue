<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="素材名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入素材名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="空开类型" prop="switchType">
        <el-select
          v-model="queryParams.switchType"
          placeholder="请选择空开类型"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in dict.type.iot_air_switch_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <!-- 0=正常(默认合闸)， 1=跳闸 分闸   2=欠压 -->
      <el-form-item label="空开状态" prop="switchStatus">
        <el-select
          v-model="queryParams.switchStatus"
          placeholder="请选择空开状态 0=正常(默认合闸)， 1=跳闸 分闸   2=欠压"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in dict.type.iot_air_switch_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="配电箱型号" prop="dboxType" label-width="100">
        <el-input
          v-model="queryParams.dboxType"
          placeholder="请输入配电箱型号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否启用" prop="enable">
        <el-select
          v-model="queryParams.enable"
          placeholder="请选择是否启用 0否1是"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in dict.type.iot_is_enable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['iot:dboxSwitch:add']"
          >新增</el-button
        >
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['iot:dboxSwitch:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['iot:dboxSwitch:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['iot:dboxSwitch:export']"
          >导出</el-button
        >
      </el-col> -->
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="dboxSwitchList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="素材名称" align="center" prop="name" />
      <el-table-column label="空开类型" align="center" prop="switchType">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.iot_air_switch_type"
            :value="scope.row.switchType"
          />
        </template>
      </el-table-column>
      <!-- 0=正常(默认合闸)， 1=跳闸 分闸   2=欠压 -->
      <el-table-column label="空开状态" align="center" prop="switchStatus">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.iot_air_switch_status"
            :value="scope.row.switchStatus"
          />
        </template>
      </el-table-column>
      <!-- <el-table-column label="空开状态图" align="center" prop="imgUrl" /> -->
      <el-table-column label="空开状态图" align="center">
        <template slot-scope="scope">
          <img
            @click="handleprc(scope.row.imgUrl)"
            :src="scope.row.imgUrl"
            width="40px"
            height="100px"
          />
        </template>
      </el-table-column>
      <!-- <el-table-column label="配电箱型号" align="center" prop="dboxType" />
      <el-table-column label="配电箱id" align="center" prop="dboxId" /> -->
      <el-table-column label="是否启用" align="center" prop="enable">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.iot_is_enable"
            :value="scope.row.enable"
          />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['iot:dboxSwitch:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['iot:dboxSwitch:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改配电箱空开素材对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="素材名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入素材名称" />
        </el-form-item>
        <el-form-item label="空开类型" prop="switchType">
          <el-select v-model="form.switchType" placeholder="请选择空开类型">
            <el-option
              v-for="dict in dict.type.iot_air_switch_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <!-- 0=正常(默认合闸)， 1=跳闸 分闸   2=欠压 -->
        <el-form-item label="空开状态" prop="switchStatus">
          <el-select v-model="form.switchStatus" placeholder="请选择空开状态">
            <el-option
              v-for="dict in dict.type.iot_air_switch_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="空开状态图">
          <el-upload
            :headers="headers"
            :action="uploadPath"
            :show-file-list="false"
            :on-success="uploadimgUrl"
            class="avatar-uploader"
            accept=".jpg,.jpeg,.png,.gif"
          >
            <img v-if="form.imgUrl" :src="form.imgUrl" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
        </el-form-item>
        <!-- <el-form-item label="配电箱型号" prop="dboxType">
          <el-input v-model="form.dboxType" placeholder="请输入配电箱型号" />
        </el-form-item> -->
        <el-form-item label="是否启用" prop="enable">
          <el-select v-model="form.enable" placeholder="请选择是否启用">
            <el-option
              v-for="dict in dict.type.iot_is_enable"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入内容"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 图片弹窗 -->
    <el-dialog
      :title="title"
      :visible.sync="prcopen"
      width="500px"
      height="auto"
      append-to-body
      style="text-align: center"
    >
      <img :src="prc" />
    </el-dialog>
  </div>
</template>

<script>
import {
  listDboxSwitch,
  getDboxSwitch,
  delDboxSwitch,
  addDboxSwitch,
  updateDboxSwitch,
} from "@/api/iot/dboxSwitch";
import { uploadPath } from "@/api/iot/storage";
import { getToken } from "@/utils/auth";

export default {
  name: "DboxSwitch",
  dicts: ["iot_air_switch_type", "iot_air_switch_status", "iot_is_enable"],
  data() {
    return {
      uploadPath,
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
      // 配电箱空开素材表格数据
      dboxSwitchList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      prcopen: false,
      prc: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        switchType: null,
        switchStatus: null,
        imgUrl: null,
        dboxType: null,
        dboxId: null,
        enable: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    const dboxId = this.$route.query && this.$route.query.dboxId;
    this.queryParams.dboxId = dboxId;
    console.log("this.queryParams", this.queryParams, this.$route.query);
    this.getList();
  },
  computed: {
    headers() {
      return {
        Authorization: "Bearer " + getToken(),
        // 'X-Litemall-Admin-Token': getToken()
      };
    },
  },
  methods: {
    handleprc(v) {
      this.title = "大图预览";
      this.prcopen = true;
      this.prc =  v;
    },
    uploadimgUrl: function (response) {
      if(response.url.startsWith("http")){
        this.form.imgUrl = response.url;
      }else{
        this.form.imgUrl = process.env.VUE_APP_BASE_API + response.url;
      }
    },
    /** 查询配电箱空开素材列表 */
    getList() {
      this.loading = true;
      listDboxSwitch(this.queryParams).then((response) => {
        this.dboxSwitchList = response.rows;
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
        name: null,
        switchType: null,
        switchStatus: null,
        imgUrl: null,
        dboxType: null,
        dboxId: null,
        enable: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
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
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加配电箱空开素材";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getDboxSwitch(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改配电箱空开素材";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateDboxSwitch(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.dboxId = this.queryParams.dboxId;
            addDboxSwitch(this.form).then((response) => {
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
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除配电箱空开素材编号为"' + ids + '"的数据项？')
        .then(function () {
          return delDboxSwitch(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "iot/dboxSwitch/export",
        {
          ...this.queryParams,
        },
        `dboxSwitch_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
