package com.eim.project.energy.entity;

import java.util.Date;

import com.eim.framework.aspectj.lang.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author chenchen
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AlarmLog {
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "测点名", cellType = Excel.ColumnType.STRING)
    private String tagName;

    @Excel(name = "测点值", cellType = Excel.ColumnType.STRING)
    private Double tagValue;

    @Excel(name = "描述", cellType = Excel.ColumnType.STRING)
    private String description;

    @Excel(name = "报警时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", cellType = Excel.ColumnType.STRING)
    private Date alarmTime;

    @Excel(name = "确认时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", cellType = Excel.ColumnType.STRING)
    private Date ackTime;

    @Excel(name = "确认人", cellType = Excel.ColumnType.STRING)
    private String ackUser;

    @Excel(name = "状态", cellType = Excel.ColumnType.STRING)
    private String status;

    @Excel(name = "区域", cellType = Excel.ColumnType.STRING)
    private String area;

    @Excel(name = "级别", cellType = Excel.ColumnType.STRING)
    private String level;

    @Excel(name = "确认状态", cellType = Excel.ColumnType.STRING, readConverterExp = "0=未确认,1=已确认")
    private Integer ackStatus;

    @Excel(name = "恢复时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", cellType = Excel.ColumnType.STRING)
    private Date restoreTime;

    private String bgColor;

    private Date beginTime;

    private Date endTime;
}