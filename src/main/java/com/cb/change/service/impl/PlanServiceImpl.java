package com.cb.change.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cb.change.dto.PlanAuditResultDTO;
import com.cb.change.entity.Plan;
import com.cb.change.enums.PlanStatus;
import com.cb.change.mapper.PlanMapper;
import com.cb.change.service.AuditService;
import com.cb.change.service.PlanService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements PlanService {

    private final PlanMapper planMapper;
    private final AuditService auditService;

    @Override
    public Plan fillTimePoints(Plan plan) {
        if (plan.getCutoverStart() == null || plan.getCutoverEnd() == null) {
            return plan;
        }
        plan.setPrecheckTime(plan.getCutoverStart().minusHours(24));
        plan.setTestTime(plan.getCutoverEnd().plusHours(2));
        plan.setRollbackTime(plan.getCutoverEnd().plusHours(3));
        return plan;
    }

    @Override
    public PlanAuditResultDTO submitForAudit(Long planId, String operator) {
        Plan plan = planMapper.selectById(planId);
        if (plan == null) {
            throw new IllegalArgumentException("方案不存在");
        }
        fillTimePoints(plan);
        plan.setStatus(PlanStatus.AUDITING);
        plan.setUpdatedAt(LocalDateTime.now());
        planMapper.updateById(plan);
        return auditService.audit(plan, operator);
    }
}
