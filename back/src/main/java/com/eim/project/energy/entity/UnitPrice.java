package com.eim.project.energy.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author chenchen
 */
@Data
@Accessors(chain = true)
public class UnitPrice {
    private Integer id;

    private String energyType;

    private Double price;
}