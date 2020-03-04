package com.eim.project.energy.service;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.UnitPrice;
import com.eim.project.energy.mapper.UnitPriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author chenchen
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class UnitPriceService {
    @Autowired
    private UnitPriceMapper unitPriceMapper;

    public int updateUnitPrice(UnitPrice unitPrice) {
        return unitPriceMapper.updateByEnergyTypeSelective(unitPrice);
    }

    public List<Map<String,Object>> selectUnitPriceList() {
        return unitPriceMapper.selectUnitPriceList();
    }
}
