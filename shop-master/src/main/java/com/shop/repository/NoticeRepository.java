package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.shop.entity.Notice;


public interface NoticeRepository extends JpaRepository<Notice, Long>,
        QuerydslPredicateExecutor<Notice>, NoticeRepositoryCustom {

    List<Notice> findByNoticeTitle(String noticeTitle);

    List<Notice> findByNoticeTitleOrNoticeText(String noticeTitle, String noticeText);

    @Query("select n from Notice n where n.noticeText like " +
            "%:noticeText% order by n.noticeTitle")
    List<Notice> findByNoticeText(@Param("noticeText") String noticeText);

    @Query(value="select * from notice n where n.text like " +
            "%:noticeText% order by n.noticeTitle", nativeQuery = true)
    List<Notice> findByNoticeTextByNative(@Param("noticeText") String noticeText);

}