package com.eim.project.energy.mapper;

import com.eim.framework.aspectj.lang.annotation.DataSource;
import com.eim.framework.aspectj.lang.enums.DataSourceType;
import com.eim.project.energy.entity.TransferNotice;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 43835
 */
@Mapper
@DataSource(value = DataSourceType.SLAVE)
public interface TransferNoticeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TransferNotice record);

    int insertSelective(TransferNotice record);

    TransferNotice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TransferNotice record);

    int updateByPrimaryKey(TransferNotice record);

    List<TransferNotice> selectTransferNoticeList(Map<String, Object> map);

    int deleteByBuildingIdAndDataTime(Integer buildingId, Date dataTime);

    int batchInsertSelective(TransferNotice[] transferNoticeArray);
}