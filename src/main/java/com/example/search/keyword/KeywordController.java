package com.example.search.keyword;

import com.example.search.keyword.exchange.KeywordResponse;
import com.example.search.keyword.exchange.KeywordSearchRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/keywords")
@RequiredArgsConstructor
public class KeywordController {
    private final KeywordService service;

    @GetMapping(path = "/popular")
    public List<KeywordResponse> popular(@Valid final KeywordSearchRequest request) {
        List<Keyword> keywords = this.service.findPopularKeywords(request.getSize());
        return keywords.stream().map(KeywordResponse::from).toList();
    }
}
