package com.github.militch.tradingbot.restapi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.militch.tradingbot.restapi.entity.MarketKLineVO;
import com.github.militch.tradingbot.restapi.model.MarketKLine;
import com.github.militch.tradingbot.restapi.service.MarketKLineService;
import com.github.militch.tradingbot.restapi.utils.NumberUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ApplicationController {
    private final MarketKLineService marketKLineService;

    public ApplicationController(MarketKLineService marketKLineService) {
        this.marketKLineService = marketKLineService;
    }

    @GetMapping("/")
    public String index(){
        return "hello";
    }

    private static MarketKLineVO cover2vo(MarketKLine data) {
        MarketKLineVO vo = new MarketKLineVO();
        vo.setId(data.getId());
        vo.setExchange(data.getExchange());
        vo.setSymbol(data.getSymbol());
        vo.setOpenTime(data.getOpenTime());
        vo.setCloseTime(data.getCloseTime());
        int decimals = data.getDecimals();
        vo.setDecimals(decimals);
        BigDecimal openPrice = NumberUtil.formatPrice(data.getOpenPrice(), decimals);
        BigDecimal closePrice = NumberUtil.formatPrice(data.getClosePrice(), decimals);
        vo.setOpenPrice(openPrice);
        vo.setClosePrice(closePrice);
        BigDecimal highPrice = NumberUtil.formatPrice(data.getHighPrice(), decimals);
        BigDecimal lowPrice = NumberUtil.formatPrice(data.getLowPrice(), decimals);
        vo.setHighPrice(highPrice);
        vo.setLowPrice(lowPrice);
        BigDecimal volume = NumberUtil.formatPrice(data.getVolume(), decimals);
        BigDecimal quoteVolume = NumberUtil.formatPrice(data.getQuoteVolume(), decimals);
        vo.setVolume(volume);
        vo.setQuoteVolume(quoteVolume);
        vo.setCounts(data.getCounts());
        vo.setCreateTime(data.getCreateTime());
        return vo;
    }
    @GetMapping("/klines")
    public List<MarketKLineVO> getKLines(
            @RequestParam(value = "start_time", defaultValue = "0") Long startTime,
            @RequestParam(value = "end_time",required = false) Long endTime,
            @RequestParam(value = "limit", defaultValue = "100") Long limit
    ) {
        LambdaQueryWrapper<MarketKLine> lineLambdaQueryWrapper = new LambdaQueryWrapper<>();
        lineLambdaQueryWrapper.ge(MarketKLine::getOpenTime, startTime);
        if (endTime != null) {
            lineLambdaQueryWrapper.lt(MarketKLine::getCloseTime, endTime);
        }
        lineLambdaQueryWrapper.last(String.format("LIMIT %d", limit));
        List<MarketKLine> marketKLines = marketKLineService.list(lineLambdaQueryWrapper);
        return marketKLines.stream().map(ApplicationController::cover2vo).collect(Collectors.toList());
    }
}
