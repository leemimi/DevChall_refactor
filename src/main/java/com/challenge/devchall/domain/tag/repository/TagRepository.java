package com.challenge.devchall.domain.tag.repository;

import com.challenge.devchall.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
