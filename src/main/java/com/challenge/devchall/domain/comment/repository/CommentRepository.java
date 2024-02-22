package com.challenge.devchall.domain.comment.repository;

import com.challenge.devchall.domain.challengepost.entity.ChallengePost;
import com.challenge.devchall.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>{

    List<Comment> findByChallengePost(ChallengePost challengePost);

}
