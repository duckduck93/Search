# Blog Search Application

## 기능

- Kakao Api, Naver Api 를 통한 블로그 데이터 조회
- 인기 검색어 목록 제공

## 빌드 결과물

Repository : https://github.com/duckduck93/Search  
Executable JAR : https://github.com/duckduck93/Search/releases/download/0.0.1/search-0.0.1-SNAPSHOT.jar

## 기술기반

- Java 17
- Spring Boot 3.0.4
- JPA
- Redis
- RabbitMQ

## 추가 고려사항

- 트래픽이 많고, 저장되어 있는 데이터가 많음을 염두에 둔 구현
    - Api Response 캐싱 Api 호출 횟수 감소
- 동시성 이슈가 발생할 수 있는 부분을 염두에 둔 구현
    - Message Queue 활용하여 키워드 증가 요청을 순차적으로 처리
- 카카오 블로그 검색 API에 장애가 발생한 경우, 네이버 블로그 검색 API를 통해 데이터 제공
    - BlogSearchClient interface 구현체 List를 받아 첫번째 구현체에서 오류 발생 시, 다음 구현체의 검색 기능 활용하도록 구성
  ```java
    public class BlogSearchClients {
        private final List<BlogSearchClient> clients;
        
        public search() {
            for (BlogSearchClients client: clients) {
                if (!client.checkHealth()) continue;
                client.search();
            }
        }
    }
  ```

## 외부 라이브러리 및 오픈소스

### Redis

Api Response 캐싱

```groovy
// Cache
implementation 'org.springframework.boot:spring-boot-starter-cache'
// Redis
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
```

### RabbitMQ

Message Queue 활용

```groovy
// RabbitMQ
implementation 'org.springframework.boot:spring-boot-starter-amqp'
```

### Spring Rest Doc

Api 명세 기능 활용

```groovy
// Spring Rest Docs
testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc'
asciidoctorExtensions 'org.springframework.restdocs:spring-restdocs-asciidoctor'
```

### Api 명세

### Blogs (블로그 데이터 조회)

#### Http Request

```bash
$ curl 'http://localhost:8080/blogs?query=naver-line&page=1&size=10&sort=ACCURACY' -i -X GET \
    -H 'Content-Type: application/json;charset=UTF-8' \
    -H 'Accept: application/json'
```

#### Http Response

- success

```json
{
  "total": 10,
  "size": 10,
  "page": 1,
  "items": [
    {
      "title": "title",
      "contents": "Content 1",
      "url": "https://naver.com",
      "blogName": "블로그명",
      "thumbnail": "https://naver.com",
      "createAt": "2023-03-22T22:09:44.101666"
    }
  ]
}
```

- error

```json
{
  "statusCode": "INTERNAL_SERVER_ERROR",
  "message": "500 모든 Api 서버가 오류 상태입니다.",
  "time": "2023-03-22T22:09:44.344713"
}
```

### Keywords (인기 검색어 조회)

#### Http Request

```bash
$ curl 'http://localhost:8080/keywords/popular' -i -X GET \
    -H 'Content-Type: application/json;charset=UTF-8' \
    -H 'Accept: application/json'
```

#### Http Response

- success

```json
[
  {
    "name": "검색어",
    "count": 99
  }
]
```
