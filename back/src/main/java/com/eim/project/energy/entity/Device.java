package com.eim.project.energy.entity;

import com.eim.framework.aspectj.lang.annotation.Excel;
import com.eim.framework.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author chenchen
 * @date 2020/1/20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Device extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID", cellType = Excel.ColumnType.NUMERIC)
    private Integer id;

    private Integer companyId;

    private Integer plantId;

    @Excel(name = "公司名称", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String companyName;

    @Excel(name = "工厂名称", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String plantName;

    @Excel(name = "建筑ID", cellType = Excel.ColumnType.NUMERIC)
    private Integer buildingId;

    @Excel(name = "建筑名称", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String buildingName;

    @Excel(name = "设备编号", cellType = Excel.ColumnType.STRING)
    private String deviceCode;

    @Excel(name = "设备名称", cellType = Excel.ColumnType.STRING)
    private String deviceName;

    @Excel(name = "设备厂家", cellType = Excel.ColumnType.STRING)
    private String deviceCompany;

    @Excel(name = "设备规格", cellType = Excel.ColumnType.STRING)
    private String deviceSpec;

    @Excel(name = "安装地点", cellType = Excel.ColumnType.STRING)
    private String location;

    @Excel(name = "通讯协议", cellType = Excel.ColumnType.STRING)
    private String commProtocol;

    @Excel(name = "通讯参数", cellType = Excel.ColumnType.STRING)
    private String commParam;

    @Excel(name = "通讯IP", cellType = Excel.ColumnType.STRING)
    private String commLink;

    @Excel(name = "设备描述", cellType = Excel.ColumnType.STRING)
    private String deviceDescription;
}