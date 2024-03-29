package com.challenge.devchall.domain.challange.repository;

import com.challenge.devchall.domain.challange.entity.Challenge;
import com.challenge.devchall.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    //language와 subject를 동적 쿼리를 이용해서 challenge list 검색 (page: 30)
    @Query("SELECT c FROM Challenge c " +
            "LEFT JOIN c.challengeTag ct " +
            "WHERE (:challengeLanguage IS NULL OR ct.challengeLanguage = :challengeLanguage) " +
            "AND c.startDate >  CURRENT_DATE " +
            "AND (:challengeSubject IS NULL OR ct.challengeSubject = :challengeSubject) " +
            "AND c.challengeStatus = true")
    Page<Challenge> findByConditions(@Param("challengeLanguage") String challengeLanguage,
                                     @Param("challengeSubject") String challengeSubject,
                                     Pageable pageable);

    @Query("SELECT c " +
            "FROM Challenge c " +
            "LEFT JOIN Tag ct ON ct.linkedChallenge = c " +
            "LEFT JOIN ChallengeMember cm ON cm.linkedChallenge = c AND cm.challenger = :me " +
            "WHERE cm.challenger = null " +
            "AND c.startDate >  CURRENT_DATE " +
            "AND (:challengeLanguage IS NULL OR ct.challengeLanguage = :challengeLanguage) " +
            "AND (:challengeSubject IS NULL OR ct.challengeSubject = :challengeSubject) " +
            "AND c.challengeStatus = true")
    Page<Challenge> findChallengeByNotJoin(@Param("challengeLanguage") String challengeLanguage,
                                           @Param("challengeSubject") String challengeSubject,
                                           @Param("me") Member me,
                                           Pageable pageable);

    @Query("SELECT c FROM Challenge c " +
            "LEFT JOIN c.challengeTag ct " +
            "WHERE (:challengeLanguage IS NULL OR ct.challengeLanguage = :challengeLanguage) " +
            "AND c.startDate >  CURRENT_DATE " +
            "AND (:challengeSubject IS NULL OR ct.challengeSubject = :challengeSubject) " +
            "AND c.challengeStatus = true")
    List<Challenge> findByConditions(@Param("challengeLanguage") String challengeLanguage,
                                     @Param("challengeSubject") String challengeSubject);


    @Query("SELECT c " +
            "FROM Challenge c " +
            "LEFT JOIN Tag ct ON ct.linkedChallenge = c " +
            "LEFT JOIN ChallengeMember cm ON cm.linkedChallenge = c AND cm.challenger = :me " +
            "WHERE cm.challenger = null " +
            "AND c.startDate >  CURRENT_DATE " +
            "AND (:challengeLanguage IS NULL OR ct.challengeLanguage = :challengeLanguage) " +
            "AND (:challengeSubject IS NULL OR ct.challengeSubject = :challengeSubject) " +
            "AND c.challengeStatus = true")
    List<Challenge> findChallengeByNotJoin(@Param("challengeLanguage") String challengeLanguage,
                                           @Param("challengeSubject") String challengeSubject,
                                           @Param("me") Member me);

    @Query("SELECT c " +
            "FROM Challenge c " +
            "LEFT JOIN ChallengeMember cm ON cm.linkedChallenge = c AND cm.challenger = :me " +
            "WHERE cm.challenger IS NOT NULL ")
    List<Challenge> findJoinChallenge(@Param("me") Member me);


    Optional<Challenge> findByChallengeName(String challengeName);

}
