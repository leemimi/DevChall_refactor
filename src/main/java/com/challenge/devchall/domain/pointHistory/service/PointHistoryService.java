package com.challenge.devchall.domain.pointHistory.service;

import com.challenge.devchall.domain.pointHistory.repository.PointHistoryRepository;
import com.challenge.devchall.domain.member.entity.Member;
import com.challenge.devchall.domain.pointHistory.entity.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PointHistoryService {
    private final PointHistoryRepository pointHistoryRepository;

    public List<PointHistory> getAllPointHistories() {
        return pointHistoryRepository.findAll();
    }

    public void addPointHistory(Member member, long point, String type) {

        PointHistory pointHistory = PointHistory.builder()
                .member(member)
                .point(point)
                .currentpoint(member.getPoint().getCurrentPoint())
                .type(type)
                .build();

        pointHistoryRepository.save(pointHistory);
    }

    public List<PointHistory> getPointHistoriesByMember(Member member) {
        return pointHistoryRepository.findByMember(member);
    }
}
