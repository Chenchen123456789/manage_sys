package com.eim.project.energy.service;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.Building;
import com.eim.project.energy.mapper.BuildingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Author: chenchen
 * @Date: 2020/1/20
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class BuildingService {
    @Autowired
    private BuildingMapper buildingMapper;

    public List<Building> selectBuildingList(Building building) {
        return buildingMapper.selectBuildingList(building);
    }

    public Building selectBuildingById(Integer id) {
        return buildingMapper.selectByPrimaryKey(id);
    }

    public int batchDeleteByIds(Integer[] ids) {
        return buildingMapper.deleteByIds(ids);
    }

    public int updateBuilding(Building building) {
        return buildingMapper.updateByPrimaryKeySelective(building);
    }

    public int insertBuilding(Building building) {
        return buildingMapper.insertSelective(building);
    }

    public Building selectBuildingByBuildingCode(String buildingCode, Integer plantId) {
        return buildingMapper.selectByBuildingCode(buildingCode, plantId);
    }
}
