package com.eim.project.system.mapper;

import com.eim.project.system.domain.SysHomeSetting;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chenchen
 */
@Mapper
public interface SysHomeSettingMapper {

    public SysHomeSetting getHomeSettingDetail();

    public int updateHomeSetting(SysHomeSetting sysHomeSetting);

    int selectTempCount();

    int updateTempCount(int countNumber);
}
