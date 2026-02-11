package com.cb.change.service;

import com.cb.change.dto.PlanAuditResultDTO;
import com.cb.change.entity.Plan;

public interface AuditService {

    PlanAuditResultDTO audit(Plan plan, String operator);
}
