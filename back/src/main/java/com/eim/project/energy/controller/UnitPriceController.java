package com.eim.project.energy.controller;

import com.eim.framework.web.controller.BaseController;
import com.eim.framework.web.domain.AjaxResult;
import com.eim.project.energy.entity.UnitPrice;
import com.eim.project.energy.service.UnitPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: chenchen
 * @Date: 2020/1/20
 */
@RestController
@RequestMapping("/energy/unitPrice")
public class UnitPriceController extends BaseController {
    @Autowired
    private UnitPriceService unitPriceService;

    @GetMapping("/list")
    public AjaxResult selectUnitPriceList() {
        List<Map<String, Object>> unitPriceList = unitPriceService.selectUnitPriceList();
        return AjaxResult.success(unitPriceList);
    }

    @PutMapping
    public AjaxResult edit(@RequestBody Map<String, Object> map) {
        Double waterPrice = null;
        Double electricityPrice = null;
        Double airPrice = null;
        Double steamPrice = null;
        if (map != null) {
            waterPrice = (Double) map.getOrDefault("waterPrice", null);
            electricityPrice = (Double) map.getOrDefault("electricityPrice", null);
            airPrice = (Double) map.getOrDefault("airPrice", null);
            steamPrice = (Double) map.getOrDefault("steamPrice", null);
        }
        UnitPrice unitPrice = new UnitPrice();
        unitPrice.setEnergyType("电").setPrice(electricityPrice);
        unitPriceService.updateUnitPrice(unitPrice);
        unitPrice.setEnergyType("水").setPrice(waterPrice);
        unitPriceService.updateUnitPrice(unitPrice);
        unitPrice.setEnergyType("空气").setPrice(airPrice);
        unitPriceService.updateUnitPrice(unitPrice);
        unitPrice.setEnergyType("蒸汽").setPrice(steamPrice);
        return toAjax(unitPriceService.updateUnitPrice(unitPrice));
    }
}
