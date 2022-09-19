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
import com.shop.constant.OqnaStatus;
import com.shop.dto.OqnaSearchDto;
import com.shop.entity.Oqna;
import com.shop.entity.QOqna;


public class OqnaRepositoryCustomImpl implements OqnaRepositoryCustom{
	
	private JPAQueryFactory queryFactory;

    public OqnaRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

	
	  private BooleanExpression searchOqnaStatusEq(OqnaStatus oqnaStatus){ return
	  oqnaStatus == null ? null :
	  QOqna.oqna.oqnaStatus.eq(oqnaStatus); }
	 

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

        if(StringUtils.equals("oqnaTitle", searchBy)){
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
                        searchOqnaStatusEq(oqnaSearchDto.getSearchOqnaStatus()),
                        searchByLike(oqnaSearchDto.getSearchBy(),
                                oqnaSearchDto.getSearchQuery()))
                .orderBy(QOqna.oqna.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QOqna.oqna)
                .where(regDtsAfter(oqnaSearchDto.getSearchDateType()),
                        searchOqnaStatusEq(oqnaSearchDto.getSearchOqnaStatus()),
                        searchByLike(oqnaSearchDto.getSearchBy(), oqnaSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }


//    @Override
//    public Page<MyOqnaHistDto> getMainOqnaPage(OqnaSearchDto oqnaSearchDto, Pageable pageable) {
//        QOqna oqna = QOqna.oqna;
//        QOqnaImg oqnaImg = QOqnaImg.oqnaImg;
//
//        List<MyOqnaHistDto> content = queryFactory
//                .select(
//                        new QMyOqnaHistDto(
//                                oqna.id,
//                                oqna.oqnaTitle,
//                                oqna.oqnaDetail,
//                                oqnaImg.imgUrl,
//                                oqna.oqnaDate,
//                                oqna.oqnaStatus
//                                )
//                )
//                .from(oqnaImg)
//                .join(oqnaImg.oqna, oqna)
//                .where(oqnaImg.repimgYn.eq("Y"))
//                .where(oqnaTitleLike(oqnaSearchDto.getSearchQuery()))
//                .orderBy(oqna.id.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        long total = queryFactory
//                .select(Wildcard.count)
//                .from(oqnaImg)
//                .join(oqnaImg.oqna, oqna)
//                .where(oqnaImg.repimgYn.eq("Y"))
//                .where(oqnaTitleLike(oqnaSearchDto.getSearchQuery()))
//                .fetchOne()
//                ;
//
//        return new PageImpl<>(content, pageable, total);
//    }
    

}
