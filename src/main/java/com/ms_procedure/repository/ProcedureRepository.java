package com.ms_procedure.repository;

import com.ms_procedure.model.Procedure;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProcedureRepository extends ReactiveCrudRepository<Procedure, Integer> {

    @Query("SELECT * FROM procedure WHERE phase_id = 4 ORDER BY id DESC")
    Flux<Procedure> findAll();

    @Query("SELECT * FROM procedure WHERE batch IS NULL ORDER BY id DESC")
    Flux<Procedure> findByBatchNull();

    @Query("SELECT * FROM procedure WHERE batch IS NOT NULL ORDER BY id DESC")
    Flux<Procedure> findByBatchNotNull();

    @Query("SELECT * FROM procedure WHERE phase_id = :phase_id ORDER BY id DESC")
    Flux<Procedure> findByPhaseId(@Param("phase_id") Integer phaseId);

    @Query("SELECT * FROM procedure WHERE id = :id ORDER BY id DESC")
    Flux<Procedure> findByProcedureId(@Param("id") Integer procedureId);

    //SIN USAR
    @Query("UPDATE procedure SET batch = :batch WHERE id = :id1 OR id = :id2")
    Flux<Procedure> consolidateByProcedureId(@Param("batch") Integer batch, @Param("id1") Long id1, @Param("id2") Long id2);

    @Query("SELECT * FROM procedure WHERE id = :id1 OR id = :id2")
    Flux<Procedure> consolidateByProcedureIdTest(@Param("id1") Long id1, @Param("id2") Long id2);

}