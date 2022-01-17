package com.elliot.demo.model.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author shi-wei
 * @date 2022/1/16
 */
@Data
public class SearchBankCurrencyResponseDto {
    String id;
    String updatedIso;
    String disclaimer;
    String chartName;
    BpiDto bpi;

    @Data
    public static class BpiDto {
        @JsonProperty("USD")
        NationalCurrencyDto usd;
        @JsonProperty("GBP")
        NationalCurrencyDto gbp;
        @JsonProperty("EUR")
        NationalCurrencyDto eur;
    }

    @Data
    public static class NationalCurrencyDto {
        String id;
        String code;
        String symbol;
        String rate;
        String description;
        Double rateFloat;
    }
}
