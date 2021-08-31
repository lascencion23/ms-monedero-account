package com.everis.bootcamp.ms.monedero.consumer;

import com.everis.bootcamp.ms.monedero.domain.dto.MonederoTransactionDto;
import com.everis.bootcamp.ms.monedero.producer.MonederoProducer;
import com.everis.bootcamp.ms.monedero.service.srv.MonederoAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MonederoConsumer {

    @Autowired
    private MonederoAccountService service;

    @Autowired
    private MonederoProducer producer;

    @KafkaListener(
            topics = "${custom.kafka.topic-name}",
            groupId = "${custom.kafka.group-id}",
            containerFactory = "monederoKafkaListenerContainerFactory")
    public void consumer(MonederoTransactionDto monederoTransactionDto) {

        log.info("Mensaje consumido [{}]", monederoTransactionDto);
        this.validarBalance(monederoTransactionDto);
    }

    private void validarBalance(MonederoTransactionDto monederoTransactionDto) {

        service.findByNroPhone(monederoTransactionDto.getOriginNumber())
                .flatMap(m -> {

                    if (m.getBalance() >= monederoTransactionDto.getAmount()) {

                        Double nuevoBalance = m.getBalance() - monederoTransactionDto.getAmount();
                        m.setBalance(nuevoBalance);
                        service.getMonedero(m).flatMap(mObj -> service.save(mObj))
                                .flatMap(d -> service.findByNroPhone(monederoTransactionDto.getDestinationNumber()))
                                .flatMap(md -> service.getMonedero(md).flatMap(mde -> {
                                    mde.setBalance(mde.getBalance() + monederoTransactionDto.getAmount());
                                    return service.save(mde);
                                })).subscribe(c -> log.info("ACTUALIZADO TODO"));

                        monederoTransactionDto.setStatus(MonederoTransactionDto.Status.SUCCESSFUL);

                    } else {

                        //REVISAR SI TIENE DEBITO ASOCIADA


                        // SI NO TIENE -> RECHAZAR PETICION
                        monederoTransactionDto.setStatus(MonederoTransactionDto.Status.REJECTED);
                    }
                    this.producer.producer(monederoTransactionDto);
                    return service.getMonedero(m);
                }).subscribe();
    }
}
