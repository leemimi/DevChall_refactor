package com.challenge.devchall.domain.item.repository;

import com.challenge.devchall.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByName(String name);

    List<Item> findByType(String type);
}

