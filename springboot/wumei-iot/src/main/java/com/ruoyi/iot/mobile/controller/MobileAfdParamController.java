package com.ruoyi.iot.mobile.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.iot.domain.AfdParam;
import com.ruoyi.iot.service.IAfdParamService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 语音维护Controller
 * 
 * @author renjiayue
 * @date 2022-12-25
 */
@Api(tags="aaaa移动端----语音参数接口")
@RestController
@RequestMapping("/mobile/afdParam")
public class MobileAfdParamController extends BaseController
{
    @Autowired
    private IAfdParamService afdParamService;

    /**
     * 查询语音维护列表
     */
    @ApiOperation("语音列表")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="deviceType",value="支持设备类型 1语音开关 2语音窗帘"),
    	@ApiImplicitParam(name="aver",value="语音版本")
    })
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam("deviceType")Integer deviceType,@RequestParam("aver") Integer aver)
    {
    	AfdParam afdParam = new AfdParam();
    	afdParam.setDeviceType(deviceType);
    	afdParam.setAver(aver);
    	afdParam.setEnable(1);
    	//前端不分页
//        startPage();
        List<AfdParam> list = afdParamService.selectAfdParamList2(afdParam);
        return getDataTable(list);
    }

}
