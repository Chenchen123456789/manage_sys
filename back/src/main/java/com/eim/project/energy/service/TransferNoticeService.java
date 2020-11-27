package com.eim.project.energy.service;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.PlantTransferNotice;
import com.eim.project.energy.entity.TransferNotice;
import com.eim.project.energy.mapper.PlantTransferNoticeMapper;
import com.eim.project.energy.mapper.TransferNoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: chenchen
 * @Date: 2020/7/17
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class TransferNoticeService {
    @Autowired
    private TransferNoticeMapper transferNoticeMapper;

    @Autowired
    private PlantTransferNoticeMapper plantTransferNoticeMapper;

    public List<TransferNotice> selectTransferNoticeList(Map<String, Object> map) {
        return transferNoticeMapper.selectTransferNoticeList(map);
    }

    public TransferNotice selectTransferNoticeById(Long id) {
        return transferNoticeMapper.selectByPrimaryKey(id);
    }

    public int updateTransferNotice(TransferNotice transferNotice) {
        return transferNoticeMapper.updateByPrimaryKeySelective(transferNotice);
    }

    public int insertTransferNotice(TransferNotice transferNotice) {
        return transferNoticeMapper.insertSelective(transferNotice);
    }

    public int insertTransferNotice(TransferNotice[] transferNoticeArray) {
        return transferNoticeMapper.batchInsertSelective(transferNoticeArray);
    }

    public int deleteByBuildingIdAndDataTime(Integer buildingId, Date dataTime) {
        return transferNoticeMapper.deleteByBuildingIdAndDataTime(buildingId, dataTime);
    }

    public List<PlantTransferNotice> selectPlantTransferNoticeList(Map<String, Object> map) {
        return plantTransferNoticeMapper.selectPlantTransferNoticeList(map);
    }

    public int deleteByPlantIdAndDataTime(Integer plantId, Date dataTime) {
        return plantTransferNoticeMapper.deleteByPlantIdAndDataTime(plantId, dataTime);
    }

    public int insertPlantTransferNotice(PlantTransferNotice[] plantTransferNotices) {
        return plantTransferNoticeMapper.batchInsertSelective(plantTransferNotices);
    }
}
