package com.dc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("menu")
public class Menu {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String path;

    private String icon;

    private Long parentId;

    private Integer sortOrder;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // 非数据库字段，用于树形结构
    @TableField(exist = false)
    private List<Menu> children;
}
