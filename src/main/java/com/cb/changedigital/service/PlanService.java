package com.cb.changedigital.service;

import com.cb.changedigital.dto.QuickEditRequest;
import com.cb.changedigital.entity.Plan;

public interface PlanService {
    Plan saveDraft(QuickEditRequest request, Long ownerId);

    Plan submit(Long planId);

    Plan generateTemplate(Long planId, String templateType, Long ownerId);
}
