package com.cb.change.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cb.change.enums.TemplateType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("template")
public class Template extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String profession;
    private String subProfession;
    private String networkType;
    private String scenarioCategory;
    private Integer riskLevel;
    private Integer operationLevel;
    private TemplateType templateType;
    private Integer versionNo;
    private String contentJson;
    private Boolean onlineFlag;
    private String author;
}
