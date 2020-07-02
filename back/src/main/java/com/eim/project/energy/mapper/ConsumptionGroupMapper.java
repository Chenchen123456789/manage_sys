package com.eim.project.energy.mapper;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.ConsumptionGroup;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @author 43835
 */
@Mapper
@DataSource(value = DataSourceType.SLAVE)
public interface ConsumptionGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsumptionGroup record);

    int insertSelective(ConsumptionGroup record);

    ConsumptionGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsumptionGroup record);

    int updateByPrimaryKey(ConsumptionGroup record);

    List<ConsumptionGroup> selectConsumptionGroupList(ConsumptionGroup consumptionGroup);

    int deleteByIds(Integer[] ids);

    ConsumptionGroup selectByTagName(String tagName);
}