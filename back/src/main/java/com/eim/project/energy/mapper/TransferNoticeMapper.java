package com.eim.project.energy.mapper;

import com.eim.project.energy.entity.TransferNotice;

public interface TransferNoticeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TransferNotice record);

    int insertSelective(TransferNotice record);

    TransferNotice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TransferNotice record);

    int updateByPrimaryKey(TransferNotice record);
}