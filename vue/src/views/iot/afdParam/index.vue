<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="语音版本" prop="aver">
        <el-input
          v-model="queryParams.aver"
          placeholder="请输入语音版本"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="语音词类型" prop="wordType">
        <el-select v-model="queryParams.wordType" placeholder="请选择语音词类型" clearable size="small">
          <el-option
            v-for="dict in dict.type.afd_word_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="硬件语音序号" prop="aorder">
        <el-input
          v-model="queryParams.aorder"
          placeholder="请输入硬件语音序号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="支持设备类型" prop="deviceType">
        <el-select v-model="queryParams.deviceType" placeholder="请选择支持设备类型" clearable size="small">
          <el-option
            v-for="dict in dict.type.afd_device_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否启用" prop="enable">
        <el-select v-model="queryParams.enable" placeholder="请选择是否启用" clearable size="small">
          <el-option
            v-for="dict in dict.type.iot_is_enable"
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
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['iot:afdParam:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['iot:afdParam:edit']"
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
          v-hasPermi="['iot:afdParam:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['iot:afdParam:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="afdParamList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="语音词" align="center" prop="name" />
      <el-table-column label="语音版本" align="center" prop="aver" />
      <el-table-column label="语音词类型" align="center" prop="wordType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.afd_word_type" :value="scope.row.wordType"/>
        </template>
      </el-table-column>
      <el-table-column label="硬件语音序号" align="center" prop="aorder" />
      <el-table-column label="支持设备类型" align="center" prop="deviceType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.afd_device_type" :value="scope.row.deviceType"/>
        </template>
      </el-table-column>
      <el-table-column label="是否启用" align="center" prop="enable">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.iot_is_enable" :value="scope.row.enable"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['iot:afdParam:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['iot:afdParam:remove']"
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

    <!-- 添加或修改语音维护对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="语音词" prop="name">
          <el-input v-model="form.name" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="语音版本" prop="aver">
          <el-input v-model="form.aver" placeholder="请输入语音版本" />
        </el-form-item>
        <el-form-item label="语音词类型" prop="wordType">
          <el-select v-model="form.wordType" placeholder="请选择语音词类型">
            <el-option
              v-for="dict in dict.type.afd_word_type"
              :key="dict.value"
              :label="dict.label"
:value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="硬件语音序号" prop="aorder">
          <el-input v-model="form.aorder" placeholder="请输入硬件语音序号" />
        </el-form-item>
        <el-form-item label="支持设备类型" prop="deviceType">
          <el-select v-model="form.deviceType" placeholder="请选择支持设备类型">
            <el-option
              v-for="dict in dict.type.afd_device_type"
              :key="dict.value"
              :label="dict.label"
:value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
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
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listAfdParam, getAfdParam, delAfdParam, addAfdParam, updateAfdParam } from "@/api/iot/afdParam";

export default {
  name: "AfdParam",
  dicts: ['afd_word_type', 'afd_device_type', 'iot_is_enable'],
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
      // 语音维护表格数据
      afdParamList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        aver: null,
        wordType: null,
        aorder: null,
        deviceType: null,
        enable: null,
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
    /** 查询语音维护列表 */
    getList() {
      this.loading = true;
      listAfdParam(this.queryParams).then(response => {
        this.afdParamList = response.rows;
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
        aver: null,
        wordType: null,
        aorder: null,
        deviceType: null,
        enable: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加语音维护";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAfdParam(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改语音维护";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAfdParam(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAfdParam(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除语音维护编号为"' + ids + '"的数据项？').then(function() {
        return delAfdParam(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('iot/afdParam/export', {
        ...this.queryParams
      }, `afdParam_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
