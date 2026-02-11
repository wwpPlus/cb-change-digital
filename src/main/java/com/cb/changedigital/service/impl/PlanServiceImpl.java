package com.cb.changedigital.service.impl;

import com.cb.changedigital.dto.QuickEditRequest;
import com.cb.changedigital.entity.Plan;
import com.cb.changedigital.entity.PlanDevice;
import com.cb.changedigital.entity.PlanPerson;
import com.cb.changedigital.entity.Template;
import com.cb.changedigital.entity.TemplateVersion;
import com.cb.changedigital.mapper.PlanDeviceMapper;
import com.cb.changedigital.mapper.PlanMapper;
import com.cb.changedigital.mapper.PlanPersonMapper;
import com.cb.changedigital.mapper.TemplateMapper;
import com.cb.changedigital.mapper.TemplateVersionMapper;
import com.cb.changedigital.service.PlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PlanServiceImpl implements PlanService {

    private final PlanMapper planMapper;
    private final PlanPersonMapper planPersonMapper;
    private final PlanDeviceMapper planDeviceMapper;
    private final TemplateVersionMapper templateVersionMapper;
    private final TemplateMapper templateMapper;

    public PlanServiceImpl(PlanMapper planMapper,
                           PlanPersonMapper planPersonMapper,
                           PlanDeviceMapper planDeviceMapper,
                           TemplateVersionMapper templateVersionMapper,
                           TemplateMapper templateMapper) {
        this.planMapper = planMapper;
        this.planPersonMapper = planPersonMapper;
        this.planDeviceMapper = planDeviceMapper;
        this.templateVersionMapper = templateVersionMapper;
        this.templateMapper = templateMapper;
    }

    @Override
    @Transactional
    public Plan saveDraft(QuickEditRequest request, Long ownerId) {
        Plan plan = new Plan();
        plan.setName(request.getName());
        plan.setMajor(request.getMajor());
        plan.setSubMajor(request.getSubMajor());
        plan.setOperationContent(request.getOperationContent());
        plan.setOperationTarget(request.getOperationTarget());
        plan.setNetworkScope(request.getNetworkScope());
        plan.setRiskLevel(request.getRiskLevel());
        plan.setOperationLevel(request.getOperationLevel());
        plan.setBusinessInterrupted(request.getBusinessInterrupted());
        plan.setBusinessImpact(request.getBusinessImpact());
        plan.setStatus("DRAFT");
        plan.setTemplateVersionId(request.getTemplateVersionId());
        plan.setOwnerId(ownerId);
        plan.setCutoverStart(request.getCutoverStart());
        plan.setCutoverEnd(request.getCutoverEnd());
        plan.setPrecheckTime(request.getPrecheckTime() == null ? request.getCutoverStart().minusHours(24) : request.getPrecheckTime());
        plan.setVerifyTime(request.getVerifyTime() == null ? request.getCutoverEnd().plusHours(2) : request.getVerifyTime());
        plan.setRollbackTime(request.getRollbackTime() == null ? request.getCutoverEnd().plusHours(3) : request.getRollbackTime());
        plan.setUpdatedAt(LocalDateTime.now());
        planMapper.insert(plan);

        if (request.getPersons() != null) {
            for (QuickEditRequest.PersonDTO personDTO : request.getPersons()) {
                PlanPerson person = new PlanPerson();
                person.setPlanId(plan.getId());
                person.setRole(personDTO.getRole());
                person.setDepartment(personDTO.getDepartment());
                person.setName(personDTO.getName());
                person.setPhone(personDTO.getPhone());
                planPersonMapper.insert(person);
            }
        }
        if (request.getDevices() != null) {
            for (QuickEditRequest.DeviceDTO deviceDTO : request.getDevices()) {
                PlanDevice device = new PlanDevice();
                device.setPlanId(plan.getId());
                device.setDeviceName(deviceDTO.getDeviceName());
                device.setIp(deviceDTO.getIp());
                planDeviceMapper.insert(device);
            }
        }
        return plan;
    }

    @Override
    public Plan submit(Long planId) {
        Plan plan = planMapper.selectById(planId);
        plan.setStatus("SUBMITTED");
        plan.setUpdatedAt(LocalDateTime.now());
        planMapper.updateById(plan);
        return plan;
    }

    @Override
    public Plan generateTemplate(Long planId, String templateType, Long ownerId) {
        Plan plan = planMapper.selectById(planId);
        Template template = new Template();
        template.setName(plan.getName() + "-生成模板");
        template.setMajor(plan.getMajor());
        template.setSubMajor(plan.getSubMajor());
        template.setRiskLevel(plan.getRiskLevel());
        template.setOperationLevel(plan.getOperationLevel());
        template.setTemplateType(templateType);
        template.setStructureType("L4");
        template.setContentJson("{\"sourcePlanId\":" + planId + "}");
        template.setLatestVersion(1);
        template.setOwnerId(ownerId);
        template.setUpdatedAt(LocalDateTime.now());
        templateMapper.insert(template);

        TemplateVersion version = new TemplateVersion();
        version.setTemplateId(template.getId());
        version.setVersionNo(1);
        version.setContentJson(template.getContentJson());
        version.setReleaseNote("由方案生成");
        version.setCreatedAt(LocalDateTime.now());
        templateVersionMapper.insert(version);

        plan.setTemplateVersionId(version.getId());
        planMapper.updateById(plan);
        return plan;
    }
}
