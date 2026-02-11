package com.cb.change.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cb.change.enums.PlanStatus;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("plan")
public class Plan extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String profession;
    private String subProfession;
    private String networkType;
    private String scenarioCategory;
    private String operationContent;
    private String operationObject;
    private String operationScope;
    private Integer riskLevel;
    private Integer operationLevel;
    private Boolean businessInterrupted;
    private String businessImpact;
    private PlanStatus status;
    private Long templateVersionId;

    @TableField("cutover_start")
    private LocalDateTime cutoverStart;
    @TableField("cutover_end")
    private LocalDateTime cutoverEnd;
    @TableField("precheck_time")
    private LocalDateTime precheckTime;
    @TableField("test_time")
    private LocalDateTime testTime;
    @TableField("rollback_time")
    private LocalDateTime rollbackTime;

    private String personnelJson;
    private String deviceJson;
    private String aiSuggestion;
}
