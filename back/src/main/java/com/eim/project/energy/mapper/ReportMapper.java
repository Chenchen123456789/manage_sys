package com.eim.project.energy.mapper;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author chenchen
 */
@Mapper
@DataSource(value = DataSourceType.SLAVE)
public interface ReportMapper {
    /**
     *
     * @param map 筛选条件
     * @return 年明细列表
     */
    List<Map<String, Object>> selectHistoryOfDosage(Map<String, Object> map);

    List<Map<String, Object>> selectMonthDosageOfWater(Map<String, Object> map);

    List<Map<String, Object>> selectMonthDosageOfElectricity(Map<String, Object> map);

    List<Map<String, Object>> selectMonthSettlement(Map<String, Object> map);

    List<Map<String, Object>> selectClassList();

    List<Map<String, Object>> selectEnergyTypeList();

    List<Map<String, Object>> selectSignalTypeList();

    List<Map<String, Object>> selectSystemList();

    List<Map<String, Object>> selectBuildingYearDosage(Map<String, Object> map);

    List<Map<String, Object>> selectBuildingMonthDosage(Map<String, Object> map);

    List<Map<String, Object>> selectBuildingDayDosage(Map<String, Object> map);

    List<Map<String, Object>> selectBuildingMainMPValue(@Param("buildingId") Integer buildingId);

    List<Map<String, Object>> selectHomePageChartSettingList();

    Map<String,Object> selectYearTotal();

    List<Map<String, Object>> selectDayDosageOfWater(Map<String, Object> map);

    List<Map<String, Object>> selectQuarterDosageOfWater(Map<String, Object> map);

    List<Map<String, Object>> selectYearDosageOfWater(Map<String, Object> map);

    List<Map<String, Object>> selectDayDosageOfElectricity(Map<String, Object> map);

    List<Map<String, Object>> selectYearDosageOfElectricity(Map<String, Object> map);

    List<Map<String, Object>> selectQuarterDosageOfElectricity(Map<String, Object> map);

    List<Map<String, Object>> selectDaySettlement(Map<String, Object> map);

    List<Map<String, Object>> selectQuarterSettlement(Map<String, Object> map);

    List<Map<String, Object>> selectYearSettlement(Map<String, Object> map);

    List<Map<String, Object>> selectElectricityOfFPGValue(Map<String, Object> map);
}
