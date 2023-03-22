package com.example.search.core.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean(name = "RedisCacheManager")
    public CacheManager redisCacheManager(RedisConnectionFactory factory, @Value("${spring.redis.ttl-second}") String ttl) {
        RedisSerializationContext.SerializationPair<String> keySerializer = RedisSerializationContext.SerializationPair
                .fromSerializer(new StringRedisSerializer());
        RedisSerializationContext.SerializationPair<Object> valueSerializer = RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer(localDateTimeObjectMapper()));

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(keySerializer)
                .serializeValuesWith(valueSerializer)
                .entryTtl(Duration.ofSeconds(Long.parseLong(ttl)));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(factory)
                .cacheDefaults(configuration)
                .build();
    }

    private ObjectMapper localDateTimeObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
        return mapper;

    }
}
