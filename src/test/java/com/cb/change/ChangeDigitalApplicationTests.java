package com.cb.change;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.cb.change.dto.PlanAuditResultDTO;
import com.cb.change.entity.Plan;
import com.cb.change.service.PlanService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChangeDigitalApplicationTests {

    @Autowired
    private PlanService planService;

    @Test
    void shouldAutoFillTimePoints() {
        Plan plan = new Plan();
        plan.setCutoverStart(LocalDateTime.now().plusDays(1));
        plan.setCutoverEnd(LocalDateTime.now().plusDays(1).plusHours(2));
        planService.fillTimePoints(plan);
        assertNotNull(plan.getPrecheckTime());
        assertNotNull(plan.getTestTime());
        assertNotNull(plan.getRollbackTime());
    }

    @Test
    void shouldRejectInvalidPlanWhenSubmit() {
        Plan plan = new Plan();
        plan.setName("bad");
        plan.setCreatedAt(LocalDateTime.now());
        plan.setUpdatedAt(LocalDateTime.now());
        planService.save(plan);

        PlanAuditResultDTO result = planService.submitForAudit(plan.getId(), "tester");
        assertFalse(result.isPassed());
        assertFalse(result.getRuleIssues().isEmpty());
    }
}
