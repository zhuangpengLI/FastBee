<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="选择日期" prop="addTime">
        <el-date-picker
          clearable
          size="small"
          v-model="queryParams.addTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择反馈日期"
        >
        </el-date-picker>
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
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['iot:feedback:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['iot:feedback:edit']"
          >修改</el-button
        >
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['iot:feedback:remove']"
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
          v-hasPermi="['iot:feedback:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar> -->
    </el-row>

    <el-table
      v-loading="loading"
      :data="feedbackList"
      @selection-change="handleSelectionChange"
      :default-sort="defaultSort"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="用户名称" align="center" prop="nickName" />
      <el-table-column label="手机号" align="center" prop="mobile" />
      <el-table-column
        label="来源 1 IOS  2 Android"
        align="center"
        prop="source"
      />
      <el-table-column label="反馈内容" align="center" prop="content" />
      <!-- <el-table-column
        label="图片地址列表，采用JSON数组格式"
        align="center"
        prop="picUrls"
      /> -->
      <el-table-column label="图片" align="center">
        <template slot-scope="scope">
          <img
            @click="handleprc(JSON.parse (scope.row.picUrls))"
            :src="JSON.parse (scope.row.picUrls)[0]"
          />
        </template>
      </el-table-column>
      <!-- <el-table-column label="回复内容" align="center" prop="reply" />
      <el-table-column
        label="回复时间"
        align="center"
        prop="replyTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.replyTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column> -->
      <el-table-column
        label="反馈时间"
        align="center"
        prop="addTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.addTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['iot:feedback:edit']"
            >修改</el-button
          > -->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['iot:feedback:remove']"
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

    <!-- 添加或修改意见反馈对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户表的用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户表的用户ID" />
        </el-form-item>
        <el-form-item label="用户名称" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名称" />
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="反馈标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入反馈标题" />
        </el-form-item>
        <el-form-item label="反馈内容">
          <editor v-model="form.content" :min-height="192" />
        </el-form-item>
        <el-form-item label="是否含有图片" prop="hasPicture">
          <el-input
            v-model="form.hasPicture"
            placeholder="请输入是否含有图片"
          />
        </el-form-item>
        <el-form-item label="图片地址列表，采用JSON数组格式" prop="picUrls">
          <el-input
            v-model="form.picUrls"
            type="textarea"
            placeholder="请输入内容"
          />
        </el-form-item>
        <!-- <el-form-item label="回复内容" prop="reply">
          <el-input v-model="form.reply" placeholder="请输入回复内容" />
        </el-form-item>
        <el-form-item label="回复时间" prop="replyTime">
          <el-date-picker
            clearable
            size="small"
            v-model="form.replyTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择回复时间"
          >
          </el-date-picker>
        </el-form-item> -->
        <el-form-item label="创建时间(反馈时间)" prop="addTime">
          <el-date-picker
            clearable
            size="small"
            v-model="form.addTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择创建时间(反馈时间)"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="逻辑删除" prop="deleted">
          <el-input v-model="form.deleted" placeholder="请输入逻辑删除" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      :title="title"
      :visible.sync="prcopen"
      width="500px"
      height="auto"
      append-to-body
      style="text-align: center"
    >
    <div v-for="key in this.prc"
              :key="key"
             >
             <img :src="key" />
    </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listFeedback,
  getFeedback,
  delFeedback,
  addFeedback,
  updateFeedback,
} from "@/api/iot/feedback";

export default {
  name: "Feedback",
  data() {
    return {
      defaultSort: { prop: "addTime", order: "descending" },
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
      // 意见反馈表格数据
      feedbackList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      prcopen: false,
      // 是否显示弹出层
      prc:[],
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        source: null,
        content: null,
        addTime: null,
        orderByColumn: "addTime",
        isAsc: "descending",
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          {
            required: true,
            message: "用户表的用户ID不能为空",
            trigger: "blur",
          },
        ],
        username: [
          { required: true, message: "用户名称不能为空", trigger: "blur" },
        ],
        mobile: [
          { required: true, message: "手机号不能为空", trigger: "blur" },
        ],
        content: [
          { required: true, message: "反馈内容不能为空", trigger: "blur" },
        ],
        status: [
          {
            required: true,
            message: "状态 0未回复 1已回复不能为空",
            trigger: "blur",
          },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handleprc(v) {
      this.title = "多图预览";
      this.prcopen = true;
      this.prc =  v;
    },
    /** 查询意见反馈列表 */
    getList() {
      this.loading = true;
      listFeedback(this.queryParams).then((response) => {
        this.feedbackList = response.rows;
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
        userId: null,
        username: null,
        mobile: null,
        feedType: null,
        source: null,
        title: null,
        content: null,
        status: 0,
        hasPicture: null,
        picUrls: null,
        reply: null,
        replyTime: null,
        addTime: null,
        updateTime: null,
        deleted: null,
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
      this.title = "添加意见反馈";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getFeedback(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改意见反馈";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateFeedback(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFeedback(this.form).then((response) => {
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
        .confirm('是否确认删除意见反馈编号为"' + ids + '"的数据项？')
        .then(function () {
          return delFeedback(ids);
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
        "iot/feedback/export",
        {
          ...this.queryParams,
        },
        `feedback_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
