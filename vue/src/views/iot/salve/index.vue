<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关联的模板id" prop="deviceTempId">
        <el-input
          v-model="queryParams.deviceTempId"
          placeholder="请输入关联的模板id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="从机编号" prop="slaveAddr">
        <el-input
          v-model="queryParams.slaveAddr"
          placeholder="请输入从机编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="${comment}" prop="slaveIndex">
        <el-input
          v-model="queryParams.slaveIndex"
          placeholder="请输入${comment}"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="从机ip地址" prop="slaveIp">
        <el-input
          v-model="queryParams.slaveIp"
          placeholder="请输入从机ip地址"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="从机名称" prop="slaveName">
        <el-input
          v-model="queryParams.slaveName"
          placeholder="请输入从机名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="从机端口" prop="slavePort">
        <el-input
          v-model="queryParams.slavePort"
          placeholder="请输入从机端口"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['iot:salve:add']"
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
          v-hasPermi="['iot:salve:edit']"
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
          v-hasPermi="['iot:salve:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['iot:salve:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="salveList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键id" align="center" prop="id" />
      <el-table-column label="关联的模板id" align="center" prop="deviceTempId" />
      <el-table-column label="从机编号" align="center" prop="slaveAddr" />
      <el-table-column label="${comment}" align="center" prop="slaveIndex" />
      <el-table-column label="从机ip地址" align="center" prop="slaveIp" />
      <el-table-column label="从机名称" align="center" prop="slaveName" />
      <el-table-column label="从机端口" align="center" prop="slavePort" />
      <el-table-column label="状态 0-启动 1-失效" align="center" prop="status" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['iot:salve:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['iot:salve:remove']"
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

    <!-- 添加或修改变量模板设备从机对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联的模板id" prop="deviceTempId">
          <el-input v-model="form.deviceTempId" placeholder="请输入关联的模板id" />
        </el-form-item>
        <el-form-item label="从机编号" prop="slaveAddr">
          <el-input v-model="form.slaveAddr" placeholder="请输入从机编号" />
        </el-form-item>
        <el-form-item label="${comment}" prop="slaveIndex">
          <el-input v-model="form.slaveIndex" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="从机ip地址" prop="slaveIp">
          <el-input v-model="form.slaveIp" placeholder="请输入从机ip地址" />
        </el-form-item>
        <el-form-item label="从机名称" prop="slaveName">
          <el-input v-model="form.slaveName" placeholder="请输入从机名称" />
        </el-form-item>
        <el-form-item label="从机端口" prop="slavePort">
          <el-input v-model="form.slavePort" placeholder="请输入从机端口" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import { listSalve, getSalve, delSalve, addSalve, updateSalve } from "@/api/iot/salve";

export default {
  name: "Salve",
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
      // 变量模板设备从机表格数据
      salveList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceTempId: null,
        slaveAddr: null,
        slaveIndex: null,
        slaveIp: null,
        slaveName: null,
        slavePort: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deviceTempId: [
          { required: true, message: "关联的模板id不能为空", trigger: "blur" }
        ],
        slaveAddr: [
          { required: true, message: "从机编号不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态 0-启动 1-失效不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询变量模板设备从机列表 */
    getList() {
      this.loading = true;
      listSalve(this.queryParams).then(response => {
        this.salveList = response.rows;
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
        deviceTempId: null,
        slaveAddr: null,
        slaveIndex: null,
        slaveIp: null,
        slaveName: null,
        slavePort: null,
        status: 0,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
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
      this.title = "添加变量模板设备从机";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSalve(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改变量模板设备从机";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSalve(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSalve(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除变量模板设备从机编号为"' + ids + '"的数据项？').then(function() {
        return delSalve(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('iot/salve/export', {
        ...this.queryParams
      }, `salve_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
