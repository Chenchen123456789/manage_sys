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
public class Plant extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 序号 */
    @Excel(name = "序号", cellType = Excel.ColumnType.NUMERIC)
    private Integer id;

    private Integer companyId;

    @Excel(name = "公司名称",  cellType = Excel.ColumnType.STRING)
    private String companyName;

    @Excel(name = "工厂编号",  cellType = Excel.ColumnType.STRING)
    private String plantCode;

    @Excel(name = "工厂名称",  cellType = Excel.ColumnType.STRING)
    private String plantName;

    @Excel(name = "工厂描述",  cellType = Excel.ColumnType.STRING)
    private String plantDescription;
}