package com.eim.project.energy.controller;

import com.eim.common.utils.SecurityUtils;
import com.eim.common.utils.poi.ExcelUtil;
import com.eim.framework.aspectj.lang.annotation.Log;
import com.eim.framework.aspectj.lang.enums.BusinessType;
import com.eim.framework.web.controller.BaseController;
import com.eim.framework.web.domain.AjaxResult;
import com.eim.framework.web.page.TableDataInfo;
import com.eim.project.energy.entity.Company;
import com.eim.project.energy.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @Author: chenchen
 * @Date: 2020/1/17
 */
@RestController
@RequestMapping("/energy/company")
public class CompanyController extends BaseController {
    @Autowired
    private CompanyService companyService;

    @PreAuthorize("@ss.hasPermi('energy:company:list')")
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
        List<Company> list = companyService.selectCompanyList(company);
        ExcelUtil<Company> util = new ExcelUtil<Company>(Company.class);
        return util.exportExcel(list, "公司数据");
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
}
