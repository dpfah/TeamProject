package com.shop.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.dto.ItemCommentRequestDto;
import com.shop.service.ItemCommentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ItemCommentApiController {

    private final ItemCommentService itemCommentService;
    
    /* CREATE */
    @PostMapping("/item/{id}/itemComments")
    public ResponseEntity itemCommentSave(@PathVariable Long id, @RequestBody ItemCommentRequestDto dto,
                                      Principal principal) {
    	return ResponseEntity.ok(itemCommentService.itemCommentSave(principal.getName(), id, dto));
        
    }
    
//    /* UPDATE */    
//    @PutMapping({"/item/{itemId}/itemComments/{id}"})    
//    public ResponseEntity update(@PathVariable Long itemId, @PathVariable Long id, @RequestBody ItemCommentRequestDto dto) {
//    	itemCommentService.update(id, dto);        
//    	return ResponseEntity.ok(id);    }        
//   
//    /* DELETE */    
//    @DeleteMapping("/item/{itemId}/itemComments/{id}")    
//    public ResponseEntity delete(@PathVariable Long itemId, @PathVariable Long id) { 
//    	itemCommentService.delete(id);        
//    	return ResponseEntity.ok(id);    }
}
