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
    public AjaxResult edit(@RequestBody Map<String, Double> map) {
        Double waterPrice = null;
        Double electricityPrice = null;
        Double electricityFPrice = null;
        Double electricityPPrice = null;
        Double electricityGPrice = null;
        Double airPrice = null;
        Double steamPrice = null;
        if (map != null) {
            waterPrice = map.getOrDefault("waterPrice", 0.0);
            electricityPrice = map.getOrDefault("electricityPrice", 0.0);
            airPrice =  map.getOrDefault("airPrice", 0.0);
            steamPrice =  map.getOrDefault("steamPrice", 0.0);
            electricityFPrice =  map.getOrDefault("electricityFPrice", 0.0);
            electricityPPrice =  map.getOrDefault("electricityPPrice", 0.0);
            electricityGPrice =   map.getOrDefault("electricityGPrice", 0.0);
        }
        UnitPrice unitPrice = new UnitPrice();
        unitPrice.setEnergyType("电").setPrice(electricityPrice);
        unitPriceService.updateUnitPrice(unitPrice);
        unitPrice.setEnergyType("水").setPrice(waterPrice);
        unitPriceService.updateUnitPrice(unitPrice);
        unitPrice.setEnergyType("空气").setPrice(airPrice);
        unitPriceService.updateUnitPrice(unitPrice);
        unitPrice.setEnergyType("蒸汽").setPrice(steamPrice);
        unitPriceService.updateUnitPrice(unitPrice);
        unitPrice.setEnergyType("电-峰").setPrice(electricityFPrice);
        unitPriceService.updateUnitPrice(unitPrice);
        unitPrice.setEnergyType("电-平").setPrice(electricityPPrice);
        unitPriceService.updateUnitPrice(unitPrice);
        unitPrice.setEnergyType("电-谷").setPrice(electricityGPrice);

        return toAjax(unitPriceService.updateUnitPrice(unitPrice));
    }
}
