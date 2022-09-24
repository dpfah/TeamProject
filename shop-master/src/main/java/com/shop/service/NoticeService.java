package com.shop.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shop.dto.NoticeFormDto;
import com.shop.dto.NoticeImgDto;
import com.shop.dto.NoticeSearchDto;
import com.shop.dto.MainNoticeDto;
import com.shop.entity.Notice;
import com.shop.entity.NoticeImg;
import com.shop.repository.NoticeImgRepository;
import com.shop.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    private final NoticeImgService noticeImgService;

    private final NoticeImgRepository noticeImgRepository;

    public Long saveNotice(NoticeFormDto noticeFormDto, List<MultipartFile> noticeImgFileList) throws Exception{

        // 공지 등록
        Notice notice = noticeFormDto.createNotice();
        noticeRepository.save(notice);

        // 공지이미지 등록
        for(int i=0;i<noticeImgFileList.size();i++){
            NoticeImg noticeImg = new NoticeImg();
            noticeImg.setNotice(notice);

            noticeImgService.saveNoticeImg(noticeImg, noticeImgFileList.get(i));
        }

        return notice.getId();
    }
    
    @Transactional(readOnly = true)
    public NoticeFormDto getNoticePost(Long noticePostId){
        List<NoticeImg> noticeImgList = noticeImgRepository.findByNoticeIdOrderByIdAsc(noticePostId);
        List<NoticeImgDto> noticeImgDtoList = new ArrayList<>();
        for (NoticeImg noticeImg : noticeImgList) {
            NoticeImgDto noticeImgDto = NoticeImgDto.of(noticeImg);
            noticeImgDtoList.add(noticeImgDto);
        }
       

        Notice notice = noticeRepository.findById(noticePostId)
                .orElseThrow(EntityNotFoundException::new);
        NoticeFormDto noticeFormDto = NoticeFormDto.of(notice);
        noticeFormDto.setNoticeImgDtoList(noticeImgDtoList);
        return noticeFormDto;
    }


    public Long updateNotice(NoticeFormDto noticeFormDto, List<MultipartFile> noticeImgFileList) throws Exception{
        // 공지 수정
        Notice notice = noticeRepository.findById(noticeFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        notice.updateNotice(noticeFormDto);
        List<Long> noticeImgIds = noticeFormDto.getNoticeImgIds();

        // 공지이미지 수정
        for(int i=0;i<noticeImgFileList.size();i++){
        	noticeImgService.updateNoticeImg(noticeImgIds.get(i), noticeImgFileList.get(i));
        }

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
    

    
    public void deleteNotice(Long noticePostId) throws Exception{
        Notice notice = noticeRepository.findById(noticePostId)
                .orElseThrow(EntityNotFoundException::new);
        
        noticeRepository.deleteById(noticePostId); //notice게시글을 삭제한다.
        
    }
    

}