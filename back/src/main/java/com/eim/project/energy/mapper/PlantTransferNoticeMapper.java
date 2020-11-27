package com.eim.project.energy.mapper;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.PlantTransferNotice;
import com.eim.project.energy.entity.TransferNotice;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author cc
 */
@Mapper
@DataSource(value = DataSourceType.SLAVE)
public interface PlantTransferNoticeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PlantTransferNotice record);

    int insertSelective(PlantTransferNotice record);

    PlantTransferNotice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PlantTransferNotice record);

    int updateByPrimaryKey(PlantTransferNotice record);

    List<PlantTransferNotice> selectPlantTransferNoticeList(Map<String, Object> map);

    int deleteByBuildingIdAndDataTime(Integer buildingId, Date dataTime);

    int batchInsertSelective(PlantTransferNotice[] plantTransferNotices);

    int deleteByPlantIdAndDataTime(Integer plantId, Date dataTime);
}