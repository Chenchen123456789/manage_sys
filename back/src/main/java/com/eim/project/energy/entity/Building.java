package com.eim.project.energy.entity;

import com.eim.framework.aspectj.lang.annotation.Excel;
import com.eim.framework.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author chenchen
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Building extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID", cellType = Excel.ColumnType.NUMERIC)
    private Integer id;

    private Integer companyId;

    @Excel(name = "公司名称", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String companyName;

    @Excel(name = "工厂ID", cellType = Excel.ColumnType.NUMERIC)
    private Integer plantId;

    @Excel(name = "工厂名称", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String plantName;

    @Excel(name = "建筑编号", cellType = Excel.ColumnType.STRING)
    private String buildingCode;

    @Excel(name = "建筑名称", cellType = Excel.ColumnType.STRING)
    private String buildingName;

    @Excel(name = "建筑描述", cellType = Excel.ColumnType.STRING)
    private String buildingDescription;
}