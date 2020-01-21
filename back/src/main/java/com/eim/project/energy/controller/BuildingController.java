package com.eim.project.energy.controller;

import com.eim.common.utils.SecurityUtils;
import com.eim.common.utils.poi.ExcelUtil;
import com.eim.framework.aspectj.lang.annotation.Log;
import com.eim.framework.aspectj.lang.enums.BusinessType;
import com.eim.framework.web.controller.BaseController;
import com.eim.framework.web.domain.AjaxResult;
import com.eim.framework.web.page.TableDataInfo;
import com.eim.project.energy.entity.Building;
import com.eim.project.energy.service.BuildingService;
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
@RequestMapping("/energy/building")
public class BuildingController extends BaseController {
    @Autowired
    private BuildingService buildingService;

    @PreAuthorize("@ss.hasPermi('energy:building:list')")
    @GetMapping("/list")
    public TableDataInfo selectBuildingList(Building building) {
        startPage();
        List<Building> buildingList = buildingService.selectBuildingList(building);
        return getDataTable(buildingList);
    }

    @Log(title = "建筑管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:building:export')")
    @GetMapping("/export")
    public AjaxResult export(Building building) {
        List<Building> list = buildingService.selectBuildingList(building);
        ExcelUtil<Building> util = new ExcelUtil<Building>(Building.class);
        return util.exportExcel(list, "建筑数据");
    }

    /**
     * 根据建筑id获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('energy:building:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Integer id) {
        return AjaxResult.success(buildingService.selectBuildingById(id));
    }

    /**
     * 新增建筑
     */
    @PreAuthorize("@ss.hasPermi('energy:building:add')")
    @Log(title = "建筑管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Building building) {
        Building existBuilding = buildingService.selectBuildingByBuildingCode(building.getBuildingCode());
        if (existBuilding != null) {
            return AjaxResult.error("新增建筑'" + building.getBuildingCode() + "'失败，建筑编码已存在");
        }
        building.setCreateBy(SecurityUtils.getUsername());
        building.setCreateTime(new Date());
        return toAjax(buildingService.insertBuilding(building));
    }

    /**
     * 修改建筑
     */
    @PreAuthorize("@ss.hasPermi('energy:building:edit')")
    @Log(title = "建筑管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Building building) {
        Building existBuilding = buildingService.selectBuildingByBuildingCode(building.getBuildingCode());
        if (existBuilding != null && !existBuilding.getId().equals(building.getId())) {
            return AjaxResult.error("修改建筑'" + building.getBuildingCode() + "'失败，建筑编码已存在");
        }
        building.setUpdateBy(SecurityUtils.getUsername());
        building.setUpdateTime(new Date());
        return toAjax(buildingService.updateBuilding(building));
    }

    /**
     * 删除建筑
     */
    @PreAuthorize("@ss.hasPermi('energy:building:remove')")
    @Log(title = "建筑管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(buildingService.batchDeleteByIds(ids));
    }
}
