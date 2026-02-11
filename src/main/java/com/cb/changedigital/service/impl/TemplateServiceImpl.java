package com.cb.changedigital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cb.changedigital.dto.TemplateCreateRequest;
import com.cb.changedigital.dto.TemplateQueryRequest;
import com.cb.changedigital.entity.Template;
import com.cb.changedigital.entity.TemplateVersion;
import com.cb.changedigital.mapper.TemplateMapper;
import com.cb.changedigital.mapper.TemplateVersionMapper;
import com.cb.changedigital.service.TemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TemplateServiceImpl implements TemplateService {

    private final TemplateMapper templateMapper;
    private final TemplateVersionMapper templateVersionMapper;

    public TemplateServiceImpl(TemplateMapper templateMapper, TemplateVersionMapper templateVersionMapper) {
        this.templateMapper = templateMapper;
        this.templateVersionMapper = templateVersionMapper;
    }

    @Override
    public List<Template> query(TemplateQueryRequest request) {
        LambdaQueryWrapper<Template> qw = new LambdaQueryWrapper<Template>()
                .eq(request.getMajor() != null, Template::getMajor, request.getMajor())
                .eq(request.getSubMajor() != null, Template::getSubMajor, request.getSubMajor())
                .eq(request.getRiskLevel() != null, Template::getRiskLevel, request.getRiskLevel())
                .eq(request.getOperationLevel() != null, Template::getOperationLevel, request.getOperationLevel())
                .eq(request.getTemplateType() != null, Template::getTemplateType, request.getTemplateType())
                .and(request.getKeyword() != null,
                        q -> q.like(Template::getName, request.getKeyword())
                                .or().like(Template::getContentJson, request.getKeyword()))
                .orderByDesc(Template::getUpdatedAt);
        return templateMapper.selectList(qw);
    }

    @Override
    @Transactional
    public Template create(TemplateCreateRequest request, Long ownerId) {
        Template template = new Template();
        template.setName(request.getName());
        template.setMajor(request.getMajor());
        template.setSubMajor(request.getSubMajor());
        template.setRiskLevel(request.getRiskLevel());
        template.setOperationLevel(request.getOperationLevel());
        template.setTemplateType(request.getTemplateType());
        template.setStructureType(request.getStructureType());
        template.setContentJson(request.getContentJson());
        template.setOwnerId(ownerId);
        template.setLatestVersion(1);
        template.setUpdatedAt(LocalDateTime.now());
        templateMapper.insert(template);

        TemplateVersion version = new TemplateVersion();
        version.setTemplateId(template.getId());
        version.setVersionNo(1);
        version.setContentJson(request.getContentJson());
        version.setReleaseNote("init");
        version.setCreatedAt(LocalDateTime.now());
        templateVersionMapper.insert(version);
        return template;
    }

    @Override
    public Template update(Long id, TemplateCreateRequest request) {
        Template template = templateMapper.selectById(id);
        template.setName(request.getName());
        template.setMajor(request.getMajor());
        template.setSubMajor(request.getSubMajor());
        template.setRiskLevel(request.getRiskLevel());
        template.setOperationLevel(request.getOperationLevel());
        template.setTemplateType(request.getTemplateType());
        template.setStructureType(request.getStructureType());
        template.setContentJson(request.getContentJson());
        template.setUpdatedAt(LocalDateTime.now());
        templateMapper.updateById(template);
        return template;
    }

    @Override
    public void delete(Long id) {
        templateMapper.deleteById(id);
    }

    @Override
    @Transactional
    public TemplateVersion publishNewVersion(Long templateId, String contentJson, String releaseNote) {
        Template template = templateMapper.selectById(templateId);
        int newVersion = template.getLatestVersion() + 1;

        TemplateVersion version = new TemplateVersion();
        version.setTemplateId(templateId);
        version.setVersionNo(newVersion);
        version.setContentJson(contentJson);
        version.setReleaseNote(releaseNote);
        version.setCreatedAt(LocalDateTime.now());
        templateVersionMapper.insert(version);

        template.setLatestVersion(newVersion);
        template.setContentJson(contentJson);
        template.setUpdatedAt(LocalDateTime.now());
        templateMapper.updateById(template);
        return version;
    }
}
