package com.challenge.devchall.domain.inventory.repository;

import com.challenge.devchall.domain.inventory.entity.Inventory;
import com.challenge.devchall.domain.item.entity.Item;
import com.challenge.devchall.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
        Optional<Inventory> findByMemberAndItem(Member member, Item item);
}
