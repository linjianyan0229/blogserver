package com.example.blogserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogserver.entity.VisitLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

/**
 * 访问日志 Mapper
 */
@Mapper
public interface VisitLogMapper extends BaseMapper<VisitLog> {

    /** 总访问量 PV */
    @Select("SELECT COUNT(*) FROM visit_log")
    long countPv();

    /** 独立访客 UV（按 IP 去重） */
    @Select("SELECT COUNT(DISTINCT ip) FROM visit_log")
    long countUv();

    /** 某日 PV */
    @Select("SELECT COUNT(*) FROM visit_log WHERE visit_date = #{date}")
    long countPvByDate(LocalDate date);

    /** 某日 UV */
    @Select("SELECT COUNT(DISTINCT ip) FROM visit_log WHERE visit_date = #{date}")
    long countUvByDate(LocalDate date);
}
