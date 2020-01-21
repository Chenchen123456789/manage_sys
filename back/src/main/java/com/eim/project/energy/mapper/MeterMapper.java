package com.eim.project.energy.mapper;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.Meter;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @author chenchen
 */
@Mapper
@DataSource(value = DataSourceType.SLAVE)
public interface MeterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Meter record);

    int insertSelective(Meter record);

    Meter selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Meter record);

    int updateByPrimaryKey(Meter record);

    List<Meter> selectMeterList(Meter meter);

    int deleteByIds(Integer[] ids);

    Meter selectByMeterCode(String meterCode);
}