package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.shop.dto.BreadItemDto;
import com.shop.dto.CakeItemDto;
import com.shop.dto.CookiesItemDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 1);

        Pageable pageable2 = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<BreadItemDto> breads = itemService.getBreadItemPage(itemSearchDto, pageable2);
        model.addAttribute("breads", breads);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 1);
        Page<CookiesItemDto> cookies = itemService.getCookiesItemPage(itemSearchDto, pageable2);
        model.addAttribute("cookies", cookies);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 1);
        Page<CakeItemDto> cakes = itemService.getCakeItemPage(itemSearchDto, pageable2);
        model.addAttribute("cakes", cakes);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 1);

        return "main";
    }

}