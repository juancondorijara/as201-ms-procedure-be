package com.ms_procedure.service;

import com.ms_procedure.model.Procedure;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface ProcedureService {

    Flux<Procedure> findAll();

    Mono<Procedure> findById(Integer id);

    Flux<Procedure> findByBatchNull();

    Flux<Procedure> findByBatchNotNull();

    Flux<Procedure> findByPhaseId(Integer phaseId);

    Mono<Procedure> save(Procedure procedure);

    Mono<Procedure> update(Procedure procedure);

    Mono<ResponseEntity<Procedure>> consolidateEnUno(Integer id);

    void consolidatePhaseId4(Integer id1, Integer id2, Integer id3);

    void consolidatePhaseId5(Integer id1, Integer id2, Integer id3, Integer id4, Integer id5);

    void consolidatePhaseId6(Integer id1, Integer id2, Integer id3, Integer id4, Integer id5);

    Flux<Procedure> findDocumentsAttachmentsByProcedureId(Integer procedureId);

    void notificationByEmail(String institutionalEmail) throws Exception;

    //CAMBIO SPRING JAVA MAIL
    void springNotificationByEmail(String institutionalEmail);

    void notificationByEmail2(Procedure procedure) throws Exception;

    void notificationByEmail3(String institutionalEmail, String message) throws Exception;

}
