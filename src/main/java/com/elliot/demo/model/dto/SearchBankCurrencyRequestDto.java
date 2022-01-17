package com.elliot.demo.model.dto;

import lombok.Data;

/**
 * @author shi-wei
 * @date 2022/1/16
 */
@Data
public class SearchBankCurrencyRequestDto {
    String updatedIso;
    String disclaimer;
    String chartName;
}