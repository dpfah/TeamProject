package com.shop.service;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shop.dto.BreadItemDto;
import com.shop.dto.CakeItemDto;
import com.shop.dto.CookiesItemDto;
import com.shop.dto.ItemCommentResponseDto;
import com.shop.dto.ItemContentImgDto;
import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.ItemComment;
import com.shop.entity.ItemContentImg;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemCommentRepository;
import com.shop.repository.ItemContentImgRepository;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;
    
    private final ItemContentImgService itemContentImgService;

    private final ItemImgRepository itemImgRepository;
    
    private final ItemContentImgRepository itemContentImgRepository;
    
    private final ItemCommentRepository itemCommentRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList, List<MultipartFile> itemContentImgFileList) throws Exception{

        //상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);
        
        
        //프로필 이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if(i == 0)
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        
      //상세 이미지 등록
        for(int i=0;i<itemContentImgFileList.size();i++){
            ItemContentImg itemContentImg = new ItemContentImg();
            itemContentImg.setItem(item);

            itemContentImgService.saveItemContentImg(itemContentImg, itemContentImgFileList.get(i));
        }

        return item.getId();
    }
    

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemContentImg> itemContentImgList = itemContentImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemComment> itemCommentList = itemCommentRepository.findByItemIdOrderByIdDesc(itemId);
        
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        List<ItemContentImgDto> itemContentImgDtoList = new ArrayList<>();
        List<ItemCommentResponseDto> itemCommentResponseDtoList = new ArrayList<>();
        
        for(ItemComment itemComment : itemCommentList) {
           ItemCommentResponseDto itemCommentResponseDto = ItemCommentResponseDto.of(itemComment);
           itemCommentResponseDtoList.add(itemCommentResponseDto);
        }
        
        
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }
        
      for (ItemContentImg itemContentImg : itemContentImgList) {
            ItemContentImgDto itemContentImgDto = ItemContentImgDto.of(itemContentImg);
            itemContentImgDtoList.add(itemContentImgDto);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemComments(itemCommentResponseDtoList);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        itemFormDto.setItemContentImgDtoList(itemContentImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList, List<MultipartFile> itemContentImgFileList) throws Exception{
        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);
        List<Long> itemImgIds = itemFormDto.getItemImgIds();
        List<Long> itemContentImgIds = itemFormDto.getItemContentImgIds();

        //상품 대표이미지, 프로필이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),
                    itemImgFileList.get(i));
        }
        
      //상품 상세이미지 등록
        for(int i=0;i<itemContentImgFileList.size();i++){
            itemContentImgService.updateItemContentImg(itemContentImgIds.get(i),
                    itemContentImgFileList.get(i));
        }

        return item.getId();
    }
    
    
    public Long updateItemGrade(ItemFormDto itemFormDto) throws Exception{
        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItemGrade(itemFormDto);

        return item.getId();
    }
    
    
    /* 조회수 */
    @Transactional
    public int updateView(Long id) {
        return itemRepository.updateView(id);
    }


    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<BreadItemDto> getBreadItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getBreadItemPage(itemSearchDto, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<CookiesItemDto> getCookiesItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getCookiesItemPage(itemSearchDto, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<CakeItemDto> getCakeItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getCakeItemPage(itemSearchDto, pageable);
    }




}