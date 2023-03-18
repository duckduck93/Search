package com.example.search.keyword;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository repository;

    public List<Keyword> findPopularKeywords(int size) {
        Pageable pageable = PageRequest.of(
                0,
                size,
                Sort.by(Sort.Order.desc("count"))
                        .and(Sort.by(Sort.Order.asc("name").ignoreCase()))
        );
        List<KeywordEntity> entities = this.repository.findBy(pageable);
        return entities.stream().map(Keyword::from).toList();
    }

    @Transactional
    public Keyword increaseCount(String keywordName) {
        Keyword keyword = this.repository.findByName(keywordName)
                .map(Keyword::from)
                .orElse(new Keyword(keywordName, 0L));
        keyword.increaseCount();

        KeywordEntity result = repository.save(KeywordEntity.from(keyword));
        return Keyword.from(result);
    }
}
