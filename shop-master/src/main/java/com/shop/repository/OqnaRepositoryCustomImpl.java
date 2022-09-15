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
import com.shop.constant.QnAType;
import com.shop.dto.MainOqnaDto;
import com.shop.dto.OqnaSearchDto;
import com.shop.dto.QMainOqnaDto;
import com.shop.entity.Oqna;
import com.shop.entity.QOqna;
import com.shop.entity.QOqnaImg;


public class OqnaRepositoryCustomImpl implements OqnaRepositoryCustom{
	
	private JPAQueryFactory queryFactory;

    public OqnaRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

	
	  private BooleanExpression searchQnATypeEq(QnAType searchQnAType){ return
	  searchQnAType == null ? null :
	  QOqna.oqna.qnaType.eq(searchQnAType); }
	 

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

        return QOqna.oqna.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("oqnaNm", searchBy)){
            return QOqna.oqna.oqnaTitle.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QOqna.oqna.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Oqna> getAdminOqnaPage(OqnaSearchDto oqnaSearchDto, Pageable pageable) {

        List<Oqna> content = queryFactory
                .selectFrom(QOqna.oqna)
                .where(regDtsAfter(oqnaSearchDto.getSearchDateType()),
                        searchQnATypeEq(oqnaSearchDto.getQnaType()),
                        searchByLike(oqnaSearchDto.getSearchBy(),
                                oqnaSearchDto.getSearchQuery()))
                .orderBy(QOqna.oqna.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QOqna.oqna)
                .where(regDtsAfter(oqnaSearchDto.getSearchDateType()),
                        searchQnATypeEq(oqnaSearchDto.getQnaType()),
                        searchByLike(oqnaSearchDto.getSearchBy(), oqnaSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression oqnaTitleLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QOqna.oqna.oqnaTitle.like("%" + searchQuery + "%");
    }

    @Override
    public Page<MainOqnaDto> getMainOqnaPage(OqnaSearchDto oqnaSearchDto, Pageable pageable) {
        QOqna oqna = QOqna.oqna;
        QOqnaImg oqnaImg = QOqnaImg.oqnaImg;

        List<MainOqnaDto> content = queryFactory
                .select(
                        new QMainOqnaDto(
                                oqna.id,
                                oqna.oqnaTitle,
                                oqna.oqnaDetail,
                                oqnaImg.imgUrl
                                )
                )
                .from(oqnaImg)
                .join(oqnaImg.oqna, oqna)
                .where(oqnaImg.repimgYn.eq("Y"))
                .where(oqnaTitleLike(oqnaSearchDto.getSearchQuery()))
                .orderBy(oqna.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(oqnaImg)
                .join(oqnaImg.oqna, oqna)
                .where(oqnaImg.repimgYn.eq("Y"))
                .where(oqnaTitleLike(oqnaSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }
    

}
