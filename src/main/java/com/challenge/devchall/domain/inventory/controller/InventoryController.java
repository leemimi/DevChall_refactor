package com.challenge.devchall.domain.inventory.controller;

import com.challenge.devchall.global.base.rq.Rq;
import com.challenge.devchall.domain.inventory.service.InventoryService;
import com.challenge.devchall.domain.item.entity.Item;
import com.challenge.devchall.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/inventory")
public class InventoryController {
    private final InventoryService inventoryService;
    private final ItemService itemService;
    private final Rq rq;

    @GetMapping("/change/{item}")
    public String changeEquip(@PathVariable("item") long itemId){
        Item item = itemService.getById(itemId).orElse(null);
        if(item != null){
            if(item.getType().equals("font")){
                inventoryService.changeFontEquip(itemId, rq.getMember());
            } else if (item.getType().equals("character")) {
                inventoryService.changeCharacterEquip(itemId, rq.getMember());
            }
        }


        return "redirect:/usr/member/store";
    }

    @GetMapping("/me/change/{item}")
    public String changeEquipMypage(@PathVariable("item") long itemId){
        Item item = itemService.getById(itemId).orElse(null);
        if(item != null){
            if(item.getType().equals("font")){
                inventoryService.changeFontEquip(itemId, rq.getMember());
            } else if (item.getType().equals("character")) {
                inventoryService.changeCharacterEquip(itemId, rq.getMember());
            }
        }
        return "redirect:/usr/member/me";
    }
}
