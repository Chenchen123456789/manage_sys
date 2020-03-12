package com.eim.project.energy.entity;

import com.eim.framework.aspectj.lang.annotation.Excel;
import com.eim.framework.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author chenchen
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Meter extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID", cellType = Excel.ColumnType.NUMERIC)
    private Integer id;

    private Integer companyId;

    private Integer plantId;

    private Integer buildingId;

    @Excel(name = "公司名称", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String companyName;

    @Excel(name = "工厂名称", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String plantName;

    @Excel(name = "建筑名称", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String buildingName;

    @Excel(name = "设备名称", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String deviceName;

    @Excel(name = "设备ID", cellType = Excel.ColumnType.NUMERIC)
    private Integer deviceId;

    @Excel(name = "仪表编号", cellType = Excel.ColumnType.STRING)
    private String meterCode;

    @Excel(name = "仪表名称", cellType = Excel.ColumnType.STRING)
    private String meterName;

    @Excel(name = "仪表厂家", cellType = Excel.ColumnType.STRING)
    private String meterCompany;

    @Excel(name = "仪表规格", cellType = Excel.ColumnType.STRING)
    private String meterSpec;

    @Excel(name = "仪表参数", cellType = Excel.ColumnType.STRING)
    private String meterParam;

    @Excel(name = "安装地点", cellType = Excel.ColumnType.STRING)
    private String location;

    @Excel(name = "仪表描述", cellType = Excel.ColumnType.STRING)
    private String meterDescription;
}