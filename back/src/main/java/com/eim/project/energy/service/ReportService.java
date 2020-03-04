package com.eim.project.energy.service;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.mapper.ReportMapper;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenchen
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class ReportService {
    @Autowired
    private ReportMapper reportMapper;

    public List<Map<String, Object>> selectHistoryOfYearDosage(Map<String, Object> map) {
        return reportMapper.selectHistoryOfYearDosage(map);
    }

    public List<Map<String, Object>> selectHistoryOfMonthDosage(Map<String, Object> map) {
        return reportMapper.selectHistoryOfMonthDosage(map);
    }

    public List<Map<String, Object>> selectHistoryOfDayDosage(Map<String, Object> map) {
        return reportMapper.selectHistoryOfDayDosage(map);
    }

    public List<Map<String, Object>> selectHistoryOfHourDosage(Map<String, Object> map) {
        return reportMapper.selectHistoryOfHourDosage(map);
    }

    public List<Map<String, Object>> selectMonthDosageOfWater(Map<String, Object> map) {
        return reportMapper.selectMonthDosageOfWater(map);
    }

    public List<Map<String, Object>> selectMonthDosageOfElectricity(Map<String, Object> map) {
        return reportMapper.selectMonthDosageOfElectricity(map);
    }

    public List<Map<String, Object>> selectMonthSettlement(Map<String, Object> map) {
        return reportMapper.selectMonthSettlement(map);
    }

    public List<Map<String, Object>> selectClassList() {
        return reportMapper.selectClassList();
    }

    public List<Map<String, Object>> selectEnergyTypeList() {
        return reportMapper.selectEnergyTypeList();
    }

    public List<Map<String, Object>> selectSignalTypeList() {
        return reportMapper.selectSignalTypeList();
    }

    public List<Map<String, Object>> selectSystemList() {
        return reportMapper.selectSystemList();
    }

    public List<Map<String, Object>> selectBuildingYearDosage(Map<String, Object> map) {
        return reportMapper.selectBuildingYearDosage(map);
    }

    public List<Map<String, Object>> selectBuildingMonthDosage(Map<String, Object> map) {
        return reportMapper.selectBuildingMonthDosage(map);
    }

    public List<Map<String, Object>> selectBuildingDayDosage(Map<String, Object> map) {
        return reportMapper.selectBuildingDayDosage(map);
    }

    public List<Map<String, Object>> selectBuildingDistribution() {
        return reportMapper.selectBuildingDistribution();
    }

    public List<Map<String, Object>> selectBuildingMainMPValue(Integer buildingId) {
        return reportMapper.selectBuildingMainMPValue(buildingId);
    }

    public List<Map<String, Object>> selectHomePageChartSettingList() {
        return reportMapper.selectHomePageChartSettingList();
    }
}
