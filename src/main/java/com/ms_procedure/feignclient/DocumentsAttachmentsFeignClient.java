package com.ms_procedure.feignclient;

import org.springframework.stereotype.Component;
import com.ms_procedure.model.DocumentsAttachments;
import org.springframework.web.bind.annotation.*;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.*;

@Component
@ReactiveFeignClient(value = "ms-documents-attachments", url = "${client.ms-documents-attachments.url}", configuration = FeignConfig.class)
public interface DocumentsAttachmentsFeignClient {

    @GetMapping
    Flux<DocumentsAttachments> findAllDocumentsAttachments();

    @GetMapping("/procedure_id/{procedure_id}")
    Flux<DocumentsAttachments> findDocumentsAttachmentsByProcedureId(@PathVariable("procedure_id") Integer procedure_id);

}
