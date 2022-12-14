package com.shop.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shop.dto.BreadItemDto;
import com.shop.dto.CakeItemDto;
import com.shop.dto.CookiesItemDto;
import com.shop.dto.ItemCommentResponseDto;
import com.shop.dto.ItemDto;
import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    
    @GetMapping(value = "/bread")
    public String bread(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<BreadItemDto> items = itemService.getBreadItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item_menu/bread";
    }
    
    @GetMapping(value = "/cookies")
    public String cookies(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<CookiesItemDto> items = itemService.getCookiesItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item_menu/cookies";
    }
    
    @GetMapping(value = "/cake")
    public String cake(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<CakeItemDto> items = itemService.getCakeItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item_menu/cake";
    }
    
    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,  @RequestParam("itemContentImgFile") List<MultipartFile> itemContentImgFileList){

        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemImgFileList.get(1).isEmpty() && itemImgFileList.get(2).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "?????? ??????????????? 3?????? ?????? ?????? ??? ?????????.");
            return "item/itemForm";
        }
        
        if(itemContentImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "????????? ?????? ?????????????????? ?????? ?????? ??? ?????????.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList, itemContentImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "?????? ?????? ??? ????????? ?????????????????????.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "???????????? ?????? ?????? ?????????.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, @RequestParam("itemContentImgFile") List<MultipartFile> itemContentImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() &&itemImgFileList.get(1).isEmpty() && itemImgFileList.get(2).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "?????? ?????? ????????? 3?????? ?????? ?????? ??? ?????????.");
            return "item/itemForm";
        }
        
        if(itemContentImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "????????? ?????? ?????? ???????????? ?????? ?????? ??? ?????????.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList, itemContentImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "?????? ?????? ??? ????????? ?????????????????????.");
            return "item/itemForm";
        }

        return "redirect:/";
    }
    
    @PostMapping(value = "/admin/itemGrade/{itemId}")
    public ResponseEntity itemUpdateGrade(@PathVariable Long itemId,@RequestBody ItemDto dto){
       ItemFormDto itemFormDto = new ItemFormDto();
       
       itemFormDto.setId(itemId);
       itemFormDto.setGrade(dto.getGrade());
       itemFormDto.setCommentsCount(dto.getCommentsCount());
       
       try {
         itemService.updateItemGrade(itemFormDto);
      } catch (Exception e) {
         e.printStackTrace();
      }
        return ResponseEntity.ok(itemId);
    }


    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item/itemMng";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId, Principal principal){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        
        List<ItemCommentResponseDto> itemComments = itemFormDto.getItemComments();
        
        /* ?????? ?????? */
        if (itemComments != null && !itemComments.isEmpty()) {
            model.addAttribute("itemComments", itemComments);
        }

        /* ????????? ?????? */
        if (principal != null) {
            model.addAttribute("member", principal.getName());

            /*????????? ????????? ???????????? ??????*/
            if (itemFormDto.getCreatedBy().equals(principal.getName())) {
                model.addAttribute("writer", true);
            }
        }
        
        itemService.updateView(itemId); // views ++ ?????????
        model.addAttribute("item", itemFormDto);
        
        return "item/itemDtl";
    }

}