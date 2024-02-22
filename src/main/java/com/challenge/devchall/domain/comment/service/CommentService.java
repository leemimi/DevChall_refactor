package com.challenge.devchall.domain.comment.service;

import com.challenge.devchall.domain.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.domain.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.domain.challengepost.entity.ChallengePost;
import com.challenge.devchall.domain.challengepost.service.ChallengePostService;
import com.challenge.devchall.domain.comment.repository.CommentRepository;
import com.challenge.devchall.domain.member.entity.Member;
import com.challenge.devchall.global.base.rsData.RsData;
import com.challenge.devchall.domain.comment.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ChallengeMemberService challengeMemberService;
    private final ChallengePostService challengePostService;

    public RsData<Comment> write(String contents, long id, Member member) {

        ChallengePost post = challengePostService.getChallengePostById(id);


        ChallengeMember challengeMember = challengeMemberService.getByChallengeAndMember(post.getLinkedChallenge(), member).orElse(null);
        if (challengeMember == null){
            return RsData.of("F-1", "챌린지에 참가해야만 댓글을 달 수 있습니다.");
        }

        Comment comment = Comment.builder()
                .commentContent(contents)
                .challengeMember(challengeMember)
                .challengePost(post)
                .build();
        commentRepository.save(comment);
        return RsData.of("S-1","댓글 작성 성공!");
    }

    public List<Comment> findByChallengePost(ChallengePost challengePost){
        return commentRepository.findByChallengePost(challengePost);
    }

}
