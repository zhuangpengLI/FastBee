<template>
  <div style="padding-left:20px;">
    <el-row :gutter="10">
      <el-col :span="14">
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="warning" plain icon="el-icon-refresh" size="mini" @click="getList">刷新</el-button>
          </el-col>
          <el-tag type="danger" style="margin-left:15px;">开源版本不支持该功能,请前往购买商业版本</el-tag>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped lang="scss">

</style>
