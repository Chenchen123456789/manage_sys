package com.eim.project.energy.service;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.mapper.MqttTestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: chenchen
 * @Date: 2020/4/15
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class MqttTestService {
    @Autowired
    private MqttTestMapper mqttTestMapper;

    public List<Map<String, Object>> selectList() {
        return mqttTestMapper.selectList();
    }
}
