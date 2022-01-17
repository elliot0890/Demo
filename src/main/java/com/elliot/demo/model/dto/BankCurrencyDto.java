package com.elliot.demo.model.dto;

import com.elliot.demo.model.entity.Currency;
import com.elliot.demo.model.entity.ExchangeRate;
import lombok.Data;

/**
 * @author shi-wei
 * @date 2022/1/15
 */
@Data
public class BankCurrencyDto {
    String id;
    String updatedIso;
    String disclaimer;
    String chartName;
    BpiDto bpi;

    public static BankCurrencyDto convertByDto(Currency currency, ExchangeRate exchangeRate) {
        BankCurrencyDto bankCurrencyDto = new BankCurrencyDto();
        bankCurrencyDto.setId(currency.getId());
        bankCurrencyDto.setChartName(currency.getChartName());
        bankCurrencyDto.setDisclaimer("This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org");
        bankCurrencyDto.setUpdatedIso(exchangeRate.getUpdated().toString());
        BpiDto bpiDto = new BpiDto();
        bpiDto.setId(exchangeRate.getId());
        bpiDto.setCode(currency.getCode());
        bpiDto.setSymbol(currency.getSymbol());
        bpiDto.setRate(exchangeRate.getRate());
        bpiDto.setDescription(currency.getDescription());
        bpiDto.setRateFloat(exchangeRate.getReteFloat());
        bankCurrencyDto.setBpi(bpiDto);

        return bankCurrencyDto;
    }

    @Data
    public static class BpiDto {
        String id;
        String code;
        String symbol;
        String rate;
        String description;
        Double rateFloat;
    }
}