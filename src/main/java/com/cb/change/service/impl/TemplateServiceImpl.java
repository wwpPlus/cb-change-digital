package com.cb.change.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cb.change.dto.TemplateQueryDTO;
import com.cb.change.entity.Plan;
import com.cb.change.entity.Template;
import com.cb.change.enums.TemplateType;
import com.cb.change.mapper.PlanMapper;
import com.cb.change.mapper.TemplateMapper;
import com.cb.change.service.TemplateService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements TemplateService {

    private final TemplateMapper templateMapper;
    private final PlanMapper planMapper;

    @Override
    public List<Template> queryTemplates(TemplateQueryDTO queryDTO) {
        LambdaQueryWrapper<Template> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(queryDTO.getProfession()), Template::getProfession, queryDTO.getProfession())
            .eq(StringUtils.hasText(queryDTO.getSubProfession()), Template::getSubProfession, queryDTO.getSubProfession())
            .eq(queryDTO.getRiskLevel() != null, Template::getRiskLevel, queryDTO.getRiskLevel())
            .eq(queryDTO.getOperationLevel() != null, Template::getOperationLevel, queryDTO.getOperationLevel())
            .eq(queryDTO.getTemplateType() != null, Template::getTemplateType, queryDTO.getTemplateType())
            .and(StringUtils.hasText(queryDTO.getKeyword()), w -> w
                .like(Template::getName, queryDTO.getKeyword())
                .or()
                .like(Template::getContentJson, queryDTO.getKeyword()))
            .orderByDesc(Template::getUpdatedAt);
        return templateMapper.selectList(wrapper);
    }

    @Override
    public Template publishNewVersion(Long templateId) {
        Template existing = templateMapper.selectById(templateId);
        if (existing == null) {
            throw new IllegalArgumentException("模板不存在");
        }
        Template copy = new Template();
        copy.setName(existing.getName());
        copy.setProfession(existing.getProfession());
        copy.setSubProfession(existing.getSubProfession());
        copy.setNetworkType(existing.getNetworkType());
        copy.setScenarioCategory(existing.getScenarioCategory());
        copy.setRiskLevel(existing.getRiskLevel());
        copy.setOperationLevel(existing.getOperationLevel());
        copy.setTemplateType(existing.getTemplateType());
        copy.setVersionNo(existing.getVersionNo() + 1);
        copy.setContentJson(existing.getContentJson());
        copy.setOnlineFlag(true);
        copy.setAuthor(existing.getAuthor());
        copy.setCreatedAt(LocalDateTime.now());
        copy.setUpdatedAt(LocalDateTime.now());
        templateMapper.insert(copy);
        return copy;
    }

    @Override
    public Template buildTemplateFromPlan(Long planId, boolean shared, boolean depersonalize) {
        Plan plan = planMapper.selectById(planId);
        if (plan == null) {
            throw new IllegalArgumentException("方案不存在");
        }
        Template template = new Template();
        template.setName(plan.getName() + "-生成模板");
        template.setProfession(plan.getProfession());
        template.setSubProfession(plan.getSubProfession());
        template.setNetworkType(plan.getNetworkType());
        template.setScenarioCategory(plan.getScenarioCategory());
        template.setRiskLevel(plan.getRiskLevel());
        template.setOperationLevel(plan.getOperationLevel());
        template.setTemplateType(shared ? TemplateType.SHARED : TemplateType.PERSONAL);
        template.setVersionNo(1);
        template.setOnlineFlag(false);
        template.setAuthor("system");
        String personnel = depersonalize ? "[{\"role\":\"operator\",\"department\":\"TBD\"}]" : plan.getPersonnelJson();
        template.setContentJson("{\"operationContent\":\"" + plan.getOperationContent() + "\",\"personnel\":" + personnel + "}");
        template.setCreatedAt(LocalDateTime.now());
        template.setUpdatedAt(LocalDateTime.now());
        templateMapper.insert(template);
        return template;
    }
}
