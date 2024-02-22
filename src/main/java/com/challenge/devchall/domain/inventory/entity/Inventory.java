package com.challenge.devchall.domain.inventory.entity;

import com.challenge.devchall.global.base.BaseEntity;
import com.challenge.devchall.domain.item.entity.Item;
import com.challenge.devchall.domain.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true , exclude = {"member", "item"})
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Inventory extends BaseEntity {
    @ManyToOne
    private Member member;
    private boolean equipped;
    @ManyToOne
    private Item item;

    public void unequip(){
        this.equipped = false;
    }
    public void equip(){
        this.equipped = true;
    }
    
}
