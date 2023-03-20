package com.example.search.blog.client.naver;

import com.example.search.blog.Blog;
import com.example.search.blog.client.ApiResponseSchemaErrorException;
import com.example.search.blog.client.ApiServerErrorException;
import com.example.search.blog.client.BlogSearchClient;
import com.example.search.blog.client.naver.model.NaverItem;
import com.example.search.blog.client.naver.model.NaverResponse;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Order(2) // BlogSearchClient 중 두번째 구현체
public class NaverSearchClient implements BlogSearchClient {
    private final String url;
    private final String id;
    private final String secret;
    private final ObjectMapper mapper;

    public NaverSearchClient(@Value("${blog.naver.url}") String url, @Value("${blog.naver.id}") String id, @Value("${blog.naver.secret}") String secret) {
        this.url = url;
        this.id = id;
        this.secret = secret;

        ObjectMapper customMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDate.class, new JsonDeserializer<>() {
            @Override
            public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return LocalDate.parse(p.getValueAsString(), DateTimeFormatter.ofPattern("yyyyMMdd"));
            }
        });

        customMapper.registerModule(module);
        this.mapper = customMapper;
    }

    @Override
    public Page<Blog> search(String keyword, SortType sort, int page, int size) {
        NaverResponse response = requestToNaver(keyword, sort, page, size);
        List<Blog> items = response.getItems().stream().map(NaverItem::toBlog).toList();
        long total = response.getTotal();
        return new PageImpl<>(items, PageRequest.of(page, size), total);
    }

    private NaverResponse requestToNaver(String keyword, SortType sort, int page, int size) {
        RestTemplate template = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("query", keyword)
                .queryParam("sort", sort.toNaverSort())
                .queryParam("start", page)
                .queryParam("display", size);
        URI uri = builder.build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", id);
        headers.add("X-Naver-Client-Secret", secret);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = template.exchange(uri, HttpMethod.GET, entity, String.class);
        HttpStatusCode status = response.getStatusCode();
        if (status.is5xxServerError()) {
            throw new ApiServerErrorException("Naver");
        }
        String result = response.getBody();

        NaverResponse naverResponse;
        try {
            naverResponse = mapper.readValue(result, NaverResponse.class);
        } catch (JsonProcessingException e) {
            throw new ApiResponseSchemaErrorException("Naver", e);
        }
        return naverResponse;
    }
}
