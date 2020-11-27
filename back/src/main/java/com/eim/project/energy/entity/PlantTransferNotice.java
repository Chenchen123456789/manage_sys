package com.eim.project.energy.entity;

import java.util.Date;

import com.eim.framework.aspectj.lang.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlantTransferNotice {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer plantId;

    private Integer buildingId;

    @Excel(name = "建筑名", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String buildingName;

    @Excel(name = "数据时间", dateFormat = "yyyy-MM-dd HH:mm:ss", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dataTime;

    private Date createTime;

    private String createBy;

    private String note;

    @Excel(name = "转出单位", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String outDepartment;

    @Excel(name = "转入单位", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String inDepartment;

    @Excel(name = "制表人", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String modifyUser;

    @Excel(name = "水数量", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private Double waterAmount;

    @Excel(name = "水单价", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private Double waterPrice;

    @Excel(name = "空气数量", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private Double airAmount;

    @Excel(name = "空气单价", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private Double airPrice;

    @Excel(name = "蒸汽数量", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private Double steamAmount;

    @Excel(name = "蒸汽单价", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private Double steamPrice;

    @Excel(name = "电-总计数量", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private Double electricityAmount;

    @Excel(name = "电-总计单价", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private Double electricityPrice;

    @Excel(name = "电-峰数量", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private Double electricityFAmount;

    @Excel(name = "电-峰单价", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private Double electricityFPrice;

    @Excel(name = "电-平数量", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private Double electricityPAmount;

    @Excel(name = "电-平单价", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private Double electricityPPrice;

    @Excel(name = "电-谷数量", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private Double electricityGAmount;

    @Excel(name = "电-谷单价", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private Double electricityGPrice;
}