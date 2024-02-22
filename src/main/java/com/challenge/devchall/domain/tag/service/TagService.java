package com.challenge.devchall.domain.tag.service;

import com.challenge.devchall.domain.tag.entity.Tag;
import com.challenge.devchall.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag createTag(String language, String subject, String postType){

        Tag tag = Tag.builder()
                .challengeLanguage(language)
                .challengeSubject(subject)
                .challengePostType(postType)
                .build();

        tagRepository.save(tag);

        return tag;
    }

}
