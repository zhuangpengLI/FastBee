package com.ruoyi.iot.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
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
import com.ruoyi.iot.domain.SysHoliday;
import com.ruoyi.iot.service.ISysHolidayService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 系统节假日Controller
 * 
 * @author renjiayue
 * @date 2022-11-14
 */
@Api(tags = "节假日维护")
@RestController
@RequestMapping("/iot/sysHoliday")
public class SysHolidayController extends BaseController
{
    @Autowired
    private ISysHolidayService sysHolidayService;

    /**
     * 查询系统节假日列表
     */
    @PreAuthorize("@ss.hasPermi('iot:sysHoliday:query')")
    @ApiOperation("节假日查询")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "year", value = "年", required = true),
    	@ApiImplicitParam(name = "month", value = "月", required = true)
    })
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam Integer year,@RequestParam Integer month)
    {
    	SysHoliday sysHoliday = new SysHoliday();
    	if(year!=null && month!=null) {
    		DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    		LocalDate start = LocalDate.of(year, month, 1).minusMonths(1);
    		LocalDate end = LocalDate.of(year, month, 1).plusMonths(2);
    		Map<String, Object> params = sysHoliday.getParams();
    		params.put("beginSetDate",start.format(ofPattern));
    		params.put("endSetDate",end.format(ofPattern));
    	}
//        startPage();
        List<SysHoliday> list = sysHolidayService.selectSysHolidayList(sysHoliday);
        return getDataTable(list);
    }

    /**
     * 新增系统节假日
     */
    @ApiOperation("节假日新增")
    @PreAuthorize("@ss.hasPermi('iot:sysHoliday:edit')")
    @Log(title = "系统节假日", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysHoliday sysHoliday)
    {
        return toAjax(sysHolidayService.insertSysHoliday(sysHoliday));
    }

    /**
     * 修改系统节假日
     */
    @ApiOperation("节假日修改")
    @PreAuthorize("@ss.hasPermi('iot:sysHoliday:edit')")
    @Log(title = "系统节假日", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysHoliday sysHoliday)
    {
        return toAjax(sysHolidayService.updateSysHoliday(sysHoliday));
    }

    /**
     * 删除系统节假日
     */
    @ApiOperation("节假日删除")
    @PreAuthorize("@ss.hasPermi('iot:sysHoliday:edit')")
    @Log(title = "系统节假日", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysHolidayService.deleteSysHolidayByIds(ids));
    }
}
