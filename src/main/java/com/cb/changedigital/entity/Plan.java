package com.cb.changedigital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("plan")
public class Plan {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String major;
    private String subMajor;
    private String operationContent;
    private String operationTarget;
    private String networkScope;
    private Integer riskLevel;
    private Integer operationLevel;
    private Boolean businessInterrupted;
    private String businessImpact;
    private String status;
    private Long templateVersionId;
    private Long ownerId;
    private LocalDateTime cutoverStart;
    private LocalDateTime cutoverEnd;
    private LocalDateTime precheckTime;
    private LocalDateTime verifyTime;
    private LocalDateTime rollbackTime;
    private LocalDateTime updatedAt;
}
