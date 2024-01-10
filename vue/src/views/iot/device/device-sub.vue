<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="设备编号" prop="serialNumber">
                <el-input v-model="queryParams.serialNumber" placeholder="请输入设备编号" clearable size="small"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="设备状态" prop="status">
                <el-select v-model="queryParams.status" placeholder="请选择设备状态" clearable size="small">
                    <el-option v-for="dict in dict.type.iot_device_status" :key="dict.value" :label="dict.label"
                        :value="dict.value" />
                </el-select>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-table v-loading="loading" :data="deviceList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="设备名称" align="center" prop="deviceName" />
            <el-table-column label="设备编号" align="center" prop="serialNumber" />
            <el-table-column label="网关编码" align="center" prop="gwDevCode" />
            <el-table-column label="从机地址" align="center" prop="addr" />
            <el-table-column label="固件版本" align="center" prop="firmwareVersion" />
            <el-table-column label="设备状态" align="center" prop="status">
                <template slot-scope="scope">
                    <dict-tag :options="dict.type.iot_device_status" :value="scope.row.status" />
                </template>
            </el-table-column>
            <el-table-column label="激活时间" align="center" prop="activeTime" width="180">
                <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.activeTime, '{y}-{m}-{d}') }}</span>
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
                <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                        v-hasPermi="['iot:device:add']">修改</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改设备对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="设备名" prop="deviceName">
                    <el-input v-model="form.deviceName" placeholder="请输入设备名" />
                </el-form-item>
                <el-form-item label="固件版本" prop="firmwareVersion">
                    <el-input v-model="form.firmwareVersion" placeholder="请输入固件版本" />
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
import {
    getGwDevCode,
    listDevice,
    getDevice,
    delDevice,
    addDevice,
    updateDevice
} from "@/api/iot/device";

export default {
    name: "device-sub",
    props: {
        device: {
            type: Object,
            default: null
        }
    },
    dicts: ['iot_device_status'],
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
            // 设备表格数据
            deviceList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 备注时间范围
            daterangeActiveTime: [],
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                gwDevCode: '',
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                deviceName: [{
                    required: true,
                    message: "设备名不能为空",
                    trigger: "blur"
                }],
                firmwareVersion: [{
                    required: true,
                    message: "固件版本不能为空",
                    trigger: "blur"
                }],
            }
        };
    },

    watch: {
        // 获取到父组件传递的device后，刷新列表
        device: function (newVal, oldVal) {
            this.deviceInfo = newVal;
            if (this.deviceInfo && this.deviceInfo.deviceId != 0) {
                this.queryParams.gwDevCode = this.deviceInfo.serialNumber;
                this.getList();
            }
        }
    },
    created() {
        // this.getList();
    },
    methods: {
        /** 查询设备列表 */
        getList() {
            this.loading = true;
            // this.queryParams.params = {};
            if (null != this.daterangeActiveTime && '' != this.daterangeActiveTime) {
                this.queryParams.params["beginActiveTime"] = this.daterangeActiveTime[0];
                this.queryParams.params["endActiveTime"] = this.daterangeActiveTime[1];
            }
            listDevice(this.queryParams).then(response => {
                this.deviceList = response.rows;
                this.total = response.total;
                this.loading = false;
                this.deviceList.map(d => {
                    var arr = d.serialNumber.split("_")
                    d.addr = arr[1];
                })
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
                deviceId: null,
                deviceName: null,
                productId: null,
                productName: null,
                userId: null,
                userName: null,
                tenantId: null,
                tenantName: null,
                serialNumber: null,
                firmwareVersion: null,
                status: 0,
                isShadow: null,
                rssi: null,
                isCustomerLocation: null,
                networkAddress: null,
                networkIp: null,
                thingsModelValue: null,
                longitude: null,
                latitude: null,
                activeTime: null,
                delFlag: null,
                createBy: null,
                imgUrl: null,
                createTime: null,
                updateBy: null,
                updateTime: null,
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
            this.daterangeActiveTime = [];
            this.resetForm("queryForm");
            this.handleQuery();
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.ids = selection.map(item => item.deviceId)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加设备";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const deviceId = row.deviceId || this.ids
            getDevice(deviceId).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改设备";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.deviceId != null) {
                        updateDevice(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addDevice(this.form).then(response => {
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
            const deviceIds = row.deviceId || this.ids;
            this.$modal.confirm('是否确认删除设备编号为"' + deviceIds + '"的数据项？').then(function () {
                return delDevice(deviceIds);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
    }
};
</script>
