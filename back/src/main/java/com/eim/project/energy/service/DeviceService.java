package com.eim.project.energy.service;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.Device;
import com.eim.project.energy.mapper.DeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: chenchen
 * @Date: 2020/1/20
 */

@Service
@DataSource(value = DataSourceType.SLAVE)
public class DeviceService {
    @Autowired
    private DeviceMapper deviceMapper;

    public List<Device> selectDeviceList(Device device) {
        return deviceMapper.selectDeviceList(device);
    }

    public Device selectDeviceById(Integer id) {
        return deviceMapper.selectByPrimaryKey(id);
    }

    public int batchDeleteByIds(Integer[] ids) {
        return deviceMapper.deleteByIds(ids);
    }

    public int updateDevice(Device device) {
        return deviceMapper.updateByPrimaryKeySelective(device);
    }

    public int insertDevice(Device device) {
        return deviceMapper.insertSelective(device);
    }

    public Device selectDeviceByDeviceCode(String deviceCode) {
        return deviceMapper.selectByDeviceCode(deviceCode);
    }
}