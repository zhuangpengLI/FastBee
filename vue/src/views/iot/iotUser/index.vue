<template>
  <div class="app-container">
    <el-row :gutter="24">
      <!--用户数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="用户名称" prop="nickName">
            <el-input
              v-model="queryParams.nickName"
              placeholder="请输入用户名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="手机号码" prop="phonenumber">
            <el-input
              v-model="queryParams.phonenumber"
              placeholder="请输入手机号码"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="dateRange"
              size="small"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['system:iot:user:remove']"
            >删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['system:iot:user:export']"
            >导出</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
          <!-- <el-table-column type="selection" width="50" align="center" /> -->
          <!-- <el-table-column type="index" width="50" align="center" /> -->
          <el-table-column label="序号" align="center" prop="index" width="80" />
          <el-table-column label="用户名称" align="center" key="nickName" prop="nickName" v-if="columns[2].visible" :show-overflow-tooltip="true" />
          <el-table-column label="家庭数量" align="center" key="countTotalFamily" prop="countTotalFamily" />
          <el-table-column label="共享家庭" align="center" key="countJoinFamily" prop="countJoinFamily" />
          <el-table-column label="手机号码" align="center" key="phonenumber" prop="phonenumber" v-if="columns[4].visible"  />
          <el-table-column label="最后登录" align="center" prop="loginDate" v-if="columns[6].visible" >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="注册时间" align="center" prop="createTime" v-if="columns[6].visible" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="160"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope" v-if="scope.row.userId !== 1">
              <el-button size="small" type="success" style="padding:5px;" icon="el-icon-search" @click="handleViewRoom(scope.row.userId)" v-hasPermi="['iot:family:user:query']">查看家庭</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system::iotuser:remove']"
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
      </el-col>
    </el-row>
    <!--查看家庭对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="familyopen"
      width="900px"
      append-to-body
    >
      <el-table
        v-loading="loading"
        :data="familyList"
        @selection-change="handleSelectionChange"
      >
        <el-table-column label="序号" align="center" prop="index" />
        <el-table-column label="家庭名称" align="center" prop="familyName" />
        <el-table-column label="手机号" align="center" prop="phonenumber" />
        <el-table-column label="权限" align="center" prop="familyUserRole">
          <template slot-scope="scope">
            <dict-tag
              :options="dict.type.family_user_role"
              :value="scope.row.familyUserRole"
            />
          </template>
        </el-table-column>
        <!--1成员2.管理员 -->
        <el-table-column label="身份" align="center" prop="userIdentity">
          <template slot-scope="scope">
            <dict-tag
              :options="dict.type.user_identity"
              :value="scope.row.userIdentity"
            />
          </template>
        </el-table-column>
        <!--1成员2.管理员 3.创建者-->
        <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          width="180"
        >
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="familytotal > 0"
        :total="familytotal"
        :page.sync="query1.pageNum"
        :limit.sync="query1.pageSize"
        @pagination="handleViewRoom"
      />
    </el-dialog>
  </div>
</template>

<script>
import { listIotUser, delIotUser ,familyList} from "@/api/iot/iotUser";

export default {
  name: "User",
  dicts: ["family_user_role", "user_identity"],
  components: {  },
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
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: "",
      // 部门树选项
      deptOptions: undefined,
      // 是否显示弹出层
      open: false,
      // 部门名称
      deptName: undefined,
      // 默认密码
      initPassword: undefined,
      //家庭数据
      familyList: [],
      // 是否显示弹出层
      familyopen: false,
      query1: {
        pageNum: 1,
        pageSize: 10,
      },
      familyId: 0,
      familytotal: 0,
      userId:0,
      // 日期范围
      dateRange: [],
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `用户编号`, visible: true },
        { key: 1, label: `用户名称`, visible: true },
        { key: 2, label: `用户昵称`, visible: true },
        { key: 3, label: `部门`, visible: true },
        { key: 4, label: `手机号码`, visible: true },
        { key: 5, label: `状态`, visible: true },
        { key: 6, label: `创建时间`, visible: true }
      ],
      // 表单校验
      rules: {
        userName: [
          { required: true, message: "用户名称不能为空", trigger: "blur" },
          { min: 2, max: 20, message: '用户名称长度必须介于 2 和 20 之间', trigger: 'blur' }
        ],
        nickName: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" }
        ],
        password: [
          { required: true, message: "用户密码不能为空", trigger: "blur" },
          { min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间', trigger: 'blur' }
        ],
        email: [
          {
            type: "email",
            message: "'请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        phonenumber: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
     // 取消按钮
    cancel() {
      this.familyopen = false;
      this.reset();
    },
        /** 查看成员 */
    handleViewRoom(row) {
      this.reset();
      this.query1.userId = row || this.userId;
      this.userId = row;
      familyList(this.query1).then((response) => {
        this.familyList = response.rows;
        this.familyList.map((item, index) => {
          item.index =
            (this.query1.pageNum - 1) * this.query1.pageSize + index + 1;
        });
        console.log(this.familyList);
        this.familytotal = response.total;
        this.familyopen = true;
        this.title = "查看家庭";
      });
    },
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listIotUser(this.queryParams).then(response => {
          this.userList = response.rows;
          this.userList.map((item, index) => {
          item.index =
            (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1;
        });
          this.total = response.total;
          this.loading = false;
        }
      );
    },
   
    // 表单重置
    reset() {
      this.form = {
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        email: undefined,
        sex: undefined,
        status: "0",
        remark: undefined,
        postIds: [],
        roleIds: []
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
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    
    /** 删除按钮操作 */
    handleDelete(row) {
      const userIds = row.userId || this.ids;
      this.$modal.confirm('是否确认删除用户编号为"' + userIds + '"的数据项？').then(function() {
        return delIotUser(userIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('iot/user/export', {
        ...this.queryParams
      }, `user_${new Date().getTime()}.xlsx`)
    },
  }
};
</script>