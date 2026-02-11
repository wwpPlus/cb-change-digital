package com.cb.change.rule;

import com.cb.change.dto.AuditIssueDTO;
import com.cb.change.entity.Plan;
import com.cb.change.enums.AuditSeverity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class RuleAuditEngine {

    public List<AuditIssueDTO> verify(Plan plan) {
        List<AuditIssueDTO> issues = new ArrayList<>();
        checkRequired(plan, issues);
        checkTimeRules(plan, issues);
        checkConsistency(plan, issues);
        return issues;
    }

    private void checkRequired(Plan plan, List<AuditIssueDTO> issues) {
        addIfBlank(issues, plan.getProfession(), "profession", "专业不能为空");
        addIfBlank(issues, plan.getSubProfession(), "subProfession", "子专业不能为空");
        addIfBlank(issues, plan.getOperationContent(), "operationContent", "变更操作内容不能为空");
        addIfBlank(issues, plan.getOperationObject(), "operationObject", "操作对象不能为空");
        addIfBlank(issues, plan.getOperationScope(), "operationScope", "网络操作范围不能为空");
        if (plan.getRiskLevel() == null) {
            issues.add(issue("REQUIRED", "riskLevel", "风险等级不能为空", "请选择风险等级"));
        }
        if (plan.getOperationLevel() == null) {
            issues.add(issue("REQUIRED", "operationLevel", "变更操作等级不能为空", "请选择变更操作等级"));
        }
    }

    private void checkTimeRules(Plan plan, List<AuditIssueDTO> issues) {
        LocalDateTime start = plan.getCutoverStart();
        LocalDateTime end = plan.getCutoverEnd();
        if (start == null || end == null) {
            issues.add(issue("REQUIRED", "cutoverStart/cutoverEnd", "割接起止时间不能为空", "请填写割接开始和结束时间"));
            return;
        }
        if (plan.getPrecheckTime() != null && plan.getPrecheckTime().isAfter(start)) {
            issues.add(issue("TIME_ORDER", "precheckTime", "操作前校验完成时间必须早于割接开始", "调整预校验时间"));
        }
        if (plan.getTestTime() != null && plan.getTestTime().isBefore(end)) {
            issues.add(issue("TIME_ORDER", "testTime", "测试验证时间必须晚于割接结束", "调整测试验证时间"));
        }
        if (plan.getRollbackTime() != null && plan.getRollbackTime().isBefore(end)) {
            issues.add(issue("TIME_ORDER", "rollbackTime", "倒回时间必须晚于割接结束", "调整倒回时间"));
        }
    }

    private void checkConsistency(Plan plan, List<AuditIssueDTO> issues) {
        if (plan.getRiskLevel() != null && plan.getOperationLevel() != null
            && plan.getRiskLevel() < plan.getOperationLevel()) {
            issues.add(issue("CONSISTENCY", "riskLevel/operationLevel", "风险等级不能低于操作等级", "调整等级或填写定级依据"));
        }
        if (Boolean.TRUE.equals(plan.getBusinessInterrupted())) {
            if (!StringUtils.hasText(plan.getBusinessImpact())) {
                issues.add(issue("CONSISTENCY", "businessImpact", "中断业务时必须填写业务影响", "补充影响范围和时长"));
            }
            if (plan.getRiskLevel() != null && plan.getRiskLevel() < 3) {
                issues.add(issue("CONSISTENCY", "riskLevel", "中断业务时风险等级不能低于3", "提升风险等级或修正中断标记"));
            }
        }
    }

    private void addIfBlank(List<AuditIssueDTO> issues, String value, String field, String message) {
        if (!StringUtils.hasText(value)) {
            issues.add(issue("REQUIRED", field, message, "请完善字段"));
        }
    }

    private AuditIssueDTO issue(String type, String field, String message, String suggestion) {
        return new AuditIssueDTO(type, AuditSeverity.ERROR, field, message, suggestion);
    }
}
