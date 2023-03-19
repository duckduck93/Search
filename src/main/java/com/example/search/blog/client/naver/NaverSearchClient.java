package com.example.search.blog.client.naver;

import com.example.search.blog.Blog;
import com.example.search.blog.client.BlogSearchClient;
import com.example.search.blog.client.naver.model.NaverItem;
import com.example.search.blog.client.naver.model.NaverResponse;
import com.example.search.blog.exchange.BlogSearchRequest;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component("NaverSearchClient")
@NoArgsConstructor
@AllArgsConstructor
public class NaverSearchClient implements BlogSearchClient {
    @Value("${blog.naver.url}")
    private String url;
    @Value("${blog.naver.id}")
    private String id;
    @Value("${blog.naver.secret}")
    private String secret;

    @Override
    public Page<Blog> search(BlogSearchRequest request) {
        RestTemplate template = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.url)
                .queryParam("query", request.getQuery())
                .queryParam("display", request.getSize())
                .queryParam("start", request.getPage())
                .queryParam("sort", request.getSortType().toNaverSort());
        URI uri = builder.build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", this.id);
        headers.add("X-Naver-Client-Secret", this.secret);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = template.exchange(uri, HttpMethod.GET, entity, String.class);
        String result = response.getBody();

        NaverResponse naverResponse;
        ObjectMapper mapper = localDateObjectMapper();
        try {
            naverResponse = mapper.readValue(result, NaverResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return new PageImpl<>(
                naverResponse.getItems().stream().map(NaverItem::toBlog).toList(),
                request,
                naverResponse.getTotal()
        );
    }

    private ObjectMapper localDateObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDate.class, new JsonDeserializer<>() {
            @Override
            public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return LocalDate.parse(p.getValueAsString(), DateTimeFormatter.ofPattern("yyyyMMdd"));
            }
        });

        mapper.registerModule(module);
        return mapper;
    }
}
