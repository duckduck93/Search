package com.example.search.keyword.repository;

import com.example.search.keyword.Keyword;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class KeywordEntity {
    @Id
    private String name;
    @Column
    private Long count;

    public static KeywordEntity from(Keyword keyword) {
        KeywordEntity entity = new KeywordEntity();
        entity.name = keyword.getName();
        entity.count = keyword.getCount();
        return entity;
    }
}
