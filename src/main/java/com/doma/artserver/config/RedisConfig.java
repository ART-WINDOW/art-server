package com.doma.artserver.config;

import com.doma.artserver.domain.exhibition.entity.Exhibition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Exhibition> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Exhibition> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key Serializer
        template.setKeySerializer(new StringRedisSerializer());

        // Value Serializer
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
