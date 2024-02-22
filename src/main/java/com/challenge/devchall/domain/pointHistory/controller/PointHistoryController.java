package com.challenge.devchall.domain.pointHistory.controller;

import com.challenge.devchall.global.base.rq.Rq;
import com.challenge.devchall.domain.member.service.MemberService;
import com.challenge.devchall.domain.pointHistory.entity.PointHistory;
import com.challenge.devchall.domain.pointHistory.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;



@Controller
@RequiredArgsConstructor
public class PointHistoryController {
    private final MemberService memberService;

    private final PointHistoryService pointHistoryService;
    private final Rq rq;

    @GetMapping("/usr/point/pointHistory")
    public String getPointHistory(Model model) {

        List<PointHistory> pointHistoryList = pointHistoryService.getPointHistoriesByMember(rq.getMember());
        model.addAttribute("pointHistoryList", pointHistoryList);
        return "usr/point/pointHistory";
    }
}