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

    @Excel(name = "序号", cellType = Excel.ColumnType.NUMERIC)
    private Integer id;

    private Integer companyId;

    private Integer plantId;

    private Integer buildingId;

    private Integer deviceId;

    private Integer meterId;

    @Excel(name = "公司名称", cellType = Excel.ColumnType.STRING)
    private String companyName;

    @Excel(name = "工厂名称", cellType = Excel.ColumnType.STRING)
    private String plantName;

    @Excel(name = "建筑名称", cellType = Excel.ColumnType.STRING)
    private String buildingName;

    @Excel(name = "设备名称", cellType = Excel.ColumnType.STRING)
    private String deviceName;

    @Excel(name = "仪表名称", cellType = Excel.ColumnType.STRING)
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

    @Excel(name = "总量点", cellType = Excel.ColumnType.STRING)
    private String totalFlag;

    @Excel(name = "主参数", cellType = Excel.ColumnType.STRING)
    private String mainTag;

    private String note;

    @Excel(name = "状态", cellType = Excel.ColumnType.STRING)
    private Integer disable;

    private Integer classId;

    @Excel(name = "className", cellType = Excel.ColumnType.STRING)
    private String className;

    private Integer energyTypeId;

    @Excel(name = "能源类型", cellType = Excel.ColumnType.STRING)
    private String energyTypeName;

    private Integer signalTypeId;

    @Excel(name = "信号类型", cellType = Excel.ColumnType.STRING)
    private String signalTypeName;

    private Integer systemId;

    @Excel(name = "系统名称", cellType = Excel.ColumnType.STRING)
    private String systemName;

    private String location;
}

