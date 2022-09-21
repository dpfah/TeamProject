package com.shop.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.dto.FaqSearchDto;
import com.shop.entity.Faq;
import com.shop.entity.QFaq;

public class FaqRepositoryCustomImpl implements FaqRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public FaqRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }


    private BooleanExpression regDtsAfter(String searchDateType){

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QFaq.faq.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("faqTitle", searchBy)){
            return QFaq.faq.faqTitle.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QFaq.faq.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Faq> getAdminFaqPage(FaqSearchDto faqSearchDto, Pageable pageable) {

        List<Faq> content = queryFactory
                .selectFrom(QFaq.faq)
                .where(regDtsAfter(faqSearchDto.getSearchDateType()),
                        searchByLike(faqSearchDto.getSearchBy(),
                                faqSearchDto.getSearchQuery()))
                .orderBy(QFaq.faq.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QFaq.faq)
                .where(regDtsAfter(faqSearchDto.getSearchDateType()),
                        searchByLike(faqSearchDto.getSearchBy(), faqSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression faqTitleLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QFaq.faq.faqTitle.like("%" + searchQuery + "%");
    }
}