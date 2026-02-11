package com.cb.changedigital.controller;

import com.cb.changedigital.common.ApiResponse;
import com.cb.changedigital.dto.TemplateCreateRequest;
import com.cb.changedigital.dto.TemplateQueryRequest;
import com.cb.changedigital.entity.Template;
import com.cb.changedigital.entity.TemplateVersion;
import com.cb.changedigital.service.TemplateService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public ApiResponse<List<Template>> query(TemplateQueryRequest request) {
        return ApiResponse.ok(templateService.query(request));
    }

    @PostMapping
    public ApiResponse<Template> create(@RequestBody @Validated TemplateCreateRequest request) {
        return ApiResponse.ok(templateService.create(request, 10001L));
    }

    @PutMapping("/{id}")
    public ApiResponse<Template> update(@PathVariable Long id, @RequestBody @Validated TemplateCreateRequest request) {
        return ApiResponse.ok(templateService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        templateService.delete(id);
        return ApiResponse.ok(null);
    }

    @PostMapping("/{id}/versions")
    public ApiResponse<TemplateVersion> publish(@PathVariable Long id,
                                                @RequestParam String contentJson,
                                                @RequestParam(defaultValue = "publish") String releaseNote) {
        return ApiResponse.ok(templateService.publishNewVersion(id, contentJson, releaseNote));
    }
}
