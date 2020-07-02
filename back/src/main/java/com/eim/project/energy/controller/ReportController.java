package com.eim.project.energy.controller;

import com.eim.common.exception.CustomException;
import com.eim.common.utils.DateUtils;
import com.eim.framework.aspectj.lang.annotation.Log;
import com.eim.framework.aspectj.lang.enums.BusinessType;
import com.eim.framework.web.controller.BaseController;
import com.eim.framework.web.domain.AjaxResult;
import com.eim.framework.web.page.PageDomain;
import com.eim.framework.web.page.TableDataInfo;
import com.eim.framework.web.page.TableSupport;
import com.eim.project.energy.service.ReportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @author chenchen
 */
@RestController
@RequestMapping("/energy/report")
public class ReportController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(ReportController.class);
    @Autowired
    private ReportService reportService;

    @PreAuthorize("@ss.hasPermi('energy:report_historyDetail:list')")
    @PostMapping("/historyDetail/list")
    public TableDataInfo selectHistoryDetailList(@RequestBody Map<String, Object> map) {
        startPage();
        List<Map<String, Object>> dosageList = null;
        int queryTypeId = (int) map.get("queryTypeId");
        String tableName = "";
        switch (queryTypeId) {
            case 1:
                tableName = "T_YearData";
                break;
            case 2:
                tableName = "T_MonthData";
                break;
            case 3:
                tableName = "T_DayData";
                break;
            case 4:
                tableName = "T_HourData";
                break;
            case 5:
                tableName = "T_MinuteData";
                break;
            default:
                break;
        }
        map.put("tableName", tableName);
        dosageList = reportService.selectHistoryOfDosage(map);
        return getDataTable(dosageList);
    }

    @PreAuthorize("@ss.hasPermi('energy:report_dayDosageOfWater:list')")
    @PostMapping("/dayDosageOfWater/list")
    public TableDataInfo selectDayDosageOfWaterList(@RequestBody Map<String, Object> map) {
        startPage();
        List<Map<String, Object>> dayDosageOfWaterList = reportService.selectDayDosageOfWater(map);
        return getDataTable(dayDosageOfWaterList);
    }

    @PreAuthorize("@ss.hasPermi('energy:report_monthDosageOfWater:list')")
    @PostMapping("/monthDosageOfWater/list")
    public TableDataInfo selectMonthDosageOfWaterList(@RequestBody Map<String, Object> map) {
        startPage();
        List<Map<String, Object>> monthDosageOfWaterList = reportService.selectMonthDosageOfWater(map);
        return getDataTable(monthDosageOfWaterList);
    }

    @PreAuthorize("@ss.hasPermi('energy:report_quarterDosageOfWater:list')")
    @PostMapping("/quarterDosageOfWater/list")
    public TableDataInfo selectQuarterDosageOfWaterList(@RequestBody Map<String, Object> map) {
        startPage();
        List<Map<String, Object>> quarterDosageOfWaterList = reportService.selectQuarterDosageOfWater(map);
        return getDataTable(quarterDosageOfWaterList);
    }

    @PreAuthorize("@ss.hasPermi('energy:report_yearDosageOfWater:list')")
    @PostMapping("/yearDosageOfWater/list")
    public TableDataInfo selectYearDosageOfWaterList(@RequestBody Map<String, Object> map) {
        startPage();
        List<Map<String, Object>> yearDosageOfWaterList = reportService.selectYearDosageOfWater(map);
        return getDataTable(yearDosageOfWaterList);
    }

    @PreAuthorize("@ss.hasPermi('energy:report_dayDosageOfElectricity:list')")
    @PostMapping("/dayDosageOfElectricity/list")
    public TableDataInfo selectDayDosageOfElectricityList(@RequestBody Map<String, Object> map) {
        startPage();
        List<Map<String, Object>> dayDosageOfElectricityList = reportService.selectDayDosageOfElectricity(map);
        return getDataTable(dayDosageOfElectricityList);
    }

    @PreAuthorize("@ss.hasPermi('energy:report_monthDosageOfElectricity:list')")
    @PostMapping("/monthDosageOfElectricity/list")
    public TableDataInfo selectMonthDosageOfElectricityList(@RequestBody Map<String, Object> map) {
        startPage();
        List<Map<String, Object>> monthDosageOfElectricityList = reportService.selectMonthDosageOfElectricity(map);
        return getDataTable(monthDosageOfElectricityList);
    }

    @PreAuthorize("@ss.hasPermi('energy:report_quarterDosageOfElectricity:list')")
    @PostMapping("/quarterDosageOfElectricity/list")
    public TableDataInfo selectQuarterDosageOfElectricityList(@RequestBody Map<String, Object> map) {
        startPage();
        List<Map<String, Object>> quarterDosageOfElectricityList = reportService.selectQuarterDosageOfElectricity(map);
        return getDataTable(quarterDosageOfElectricityList);
    }

    @PreAuthorize("@ss.hasPermi('energy:report_yearDosageOfElectricity:list')")
    @PostMapping("/yearDosageOfElectricity/list")
    public TableDataInfo selectYearDosageOfElectricityList(@RequestBody Map<String, Object> map) {
        startPage();
        List<Map<String, Object>> yearDosageOfElectricityList = reportService.selectYearDosageOfElectricity(map);
        return getDataTable(yearDosageOfElectricityList);
    }

    @PreAuthorize("@ss.hasPermi('energy:report_daySettlement:list')")
    @PostMapping("/daySettlement/list")
    public TableDataInfo selectDaySettlementList(@RequestBody Map<String, Object> map) {
        startPage();
        List<Map<String, Object>> daySettlementList = reportService.selectDaySettlement(map);
        return getDataTable(daySettlementList);
    }

    @PreAuthorize("@ss.hasPermi('energy:report_monthSettlement:list')")
    @PostMapping("/monthSettlement/list")
    public TableDataInfo selectMonthSettlementList(@RequestBody Map<String, Object> map) {
        startPage();
        List<Map<String, Object>> monthSettlementList = reportService.selectMonthSettlement(map);
        return getDataTable(monthSettlementList);
    }

    @PreAuthorize("@ss.hasPermi('energy:report_quarterSettlement:list')")
    @PostMapping("/quarterSettlement/list")
    public TableDataInfo selectQuarterSettlementList(@RequestBody Map<String, Object> map) {
        startPage();
        List<Map<String, Object>> quarterSettlementList = reportService.selectQuarterSettlement(map);
        return getDataTable(quarterSettlementList);
    }

    @PreAuthorize("@ss.hasPermi('energy:report_yearSettlement:list')")
    @PostMapping("/yearSettlement/list")
    public TableDataInfo selectYearSettlementList(@RequestBody Map<String, Object> map) {
        startPage();
        List<Map<String, Object>> yearSettlementList = reportService.selectYearSettlement(map);
        return getDataTable(yearSettlementList);
    }

    @PostMapping("/buildingDosage/list")
    public AjaxResult selectBuildingDosage(@RequestBody Map<String, Object> map) {
        List<Map<String, Object>> yearList = reportService.selectBuildingYearDosage(map);
        List<Map<String, Object>> monthList = reportService.selectBuildingMonthDosage(map);
        List<Map<String, Object>> dayList = reportService.selectBuildingDayDosage(map);
        Map<String, Object> resultMap = new HashMap();
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

    @GetMapping("/yearTotal")
    public AjaxResult selectYearTotal() {
        Map<String, Object> yearTotal = reportService.selectYearTotal();
        return AjaxResult.success(yearTotal);
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


    @GetMapping("/buildingMainMPValue/list/{buildingId}")
    public AjaxResult selectBuildingMainMPValueList(@PathVariable Integer buildingId) {
        List<Map<String, Object>> buildingMainMPValueList = reportService.selectBuildingMainMPValue(buildingId);
        return AjaxResult.success(buildingMainMPValueList);
    }

    @GetMapping("/homePageChartSettingList/list")
    public AjaxResult selectHomePageChartSettingList() {
        List<Map<String, Object>> homePageChartSettingList = reportService.selectHomePageChartSettingList();
        return AjaxResult.success(homePageChartSettingList);
    }

    @Log(title = "报表管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:report_historyDetail:export')")
    @PostMapping("/historyDetail/export")
    public AjaxResult exportHistoryDetail(@RequestBody Map<String, Object> map) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<Map<String, Object>> dosageList = null;
        int queryTypeId = (int) map.get("queryTypeId");
        String tableName = "";
        switch (queryTypeId) {
            case 1:
                tableName = "T_YearData";
                break;
            case 2:
                tableName = "T_MonthData";
                break;
            case 3:
                tableName = "T_DayData";
                break;
            case 4:
                tableName = "T_HourData";
                break;
            case 5:
                tableName = "T_MinuteData";
                break;
            default:
                break;
        }
        map.put("tableName", tableName);
        dosageList = reportService.selectHistoryOfDosage(map);
        String sheetName = "建筑数据";
        String[] tableHead = {"时间", "工厂名", "建筑名", "设备名", "仪表编号", "数据位号", "数据值", "系统名", "数据点描述"};
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = reportService.initSheetHistoryDetail(workbook, sheetName, tableHead);
        Map<String, CellStyle> cellStyle = reportService.createStyles(workbook);
        int column = 1;

        for (Map<String, Object> yearDosageMap : dosageList) {
            XSSFRow row = sheet.createRow(column);
            // 设置列宽
            if (column == 8) {
                sheet.setColumnWidth(column, (int) ((22 + 0.72) * 256));
            } else {
                sheet.setColumnWidth(column, (int) ((16 + 0.72) * 256));
            }
            row.setHeight((short) (14 * 20));
            column++;
            XSSFCell cell;
            for (int i = 0; i < tableHead.length; i++) {
                cell = row.createCell(i);
                cell.setCellStyle(cellStyle.get("data"));
                switch (i) {
                    case 0:
                        cell.setCellValue(DateUtils.parseDateToStr(
                                "yyyy-MM-dd HH:mm:ss",
                                (Date) yearDosageMap.get("dataTime")));
                        break;
                    case 1:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(String.valueOf(yearDosageMap.get("plantName")));
                        break;
                    case 2:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(String.valueOf(yearDosageMap.get("buildingName")));
                        break;
                    case 3:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(String.valueOf(yearDosageMap.get("deviceName")));
                        break;
                    case 4:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(String.valueOf(yearDosageMap.get("meterCode")));
                        break;
                    case 5:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(String.valueOf(yearDosageMap.get("tagName")));
                        break;
                    case 6:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(String.valueOf(yearDosageMap.get("tagValue")));
                        break;
                    case 7:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(String.valueOf(yearDosageMap.get("systemName")));
                        break;
                    case 8:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(String.valueOf(yearDosageMap.get("description")));
                        break;
                    default:
                        break;
                }
            }
        }
        return writeWorkBook(workbook, sheetName);
    }

    @Log(title = "报表管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:report_dayDosageOfWater:export')")
    @PostMapping("/dayDosageOfWater/export")
    public AjaxResult exportDayDosageOfWater(@RequestBody Map<String, Object> map) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<Map<String, Object>> dayDosageOfWaterList = reportService.selectDayDosageOfWater(map);
        String sheetName = "用水日报";
        String[] tableHead = {"单位", "测点名", "安装地点", "水表规格", "昨天抄见", "今天抄见", "实际用量"};
        return exportDosageOfWater(map, dayDosageOfWaterList, sheetName, tableHead, 1);
    }

    @Log(title = "报表管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:report_monthDosageOfWater:export')")
    @PostMapping("/monthDosageOfWater/export")
    public AjaxResult exportMonthDosageOfWater(@RequestBody Map<String, Object> map) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<Map<String, Object>> monthDosageOfWaterList = reportService.selectMonthDosageOfWater(map);
        String sheetName = "用水月报";
        String[] tableHead = {"单位", "测点名", "安装地点", "水表规格", "上月抄见", "本月抄见", "实际用量"};
        return exportDosageOfWater(map, monthDosageOfWaterList, sheetName, tableHead, 2);
    }

    @Log(title = "报表管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:report_quarterDosageOfWater:export')")
    @PostMapping("/quarterDosageOfWater/export")
    public AjaxResult exportQuarterDosageOfWater(@RequestBody Map<String, Object> map) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<Map<String, Object>> quarterDosageOfWaterList = reportService.selectQuarterDosageOfWater(map);
        String sheetName = "用水月报";
        String[] tableHead = {"单位", "测点名", "安装地点", "水表规格", "本季度抄见", "上季度抄见", "实际用量"};
        return exportDosageOfWater(map, quarterDosageOfWaterList, sheetName, tableHead, 3);
    }

    @Log(title = "报表管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:report_yearDosageOfWater:export')")
    @PostMapping("/yearDosageOfWater/export")
    public AjaxResult exportYearDosageOfWater(@RequestBody Map<String, Object> map) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<Map<String, Object>> yearDosageOfWaterList = reportService.selectYearDosageOfWater(map);
        String sheetName = "用水年报";
        String[] tableHead = {"单位", "测点名", "安装地点", "水表规格", "去年抄见", "今年抄见", "实际用量"};
        return exportDosageOfWater(map, yearDosageOfWaterList, sheetName, tableHead, 4);
    }

    public AjaxResult exportDosageOfWater(Map<String, Object> map, List<Map<String, Object>> dosageOfWaterList,
                                          String sheetName, String[] tableHead, Integer queryTimeType) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = reportService.initSheetDosageOfWater(workbook, sheetName, tableHead, map, queryTimeType);
        Map<String, CellStyle> cellStyle = reportService.createStyles(workbook);
        int column = 2;
        for (Map<String, Object> dosageOfWaterMap : dosageOfWaterList) {
            XSSFRow row = sheet.createRow(column);
            // 设置列宽
            sheet.setColumnWidth(column, (int) ((16 + 0.72) * 256));
            row.setHeight((short) (14 * 20));
            column++;
            XSSFCell cell;
            for (int i = 0; i < tableHead.length; i++) {
                cell = row.createCell(i);
                cell.setCellStyle(cellStyle.get("data"));
                double currentTimeValue = dosageOfWaterMap.get("currentTimeValue") != null ? Double.valueOf(String.valueOf(dosageOfWaterMap.get("currentTimeValue"))) : 0;
                double preTimeValue = dosageOfWaterMap.get("preTimeValue") != null ? Double.valueOf(String.valueOf(dosageOfWaterMap.get("preTimeValue"))) : 0;
                switch (i) {
                    case 0:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(String.valueOf(dosageOfWaterMap.get("plantName")));
                        break;
                    case 1:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(String.valueOf(dosageOfWaterMap.get("tagName")));
                        break;
                    case 2:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(String.valueOf(dosageOfWaterMap.get("buildingName")));
                        break;
                    case 3:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue("");
                        break;
                    case 4:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(preTimeValue);
                        break;
                    case 5:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(currentTimeValue);
                        break;
                    case 6:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(currentTimeValue - preTimeValue);
                        break;
                    default:
                        break;
                }
            }
        }
        return writeWorkBook(workbook, sheetName);
    }

    @Log(title = "报表管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:report_dayDosageOfElectricity:export')")
    @PostMapping("/dayDosageOfElectricity/export")
    public AjaxResult exportDayDosageOfElectricity(@RequestBody Map<String, Object> map) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<Map<String, Object>> dayDosageOfElectricityList = reportService.selectDayDosageOfElectricity(map);
        String sheetName = "用电日报";
        String[] tableHead = {"单位名称", "建筑名称", "设备名称", "表号", "测点名称", "装表地点", "倍率", "昨天抄见数", "今天抄见数", "峰", "平", "谷", "总量"};
        return exportDosageOfElectricity(map, dayDosageOfElectricityList, sheetName, tableHead, 1);
    }

    @Log(title = "报表管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:report_monthDosageOfElectricity:export')")
    @PostMapping("/monthDosageOfElectricity/export")
    public AjaxResult exportMonthDosageOfElectricity(@RequestBody Map<String, Object> map) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<Map<String, Object>> monthDosageOfElectricityList = reportService.selectMonthDosageOfElectricity(map);
        String sheetName = "用电月报";
        String[] tableHead = {"单位名称", "建筑名称", "设备名称", "表号", "测点名称", "装表地点", "倍率", "上月抄见数", "本月抄见数", "峰", "平", "谷", "总量"};
        return exportDosageOfElectricity(map, monthDosageOfElectricityList, sheetName, tableHead, 2);
    }

    @Log(title = "报表管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:report_quarterDosageOfElectricity:export')")
    @PostMapping("/quarterDosageOfElectricity/export")
    public AjaxResult exportQuarterDosageOfElectricity(@RequestBody Map<String, Object> map) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<Map<String, Object>> quarterDosageOfElectricityList = reportService.selectQuarterDosageOfElectricity(map);
        String sheetName = "用电季报";
        String[] tableHead = {"单位名称", "建筑名称", "设备名称", "表号", "测点名称", "装表地点", "倍率", "上季度抄见数", "本季度抄见数", "峰", "平", "谷", "总量"};
        return exportDosageOfElectricity(map, quarterDosageOfElectricityList, sheetName, tableHead, 3);
    }

    @Log(title = "报表管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:report_yearDosageOfElectricity:export')")
    @PostMapping("/yearDosageOfElectricity/export")
    public AjaxResult exportYearDosageOfElectricity(@RequestBody Map<String, Object> map) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<Map<String, Object>> yearDosageOfElectricityList = reportService.selectYearDosageOfElectricity(map);
        String sheetName = "用电年报";
        String[] tableHead = {"单位名称", "建筑名称", "设备名称", "表号", "测点名称", "装表地点", "倍率", "去年抄见数", "今年抄见数", "峰", "平", "谷", "总量"};
        return exportDosageOfElectricity(map, yearDosageOfElectricityList, sheetName, tableHead, 4);
    }

    public AjaxResult exportDosageOfElectricity(Map<String, Object> map, List<Map<String, Object>> dosageOfElectricityList,
                                                String sheetName, String[] tableHead, Integer queryTimeType) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = reportService.initSheetDosageOfElectricity(workbook, sheetName, tableHead, map, queryTimeType);
        Map<String, CellStyle> cellStyle = reportService.createStyles(workbook);
        int column = 2;
        for (Map<String, Object> dosageOfElectricityMap : dosageOfElectricityList) {
            XSSFRow row = sheet.createRow(column);
            // 设置列宽
            row.setHeight((short) (14 * 20));
            column++;
            XSSFCell cell;
            for (int i = 0; i < tableHead.length; i++) {
                cell = row.createCell(i);
                cell.setCellStyle(cellStyle.get("data"));
                double currentTimeValue = dosageOfElectricityMap.get("currentTimeValue") != null ? Double.valueOf(String.valueOf(dosageOfElectricityMap.get("currentTimeValue"))) : 0;
                double preTimeValue = dosageOfElectricityMap.get("preTimeValue") != null ? Double.valueOf(String.valueOf(dosageOfElectricityMap.get("preTimeValue"))) : 0;
                double fValue = dosageOfElectricityMap.get("fValue") != null ? Double.valueOf(String.valueOf(dosageOfElectricityMap.get("fValue"))) : 0;
                double pValue = dosageOfElectricityMap.get("pValue") != null ? Double.valueOf(String.valueOf(dosageOfElectricityMap.get("pValue"))) : 0;
                double gValue = dosageOfElectricityMap.get("gValue") != null ? Double.valueOf(String.valueOf(dosageOfElectricityMap.get("gValue"))) : 0;
                switch (i) {
                    case 0:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(String.valueOf(dosageOfElectricityMap.get("plantName")));
                        break;
                    case 1:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(String.valueOf(dosageOfElectricityMap.get("buildingName")));
                        break;
                    case 2:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(String.valueOf(dosageOfElectricityMap.get("deviceName")));
                        break;
                    case 3:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(String.valueOf(dosageOfElectricityMap.get("meterCode")));
                        break;
                    case 4:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(String.valueOf(dosageOfElectricityMap.get("tagName")));
                        break;
                    case 5:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(String.valueOf(dosageOfElectricityMap.get("meterLocation")));
                        break;
                    case 6:
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(String.valueOf(dosageOfElectricityMap.get("meterSpec")));
                        break;
                    case 7:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(preTimeValue);
                        break;
                    case 8:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(currentTimeValue);
                        break;
                    case 9:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(fValue);
                        break;
                    case 10:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(pValue);
                        break;
                    case 11:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(gValue);
                        break;
                    case 12:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(currentTimeValue - preTimeValue);
                        break;
                    default:
                        break;
                }
            }
        }
        return writeWorkBook(workbook, sheetName);
    }

    @Log(title = "报表管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:report_daySettlement:export')")
    @PostMapping("/daySettlement/export")
    public AjaxResult exportDaySettlement(@RequestBody Map<String, Object> map) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<Map<String, Object>> daySettlementList = reportService.selectDaySettlement(map);
        String sheetName = "结算日报";
        String[] tableHead = {"单位", "建筑", "水数量", "单价", "金额", "昨天累计", "耗水累计",
                "空气数量", "单价", "金额", "昨天累计", "空气累计",
                "电数量", "单价", "金额", "昨天累计", "耗电累计",
                "蒸汽数量", "单价", "金额", "昨天累计", "蒸汽累计",
                "金额合计"};
        return exportSettlement(map, daySettlementList, sheetName, tableHead, 1);
    }

    @Log(title = "报表管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:report_monthSettlement:export')")
    @PostMapping("/monthSettlement/export")
    public AjaxResult exportMonthSettlement(@RequestBody Map<String, Object> map) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<Map<String, Object>> monthSettlementList = reportService.selectMonthSettlement(map);
        String sheetName = "结算月报";
        String[] tableHead = {"单位", "建筑", "水数量", "单价", "金额", "上月累计", "耗水累计",
                "空气数量", "单价", "金额", "上月累计", "空气累计",
                "电数量", "单价", "金额", "上月累计", "耗电累计",
                "蒸汽数量", "单价", "金额", "上月累计", "蒸汽累计",
                "金额合计"};
        return exportSettlement(map, monthSettlementList, sheetName, tableHead, 2);
    }

    @Log(title = "报表管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:report_quarterSettlement:export')")
    @PostMapping("/quarterSettlement/export")
    public AjaxResult exportQuarterSettlement(@RequestBody Map<String, Object> map) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<Map<String, Object>> quarterSettlementList = reportService.selectQuarterSettlement(map);
        String sheetName = "结算季报";
        String[] tableHead = {"单位", "建筑", "水数量", "单价", "金额", "上季度累计", "耗水累计",
                "空气数量", "单价", "金额", "上季度累计", "空气累计",
                "电数量", "单价", "金额", "上季度累计", "耗电累计",
                "蒸汽数量", "单价", "金额", "上季度累计", "蒸汽累计",
                "金额合计"};
        return exportSettlement(map, quarterSettlementList, sheetName, tableHead, 3);
    }

    @Log(title = "报表管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:report_yearSettlement:export')")
    @PostMapping("/yearSettlement/export")
    public AjaxResult exportYearSettlement(@RequestBody Map<String, Object> map) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<Map<String, Object>> yearSettlementList = reportService.selectYearSettlement(map);
        String sheetName = "结算年报";
        String[] tableHead = {"单位", "建筑", "水数量", "单价", "金额", "去年累计", "耗水累计",
                "空气数量", "单价", "金额", "去年累计", "空气累计",
                "电数量", "单价", "金额", "去年累计", "耗电累计",
                "蒸汽数量", "单价", "金额", "去年累计", "蒸汽累计",
                "金额合计"};
        return exportSettlement(map, yearSettlementList, sheetName, tableHead, 4);
    }

    public AjaxResult exportSettlement(Map<String, Object> map, List<Map<String, Object>> settlementList,
                                       String sheetName, String[] tableHead, Integer queryTimeType) {
        Double waterPrice = map.get("waterPrice") != null ? (Double) map.get("waterPrice") : 0;
        Double electricityPrice = map.get("electricityPrice") != null ? (Double) map.get("electricityPrice") : 0;
        Double airPrice = map.get("airPrice") != null ? (Double) map.get("airPrice") : 0;
        Double steamPrice = map.get("steamPrice") != null ? (Double) map.get("steamPrice") : 0;

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = reportService.initSheetSettlement(workbook, sheetName, tableHead, map, queryTimeType);
        Map<String, CellStyle> cellStyle = reportService.createStyles(workbook);
        int column = 2;
        for (Map<String, Object> settlementMap : settlementList) {
            XSSFRow row = sheet.createRow(column);
            // 设置列宽
            row.setHeight((short) (14 * 20));
            column++;
            XSSFCell cell;
            for (int i = 0; i < tableHead.length; i++) {
                cell = row.createCell(i);
                cell.setCellStyle(cellStyle.get("data"));
                double currentWaterSumValue = 0;
                if (settlementMap.get("currentWaterSumValue") != null) {
                    currentWaterSumValue = Double.valueOf(String.valueOf(settlementMap.get("currentWaterSumValue")));
                }
                double preWaterSumValue = 0;
                if (settlementMap.get("preWaterSumValue") != null) {
                    preWaterSumValue = Double.valueOf(String.valueOf(settlementMap.get("preWaterSumValue")));
                }
                double currentAirSumValue = 0;
                if (settlementMap.get("currentAirSumValue") != null) {
                    currentAirSumValue = Double.valueOf(String.valueOf(settlementMap.get("currentAirSumValue")));
                }
                double preAirSumValue = 0;
                if (settlementMap.get("preAirSumValue") != null) {
                    preAirSumValue = Double.valueOf(String.valueOf(settlementMap.get("preAirSumValue")));
                }
                double currentElectricitySumValue = 0;
                if (settlementMap.get("currentElectricitySumValue") != null) {
                    currentElectricitySumValue = Double.valueOf(String.valueOf(settlementMap.get("currentElectricitySumValue")));
                }
                double preElectricitySumValue = 0;
                if (settlementMap.get("preElectricitySumValue") != null) {
                    preElectricitySumValue = Double.valueOf(String.valueOf(settlementMap.get("preElectricitySumValue")));
                }
                double currentSteamSumValue = 0;
                if (settlementMap.get("currentSteamSumValue") != null) {
                    currentSteamSumValue = Double.valueOf(String.valueOf(settlementMap.get("currentSteamSumValue")));
                }
                double preSteamSumValue = 0;
                if (settlementMap.get("preSteamSumValue") != null) {
                    preSteamSumValue = Double.valueOf(String.valueOf(settlementMap.get("preSteamSumValue")));
                }
                switch (i) {
                    case 0:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(String.valueOf(settlementMap.get("plantName")));
                        break;
                    case 1:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(String.valueOf(settlementMap.get("buildingName")));
                        break;
                    case 2:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(currentWaterSumValue - preWaterSumValue);
                        break;
                    case 3:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(waterPrice);
                        break;
                    case 4:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(waterPrice * (currentWaterSumValue - preWaterSumValue));
                        break;
                    case 5:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(preWaterSumValue);
                        break;
                    case 6:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(currentWaterSumValue);
                        break;
                    case 7:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(currentAirSumValue - preAirSumValue);
                        break;
                    case 8:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(airPrice);
                        break;
                    case 9:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(airPrice * (currentAirSumValue - preAirSumValue));
                        break;
                    case 10:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(preAirSumValue);
                        break;
                    case 11:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(currentAirSumValue);
                        break;
                    case 12:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(currentElectricitySumValue - preElectricitySumValue);
                        break;
                    case 13:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(electricityPrice);
                        break;
                    case 14:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(electricityPrice * (currentElectricitySumValue - preElectricitySumValue));
                        break;
                    case 15:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(preElectricitySumValue);
                        break;
                    case 16:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(currentElectricitySumValue);
                        break;
                    case 17:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(currentSteamSumValue - preSteamSumValue);
                        break;
                    case 18:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(steamPrice);
                        break;
                    case 19:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(steamPrice * (currentSteamSumValue - preSteamSumValue));
                        break;
                    case 20:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(preSteamSumValue);
                        break;
                    case 21:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(currentSteamSumValue);
                        break;
                    case 22:
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(waterPrice * (currentWaterSumValue - preWaterSumValue)
                                + airPrice * (currentAirSumValue - preAirSumValue)
                                + electricityPrice * (currentElectricitySumValue - preElectricitySumValue)
                                + steamPrice * (currentSteamSumValue - preSteamSumValue));
                        break;
                    default:
                        break;
                }
            }
        }
        return writeWorkBook(workbook, sheetName);
    }

    public AjaxResult writeWorkBook(Workbook workbook, String sheetName) {
        OutputStream out = null;
        try {
            String filename = reportService.encodingFilename(sheetName);
            out = new FileOutputStream(reportService.getAbsoluteFile(filename));
            workbook.write(out);
            return AjaxResult.success(filename);
        } catch (Exception e) {
            log.error("导出Excel异常{}", e.getMessage());
            throw new CustomException("导出Excel失败，请联系网站管理员！");
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}

