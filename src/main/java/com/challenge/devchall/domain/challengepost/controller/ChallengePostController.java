package com.challenge.devchall.domain.challengepost.controller;


import com.challenge.devchall.global.base.config.AppConfig;
import com.challenge.devchall.global.base.rq.Rq;
import com.challenge.devchall.global.base.rsData.RsData;
import com.challenge.devchall.domain.challange.entity.Challenge;
import com.challenge.devchall.domain.challange.service.ChallengeService;
import com.challenge.devchall.domain.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.domain.challengeMember.repository.ChallengeMemberRepository;
import com.challenge.devchall.domain.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.domain.challengepost.entity.ChallengePost;
import com.challenge.devchall.domain.challengepost.service.ChallengePostService;
import com.challenge.devchall.domain.comment.service.CommentService;
import com.challenge.devchall.domain.member.entity.Member;
import com.challenge.devchall.global.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/challenge")
public class ChallengePostController {

    private final ChallengePostService challengePostService;
    private final ChallengeService challengeService;
    private final ChallengeMemberService challengeMemberService;
    private final PhotoService photoService;
    private final Rq rq;
    private final CommentService commentService;
    private final ChallengeMemberRepository challengeMemberRepository;

    @GetMapping("/write_form/{id}")
    public String writeChallengePost(Model model, @PathVariable("id") long id) {

        Challenge challenge = this.challengeService.getChallengeById(id);

        model.addAttribute("challenge", challenge);

        return "usr/challenge/write_form";
    }

    @PostMapping("/write_form/{id}")
    public String createChallenge(@PathVariable("id") long id,
                                  @RequestParam String title,
                                  @RequestParam String contents,
                                  @RequestParam boolean status,
                                  @RequestParam long postScore,
                                  @RequestParam MultipartFile file,
                                  Model model
    ) throws IOException {

        //포스트를 쓰기 전에, 쓸 수 있는지부터 검사 해야한다.
        Challenge linkedChallenge = challengeService.getChallengeById(id);
        Member member = rq.getMember();

        String photoUrl = photoService.getPhotoUrl(file);

        if(photoUrl.startsWith("F-")){
            return rq.redirectWithMsg("/", RsData.of(photoUrl, "이미지 업로드에 실패하였습니다."));
        }

        ChallengePost post = challengePostService.write(title, contents, status, postScore, id, photoUrl, member).getData();

        model.addAttribute("linkedChallenge", linkedChallenge);
        model.addAttribute("post", post);

        return "redirect:/usr/challenge/detail/{id}";
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") long id) {

        ChallengePost challengePostById = challengePostService.getChallengePostById(id);

        Long linkedChallengeId = challengePostById.getLinkedChallenge().getId();

        challengePostService.deletePost(id);

        System.out.println("linkedChallengeId = " + linkedChallengeId);

        return "redirect:/usr/challenge/detail/%d".formatted(linkedChallengeId);
    }

    @GetMapping("/postdetail/{id}")
    public String postDetail(@PathVariable long id, Model model) {

        ChallengePost post = this.challengePostService.getChallengePostById(id);

        Challenge linkedChallenge = post.getLinkedChallenge();

        model.addAttribute("post", post);
        model.addAttribute("linkedChallenge", linkedChallenge);
        model.addAttribute("commentList", commentService.findByChallengePost(post));
        model.addAttribute("challengePostService", challengePostService);

        return "usr/challenge/postdetail";
    }

    @GetMapping("/modifypost/{id}")
    public String modifyPost(@PathVariable long id, Model model) {

        ChallengePost post = this.challengePostService.getChallengePostById(id);

        Challenge linkedChallenge = post.getLinkedChallenge();

        model.addAttribute("post", post);
        model.addAttribute("linkedChallenge", linkedChallenge);

        return "usr/challenge/modifypost";
    }

    @PostMapping("/modifypost/{id}")
    public String modifyPost(@PathVariable long id,
                             @RequestParam String title,
                             @RequestParam String contents,
                             @RequestParam boolean status) {

        challengePostService.modifyPost(id, title, contents, status);

        return "redirect:/usr/challenge/postdetail/{id}";
    }

    @GetMapping("/report/{id}")
    public String reportPost(@PathVariable("id") long id) {

        ChallengePost challengePostById = challengePostService.getChallengePostById(id);

        Long linkedChallengeId = challengePostById.getLinkedChallenge().getId();

        String loginId = rq.getMember().getLoginID();

        String postCreatorId = challengePostById.getCreatorId();

        if (loginId.equals(postCreatorId)) {
            System.out.println("자신의 글은 신고할 수 없습니다.");
            return "redirect:/usr/challenge/postdetail/{id}";
        }

        if (challengePostService.hasReportedPost(id, loginId)) {
            System.out.println("이미 신고한 게시물입니다.");
            return rq.historyBack("이미 신고한 게시물 입니다.");
        }

        challengePostService.addReportedBy(id, loginId);

        challengePostService.incrementCount(id);
        if (challengePostById.getReportCount() >= AppConfig.getReportCount()) {
            ChallengeMember challengeMember = challengeMemberService.getByChallengeAndMember(challengePostById.getLinkedChallenge(), challengePostById.getChallenger()).orElse(null);
            if (challengeMember != null) {
                challengeMember.increaseOutCount();
                challengeMemberRepository.save(challengeMember);
            }
            return "redirect:/usr/challenge/detail/{id}".replace("{id}", String.valueOf(linkedChallengeId));
        }

        return "redirect:/usr/challenge/postdetail/{id}";
    }
}
