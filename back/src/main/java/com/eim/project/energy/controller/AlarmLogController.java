package com.eim.project.energy.controller;

import com.eim.common.utils.SecurityUtils;
import com.eim.common.utils.poi.ExcelUtil;
import com.eim.framework.aspectj.lang.annotation.Log;
import com.eim.framework.aspectj.lang.enums.BusinessType;
import com.eim.framework.security.service.TokenService;
import com.eim.framework.web.controller.BaseController;
import com.eim.framework.web.domain.AjaxResult;
import com.eim.framework.web.page.PageDomain;
import com.eim.framework.web.page.TableDataInfo;
import com.eim.framework.web.page.TableSupport;
import com.eim.project.energy.entity.AlarmLog;
import com.eim.project.energy.entity.Building;
import com.eim.project.energy.service.AlarmLogService;
import com.eim.project.energy.service.MeasuringPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: chenchen
 * @Date: 2020/1/20
 */
@RestController
@RequestMapping("/energy/alarmLog")
public class AlarmLogController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(AlarmLogController.class);
    @Autowired
    private AlarmLogService alarmLogService;

    @GetMapping("/measuringPoint/list")
    public TableDataInfo selectMeasuringPointList() {
        List<Map<String, Object>> measuringPointList = alarmLogService.selectMeasuringPointList();
        return getDataTable(measuringPointList);
    }

    @PostMapping(value = "/measuringPoint")
    public AjaxResult getInfo(@RequestBody Map<String, Object> map) {
        String tagName = (String) map.getOrDefault("tagName","");
        return AjaxResult.success(alarmLogService.selectMeasuringPointByTagName(tagName));
    }


    @PostMapping("/list")
    public TableDataInfo selectAlarmLogList(@RequestBody Map<String, Object> map) {
        startPage();
        List<AlarmLog> alarmLogList = alarmLogService.selectAlarmLogList(map);
        return getDataTable(alarmLogList);
    }

    @Log(title = "报警记录管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:alarmLog:export')")
    @PostMapping("/export")
    public AjaxResult export(@RequestBody Map<String, Object> map) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<AlarmLog> list = alarmLogService.selectAlarmLogList(map);
        ExcelUtil<AlarmLog> util = new ExcelUtil<AlarmLog>(AlarmLog.class);
        return util.exportExcel(list, "报警记录数据");
    }

    /**
     * 根据报警记录id获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('energy:alarmLog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(alarmLogService.selectAlarmLogById(id));
    }

    @PreAuthorize("@ss.hasPermi('energy:alarmLog:edit')")
    @Log(title = "报警记录管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{ids}")
    public AjaxResult edit(@PathVariable Long[] ids) {
        String ackUser = SecurityUtils.getUsername();
        return toAjax(alarmLogService.updateAlarmLog(ids, ackUser));
    }

    @GetMapping("/getUnAckAlarmLog")
    public AjaxResult selectUnAckAlarmLog() {
        return AjaxResult.success(alarmLogService.selectUnAckAlarmLog());
    }
}
