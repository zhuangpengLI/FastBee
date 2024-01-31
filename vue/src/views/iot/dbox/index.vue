<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="配电箱型号" prop="dboxType">
        <el-input
          v-model="queryParams.dboxType"
          placeholder="请输入配电箱型号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="配电箱名称" prop="dboxName">
        <el-input
          v-model="queryParams.dboxName"
          placeholder="请输入配电箱名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否启用 0否1是" prop="enable">
        <el-select v-model="queryParams.enable" placeholder="请选择是否启用 0否1是" clearable size="small">
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
          v-hasPermi="['iot:dbox:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          @click="handledboxSwitch"
          v-hasPermi="['iot:dbox:Switch']"
        >空开素材</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['iot:dbox:edit']"
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
          v-hasPermi="['iot:dbox:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['iot:dbox:export']"
        >导出</el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dboxList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="配电箱型号" align="center" prop="dboxType" />
      <el-table-column label="配电箱背景图">
        <template slot-scope="scope">
          <img
            :src="scope.row.bgImgUrl"
          />
        </template>
      </el-table-column>
      <el-table-column label="背景线条图片">
        <template slot-scope="scope">
          <img
            :src="scope.row.stringImgUrl"
          />
        </template>
      </el-table-column>
      <!-- <el-table-column label="配电箱背景图" align="center" prop="bgImgUrl" /> -->
      <!-- <el-table-column label="背景线条图片" align="center" prop="stringImgUrl" /> -->
      <el-table-column label="配电箱名称" align="center" prop="dboxName" />
      <el-table-column label="是否启用 0否1是" align="center" prop="enable">
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
            v-hasPermi="['iot:dbox:edit']"
          >修改</el-button>
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handledboxSwitch(scope.row)"
            v-hasPermi="['iot:dbox:Switch']"
            >配电箱素材</el-button
          >-->
          <el-button 
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['iot:dbox:remove']"
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

    <!-- 添加或修改配电箱配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="配电箱型号" prop="dboxType">
          <el-input v-model="form.dboxType" placeholder="请输入配电箱型号" />
        </el-form-item>
        <el-form-item label="配电箱名称" prop="dboxName">
          <el-input v-model="form.dboxName" placeholder="请输入配电箱名称" />
        </el-form-item>
        <el-form-item label="是否启用 0否1是" prop="enable">
          <el-select v-model="form.enable" placeholder="请选择是否启用 0否1是">
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
        <el-form-item label="配电箱背景图">
          <el-upload
            :headers="headers"
            :action="uploadPath"
            :show-file-list="false"
            :on-success="uploadbgImgUrl"
            class="avatar-uploader"
            accept=".jpg,.jpeg,.png,.gif"
          >
            <img v-if="form.bgImgUrl" :src="form.bgImgUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
          <div style="color: #f56c6c">图片底部透明，边框贴边留白</div>
        </el-form-item>
        <el-form-item label="背景线条图片">
          <el-upload
            :headers="headers"
            :action="uploadPath"
            :show-file-list="false"
            :on-success="uploadstringImgUrl"
            class="avatar-uploader"
            accept=".jpg,.jpeg,.png,.gif"
          >
            <img v-if="form.stringImgUrl" :src="form.stringImgUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
          <div style="color: #f56c6c">图片底部透明，尺寸与上图保持一致</div>
        </el-form-item>
        <!-- <el-divider content-position="center">配电箱空开素材信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddDboxSwitch">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteDboxSwitch">删除</el-button>
          </el-col>
        </el-row> -->
        <!-- <el-table :data="dboxSwitchList" :row-class-name="rowDboxSwitchIndex" @selection-change="handleDboxSwitchSelectionChange" ref="dboxSwitch">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="素材名称" prop="name">
            <template slot-scope="scope">
              <el-input v-model="scope.row.name" placeholder="请输入素材名称" />
            </template>
          </el-table-column>
          <el-table-column label="空开类型" prop="switchType">
            <template slot-scope="scope">
              <el-input v-model="scope.row.switchType" placeholder="请输入空开类型" />
            </template>
          </el-table-column>
          <el-table-column label="空开状态 0=正常(默认合闸)， 1=跳闸 分闸   2=欠压" prop="switchStatus">
            <template slot-scope="scope">
              <el-input v-model="scope.row.switchStatus" placeholder="请输入空开状态 0=正常(默认合闸)， 1=跳闸 分闸   2=欠压" />
            </template>
          </el-table-column>
          <el-table-column label="空开状态图" prop="imgUrl">
            <template slot-scope="scope">
              <el-input v-model="scope.row.imgUrl" placeholder="请输入空开状态图" />
            </template>
          </el-table-column>
          <el-table-column label="配电箱型号" prop="dboxType">
            <template slot-scope="scope">
              <el-input v-model="scope.row.dboxType" placeholder="请输入配电箱型号" />
            </template>
          </el-table-column>
          <el-table-column label="是否启用 0否1是" prop="enable">
            <template slot-scope="scope">
              <el-input v-model="scope.row.enable" placeholder="请输入是否启用 0否1是" />
            </template>
          </el-table-column>
          <el-table-column label="备注" prop="remark">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remark" placeholder="请输入备注" />
            </template>
          </el-table-column>
        </el-table> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDbox, getDbox, delDbox, addDbox, updateDbox } from "@/api/iot/dbox";
