package com.shop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.shop.entity.Cmt;

public interface CmtRepository extends JpaRepository<Cmt, Long>,
        QuerydslPredicateExecutor<Cmt>, CmtRepositoryCustom {

    List<Cmt> findByCmtTitle(String cmtTitle);

    List<Cmt> findByCmtTitleOrCmtDetail(String cmtTitle, String cmtDetail);

    @Query("select i from Cmt i where i.cmtDetail like " +
            "%:cmtDetail% order by i.cmtTitle desc")
    List<Cmt> findByCmtDetail(@Param("cmtDetail") String cmtDetail);

    @Query(value="select * from cmt i where i.cmt_detail like " +
            "%:cmtDetail% order by i.cmtTitle desc", nativeQuery = true)
    List<Cmt> findByCmtDetailByNative(@Param("cmtDetail") String cmtDetail);

    //마이페이지에서 자신이 쓴 문의 내역 리스트를 뽑아오기 위함
    @Query("select o from Cmt o " +
            "where o.member.email = :email " +
            "order by o.cmtDate desc"
    )
    List<Cmt> findCmts(@Param("email") String email, Pageable pageable);

    @Query("select count(o) from Cmt o " +
            "where o.member.email = :email"
    )
    Long countCmt(@Param("email") String email);


}