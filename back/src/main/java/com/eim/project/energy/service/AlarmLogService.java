package com.eim.project.energy.service;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.AlarmLog;
import com.eim.project.energy.entity.Building;
import com.eim.project.energy.mapper.AlarmLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: chenchen
 * @Date: 2020/5/11
 */

@Service
@DataSource(value = DataSourceType.SLAVE)
public class AlarmLogService {
    @Autowired
    private AlarmLogMapper alarmLogMapper;

    public List<Map<String, Object>> selectAlarmTagValueList(){
        return alarmLogMapper.selectAlarmTagValueList();
    }

    public int insertAlarmLog(AlarmLog alarmLog){
        return  alarmLogMapper.insertSelective(alarmLog);
    }

    public int updateAlarmLog(Long[] ids, String ackUser){
        return  alarmLogMapper.updateByPrimaryKeySelective(ids, ackUser);
    }

    public AlarmLog selectAlarmLogById(Long id){
        return  alarmLogMapper.selectByPrimaryKey(id);
    }

    public List<AlarmLog> selectAlarmLogList(Map<String, Object> map) {
        return alarmLogMapper.selectAlarmLogList(map);
    }

    public AlarmLog selectAlarmLogByTagName(String tagName) {
        return alarmLogMapper.selectAlarmLogByTagName(tagName);
    }

    public int updateCurrentAlarmLogList(String tagName) {
        return alarmLogMapper.updateCurrentAlarmLogList(tagName);
    }

    public int selectUnAckAlarmLog() {
        return alarmLogMapper.selectUnAckAlarmLog();
    }

     public List<Map<String, Object>> selectMeasuringPointList(){
        return alarmLogMapper.selectMeasuringPointList();
     }

    public Map<String, Object> selectMeasuringPointByTagName(String tagName) {
        return alarmLogMapper.selectMeasuringPointByTagName(tagName);
    }
}
