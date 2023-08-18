package com.ms_procedure.feignclient;

import org.springframework.stereotype.Component;
import com.ms_procedure.model.Student;
import org.springframework.web.bind.annotation.*;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.*;

@Component
@ReactiveFeignClient(value = "ms-student", url = "${client.ms-student.url}", configuration = FeignConfig.class)
public interface StudentFeignClient {

    @GetMapping
    Flux<Student> findAllStudent();

    @GetMapping("/id/{student_id}")
    Mono<Student> findStudentById(@PathVariable("student_id") Integer student_id);

}
