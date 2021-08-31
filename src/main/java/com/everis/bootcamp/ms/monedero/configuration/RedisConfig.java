package com.everis.bootcamp.ms.monedero.configuration;

import com.everis.bootcamp.ms.monedero.domain.dto.MaestrasDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, MaestrasDto> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {

        Jackson2JsonRedisSerializer<MaestrasDto> serializer = new Jackson2JsonRedisSerializer<>(MaestrasDto.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, MaestrasDto> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, MaestrasDto> context = builder.value(serializer)
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }


}
