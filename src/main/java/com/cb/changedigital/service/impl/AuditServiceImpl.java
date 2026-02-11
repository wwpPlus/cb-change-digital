package com.cb.changedigital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cb.changedigital.dto.AuditIssue;
import com.cb.changedigital.dto.AuditResultDTO;
import com.cb.changedigital.entity.AuditRecord;
import com.cb.changedigital.entity.Plan;
import com.cb.changedigital.entity.PlanDevice;
import com.cb.changedigital.entity.PlanPerson;
import com.cb.changedigital.entity.WhiteListItem;
import com.cb.changedigital.mapper.AuditRecordMapper;
import com.cb.changedigital.mapper.PlanDeviceMapper;
import com.cb.changedigital.mapper.PlanMapper;
import com.cb.changedigital.mapper.PlanPersonMapper;
import com.cb.changedigital.mapper.WhiteListItemMapper;
import com.cb.changedigital.service.AuditService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class AuditServiceImpl implements AuditService {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^1\\d{10}$");
    private static final Pattern IPV4_PATTERN = Pattern.compile("^((25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\.){3}(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)$");

    private final PlanMapper planMapper;
    private final PlanPersonMapper planPersonMapper;
    private final PlanDeviceMapper planDeviceMapper;
    private final AuditRecordMapper auditRecordMapper;
    private final WhiteListItemMapper whiteListItemMapper;

    public AuditServiceImpl(PlanMapper planMapper,
                            PlanPersonMapper planPersonMapper,
                            PlanDeviceMapper planDeviceMapper,
                            AuditRecordMapper auditRecordMapper,
                            WhiteListItemMapper whiteListItemMapper) {
        this.planMapper = planMapper;
        this.planPersonMapper = planPersonMapper;
        this.planDeviceMapper = planDeviceMapper;
        this.auditRecordMapper = auditRecordMapper;
        this.whiteListItemMapper = whiteListItemMapper;
    }

    @Override
    public AuditResultDTO audit(Long planId) {
        Plan plan = planMapper.selectById(planId);
        List<PlanPerson> persons = planPersonMapper.selectList(new LambdaQueryWrapper<PlanPerson>().eq(PlanPerson::getPlanId, planId));
        List<PlanDevice> devices = planDeviceMapper.selectList(new LambdaQueryWrapper<PlanDevice>().eq(PlanDevice::getPlanId, planId));

        List<AuditIssue> ruleIssues = new ArrayList<AuditIssue>();
        List<AuditIssue> aiIssues = new ArrayList<AuditIssue>();

        checkRequired(plan, persons, devices, ruleIssues);
        checkConsistency(plan, ruleIssues);
        checkTimeSequence(plan, ruleIssues);
        checkWhitelist(plan, ruleIssues);
        generateAiSuggestion(plan, aiIssues);

        boolean pass = ruleIssues.isEmpty();
        String conclusion = pass ? "PASS" : "REJECT";

        AuditRecord record = new AuditRecord();
        record.setPlanId(planId);
        record.setVersionNo(nextVersion(planId));
        record.setRuleResultJson(ruleIssues.toString());
        record.setAiSuggestionJson(aiIssues.toString());
        record.setConclusion(conclusion);
        record.setCreatedAt(LocalDateTime.now());
        auditRecordMapper.insert(record);

        AuditResultDTO result = new AuditResultDTO();
        result.setPass(pass);
        result.setRuleIssues(ruleIssues);
        result.setAiIssues(aiIssues);
        result.setConclusion(conclusion);
        return result;
    }

    private void checkRequired(Plan plan, List<PlanPerson> persons, List<PlanDevice> devices, List<AuditIssue> issues) {
        if (blank(plan.getMajor()) || blank(plan.getSubMajor()) || blank(plan.getOperationContent()) || blank(plan.getOperationTarget()) || blank(plan.getNetworkScope())) {
            issues.add(new AuditIssue("REQUIRED", "HIGH", "基础信息", "补全专业/子专业/内容/对象/范围"));
        }
        if (plan.getRiskLevel() == null || plan.getOperationLevel() == null) {
            issues.add(new AuditIssue("REQUIRED", "HIGH", "等级", "补全风险等级和操作等级"));
        }
        if (plan.getCutoverStart() == null || plan.getCutoverEnd() == null || plan.getPrecheckTime() == null || plan.getVerifyTime() == null || plan.getRollbackTime() == null) {
            issues.add(new AuditIssue("REQUIRED", "HIGH", "时间", "补全割接、校验、验证、倒回时间"));
        }
        if (persons == null || persons.isEmpty()) {
            issues.add(new AuditIssue("REQUIRED", "HIGH", "人员", "至少填写关键角色人员"));
        } else {
            for (PlanPerson person : persons) {
                if (!PHONE_PATTERN.matcher(person.getPhone() == null ? "" : person.getPhone()).matches()) {
                    issues.add(new AuditIssue("FORMAT", "MEDIUM", "phone", "手机号格式应为11位手机号"));
                }
            }
        }
        if (devices == null || devices.isEmpty()) {
            issues.add(new AuditIssue("REQUIRED", "HIGH", "设备", "至少填写一条设备信息"));
        } else {
            for (PlanDevice device : devices) {
                if (!IPV4_PATTERN.matcher(device.getIp() == null ? "" : device.getIp()).matches()) {
                    issues.add(new AuditIssue("FORMAT", "MEDIUM", "ip", "设备IP格式不正确"));
                }
            }
        }
    }

    private void checkConsistency(Plan plan, List<AuditIssue> issues) {
        if (plan.getRiskLevel() != null && plan.getOperationLevel() != null && plan.getRiskLevel() < plan.getOperationLevel()) {
            issues.add(new AuditIssue("CONSISTENCY", "HIGH", "riskLevel", "风险等级不得低于操作等级"));
        }
        if (Boolean.TRUE.equals(plan.getBusinessInterrupted()) && blank(plan.getBusinessImpact())) {
            issues.add(new AuditIssue("CONSISTENCY", "HIGH", "businessImpact", "中断业务时必须填写业务影响"));
        }
    }

    private void checkTimeSequence(Plan plan, List<AuditIssue> issues) {
        if (plan.getPrecheckTime() != null && plan.getCutoverStart() != null && plan.getPrecheckTime().isAfter(plan.getCutoverStart())) {
            issues.add(new AuditIssue("TIME", "HIGH", "precheckTime", "操作前校验时间必须早于割接开始"));
        }
        if (plan.getVerifyTime() != null && plan.getCutoverEnd() != null && plan.getVerifyTime().isBefore(plan.getCutoverEnd())) {
            issues.add(new AuditIssue("TIME", "HIGH", "verifyTime", "测试验证时间必须不早于割接结束"));
        }
        if (plan.getRollbackTime() != null && plan.getCutoverEnd() != null && plan.getRollbackTime().isBefore(plan.getCutoverEnd())) {
            issues.add(new AuditIssue("TIME", "HIGH", "rollbackTime", "倒回时间必须不早于割接结束"));
        }
    }

    private void checkWhitelist(Plan plan, List<AuditIssue> issues) {
        if (plan.getRiskLevel() != null && plan.getRiskLevel() >= 5) {
            Long count = whiteListItemMapper.selectCount(new LambdaQueryWrapper<WhiteListItem>()
                    .eq(WhiteListItem::getOperationTarget, plan.getOperationTarget())
                    .eq(WhiteListItem::getOperationContent, plan.getOperationContent()));
            if (count == null || count == 0) {
                issues.add(new AuditIssue("WHITELIST", "HIGH", "whiteList", "风险5未命中白名单，需补充材料或升级审批"));
            }
        }
    }

    private void generateAiSuggestion(Plan plan, List<AuditIssue> aiIssues) {
        if (Boolean.FALSE.equals(plan.getBusinessInterrupted()) && !blank(plan.getBusinessImpact()) && plan.getBusinessImpact().contains("中断")) {
            aiIssues.add(new AuditIssue("AI_CONFLICT", "MEDIUM", "businessImpact", "存在文本冲突：不中断业务但影响描述提及中断"));
        }
        if (plan.getRiskLevel() != null && plan.getRiskLevel() >= 4 && blank(plan.getBusinessImpact())) {
            aiIssues.add(new AuditIssue("AI_SUGGESTION", "LOW", "businessImpact", "高风险方案建议补充业务影响与舆情应对口径"));
        }
    }

    private Integer nextVersion(Long planId) {
        AuditRecord latest = auditRecordMapper.selectOne(new LambdaQueryWrapper<AuditRecord>()
                .eq(AuditRecord::getPlanId, planId)
                .orderByDesc(AuditRecord::getVersionNo)
                .last("limit 1"));
        return latest == null ? 1 : latest.getVersionNo() + 1;
    }

    private boolean blank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
