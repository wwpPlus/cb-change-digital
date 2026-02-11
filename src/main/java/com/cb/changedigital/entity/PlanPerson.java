package com.cb.changedigital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("plan_person")
public class PlanPerson {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long planId;
    private String role;
    private String department;
    private String name;
    private String phone;
}
