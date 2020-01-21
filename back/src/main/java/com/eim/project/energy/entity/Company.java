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
public class Company extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 序号 */
    @Excel(name = "序号", cellType = Excel.ColumnType.NUMERIC)
    private Integer id;

    /** 公司编号 */
    @Excel(name = "公司编号", cellType = Excel.ColumnType.STRING)
    private String companyCode;

    /** 公司名称 */
    @Excel(name = "公司名称", cellType = Excel.ColumnType.STRING)
    private String companyName;

    /** 公司描述 */
    @Excel(name = "公司描述", cellType = Excel.ColumnType.STRING)
    private String companyDescription;
}