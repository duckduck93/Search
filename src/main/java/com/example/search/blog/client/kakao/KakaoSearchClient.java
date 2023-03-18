package com.example.search.blog.client.kakao;

import com.example.search.blog.Blog;
import com.example.search.blog.client.BlogSearchClient;
import com.example.search.blog.client.kakao.model.KakaoDocument;
import com.example.search.blog.client.kakao.model.KakaoResponse;
import com.example.search.util.SortType;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class KakaoSearchClient implements BlogSearchClient {
    private static final String KAKAO_URL = "https://dapi.kakao.com/v2/search/blog";

    @Override
    public Page<Blog> search(String keyword, SortType sort, Pageable pageable) {
        KakaoResponse response = requestToKakao(keyword, sort, pageable);
        return new PageImpl<>(
                response.getDocuments().stream().map(KakaoDocument::toBlog).toList(),
                pageable,
                response.getMeta().getTotalCount()
        );
    }

    public KakaoResponse requestToKakao(String keyword, SortType sort, Pageable pageable) {
        RestTemplate template = new RestTemplate();

        KakaoObjectMapper mapper = new KakaoObjectMapper();

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper);

        template.getMessageConverters().add(converter);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(KAKAO_URL)
                .queryParam("query", keyword)
                .queryParam("sort", sort.toKakaoSort())
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize());
        URI uri = builder.build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK ");

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = template.exchange(uri, HttpMethod.GET, entity, String.class);
        String result = response.getBody();

        KakaoResponse kakaoResponse;
        try {
            kakaoResponse = mapper.readValue(result, KakaoResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return kakaoResponse;
    }
}
