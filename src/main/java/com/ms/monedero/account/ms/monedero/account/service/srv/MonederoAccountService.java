package com.ms.monedero.account.ms.monedero.account.service.srv;

import com.ms.monedero.account.ms.monedero.account.domain.dto.MonederoAccountDto;
import com.ms.monedero.account.ms.monedero.account.domain.entity.MonederoAccount;
import com.ms.monedero.account.ms.monedero.account.service.generic.GenericService;
import reactor.core.publisher.Mono;

public interface MonederoAccountService extends GenericService<MonederoAccountDto> {

    Mono<MonederoAccountDto> save(MonederoAccount monederoAccount);

}
