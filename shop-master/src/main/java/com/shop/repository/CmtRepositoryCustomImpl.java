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
import com.shop.dto.CmtSearchDto;
import com.shop.dto.MainCmtDto;
import com.shop.dto.QMainCmtDto;
import com.shop.entity.Cmt;
import com.shop.entity.QCmt;
import com.shop.entity.QCmtImg;


public class CmtRepositoryCustomImpl implements CmtRepositoryCustom{
	
	private JPAQueryFactory queryFactory;

    public CmtRepositoryCustomImpl(EntityManager em){
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

        return QCmt.cmt.regTime.after(dateTime);
    }

    
    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("cmtTitle", searchBy)){
            return QCmt.cmt.cmtTitle.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QCmt.cmt.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Cmt> getAdminCmtPage(CmtSearchDto cmtSearchDto, Pageable pageable) {

        List<Cmt> content = queryFactory
                .selectFrom(QCmt.cmt)
                .where(regDtsAfter(cmtSearchDto.getSearchDateType()),
                        searchByLike(cmtSearchDto.getSearchBy(),
                                cmtSearchDto.getSearchQuery()))
                .orderBy(QCmt.cmt.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QCmt.cmt)
                .where(regDtsAfter(cmtSearchDto.getSearchDateType()),
                        searchByLike(cmtSearchDto.getSearchBy(), cmtSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }
    
    private BooleanExpression cmtTitleLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QCmt.cmt.cmtTitle.like("%" + searchQuery + "%");
    }


    // 메인 제품
    @Override
    public Page<MainCmtDto> getMainCmtPage(CmtSearchDto cmtSearchDto, Pageable pageable) {
        QCmt cmt = QCmt.cmt;
        QCmtImg cmtImg = QCmtImg.cmtImg;

        List<MainCmtDto> content = queryFactory
                .select(
                        new QMainCmtDto(
                                cmt.id,
                                cmt.cmtTitle,
                                cmt.cmtDetail,
                                cmtImg.imgUrl,
                                cmt.createdBy,
                                cmt.view)
                )
                .from(cmtImg)
                .join(cmtImg.cmt, cmt)
                .where(cmtImg.repimgYn.eq("Y"))
                .where(cmtTitleLike(cmtSearchDto.getSearchQuery()))
                .orderBy(cmt.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(cmtImg)
                .join(cmtImg.cmt, cmt)
                .where(cmtImg.repimgYn.eq("Y"))
                .where(cmtTitleLike(cmtSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }
    

}
