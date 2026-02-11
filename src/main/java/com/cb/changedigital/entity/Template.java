package com.cb.changedigital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("template")
public class Template {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String major;
    private String subMajor;
    private String networkElementType;
    private Integer riskLevel;
    private Integer operationLevel;
    private String templateType;
    private String structureType;
    private String contentJson;
    private Long ownerId;
    private Integer latestVersion;
    private LocalDateTime updatedAt;
}
