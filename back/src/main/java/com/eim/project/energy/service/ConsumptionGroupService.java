package com.eim.project.energy.service;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.ConsumptionGroup;
import com.eim.project.energy.mapper.ConsumptionGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author chenchen
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class ConsumptionGroupService {

    @Autowired
    private ConsumptionGroupMapper consumptionGroupMapper;

    public List<ConsumptionGroup> selectConsumptionGroupList(ConsumptionGroup consumptionGroup) {
        return consumptionGroupMapper.selectConsumptionGroupList(consumptionGroup);
    }

    public ConsumptionGroup selectConsumptionGroupById(Integer id) {
        return consumptionGroupMapper.selectByPrimaryKey(id);
    }

    public int batchDeleteByIds(Integer[] ids) {
        return consumptionGroupMapper.deleteByIds(ids);
    }

    public int updateConsumptionGroup(ConsumptionGroup consumptionGroup) {
        return consumptionGroupMapper.updateByPrimaryKeySelective(consumptionGroup);
    }

    public int insertConsumptionGroup(ConsumptionGroup consumptionGroup) {
        return consumptionGroupMapper.insertSelective(consumptionGroup);
    }

    public ConsumptionGroup selectByTagName(String tagName) {
        return consumptionGroupMapper.selectByTagName(tagName);
    }
}
