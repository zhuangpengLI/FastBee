package com.ruoyi.iot.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.iot.domain.DboxSwitch;
import com.ruoyi.iot.mapper.DboxMapper;
import com.ruoyi.iot.domain.Dbox;
import com.ruoyi.iot.service.IDboxService;

/**
 * 配电箱配置Service业务层处理
 * 
 * @author renjiayue
 * @date 2022-10-08
 */
@Service
public class DboxServiceImpl implements IDboxService 
{
    @Autowired
    private DboxMapper dboxMapper;

    /**
     * 查询配电箱配置
     * 
     * @param id 配电箱配置主键
     * @return 配电箱配置
     */
    @Override
    public Dbox selectDboxById(Long id)
    {
        return dboxMapper.selectDboxById(id);
    }
    
    @Override
    public Dbox selectDboxByType(String dboxType)
    {
    	Dbox dbox = new Dbox();
    	dbox.setDboxType(dboxType);
		List<Dbox> list = selectDboxList(dbox);
		if(list.isEmpty()) {
			return null;
		}
    	return list.get(0);
    }

    /**
     * 查询配电箱配置列表
     * 
     * @param dbox 配电箱配置
     * @return 配电箱配置
     */
    @Override
    public List<Dbox> selectDboxList(Dbox dbox)
    {
        return dboxMapper.selectDboxList(dbox);
    }

    /**
     * 新增配电箱配置
     * 
     * @param dbox 配电箱配置
     * @return 结果
     */
    @Transactional
    @Override
    public int insertDbox(Dbox dbox)
    {
        dbox.setCreateTime(DateUtils.getNowDate());
        int rows = dboxMapper.insertDbox(dbox);
//        insertDboxSwitch(dbox);
        return rows;
    }

    /**
     * 修改配电箱配置
     * 
     * @param dbox 配电箱配置
     * @return 结果
     */
    @Transactional
    @Override
    public int updateDbox(Dbox dbox)
    {
        dbox.setUpdateTime(DateUtils.getNowDate());
//        dboxMapper.deleteDboxSwitchByDboxId(dbox.getId());
//        insertDboxSwitch(dbox);
        return dboxMapper.updateDbox(dbox);
    }

    /**
     * 批量删除配电箱配置
     * 
     * @param ids 需要删除的配电箱配置主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteDboxByIds(Long[] ids)
    {
        dboxMapper.deleteDboxSwitchByDboxIds(ids);
        return dboxMapper.deleteDboxByIds(ids);
    }

    /**
     * 删除配电箱配置信息
     * 
     * @param id 配电箱配置主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteDboxById(Long id)
    {
        dboxMapper.deleteDboxSwitchByDboxId(id);
        return dboxMapper.deleteDboxById(id);
    }

    /**
     * 新增配电箱空开素材信息
     * 
     * @param dbox 配电箱配置对象
     */
    public void insertDboxSwitch(Dbox dbox)
    {
        List<DboxSwitch> dboxSwitchList = dbox.getDboxSwitchList();
        Long id = dbox.getId();
        if (StringUtils.isNotNull(dboxSwitchList))
        {
            List<DboxSwitch> list = new ArrayList<DboxSwitch>();
            for (DboxSwitch dboxSwitch : dboxSwitchList)
            {
                dboxSwitch.setDboxId(id);
                list.add(dboxSwitch);
            }
            if (list.size() > 0)
            {
                dboxMapper.batchDboxSwitch(list);
            }
        }
    }
}
