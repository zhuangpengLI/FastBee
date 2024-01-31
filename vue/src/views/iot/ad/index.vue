<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="广告标题" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入广告标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="所广告的商品页面或者活动页面链接地址" prop="link">
        <el-input
          v-model="queryParams.link"
          placeholder="请输入所广告的商品页面或者活动页面链接地址"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="广告位置" prop="position">
        <el-select v-model="queryParams.position" placeholder="请选择广告位置" clearable size="small">
          <el-option
            v-for="dict in dict.type.iot_ad_position"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否启动" prop="enabled">
        <el-select v-model="queryParams.enabled" placeholder="请选择是否启动" clearable size="small">
          <el-option
            v-for="dict in dict.type.iot_is_enable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="addTime">
        <el-date-picker clearable size="small"
          v-model="queryParams.addTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择创建时间">
        </el-date-picker>
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
          v-hasPermi="['iot:ad:add']"
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
          v-hasPermi="['iot:ad:edit']"
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
          v-hasPermi="['iot:ad:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['iot:ad:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="adList" @selection-change="handleSelectionChange">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="广告标题" align="center" prop="name" />
      <el-table-column label="跳转链接" align="center" prop="link" />
      <!-- <el-table-column label="素材" align="center" prop="url" /> -->
      <el-table-column label="广告宣传图片" align="center">
        <template slot-scope="scope">
          <img
            @click="handleprc(scope.row.url)"
            :src="scope.row.url"
          />
        </template>
      </el-table-column>
      <el-table-column label="广告位置" align="center" prop="position">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.iot_ad_position" :value="scope.row.position"/>
        </template>
      </el-table-column>
      <!-- <el-table-column label="广告内容" align="center" prop="content" /> -->
      <el-table-column label="是否启动" align="center" prop="enabled">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.iot_is_enable" :value="scope.row.enabled"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="addTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.addTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['iot:ad:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['iot:ad:remove']"
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

    <!-- 添加或修改广告对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="广告标题" prop="name">
          <el-input v-model="form.name" placeholder="请输入广告标题" />
        </el-form-item>
        <el-form-item label="跳转链接" prop="link">
          <el-input v-model="form.link" placeholder="跳转链接" />
        </el-form-item>
        <el-form-item label="广告位置" prop="position">
          <el-select v-model="form.position" placeholder="请选择广告位置">
            <el-option
              v-for="dict in dict.type.iot_ad_position"
              :key="dict.value"
              :label="dict.label"
:value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="广告宣传图片">
          <el-upload
            :headers="headers"
            :action="uploadPath"
            :show-file-list="false"
            :on-success="uploadimgUrl"
            class="avatar-uploader"
            accept=".jpg,.jpeg,.png,.gif"
          >
            <img v-if="form.url" :src="form.url" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
        </el-form-item>
        <!-- <el-form-item label="广告内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item> -->
        <el-form-item label="是否启动" prop="enabled">
          <el-select v-model="form.enabled" placeholder="请选择是否启动">
            <el-option
              v-for="dict in dict.type.iot_is_enable"
              :key="dict.value"
              :label="dict.label"
:value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间" prop="addTime">
          <el-date-picker clearable size="small"
            v-model="form.addTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择创建时间">
          </el-date-picker>
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
import { listAd, getAd, delAd, addAd, updateAd } from "@/api/iot/ad";
import { uploadPath } from "@/api/iot/storage";
import { getToken } from "@/utils/auth";

export default {
  name: "Ad",
  dicts: ['iot_ad_position', 'iot_is_enable'],
  data() {
    return {
      uploadPath,
      title: "",
      // 是否显示弹出层
      prcopen: false,
      prc: "",
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
      // 广告表格数据
      adList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        link: null,
        url: null,
        position: null,
        content: null,
        enabled: null,
        addTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "广告标题不能为空", trigger: "blur" }
        ],
        // link: [
        //   { required: true, message: "所广告的商品页面或者活动页面链接地址不能为空", trigger: "blur" }
        // ],
        url: [
          { required: true, message: "广告宣传图片不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
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
        this.form.url = response.url;
      }else{
        this.form.url = process.env.VUE_APP_BASE_API + response.url;
      }
    },
    /** 查询广告列表 */
    getList() {
      this.loading = true;
      listAd(this.queryParams).then(response => {
        this.adList = response.rows;
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
        link: null,
        url: null,
        position: null,
        content: null,
        enabled: null,
        addTime: null,
        updateTime: null
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
      this.title = "添加广告";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAd(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改广告";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAd(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAd(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除广告编号为"' + ids + '"的数据项？').then(function() {
        return delAd(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('iot/ad/export', {
        ...this.queryParams
      }, `ad_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
