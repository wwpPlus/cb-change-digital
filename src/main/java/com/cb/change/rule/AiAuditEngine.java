package com.cb.change.rule;

import com.cb.change.dto.AuditIssueDTO;
import com.cb.change.entity.Plan;
import com.cb.change.enums.AuditSeverity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AiAuditEngine {

    public List<AuditIssueDTO> analyze(Plan plan) {
        List<AuditIssueDTO> issues = new ArrayList<>();
        if (!StringUtils.hasText(plan.getAiSuggestion())) {
            issues.add(new AuditIssueDTO(
                "AI_SUGGESTION",
                AuditSeverity.WARN,
                "steps",
                "建议补充关键步骤：登录检查、配置备份、回滚触发点",
                "完善操作步骤并明确回滚条件"
            ));
        }
        if (plan.getRiskLevel() != null && plan.getRiskLevel() >= 4 && !StringUtils.hasText(plan.getBusinessImpact())) {
            issues.add(new AuditIssueDTO(
                "AI_RISK",
                AuditSeverity.WARN,
                "businessImpact",
                "高风险方案建议补充业务影响口径与舆情应对模板",
                "补充业务影响、口径文本与升级联系人"
            ));
        }
        return issues;
    }
}
