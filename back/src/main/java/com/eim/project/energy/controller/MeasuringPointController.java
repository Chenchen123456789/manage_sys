package com.eim.project.energy.controller;

import com.alibaba.fastjson.JSONObject;
import com.eim.common.exception.CustomException;
import com.eim.common.utils.SecurityUtils;
import com.eim.common.utils.ServletUtils;
import com.eim.common.utils.StringUtils;
import com.eim.common.utils.poi.ExcelUtil;
import com.eim.framework.aspectj.lang.annotation.Log;
import com.eim.framework.aspectj.lang.enums.BusinessType;
import com.eim.framework.registercode.LicenseConst;
import com.eim.framework.registercode.utils.Base64Utils;
import com.eim.framework.registercode.utils.ComputerSerialNumberUtil;
import com.eim.framework.registercode.utils.FileUtil;
import com.eim.framework.registercode.utils.RSAUtils;
import com.eim.framework.security.LoginUser;
import com.eim.framework.security.service.TokenService;
import com.eim.framework.web.controller.BaseController;
import com.eim.framework.web.domain.AjaxResult;
import com.eim.framework.web.page.PageDomain;
import com.eim.framework.web.page.TableDataInfo;
import com.eim.framework.web.page.TableSupport;
import com.eim.project.energy.entity.MeasuringPoint;
import com.eim.project.energy.entity.MeasuringPoint;
import com.eim.project.energy.entity.Plant;
import com.eim.project.energy.service.MeasuringPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: chenchen
 * @Date: 2020/1/21
 */
@RestController
@RequestMapping("/energy/measuringPoint")
public class MeasuringPointController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(MeasuringPointController.class);

    @Autowired
    private MeasuringPointService measuringPointService;
    @Autowired
    private TokenService tokenService;

    //    @PreAuthorize("@ss.hasPermi('energy:measuringPoint:list')")
    @GetMapping("/list")
    public TableDataInfo selectMeasuringPointList(MeasuringPoint measuringPoint) {
        startPage();
        List<MeasuringPoint> measuringPointList = measuringPointService.selectMeasuringPointList(measuringPoint);
        return getDataTable(measuringPointList);
    }

    @Log(title = "测点管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:measuringPoint:export')")
    @GetMapping("/export")
    public AjaxResult export(MeasuringPoint measuringPoint) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<MeasuringPoint> list = measuringPointService.selectMeasuringPointList(measuringPoint);
        ExcelUtil<MeasuringPoint> util = new ExcelUtil<MeasuringPoint>(MeasuringPoint.class);
        return util.exportExcel(list, "测点数据");
    }

    @Log(title = "测点管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('energy:measuringPoint:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<MeasuringPoint> util = new ExcelUtil<MeasuringPoint>(MeasuringPoint.class);
        List<MeasuringPoint> measuringPointList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = importMeasuringPoint(measuringPointList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<MeasuringPoint> util = new ExcelUtil<MeasuringPoint>(MeasuringPoint.class);
        return util.importTemplateExcel("测点数据");
    }

    /**
     * 根据测点id获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('energy:measuringPoint:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Integer id) {
        return AjaxResult.success(measuringPointService.selectMeasuringPointById(id));
    }

    /**
     * 新增测点
     */
    @PreAuthorize("@ss.hasPermi('energy:measuringPoint:add')")
    @Log(title = "测点管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MeasuringPoint measuringPoint) throws Exception {
        int currentCount = measuringPointService.selectMeasuringPointCount();
        if (currentCount >= LicenseConst.sqlMaxCount) {
            return AjaxResult.error("测点数目已达上限");
        }

        MeasuringPoint existMeasuringPoint = measuringPointService.selectMeasuringPointByTagName(measuringPoint.getTagName());
        if (existMeasuringPoint != null) {
            return AjaxResult.error("新增测点'" + measuringPoint.getTagName() + "'失败，测点编码已存在");
        }
        measuringPoint.setCreateBy(SecurityUtils.getUsername());
        measuringPoint.setCreateTime(new Date());
        return toAjax(measuringPointService.insertMeasuringPoint(measuringPoint));
    }

    /**
     * 修改测点
     */
    @PreAuthorize("@ss.hasPermi('energy:measuringPoint:edit')")
    @Log(title = "测点管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody MeasuringPoint measuringPoint) {
        MeasuringPoint existMeasuringPoint = measuringPointService.selectMeasuringPointByTagName(measuringPoint.getTagName());
        if (existMeasuringPoint != null && !existMeasuringPoint.getId().equals(measuringPoint.getId())) {
            return AjaxResult.error("修改测点'" + measuringPoint.getTagName() + "'失败，测点编码已存在");
        }
        measuringPoint.setUpdateBy(SecurityUtils.getUsername());
        measuringPoint.setUpdateTime(new Date());
        return toAjax(measuringPointService.updateMeasuringPoint(measuringPoint));
    }

    /**
     * 删除测点
     */
    @PreAuthorize("@ss.hasPermi('energy:measuringPoint:remove')")
    @Log(title = "测点管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(measuringPointService.batchDeleteByIds(ids));
    }

    /**
     * 导入数据
     *
     * @param measuringPointList 数据列表
     * @param isUpdateSupport    是否更新支持，如果已存在，则进行更新数据
     * @param operName           操作人
     * @return 结果
     */
    public String importMeasuringPoint(List<MeasuringPoint> measuringPointList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(measuringPointList) || measuringPointList.size() == 0) {
            throw new CustomException("导入测点数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (MeasuringPoint measuringPoint : measuringPointList) {
            try {
                MeasuringPoint checkMeasuringPointCode = measuringPointService.selectMeasuringPointByTagName(measuringPoint.getTagName());
                // 验证是否存在这个测点
                MeasuringPoint existMeasuringPoint = measuringPointService.selectMeasuringPointById(measuringPoint.getId());
                if (StringUtils.isNull(existMeasuringPoint)) {
                    measuringPoint.setCreateBy(operName);
                    measuringPoint.setCreateTime(new Date());
                    if (checkMeasuringPointCode != null) {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、测点名" + measuringPoint.getTagName() + " 已存在");
                    } else {
                        int currentCount = measuringPointService.selectMeasuringPointCount();
                        if (currentCount >= LicenseConst.sqlMaxCount) {
                            return "测点数目已达上限";
                        }
                        measuringPointService.insertMeasuringPoint(measuringPoint);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、测点 " + measuringPoint.getTagName() + " 导入成功");
                    }
                } else if (isUpdateSupport) {
                    measuringPoint.setUpdateBy(operName);
                    measuringPoint.setUpdateTime(new Date());
                    if (checkMeasuringPointCode != null && !checkMeasuringPointCode.getId().equals(measuringPoint.getId())) {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、测点名 " + measuringPoint.getTagName() + " 已存在");
                    } else {
                        measuringPointService.updateMeasuringPoint(measuringPoint);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、测点 " + measuringPoint.getTagName() + " 更新成功");
                    }
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、测点 " + measuringPoint.getTagName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、测点 " + measuringPoint.getTagName() + " 导入失败：";
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