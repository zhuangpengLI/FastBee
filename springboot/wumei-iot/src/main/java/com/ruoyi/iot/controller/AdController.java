package com.ruoyi.iot.controller;

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
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.iot.domain.Ad;
import com.ruoyi.iot.service.IAdService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 广告Controller
 * 
 * @author renjiayue
 * @date 2022-09-03
 */
@RestController
@RequestMapping("/iot/ad")
public class AdController extends BaseController
{
    @Autowired
    private IAdService adService;

    /**
     * 查询广告列表
     */
    @PreAuthorize("@ss.hasPermi('iot:ad:list')")
    @GetMapping("/list")
    public TableDataInfo list(Ad ad)
    {
        startPage();
        List<Ad> list = adService.selectAdList(ad);
        return getDataTable(list);
    }

    /**
     * 导出广告列表
     */
    @PreAuthorize("@ss.hasPermi('iot:ad:export')")
    @Log(title = "广告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Ad ad)
    {
        List<Ad> list = adService.selectAdList(ad);
        ExcelUtil<Ad> util = new ExcelUtil<Ad>(Ad.class);
        util.exportExcel(response, list, "广告数据");
    }

    /**
     * 获取广告详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:ad:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(adService.selectAdById(id));
    }

    /**
     * 新增广告
     */
    @PreAuthorize("@ss.hasPermi('iot:ad:add')")
    @Log(title = "广告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Ad ad)
    {
        return toAjax(adService.insertAd(ad));
    }

    /**
     * 修改广告
     */
    @PreAuthorize("@ss.hasPermi('iot:ad:edit')")
    @Log(title = "广告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Ad ad)
    {
        return toAjax(adService.updateAd(ad));
    }

    /**
     * 删除广告
     */
    @PreAuthorize("@ss.hasPermi('iot:ad:remove')")
    @Log(title = "广告", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(adService.deleteAdByIds(ids));
    }
}
