package com.cb.changedigital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("white_list_item")
public class WhiteListItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String scenario;
    private String operationTarget;
    private String operationContent;
    private String note;
}
