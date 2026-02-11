package com.cb.change.controller;

import com.cb.change.common.ApiResponse;
import com.cb.change.dto.PlanAuditResultDTO;
import com.cb.change.entity.Plan;
import com.cb.change.enums.PlanStatus;
import com.cb.change.service.PlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "变更方案")
@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @ApiOperation("创建方案草稿")
    @PostMapping
    public ApiResponse<Plan> create(@RequestBody Plan plan) {
        planService.fillTimePoints(plan);
        plan.setStatus(PlanStatus.DRAFT);
        plan.setCreatedAt(LocalDateTime.now());
        plan.setUpdatedAt(LocalDateTime.now());
        planService.save(plan);
        return ApiResponse.ok(plan);
    }

    @ApiOperation("更新方案")
    @PutMapping("/{id}")
    public ApiResponse<Plan> update(@PathVariable Long id, @RequestBody Plan plan) {
        planService.fillTimePoints(plan);
        plan.setId(id);
        plan.setUpdatedAt(LocalDateTime.now());
        planService.updateById(plan);
        return ApiResponse.ok(planService.getById(id));
    }

    @ApiOperation("方案列表")
    @GetMapping
    public ApiResponse<List<Plan>> list() {
        return ApiResponse.ok(planService.list());
    }

    @ApiOperation("发起申请并稽核")
    @PostMapping("/{id}/submit")
    public ApiResponse<PlanAuditResultDTO> submit(@PathVariable Long id,
                                                   @RequestParam(defaultValue = "system") String operator) {
        PlanAuditResultDTO result = planService.submitForAudit(id, operator);
        if (!result.isPassed()) {
            return ApiResponse.fail("规则稽核未通过", result);
        }
        return ApiResponse.ok(result);
    }
}
