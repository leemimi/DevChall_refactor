package com.challenge.devchall.challengepost.service;

import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.repository.ChallengeRepository;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.challengepost.repository.ChallengePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengePostService {

    private final ChallengePostRepository challengePostRepository;
    private final ChallengeService challengeService;

    public void write(String title, String contents, String status, long id) {

        boolean formattingStatus = formattingStatus(status);

        Challenge challengeById = challengeService.getChallengeById(id);

        ChallengePost challengePost = ChallengePost.builder()
                .postTitle(title)
                .postContents(contents)
                .postIsPublic(formattingStatus)
                .linkedChallenge(challengeById)
                .build();

        challengePostRepository.save(challengePost);
    }

    //FIXME js로 변환해서 받아오는 방법 고려
    public boolean formattingStatus(String status){

        boolean challengeStatus;

        if(status.equals("비공개")) {
            challengeStatus = false;
        }
        else{
            challengeStatus = true;
        }

        return challengeStatus;
    }
}