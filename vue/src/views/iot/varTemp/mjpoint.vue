<template>
  <div style="padding:6px;">

    <el-card style="margin-bottom:10px;">
      <div slot="header">
        <span style="font-weight:bold;">变量模板</span>
        <el-button style="float: right;padding:5px 15px;" size="mini" type="info" @click="goBack">返回</el-button>
      </div>
      <el-form inline ref="form" :model="content" label-width="150px" align="left" style="margin-bottom:-20px;">
        <el-form-item label="模板名称: " size="mini">
          <span>{{ content.templateName }}</span>
        </el-form-item>
        <el-form-item label="采集方式: " size="mini">
          <dict-tag :options="dict.type.data_collect_type" :value="content.pollingMethod" size="small"
                    style="display: inline-block"/>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="padding:-10px;padding-bottom:30px;">
      <el-row :gutter="0">
        <el-col :span="8">
          <div style="border: 1px solid #e6ebf5;">
            <div style="padding:20px 20px 30px 20px;background-color:#f8f8f9;">
              <span style="font-size:15px;font-weight:bold;">从机列表</span>
              <el-button type="primary" size="mini" style=float:right @click="addSalve">添加从机</el-button>
            </div>
            <el-table v-loading="loading" :data="salveList" size="mini" highlight-current-row
                      @row-click="handleSelectionChange" style="width: 100%;height: 500px">
              <el-table-column label="选择" width="50" align="center">
                <template slot-scope="scope">
                  <input type="radio" :checked="scope.row.isSelect"/>
                </template>
              </el-table-column>
              <el-table-column prop="slaveName" label="从机名称" header-align="center" align="left" min-width="120">
              </el-table-column>
              <el-table-column label="操作" align="center" width="110">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdateSlave(scope.row)"
                             v-hasPermi="['iot:temp:edit']">编辑
                  </el-button>
                  <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDeleteSalve(scope.row)"
                             v-hasPermi="['iot:temp:remove']">删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <pagination v-show="slaveTotal>0" :total="slaveTotal" :page.sync="queryParams.pageNum"
                        :limit.sync="queryParams.pageSize" @pagination="getSalveList"/>
          </div>
        </el-col>
        <el-col :span="16">
          <div class="container-02">
            <el-form :inline="true" :model="queryParams">
              <el-form-item>
                <el-input v-model="queryParams.name" size="mini" placeholder="请输入变量名称"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" plain size="mini" @click="queryByName">查询</el-button>
              </el-form-item>
              <el-form-item style="float: right">
                <el-button type="warning" icon="el-icon-upload2" plain size="mini" @click="handleImport">导入变量
                </el-button>
                <el-button type="primary" size="mini" @click="addValTemp">添加变量</el-button>
                <el-button type="primary" size="mini" @click="synchronizaToProduct">同步至产品</el-button>
              </el-form-item>
            </el-form>
            <el-table v-loading="sLoadoing" :data="pointList" size="mini">
              <el-table-column label="序号" prop="templateId" width="50">
              </el-table-column>
              <el-table-column prop="templateName" label="物模型名称" width="200">
              </el-table-column>
              <el-table-column prop="identifier" label="标识符"/>
              <el-table-column prop="datatype" label="数值类型"/>
              <el-table-column prop="isReadonly" label="读写类型">
                <template slot-scope="scope">
                  <dict-tag :options="dict.type.iot_data_read_write" :value="scope.row.isReadonly"/>
                </template>
              </el-table-column>
              <el-table-column prop="isHistory" label="是否历史存储">
                <template slot-scope="scope">
                  <dict-tag :options="dict.type.iot_yes_no" :value="scope.row.isHistory"/>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                             v-hasPermi="['iot:temp:edit']">编辑
                  </el-button>
                  <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                             v-hasPermi="['iot:temp:remove']">删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum"
                        :limit.sync="queryParams.pageSize" @pagination="getList"/>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 添加或修改通用物模型对话框 -->
    <el-dialog :title="title" :visible.sync="openViewVarTemp" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="模型名称" prop="templateName">
          <el-input v-model="form.templateName" placeholder="请输入物模型名称，例如：温度" style="width:385px;"/>
        </el-form-item>
        <el-form-item label="模型标识" prop="identifier">
          <el-input v-model="form.identifier" placeholder="请输入标识符，例如：temperature" style="width:385px;"/>
        </el-form-item>
        <el-form-item label="模型排序" prop="modelOrder">
          <el-input v-model="form.modelOrder" placeholder="请输入排序" type="number" style="width:385px;"/>
        </el-form-item>
        <el-form-item label="模型类别" prop="type">
          <el-radio-group v-model="form.type" @change="typeChange(form.type)">
            <el-radio-button label="1">属性</el-radio-button>
            <el-radio-button label="2">功能</el-radio-button>
            <el-radio-button label="3">事件</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="模型特性" prop="property">
          <el-checkbox name="isChart" label="图表展示" @change="isChartChange" v-show="form.type == 1"
                       v-model="form.isChart" :true-label="1" :false-label="0"></el-checkbox>
          <el-checkbox name="isMonitor" label="实时监测" @change="isMonitorChange" v-show="form.type == 1"
                       v-model="form.isMonitor" :true-label="1" :false-label="0"></el-checkbox>
          <el-checkbox name="isReadonly" label="只读数据" @change="isReadonlyChange" :disabled="form.type==3"
                       v-model="form.isReadonly" :true-label="1" :false-label="0"></el-checkbox>
          <el-checkbox name="isHistory" label="历史存储" v-model="form.isHistory" :true-label="1"
                       :false-label="0"></el-checkbox>
        </el-form-item>

        <el-divider></el-divider>
        <el-form-item label="数据类型" prop="datatype">
          <el-select v-model="form.datatype" placeholder="请选择数据类型" @change="dataTypeChange" style="width:175px;">
            <el-option key="integer" label="整数" value="integer"></el-option>
            <el-option key="decimal" label="小数" value="decimal"></el-option>
            <el-option key="bool" label="布尔" value="bool" :disabled="form.isChart==1"></el-option>
            <el-option key="enum" label="枚举" value="enum" :disabled="form.isChart==1"></el-option>
            <el-option key="string" label="字符串" value="string" :disabled="form.isChart==1"></el-option>
            <el-option key="array" label="数组" value="array" :disabled="form.isChart==1"></el-option>
            <!-- <el-option key="object" label="对象" value="object" :disabled="form.isChart==1"></el-option> -->
          </el-select>
        </el-form-item>
        <div v-if="form.datatype == 'integer' || form.datatype == 'decimal'">
          <el-form-item label="取值范围">
            <el-row>
              <el-col :span="9">
                <el-input v-model="form.specs.min" placeholder="最小值" type="number"/>
              </el-col>
              <el-col :span="2" align="center">到</el-col>
              <el-col :span="9">
                <el-input v-model="form.specs.max" placeholder="最大值" type="number"/>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item label="单位">
            <el-input v-model="form.specs.unit" placeholder="请输入单位，例如：℃" style="width:385px;"/>
          </el-form-item>
        </div>
        <div v-if="form.datatype == 'bool'">
          <el-form-item label="布尔值" prop="">
            <el-row style="margin-bottom:10px;">
              <el-col :span="9">
                <el-input v-model="form.specs.falseText" placeholder="例如：关闭"/>
              </el-col>
              <el-col :span="10" :offset="1"> （0 值对应文本）</el-col>
            </el-row>
            <el-row>
              <el-col :span="9">
                <el-input v-model="form.specs.trueText" placeholder="例如：打开"/>
              </el-col>
              <el-col :span="10" :offset="1"> （1 值对应文本）</el-col>
            </el-row>
          </el-form-item>
        </div>
        <div v-if="form.datatype == 'enum'">
          <el-form-item label="展示方式">
            <el-select v-model="form.specs.showWay" placeholder="请选择展示方式" style="width:175px;">
              <el-option key="select" label="下拉框" value="select"></el-option>
              <el-option key="button" label="按钮" value="button"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="枚举项" prop="">
            <el-row v-for="(item,index) in form.specs.enumList" :key="'enum'+index" style="margin-bottom:10px;">
              <el-col :span="9">
                <el-input v-model="item.value" placeholder="参数值，例如：0"/>
              </el-col>
              <el-col :span="11" :offset="1">
                <el-input v-model="item.text" placeholder="参数描述，例如：中速档位"/>
              </el-col>
              <el-col :span="2" :offset="1" v-if="index!=0"><a style="color:#F56C6C"
                                                               @click="removeEnumItem(index)">删除</a></el-col>
            </el-row>
            <div>+ <a style="color:#409EFF" @click="addEnumItem()">添加枚举项</a></div>
          </el-form-item>
        </div>
        <div v-if="form.datatype == 'string'">
          <el-form-item label="最大长度" prop="">
            <el-row>
              <el-col :span="9">
                <el-input v-model="form.specs.maxLength" placeholder="例如：1024" type="number"/>
              </el-col>
              <el-col :span="14" :offset="1">（字符串的最大长度）</el-col>
            </el-row>
          </el-form-item>
        </div>
        <div v-if="form.datatype == 'array'">
          <el-form-item label="元素个数" prop="">
            <el-row>
              <el-col :span="9">
                <el-input v-model="form.specs.arrayCount" placeholder="例如：5" type="number"/>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item label="数组类型" prop="">
            <el-radio-group v-model="form.specs.arrayType">
              <el-radio label="integer">整数</el-radio>
              <el-radio label="decimal">小数</el-radio>
              <el-radio label="string">字符串</el-radio>
              <!-- <el-radio label="object">对象</el-radio> -->
            </el-radio-group>
          </el-form-item>
          <el-form-item label="对象参数" v-if="form.specs.arrayType=='object'">
            <div style="background-color:#f8f8f8;border-radius:5px;">
              <el-row style="padding:0 10px 5px;" v-for="(item,index) in form.specs.params" :key="index">
                <div style="margin-top:5px;" v-if="index==0"></div>
                <el-col :span="18">
                  <el-input readonly v-model="item.name" size="mini" placeholder="请选择设备" style="margin-top:3px;">
                    <template slot="prepend">
                      <el-tag size="mini" effect="dark" style="margin-left:-21px;height:26px;line-height:26px;">
                        {{ item.order }}
                      </el-tag>
                      {{ form.identifier + '_' + item.id }}
                    </template>
                    <el-button slot="append" @click="editParameter(item,index)" size="small">编辑</el-button>
                  </el-input>
                </el-col>
                <el-col :span="2" :offset="2">
                  <el-button size="small" plain type="danger" style="padding:5px;" icon="el-icon-delete"
                             @click="removeParameter(index)">删除
                  </el-button>
                </el-col>
              </el-row>
            </div>
            <div>+ <a style="color:#409EFF" @click="addParameter()">添加参数</a></div>
          </el-form-item>
        </div>
        <div v-if="form.datatype == 'object'">
          <el-form-item label="对象参数" prop="">
            <div style="background-color:#f8f8f8;border-radius:5px;">
              <el-row style="padding:0 10px 5px;" v-for="(item,index) in form.specs.params" :key="index">
                <div style="margin-top:5px;" v-if="index==0"></div>
                <el-col :span="18">
                  <el-input readonly v-model="item.name" size="mini" placeholder="请选择设备" style="margin-top:3px;">
                    <template slot="prepend">
                      <el-tag size="mini" effect="dark" style="margin-left:-21px;height:26px;line-height:26px;">
                        {{ item.order }}
                      </el-tag>
                      {{ form.identifier + '_' + item.id }}
                    </template>
                    <el-button slot="append" @click="editParameter(item,index)" size="small">编辑</el-button>
                  </el-input>
                </el-col>
                <el-col :span="2" :offset="2">
                  <el-button size="small" plain type="danger" style="padding:5px;" icon="el-icon-delete"
                             @click="removeParameter(index)">删除
                  </el-button>
                </el-col>
              </el-row>
            </div>
            <div>+ <a style="color:#409EFF" @click="addParameter()">添加参数</a></div>
          </el-form-item>
        </div>

        <el-form-item label="高级选项">
          <el-switch @change="selectOpenAction" v-model="isAdvance">
          </el-switch>
        </el-form-item>
        <el-form-item v-if="selectOpen" label="计算公式">
          <template slot="label">
            <span>计算公式</span>
            <el-tooltip style="cursor: pointer;" effect="light" placement="top">
              <div slot="content">
                设备上行数据经计算公式计算后显示 。<br/>
                公式中的%s为占位符，是固定字段。<br/>
                如：<br/>
                加：%s+10<br/>
                减：%s-10<br/>
                乘：%s*10<br/>
                除：%s/10<br/>
                余数：%s%10<br/>
              </div>
              <i class="el-icon-question"/>
            </el-tooltip>
          </template>
          <el-input v-model="form.formula" placeholder="计算公式"/>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--物模型参数类型-->
    <things-parameter :data="paramData" @dataEvent="getParamData($event)"/>

    <!--添加从机对话框-->
    <el-dialog :title="title" style="" :visible.sync="openViewSalve" width="550px" append-to-body>
      <el-form ref="addSavleFrom" :model="addSavleFrom" :rules="salveRules" label-width="130px" size="mini"
               style="width: 500px">
        <el-form-item label="从机名称" prop="slaveName" size="small">
          <el-input v-model="addSavleFrom.slaveName" placeholder="请输入从机名称"/>
        </el-form-item>
        <el-form-item label="从机地址" prop="slaveAddr" size="small">
          <el-input v-model="addSavleFrom.slaveAddr" placeholder="请输入从机地址"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitFormSlave">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
                 :action="upload.url" :disabled="upload.isUploading"
                 :data="{'tempSlaveId' : this.queryParams.tempSlaveId}"
                 :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;"
                   @click="importTemplate">下载模板
          </el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 采集点对应所有产品 -->
    <el-dialog :title="title" :visible.sync="openView" width="550px" append-to-body>
      <div style="color: red">采集点模板同步到产品后,产品物模型以及缓存物模型将更新,请谨慎更新产品采集点模板数据</div>
      <el-table v-loading="loading" :data="productList" size="mini" highlight-current-row
                @selection-change="selectionChange" style="width: 100%;height: 500px">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column prop="productId" label="产品id" header-align="center" align="left" min-width="60">
        </el-table-column>
        <el-table-column prop="productName" label="产品名称" header-align="center" align="left" min-width="120">
        </el-table-column>
        <el-table-column prop="transport" label="传输协议" header-align="center" align="left" min-width="60">
        </el-table-column>
        <el-table-column prop="protocolCode" label="设备协议" header-align="center" align="left" min-width="80">
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitSyncToProduct">确定同步</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listTemplate,
  getTemplate,
  addTemplate,
  delTemplate,
  updateTemplate
} from "@/api/iot/template";
import {
  listSalve,
  getSalve,
  delSalve,
  addSalve,
  updateSalve
} from "@/api/iot/salve";
import {
  checkNumber,
  checkNumberAddr
} from "@/utils/validate";
import {
  getToken
} from "@/utils/auth";
import thingsParameter from "../template/parameter"
import {selectByTempleId} from "@/api/iot/product";
import {synchron} from "@/api/iot/model";
import Template from "@/views/iot/template/index";

