package com.eim.project.energy.controller;

import com.eim.common.utils.SecurityUtils;
import com.eim.common.utils.poi.ExcelUtil;
import com.eim.framework.aspectj.lang.annotation.Log;
import com.eim.framework.aspectj.lang.enums.BusinessType;
import com.eim.framework.web.controller.BaseController;
import com.eim.framework.web.domain.AjaxResult;
import com.eim.framework.web.page.TableDataInfo;
import com.eim.project.energy.entity.Plant;
import com.eim.project.energy.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @Author: chenchen
 * @Date: 2020/1/20
 */
@RestController
@RequestMapping("/energy/plant")
public class PlantController extends BaseController {
    @Autowired
    private PlantService plantService;

    @PreAuthorize("@ss.hasPermi('energy:plant:list')")
    @GetMapping("/list")
    public TableDataInfo selectPlantList(Plant plant) {
        startPage();
        List<Plant> plantList = plantService.selectPlantList(plant);
        return getDataTable(plantList);
    }

    @Log(title = "工厂管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:plant:export')")
    @GetMapping("/export")
    public AjaxResult export(Plant plant) {
        List<Plant> list = plantService.selectPlantList(plant);
        ExcelUtil<Plant> util = new ExcelUtil<Plant>(Plant.class);
        return util.exportExcel(list, "工厂数据");
    }

    /**
     * 根据工厂id获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('energy:plant:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Integer id) {
        return AjaxResult.success(plantService.selectPlantById(id));
    }

    /**
     * 新增工厂
     */
    @PreAuthorize("@ss.hasPermi('energy:plant:add')")
    @Log(title = "工厂管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Plant plant) {
        Plant existPlant = plantService.selectPlantByPlantCode(plant.getPlantCode());
        if (existPlant != null) {
            return AjaxResult.error("新增工厂'" + plant.getPlantCode() + "'失败，工厂编码已存在");
        }
        plant.setCreateBy(SecurityUtils.getUsername());
        plant.setCreateTime(new Date());
        return toAjax(plantService.insertPlant(plant));
    }

    /**
     * 修改工厂
     */
    @PreAuthorize("@ss.hasPermi('energy:plant:edit')")
    @Log(title = "工厂管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Plant plant) {
        Plant existPlant = plantService.selectPlantByPlantCode(plant.getPlantCode());
        if (existPlant != null && !existPlant.getId().equals(plant.getId())) {
            return AjaxResult.error("修改工厂'" + plant.getPlantCode() + "'失败，工厂编码已存在");
        }
        plant.setUpdateBy(SecurityUtils.getUsername());
        plant.setUpdateTime(new Date());
        return toAjax(plantService.updatePlant(plant));
    }

    /**
     * 删除工厂
     */
    @PreAuthorize("@ss.hasPermi('energy:plant:remove')")
    @Log(title = "工厂管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(plantService.batchDeleteByIds(ids));
    }
}
