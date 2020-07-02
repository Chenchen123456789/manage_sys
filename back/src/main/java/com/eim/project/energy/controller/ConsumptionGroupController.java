package com.eim.project.energy.controller;

import com.eim.common.exception.CustomException;
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
import com.eim.project.energy.entity.Company;
import com.eim.project.energy.entity.ConsumptionGroup;
import com.eim.project.energy.service.ConsumptionGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: chenchen
 * @Date: 2020/1/17
 */
@RestController
@RequestMapping("/energy/consumptionGroup")
public class ConsumptionGroupController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(ConsumptionGroupController.class);
    @Autowired
    private ConsumptionGroupService consumptionGroupService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/list")
    public TableDataInfo selectConsumptionGroupList(ConsumptionGroup consumptionGroup) {
        startPage();
        List<ConsumptionGroup> consumptionGroupList = consumptionGroupService.selectConsumptionGroupList(consumptionGroup);
        return getDataTable(consumptionGroupList);
    }

    @Log(title = "能耗分类管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:consumptionGroup:export')")
    @GetMapping("/export")
    public AjaxResult export(ConsumptionGroup consumptionGroup) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<ConsumptionGroup> list = consumptionGroupService.selectConsumptionGroupList(consumptionGroup);
        ExcelUtil<ConsumptionGroup> util = new ExcelUtil<ConsumptionGroup>(ConsumptionGroup.class);
        return util.exportExcel(list, "能耗分类数据");
    }

    @Log(title = "能耗分类管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('energy:consumptionGroup:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ConsumptionGroup> util = new ExcelUtil<ConsumptionGroup>(ConsumptionGroup.class);
        List<ConsumptionGroup> consumptionGroupList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = importConsumptionGroup(consumptionGroupList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<ConsumptionGroup> util = new ExcelUtil<ConsumptionGroup>(ConsumptionGroup.class);
        return util.importTemplateExcel("能耗分类数据");
    }

    /**
     * 根据能耗分类编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('energy:consumptionGroup:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Integer id) {
        return AjaxResult.success(consumptionGroupService.selectConsumptionGroupById(id));
    }

    /**
     * 新增能耗分类
     */
    @PreAuthorize("@ss.hasPermi('energy:consumptionGroup:add')")
    @Log(title = "能耗分类管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ConsumptionGroup consumptionGroup) {
        ConsumptionGroup existConsumptionGroup = consumptionGroupService.selectByTagName(consumptionGroup.getTagName());
        if (existConsumptionGroup != null) {
            return AjaxResult.error("新增能耗分类'" + consumptionGroup.getTagName() + "'失败，测点名已存在");
        }
        return toAjax(consumptionGroupService.insertConsumptionGroup(consumptionGroup));
    }

    /**
     * 修改能耗分类
     */
    @PreAuthorize("@ss.hasPermi('energy:consumptionGroup:edit')")
    @Log(title = "能耗分类管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ConsumptionGroup consumptionGroup) {
        ConsumptionGroup existConsumptionGroup = consumptionGroupService.selectByTagName(consumptionGroup.getTagName());
        if (existConsumptionGroup != null && !existConsumptionGroup.getId().equals(consumptionGroup.getId())) {
            return AjaxResult.error("修改公司'" + consumptionGroup.getTagName() + "'失败，测点名已存在");
        }
        return toAjax(consumptionGroupService.updateConsumptionGroup(consumptionGroup));
    }

    /**
     * 删除能耗分类
     */
    @PreAuthorize("@ss.hasPermi('energy:consumptionGroup:remove')")
    @Log(title = "能耗分类管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(consumptionGroupService.batchDeleteByIds(ids));
    }

    /**
     * 导入用户数据
     *
     * @param consumptionGroupList 能耗分类数据列表
     * @param isUpdateSupport      是否更新支持，如果已存在，则进行更新数据
     * @param operName             操作人
     * @return 结果
     */
    public String importConsumptionGroup(List<ConsumptionGroup> consumptionGroupList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(consumptionGroupList) || consumptionGroupList.size() == 0) {
            throw new CustomException("导入能耗分类数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (ConsumptionGroup consumptionGroup : consumptionGroupList) {
            try {
                // 验证是否存在这个能耗分类
                ConsumptionGroup existConsumptionGroup = consumptionGroupService.selectConsumptionGroupById(consumptionGroup.getId());
                ConsumptionGroup checkTagName = consumptionGroupService.selectByTagName(consumptionGroup.getTagName());
                if (StringUtils.isNull(existConsumptionGroup)) {
                    if (checkTagName != null) {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、公司编号 " + consumptionGroup.getTagName() + " 已存在");
                    } else {
                        consumptionGroupService.insertConsumptionGroup(consumptionGroup);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、能耗分类 " + consumptionGroup.getTagName() + " 导入成功");
                    }
                } else {
                    if (isUpdateSupport) {
                        if (checkTagName != null && !checkTagName.getId().equals(consumptionGroup.getId())) {
                            failureNum++;
                            failureMsg.append("<br/>" + failureNum + "、公司编号 " + consumptionGroup.getTagName() + " 已存在");
                        } else {
                            consumptionGroupService.updateConsumptionGroup(consumptionGroup);
                            successNum++;
                            successMsg.append("<br/>" + successNum + "、能耗分类 " + consumptionGroup.getTagName() + " 更新成功");
                        }
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、能耗分类 " + consumptionGroup.getTagName() + " 已存在");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、能耗分类 " + consumptionGroup.getTagName() + " 导入失败：";
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
