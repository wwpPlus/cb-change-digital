package com.cb.change.dto;

import com.cb.change.enums.TemplateType;
import lombok.Data;

@Data
public class TemplateQueryDTO {

    private String profession;
    private String subProfession;
    private Integer riskLevel;
    private Integer operationLevel;
    private TemplateType templateType;
    private String keyword;
}
