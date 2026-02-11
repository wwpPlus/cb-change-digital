package com.cb.change.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlanAuditResultDTO {

    private boolean passed;
    private List<AuditIssueDTO> ruleIssues;
    private List<AuditIssueDTO> aiIssues;
}
