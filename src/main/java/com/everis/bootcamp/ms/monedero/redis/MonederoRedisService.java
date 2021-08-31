package com.everis.bootcamp.ms.monedero.redis;

import com.everis.bootcamp.ms.monedero.domain.dto.MaestrasDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class MonederoRedisService {

    public static final String KEY = "MAESTRAS";

    @Autowired
    private ReactiveRedisTemplate<String, MaestrasDto> redisTemplate;

    public Mono<Long> put(MaestrasDto maestro) {
        return redisTemplate.opsForList().rightPush(KEY, maestro);
    }

    public Flux<MaestrasDto> getAll() {
        return redisTemplate.opsForList().range(KEY,0,-1);
    }

}
