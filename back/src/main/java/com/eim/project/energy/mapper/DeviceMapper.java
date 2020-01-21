package com.eim.project.energy.mapper;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.Device;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author chenchen
 */
@Mapper
@DataSource(value = DataSourceType.SLAVE)
public interface DeviceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Device record);

    int insertSelective(Device record);

    Device selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Device record);

    int updateByPrimaryKey(Device record);
    
    List<Device> selectDeviceList(Device device);

    int deleteByIds(Integer[] ids);

    Device selectByDeviceCode(String deviceCode);
}