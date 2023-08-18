package com.ms_procedure.controller;

import com.ms_procedure.feignclient.*;
import com.ms_procedure.model.*;
import com.ms_procedure.model.DocumentsAttachments;
import com.ms_procedure.service.ProcedureService;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/procedure")
public class ProcedureController {

    @Autowired
    private ProcedureService procedureService;

    @GetMapping
    public Flux<Procedure> findAll(){ return procedureService.findAll(); }

    @GetMapping("/id/{id}")
    public Mono<Procedure> findById(@PathVariable Integer id){ return procedureService.findById(id); }

    @GetMapping("/null")
    public Flux<Procedure> findByBatchNull(){ return procedureService.findByBatchNull(); }

    @GetMapping("/notnull")
    public Flux<Procedure> findByBatchNotNull(){ return procedureService.findByBatchNotNull(); }

    @GetMapping("/phase_id/{phase_id}")
    public Flux<Procedure> findByPhaseId(@PathVariable Integer phase_id){ return procedureService.findByPhaseId(phase_id); }

    @PostMapping
    public Mono<Procedure> save(@RequestBody Procedure procedure){
        return procedureService.save(procedure);
    }

    @PutMapping
    public Mono<Procedure> update(@RequestBody Procedure procedure){
        return procedureService.update(procedure);
    }

    @PostMapping("/consolidateuno/{id}")
    public Mono<ResponseEntity<Procedure>> consolidate(@PathVariable Integer id){ return procedureService.consolidateEnUno(id); }

    @PostMapping("/consolidate4/{id1}-{id2}-{id3}")
    public void consolidate4(@PathVariable Integer id1, @PathVariable Integer id2, @PathVariable Integer id3) {
        procedureService.consolidatePhaseId4(id1, id2, id3); }

    @PostMapping("/consolidate5/{id1}-{id2}-{id3}-{id4}-{id5}")
    public void consolidate5(@PathVariable Integer id1, @PathVariable Integer id2, @PathVariable Integer id3, @PathVariable Integer id4, @PathVariable Integer id5) {
        procedureService.consolidatePhaseId5(id1, id2, id3, id4, id5); }

    @PostMapping("/consolidate6/{id1}-{id2}-{id3}-{id4}-{id5}")
    public void consolidate6(@PathVariable Integer id1, @PathVariable Integer id2, @PathVariable Integer id3, @PathVariable Integer id4, @PathVariable Integer id5) {
        procedureService.consolidatePhaseId6(id1, id2, id3, id4, id5); }

    @Autowired
    private DocumentsAttachmentsFeignClient documentsAttachmentsFeignClient;

    @GetMapping("/documents_attachments/{procedure_id}")
    public Flux<Object> findDocumentsAttachmentsById(@PathVariable Integer procedure_id) {
        Flux<Procedure> procedureFlux = procedureService.findDocumentsAttachmentsByProcedureId(procedure_id);
        Flux<DocumentsAttachments> documentsAttachmentsFlux = documentsAttachmentsFeignClient.findDocumentsAttachmentsByProcedureId(procedure_id);
        return Flux.from(Flux.concat(Flux.from(procedureFlux),Flux.from(documentsAttachmentsFlux)));
    }

    // SEGURIDAD
//    @PostMapping("/notification/{institutional_email}")
//    public void notificationByEmail(@PathVariable String institutional_email) throws Exception {
//        procedureService.notificationByEmail(institutional_email); }

    @PostMapping("/notification/{institutional_email}")
    public ResponseEntity notificationByEmail(@PathVariable String institutional_email) throws Exception {
        procedureService.notificationByEmail(institutional_email);
        return ResponseEntity.ok("SUCCESS " + "Notificación Enviada"); }

    //CAMBIO SPRING JAVA MAIL
    @PostMapping("/springnotification/{institutional_email}")
    public ResponseEntity springNotificationByEmail(@PathVariable String institutional_email) {
        procedureService.springNotificationByEmail(institutional_email);
        return ResponseEntity.ok("SUCCESS " + "Notificación Enviada"); }

    @PostMapping("/notification2")
    public void notificationByEmail2(@RequestBody Procedure procedure) throws Exception {
        procedureService.notificationByEmail2(procedure); }

    @PostMapping("/notification3/{institutional_email}")
    public void notificationByEmail3(@PathVariable String institutional_email, @RequestBody String message) throws Exception {
        procedureService.notificationByEmail3(institutional_email, message); }

}
