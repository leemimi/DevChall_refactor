package com.challenge.devchall.domain.challengeMember.repository;

import com.challenge.devchall.domain.challange.entity.Challenge;
import com.challenge.devchall.domain.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.domain.challengepost.dto.SettleChallengeDTO;
import com.challenge.devchall.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChallengeMemberRepository extends JpaRepository<ChallengeMember, Long> {

    List<ChallengeMember> findByChallenger(Member member);

    List<ChallengeMember> findByLinkedChallenge(Challenge challenge);

    Optional<ChallengeMember> findByLinkedChallengeAndChallenger(Challenge challenge, Member member);

    Long findByChallengerId (Long id);

    @Query("SELECT cm.id as challengemember_id, COUNT(p.id) as count " +
            "FROM ChallengeMember cm " +
            "JOIN cm.linkedChallenge c " +
            "JOIN ChallengePost p ON cm.challenger = p.challenger AND p.linkedChallenge = c " +
            "WHERE c.endDate = :today AND c.settleComplete = false " +
            "GROUP BY cm.id") // 챌린지 아이디 + 멤버 아이디
    List<SettleChallengeDTO> findChallengeMemberCountByEndDate(@Param("today") LocalDate today);
}
