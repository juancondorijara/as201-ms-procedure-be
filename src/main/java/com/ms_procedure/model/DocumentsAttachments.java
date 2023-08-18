package com.ms_procedure.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentsAttachments {

    private Long id;
    private Long procedure_id ;
    private Long document_type_id;
    private Long phase_id;
    private String attached;
    private String extension;
    private String state;

}
