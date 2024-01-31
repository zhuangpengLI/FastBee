package com.ruoyi.iot.controller;

import java.util.HashMap;
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
import com.ruoyi.iot.domain.Family;
import com.ruoyi.iot.domain.FamilyUserRela;
import com.ruoyi.iot.mobile.respModel.FamilyStat;
import com.ruoyi.iot.mobile.respModel.RoomStat;
import com.ruoyi.iot.mobile.service.IFamilyDeviceService;
import com.ruoyi.iot.service.IFamilyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 家庭管理Controller
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
@Api(tags="b管理端----家庭相关接口")
@RestController
@RequestMapping("/iot/family")
public class FamilyController extends BaseController
{
    @Autowired
    private IFamilyService familyService;
    @Autowired
    private IFamilyDeviceService familyDeviceService;

    /**
     * 查询家庭管理列表
     */
//    @PreAuthorize("@ss.hasPermi('iot:family:list')")
//    @GetMapping("/list")
//    public TableDataInfo list(Family family)
//    {
//        startPage();
//        List<Family> list = familyService.selectFamilyList(family);
//        return getDataTable(list);
//    }
    @ApiOperation("查看我的家庭列表 -- 包含统计信息 设备/成员/房间等")
    @PreAuthorize("@ss.hasPermi('iot:family:list')")
    @GetMapping("/list")
    public TableDataInfo statList(Family family)
    {
    	startPage();
    	List<FamilyStat> list = familyService.selectFamilyStatList(family);
    	return getDataTable(list);
    }

    /**
     * 导出家庭管理列表
     */
    @PreAuthorize("@ss.hasPermi('iot:family:export')")
    @Log(title = "家庭管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Family family)
    {
        List<Family> list = familyService.selectFamilyList(family);
        ExcelUtil<Family> util = new ExcelUtil<Family>(Family.class);
        util.exportExcel(response, list, "家庭管理数据");
    }

    /**
     * 获取家庭管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:family:query')")
    @GetMapping(value = "/{familyId}")
    public AjaxResult getInfo(@PathVariable("familyId") Long familyId)
    {
        return AjaxResult.success(familyService.selectFamilyByFamilyId(familyId));
    }

    /**
     * 新增家庭管理
     */
    @PreAuthorize("@ss.hasPermi('iot:family:add')")
    @Log(title = "家庭管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Family family)
    {
    	Long userId = getUserId();
    	family.setBelongUserId(userId);
    	family.setCreateUserId(userId);
        return familyService.insertFamily(family);
    }

    /**
     * 修改家庭管理
     */
    @PreAuthorize("@ss.hasPermi('iot:family:edit')")
    @Log(title = "家庭管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Family family)
    {
        return toAjax(familyService.updateFamily(family));
    }

    /**
     * 删除家庭管理
     */
    @PreAuthorize("@ss.hasPermi('iot:family:remove')")
    @Log(title = "家庭管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{familyIds}")
    public AjaxResult remove(@PathVariable Long[] familyIds)
    {
        return toAjax(familyService.deleteFamilyByFamilyIds(familyIds));
    }
    
    @ApiOperation("查看家庭用户列表")
    @PreAuthorize("@ss.hasPermi('iot:family:userQuery')")
    @GetMapping("/userList")
    public TableDataInfo userList(@RequestParam("familyId") Long familyId)
    {
    	startPage();
        List<FamilyUserRela> list = familyService.selectUserListByFamilyIdAndUserId(familyId,null);
        return getDataTable(list);
    }
    
    @ApiOperation("查看家庭房间列表")
    @PreAuthorize("@ss.hasPermi('iot:family:roomQuery')")
    @GetMapping("/roomList")
    public TableDataInfo roomList(@RequestParam("familyId") Long familyId)
    {
    	startPage();
    	List<RoomStat> list = familyService.selectRoomAndStatByFamilyId(familyId);
        return getDataTable(list);
    }
    
    @ApiOperation("查看未分配设备数量")
    @PreAuthorize("@ss.hasPermi('iot:family:roomQuery')")
    @GetMapping("/countNotInRomm")
    public AjaxResult countNotInRomm(@RequestParam("familyId") Long familyId)
    {
    	int count = familyDeviceService.countNotInRomm(familyId);
    	HashMap<String, Object> hashMap = new HashMap<String,Object>();
    	hashMap.put("countDevice",count);
        return AjaxResult.success(hashMap);
    }
}
