package com.eim.project.energy.entity;

import java.util.Date;

import com.eim.framework.aspectj.lang.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 43835
 * 2020年7月17日
 */
@Data
@Accessors(chain = true)
public class TransferNotice {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer buildingId;

    @Excel(name = "数据时间", dateFormat = "yyyy-MM-dd HH:mm:ss", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dataTime;

    @Excel(name = "转入单位", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String inDepartment;

    @Excel(name = "转出单位", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String outDepartment;

    @Excel(name = "制表人", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String modifyUser;

    @Excel(name = "费用项目", readConverterExp = "1=水,2=电-峰,3=电-平,4=电-谷,5=电-总计,6=空气,7=蒸汽", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private Integer energyType;

    @Excel(name = "数量", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private Double amount;

    @Excel(name = "单价", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private Double price;

    @Excel(name = "金额", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private Double totalPrice;

    //    @Excel(name = "创建时间", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //    @Excel(name = "创建人", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String createBy;

    @Excel(name = "补充说明", cellType = Excel.ColumnType.STRING, type = Excel.Type.EXPORT)
    private String note;
}