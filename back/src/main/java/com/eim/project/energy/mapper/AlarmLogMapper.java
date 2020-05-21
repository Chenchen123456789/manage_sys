package com.eim.project.energy.mapper;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.AlarmLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author chenchen
 */
@Mapper
@DataSource(value = DataSourceType.SLAVE)
public interface AlarmLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AlarmLog record);

    int insertSelective(AlarmLog record);

    AlarmLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Long[] ids, String ackUser);

    int updateByPrimaryKey(AlarmLog record);

    List<AlarmLog> selectAlarmLogList(Map<String, Object> map);

    List<Map<String, Object>> selectAlarmTagValueList();

    AlarmLog selectAlarmLogByTagName(String tagName);

    int updateCurrentAlarmLogList(String tagName);

    int selectUnAckAlarmLog();

    List<Map<String, Object>> selectMeasuringPointList();

    Map<String, Object> selectMeasuringPointByTagName(String tagName);
}