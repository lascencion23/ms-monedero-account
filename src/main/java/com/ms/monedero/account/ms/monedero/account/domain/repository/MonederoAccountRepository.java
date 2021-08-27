package com.ms.monedero.account.ms.monedero.account.domain.repository;

import com.ms.monedero.account.ms.monedero.account.domain.entity.MonederoAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MonederoAccountRepository extends ReactiveMongoRepository<MonederoAccount, String> {
}
