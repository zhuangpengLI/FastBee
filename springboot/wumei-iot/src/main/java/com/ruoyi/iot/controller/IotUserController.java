package com.ruoyi.iot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.iot.domain.FamilyUserRela;
import com.ruoyi.iot.dto.IotUser;
import com.ruoyi.iot.service.IFamilyService;
import com.ruoyi.system.otherDto.SysUserWithFamilyStat;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 用户信息
 * 
 * @author ruoyi
 */
@Api(tags="b管理端----app用户相关接口")
@RestController
@RequestMapping("/iot/user")
public class IotUserController extends BaseController
{
    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private IFamilyService familyService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:iot:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user)
    {
        startPage();
        user.setUserType("01");//00系统用户 01 app用户
        List<SysUserWithFamilyStat> list = userService.selectUserFamilyList(user);
        list.forEach(user1->{
        	user1.setCountTotalFamily(familyService.selectCountTotalFamily(user1.getUserId()));
        	user1.setCountCreateFamily(familyService.selectCountCreateFamily(user1.getUserId()));
        	user1.setCountJoinFamily(user1.getCountTotalFamily()-user1.getCountCreateFamily());
        });
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:iot:user:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user)
    {
    	user.setUserType("01");//00系统用户 01 app用户
        List<SysUser> list = userService.selectUserList(user);
        List<IotUser> iotList = new ArrayList<IotUser>(); 
        for(SysUser user1:list){
        	IotUser iotUser = new IotUser();
        	BeanUtils.copyProperties(user1, iotUser);
        	iotList.add(iotUser);
        }
        ExcelUtil<IotUser> util = new ExcelUtil<IotUser>(IotUser.class);
        util.exportExcel(response, iotList, "用户数据");
    }


    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:iot:user:query')")
    @GetMapping(value = { "/", "/{userId}" })
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId)
    {
        AjaxResult ajax = AjaxResult.success();
        if (StringUtils.isNotNull(userId))
        {
            ajax.put(AjaxResult.DATA_TAG, userService.selectUserById(userId));
        }
        return ajax;
    }


    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:iot:user:remove')")
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        if (ArrayUtils.contains(userIds, getUserId()))
        {
            return error("当前用户不能删除");
        }
        return AjaxResult.error("暂不支持删除用户 TODO");
//        return toAjax(userService.deleteUserByIds(userIds));
    }
    
    @ApiOperation("查看用户家庭列表")
    @PreAuthorize("@ss.hasPermi('system:iot:user:familyQuery')")
    @GetMapping("/familyList")
    public TableDataInfo userList(@RequestParam("userId") Long userId)
    {
    	startPage();
        List<FamilyUserRela> list = familyService.selectUserListByFamilyIdAndUserId(null,userId);
        return getDataTable(list);
    }

}
