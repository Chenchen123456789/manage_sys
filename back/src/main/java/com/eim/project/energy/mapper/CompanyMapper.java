package com.eim.project.energy.mapper;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.Company;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author chenchen
 */
@Mapper
@DataSource(value = DataSourceType.SLAVE)
public interface CompanyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Company record);

    int insertSelective(Company record);

    Company selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Company record);

    int updateByPrimaryKey(Company record);

    List<Company> selectCompanyList(Company company);

    int deleteByIds(Integer[] ids);

    Company selectByCompanyCode(String companyCode);
}