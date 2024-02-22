package com.challenge.devchall.domain.comment.controller;

import com.challenge.devchall.domain.challengepost.service.ChallengePostService;
import com.challenge.devchall.domain.member.entity.Member;
import com.challenge.devchall.global.base.rq.Rq;
import com.challenge.devchall.global.base.rsData.RsData;
import com.challenge.devchall.domain.comment.entity.Comment;
import com.challenge.devchall.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@RequestMapping("usr/challenge/comment")
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final Rq rq;
    private final ChallengePostService challengePostService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write/{id}")
    public String createComment(@PathVariable("id") Long id,
                                @RequestParam String contents
    )throws IOException {

        Member member = rq.getMember();

        RsData<Comment> commentRsData = commentService.write(contents, id, member);
        if (commentRsData.getResultCode().equals("F-1")){
            return rq.historyBack("댓글은 챌린지에 참가한 맴버만 작성 할 수 있습니다.");
        }

        return "redirect:/usr/challenge/postdetail/{id}";

    }

}
