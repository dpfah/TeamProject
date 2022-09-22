package com.shop.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.dto.NoticeFormDto;
import com.shop.dto.NoticeSearchDto;
import com.shop.dto.MainNoticeDto;
import com.shop.entity.Notice;
import com.shop.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Long saveNotice(NoticeFormDto noticeFormDto) throws Exception{

        //공지 등록
        Notice notice = noticeFormDto.createNotice();
        noticeRepository.save(notice);


        return notice.getId();
    }


    public Long updateNotice(NoticeFormDto noticeFormDto) throws Exception{
        //공지 수정
        Notice notice = noticeRepository.findById(noticeFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        notice.updateNotice(noticeFormDto);

       

        return notice.getId();
    }

    @Transactional(readOnly = true)
    public Page<Notice> getAdminNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable){
        return noticeRepository.getAdminNoticePage(noticeSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainNoticeDto> getMainNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable){
        return noticeRepository.getMainNoticePage(noticeSearchDto, pageable);
    }
    
    @Transactional(readOnly = true)
    public NoticeFormDto getNoticeDtl(Long noticePostId){
       

        Notice notice = noticeRepository.findById(noticePostId)
                .orElseThrow(EntityNotFoundException::new);
        NoticeFormDto noticeFormDto = NoticeFormDto.of(notice);
        return noticeFormDto;
    }
    

}