package com.cb.changedigital.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuditResultDTO {
    private boolean pass;
    private List<AuditIssue> ruleIssues;
    private List<AuditIssue> aiIssues;
    private String conclusion;
}