export default {
  name: "Point",
  dicts: ['data_collect_type', 'iot_modbus_status_code', 'iot_modbus_data_type', 'iot_data_read_write','iot_yes_no'],
  components: {
    Template,
    thingsParameter
  },
  data() {
    return {
      // 高级选项
      isAdvance: false,
      // 遮罩层
      loading: true,
      sLoadoing: false,
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
      slaveTotal: 0,
      // 变量模板从机采集点表格数据
      pointList: [],
      // 变量模板设备从机表格数据
      salveList: [],
      // 对象类型参数
      paramData: {
        index: -1,
        parameter: {}
      },
      // 弹出层标题
      title: "",
      // 添加变量弹出层
      openViewVarTemp: false,
      /*添加从机弹出框*/
      openViewSalve: false,
      /*采集点模板关联的产品列表弹窗*/
      openView: false,
      selectOpen: false,
      selectRowData: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tempSlaveId: null,
        name: null,
        identifier: null,
        weight: null,
        deviceTempId: null,
      },
      // 导入参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "",
        // 设置上传的请求头部
        headers: {
          Authorization: "Bearer " + getToken()
        },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/iot/template/importData"
      },
      // 表单参数
      // 表单参数
      form: {
        regType: 3,
      },
      // 表单校验
      rules: {
        templateName: [{
          required: true,
          message: "物模型名称不能为空",
          trigger: "blur"
        },],
        identifier: [{
          required: true,
          message: "物模型标识符不能为空",
          trigger: "blur",
        },],
        modelOrder: [{
          required: true,
          message: "模型排序不能为空",
          trigger: "blur",
        },],
        type: [{
          required: true,
          message: "模型类别不能为空",
          trigger: "change"
        },],
        datatype: [{
          required: true,
          message: "数据类型不能为空",
          trigger: "change"
        }],
        specs: [{
          required: true,
          message: "数据定义不能为空",
          trigger: "blur"
        }],
        regStr: [{
          required: true,
          message: "寄存器地址不能为空",
          trigger: "blur"
        }],
        regType: [{
          required: true,
          message: "功能码不能为空",
          trigger: "blur"
        }]
      },
      content: {
        pollingMethod: 0,
      },
      addSavleFrom: {
        code: 3,
      },
      salveRules: {
        slaveName: [{
          required: true,
          message: "从机名称不能为空",
          trigger: "blur"
        }],
        addrStart: [{
          required: true,
          message: "寄存器起始地址不能为空",
          trigger: "blur"
        }],
        addrEnd: [{
          required: true,
          message: "寄存器结束地址不能为空",
          trigger: "blur"
        }],
        slaveAddr: [{
          required: true,
          message: "从机地址不能为空",
          trigger: "blur"
        }],
        packetLength: [{
          required: true,
          message: '数量不能为空',
          trigger: 'blur'
        }
        ]
      },
    };
  },
  created() {
    this.content.templateName = this.$route.query && this.$route.query.templateName;
    this.content.pollingMethod = this.$route.query && this.$route.query.pollingMethod;
    this.content.share = this.$route.query && this.$route.query.share;
    this.content.templateId = this.$route.query && this.$route.query.templateId;
    let isOpen = this.$route.query && this.$route.query.isOpen;
    if (isOpen === 1) {
      this.openViewSalve = true;
    }
    this.queryParams.deviceTempId = this.content.templateId;
    this.getSalveList();
  },

  methods: {
    /** 查询变量模板从机采集点列表 */
    getList() {
      this.sLoadoing = true;
      listTemplate(this.queryParams).then(response => {
        this.pointList = response.rows;
        this.total = response.total;
        this.sLoadoing = false;
      });
    },
    /*查询子设备列表*/
    getSalveList() {
      this.loading = true;
      listSalve(this.queryParams).then(response => {
        for (let i = 0; i < response.rows.length; i++) {
          response.rows[i].isSelect = false;
          if (this.selectRowData && this.selectRowData.id == response.rows[i].id) {
            response.rows[i].isSelect = true;
          }
        }
        this.salveList = response.rows;
        this.slaveTotal = response.total;
        this.loading = false;
        if (this.salveList.length > 0) {
          let row = this.salveList[0];
          //自动选择
          this.handleSelectionChange(row)
          this.queryParams.tempSlaveId = row.deviceTempId + "#" + row.slaveAddr;
          this.getList();
        }
      });
    },
    // 取消按钮
    cancel() {
      this.openViewVarTemp = false;
      this.openViewSalve = false;
      this.openView = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        templateId: null,
        templateName: null,
        userId: null,
        userName: null,
        tenantId: null,
        tenantName: null,
        identifier: null,
        modelOrder: 0,
        type: 1,
        datatype: "integer",
        isSys: null,
        isChart: 1,
        isHistory: 1,
        isMonitor: 1,
        isReadonly: 1,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        regType: '3',
        productList: [],
        specs: {
          enumList: [{
            value: "",
            text: ""
          }],
          arrayType: "integer",
          arrayCount: 5,
          showWay: "select", // 显示方式select=下拉选择框，button=按钮
          params: [],
        },
      };

      this.addSavleFrom = {
        deviceTempId: null,
        slaveAddr: null,
        slaveIndex: null,
        code: null,
        slaveIp: null,
        slaveName: null,
        slavePort: null,
        status: 0,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remark: null
      }
      this.resetForm("form");
      this.resetForm("addSavleFrom");
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

    /*选中*/
    handleSelectionChange(slave) {
      this.setRadioSelected(slave)
      if (slave != null) {
        this.selectRowData = slave;
      }
      //获取采集点列表
      this.queryParams.tempSlaveId = slave.deviceTempId + "#" + slave.slaveAddr;
      this.getList();

    },
    /*选中同步到的产品*/
    selectionChange(selection) {
      this.productIds = selection.map(item => item.productId);
    },

    /** 设置单选按钮选中 */
    setRadioSelected(slave) {
      for (let i = 0; i < this.salveList.length; i++) {
        if (this.salveList[i].id == slave.id) {
          this.salveList[i].isSelect = true;
        } else {
          this.salveList[i].isSelect = false;
        }
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.openViewVarTemp = true;
      this.title = "添加变量模板从机采集点";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.templateId || this.ids
      getTemplate(id).then(response => {
        let tempForm = response.data;
        this.openViewVarTemp = true;
        this.title = "修改物模型";
        // Json转对象
        tempForm.specs = JSON.parse(tempForm.specs);
        if (!tempForm.specs.enumList) {
          tempForm.specs.showWay = "select";
          tempForm.specs.enumList = [{
            value: "",
            text: ""
          }];
        }
        if (!tempForm.specs.arrayType) {
          tempForm.specs.arrayType = "integer";
        }
        if (!tempForm.specs.arrayCount) {
          tempForm.specs.arrayCount = 5;
        }
        if (!tempForm.specs.params) {
          tempForm.specs.params = [];
        }
        // 对象和数组中参数删除前缀
        if ((tempForm.specs.type == "array" && tempForm.specs.arrayType == "object") || tempForm.specs.type == "object") {
          for (let i = 0; i < tempForm.specs.params.length; i++) {
            tempForm.specs.params[i].id = String(tempForm.specs.params[i].id).substring(String(tempForm.identifier).length + 1);
          }
        }
        this.form = tempForm;
        this.form.oldIdentifier = tempForm.identifier
        this.form.regType = '3';
        this.form.regStr = this.form.regAddr + "";
      });
    },
    /** 修改按钮操作 */
    handleUpdateSlave(row) {
      this.reset();
      const id = row.id || this.ids
      getSalve(id).then(response => {
        this.addSavleFrom = response.data;
        this.openViewSalve = true;
        this.addSavleFrom.code = this.addSavleFrom.code + '';
        this.addSavleFrom.timer = this.addSavleFrom.timer + '';
        this.title = "修改传感器";
        this.addSavleFrom.oldSlaveId = this.addSavleFrom.slaveAddr;
      });
    },
    /** 删除按钮操作 */
    handleDeleteSalve(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除变量模板设备从机编号为"' + ids + '"的数据项？').then(function () {
        delSalve(ids);
        /*TODO-删除采集点数据*/
        // delBySlaveId()
      }).then(() => {
        this.getSalveList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 验证对象或对象数组中的参数不能为空
          if (this.form.datatype == 'object' || (this.form.datatype == "array" && this.form.specs.arrayType == "object")) {
            if (!this.form.specs.params || this.form.specs.params == 0) {
              this.$modal.msgError("对象的参数不能为空");
              return;
            }
          }
          // 验证对象参数标识符不能相同
          if (this.form.specs.params && this.form.specs.params.length > 0) {
            let arr = this.form.specs.params.map(item => item.id).sort();
            for (let i = 0; i < arr.length; i++) {
              if (arr[i] == arr[i + 1]) {
                this.$modal.msgError("参数标识 " + arr[i] + " 重复");
                return;
              }
            }
          }
          this.form.deviceTempId = this.content.templateId;
          this.form.tempSlaveId = this.form.deviceTempId + "#" + this.selectRowData.slaveAddr;
          this.form.slaveId = this.selectRowData.slaveAddr;
          if (this.form.templateId != null) {
            // 格式化specs
            let tempForm = JSON.parse(JSON.stringify(this.form));
            tempForm.specs = this.formatThingsSpecs();
            if (this.form.type == 2) {
              tempForm.isMonitor = 0;
              tempForm.isChart = 0;
            } else if (this.form.type == 3) {
              tempForm.isMonitor = 0;
              tempForm.isChart = 0;
            }
            updateTemplate(tempForm).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.openViewVarTemp = false;
              this.getList();
            });
          } else {
            // 格式化specs
            let tempForm = JSON.parse(JSON.stringify(this.form));
            tempForm.specs = this.formatThingsSpecs();
            if (this.form.type == 2) {
              tempForm.isMonitor = 0;
            } else if (this.form.type == 3) {
              tempForm.isMonitor = 0;
              tempForm.isChart = 0;
            }
            addTemplate(tempForm).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.openViewVarTemp = false;
              this.getList();
            });
          }
        }
      });
    },

    /** 提交按钮 */
    submitFormSlave() {
      this.addSavleFrom.pollingMethod = 1;
      this.$refs["addSavleFrom"].validate(valid => {
        if (valid) {
          this.addSavleFrom.deviceTempId = this.content.templateId;
          if (this.addSavleFrom.id != null) {
            updateSalve(this.addSavleFrom).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.openViewSalve = false;
              this.getSalveList();
            });
          } else {
            addSalve(this.addSavleFrom).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.openViewSalve = false;
              this.getSalveList();
            });
          }
        }
      });
    },
    /*根据变量名查询*/
    queryByName() {
      if (!this.selectRowData) {
        this.$modal.alert("请选选择或添加子设备")
        return;
      }
    },
    /*添加从机*/
    addSalve() {
      this.reset();
      this.openViewSalve = true;
      this.title = "添加从机";
    },

    /*添加变量*/
    addValTemp() {
      console.log("sele", this.selectRowData)
      if (!this.selectRowData) {
        this.$modal.alert("请选选择或添加子设备")
        return;
      }
      this.reset();
      this.openViewVarTemp = true;
      this.title = "添加变量";
    },

    //同步采集点模板到指定产品
    synchronizaToProduct() {
      const params = {"templateId": this.content.templateId}
      selectByTempleId(params).then((res) => {
        this.productList = res.data;
        this.openView = true;
        this.title = "同步采集点到产品";
      });
    },
    /*同步模板到产品确认*/
    submitSyncToProduct() {
      const data = {"productIds": this.productIds, "templateId": this.content.templateId}
      synchron(data).then(res => {
        if (res.code == 200) {
          this.$modal.msgSuccess("同步成功");
        } else {
          this.$modal.msgError(res.msg);
        }
        this.openView = false;
        this.reset();
      })
    },

    //高级选项
    selectOpenAction() {
      this.selectOpen = !this.selectOpen;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      console.log("row", row)
      const ids = row.templateId || this.ids;
      this.$modal
        .confirm('是否确认删除变量模板从机采集点编号为"' + ids + '"的数据项？')
        .then(function () {
          return delTemplate(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {
        });
    },

    /*导入模板*/
    importTemplate() {
      this.download('iot/template/temp-json', {}, `采集点Json_${new Date().getTime()}.xlsx`)
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", {
        dangerouslyUseHTMLString: true
      });
      this.getList();
    },
    /** 导入按钮操作 */
    handleImport() {
      if (!this.selectRowData) {
        this.$modal.alert("请选选择或添加子设备")
        return;
      }
      this.upload.title = "采集点导入";
      this.upload.open = true;
    },
    /** 切换为枚举项 */
    dataTypeChange(val) {
      // if (val == "enum") {
      //     this.form.specs.enumList = [{
      //         value: "",
      //         text: ""
      //     }];
      // }
    },
    /** 添加枚举项 */
    addEnumItem() {
      this.form.specs.enumList.push({
        value: "",
        text: ""
      });
    },
    /** 删除枚举项 */
    removeEnumItem(index) {
      this.form.specs.enumList.splice(index, 1);
    },
    // 类型改变
    typeChange(type) {
      if (type == 1) {
        this.form.isChart = 1;
        this.form.isHistory = 1;
        this.form.isMonitor = 1;
        this.form.isReadonly = 1;
        this.form.datatype = "integer";
      } else if (type == 2) {
        this.form.isChart = 0;
        this.form.isHistory = 1;
        this.form.isMonitor = 0;
        this.form.isReadonly = 0;
      } else if (type == 3) {
        this.form.isChart = 0;
        this.form.isHistory = 1;
        this.form.isMonitor = 0;
        this.form.isReadonly = 1;
      }
    },
    // 是否图表展示改变
    isChartChange() {
      if (this.form.isChart == 1) {
        this.form.isReadonly = 1;
      } else {
        this.form.isMonitor = 0;
      }
    },
    // 是否实时监测改变
    isMonitorChange() {
      if (this.form.isMonitor == 1) {
        this.form.isReadonly = 1;
        this.form.isChart = 1;
      }
    },
    // 是否只读数据改变
    isReadonlyChange() {
      if (this.form.isReadonly == 0) {
        this.form.isMonitor = 0;
        this.form.isChart = 0;
      }
    },
    // 格式化物模型
    formatThingsSpecs() {
      var data = {};
      data.type = this.form.datatype;
      if (this.form.datatype == "integer" || this.form.datatype == "decimal") {
        data.min = Number(this.form.specs.min ? this.form.specs.min : 0);
        data.max = Number(this.form.specs.max ? this.form.specs.max : 100);
        data.unit = this.form.specs.unit ? this.form.specs.unit : "";
        data.step = Number(this.form.specs.step ? this.form.specs.step : 1);
      } else if (this.form.datatype == "string") {
        data.maxLength = Number(this.form.specs.maxLength ? this.form.specs.maxLength : 1024);
      } else if (this.form.datatype == "bool") {
        data.falseText = this.form.specs.falseText ? this.form.specs.falseText : '关闭';
        data.trueText = this.form.specs.trueText ? this.form.specs.trueText : '打开';
      } else if (this.form.datatype == "array") {
        data.arrayType = this.form.specs.arrayType;
      } else if (this.form.datatype == "enum") {
        data.showWay = this.form.specs.showWay;
        if (this.form.specs.enumList && this.form.specs.enumList[0].text != '') {
          data.enumList = this.form.specs.enumList;
        } else {
          data.showWay = "select";
          data.enumList = [{
            "value": "0",
            "text": "低"
          }, {
            "value": "1",
            "text": "高"
          }];
        }
      } else if (this.form.datatype == "array") {
        data.arrayType = this.form.specs.arrayType;
        data.arrayCount = this.form.specs.arrayCount ? this.form.specs.arrayCount : 5;
        if (data.arrayType == "object") {
          data.params = this.form.specs.params;
          // 物模型名称作为参数的标识符前缀
          for (let i = 0; i < data.params.length; i++) {
            data.params[i].id = this.form.identifier + "_" + data.params[i].id;
          }
        }
      } else if (this.form.datatype == 'object') {
        data.params = this.form.specs.params;
        // 物模型名称作为参数的标识符前缀
        for (let i = 0; i < data.params.length; i++) {
          data.params[i].id = this.form.identifier + "_" + data.params[i].id;
        }
      }
      return JSON.stringify(data);
    },
    /** 添加参数 */
    addParameter() {
      this.paramData = {
        index: -1,
        parameter: {}
      }
    },
    /** 编辑参数*/
    editParameter(data, index) {
      this.paramData = null;
      this.paramData = {
        index: index,
        parameter: data
      }
    },
    /**获取设置的参数对象*/
    getParamData(data) {
      if (data.index == -1) {
        this.form.specs.params.push(data.parameter);
      } else {
        this.form.specs.params[data.index] = data.parameter;
        // 解决数组在界面中不更新问题
        this.$set(this.form.specs.params, data.index, this.form.specs.params[data.index]);
      }
    },
    /** 返回按钮 */
    goBack() {
      const obj = {
        path: "/template/template",
      };
      this.$tab.closeOpenPage(obj);
      this.reset();
    },
  }
};
</script>

<style>
.container-02 {
  padding: 8px 20px;
}
</style>
