<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="配电名称" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="配电名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="SN码" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="SN码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="在线状态" prop="categoryName">
        <el-input
          v-model="queryParams.categoryName"
          placeholder="请输入分类名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分类名称" prop="categoryName">
        <el-input
          v-model="queryParams.categoryName"
          placeholder="请输入分类名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="显示顺序" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入显示顺序"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="状态" prop="enabled">
        <el-select
          v-model="queryParams.enabled"
          placeholder="请选择状态"
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
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['iot:helpCenter:add']"
        >新增</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['iot:helpCenter:edit']"
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
          v-hasPermi="['iot:helpCenter:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['iot:helpCenter:export']"
        >导出</el-button>
      </el-col> -->
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="helpCenterList"
      @selection-change="handleSelectionChange"
    >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <!-- <el-table-column label="id" align="center" prop="id" /> -->
      <el-table-column label="配电箱名称" align="center" prop="deviceName" />
      <!-- <el-table-column label="内容" align="center" prop="content" /> -->
      <!-- <el-table-column label="分类ID" align="center" prop="categoryId" /> -->
      <el-table-column label="SN" align="center" prop="serialNumber" />
      <!-- <el-table-column label="显示顺序" align="center" prop="orderNum" /> -->
      <el-table-column label="在线状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.iot_device_status"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column label="检测版状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.iot_device_status"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column label="绑定状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.iot_device_status"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="绑定家庭"
        align="center"
        prop="createTime"
        width="180"
      >
        <template slot-scope="scope">
          <div>所属家庭：{{ scope.row.belongFamilyName }}</div>
          <!-- <div>所属房间：{{ scope.row.belongRoomName }}</div> -->
          <div>管理员：{{ scope.row.belongUserName }}</div>
        </template>
      </el-table-column>
      <el-table-column label="位置" align="center" prop="networkAddress" />
      <el-table-column
        label="绑定时间"
        align="center"
        prop="createTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.activeTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-data"
            @click="handleenergy(scope.row)"
            v-hasPermi="['iot:powerDistribution:energy']"
            >耗能信息</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-date"
            @click="handledate(scope.row)"
            v-hasPermi="['iot:powerDistribution:date']"
            >设备信息</el-button
          ><el-button
            size="mini"
            type="text"
            icon="el-icon-picture-outline"
            @click="handlepicture(scope.row)"
            v-hasPermi="['iot:powerDistribution:picture']"
            >查看配电</el-button
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
    <!--查看设备对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="deviceopen"
      width="900px"
      append-to-body
    >
      <el-table
        v-loading="loading"
        :data="deviceList"
        @selection-change="handleSelectionChange"
      >
        <el-table-column label="序号" align="center" prop="index" />
        <el-table-column label="设备名称" align="center" prop="deviceName" />
        <el-table-column label="类型" align="center" prop="phonenumber" />
        <el-table-column label="检测版状态" align="center" prop="status">
          <template slot-scope="scope">
            <dict-tag
              :options="dict.type.iot_device_status"
              :value="scope.row.status"
            />
          </template>
        </el-table-column>
        <el-table-column label="所属配电箱" align="center" prop="productName" />
        <!--1成员2.管理员 -->
        <!-- <el-table-column label="身份" align="center" prop="userIdentity">
          <template slot-scope="scope">
            <dict-tag
              :options="dict.type.user_identity"
              :value="scope.row.userIdentity"
            />
          </template>
        </el-table-column> -->
        <!--1成员2.管理员 3.创建者-->
      </el-table>

      <pagination
        v-show="devicetotal > 0"
        :total="devicetotal"
        :page.sync="query1.pageNum"
        :limit.sync="query1.pageSize"
        @pagination="handledate"
      />
    </el-dialog>
    <!-- 添加或修改帮助中心内容对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="内容">
          <editor v-model="form.content" :min-height="192" />
        </el-form-item>
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="form.categoryName" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入显示顺序" />
        </el-form-item>
        <el-form-item label="状态" prop="enabled">
          <el-select v-model="form.enabled" placeholder="请选择状态">
            <el-option
              v-for="dict in dict.type.iot_is_enable"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      :title="title"
      :visible.sync="lineopen"
      width="900px"
      append-to-body
    >
      <el-radio-group
        v-model="tabPosition"
        style="margin-right: 30px"
        @change="changedata"
      >
        <el-radio-button label="1">今日</el-radio-button>
        <el-radio-button label="4">最近一周</el-radio-button>
        <el-radio-button label="3">最近一年</el-radio-button>
      </el-radio-group>
      <div
        ref="linestatsChart"
        style="height: 275px; margin: 20px 0 40px 0"
      ></div>
    </el-dialog>
    <el-dialog
      :title="title"
      :visible.sync="pictureopen"
      width="900px"
      append-to-body
    >
      <img :src="this.img" alt="" />
    </el-dialog>
  </div>
