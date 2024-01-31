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
import com.ruoyi.iot.domain.SceneRecord;
import com.ruoyi.iot.service.ISceneRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 场景执行记录Controller
 * 
 * @author renjiayue
 * @date 2022-10-09
 */
@RestController
@RequestMapping("/iot/sceneRecord")
public class SceneRecordController extends BaseController
{
    @Autowired
    private ISceneRecordService sceneRecordService;

    /**
     * 查询场景执行记录列表
     */
    @PreAuthorize("@ss.hasPermi('iot:sceneRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(SceneRecord sceneRecord)
    {
        startPage();
        List<SceneRecord> list = sceneRecordService.selectSceneRecordList(sceneRecord);
        return getDataTable(list);
    }

    /**
     * 导出场景执行记录列表
     */
    @PreAuthorize("@ss.hasPermi('iot:sceneRecord:export')")
    @Log(title = "场景执行记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SceneRecord sceneRecord)
    {
        List<SceneRecord> list = sceneRecordService.selectSceneRecordList(sceneRecord);
        ExcelUtil<SceneRecord> util = new ExcelUtil<SceneRecord>(SceneRecord.class);
        util.exportExcel(response, list, "场景执行记录数据");
    }

    /**
     * 获取场景执行记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:sceneRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sceneRecordService.selectSceneRecordById(id));
    }

    /**
     * 新增场景执行记录
     */
    @PreAuthorize("@ss.hasPermi('iot:sceneRecord:add')")
    @Log(title = "场景执行记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SceneRecord sceneRecord)
    {
        return toAjax(sceneRecordService.insertSceneRecord(sceneRecord));
    }

    /**
     * 修改场景执行记录
     */
    @PreAuthorize("@ss.hasPermi('iot:sceneRecord:edit')")
    @Log(title = "场景执行记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SceneRecord sceneRecord)
    {
        return toAjax(sceneRecordService.updateSceneRecord(sceneRecord));
    }

    /**
     * 删除场景执行记录
     */
    @PreAuthorize("@ss.hasPermi('iot:sceneRecord:remove')")
    @Log(title = "场景执行记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sceneRecordService.deleteSceneRecordByIds(ids));
    }
}
