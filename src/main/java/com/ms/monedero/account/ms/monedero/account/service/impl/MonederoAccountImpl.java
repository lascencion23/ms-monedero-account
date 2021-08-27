package com.ms.monedero.account.ms.monedero.account.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.monedero.account.ms.monedero.account.domain.dto.MonederoAccountDto;
import com.ms.monedero.account.ms.monedero.account.domain.entity.MonederoAccount;
import com.ms.monedero.account.ms.monedero.account.domain.repository.MonederoAccountRepository;
import com.ms.monedero.account.ms.monedero.account.service.srv.MonederoAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MonederoAccountImpl implements MonederoAccountService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MonederoAccountRepository repository;

    @Override
    public Flux<MonederoAccountDto> findAll() {
        return repository.findAll().flatMap(this::getMonederoDto);
    }

    @Override
    public Mono<MonederoAccountDto> findById(String id) {
        return repository.findById(id).flatMap(this::getMonederoDto);
    }

    @Override
    public Mono<MonederoAccountDto> save(MonederoAccount monederoAccount) {
        return repository.save(monederoAccount).flatMap(this::getMonederoDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }

    private Mono<MonederoAccountDto> getMonederoDto(MonederoAccount monederoAccount) {
        return Mono.just(objectMapper.convertValue(monederoAccount, MonederoAccountDto.class));
    }
}
