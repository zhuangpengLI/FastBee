package com.ruoyi.iot.mobile.controller;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.iot.domain.Room;
import com.ruoyi.iot.mobile.model.AddOrUpdateOrDelRoomReq;
import com.ruoyi.iot.mobile.respModel.RoomStat;
import com.ruoyi.iot.mobile.service.IFamilyDeviceService;
import com.ruoyi.iot.service.IDeviceService;
import com.ruoyi.iot.service.IFamilyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 家庭管理Controller
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
@Api(tags="a移动端----房间相关接口")
@RestController
@RequestMapping("/mobile/room")
public class MobileRoomController extends BaseController
{
    @Autowired
    private IFamilyService familyService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IFamilyDeviceService familyDeviceService;

    /**
     * 查询房间列表
     */
    @ApiOperation("查看我的房间列表")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam("familyId") Long familyId)
    {
    	List<RoomStat> list = familyService.selectRoomAndStatByFamilyId(familyId,getUserId());
        return getDataTable(list);
    }
    
    /**
     * 查询房间列表
     */
    @ApiOperation("查看未分配设备数量")
    @GetMapping("/countNotInRomm")
    public AjaxResult countNotInRomm(@RequestParam("familyId") Long familyId)
    {
    	int count = familyDeviceService.countNotInRomm(familyId,getUserId());
    	HashMap<String, Object> hashMap = new HashMap<String,Object>();
    	hashMap.put("countDevice",count);
        return AjaxResult.success(hashMap);
    }

    /**
     * 新增房间管理
     */
    @ApiOperation("创建房间信息---返回房间id")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody @Valid AddOrUpdateOrDelRoomReq req)
    {
    	//新增时id为空 网关为空
    	req.setRoomId(null);
    	Room room = new Room();
    	BeanUtils.copyProperties(req, room);
    	Long userId = getUserId();
    	return familyService.insertRoom(room,userId);
//        return AjaxResult.success(room.getRoomId());
    }

    /**
     * 修改房间管理
     */
    @ApiOperation("修改房间信息 -- 暂时只有修改名称")
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody @Valid AddOrUpdateOrDelRoomReq req)
    {
    	Long roomId = req.getRoomId();
    	if(roomId==null){
    		return AjaxResult.error("房间信息为空");
    	}
    	//修改只修改名称
    	Room room = new Room();
    	room.setRoomId(roomId);
    	room.setName(req.getName());
    	return familyService.updateRoomName(room,getUserId());
//        return AjaxResult.success();
    }

    /**
     * 删除家庭管理
     */
    @ApiOperation("删除房间")
    @ApiImplicitParam(name="roomId",value="房间id")
	@DeleteMapping("/delete")
    public AjaxResult remove(@RequestParam("roomId") Long roomId)
    {
        return familyService.deleteRoomByRoomId(roomId,getUserId());
    }
}
