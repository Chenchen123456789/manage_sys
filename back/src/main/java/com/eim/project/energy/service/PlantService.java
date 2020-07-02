package com.eim.project.energy.service;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.Plant;
import com.eim.project.energy.mapper.PlantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Author: chenchen
 * @Date: 2020/1/20
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class PlantService {
    @Autowired
    private PlantMapper plantMapper;

    public List<Plant> selectPlantList(Plant plant) {
        return plantMapper.selectPlantList(plant);
    }

    public Plant selectPlantById(Integer id) {
        return plantMapper.selectByPrimaryKey(id);
    }

    public int batchDeleteByIds(Integer[] ids) {
        return plantMapper.deleteByIds(ids);
    }

    public int updatePlant(Plant plant) {
        return plantMapper.updateByPrimaryKeySelective(plant);
    }

    public int insertPlant(Plant plant) {
        return plantMapper.insertSelective(plant);
    }

    public Plant selectPlantByPlantCode(String plantCode, Integer companyId) {
        return plantMapper.selectByPlantCode(plantCode, companyId);
    }
}