</template>

<script>
import {
  gatewayDbox,
  deviceList,
  energyData,
  airSwitchData,
  getHelpCenter,
  delHelpCenter,
  addHelpCenter,
  updateHelpCenter,
} from "@/api/iot/powerDistribution";
import * as echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme

export default {
  name: "HelpCenter",
  dicts: ["iot_is_enable", "iot_device_status"],
  data() {
    return {
      tabPosition: 1,
      lineChartDatax: [],
      lineChartDatay: [],
      classDate: "",
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
      // 帮助中心内容表格数据
      helpCenterList: [],
      deviceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      deviceopen: false,
      pictureopen: false,
      lineopen: false,
      devicetotal: 0,
      query1: {
        pageNum: 1,
        pageSize: 10,
      },
      picture: {},
      img: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        content: null,
        categoryId: null,
        categoryName: null,
        orderNum: null,
        enabled: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [{ required: true, message: "标题不能为空", trigger: "blur" }],
        content: [{ required: true, message: "内容不能为空", trigger: "blur" }],
        categoryId: [
          { required: true, message: "分类ID不能为空", trigger: "change" },
        ],
        categoryName: [
          { required: true, message: "分类名称不能为空", trigger: "blur" },
        ],
        orderNum: [
          { required: true, message: "显示顺序不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      gatewayDbox(this.queryParams).then((response) => {
        this.helpCenterList = response.rows;
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
        title: null,
        content: null,
        categoryId: null,
        categoryName: null,
        orderNum: null,
        enabled: null,
        delFlag: null,
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
      this.title = "添加帮助中心内容";
    },
    initChart() {
      let myChart = echarts.init(this.$refs.linestatsChart);
      var option;
      option = {
        xAxis: {
          data: this.lineChartDatax,
          boundaryGap: false,
          axisTick: {
            show: false,
          },
        },
        grid: {
          left: 10,
          right: 10,
          bottom: 20,
          top: 30,
          containLabel: true,
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross",
          },
          padding: [5, 10],
        },
        yAxis: {
          name: "单位（度）",
          axisTick: {
            show: false,
          },
        },
        legend: {
          data: ["耗电量"],
        },
        series: [
          {
            name: "耗电量",
            itemStyle: {
              normal: {
                color: "#FF005A",
                lineStyle: {
                  color: "#FF005A",
                  width: 2,
                },
              },
            },
            smooth: true,
            type: "line",
            data: this.lineChartDatay,
            animationDuration: 2800,
            animationEasing: "cubicInOut",
          },
        ],
      };
      option && myChart.setOption(option);
    },
    changedata(v) {
      console.log("changedata", v);
      this.lineChartDatax = [];
      this.lineChartDatay = [];
      this.energydata(v);
    },
    energydata(v) {
      const energydata = {};
      energydata.deviceId = this.deviceId;
      energydata.type = v;
      energydata.unit = 2;
      energyData(energydata).then((response) => {
        // response.data.list
        if(response.code!=200){
          this.$modal.alertError(response.msg);
          return;
        }
        const deviceList = response.data.list;

        deviceList.map((item, index) => {
          this.lineChartDatay.push(item.data);
          this.lineChartDatax.push(item.date);
        });
        this.$nextTick(() => {
          this.initChart();
        });
        console.log(response);
      });
    },
    handleenergy(row) {
      this.lineChartDatax = [];
      this.lineChartDatay = [];
      this.tabPosition = 1;
      this.lineopen = true;
      this.title = "耗能分析";
      this.deviceId = row.deviceId;
      this.energydata(this.tabPosition);
    },
    //查看配电
    handlepicture(row) {
      this.reset();
      // /prod-api/iot/gatewayDbox/airSwitchData
      this.picture.deviceId = row.deviceId || this.deviceId;
      airSwitchData(this.picture).then((response) => {
        console.log(
          "process.env.VUE_APP_BASE_API",
          process.env.VUE_APP_BASE_API
        );
        const bgsrc = "http://iot.com" + response.data.dbox.bgImgUrl;
        const src1 =
          "http://iot.com" +
          response.data.jcbs[0].airSwitchs[0].dDboxSwitch.imgUrl;
        const src2 =
          "http://iot.com" +
          response.data.jcbs[0].airSwitchs[1].dDboxSwitch.imgUrl;
        const src3 =
          "http://iot.com" +
          response.data.jcbs[1].airSwitchs[0].dDboxSwitch.imgUrl;
        const src4 =
          "http://iot.com" +
          response.data.jcbs[1].airSwitchs[1].dDboxSwitch.imgUrl;
        const src5 =
          "http://iot.com" +
          response.data.jcbs[1].airSwitchs[2].dDboxSwitch.imgUrl;
        const src6 =
          "http://iot.com" +
          response.data.jcbs[2].airSwitchs[0].dDboxSwitch.imgUrl;
        const src7 =
          "http://iot.com" +
          response.data.jcbs[2].airSwitchs[1].dDboxSwitch.imgUrl;
        const src8 =
          "http://iot.com" +
          response.data.jcbs[2].airSwitchs[2].dDboxSwitch.imgUrl;
        const src9 =
          "http://iot.com" +
          response.data.jcbs[2].airSwitchs[3].dDboxSwitch.imgUrl;
        const src10 =
          "http://iot.com" +
          response.data.jcbs[3].airSwitchs[0].dDboxSwitch.imgUrl;
        const src11 =
          "http://iot.com" +
          response.data.jcbs[3].airSwitchs[1].dDboxSwitch.imgUrl;
        const src12 =
          "http://iot.com" +
          response.data.jcbs[3].airSwitchs[2].dDboxSwitch.imgUrl;
        const src13 =
          "http://iot.com" +
          response.data.jcbs[3].airSwitchs[3].dDboxSwitch.imgUrl;
        const src14 =
          "http://iot.com" +
          response.data.jcbs[4].airSwitchs[0].dDboxSwitch.imgUrl;
        const src15 =
          "http://iot.com" +
          response.data.jcbs[4].airSwitchs[1].dDboxSwitch.imgUrl;
        const src16 =
          "http://iot.com" +
          response.data.jcbs[4].airSwitchs[2].dDboxSwitch.imgUrl;
        const src17 =
          "http://iot.com" +
          response.data.jcbs[4].airSwitchs[3].dDboxSwitch.imgUrl;
        console.log(bgsrc, src1);
        let canvas = document.createElement("canvas");
        canvas.width = 860;
        canvas.height = 450;
        let context = canvas.getContext("2d");
        context.rect(0, 0, canvas.width, canvas.height);
        let bgImg = new Image();
        bgImg.src = bgsrc; // 背景图的url
        bgImg.crossOrigin = "Anonymous";
        bgImg.onload = () => {
          context.drawImage(bgImg, 0, 0, 860, 450);
          let img = new Image();
          img.src = src1; // 需要合进去的图片url
          img.crossOrigin = "Anonymous";
          img.onload = () => {
            context.drawImage(img, 67, 144, 67, 255);
            let img2 = new Image();
            img2.src = src2; // 需要合进去的图片url
            img2.crossOrigin = "Anonymous";
            img2.onload = () => {
              context.drawImage(img2, 134, 144, 67, 255);
              let img3 = new Image();
              img3.src = src3; // 需要合进去的图片url
              img3.crossOrigin = "Anonymous";
              img3.onload = () => {
                context.drawImage(img3, 205, 144, 67, 255);
                let img4 = new Image();
                img4.src = src4; // 需要合进去的图片url
                img4.crossOrigin = "Anonymous";
                img4.onload = () => {
                  context.drawImage(img4, 272, 144, 35, 255);
                  let img5 = new Image();
                  img5.src = src5; // 需要合进去的图片url
                  img5.crossOrigin = "Anonymous";
                  img5.onload = () => {
                    context.drawImage(img5, 305, 144, 35, 255);
                    let img6 = new Image();
                    img6.src = src6; // 需要合进去的图片url
                    img6.crossOrigin = "Anonymous";
                    img6.onload = () => {
                      context.drawImage(img6, 348, 144, 36, 255);
                      let img7 = new Image();
                      img7.src = src7; // 需要合进去的图片url
                      img7.crossOrigin = "Anonymous";
                      img7.onload = () => {
                        context.drawImage(img7, 383, 144, 36, 255);
                        let img8 = new Image();
                        img8.src = src8; // 需要合进去的图片url
                        img8.crossOrigin = "Anonymous";
                        img8.onload = () => {
                          context.drawImage(img8, 419, 144, 36, 255);
                          let img9 = new Image();
                          img9.src = src9; // 需要合进去的图片url
                          img9.crossOrigin = "Anonymous";
                          img9.onload = () => {
                            context.drawImage(img9, 454, 144, 36, 255);
                            let img10 = new Image();
                            img10.src = src10; // 需要合进去的图片url
                            img10.crossOrigin = "Anonymous";
                            img10.onload = () => {
                              context.drawImage(img10, 495, 144, 36, 255);
                              let img11 = new Image();
                              img11.src = src11; // 需要合进去的图片url
                              img11.crossOrigin = "Anonymous";
                              img11.onload = () => {
                                context.drawImage(img11, 531, 144, 36, 255);
                                let img12 = new Image();
                                img12.src = src12; // 需要合进去的图片url
                                img12.crossOrigin = "Anonymous";
                                img12.onload = () => {
                                  context.drawImage(img12, 565, 144, 36, 255);
                                  let img13 = new Image();
                                  img13.src = src13; // 需要合进去的图片url
                                  img13.crossOrigin = "Anonymous";
                                  img13.onload = () => {
                                    context.drawImage(img13, 600, 144, 36, 255);
                                    let img14 = new Image();
                                    img14.src = src14; // 需要合进去的图片url
                                    img14.crossOrigin = "Anonymous";
                                    img14.onload = () => {
                                      context.drawImage(
                                        img14,
                                        645,
                                        144,
                                        36,
                                        255
                                      );
                                      let img15 = new Image();
                                      img15.src = src15; // 需要合进去的图片url
                                      img15.crossOrigin = "Anonymous";
                                      img15.onload = () => {
                                        context.drawImage(
                                          img15,
                                          680,
                                          144,
                                          36,
                                          255
                                        );
                                        let img16 = new Image();
                                        img16.src = src16; // 需要合进去的图片url
                                        img16.crossOrigin = "Anonymous";
                                        img16.onload = () => {
                                          context.drawImage(
                                            img16,
                                            716,
                                            144,
                                            36,
                                            255
                                          );
                                          let img17 = new Image();
                                          img17.src = src17; // 需要合进去的图片url
                                          img17.crossOrigin = "Anonymous";
                                          img17.onload = () => {
                                            context.drawImage(
                                              img17,
                                              752,
                                              144,
                                              36,
                                              255
                                            );
                                            // 这个就是合成后的图片base65
                                            let base64 =
                                              canvas.toDataURL("image/png");
                                            //   console.log(base64);
                                            const q = {};
                                            q.base64 = base64;
                                            this.img = base64;
                                          };
                                        };
                                      };
                                    };
                                  };
                                };
                              };
                            };
                          };
                        };
                      };
                    };
                  };
                };
              };
            };
          };
        };
        // console.log(response, this.img);
        this.pictureopen = true;
        this.title = "查看配电";
      });
    },
    /**设备信息 */
    handledate(row) {
      this.reset();
      this.query1.deviceId = row.deviceId || this.deviceId;
      this.deviceId = row.deviceId||this.deviceId;
      deviceList(this.query1).then((response) => {
        this.deviceList = response.rows;
        this.deviceList.map((item, index) => {
          item.index =
            (this.query1.pageNum - 1) * this.query1.pageSize + index + 1;
        });
        console.log(this.userList);
        this.devicetotal = response.total;
        this.deviceopen = true;
        this.title = "设备列表";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateHelpCenter(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addHelpCenter(this.form).then((response) => {
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
        .confirm('是否确认删除帮助中心内容编号为"' + ids + '"的数据项？')
        .then(function () {
          return delHelpCenter(ids);
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
        "iot/helpCenter/export",
        {
          ...this.queryParams,
        },
        `helpCenter_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
