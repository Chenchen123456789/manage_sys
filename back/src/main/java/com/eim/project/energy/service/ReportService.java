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

    public List<Map<String, Object>> selectHistoryOfYearDosage(Map<String, Object> map) {
        return reportMapper.selectHistoryOfYearDosage(map);
    }

    public List<Map<String, Object>> selectHistoryOfMonthDosage(Map<String, Object> map) {
        return reportMapper.selectHistoryOfMonthDosage(map);
    }

    public List<Map<String, Object>> selectHistoryOfDayDosage(Map<String, Object> map) {
        return reportMapper.selectHistoryOfDayDosage(map);
    }

    public List<Map<String, Object>> selectHistoryOfHourDosage(Map<String, Object> map) {
        return reportMapper.selectHistoryOfHourDosage(map);
    }

    public List<Map<String, Object>> selectMonthDosageOfWater(Map<String, Object> map) {
        return reportMapper.selectMonthDosageOfWater(map);
    }

    public List<Map<String, Object>> selectMonthDosageOfElectricity(Map<String, Object> map) {
        return reportMapper.selectMonthDosageOfElectricity(map);
    }

    public List<Map<String, Object>> selectMonthSettlement(Map<String, Object> map) {
        return reportMapper.selectMonthSettlement(map);
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

    public List<Map<String, Object>> selectBuildingDistribution() {
        return reportMapper.selectBuildingDistribution();
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

    public XSSFSheet initSheetMonthDosageOfWater(XSSFWorkbook workbook, String sheetName, String[] tableHead, Map map) {
        String queryTime = String.valueOf(map.get("queryTime"));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate localDate = LocalDate.parse(queryTime, fmt);
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        String dataTime = "数据时间：" + year + "年" + month + "月";
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

    public XSSFSheet initSheetMonthDosageOfElectricity(XSSFWorkbook workbook, String sheetName, String[] tableHead) {
        XSSFSheet sheet = workbook.createSheet(sheetName);
        Map<String, CellStyle> styles = createStyles(workbook);
        //设置第一行
        int column = 0;
        XSSFRow row1 = sheet.createRow(column);
        row1.setHeight((short) (14 * 20));
        Cell cell;

        for (int i = 0; i < tableHead.length; i++) {
            if (i < 8) {
                cell = row1.createCell(i);
                cell.setCellStyle(styles.get("header"));
                cell.setCellValue(tableHead[i]);
            }
            if (i == 8) {
                cell = row1.createCell(i);
                cell.setCellValue("本月用电数");
                cell.setCellStyle(styles.get("header"));
                CellRangeAddress region1 = new CellRangeAddress(0, 0, 8, 10);
                sheet.addMergedRegion(region1);
                break;
            }
        }
        column++;
        XSSFRow row2 = sheet.createRow(column);
        row2.setHeight((short) (14 * 20));
        for (int i = 0; i < tableHead.length; i++) {
            if (i < 8) {
                CellRangeAddress region2 = new CellRangeAddress(0, 1, i, i);
                sheet.addMergedRegion(region2);
            } else {
                cell = row2.createCell(i);
                cell.setCellStyle(styles.get("header"));
                cell.setCellValue(tableHead[i]);
            }
        }
        for (int i = 0; i < 11; i++) {
            sheet.setColumnWidth(i, (int) ((16 + 0.72) * 256));
        }
        return sheet;
    }

    public XSSFSheet initSheetMonthSettlement(XSSFWorkbook workbook, String sheetName, String[] tableHead) {
        XSSFSheet sheet = workbook.createSheet(sheetName);
        Map<String, CellStyle> styles = createStyles(workbook);
        //设置第一行
        int column = 0;
        XSSFRow row1 = sheet.createRow(column);
        row1.setHeight((short) (14 * 20));
        Cell cell;
        for (int i = 0; i < tableHead.length; i++) {
            if (i == 0) {
                cell = row1.createCell(i);
                cell.setCellStyle(styles.get("header"));
                cell.setCellValue(tableHead[i]);
            }
            if (i == 1) {
                cell = row1.createCell(i);
                cell.setCellValue("水");
                cell.setCellStyle(styles.get("header"));
                CellRangeAddress region1 = new CellRangeAddress(0, 0, 1, 5);
                sheet.addMergedRegion(region1);
            }
            if (i == 6) {
                cell = row1.createCell(i);
                cell.setCellValue("空气");
                cell.setCellStyle(styles.get("header"));
                CellRangeAddress region1 = new CellRangeAddress(0, 0, 6, 10);
                sheet.addMergedRegion(region1);
            }
            if (i == 11) {
                cell = row1.createCell(i);
                cell.setCellValue("电");
                cell.setCellStyle(styles.get("header"));
                CellRangeAddress region1 = new CellRangeAddress(0, 0, 11, 15);
                sheet.addMergedRegion(region1);
            }
            if (i == 16) {
                cell = row1.createCell(i);
                cell.setCellValue("蒸汽");
                cell.setCellStyle(styles.get("header"));
                CellRangeAddress region1 = new CellRangeAddress(0, 0, 16, 20);
                sheet.addMergedRegion(region1);
            }
            if (i == 21) {
                cell = row1.createCell(i);
                cell.setCellStyle(styles.get("header"));
                cell.setCellValue(tableHead[i]);
            }
        }
        column++;
        XSSFRow row2 = sheet.createRow(column);
        row2.setHeight((short) (14 * 20));
        for (int i = 0; i < tableHead.length; i++) {
            if (i == 0 || i == 21) {
                CellRangeAddress region2 = new CellRangeAddress(0, 1, i, i);
                sheet.addMergedRegion(region2);
            }
            if (i > 0 && i < 21) {
                cell = row2.createCell(i);
                cell.setCellStyle(styles.get("header"));
                cell.setCellValue(tableHead[i]);
            }
        }
        for (int i = 0; i < 22; i++) {
            sheet.setColumnWidth(i, (int) ((16 + 0.72) * 256));
        }
        return sheet;
    }
}
