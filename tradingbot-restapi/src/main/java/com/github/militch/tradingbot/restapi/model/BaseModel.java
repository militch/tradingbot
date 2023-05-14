package com.github.militch.tradingbot.restapi.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseModel {
    @TableId(type=IdType.AUTO)
    private Integer id;
    @TableField("create_time")
    private LocalDateTime createTime;
}
