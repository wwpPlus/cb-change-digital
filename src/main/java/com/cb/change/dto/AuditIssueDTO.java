package com.cb.change.dto;

import com.cb.change.enums.AuditSeverity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditIssueDTO {

    private String type;
    private AuditSeverity severity;
    private String field;
    private String message;
    private String suggestion;
}
