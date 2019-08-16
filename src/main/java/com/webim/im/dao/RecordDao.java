package com.webim.im.dao;

import com.webim.im.dao.custom.RecordDaoCustom;
import com.webim.im.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecordDao extends JpaRepository<Record,Integer>, RecordDaoCustom {

}
