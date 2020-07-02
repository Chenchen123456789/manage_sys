package com.eim.project.energy.service;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.Company;
import com.eim.project.energy.mapper.CompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author chenchen
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    public List<Company> selectCompanyList(Company company) {
        return companyMapper.selectCompanyList(company);
    }

    public Company selectCompanyById(Integer id) {
        return companyMapper.selectByPrimaryKey(id);
    }

    public int batchDeleteByIds(Integer[] ids) {
        return companyMapper.deleteByIds(ids);
    }

    public int updateCompany(Company company) {
        return companyMapper.updateByPrimaryKeySelective(company);
    }

    public int insertCompany(Company company) {
        return companyMapper.insertSelective(company);
    }

    public Company selectCompanyByCompanyCode(String companyCode) {
        return companyMapper.selectByCompanyCode(companyCode);
    }
}
