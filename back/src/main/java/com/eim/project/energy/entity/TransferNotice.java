package com.eim.project.energy.entity;

import java.util.Date;
import lombok.Data;

@Data
public class TransferNotice {
    private Long id;

    private Integer buildingId;

    private Date dataTime;

    private Date createTime;

    private String createBy;

    private String note;

    private String outDepartment;

    private String modifyUser;

    private Double amount;

    private Double price;

    private Integer energyType;
}