import { uploadPath } from '@/api/iot/storage'
import { getToken } from '@/utils/auth'

export default {
  name: "Dbox",
  dicts: ['iot_is_enable'],
  data() {
    return {
      uploadPath,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedDboxSwitch: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 配电箱配置表格数据
      dboxList: [],
      // 配电箱空开素材表格数据
      dboxSwitchList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dboxType: null,
        bgImgUrl: null,
        stringImgUrl: null,
        dboxName: null,
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
  computed:{
    headers() {
      return {
        Authorization: "Bearer " + getToken(),
        // 'X-Litemall-Admin-Token': getToken()
      }
    },
  },
  methods: {
    /** 查询配电箱配置列表 */
    getList() {
      this.loading = true;
      listDbox(this.queryParams).then(response => {
        this.dboxList = response.rows;
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
        dboxType: null,
        bgImgUrl: null,
        stringImgUrl: null,
        dboxName: null,
        enable: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.dboxSwitchList = [];
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
      this.title = "添加配电箱配置";
    },
     /** 修改按钮操作 */
    handledboxSwitch(row) {
      // let dboxId = 0;
      // if (row != 0) {
      //   dboxId = row.id || this.ids;
      // }
      this.$router.push({
        path: "/iot/dboxSwitch",
        query: {
          // dboxId: dboxId,
        },
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDbox(id).then(response => {
        this.form = response.data;
        this.dboxSwitchList = response.data.dboxSwitchList;
        this.open = true;
        this.title = "修改配电箱配置";
      });
    },
    uploadbgImgUrl: function(response) {
      if(response.url.startsWith("http")){
        this.form.bgImgUrl = response.url;
      }else{
        this.form.bgImgUrl = process.env.VUE_APP_BASE_API+ response.url
      }
    },
    uploadstringImgUrl: function(response) {
      if(response.url.startsWith("http")){
        this.form.stringImgUrl = response.url;
      }else{
        this.form.stringImgUrl = process.env.VUE_APP_BASE_API+ response.url
      }
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.dboxSwitchList = this.dboxSwitchList;
          if (this.form.id != null) {
            updateDbox(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDbox(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除配电箱配置编号为"' + ids + '"的数据项？').then(function() {
        return delDbox(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
	/** 配电箱空开素材序号 */
    rowDboxSwitchIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 配电箱空开素材添加按钮操作 */
    handleAddDboxSwitch() {
      let obj = {};
      obj.name = "";
      obj.switchType = "";
      obj.switchStatus = "";
      obj.imgUrl = "";
      obj.dboxType = "";
      obj.enable = "";
      obj.remark = "";
      this.dboxSwitchList.push(obj);
    },
    /** 配电箱空开素材删除按钮操作 */
    handleDeleteDboxSwitch() {
      if (this.checkedDboxSwitch.length == 0) {
        this.$modal.msgError("请先选择要删除的配电箱空开素材数据");
      } else {
        const dboxSwitchList = this.dboxSwitchList;
        const checkedDboxSwitch = this.checkedDboxSwitch;
        this.dboxSwitchList = dboxSwitchList.filter(function(item) {
          return checkedDboxSwitch.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleDboxSwitchSelectionChange(selection) {
      this.checkedDboxSwitch = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('iot/dbox/export', {
        ...this.queryParams
      }, `dbox_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
