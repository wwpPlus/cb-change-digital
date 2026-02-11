package com.cb.change.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cb.change.dto.TemplateQueryDTO;
import com.cb.change.entity.Template;
import java.util.List;

public interface TemplateService extends IService<Template> {

    List<Template> queryTemplates(TemplateQueryDTO queryDTO);

    Template publishNewVersion(Long templateId);

    Template buildTemplateFromPlan(Long planId, boolean shared, boolean depersonalize);
}
