package com.everis.bootcamp.ms.monedero.handler;

import com.everis.bootcamp.ms.monedero.domain.dto.MonederoAccountDto;
import com.everis.bootcamp.ms.monedero.domain.entity.MonederoAccount;
import com.everis.bootcamp.ms.monedero.service.srv.MonederoAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class MonederoAccountHandler {

    @Autowired
    private MonederoAccountService service;

    @Autowired
    private Validator validator;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), MonederoAccountDto.class);
    }

    public Mono<ServerResponse> findId(ServerRequest request) {
        String id = request.pathVariable("id");

        return service.findById(id)
                .flatMap(dto -> ServerResponse.status(HttpStatus.FOUND)
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(dto))
                .switchIfEmpty(
                        ErrorResponse.buildBadResponse("El monedero account con el id: ".concat(id).concat(" no se encontró."), HttpStatus.NOT_FOUND))
                .onErrorResume(throwable ->
                        ErrorResponse.buildBadResponse(throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public Mono<ServerResponse> create(ServerRequest request) {

        Mono<MonederoAccount> monedero = request.bodyToMono(MonederoAccount.class);

        return monedero.flatMap(m -> {
            Errors errors = new BeanPropertyBindingResult(m, MonederoAccount.class.getName());
            validator.validate(m, errors);

            if (errors.hasErrors()) {
                return Flux.fromIterable(errors.getFieldErrors())
                        .map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
                        .collectList()
                        .flatMap(list -> ServerResponse.badRequest().body(fromValue(list)));
            } else {
                if (m.getCreateAt() == null) {
                    m.setCreateAt(LocalDateTime.now());
                }
                return service.save(m).flatMap(mdb -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(mdb)));
            }

        });

    }

}
