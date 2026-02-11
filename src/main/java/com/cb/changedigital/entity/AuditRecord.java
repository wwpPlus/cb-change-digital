package com.cb.changedigital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("audit_record")
public class AuditRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long planId;
    private Integer versionNo;
    private String ruleResultJson;
    private String aiSuggestionJson;
    private String conclusion;
    private LocalDateTime createdAt;
}
