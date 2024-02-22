package com.challenge.devchall.domain.point.repository;

import com.challenge.devchall.domain.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
}
