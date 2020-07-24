package com.eim.project.energy.service;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.framework.config.EimConfig;
import com.eim.project.energy.mapper.ReportMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author chenchen
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class ReportService {
    @Autowired
    private ReportMapper reportMapper;

    public List<Map<String, Object>> selectHistoryOfDosage(Map<String, Object> map) {
        return reportMapper.selectHistoryOfDosage(map);
    }

    public List<Map<String, Object>> selectMonthDosageOfWater(Map<String, Object> map) {
        return reportMapper.selectMonthDosageOfWater(map);
    }

    public List<Map<String, Object>> selectDayDosageOfWater(Map<String, Object> map) {
        return reportMapper.selectDayDosageOfWater(map);
    }

    public List<Map<String, Object>> selectQuarterDosageOfWater(Map<String, Object> map) {
        return reportMapper.selectQuarterDosageOfWater(map);
    }

    public List<Map<String, Object>> selectYearDosageOfWater(Map<String, Object> map) {
        return reportMapper.selectYearDosageOfWater(map);
    }

    public List<Map<String, Object>> selectYearDosageOfElectricity(Map<String, Object> map) {
        return reportMapper.selectYearDosageOfElectricity(map);
    }

    public List<Map<String, Object>> selectQuarterDosageOfElectricity(Map<String, Object> map) {
        return reportMapper.selectQuarterDosageOfElectricity(map);
    }

    public List<Map<String, Object>> selectDayDosageOfElectricity(Map<String, Object> map) {
        return reportMapper.selectDayDosageOfElectricity(map);
    }

    public List<Map<String, Object>> selectMonthDosageOfElectricity(Map<String, Object> map) {
        return reportMapper.selectMonthDosageOfElectricity(map);
    }

    public List<Map<String, Object>> selectDaySettlement(Map<String, Object> map) {
        return reportMapper.selectDaySettlement(map);
    }

    public List<Map<String, Object>> selectMonthSettlement(Map<String, Object> map) {
        return reportMapper.selectMonthSettlement(map);
    }

    public List<Map<String, Object>> selectQuarterSettlement(Map<String, Object> map) {
        return reportMapper.selectQuarterSettlement(map);
    }

    public List<Map<String, Object>> selectYearSettlement(Map<String, Object> map) {
        return reportMapper.selectYearSettlement(map);
    }

    public List<Map<String, Object>> selectClassList() {
        return reportMapper.selectClassList();
    }

    public List<Map<String, Object>> selectEnergyTypeList() {
        return reportMapper.selectEnergyTypeList();
    }

    public List<Map<String, Object>> selectSignalTypeList() {
        return reportMapper.selectSignalTypeList();
    }

    public List<Map<String, Object>> selectSystemList() {
        return reportMapper.selectSystemList();
    }

    public List<Map<String, Object>> selectBuildingYearDosage(Map<String, Object> map) {
        return reportMapper.selectBuildingYearDosage(map);
    }

    public List<Map<String, Object>> selectBuildingMonthDosage(Map<String, Object> map) {
        return reportMapper.selectBuildingMonthDosage(map);
    }

    public List<Map<String, Object>> selectBuildingDayDosage(Map<String, Object> map) {
        return reportMapper.selectBuildingDayDosage(map);
    }

    public List<Map<String, Object>> selectBuildingMainMPValue(Integer buildingId) {
        return reportMapper.selectBuildingMainMPValue(buildingId);
    }

    public List<Map<String, Object>> selectHomePageChartSettingList() {
        return reportMapper.selectHomePageChartSettingList();
    }

    public Map<String, Object> selectYearTotal() {
        return reportMapper.selectYearTotal();
    }

    /**
     * 获取下载路径
     *
     * @param filename 文件名称
     */
    public String getAbsoluteFile(String filename) {
        String downloadPath = EimConfig.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }

    /**
     * 编码文件名
     */
    public String encodingFilename(String filename) {
        filename = UUID.randomUUID().toString() + "_" + filename + ".xlsx";
        return filename;
    }

    /**
     * 创建表头 和 sheetName
     *
     * @param wb 工作薄对象,sheetName  sheet名字
     * @return wb
     */
    public XSSFSheet initSheetHistoryDetail(XSSFWorkbook wb, String sheetName, String[] tableHead) {
        XSSFSheet sheet = wb.createSheet(sheetName);
        //设置第一行
        XSSFRow row = sheet.createRow(0);
        sheet.setColumnWidth(0, (int) ((16 + 0.72) * 256));
        row.setHeight((short) (14 * 20));
        Map<String, CellStyle> styles = createStyles(wb);
        Cell cell;
        for (int i = 0; i < tableHead.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(styles.get("header"));
            cell.setCellValue(tableHead[i]);
        }
        return sheet;
    }

    /**
     * 创建表格样式
     *
     * @param wb 工作薄对象
     * @return 样式列表
     */
    public Map<String, CellStyle> createStyles(Workbook wb) {
        // 写入各条记录,每条记录对应excel表中的一行
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);
        return styles;
    }

    public XSSFSheet initSheetDosageOfWater(XSSFWorkbook workbook, String sheetName, String[] tableHead, Map map, Integer queryTimeType) {
        String queryTime = String.valueOf(map.get("queryTime"));
        Integer queryTimeQuarter = (Integer) map.get("queryTimeQuarter");
        String[] quarterOptions = new String[]{"第一季度", "第二季度", "第三季度", "第四季度"};
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate localDate = LocalDate.parse(queryTime, fmt);
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
        String dataTime = "";
        switch (queryTimeType) {
            case 1:
                dataTime = "数据时间：" + year + "年" + month + "月" + day + "日";
                break;
            case 2:
                dataTime = "数据时间：" + year + "年" + month + "月";
                break;
            case 3:
                dataTime = "数据时间：" + year + "年 " + quarterOptions[queryTimeQuarter - 1];
                break;
            case 4:
                dataTime = "数据时间：" + year + "年";
                break;
            default:
                break;
        }
        XSSFSheet sheet = workbook.createSheet(sheetName);
        Map<String, CellStyle> styles = createStyles(workbook);

        //设置第一行
        int column = 0;
        sheet.setColumnWidth(column, (int) ((16 + 0.72) * 256));
        XSSFRow row1 = sheet.createRow(column);
        row1.setHeight((short) (14 * 20));
        Cell cell;


        for (int i = 0; i < tableHead.length; i++) {
            if (i < 4) {
                cell = row1.createCell(i);
                cell.setCellStyle(styles.get("header"));
                cell.setCellValue(tableHead[i]);
            }
            if (i == 4) {
                cell = row1.createCell(i);
                cell.setCellValue(dataTime);
                cell.setCellStyle(styles.get("header"));
                CellRangeAddress region1 = new CellRangeAddress(0, 0, 4, 6);
                sheet.addMergedRegion(region1);
                break;
            }
        }
        column++;
        sheet.setColumnWidth(column, (int) ((16 + 0.72) * 256));
        XSSFRow row2 = sheet.createRow(column);
        row2.setHeight((short) (14 * 20));
        for (int i = 0; i < tableHead.length; i++) {
            if (i < 4) {
                CellRangeAddress region2 = new CellRangeAddress(0, 1, i, i);
                sheet.addMergedRegion(region2);
            } else {
                cell = row2.createCell(i);
                cell.setCellStyle(styles.get("header"));
                cell.setCellValue(tableHead[i]);
            }
        }
        return sheet;
    }

    public XSSFSheet initSheetDosageOfElectricity(XSSFWorkbook workbook, String sheetName, String[] tableHead, Map map, Integer queryTimeType) {
        XSSFSheet sheet = workbook.createSheet(sheetName);
        Map<String, CellStyle> styles = createStyles(workbook);

        String queryTime = String.valueOf(map.get("queryTime"));
        Integer queryTimeQuarter = (Integer) map.get("queryTimeQuarter");
        String[] quarterOptions = new String[]{"第一季度", "第二季度", "第三季度", "第四季度"};
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate localDate = LocalDate.parse(queryTime, fmt);
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
        String dataTime = "";
        switch (queryTimeType) {
            case 1:
                dataTime = "数据时间：" + year + "年" + month + "月" + day + "日";
                break;
            case 2:
                dataTime = "数据时间：" + year + "年" + month + "月";
                break;
            case 3:
                dataTime = "数据时间：" + year + "年 " + quarterOptions[queryTimeQuarter - 1];
                break;
            case 4:
                dataTime = "数据时间：" + year + "年";
                break;
            default:
                break;
        }

        //设置第一行
        int column = 0;
        XSSFRow row1 = sheet.createRow(column);
        row1.setHeight((short) (14 * 20));
        Cell cell;

        for (int i = 0; i < tableHead.length; i++) {
            if (i < 9) {
                cell = row1.createCell(i);
                cell.setCellStyle(styles.get("header"));
                cell.setCellValue(tableHead[i]);
            }
            if (i == 9) {
                cell = row1.createCell(i);
                cell.setCellValue(dataTime);
                cell.setCellStyle(styles.get("header"));
                CellRangeAddress region1 = new CellRangeAddress(0, 0, 9, 12);
                sheet.addMergedRegion(region1);
                break;
            }
        }
        column++;
        XSSFRow row2 = sheet.createRow(column);
        row2.setHeight((short) (14 * 20));
        for (int i = 0; i < tableHead.length; i++) {
            if (i < 9) {
                CellRangeAddress region2 = new CellRangeAddress(0, 1, i, i);
                sheet.addMergedRegion(region2);
            } else {
                cell = row2.createCell(i);
                cell.setCellStyle(styles.get("header"));
                cell.setCellValue(tableHead[i]);
            }
        }
        for (int i = 0; i < 12; i++) {
            sheet.setColumnWidth(i, (int) ((16 + 0.72) * 256));
        }
        return sheet;
    }

    public XSSFSheet initSheetSettlement(XSSFWorkbook workbook, String sheetName, String[] tableHead, Map map, Integer queryTimeType) {
        XSSFSheet sheet = workbook.createSheet(sheetName);
        Map<String, CellStyle> styles = createStyles(workbook);

        String queryTime = String.valueOf(map.get("queryTime"));
        Integer queryTimeQuarter = (Integer) map.get("queryTimeQuarter");
        String[] quarterOptions = new String[]{"第一季度", "第二季度", "第三季度", "第四季度"};
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate localDate = LocalDate.parse(queryTime, fmt);
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
        String dataTime = "";
        switch (queryTimeType) {
            case 1:
                dataTime = "结算时间范围：" + year + "年" + month + "月" + day + "日";
                break;
            case 2:
                dataTime = "结算时间范围：" + year + "年" + month + "月";
                break;
            case 3:
                dataTime = "结算时间范围：" + year + "年 " + quarterOptions[queryTimeQuarter - 1];
                break;
            case 4:
                dataTime = "结算时间范围：" + year + "年";
                break;
            default:
                break;
        }

        //设置第一行
        Cell cell;
        int column = 0;
        XSSFRow row0 = sheet.createRow(column);
        row0.setHeight((short) (14 * 20));
        cell = row0.createCell(0);
        cell.setCellValue(dataTime);
        cell.setCellStyle(styles.get("header"));
        CellRangeAddress region0 = new CellRangeAddress(0, 0, 0, 1);
        sheet.addMergedRegion(region0);
        column++;

        XSSFRow row1 = sheet.createRow(column);
        row0.setHeight((short) (14 * 20));
        for (int i = 0; i < tableHead.length; i++) {
            if (i <= 1) {
                cell = row1.createCell(i);
                cell.setCellStyle(styles.get("header"));
                cell.setCellValue(tableHead[i]);
            }
            if (i == 2) {
                cell = row1.createCell(i);
                cell.setCellValue("水");
                cell.setCellStyle(styles.get("header"));
                CellRangeAddress region1 = new CellRangeAddress(1, 1, 2, 6);
                sheet.addMergedRegion(region1);
            }
            if (i == 7) {
                cell = row1.createCell(i);
                cell.setCellValue("空气");
                cell.setCellStyle(styles.get("header"));
                CellRangeAddress region1 = new CellRangeAddress(1, 1, 7, 11);
                sheet.addMergedRegion(region1);
            }
            if (i == 12) {
                cell = row1.createCell(i);
                cell.setCellValue("电");
                cell.setCellStyle(styles.get("header"));
                CellRangeAddress region1 = new CellRangeAddress(1, 1, 12, 16);
                sheet.addMergedRegion(region1);
            }
            if (i == 17) {
                cell = row1.createCell(i);
                cell.setCellValue("蒸汽");
                cell.setCellStyle(styles.get("header"));
                CellRangeAddress region1 = new CellRangeAddress(1, 1, 17, 21);
                sheet.addMergedRegion(region1);
            }
            if (i == 22) {
                cell = row1.createCell(i);
                cell.setCellStyle(styles.get("header"));
                cell.setCellValue(tableHead[i]);
            }
        }
        column++;
        XSSFRow row2 = sheet.createRow(column);
        row2.setHeight((short) (14 * 20));
        for (int i = 0; i < tableHead.length; i++) {
            if (i <= 1 || i == 22) {
                CellRangeAddress region2 = new CellRangeAddress(1, 2, i, i);
                sheet.addMergedRegion(region2);
            }
            if (i > 1 && i < 22) {
                cell = row2.createCell(i);
                cell.setCellStyle(styles.get("header"));
                cell.setCellValue(tableHead[i]);
            }
        }
        for (int i = 0; i < 23; i++) {
            sheet.setColumnWidth(i, (int) ((16 + 0.72) * 256));
        }
        return sheet;
    }

    public List<Map<String, Object>> selectElectricityOfFPGValue(Map<String, Object> map) {
        return reportMapper.selectElectricityOfFPGValue(map);
    }
}
