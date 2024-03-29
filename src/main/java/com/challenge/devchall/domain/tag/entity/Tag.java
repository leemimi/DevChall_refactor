package com.challenge.devchall.domain.tag.entity;

import com.challenge.devchall.domain.challange.entity.Challenge;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"linkedChallenge"})
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String challengeLanguage;
    private String challengeSubject;
    private String challengePostType;

    @OneToOne
    private Challenge linkedChallenge;

    public void updateLinkedChallenge(Challenge challenge){
        this.linkedChallenge = challenge;
    }

}
