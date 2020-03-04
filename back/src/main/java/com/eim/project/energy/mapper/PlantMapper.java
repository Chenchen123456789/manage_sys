package com.eim.project.energy.mapper;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.Plant;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @author chenchen
 */
@Mapper
@DataSource(value = DataSourceType.SLAVE)
public interface PlantMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Plant record);

    int insertSelective(Plant record);

    Plant selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Plant record);

    int updateByPrimaryKey(Plant record);

    List<Plant> selectPlantList(Plant plant);

    int deleteByIds(Integer[] ids);

    Plant selectByPlantCode(String plantCode, Integer companyId);
}