package com.eim.project.system.controller;

import com.eim.framework.web.controller.BaseController;
import com.eim.framework.web.domain.AjaxResult;
import com.eim.project.system.domain.SysHomeSetting;
import com.eim.project.system.service.impl.SysHomeSettingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenchen
 */
@RestController
@RequestMapping("/system/homeSetting")
public class SysHomeSettingController extends BaseController {

    @Autowired
    private SysHomeSettingServiceImpl sysHomeSettingService;

    @GetMapping("/list")
    public AjaxResult getSysHomeSetting(){
        return AjaxResult.success(sysHomeSettingService.getHomeSettingDetail());
    }

    @PutMapping()
    public AjaxResult updateHomeSetting(@RequestBody SysHomeSetting sysHomeSetting){
        return toAjax(sysHomeSettingService.updateHomeSetting(sysHomeSetting));
    }
}
