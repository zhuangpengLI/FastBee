<template>
  <div class="running-status H100">
    <div>
      <!-- tabs多时，可以自己新建组件，免重复代码   -->
      <el-tabs type="border-card" v-model="thingsType" @tab-click="handleClick" style="flex: 1; height: 800px; margin-bottom: 5px">
        <el-tab-pane label="属性上报" name="prop">
          <el-main v-loading="loading" style="position: relative" class="H100">
            <el-row :gutter="20" class="row-list">
              <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-for="(item, index) in runningData" :key="index" style="margin-bottom: 10px">
                <el-card style="padding: 0px; height: 90px">
                  <div class="head">
                    <div class="title">{{ item.name }}({{ item.id }})</div>
                    <div class="name">
                      <span style="color: #0f73ee">{{ item.value }}</span>
                      <span v-if="item.datatype.unit">{{ item.datatype.unit || item.datatype.unitName }}</span>
                    </div>
                  </div>
                  <div>时间：{{ item.ts }}</div>
                </el-card>
              </el-col>
            </el-row>
          </el-main>
        </el-tab-pane>
        <el-tab-pane label="服务下发" name="function">
          <el-main v-loading="loading" style="position: relative" class="H100">
            <el-row :gutter="20" class="row-list">
              <el-col ::xs="17" :sm="12" :md="12" :lg="8" :xl="6" v-for="(item, index) in functionData" :key="index" style="margin-bottom: 10px">
                <el-card shadow="hover" class="elcard" style="height: 90px">
                  <div class="head">
                    <div class="title">
                      {{ item.name }}
                    </div>
                    <div class="name">
                      <span style="color: #0f73ee">{{ item.value }}</span>
                      <span v-if="item.datatype.unit">{{ item.datatype.unit }}</span>
                      <el-button type="primary" plain icon="el-icon-s-promotion" size="mini" style="float: right; margin-right: -5px; padding: 3px 5px" @click.stop="editFunc(item)">发送</el-button>
                    </div>
                  </div>
                  <div>
                    <span>时间：{{ item.ts }}</span>
                  </div>
                </el-card>
              </el-col>
              <el-col ::xs="7" :sm="12" :md="12" :lg="8" :xl="6" class="phone-main">
                <div class="phone">
                  <div class="phone-container">
                    <div class="phone-title">设 备 指 令</div>
                    <div class="log-content" ref="logContent">
                      <el-scrollbar style="height: 100%" ref="scrollContent">
                        <ul v-for="(item, index) in logList" :key="index">
                          <li>
                            <a href="#" style="float: left; text-align: left">
                              <div class="time">{{ item.createTime }}</div>
                              <div class="spa">
                                <span class="lable-s1">服务下发:</span>
                                {{ item.modelName }}
                              </div>
                            </a>
                            <a href="#" style="float: right; text-align: right">
                              <div class="time">{{ item.replyTime }}</div>
                              <div :class="{ fail: item.resultCode == 201, wait: item.resultCode == 203 }">
                                <span class="lable-s1">设备应答:</span>
                                {{ item.showValue }}
                              </div>
                            </a>
                          </li>
                        </ul>
                      </el-scrollbar>
                    </div>
                  </div>
                </div>
              </el-col>
            </el-row>
            <el-empty description="暂无数据" v-show="runningData.length == 0"></el-empty>
          </el-main>
        </el-tab-pane>

        <el-tab-pane disabled name="slave">
          <span slot="label" style="margin-left: 50px">
            <span ref="statusTitle" style="color: #409eff; margin-right: 30px">{{ title }}</span>
            <el-select v-model="params.slaveId" placeholder="请选择设备从机" @change="selectSlave" size="mini">
              <el-option v-for="slave in slaveList" :key="slave.slaveId" :label="`${slave.deviceName}   (${slave.slaveId})`" :value="slave.slaveId"></el-option>
            </el-select>
          </span>
        </el-tab-pane>
      </el-tabs>
    </div>

    <el-dialog title="服务调用" :visible.sync="dialogValue" label-width="200px">
      <el-form v-model="from" size="mini" style="height: 100%; padding: 0 20px">
        <el-form-item :label="from.name" label-width="180px">
          <el-input v-model="from.shadow" type="number" @input="justicNumber()" v-if="from.datatype.type == 'integer' || from.datatype.type == 'decimal' || from.datatype.type == 'string'" style="width: 50%"></el-input>
          <el-select v-if="from.datatype.type == 'enum'" v-model="from.shadow" @change="changeSelect()">
            <el-option v-for="option in from.datatype.enumList" :key="option.value" :label="option.text" :value="option.value"></el-option>
          </el-select>
          <el-switch v-if="from.datatype.type === 'bool'" v-model="from.shadow" active-value="1" inactive-value="0" inline-prompt />
          <span v-if="(from.datatype.type == 'integer' || from.datatype.type == 'decimal') && from.datatype.type.unit && from.datatype.type.unit != 'un' && from.datatype.type.unit != '/'">（{{ from.unit }}）</span>
          <div v-if="from.datatype.type == 'integer' || from.datatype.type == 'decimal'" class="range">
            (数据范围:{{ from.datatype.max == 'null' ? (from.datatype.type == 'bool' ? 0 : '') : from.datatype.min }} ~ {{ from.datatype.max == 'null' ? (from.datatype.type == 'bool' ? 1 : '') : from.datatype.max }})
          </div>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogValue = false">取消</el-button>
        <el-button type="primary" @click="sendService" :loading="btnLoading" :disabled="!canSend">确认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { serviceInvoke, funcLog } from '@/api/iot/runstatus';
