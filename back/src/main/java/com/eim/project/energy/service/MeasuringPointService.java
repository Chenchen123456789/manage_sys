package com.eim.project.energy.service;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.MeasuringPoint;
import com.eim.project.energy.mapper.MeasuringPointMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: chenchen
 * @Date: 2020/1/21
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class MeasuringPointService {
    @Autowired
    private MeasuringPointMapper measuringPointMapper;

    public List<MeasuringPoint> selectMeasuringPointList(MeasuringPoint measuringPoint) {
        return measuringPointMapper.selectMeasuringPointList(measuringPoint);
    }

    public MeasuringPoint selectMeasuringPointById(Integer id) {
        return measuringPointMapper.selectByPrimaryKey(id);
    }

    public int batchDeleteByIds(Integer[] ids) {
        return measuringPointMapper.deleteByIds(ids);
    }

    public int updateMeasuringPoint(MeasuringPoint measuringPoint) {
        return measuringPointMapper.updateByPrimaryKeySelective(measuringPoint);
    }

    public int insertMeasuringPoint(MeasuringPoint measuringPoint) {
        return measuringPointMapper.insertSelective(measuringPoint);
    }

    public MeasuringPoint selectMeasuringPointByTagName(String tagName) {
        return measuringPointMapper.selectByTagName(tagName);
    }

    public int selectMeasuringPointCount() {
        return measuringPointMapper.selectMeasuringPointCount();
    }
}
