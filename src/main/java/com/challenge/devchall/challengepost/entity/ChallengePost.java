package com.challenge.devchall.challengepost.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.challange.entity.Challenge;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Getter
public class ChallengePost extends BaseEntity {

    private String postTitle;
    private String postContents;
    private boolean postIsPublic;

    //FIXME 일단 보류
//    private String postImg;
//    private boolean postModify;
//    private int postStarPoint;

    @ManyToOne
    private Challenge linkedChallenge;

    public void modifyPost(String postTitle, String postContents, boolean postIsPublic){

        this.postTitle = postTitle;
        this.postContents = postContents;
        this.postIsPublic = postIsPublic;

    }

    public boolean isPublic(){

        if(this.postIsPublic){
            return true;
        }
        else {
            return false;
        }

    }

}