import { listByPid } from '@/api/iot/salve';
import { getDeviceRunningStatus } from '@/api/iot/device';


const INTEGER = 'integer';
const DECIMAL = 'decimal';
const BOOL = 'bool';
const ENUM = 'enum';

export default {
  name: 'realTime-status',
  props: {
    device: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      /**设备模拟消息列表**/
      messageList: [],
      /**设备模拟发送消息表单**/
      simulateForm: {},
      deviceInfo: {}, // 设备信息
      dialogValue: false, // 查看详情弹框
      gridData: [], // 事件数据
      groupId: 1,
      treeData: [],
      runningData: [], // 实时状态列表
      functionData: [],
      loading: false,
      debounceGetRuntime: '',
      runtimeName: '',
      serialNumber: '',
      params: {
        serialNumber: undefined,
        type: 1,
      },
      slaveList: [],
      queryParams: {},
      thingsType: 'prop',
      opationList: [], // 指令数值数组
      funVal: {},
      canSend: false, //是否可以下发，主要判断数值在不在范围
      functionName: {},
      btnLoading: false,
      logList: [],
      showValue: '',
      from: {
        datatype: {
          type: '',
        },
      },
      title: '在线模式',
    };
  },

  created() {},

  watch: {
    device: function (newVal) {
      if (newVal && newVal.serialNumber) {
        this.params.serialNumber = newVal.serialNumber;
        this.serialNumber = newVal.serialNumber;
        this.params.productId = newVal.productId;
        this.params.slaveId = newVal.slaveId;
        this.params.deviceId = newVal.deviceId;
        this.deviceInfo = newVal;
        this.updateDeviceStatus(this.deviceInfo);
        this.slaveList = newVal.subDeviceList;
        this.getSlaveList(this.deviceInfo);
        this.$busEvent.$on('updateData', (params) => {
          if (params.data && params.data[0].remark){
            this.getDeviceFuncLog();
            params.data[0].ts =  params.data[0].remark;
          }
          this.updateData(params);
        });
        this.$busEvent.$on('logData', (params) => {
          this.messageList.push(...params.data);
        });
      }
    },
  },
  methods: {
    /** qos改变事件 **/
    qosChange(data) {},
    /** 载体改变事件 **/
    payloadTypeChange(data) {},
    /** 获取当前时间 **/
    getTime() {
      let date = new Date();
      let y = date.getFullYear();
      let m = date.getMonth() + 1;
      let d = date.getDate();
      let H = date.getHours();
      let mm = date.getMinutes();
      let s = date.getSeconds();
      m = m < 10 ? '0' + m : m;
      d = d < 10 ? '0' + d : d;
      H = H < 10 ? '0' + H : H;
      return y + '-' + m + '-' + d + ' ' + H + ':' + mm + ':' + s;
    },

    /*获取运行状态*/
    getRuntimeStatus() {
      getDeviceRunningStatus(this.params).then((response) => {
        this.runningData = response.data.thingsModels;
        this.runningData.forEach((item) => {
          if (item.datatype.type == 'enum') {
            item.datatype.enumList.forEach((val) => {
              if (val.value == item.value) {
                item.value = val.text;
              }
            });
          } else if (item.datatype.type == 'bool') {
            item.value = item.value == 0 ? item.falseText : item.trueText;
          }
        });
        //筛选读写物模型
        this.functionData = this.runningData.filter((item) => item.isReadonly == 0);
      });
    },

    /**根据产品id获取从机列表*/
    getSlaveList() {
      this.getRuntimeStatus();
      this.getDeviceFuncLog();
    },
    /*选择从机*/
    selectSlave() {
      this.params.serialNumber = this.serialNumber + '_' + this.params.slaveId;
      this.getRuntimeStatus();
    },
    /*tabs切换*/
    handleClick() {
      if (this.thingsType === 'prop') {
        this.params.type = 1;
      } else if (this.thingsType === 'function') {
        this.params.type = 2;
        //筛选读写物模型
        this.functionData = this.runningData.filter((item) => item.isReadonly == 0);
      }
    },
    // 更新参数值
    updateParam(data) {},
    //指令下发
    editFunc(item) {
      this.dialogValue = true;
      this.canSend = true;
      this.funVal = {};
      this.getValueName(item);
      this.from = item;
      console.log(this.runningData);
    },

    /** 更新设备状态 */
    updateDeviceStatus(device) {
      if (device.status == 3) {
        this.title = '在线模式';
      } else {
        if (device.isShadow == 1) {
          this.title = '影子模式';
        } else {
          this.title = '离线模式';
        }
      }
      this.$emit('statusEvent', this.deviceInfo.status);
    },

    // 解析值
    getValueName(item) {
      this.funVal[item.id] = item.value;
    },
    // 发送指令
    sendService() {
      console.log('下发指令', this.from.shadow);
      try {
        this.funVal[this.from.id] = this.from.shadow;
        const data = {
          serialNumber: this.serialNumber,
          productId: this.params.productId,
          remoteCommand: this.funVal,
          identifier: this.from.id,
          slaveId: this.params.slaveId,
          modelName: this.from.name,
          isShadow: this.device.status != 3,
          type: this.from.type,
        };
        serviceInvoke(data).then((response) => {
          if (response.code == 200) {
            this.$message({
              type: 'success',
              message: '服务调用成功!',
            });
            // setTimeout(this.getDeviceFuncLog,900);
          }
        });
      } finally {
        this.dialogValue = false;
      }
    },

    getShowValue(value) {
      switch (this.from.datatype.type) {
        case ENUM:
          const list = this.from.datatype.enumList;
          list.forEach((m) => {
            if (m.value === value) {
              this.showValue = m.text;
            }
          });
          break;
        case INTEGER:
        case DECIMAL:
          this.showValue = value;
        case BOOL:
          this.showValue = value == 1 ? this.from.datatype.trueText : this.from.datatype.falseText;
          break;
      }
    },

    //下拉选择修改触发
    changeSelect() {
      this.$forceUpdate();
    },

    //判断输入是否超过范围
    justicNumber() {
      this.canSend = true;
      if (this.from.datatype.max < this.funVal[this.from.identity] || this.from.datatype.min > this.funVal[this.from.identity]) {
        this.canSend = false;
        return true;
      }
      this.$forceUpdate();
    },

    //  获取设备服务下发日志
    getDeviceFuncLog() {
      const params = {
        serialNumber: this.params.serialNumber + '_' + this.params.slaveId,
      };
      funcLog(params).then((response) => {
        this.logList = response.rows;
      });
    },
    updateData(msg) {
      if (msg.data) {
        msg.data.forEach((d) => {
          this.runningData.some((old, index) => {
            if (d.slaveId === old.slaveId && d.id == old.id) {
              const template = this.runningData[index];
              template.ts = d.ts;
              template.value = d.value;
              if (old.datatype.type == 'enum') {
                old.datatype.enumList.forEach((val) => {
                  if (val.value == template.value) {
                    template.value = val.text;
                  }
                });
              } else if (old.datatype.type == 'bool') {
                template.value = template.value == 0 ? old.datatype.falseText : old.datatype.trueText;
              }
              this.$set(this.runningData, index, template);
              return true;
            }
          });
        });
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.phone-main {
  float: right;
}

.phone {
  height: 729px;
  width: 370px;
  background-image: url('../../../assets/images/phone.png');
  background-size: cover;
}

.phone-container {
  height: 618px;
  width: 343px;
  position: relative;
  top: 46px;
  left: 12px;
  background-color: #fff;

  .phone-title {
    line-height: 40px;
    color: #fff;
    background-color: #007aff;
    text-align: center;
  }

  .messageContent {
    height: 440px;
    overflow-y: scroll;
    word-wrap: break-word;
    padding: 6px 0;
    color: #fff;
  }

  .messageBottom {
    height: 150px;
    position: absolute;
    bottom: 0;
    width: 100%;
    background-color: #eef3f7;
    padding: 5px;
    border-top: 1px solid #d2dae1;
  }

  .messageReceive {
    float: left;
    background-color: #409eff;
    border-radius: 6px;
    padding: 10px;
    width: 70%;
    font-size: 12px;
    margin-bottom: 15px;
    border-style: dotted;
  }

  .messageSend {
    float: right;
    background-color: #13ce66;
    border-radius: 10px;
    padding: 10px;
    width: 70%;
    font-size: 12px;
    margin-bottom: 15px;
    border-right-style: double;
  }
}

.log-content {
  padding: 2px;
  height: calc(100% - 44px);
  overflow: auto;
  ul {
    padding: 0;
    margin: 4px 0;
    list-style: none;
  }

  a {
    width: 100%;
    color: #333;
    //border: 1px solid #fff;
    flex-wrap: wrap;
    padding: 5px 5px 1px 5px;
    text-decoration: none;
    font-size: 12px;

    .time {
      font-size: 10px;
      color: gray;
    }

    div {
      color: #1b93e0;

      .lable-s1 {
        color: gray;
      }
    }

    .fail {
      color: #f56c6c;
    }

    .wait {
      color: #909399;
    }
  }
}

.H100 {
  //overflow: hidden;
  margin-left: 10px;
}

.row-list {
  height: calc(100% - 20px);
  height: 700px;
  overflow: auto;
  margin: -20px -20px -20px -30px !important;
  font-size: 12px;
  line-height: 20px;
}

.running-status {
  .select {
    margin-bottom: 15px;
  }

  .edit-class {
    margin-top: 10px;
  }
}
</style>
