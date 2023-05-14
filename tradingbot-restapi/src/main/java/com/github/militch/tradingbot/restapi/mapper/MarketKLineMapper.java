package com.github.militch.tradingbot.restapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.militch.tradingbot.restapi.model.MarketKLine;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MarketKLineMapper extends BaseMapper<MarketKLine> {
}
