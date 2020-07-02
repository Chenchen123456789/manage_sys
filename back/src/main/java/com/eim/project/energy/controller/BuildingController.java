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
import com.eim.project.energy.entity.Plant;
import com.eim.project.energy.service.BuildingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @Author: chenchen
 * @Date: 2020/1/20
 */
@RestController
@RequestMapping("/energy/building")
public class BuildingController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(BuildingController.class);
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private TokenService tokenService;

    //    @PreAuthorize("@ss.hasPermi('energy:building:list')")
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
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<Building> list = buildingService.selectBuildingList(building);
        ExcelUtil<Building> util = new ExcelUtil<Building>(Building.class);
        return util.exportExcel(list, "建筑数据");
    }

    @Log(title = "建筑管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('energy:building:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<Building> util = new ExcelUtil<Building>(Building.class);
        List<Building> buildingList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = importBuilding(buildingList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<Building> util = new ExcelUtil<Building>(Building.class);
        return util.importTemplateExcel("建筑数据");
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
        Building existBuilding = buildingService.selectBuildingByBuildingCode(building.getBuildingCode(), building.getPlantId());
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
        Building existBuilding = buildingService.selectBuildingByBuildingCode(building.getBuildingCode(), building.getPlantId());
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

    /**
     * 导入数据
     *
     * @param buildingList    数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作人
     * @return 结果
     */
    public String importBuilding(List<Building> buildingList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(buildingList) || buildingList.size() == 0) {
            throw new CustomException("导入建筑数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Building building : buildingList) {
            try {
                Building checkBuildingCode = buildingService.selectBuildingByBuildingCode(building.getBuildingCode(), building.getPlantId());
                // 验证是否存在这个建筑
                Building existBuilding = buildingService.selectBuildingById(building.getId());
                if (StringUtils.isNull(existBuilding)) {
                    building.setCreateBy(operName);
                    building.setCreateTime(new Date());
                    if (checkBuildingCode != null) {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、建筑编码" + building.getBuildingCode() + " 已存在");
                    } else {
                        buildingService.insertBuilding(building);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、建筑 " + building.getBuildingName() + " 导入成功");
                    }
                } else {
                    if (isUpdateSupport) {
                        building.setUpdateBy(operName);
                        building.setUpdateTime(new Date());
                        if (checkBuildingCode != null && !checkBuildingCode.getId().equals(building.getId())) {
                            failureNum++;
                            failureMsg.append("<br/>" + failureNum + "、建筑编码 " + building.getBuildingCode() + " 已存在");
                        } else {
                            buildingService.updateBuilding(building);
                            successNum++;
                            successMsg.append("<br/>" + successNum + "、建筑 " + building.getBuildingName() + " 更新成功");
                        }
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、建筑 " + building.getBuildingName() + " 已存在");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、建筑 " + building.getBuildingName() + " 导入失败：";
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
