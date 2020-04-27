package com.eim.project.energy.mapper;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: chenchen
 * @Date: 2020/4/15
 */
@Mapper
@DataSource(value = DataSourceType.SLAVE)
public interface MqttTestMapper {
    List<Map<String,Object>> selectList();
}
