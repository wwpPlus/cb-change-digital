package com.cb.changedigital;

import com.cb.changedigital.dto.QuickEditRequest;
import com.cb.changedigital.entity.Plan;
import com.cb.changedigital.service.AuditService;
import com.cb.changedigital.service.PlanService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Collections;

@SpringBootTest
class ChangeDigitalApplicationTests {

    @Autowired
    private PlanService planService;

    @Autowired
    private AuditService auditService;

    @Test
    void auditShouldPassForBasicValidPlan() {
        QuickEditRequest request = new QuickEditRequest();
        request.setName("核心网割接测试");
        request.setMajor("ip");
        request.setSubMajor("core");
        request.setOperationContent("BGP邻居重建");
        request.setOperationTarget("核心路由器A");
        request.setNetworkScope("省干核心层");
        request.setRiskLevel(5);
        request.setOperationLevel(4);
        request.setBusinessInterrupted(false);
        request.setCutoverStart(LocalDateTime.of(2026, 1, 1, 0, 0));
        request.setCutoverEnd(LocalDateTime.of(2026, 1, 1, 3, 0));

        QuickEditRequest.PersonDTO person = new QuickEditRequest.PersonDTO();
        person.setRole("操作人");
        person.setName("张三");
        person.setDepartment("网维中心");
        person.setPhone("13800138000");
        request.setPersons(Collections.singletonList(person));

        QuickEditRequest.DeviceDTO device = new QuickEditRequest.DeviceDTO();
        device.setDeviceName("核心路由器A");
        device.setIp("10.1.1.1");
        request.setDevices(Collections.singletonList(device));

        Plan plan = planService.saveDraft(request, 10001L);
        boolean pass = auditService.audit(plan.getId()).isPass();
        Assertions.assertTrue(pass);
    }
}
