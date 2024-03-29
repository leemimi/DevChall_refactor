package com.challenge.devchall.domain.comment.entity;

import com.challenge.devchall.domain.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.domain.challengepost.entity.ChallengePost;
import com.challenge.devchall.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Comment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private ChallengePost challengePost;


    @ManyToOne(fetch = FetchType.LAZY)
    private ChallengeMember challengeMember;

    @Column(nullable = false)
    private String commentContent;

    private void modifyComment(String commentContent){
        this.commentContent = commentContent;
    }

    @Builder
    public Comment(ChallengePost challengePost, String commentContent, ChallengeMember challengeMember) {
        this.challengePost = challengePost;
        this.commentContent = commentContent;
        this.challengeMember = challengeMember;
    }

}
