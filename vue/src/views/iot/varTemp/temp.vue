<template>
<div style="padding:6px;">
    <el-card v-show="showSearch" style="margin-bottom:6px;">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px" style="margin-bottom:-20px;">
            <el-form-item label="模板名称" prop="templateName">
                <el-input v-model="queryParams.templateName" placeholder="请输入模板名称" clearable size="small" @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
            <el-form-item style="float:right;">
              <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['iot:temp:add']">新增</el-button>
          </el-form-item>
        </el-form>
    </el-card>

    <el-card style="padding-bottom:100px;">
        <el-table v-loading="loading" :data="tempList" @selection-change="handleSelectionChange">
            <el-table-column label="模板名称" align="center" prop="templateName" />
            <el-table-column label="采集方式" align="center" prop="pollingMethod">
                <template slot-scope="scope">
                    <dict-tag :options="dict.type.data_collect_type" :value="scope.row.pollingMethod" />
                </template>
            </el-table-column>
            <el-table-column label="从机/变量" align="center" prop="slaveTotal,pointTotal">
                <template slot-scope="scope">
                    {{ scope.row.slaveTotal }}/{{ scope.row.pointTotal }}
                </template>
            </el-table-column>
            <el-table-column label="更新时间" align="center" prop="createTime" />
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['iot:temp:edit']">修改
                    </el-button>
                    <el-button size="mini" type="text" icon="el-icon-cpu" @click="editForm(scope.row)" v-hasPermi="['iot:temp:edit']">从机
                    </el-button>
                    <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['iot:temp:remove']">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

        <!-- 添加或修改设备采集变量模板对话框 -->
        <el-dialog :title="title" style="font-size: medium" :visible.sync="open" width="550px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="模板名称" prop="templateName" size="small">
                    <el-input v-model="form.templateName" placeholder="请输入模板名称" />
                </el-form-item>
                <el-form-item label="采集方式" prop="pollingMethod">
                    <el-radio-group v-model="form.pollingMethod" @change="typeChange(form.pollingMethod)">
                        <el-radio v-for="dict in dict.type.data_collect_type" :key="dict.value" :label="dict.value">{{ dict.label }}
                        </el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <div slot="footer" align="center" class="dialog-footer" v-if="form.isAdd">
                <el-button type="primary" @click="submitForm">下一步,配置从机和变量</el-button>
            </div>
          <div slot="footer" class="dialog-footer" v-else>
            <el-button @click="cancel">取 消</el-button>
            <el-button type="primary" @click="submitForm">确 定</el-button>
          </div>
        </el-dialog>
    </el-card>
</div>
</template>

<script>
import {
    listTemp,
    getTemp,
    delTemp,
    addTemp,
    updateTemp
} from "@/api/iot/temp";

export default {
    name: "Temp",
    dicts: ['data_collect_type'],
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
            // 设备采集变量模板表格数据
            tempList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                templateName: null,
                type: null,
                pollingMethod: null,
                slaveTotal: null,
                pointTotal: null,
                share: null,
                userId: null
            },
            // 表单参数
            form: {
                pollingMethod: "0",
                //是否新增
                isAdd : true,
            },
            // 表单校验
            rules: {
                templateName: [{
                    required: true,
                    message: "模板名称不能为空",
                    trigger: "blur"
                }],
                pollingMethod: [{
                    required: true,
                    message: "采集方式",
                    trigger: "blur"
                }]
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询设备采集变量模板列表 */
        getList() {
            this.loading = true;
            listTemp(this.queryParams).then(response => {
                this.tempList = response.rows;
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
                templateId: null,
                templateName: null,
                type: null,
                pollingMethod: "0",
                slaveTotal: null,
                pointTotal: null,
                share: null,
                createTime: null,
                createBy: null,
                updateTime: null,
                updateBy: null,
                userId: null
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
            this.ids = selection.map(item => item.templateId)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
             this.title = "添加变量模板";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const templateId = row.templateId || this.ids
            getTemp(templateId).then(response => {
                this.form = response.data;
                this.form.pollingMethod = this.form.pollingMethod+"";
                this.open = true;
                this.form.isAdd = false;
                this.title = "修改变量模板";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                  if (this.form.templateId != null) {
                    updateTemp(this.form).then(response =>{
                      this.$modal.msgSuccess("修改成功");
                      this.open = false;
                      this.getList();
                    })
                  }else {
                      addTemp(this.form).then(response => {
                        this.$modal.msgSuccess("新增成功");
                        this.open = false;
                        this.$router.push({
                          path: this.form.pollingMethod == 0 ? '/iot/varTemp-edit/point/' : '/iot/varTemp-edit/mjpoint/',
                          query: {
                            templateId: response.data,
                            templateName: this.form.templateName,
                            pollingMethod: this.form.pollingMethod,
                            share: this.form.share,
                            isOpen: 1,
                          }
                        });

                      });
                  }
                }
            });
        },

        /** 编辑按钮 */
        editForm(row) {
            this.$router.push({
              path: row.pollingMethod === 0 ? '/iot/varTemp-edit/point/' : '/iot/varTemp-edit/mjpoint/',
                query: {
                    templateId: row.templateId,
                    templateName: row.templateName,
                    pollingMethod: row.pollingMethod,
                    share: row.share,
                    isOpen: 2,
                }
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const templateIds = row.templateId || this.ids;
            this.$modal.confirm('是否确认删除变量模板编号为"' + templateIds + '"的数据项？').then(function () {
                return delTemp(templateIds);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => {});
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('iot/temp/export', {
                ...this.queryParams
            }, `temp_${new Date().getTime()}.xlsx`)
        },
        typeChange(type) {
            this.form.pollingMethod = type;
        }
    }
};
</script>
