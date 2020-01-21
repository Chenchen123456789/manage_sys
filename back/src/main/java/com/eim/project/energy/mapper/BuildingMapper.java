package com.eim.project.energy.mapper;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.Building;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author chenchen
 */
@Mapper
@DataSource(value = DataSourceType.SLAVE)
public interface BuildingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Building record);

    int insertSelective(Building record);

    Building selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Building record);

    int updateByPrimaryKey(Building record);

    List<Building> selectBuildingList(Building building);

    int deleteByIds(Integer[] ids);

    Building selectByBuildingCode(String buildingCode);
}