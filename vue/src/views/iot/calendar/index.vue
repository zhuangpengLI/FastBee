<template>
  <div class="app-container">
    <el-calendar v-model="value" id="calendar">
      <!-- 这里使用的是 2.5 slot 语法，对于新项目请使用 2.6 slot 语法-->
      <template slot="dateCell" slot-scope="{ date, data }">
        <!--自定义内容-->
        <div @click="click(date)">
          <div class="calendar-day">
            {{ data.day.split("-").slice(2).join("-") }}
          </div>
          <div v-for="item in calendarData" :key="item">
            <div
              v-if="
                item.setDate
                  .split('-')
                  .slice(1)[0]
                  .indexOf(data.day.split('-').slice(1)[0]) != -1
              "
            >
              <div
                v-if="
                  item.setDate
                    .split('-')
                    .slice(2)
                    .join('-')
                    .indexOf(data.day.split('-').slice(2).join('-')) != -1
                "
              >
                <div v-if="item.dateType === 1" class="red">休</div>
                <div v-if="item.dateType === 2" class="green">班</div>
                <el-tooltip
                  class="item"
                  effect="dark"
                  :content="item.things"
                  placement="right"
                >
                  <div class="is-selected">{{ item.things }}</div>
                </el-tooltip>
              </div>
              <div v-else></div>
            </div>
            <div v-else></div>
          </div>
        </div>
      </template>
    </el-calendar>
    <!-- 添加日历备注对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="日期类型" prop="dateType">
          <el-select v-model="form.dateType" placeholder="请选择日期类型">
            <el-option
              v-for="dict in dict.type.iot_dateType"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button type="danger" @click="handleDelete">删 除</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listcalendar,
  addsysHoliday,
  updatesysHoliday,
  delsysHoliday,
} from "@/api/iot/calendar";

export default {
  name: "calendar",
  dicts: ["iot_dateType"],
  data() {
    return {
      calendarData: [
        // { setDate: "2022-11-02", dateType: 1 },
        // { setDate: "2022-11-03", dateType: 1 },
        // { setDate: "2022-11-30", dateType: 1 },
        // { setDate: "2022-11-05", dateType: 1 },
        // { setDate: "2022-11-06", dateType: 2 },
      ],
      value: new Date(),
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {},
      queryParams: {
        year: new Date().getFullYear(),
        month: new Date().getMonth()+ 1,
      },
    };
  },
  watch: {
    value(value) {
      console.log(value);
      var d = new Date(value)
      var y = d.getFullYear();
      var m = d.getMonth() + 1;
      m = m < 10 ? "0" + m : m;
      this.queryParams.year= y
      this.queryParams.month= m
      this.getList();
    },
  },
  created() {
    this.getList();
  },
  methods: {
    click(v) {
      this.form={}
      console.log(v);
      this.open = true;
      var d = new Date(v)
      var y = d.getFullYear();
      var m = d.getMonth() + 1;
      m = m < 10 ? "0" + m : m;
      var d = d.getDate();
      d = d < 10 ? "0" + d : d;
      this.form.setDate= y + "-" + m + "-" + d 
      const params = this.calendarData.filter(item=> this.form.setDate===item.setDate)
      console.log(params)
      if(params.length>0){
        this.form.id=params[0].id
        this.form.dateType=params[0].dateType.toString()
      }
      console.log(this.form)
    },
    /** 删除按钮操作 */
    handleDelete() {
      const id = this.form.id;
      let msg = "";
      this.$modal
        .confirm('是否确认删除数据？')
        .then(function () {
          return delsysHoliday(id).then((response) => {
            msg = response.msg;
          });
        })
        .then(() => {
          this.getList();
          this.open = false;
        })
        .catch(() => {});
    },
    /** 查询系统参数列表 */
    getList() {
      this.loading = true;
      listcalendar(this.queryParams).then((response) => {
        this.calendarData = response.rows
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
        setDate: null,
        dateType: null,
      };
      this.resetForm("form");
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updatesysHoliday(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addsysHoliday(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
  },
};
</script>
<style scoped>
.el-input {
  width: 350px;
}
.dialog-footer {
  position: fixed;
  bottom: 30px;
}
.el-form-item {
  margin-top: 40px;
}
.calendar-day {
  text-align: center;
  color: #202535;
  line-height: 30px;
  font-size: 12px;
}
.is-selected {
  color: #f8a535;
  font-size: 8px;
  margin-top: 4px;
}
#calendar
  .el-button-group
  > .el-button:not(:first-child):not(:last-child):after {
  content: "当月";
}
.green {
  color: green;
}
.red {
  color: red;
}
</style>

