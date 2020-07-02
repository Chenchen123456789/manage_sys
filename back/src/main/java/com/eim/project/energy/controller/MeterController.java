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
import com.eim.project.energy.entity.Meter;
import com.eim.project.energy.entity.Plant;
import com.eim.project.energy.service.MeterService;
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
 * @Date: 2020/1/21
 */
@RestController
@RequestMapping("/energy/meter")
public class MeterController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(MeterController.class);

    @Autowired
    private MeterService meterService;

    @Autowired
    private TokenService tokenService;

    //    @PreAuthorize("@ss.hasPermi('energy:meter:list')")
    @GetMapping("/list")
    public TableDataInfo selectMeterList(Meter meter) {
        startPage();
        List<Meter> meterList = meterService.selectMeterList(meter);
        return getDataTable(meterList);
    }

    @Log(title = "仪表管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:meter:export')")
    @GetMapping("/export")
    public AjaxResult export(Meter meter) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<Meter> list = meterService.selectMeterList(meter);
        ExcelUtil<Meter> util = new ExcelUtil<Meter>(Meter.class);
        return util.exportExcel(list, "仪表数据");
    }

    @Log(title = "仪表管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('energy:meter:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<Meter> util = new ExcelUtil<Meter>(Meter.class);
        List<Meter> meterList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = importMeter(meterList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<Meter> util = new ExcelUtil<Meter>(Meter.class);
        return util.importTemplateExcel("仪表数据");
    }

    /**
     * 根据仪表id获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('energy:meter:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Integer id) {
        return AjaxResult.success(meterService.selectMeterById(id));
    }

    /**
     * 新增仪表
     */
    @PreAuthorize("@ss.hasPermi('energy:meter:add')")
    @Log(title = "仪表管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Meter meter) {
        Meter existMeter = meterService.selectMeterByMeterCode(meter.getMeterCode(), meter.getDeviceId());
        if (existMeter != null) {
            return AjaxResult.error("新增仪表'" + meter.getMeterCode() + "'失败，仪表编码已存在");
        }
        meter.setCreateBy(SecurityUtils.getUsername());
        meter.setCreateTime(new Date());
        return toAjax(meterService.insertMeter(meter));
    }

    /**
     * 修改仪表
     */
    @PreAuthorize("@ss.hasPermi('energy:meter:edit')")
    @Log(title = "仪表管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Meter meter) {
        Meter existMeter = meterService.selectMeterByMeterCode(meter.getMeterCode(), meter.getDeviceId());
        if (existMeter != null && !existMeter.getId().equals(meter.getId())) {
            return AjaxResult.error("修改仪表'" + meter.getMeterCode() + "'失败，仪表编码已存在");
        }
        meter.setUpdateBy(SecurityUtils.getUsername());
        meter.setUpdateTime(new Date());
        return toAjax(meterService.updateMeter(meter));
    }

    /**
     * 删除仪表
     */
    @PreAuthorize("@ss.hasPermi('energy:meter:remove')")
    @Log(title = "仪表管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(meterService.batchDeleteByIds(ids));
    }

    /**
     * 导入数据
     *
     * @param meterList       数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作人
     * @return 结果
     */
    public String importMeter(List<Meter> meterList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(meterList) || meterList.size() == 0) {
            throw new CustomException("导入仪表数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Meter meter : meterList) {
            try {
                Meter checkMeterCode = meterService.selectMeterByMeterCode(meter.getMeterCode(), meter.getDeviceId());
                // 验证是否存在这个仪表
                Meter existMeter = meterService.selectMeterById(meter.getId());
                if (StringUtils.isNull(existMeter)) {
                    meter.setCreateBy(operName);
                    meter.setCreateTime(new Date());
                    if (checkMeterCode != null) {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、仪表编码" + meter.getMeterCode() + " 已存在");
                    } else {
                        meterService.insertMeter(meter);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、仪表 " + meter.getMeterName() + " 导入成功");
                    }
                } else {
                    if (isUpdateSupport) {
                        meter.setUpdateBy(operName);
                        meter.setUpdateTime(new Date());
                        if (checkMeterCode != null && !checkMeterCode.getId().equals(meter.getId())) {
                            failureNum++;
                            failureMsg.append("<br/>" + failureNum + "、仪表编码 " + meter.getMeterCode() + " 已存在");
                        } else {
                            meterService.updateMeter(meter);
                            successNum++;
                            successMsg.append("<br/>" + successNum + "、仪表 " + meter.getMeterName() + " 更新成功");
                        }
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、仪表 " + meter.getMeterName() + " 已存在");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、仪表 " + meter.getMeterName() + " 导入失败：";
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