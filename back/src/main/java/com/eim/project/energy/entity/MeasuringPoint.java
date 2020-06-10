package com.eim.project.energy.entity;

import java.util.Date;

import com.eim.framework.aspectj.lang.annotation.Excel;
import com.eim.framework.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author chenchen
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MeasuringPoint extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID", cellType = Excel.ColumnType.NUMERIC)
    private Integer id;

    private Integer companyId;

    private Integer plantId;

    private Integer buildingId;

    private Integer deviceId;

    @Excel(name = "仪表ID", cellType = Excel.ColumnType.NUMERIC, type = Excel.Type.IMPORT)
    private Integer meterId;

    @Excel(name = "公司名称", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String companyName;

    @Excel(name = "工厂名称", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String plantName;

    @Excel(name = "建筑名称", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String buildingName;

    @Excel(name = "设备名称", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String deviceName;

    @Excel(name = "仪表名称", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String meterName;

    @Excel(name = "点名", cellType = Excel.ColumnType.STRING)
    private String tagName;

    @Excel(name = "设备Tag", cellType = Excel.ColumnType.STRING)
    private String deviceTag;

    @Excel(name = "点描述", cellType = Excel.ColumnType.STRING)
    private String description;

    private String unit;

    private String rangeMin;

    private String rangeMax;

    @Excel(name = "总量点", cellType = Excel.ColumnType.STRING, readConverterExp = "0=否,1=是")
    private String totalFlag;

    @Excel(name = "主参数", cellType = Excel.ColumnType.STRING, readConverterExp = "0=否,1=是")
    private String mainTag;

    private String note;

    @Excel(name = "状态", cellType = Excel.ColumnType.STRING, readConverterExp = "0=可用,1=不可用")
    private Integer disable;

    private Integer classId;

    @Excel(name = "className", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String className;

    @Excel(name = "能源类型ID", cellType = Excel.ColumnType.NUMERIC)
    private Integer energyTypeId;

    @Excel(name = "能源类型", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String energyTypeName;

    @Excel(name = "数据分类ID", cellType = Excel.ColumnType.NUMERIC)
    private Integer signalTypeId;

    @Excel(name = "数据分类", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String signalTypeName;

    @Excel(name = "系统ID", cellType = Excel.ColumnType.NUMERIC)
    private Integer systemId;

    @Excel(name = "系统名称", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String systemName;

    @Excel(name = "报警类型", cellType = Excel.ColumnType.NUMERIC)
    private Integer alarmTag;

    private String location;

    @Excel(name = "报警LL值", cellType = Excel.ColumnType.NUMERIC)
    private Double alarmLLValue;

    @Excel(name = "报警L值", cellType = Excel.ColumnType.NUMERIC)
    private Double alarmLValue;

    @Excel(name = "报警H值", cellType = Excel.ColumnType.NUMERIC)
    private Double alarmHValue;

    @Excel(name = "报警HH值", cellType = Excel.ColumnType.NUMERIC)
    private Double alarmHHValue;

    @Excel(name = "开关量报警方式", cellType = Excel.ColumnType.NUMERIC)
    private Integer alarmDigitalValue;

    @Excel(name = "报警等级", cellType = Excel.ColumnType.NUMERIC)
    private Integer alarmLevel;

    @Excel(name = "为0报警说明", cellType = Excel.ColumnType.STRING)
    private String alarmDigitalZeroDescription;

    @Excel(name = "为1报警说明", cellType = Excel.ColumnType.STRING)
    private String alarmDigitalOneDescription;
}

