package com.cb.change.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cb.change.dto.PlanAuditResultDTO;
import com.cb.change.entity.Plan;

public interface PlanService extends IService<Plan> {

    Plan fillTimePoints(Plan plan);

    PlanAuditResultDTO submitForAudit(Long planId, String operator);
}
