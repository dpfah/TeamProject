package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.shop.entity.Faq;


public interface FaqRepository extends JpaRepository<Faq, Long>,
        QuerydslPredicateExecutor<Faq>, FaqRepositoryCustom {

    List<Faq> findByFaqTitle(String faqTitle);

    List<Faq> findByFaqTitleOrFaqReply(String faqTitle, String faqReply);

    @Query("select f from Faq f where f.faqReply like " +
            "%:faqReply% order by f.faqTitle")
    List<Faq> findByFaqReply(@Param("faqReply") String faqReply);

    @Query(value="select * from faq f where f.faqReply like " +
            "%:faqReply% order by f.faqTitle", nativeQuery = true)
    List<Faq> findByFaqReplyByNative(@Param("faqReply") String faqReply);

}