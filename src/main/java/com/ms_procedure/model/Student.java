package com.ms_procedure.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Transient;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    private Long id;
    private Long person_id;
    private Long career_id;
    private String institutional_email;
    private String pay_method;
    private String admission_date;
    private String guardian_name;
    private String home_phone;
    private String status;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String person_name;
    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String career_name;

}
