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
import com.eim.project.energy.entity.Company;
import com.eim.project.energy.service.CompanyService;
import com.eim.project.system.domain.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: chenchen
 * @Date: 2020/1/17
 */
@RestController
@RequestMapping("/energy/company")
public class CompanyController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(CompanyController.class);
    @Autowired
    private CompanyService companyService;

    @Autowired
    private TokenService tokenService;

    //    @PreAuthorize("@ss.hasPermi('energy:company:list')")
    @GetMapping("/list")
    public TableDataInfo selectCompanyList(Company company) {
        startPage();
        List<Company> companyList = companyService.selectCompanyList(company);
        return getDataTable(companyList);
    }

    @Log(title = "公司管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:company:export')")
    @GetMapping("/export")
    public AjaxResult export(Company company) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        if (pageNum != null) {
            startPage();
        }
        List<Company> list = companyService.selectCompanyList(company);
        ExcelUtil<Company> util = new ExcelUtil<Company>(Company.class);
        return util.exportExcel(list, "公司数据");
    }

    @Log(title = "公司管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('energy:company:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<Company> util = new ExcelUtil<Company>(Company.class);
        List<Company> companyList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = importCompany(companyList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<Company> util = new ExcelUtil<Company>(Company.class);
        return util.importTemplateExcel("公司数据");
    }

    /**
     * 根据公司编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('energy:company:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Integer id) {
        return AjaxResult.success(companyService.selectCompanyById(id));
    }

    /**
     * 新增公司
     */
    @PreAuthorize("@ss.hasPermi('energy:company:add')")
    @Log(title = "公司管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Company company) {
        Company existCompany = companyService.selectCompanyByCompanyCode(company.getCompanyCode());
        if (existCompany != null) {
            return AjaxResult.error("新增公司'" + company.getCompanyCode() + "'失败，公司编码已存在");
        }
        company.setCreateBy(SecurityUtils.getUsername());
        company.setCreateTime(new Date());
        return toAjax(companyService.insertCompany(company));
    }

    /**
     * 修改公司
     */
    @PreAuthorize("@ss.hasPermi('energy:company:edit')")
    @Log(title = "公司管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Company company) {
        Company existCompany = companyService.selectCompanyByCompanyCode(company.getCompanyCode());
        if (existCompany != null && !existCompany.getId().equals(company.getId())) {
            return AjaxResult.error("修改公司'" + company.getCompanyCode() + "'失败，公司编码已存在");
        }
        company.setUpdateBy(SecurityUtils.getUsername());
        company.setUpdateTime(new Date());
        return toAjax(companyService.updateCompany(company));
    }

    /**
     * 删除公司
     */
    @PreAuthorize("@ss.hasPermi('energy:company:remove')")
    @Log(title = "公司管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(companyService.batchDeleteByIds(ids));
    }

    /**
     * 导入用户数据
     *
     * @param companyList     公司数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作人
     * @return 结果
     */
    public String importCompany(List<Company> companyList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(companyList) || companyList.size() == 0) {
            throw new CustomException("导入公司数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Company company : companyList) {
            try {
                // 验证是否存在这个公司
                Company existCompany = companyService.selectCompanyById(company.getId());
                Company checkCompanyCode = companyService.selectCompanyByCompanyCode(company.getCompanyCode());
                if (StringUtils.isNull(existCompany)) {
                    company.setCreateBy(operName);
                    company.setCreateTime(new Date());
                    if (checkCompanyCode != null) {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、公司编号 " + company.getCompanyCode() + " 已存在");
                    } else {
                        companyService.insertCompany(company);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、公司 " + company.getCompanyName() + " 导入成功");
                    }
                } else {
                    if (isUpdateSupport) {
                        company.setUpdateBy(operName);
                        company.setUpdateTime(new Date());
                        if (checkCompanyCode != null && !checkCompanyCode.getId().equals(company.getId())) {
                            failureNum++;
                            failureMsg.append("<br/>" + failureNum + "、公司编号 " + company.getCompanyCode() + " 已存在");
                        } else {
                            companyService.updateCompany(company);
                            successNum++;
                            successMsg.append("<br/>" + successNum + "、公司 " + company.getCompanyName() + " 更新成功");
                        }
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、公司 " + company.getCompanyName() + " 已存在");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、公司 " + company.getCompanyName() + " 导入失败：";
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
