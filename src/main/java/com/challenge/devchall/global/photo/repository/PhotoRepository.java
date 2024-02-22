package com.challenge.devchall.global.photo.repository;

import com.challenge.devchall.global.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {


}
