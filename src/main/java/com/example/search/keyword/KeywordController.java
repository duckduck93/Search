package com.example.search.keyword;

import com.example.search.errors.RequestParameterMaxValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/keywords")
@RequiredArgsConstructor
public class KeywordController {
    private final KeywordService service;

    @GetMapping(path = "/popular")
    public List<KeywordResponse> popular(@RequestParam(value = "size", required = false) Integer size) {
        size = Optional.ofNullable(size).orElse(10);
        if (size > 10) {
            throw new RequestParameterMaxValueException(10);
        }
        List<Keyword> keywords = this.service.findPopularKeywords(size);
        return keywords.stream().map(KeywordResponse::from).toList();
    }
}
