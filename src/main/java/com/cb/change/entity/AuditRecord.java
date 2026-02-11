package com.cb.change.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("audit_record")
public class AuditRecord extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long planId;
    private String ruleIssuesJson;
    private String aiIssuesJson;
    private Boolean passed;
    private String operator;
}
