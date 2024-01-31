package com.ruoyi.iot.mobile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.iot.domain.CmsHelpCenter;
import com.ruoyi.iot.domain.CmsHelpCenterCategory;
import com.ruoyi.iot.service.ICmsHelpCenterCategoryService;
import com.ruoyi.iot.service.ICmsHelpCenterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 帮助中心内容Controller
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
@Api(tags="a移动端----帮助中心")
@RestController
@RequestMapping("/mobile/helpCenter")
public class MobileHelpCenterController extends BaseController
{
    @Autowired
    private ICmsHelpCenterService cmsHelpCenterService;

    @Autowired
    private ICmsHelpCenterCategoryService cmsHelpCenterCategoryService;

    /**
     * 查询帮助分类列表
     */
    @ApiOperation("帮助中心分类列表查询")
    @GetMapping("/catList")
    public TableDataInfo catList()
    {
    	CmsHelpCenterCategory cmsHelpCenterCategory = new CmsHelpCenterCategory();
        List<CmsHelpCenterCategory> list = cmsHelpCenterCategoryService.selectCmsHelpCenterCategoryList(cmsHelpCenterCategory);
        return getDataTable(list);
    }
    /**
     * 查询帮助中心内容列表
     */
    @ApiOperation("帮助中心内容查询")
    @ApiImplicitParam(name="categoryId",value="分类id")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value="categoryId",required=false)Long categoryId)
    {
    	//不分页
//    	startPage();
    	CmsHelpCenter cmsHelpCenter = new CmsHelpCenter();
    	cmsHelpCenter.setCategoryId(categoryId);
        List<CmsHelpCenter> list = cmsHelpCenterService.selectCmsHelpCenterList(cmsHelpCenter);
        return getDataTable(list);
    }
    
    @ApiOperation("帮助中心内容查询详细信息")
    @GetMapping(value = "/getInfo")
    public AjaxResult getInfo(@RequestParam("id") Long id)
    {
        return AjaxResult.success(cmsHelpCenterService.selectCmsHelpCenterById(id));
    }

}
