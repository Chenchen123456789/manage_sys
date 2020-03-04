package com.eim.project.energy.controller;

import com.eim.framework.web.controller.BaseController;
import com.eim.framework.web.domain.AjaxResult;
import com.eim.framework.web.domain.server.Sys;
import com.eim.framework.web.page.TableDataInfo;
import com.eim.project.energy.service.ReportService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenchen
 */
@RestController
@RequestMapping("/energy/report")
public class ReportController extends BaseController {
    @Autowired
    private ReportService reportService;

    @PreAuthorize("@ss.hasPermi('energy:report_historyDetail:list')")
    @PostMapping("/historyDetail/list")
    public TableDataInfo selectHistoryDetailList(@RequestBody Map<String, Object> map) {
        startPage();
        List<Map<String, Object>> yearDosageList = null;
        int queryTypeId = (int) map.get("queryTypeId");
        switch (queryTypeId) {
            case 1:
                yearDosageList = reportService.selectHistoryOfYearDosage(map);
                break;
            case 2:
                yearDosageList = reportService.selectHistoryOfMonthDosage(map);
                break;
            case 3:
                yearDosageList = reportService.selectHistoryOfDayDosage(map);
                break;
            case 4:
                yearDosageList = reportService.selectHistoryOfHourDosage(map);
                break;
            default:
                break;
        }
        return getDataTable(yearDosageList);
    }

    @PreAuthorize("@ss.hasPermi('energy:report_monthDosageOfWater:list')")
    @PostMapping("/monthDosageOfWater/list")
    public TableDataInfo selectMonthDosageOfWaterList(@RequestBody Map<String, Object> map) {
        startPage();
        List<Map<String, Object>> monthDosageOfWaterList = reportService.selectMonthDosageOfWater(map);
        return getDataTable(monthDosageOfWaterList);
    }

    @PreAuthorize("@ss.hasPermi('energy:report_monthDosageOfElectricity:list')")
    @PostMapping("/monthDosageOfElectricity/list")
    public TableDataInfo selectMonthDosageOfElectricityList(@RequestBody Map<String, Object> map) {
        startPage();
        List<Map<String, Object>> monthDosageOfElectricityList = reportService.selectMonthDosageOfElectricity(map);
        return getDataTable(monthDosageOfElectricityList);
    }

    @PreAuthorize("@ss.hasPermi('energy:report_monthSettlement:list')")
    @PostMapping("/monthSettlement/list")
    public TableDataInfo selectMonthSettlementList(@RequestBody Map<String, Object> map) {
        startPage();
        List<Map<String, Object>> monthSettlementList = reportService.selectMonthSettlement(map);
        return getDataTable(monthSettlementList);
    }

    @PostMapping("/buildingDosage/list")
    public AjaxResult selectBuildingDosage(@RequestBody Map<String, Object> map) {
        List<Map<String, Object>> yearList = reportService.selectBuildingYearDosage(map);
        List<Map<String, Object>> monthList = reportService.selectBuildingMonthDosage(map);
        List<Map<String, Object>> dayList = reportService.selectBuildingDayDosage(map);
        Map resultMap = new HashMap();
        resultMap.put("yearDosage", null);
        resultMap.put("monthDosage", null);
        resultMap.put("dayDosage", null);
        if (yearList != null && yearList.size() > 0) {
            resultMap.put("yearDosage", yearList.get(0));
        }
        if (monthList != null && monthList.size() > 0) {
            resultMap.put("monthDosage", monthList.get(0));
        }
        if (dayList != null && dayList.size() > 0) {
            resultMap.put("dayDosage", dayList.get(0));
        }
        return AjaxResult.success(resultMap);
    }

    @GetMapping("/buildingDosage/list")
    public AjaxResult selectBuildingDosage() {
        List<Map<String, Object>> yearList = reportService.selectBuildingYearDosage(new HashMap<>());
        List<Map<String, Object>> monthList = reportService.selectBuildingMonthDosage(new HashMap<>());
        List<Map<String, Object>> dayList = reportService.selectBuildingDayDosage(new HashMap<>());
        Map resultMap = new HashMap();
        resultMap.put("yearDosage", null);
        resultMap.put("monthDosage", null);
        resultMap.put("dayDosage", null);
        if (yearList != null && yearList.size() > 0) {
            resultMap.put("yearDosage", yearList);
        }
        if (monthList != null && monthList.size() > 0) {
            resultMap.put("monthDosage", monthList);
        }
        if (dayList != null && dayList.size() > 0) {
            resultMap.put("dayDosage", dayList);
        }
        return AjaxResult.success(resultMap);
    }

    @GetMapping("/class/list")
    public TableDataInfo selectClassList() {
        startPage();
        List<Map<String, Object>> classList = reportService.selectClassList();
        return getDataTable(classList);
    }

    @GetMapping("/energyType/list")
    public TableDataInfo selectEnergyTypeList() {
        startPage();
        List<Map<String, Object>> energyTypeList = reportService.selectEnergyTypeList();
        return getDataTable(energyTypeList);
    }

    @GetMapping("/signalType/list")
    public TableDataInfo selectSignalTypeList() {
        startPage();
        List<Map<String, Object>> signalTypeList = reportService.selectSignalTypeList();
        return getDataTable(signalTypeList);
    }

    @GetMapping("/system/list")
    public TableDataInfo selectSystemList() {
        startPage();
        List<Map<String, Object>> systemList = reportService.selectSystemList();
        return getDataTable(systemList);
    }

    @GetMapping("/buildingDistribution/list")
    public AjaxResult selectBuildingDistributionList() {
        List<Map<String, Object>> buildingDistributionList = reportService.selectBuildingDistribution();
        return AjaxResult.success(buildingDistributionList);
    }

    @GetMapping("/buildingMainMPValue/list/{buildingId}")
    public AjaxResult selectBuildingMainMPValueList(@PathVariable Integer buildingId ) {
        List<Map<String, Object>> buildingMainMPValueList = reportService.selectBuildingMainMPValue(buildingId);
        return AjaxResult.success(buildingMainMPValueList);
    }

    @GetMapping("/homePageChartSettingList/list")
    public AjaxResult selectHomePageChartSettingList() {
        List<Map<String, Object>> homePageChartSettingList = reportService.selectHomePageChartSettingList();
        return AjaxResult.success(homePageChartSettingList);
    }
}
