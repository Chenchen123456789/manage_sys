package com.eim.project.energy.mapper;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.UnitPrice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
@DataSource(value = DataSourceType.SLAVE)
public interface UnitPriceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UnitPrice record);

    int insertSelective(UnitPrice record);

    UnitPrice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UnitPrice record);

    int updateByEnergyTypeSelective(UnitPrice record);

    int updateByPrimaryKey(UnitPrice record);

    List<Map<String, Object>> selectUnitPriceList();
}