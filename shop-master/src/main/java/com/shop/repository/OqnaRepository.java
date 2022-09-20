package com.shop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.shop.entity.Oqna;

public interface OqnaRepository extends JpaRepository<Oqna, Long>,
        QuerydslPredicateExecutor<Oqna>, OqnaRepositoryCustom {

    List<Oqna> findByOqnaTitle(String oqnaTitle);

    List<Oqna> findByOqnaTitleOrOqnaDetail(String oqnaTitle, String oqnaDetail);


    @Query("select o from Oqna o where o.oqnaDetail like " +
            "%:oqnaDetail% order by o.oqnaTitle desc")
    List<Oqna> findByOqnaDetail(@Param("oqnaDetail") String oqnaDetail);

    @Query(value="select * from oqna o where o.oqna_detail like " +
            "%:oqnaDetail% order by o.price desc", nativeQuery = true)
    List<Oqna> findByOqnaDetailByNative(@Param("oqnaDetail") String oqnaDetail);
    
    //마이페이지에서 자신이 쓴 문의 내역 리스트를 뽑아오기 위함
    @Query("select o from Oqna o " +
            "where o.member.email = :email " +
            "order by o.oqnaDate desc"
    )
    List<Oqna> findOqnas(@Param("email") String email, Pageable pageable);

    @Query("select count(o) from Oqna o " +
            "where o.member.email = :email"
    )
    Long countOqna(@Param("email") String email);
    
    

}