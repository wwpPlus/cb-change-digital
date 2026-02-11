package com.cb.changedigital.service;

import com.cb.changedigital.dto.TemplateCreateRequest;
import com.cb.changedigital.dto.TemplateQueryRequest;
import com.cb.changedigital.entity.Template;
import com.cb.changedigital.entity.TemplateVersion;

import java.util.List;

public interface TemplateService {
    List<Template> query(TemplateQueryRequest request);

    Template create(TemplateCreateRequest request, Long ownerId);

    Template update(Long id, TemplateCreateRequest request);

    void delete(Long id);

    TemplateVersion publishNewVersion(Long templateId, String contentJson, String releaseNote);
}
