package com.eim.project.energy.controller;

import com.eim.common.utils.SecurityUtils;
import com.eim.common.utils.poi.ExcelUtil;
import com.eim.framework.aspectj.lang.annotation.Log;
import com.eim.framework.aspectj.lang.enums.BusinessType;
import com.eim.framework.web.controller.BaseController;
import com.eim.framework.web.domain.AjaxResult;
import com.eim.framework.web.page.TableDataInfo;
import com.eim.project.energy.entity.Device;
import com.eim.project.energy.service.DeviceService;
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
@RequestMapping("/energy/device")
public class DeviceController extends BaseController {
    @Autowired
    private DeviceService deviceService;

    @PreAuthorize("@ss.hasPermi('energy:device:list')")
    @GetMapping("/list")
    public TableDataInfo selectDeviceList(Device device) {
        startPage();
        List<Device> deviceList = deviceService.selectDeviceList(device);
        return getDataTable(deviceList);
    }

    @Log(title = "设备管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:device:export')")
    @GetMapping("/export")
    public AjaxResult export(Device device) {
        List<Device> list = deviceService.selectDeviceList(device);
        ExcelUtil<Device> util = new ExcelUtil<Device>(Device.class);
        return util.exportExcel(list, "设备数据");
    }

    /**
     * 根据设备id获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('energy:device:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Integer id) {
        return AjaxResult.success(deviceService.selectDeviceById(id));
    }

    /**
     * 新增设备
     */
    @PreAuthorize("@ss.hasPermi('energy:device:add')")
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Device device) {
        Device existDevice = deviceService.selectDeviceByDeviceCode(device.getDeviceCode());
        if (existDevice != null) {
            return AjaxResult.error("新增设备'" + device.getDeviceCode() + "'失败，设备编码已存在");
        }
        device.setCreateBy(SecurityUtils.getUsername());
        device.setCreateTime(new Date());
        return toAjax(deviceService.insertDevice(device));
    }

    /**
     * 修改设备
     */
    @PreAuthorize("@ss.hasPermi('energy:device:edit')")
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Device device) {
        Device existDevice = deviceService.selectDeviceByDeviceCode(device.getDeviceCode());
        if (existDevice != null && !existDevice.getId().equals(device.getId())) {
            return AjaxResult.error("修改设备'" + device.getDeviceCode() + "'失败，设备编码已存在");
        }
        device.setUpdateBy(SecurityUtils.getUsername());
        device.setUpdateTime(new Date());
        return toAjax(deviceService.updateDevice(device));
    }

    /**
     * 删除设备
     */
    @PreAuthorize("@ss.hasPermi('energy:device:remove')")
    @Log(title = "设备管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(deviceService.batchDeleteByIds(ids));
    }
}