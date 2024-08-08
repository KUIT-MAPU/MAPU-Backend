package com.mapu.domain.map.application;

import com.mapu.domain.map.dao.KeywordRepository;
import com.mapu.domain.map.domain.Keyword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public List<String> getRandomKeywords(int limit) {
        log.info("getRandomKeywords");
        List<Keyword> keywords = keywordRepository.findRandomKeywords(limit);
        return keywords.stream()
                .map(Keyword::getKeyword)
                .collect(Collectors.toList());
    }
}