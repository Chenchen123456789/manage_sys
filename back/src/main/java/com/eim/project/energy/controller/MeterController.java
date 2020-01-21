package com.eim.project.energy.controller;

import com.eim.common.utils.SecurityUtils;
import com.eim.common.utils.poi.ExcelUtil;
import com.eim.framework.aspectj.lang.annotation.Log;
import com.eim.framework.aspectj.lang.enums.BusinessType;
import com.eim.framework.web.controller.BaseController;
import com.eim.framework.web.domain.AjaxResult;
import com.eim.framework.web.page.TableDataInfo;
import com.eim.project.energy.entity.Meter;
import com.eim.project.energy.service.MeterService;
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
@RequestMapping("/energy/meter")
public class MeterController extends BaseController {
    @Autowired
    private MeterService meterService;

    @PreAuthorize("@ss.hasPermi('energy:meter:list')")
    @GetMapping("/list")
    public TableDataInfo selectMeterList(Meter meter) {
        startPage();
        List<Meter> meterList = meterService.selectMeterList(meter);
        return getDataTable(meterList);
    }

    @Log(title = "设备管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:meter:export')")
    @GetMapping("/export")
    public AjaxResult export(Meter meter) {
        List<Meter> list = meterService.selectMeterList(meter);
        ExcelUtil<Meter> util = new ExcelUtil<Meter>(Meter.class);
        return util.exportExcel(list, "设备数据");
    }

    /**
     * 根据设备id获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('energy:meter:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Integer id) {
        return AjaxResult.success(meterService.selectMeterById(id));
    }

    /**
     * 新增设备
     */
    @PreAuthorize("@ss.hasPermi('energy:meter:add')")
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Meter meter) {
        Meter existMeter = meterService.selectMeterByMeterCode(meter.getMeterCode());
        if (existMeter != null) {
            return AjaxResult.error("新增设备'" + meter.getMeterCode() + "'失败，设备编码已存在");
        }
        meter.setCreateBy(SecurityUtils.getUsername());
        meter.setCreateTime(new Date());
        return toAjax(meterService.insertMeter(meter));
    }

    /**
     * 修改设备
     */
    @PreAuthorize("@ss.hasPermi('energy:meter:edit')")
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Meter meter) {
        Meter existMeter = meterService.selectMeterByMeterCode(meter.getMeterCode());
        if (existMeter != null && !existMeter.getId().equals(meter.getId())) {
            return AjaxResult.error("修改设备'" + meter.getMeterCode() + "'失败，设备编码已存在");
        }
        meter.setUpdateBy(SecurityUtils.getUsername());
        meter.setUpdateTime(new Date());
        return toAjax(meterService.updateMeter(meter));
    }

    /**
     * 删除设备
     */
    @PreAuthorize("@ss.hasPermi('energy:meter:remove')")
    @Log(title = "设备管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(meterService.batchDeleteByIds(ids));
    }
}