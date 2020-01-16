package com.eim.project.system.service.impl;

import com.eim.project.system.domain.SysHomeSetting;
import com.eim.project.system.mapper.SysHomeSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenchen
 */
@Service
public class SysHomeSettingServiceImpl {

    @Autowired
    private SysHomeSettingMapper sysHomeSettingMapper;

    public SysHomeSetting getHomeSettingDetail(){
        return sysHomeSettingMapper.getHomeSettingDetail();
    }

    public int updateHomeSetting(SysHomeSetting sysHomeSetting){
        return  sysHomeSettingMapper.updateHomeSetting(sysHomeSetting);
    }
}
