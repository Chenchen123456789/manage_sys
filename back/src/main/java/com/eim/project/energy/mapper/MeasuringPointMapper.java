package com.eim.project.energy.mapper;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.MeasuringPoint;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author chenchen
 */

@Mapper
@DataSource(value = DataSourceType.SLAVE)
public interface MeasuringPointMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MeasuringPoint record);

    int insertSelective(MeasuringPoint record);

    MeasuringPoint selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MeasuringPoint record);

    int updateByPrimaryKey(MeasuringPoint record);

    List<MeasuringPoint> selectMeasuringPointList(MeasuringPoint MeasuringPoint);

    int deleteByIds(Integer[] ids);

    MeasuringPoint selectByTagName(String tagName);

    int selectMeasuringPointCount();
}