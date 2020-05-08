package com.eim.project.energy.controller;

import com.eim.common.exception.CustomException;
import com.eim.common.utils.SecurityUtils;
import com.eim.common.utils.ServletUtils;
import com.eim.common.utils.StringUtils;
import com.eim.common.utils.poi.ExcelUtil;
import com.eim.framework.aspectj.lang.annotation.Log;
import com.eim.framework.aspectj.lang.enums.BusinessType;
import com.eim.framework.security.LoginUser;
import com.eim.framework.security.service.TokenService;
import com.eim.framework.web.controller.BaseController;
import com.eim.framework.web.domain.AjaxResult;
import com.eim.framework.web.page.PageDomain;
import com.eim.framework.web.page.TableDataInfo;
import com.eim.framework.web.page.TableSupport;
import com.eim.project.energy.entity.Building;
import com.eim.project.energy.entity.Device;
import com.eim.project.energy.entity.Plant;
import com.eim.project.energy.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @Author: chenchen
 * @Date: 2020/1/20
 */
@RestController
@RequestMapping("/energy/device")
public class DeviceController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private TokenService tokenService;

//    @PreAuthorize("@ss.hasPermi('energy:device:list')")
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
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<Device> list = deviceService.selectDeviceList(device);
        ExcelUtil<Device> util = new ExcelUtil<Device>(Device.class);
        return util.exportExcel(list, "设备数据");
    }

    @Log(title = "设备管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('energy:device:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<Device> util = new ExcelUtil<Device>(Device.class);
        List<Device> deviceList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = importDevice(deviceList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<Device> util = new ExcelUtil<Device>(Device.class);
        return util.importTemplateExcel("设备数据");
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
        Device existDevice = deviceService.selectDeviceByDeviceCode(device.getDeviceCode(), device.getBuildingId());
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
        Device existDevice = deviceService.selectDeviceByDeviceCode(device.getDeviceCode(), device.getBuildingId());
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

    /**
     * 导入数据
     *
     * @param deviceList       数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作人
     * @return 结果
     */
    public String importDevice(List<Device> deviceList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(deviceList) || deviceList.size() == 0) {
            throw new CustomException("导入设备数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Device device : deviceList) {
            try {
                Device checkDeviceCode = deviceService.selectDeviceByDeviceCode(device.getDeviceCode(), device.getBuildingId());
                // 验证是否存在这个设备
                Device existDevice = deviceService.selectDeviceById(device.getId());
                if (StringUtils.isNull(existDevice)) {
                    device.setCreateBy(operName);
                    device.setCreateTime(new Date());
                    if (checkDeviceCode != null) {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、设备编码" + device.getDeviceCode() + " 已存在");
                    } else {
                        deviceService.insertDevice(device);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、设备 " + device.getDeviceName() + " 导入成功");
                    }
                } else if (isUpdateSupport) {
                    device.setUpdateBy(operName);
                    device.setUpdateTime(new Date());
                    if (checkDeviceCode != null && !checkDeviceCode.getId().equals(device.getId())) {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、设备编号 " + device.getDeviceCode() + " 已存在");
                    } else {
                        deviceService.updateDevice(device);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、设备 " + device.getDeviceName() + " 更新成功");
                    }
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、设备 " + device.getDeviceName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、设备 " + device.getDeviceName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}