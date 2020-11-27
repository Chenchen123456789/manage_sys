package com.eim.project.energy.entity;

import com.eim.framework.aspectj.lang.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 43835
 */
@Data
@Accessors(chain = true)
public class ConsumptionGroup {
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID", cellType = Excel.ColumnType.NUMERIC)
    private Integer id;

    @Excel(name = "测点名", cellType = Excel.ColumnType.STRING)
    private String tagName;

    @Excel(name = "测点描述", cellType = Excel.ColumnType.STRING)
    private String tagNameDesc;

    @Excel(name = "计量等级", cellType = Excel.ColumnType.STRING)
    private Integer measureLevel;

    private Integer buildingId;

//    @Excel(name = "能耗建筑", type = Excel.Type.EXPORT, cellType = Excel.ColumnType.STRING)
    private String buildingName;

    @Excel(name = "大功率", cellType = Excel.ColumnType.STRING, readConverterExp = "1=大功率电器,0=未分类")
    private Integer deviceType;

    @Excel(name = "能源类型", type = Excel.Type.EXPORT, cellType = Excel.ColumnType.STRING, readConverterExp = "1=水,2=电")
    private Integer energyTypeId;

    @Excel(name = "能耗划分建筑Id", cellType = Excel.ColumnType.NUMERIC,type = Excel.Type.IMPORT)
    private Integer energyBuildingId;

    @Excel(name = "能耗划分建筑", type = Excel.Type.EXPORT, cellType = Excel.ColumnType.STRING)
    private String energyBuildingName;

    private String energyTypeName;
}