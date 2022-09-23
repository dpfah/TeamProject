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
import com.shop.dto.NoticeSearchDto;
import com.shop.dto.MainNoticeDto;
import com.shop.dto.QMainNoticeDto;
import com.shop.entity.Notice;
import com.shop.entity.QNotice;
import com.shop.entity.QItemImg;

public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public NoticeRepositoryCustomImpl(EntityManager em){
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

        return QNotice.notice.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("noticeTitle", searchBy)){
            return QNotice.notice.noticeTitle.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QNotice.notice.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Notice> getAdminNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable) {

        List<Notice> content = queryFactory
                .selectFrom(QNotice.notice)
                .where(regDtsAfter(noticeSearchDto.getSearchDateType()),
                        searchByLike(noticeSearchDto.getSearchBy(),
                                noticeSearchDto.getSearchQuery()))
                .orderBy(QNotice.notice.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QNotice.notice)
                .where(regDtsAfter(noticeSearchDto.getSearchDateType()),
                        searchByLike(noticeSearchDto.getSearchBy(), noticeSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression noticeTitleLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QNotice.notice.noticeTitle.like("%" + searchQuery + "%");
    }
    
    
    @Override
    public Page<MainNoticeDto> getMainNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable) {
        QNotice notice = QNotice.notice;

        List<MainNoticeDto> content = queryFactory
                .select(
                        new QMainNoticeDto(
                                notice.id,
                                notice.noticeTitle,
                                notice.noticeText,
                                notice.noticeType,
                                notice.regTime
                                )
                )
                .from(notice)
                .where(noticeTitleLike(noticeSearchDto.getSearchQuery()))
                .orderBy(notice.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(notice)
                .where(noticeTitleLike(noticeSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }
}