package com.example.search.keyword;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<KeywordEntity, String> {
    List<KeywordEntity> findBy(Pageable pageable);

    Optional<KeywordEntity> findByName(String name);
}