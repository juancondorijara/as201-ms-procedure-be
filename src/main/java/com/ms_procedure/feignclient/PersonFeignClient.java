package com.ms_procedure.feignclient;

import org.springframework.stereotype.Component;
import com.ms_procedure.model.Person;
import org.springframework.web.bind.annotation.*;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.*;

@Component
@ReactiveFeignClient(value = "ms-person", url = "${client.ms-person.url}", configuration = FeignConfig.class)
public interface PersonFeignClient {

    @GetMapping
    Flux<Person> findAllPerson();

    @GetMapping("/id/{person_id}")
    Mono<Person> findPersonById(@PathVariable("person_id") Integer person_id);

}

