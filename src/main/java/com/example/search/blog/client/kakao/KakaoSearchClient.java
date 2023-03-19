package com.example.search.blog.client.kakao;

import com.example.search.blog.Blog;
import com.example.search.blog.client.BlogSearchClient;
import com.example.search.blog.client.kakao.model.KakaoDocument;
import com.example.search.blog.client.kakao.model.KakaoResponse;
import com.example.search.blog.exchange.BlogSearchRequest;
import com.example.search.errors.blogs.KakaoApiServerErrorException;
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
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component("KakaoSearchClient")
@NoArgsConstructor
@AllArgsConstructor
public class KakaoSearchClient implements BlogSearchClient {
    @Value("${blog.kakao.url}")
    private String url;
    @Value("${blog.kakao.key}")
    private String key;

    @Override
    public Page<Blog> search(BlogSearchRequest request) {
        KakaoResponse response = requestToKakao(request);
        return new PageImpl<>(
                response.getDocuments().stream().map(KakaoDocument::toBlog).toList(),
                request,
                response.getMeta().getTotalCount()
        );
    }

    private KakaoResponse requestToKakao(BlogSearchRequest request) {
        RestTemplate template = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.url)
                .queryParam("query", request.getQuery())
                .queryParam("sort", request.getSortType().toKakaoSort())
                .queryParam("page", request.getPage())
                .queryParam("size", request.getSize());
        URI uri = builder.build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK %s".formatted(this.key));

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = template.exchange(uri, HttpMethod.GET, entity, String.class);
        HttpStatusCode status = response.getStatusCode();
        if (status.is5xxServerError()) {
            throw new KakaoApiServerErrorException();
        }
        String result = response.getBody();

        KakaoResponse kakaoResponse;
        ObjectMapper mapper = localDateTimeObjectMapper();
        try {
            kakaoResponse = mapper.readValue(result, KakaoResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return kakaoResponse;
    }

    private ObjectMapper localDateTimeObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new JsonDeserializer<>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return LocalDateTime.parse(p.getValueAsString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            }
        });

        mapper.registerModule(module);
        return mapper;
    }
}
