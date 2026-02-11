package com.cb.change.controller;

import com.cb.change.common.ApiResponse;
import com.cb.change.dto.TemplateQueryDTO;
import com.cb.change.entity.Template;
import com.cb.change.service.TemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "模板库")
@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @ApiOperation("筛选模板")
    @PostMapping("/query")
    public ApiResponse<List<Template>> query(@RequestBody TemplateQueryDTO queryDTO) {
        return ApiResponse.ok(templateService.queryTemplates(queryDTO));
    }

    @ApiOperation("创建模板")
    @PostMapping
    public ApiResponse<Template> create(@Valid @RequestBody Template template) {
        template.setCreatedAt(LocalDateTime.now());
        template.setUpdatedAt(LocalDateTime.now());
        templateService.save(template);
        return ApiResponse.ok(template);
    }

    @ApiOperation("更新模板")
    @PutMapping("/{id}")
    public ApiResponse<Template> update(@PathVariable Long id, @RequestBody Template template) {
        template.setId(id);
        template.setUpdatedAt(LocalDateTime.now());
        templateService.updateById(template);
        return ApiResponse.ok(templateService.getById(id));
    }

    @ApiOperation("删除模板")
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        return ApiResponse.ok(templateService.removeById(id));
    }

    @ApiOperation("发布模板新版本")
    @PostMapping("/{id}/publish")
    public ApiResponse<Template> publish(@PathVariable Long id) {
        return ApiResponse.ok(templateService.publishNewVersion(id));
    }

    @ApiOperation("由方案生成模板")
    @PostMapping("/from-plan/{planId}")
    public ApiResponse<Template> fromPlan(@PathVariable Long planId,
                                          @RequestParam(defaultValue = "false") boolean shared,
                                          @RequestParam(defaultValue = "true") boolean depersonalize) {
        return ApiResponse.ok(templateService.buildTemplateFromPlan(planId, shared, depersonalize));
    }
}
