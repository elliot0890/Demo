package com.elliot.demo.model.dto;

import lombok.Data;

/**
 * @author shi-wei
 * @date 2022/1/15
 */
@Data
public class BankCurrencyDetailDto {
    String code;
    String symbol ;
    String rate ;
    String description;
    Double rateFloat;
}