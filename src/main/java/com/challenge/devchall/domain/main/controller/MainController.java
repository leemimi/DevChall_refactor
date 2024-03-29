package com.challenge.devchall.domain.main.controller;

import com.challenge.devchall.domain.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.domain.point.service.PointService;
import com.challenge.devchall.global.base.rq.Rq;
import com.challenge.devchall.domain.challange.entity.Challenge;
import com.challenge.devchall.domain.challange.service.ChallengeService;
import com.challenge.devchall.global.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    final private ChallengeService challengeService;
    final private ChallengeMemberService challengeMemberService;
    final private PointService pointService;
    final private PhotoService photoService;

    private final Rq rq;
    @GetMapping("/")
    public String showMain(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(required = false, defaultValue = "") String language,
                           @RequestParam(required = false, defaultValue = "") String subject){
        page = page<0? 0 : page;

        language = language.isBlank()? null: language.trim();
        subject = subject.isBlank()? null : subject.trim();

        if(rq.isLogin()){

            //내가 참여하지 않은 챌린지 중 공개(true)인 것만 가져오기
            Page<Challenge> notJoinChallengeList = challengeService.getNotJoinChallengeList(page, language, subject, rq.getMember());
            List<Challenge > joinChallengeList = challengeService.getJoinChallenge(rq.getMember());

            //나의 챌린지(공개, 비공개 상관 없음) => 내가 챌린지 멤버인 것 들 ... 
            model.addAttribute("joinChallengeList", joinChallengeList);
            model.addAttribute("challenges", notJoinChallengeList);
            model.addAttribute("photoService", photoService);
        } else {
            Page<Challenge> allChallengeList = challengeService.getChallengeList(page, language,subject);

            model.addAttribute("challenges", allChallengeList);
        }
        return "index";
    }

//    @GetMapping("/test")
//    public String test() {
//        pointService.settle();
//        return "redirect:/usr/member/me" ;
//    }
//
//    @ResponseBody
//    @GetMapping("/test2")
//    public List<SettleChallengeDTO> test2() {
//
//        return challengeMemberService.getSettleChallengeDto();
//    }

}
