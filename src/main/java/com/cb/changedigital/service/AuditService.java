package com.cb.changedigital.service;

import com.cb.changedigital.dto.AuditResultDTO;

public interface AuditService {
    AuditResultDTO audit(Long planId);
}
