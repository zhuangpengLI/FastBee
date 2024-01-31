package com.ruoyi.iot.mobile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.iot.domain.Ad;
import com.ruoyi.iot.service.IAdService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 广告Controller
 * 
 * @author renjiayue
 * @date 2022-09-03
 */
@Api(tags="a移动端----广告banner接口")
@RestController
@RequestMapping("/mobile/ad")
public class MobileAdController extends BaseController
{
    @Autowired
    private IAdService adService;

    /**
     * 查询广告列表
     */
    @ApiOperation("广告列表")
    @ApiImplicitParam(name="position",value="位置信息 1是启动页面 2是首页 3是我的页面")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam("position")Integer position)
    {
    	Ad ad = new Ad();
    	ad.setPosition(position);
//        startPage();
        List<Ad> list = adService.selectAdList(ad);
        return getDataTable(list);
    }
}
