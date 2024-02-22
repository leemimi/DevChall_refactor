package com.challenge.devchall.domain.pointHistory.repository;

import com.challenge.devchall.domain.member.entity.Member;
import com.challenge.devchall.domain.pointHistory.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    List<PointHistory> findByMember(Member member);
}