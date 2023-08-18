package com.ms_procedure.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "procedure")
public class Procedure {

    @Id private Integer id;
    @Column(value = "procedure_config_id") private Integer procedureConfigId;
    @Column(value = "phase_id") private Integer phaseId;
    @Column(value = "person_id") private Integer personId;
    @Column(value = "student_id") private Integer studentId;
    @Column private Integer batch;
    @Column private Integer note;
    @Column(value = "collaborator_type_id") private Integer collaboratorTypeId;

    @Column(value = "createddate") private String createdDate;
    @Column(value = "modifieddate") private String modifiedDate;

    @Column private boolean active;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String attached1;
    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String attached2;
    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String attached3;
    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String attached4;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String institutionalEmail;
    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String personName;

    private String message;

}