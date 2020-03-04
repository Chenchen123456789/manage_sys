package com.eim.project.energy.service;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.Meter;
import com.eim.project.energy.mapper.MeterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Author: chenchen
 * @Date: 2020/1/21
 */@Service
@DataSource(value = DataSourceType.SLAVE)
public class MeterService {
    @Autowired
    private MeterMapper meterMapper;

    public List<Meter> selectMeterList(Meter meter) {
        return meterMapper.selectMeterList(meter);
    }

    public Meter selectMeterById(Integer id) {
        return meterMapper.selectByPrimaryKey(id);
    }

    public int batchDeleteByIds(Integer[] ids) {
        return meterMapper.deleteByIds(ids);
    }

    public int updateMeter(Meter meter) {
        return meterMapper.updateByPrimaryKeySelective(meter);
    }

    public int insertMeter(Meter meter) {
        return meterMapper.insertSelective(meter);
    }

    public Meter selectMeterByMeterCode(String meterCode, Integer deviceId) {
        return meterMapper.selectByMeterCode(meterCode, deviceId);
    }
}
