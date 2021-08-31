package com.everis.bootcamp.ms.monedero.domain.repository;

import com.everis.bootcamp.ms.monedero.domain.entity.MonederoAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MonederoAccountRepository extends ReactiveMongoRepository<MonederoAccount, String> {

    Mono<MonederoAccount> findByNroPhone(int nroPhone);

}
