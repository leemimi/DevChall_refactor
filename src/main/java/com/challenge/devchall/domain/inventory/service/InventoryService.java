package com.challenge.devchall.domain.inventory.service;

import com.challenge.devchall.domain.inventory.repository.InventoryRepository;
import com.challenge.devchall.global.base.rsData.RsData;
import com.challenge.devchall.domain.inventory.entity.Inventory;
import com.challenge.devchall.domain.item.entity.Item;
import com.challenge.devchall.domain.member.entity.Member;
import com.challenge.devchall.domain.pointHistory.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final PointHistoryService pointHistoryService;

    public RsData<Inventory> create(Member member, Item item, boolean equipped){
        if(isPurchased(member, item)){
            return RsData.of("F-1", "이미 구매한 아이템입니다.");
        }
        Inventory inventory = inventoryRepository.save(Inventory.builder()
                .member(member)
                .item(item)
                .equipped(equipped)
                .build());
        return RsData.of("S-1", "구매에 성공하였습니다.", inventory);
    }

    public boolean isPurchased(Member member, Item item){ //true: 이미 구매함, false: 처음 구매
        return inventoryRepository.findByMemberAndItem(member, item).isPresent();
    }

    public void changeFontEquip(long itemId, Member member) {

        if(member!=null){
            Inventory  equippedFont=  member.getEquippedFont();
            if(equippedFont != null)
                equippedFont.unequip();

            for(Inventory inventory: member.getInventoryList()){

                if(inventory.getItem().getId()==itemId){

                    inventory.equip();

                    return;
                }
            }
        }
    }

    public void changeCharacterEquip(long itemId, Member member) {
        if(member!=null){
            Inventory equippedCharacter=  member.getEquippedCharacter();
            if(equippedCharacter != null)
                equippedCharacter.unequip();

            for(Inventory inventory: member.getInventoryList()){

                if(inventory.getItem().getId()==itemId){

                    inventory.equip();

                    return;
                }
            }
        }
    }
}
