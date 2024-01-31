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
import com.ruoyi.iot.domain.DeviceOtaUpdate;
import com.ruoyi.iot.service.IDeviceOtaUpdateService;

import io.swagger.annotations.Api;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 设备升级Controller
 * 
 * @author renjiayue
 * @date 2022-09-27
 */
@Api(tags="a移动端----网关升级接口")
@RestController
@RequestMapping("/mobile/deviceOta")
public class MobileDeviceOtaUpdateController extends BaseController
{
    @Autowired
    private IDeviceOtaUpdateService deviceOtaUpdateService;

    /**
     * 查询设备升级列表
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam("familyId") Long familyId)
    {
    	DeviceOtaUpdate deviceOtaUpdate = new DeviceOtaUpdate();
    	deviceOtaUpdate.setStatus(1);//升级成功的数据
    	deviceOtaUpdate.setFamilyId(familyId);
        startPage();
        List<DeviceOtaUpdate> list = deviceOtaUpdateService.selectDeviceOtaUpdateList(deviceOtaUpdate);
        return getDataTable(list);
    }

}
