package com.cb.changedigital.controller;

import com.cb.changedigital.common.ApiResponse;
import com.cb.changedigital.dto.AuditResultDTO;
import com.cb.changedigital.dto.QuickEditRequest;
import com.cb.changedigital.entity.Plan;
import com.cb.changedigital.service.AuditService;
import com.cb.changedigital.service.PlanService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService planService;
    private final AuditService auditService;

    public PlanController(PlanService planService, AuditService auditService) {
        this.planService = planService;
        this.auditService = auditService;
    }

    @PostMapping("/draft")
    public ApiResponse<Plan> saveDraft(@RequestBody @Validated QuickEditRequest request) {
        return ApiResponse.ok(planService.saveDraft(request, 10001L));
    }

    @PostMapping("/{id}/submit")
    public ApiResponse<AuditResultDTO> submit(@PathVariable Long id) {
        planService.submit(id);
        return ApiResponse.ok(auditService.audit(id));
    }

    @PostMapping("/{id}/generate-template")
    public ApiResponse<Plan> generateTemplate(@PathVariable Long id,
                                              @RequestParam(defaultValue = "PERSONAL") String templateType) {
        return ApiResponse.ok(planService.generateTemplate(id, templateType, 10001L));
    }

    @PostMapping("/{id}/pre-audit")
    public ApiResponse<AuditResultDTO> preAudit(@PathVariable Long id) {
        return ApiResponse.ok(auditService.audit(id));
    }
}
