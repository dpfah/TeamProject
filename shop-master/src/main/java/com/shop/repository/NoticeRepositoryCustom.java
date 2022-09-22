package com.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shop.dto.NoticeSearchDto;
import com.shop.dto.MainNoticeDto;
import com.shop.entity.Notice;

public interface NoticeRepositoryCustom {

    Page<Notice> getAdminNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable);

    Page<MainNoticeDto> getMainNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable);

}