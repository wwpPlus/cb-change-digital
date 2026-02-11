package com.cb.changedigital.dto;

import lombok.Data;

@Data
public class TemplateQueryRequest {
    private String major;
    private String subMajor;
    private Integer riskLevel;
    private Integer operationLevel;
    private String templateType;
    private String keyword;
}
