package com.example.search.blog.client.kakao;

import com.example.search.blog.Blog;
import com.example.search.blog.client.ApiResponseSchemaErrorException;
import com.example.search.blog.client.ApiServerErrorException;
import com.example.search.blog.client.BlogSearchClient;
import com.example.search.blog.client.kakao.model.KakaoDocument;
import com.example.search.blog.client.kakao.model.KakaoResponse;
import com.example.search.blog.exchange.SortType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Order(1) // BlogSearchClient 중 첫번째 구현체
public class KakaoSearchClient implements BlogSearchClient {
    private final String url;
    private final String key;
    private final ObjectMapper mapper;

    public KakaoSearchClient(@Value("${blog.kakao.url}") String url, @Value("${blog.kakao.key}") String key) {
        this.url = url;
        this.key = key;

        ObjectMapper customMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new JsonDeserializer<>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return LocalDateTime.parse(p.getValueAsString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            }
        });

        customMapper.registerModule(module);
        this.mapper = customMapper;
    }

    @Override
    public Page<Blog> search(String keyword, SortType sort, int page, int size) {
        KakaoResponse response = requestToKakao(keyword, sort, page, size);
        List<Blog> items = response.getDocuments().stream().map(KakaoDocument::toBlog).toList();
        long total = response.getMeta().getTotalCount();
        return new PageImpl<>(items, PageRequest.of(page, size), total);
    }

    private KakaoResponse requestToKakao(String keyword, SortType sort, int page, int size) {
        RestTemplate template = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("query", keyword)
                .queryParam("sort", sort.toKakaoSort())
                .queryParam("page", page)
                .queryParam("size", size);
        URI uri = builder.build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK %s".formatted(key));

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = template.exchange(uri, HttpMethod.GET, entity, String.class);
        HttpStatusCode status = response.getStatusCode();
        if (status.is5xxServerError()) {
            throw new ApiServerErrorException("Kakao");
        }
        String result = response.getBody();

        KakaoResponse kakaoResponse;
        try {
            kakaoResponse = mapper.readValue(result, KakaoResponse.class);
        } catch (JsonProcessingException e) {
            throw new ApiResponseSchemaErrorException("Kakao", e);
        }

        return kakaoResponse;
    }
}
