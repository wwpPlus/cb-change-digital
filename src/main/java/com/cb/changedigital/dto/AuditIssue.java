package com.cb.changedigital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuditIssue {
    private String type;
    private String severity;
    private String field;
    private String suggestion;
}
