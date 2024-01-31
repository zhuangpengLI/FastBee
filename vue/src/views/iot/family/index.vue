<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="家庭名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入家庭名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label-width="120px" label="是否绑定网关" prop="isBind">
        <el-select
          v-model="queryParams.isBind"
          placeholder="全部"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in dict.type.iot_yes_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="所属用户id" prop="belongUserId">
        <el-input
          v-model="queryParams.belongUserId"
          placeholder="请输入所属用户id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
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

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['iot:family:add']"
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
          v-hasPermi="['iot:family:edit']"
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
          v-hasPermi="['iot:family:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['iot:family:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> -->

    <el-table
      v-loading="loading"
      :data="familyList"
      @selection-change="handleSelectionChange"
    >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <!-- <el-table-column label="id" align="center" prop="familyId" /> -->
      <el-table-column label="序号" align="center" prop="index" width="80" />
      <el-table-column label="头像">
        <template slot-scope="scope">
          <img
            :src="scope.row.avatarUrl"
            class="img-circle"
            width="40px"
            height="40px"
          />
        </template>
      </el-table-column>
      <!-- <el-table-column label="头像">
        <template slot-scope="scope">
          <img src="http://profile-avatar.csdnimg.cn/b997d7fc85134d8b89d188260c1b8317_tool_player.jpg" class="img-circle" width="40px" height="40px"  />
        </template>
      </el-table-column> -->
      <el-table-column label="家庭名称" align="center" prop="name" />
      <el-table-column label="位置" align="center" prop="position" />
      <el-table-column label="房间数" align="center" prop="countRoom" />
      <el-table-column label="设备数" align="center" prop="countDevice" />
      <el-table-column label="场景数" align="center" prop="countScene" />
      <el-table-column label="成员数" align="center" prop="countUser" />
      <el-table-column label="是否绑定网关" align="center" prop="isBind">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.iot_yes_no" :value="scope.row.isBind" />
        </template>
      </el-table-column>
      <el-table-column label="管理员" align="center" prop="nickName" />
      <el-table-column label="管理员手机号" align="center" prop="phonenumber" />
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
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        width="200"
      >
        <template slot-scope="scope">
          <el-button
            size="small"
            type="warning"
            style="padding: 5px"
            icon="el-icon-search"
            @click="handleViewRoom(scope.row.familyId)"
            v-hasPermi="['iot:family:room:query']"
            >查看房间</el-button
          >
          <el-button
            size="small"
            type="success"
            style="padding: 5px"
            icon="el-icon-search"
            @click="handleViewUser(scope.row.familyId)"
            v-hasPermi="['iot:family:user:query']"
            >查看成员</el-button
          >
        </template>
        <!-- <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['iot:family:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['iot:family:remove']"
          >删除</el-button>
        </template> -->
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!--查看房间对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <div>未分配设备数量：{{ this.countDevice }}</div>
      <el-table
        v-loading="loading"
        :data="roomList"
        @selection-change="handleSelectionChange"
      >
        <el-table-column label="序号" align="center" prop="index" />
        <el-table-column label="房间名称" align="center" prop="name" />
        <el-table-column label="设备数量" align="center" prop="countDevice" />
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
        v-show="roomtotal > 0"
        :total="roomtotal"
        :page.sync="query.pageNum"
        :limit.sync="query.pageSize"
        @pagination="handleViewRoom"
      />
    </el-dialog>
    <!--查看成员对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="useropen"
      width="900px"
      append-to-body
    >
      <el-table
        v-loading="loading"
        :data="userList"
        @selection-change="handleSelectionChange"
      >
        <el-table-column label="序号" align="center" prop="index" />
        <el-table-column label="成员名称" align="center" prop="nickName" />
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
        v-show="usertotal > 0"
        :total="usertotal"
        :page.sync="query1.pageNum"
        :limit.sync="query1.pageSize"
        @pagination="handleViewUser"
      />
    </el-dialog>
  </div>
</template>

<script>
import {
  listFamily,
  listRoom,
  countNotInRomm,
  userList,
  delFamily,
  addFamily,
  updateFamily,
} from "@/api/iot/family";

export default {
  name: "Family",
  dicts: ["iot_is_enable", "iot_yes_no", "family_user_role", "user_identity"],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedRoom: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 家庭管理表格数据
      familyList: [],
      // 房间表格数据
      roomList: [],
      // 成员数据
      userList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      useropen: false,
      // 备注时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        isBind: null,
        belongUserId: null,
        createTime: null,
      },
      query: {
        pageNum: 1,
        pageSize: 10,
      },
      query1: {
        pageNum: 1,
        pageSize: 10,
      },
      countNotInRomm: {},
      familyId: 0,
      roomtotal: 0,
      usertotal: 0,
      countDevice: 0,
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
    /** 查询家庭管理列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && "" != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] =
          this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listFamily(this.queryParams).then((response) => {
        this.familyList = response.rows;
        this.familyList.map((item, index) => {
          item.index =
            (this.queryParams.pageNum - 1) * this.queryParams.pageSize +
            index +
            1;
        });
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.useropen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        familyId: null,
        name: null,
        avatarUrl: null,
        position: null,
        isEnableJoinAuth: null,
        isBind: null,
        gatewaySn: null,
        belongUserId: null,
        createUserId: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        delFlag: null,
      };
      this.roomList = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.familyId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加家庭管理";
    },
    /** 查看房间 */
    handleViewRoom(row) {
      this.reset();
      this.query.familyId = row || this.familyId;
      this.familyId = row;
      listRoom(this.query).then((response) => {
        this.roomList = response.rows;
        this.roomList.map((item, index) => {
          item.index =
            (this.query.pageNum - 1) * this.query.pageSize + index + 1;
        });
        console.log(this.roomList);
        this.roomtotal = response.total;
        this.open = true;
        this.title = "查看房间";
      });
      this.countNotInRomm.familyId = row || this.familyId;
      countNotInRomm(this.countNotInRomm).then((response) => {
        this.countDevice = response.data.countDevice;
      });
    },
    /** 查看成员 */
    handleViewUser(row) {
      this.reset();
      this.query1.familyId = row || this.familyId;
      this.familyId = row;
      userList(this.query1).then((response) => {
        this.userList = response.rows;
        this.userList.map((item, index) => {
          item.index =
            (this.query1.pageNum - 1) * this.query1.pageSize + index + 1;
        });
        console.log(this.userList);
        this.usertotal = response.total;
        this.useropen = true;
        this.title = "查看成员";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.form.roomList = this.roomList;
          if (this.form.familyId != null) {
            updateFamily(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFamily(this.form).then((response) => {
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
      const familyIds = row.familyId || this.ids;
      this.$modal
        .confirm('是否确认删除家庭管理编号为"' + familyIds + '"的数据项？')
        .then(function () {
          return delFamily(familyIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 房间序号 */
    rowRoomIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 房间添加按钮操作 */
    handleAddRoom() {
      let obj = {};
      obj.name = "";
      obj.roomOrder = "";
      obj.remark = "";
      this.roomList.push(obj);
    },
    /** 房间删除按钮操作 */
    handleDeleteRoom() {
      if (this.checkedRoom.length == 0) {
        this.$modal.msgError("请先选择要删除的房间数据");
      } else {
        const roomList = this.roomList;
        const checkedRoom = this.checkedRoom;
        this.roomList = roomList.filter(function (item) {
          return checkedRoom.indexOf(item.index) == -1;
        });
      }
    },
    /** 复选框选中数据 */
    handleRoomSelectionChange(selection) {
      this.checkedRoom = selection.map((item) => item.index);
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "iot/family/export",
        {
          ...this.queryParams,
        },
        `family_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
