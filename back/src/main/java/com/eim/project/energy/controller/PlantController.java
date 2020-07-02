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
import com.eim.project.energy.entity.Plant;
import com.eim.project.energy.service.PlantService;
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
@RequestMapping("/energy/plant")
public class PlantController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(PlantController.class);
    @Autowired
    private PlantService plantService;
    @Autowired
    private TokenService tokenService;

    //    @PreAuthorize("@ss.hasPermi('energy:plant:list')")
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
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<Plant> list = plantService.selectPlantList(plant);
        ExcelUtil<Plant> util = new ExcelUtil<Plant>(Plant.class);
        return util.exportExcel(list, "工厂数据");
    }

    @Log(title = "工厂管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('energy:plant:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<Plant> util = new ExcelUtil<Plant>(Plant.class);
        List<Plant> plantList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = importPlant(plantList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<Plant> util = new ExcelUtil<Plant>(Plant.class);
        return util.importTemplateExcel("工厂数据");
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
        Plant existPlant = plantService.selectPlantByPlantCode(plant.getPlantCode(), plant.getCompanyId());
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
        Plant existPlant = plantService.selectPlantByPlantCode(plant.getPlantCode(), plant.getCompanyId());
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

    /**
     * 导入数据
     *
     * @param plantList       数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作人
     * @return 结果
     */
    public String importPlant(List<Plant> plantList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(plantList) || plantList.size() == 0) {
            throw new CustomException("导入工厂数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Plant plant : plantList) {
            try {
                Plant checkPlantCode = plantService.selectPlantByPlantCode(plant.getPlantCode(), plant.getCompanyId());
                // 验证是否存在这个工厂
                Plant existPlant = plantService.selectPlantById(plant.getId());
                if (StringUtils.isNull(existPlant)) {
                    plant.setCreateBy(operName);
                    plant.setCreateTime(new Date());
                    if (checkPlantCode != null) {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、工厂编码" + plant.getPlantCode() + " 已存在");
                    } else {
                        plantService.insertPlant(plant);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、工厂 " + plant.getPlantName() + " 导入成功");
                    }
                } else {
                    if (isUpdateSupport) {
                        plant.setUpdateBy(operName);
                        plant.setUpdateTime(new Date());
                        if (checkPlantCode != null && !checkPlantCode.getId().equals(plant.getId())) {
                            failureNum++;
                            failureMsg.append("<br/>" + failureNum + "、工厂编号 " + plant.getPlantCode() + " 已存在");
                        } else {
                            plantService.updatePlant(plant);
                            successNum++;
                            successMsg.append("<br/>" + successNum + "、工厂 " + plant.getPlantName() + " 更新成功");
                        }
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、工厂 " + plant.getPlantName() + " 已存在");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、工厂 " + plant.getPlantName() + " 导入失败：";
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
