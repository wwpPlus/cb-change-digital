package com.cb.changedigital.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TemplateCreateRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String major;
    @NotBlank
    private String subMajor;
    @NotNull
    private Integer riskLevel;
    @NotNull
    private Integer operationLevel;
    @NotBlank
    private String templateType;
    @NotBlank
    private String structureType;
    private String contentJson;
}
