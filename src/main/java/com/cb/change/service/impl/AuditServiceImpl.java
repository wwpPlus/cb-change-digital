package com.cb.change.service.impl;

import com.cb.change.dto.AuditIssueDTO;
import com.cb.change.dto.PlanAuditResultDTO;
import com.cb.change.entity.AuditRecord;
import com.cb.change.entity.Plan;
import com.cb.change.enums.PlanStatus;
import com.cb.change.mapper.AuditRecordMapper;
import com.cb.change.mapper.PlanMapper;
import com.cb.change.rule.AiAuditEngine;
import com.cb.change.rule.RuleAuditEngine;
import com.cb.change.service.AuditService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final RuleAuditEngine ruleAuditEngine;
    private final AiAuditEngine aiAuditEngine;
    private final AuditRecordMapper auditRecordMapper;
    private final PlanMapper planMapper;
    private final ObjectMapper objectMapper;

    @Override
    public PlanAuditResultDTO audit(Plan plan, String operator) {
        List<AuditIssueDTO> ruleIssues = ruleAuditEngine.verify(plan);
        List<AuditIssueDTO> aiIssues = aiAuditEngine.analyze(plan);
        boolean passed = ruleIssues.isEmpty();

        plan.setStatus(passed ? PlanStatus.APPROVED : PlanStatus.REJECTED);
        plan.setUpdatedAt(LocalDateTime.now());
        planMapper.updateById(plan);

        AuditRecord record = new AuditRecord();
        record.setPlanId(plan.getId());
        record.setPassed(passed);
        record.setOperator(operator);
        record.setRuleIssuesJson(toJson(ruleIssues));
        record.setAiIssuesJson(toJson(aiIssues));
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());
        auditRecordMapper.insert(record);
        return new PlanAuditResultDTO(passed, ruleIssues, aiIssues);
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }
}
