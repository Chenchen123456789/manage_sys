package com.eim.project.energy.controller;

import com.eim.common.utils.SecurityUtils;
import com.eim.common.utils.poi.ExcelUtil;
import com.eim.framework.aspectj.lang.annotation.Log;
import com.eim.framework.aspectj.lang.enums.BusinessType;
import com.eim.framework.web.controller.BaseController;
import com.eim.framework.web.domain.AjaxResult;
import com.eim.framework.web.page.TableDataInfo;
import com.eim.project.energy.entity.MeasuringPoint;
import com.eim.project.energy.service.MeasuringPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @Author: chenchen
 * @Date: 2020/1/21
 */
@RestController
@RequestMapping("/energy/measuringPoint")
public class MeasuringPointController extends BaseController {
    @Autowired
    private MeasuringPointService measuringPointService;

    @PreAuthorize("@ss.hasPermi('energy:measuringPoint:list')")
    @GetMapping("/list")
    public TableDataInfo selectMeasuringPointList(MeasuringPoint measuringPoint) {
        startPage();
        List<MeasuringPoint> measuringPointList = measuringPointService.selectMeasuringPointList(measuringPoint);
        return getDataTable(measuringPointList);
    }

    @Log(title = "设备管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:measuringPoint:export')")
    @GetMapping("/export")
    public AjaxResult export(MeasuringPoint measuringPoint) {
        List<MeasuringPoint> list = measuringPointService.selectMeasuringPointList(measuringPoint);
        ExcelUtil<MeasuringPoint> util = new ExcelUtil<MeasuringPoint>(MeasuringPoint.class);
        return util.exportExcel(list, "设备数据");
    }

    /**
     * 根据设备id获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('energy:measuringPoint:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Integer id) {
        return AjaxResult.success(measuringPointService.selectMeasuringPointById(id));
    }

    /**
     * 新增设备
     */
    @PreAuthorize("@ss.hasPermi('energy:measuringPoint:add')")
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MeasuringPoint measuringPoint) {
        MeasuringPoint existMeasuringPoint = measuringPointService.selectMeasuringPointByTagName(measuringPoint.getTagName());
        if (existMeasuringPoint != null) {
            return AjaxResult.error("新增设备'" + measuringPoint.getTagName() + "'失败，设备编码已存在");
        }
        measuringPoint.setCreateBy(SecurityUtils.getUsername());
        measuringPoint.setCreateTime(new Date());
        return toAjax(measuringPointService.insertMeasuringPoint(measuringPoint));
    }

    /**
     * 修改设备
     */
    @PreAuthorize("@ss.hasPermi('energy:measuringPoint:edit')")
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody MeasuringPoint measuringPoint) {
        MeasuringPoint existMeasuringPoint = measuringPointService.selectMeasuringPointByTagName(measuringPoint.getTagName());
        if (existMeasuringPoint != null && !existMeasuringPoint.getId().equals(measuringPoint.getId())) {
            return AjaxResult.error("修改设备'" + measuringPoint.getTagName() + "'失败，设备编码已存在");
        }
        measuringPoint.setUpdateBy(SecurityUtils.getUsername());
        measuringPoint.setUpdateTime(new Date());
        return toAjax(measuringPointService.updateMeasuringPoint(measuringPoint));
    }

    /**
     * 删除设备
     */
    @PreAuthorize("@ss.hasPermi('energy:measuringPoint:remove')")
    @Log(title = "设备管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(measuringPointService.batchDeleteByIds(ids));
    }
}