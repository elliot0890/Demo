package com.elliot.demo.service;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.elliot.demo.model.dao.CurrencyDao;
import com.elliot.demo.model.dao.ExchangeRateDao;
import com.elliot.demo.model.dto.BankCurrencyDetailDto;
import com.elliot.demo.model.dto.BankCurrencyDto;
import com.elliot.demo.model.dto.SearchBankCurrencyResponseDto;
import com.elliot.demo.model.entity.Currency;
import com.elliot.demo.model.entity.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author shi-wei
 * @date 2022/1/15
 */
@Service
public class BankCurrencyService {

    @Autowired
    CurrencyDao currencyDao;
    @Autowired
    ExchangeRateDao exchangeRateDao;

    public void saveJsonObject(String data) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(data);
            JSONObject timeObject = jsonObject.getJSONObject("time");
            String updatedIso = timeObject.getString("updatedISO");
            System.out.println(updatedIso);
            String chartName = jsonObject.getString("chartName");
            JSONObject bpiObject = jsonObject.getJSONObject("bpi");

            List<BankCurrencyDetailDto> bankCurrencyDetailDtoList = new ArrayList<>();
            BankCurrencyDetailDto usdDto = jsonToDetailDto(bpiObject.getJSONObject("USD"));
            bankCurrencyDetailDtoList.add(usdDto);
            BankCurrencyDetailDto gbpDto = jsonToDetailDto(bpiObject.getJSONObject("GBP"));
            bankCurrencyDetailDtoList.add(gbpDto);
            BankCurrencyDetailDto eurDto = jsonToDetailDto(bpiObject.getJSONObject("EUR"));
            bankCurrencyDetailDtoList.add(eurDto);

            bankCurrencyDetailDtoList.forEach(dto -> {
                Currency currency = new Currency();
                currency.setChartName(chartName);
                currency.setDescription(dto.getDescription());
                currency.setCode(dto.getCode());
                currency.setSymbol(dto.getSymbol());
                Currency newCurrency = currencyDao.save(currency);

                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setCurrencyId(newCurrency.getId());
                exchangeRate.setRate(dto.getRate());
                exchangeRate.setReteFloat(dto.getRateFloat());
                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
                sdFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = null;
                try {
                    date = sdFormat.parse(updatedIso);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                exchangeRate.setUpdated(date);
                exchangeRateDao.save(exchangeRate);
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public BankCurrencyDetailDto jsonToDetailDto(JSONObject jsonObject) {
        BankCurrencyDetailDto bankCurrencyDetailDto = new BankCurrencyDetailDto();
        bankCurrencyDetailDto.setCode(jsonObject.getString("code"));
        bankCurrencyDetailDto.setSymbol(jsonObject.getString("symbol"));
        bankCurrencyDetailDto.setRate(jsonObject.getString("rate"));
        bankCurrencyDetailDto.setDescription(jsonObject.getString("description"));
        bankCurrencyDetailDto.setRateFloat(jsonObject.getDouble("rate_float"));

        return bankCurrencyDetailDto;
    }

    public BankCurrencyDto saveOrUpdate(BankCurrencyDto bankCurrencyDto) {
        Currency currency = new Currency();
        currency.setId(bankCurrencyDto.getId());
        if (ObjectUtils.isEmpty(bankCurrencyDto.getId())) {
            currency.setId(UUID.randomUUID().toString());
        }
        currency.setChartName(bankCurrencyDto.getChartName());
        currency.setDescription(bankCurrencyDto.getBpi().getDescription());
        currency.setCode(bankCurrencyDto.getBpi().getCode());
        currency.setSymbol(bankCurrencyDto.getBpi().getSymbol());
        Currency newCurrency = currencyDao.save(currency);

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setId(bankCurrencyDto.getBpi().getId());
        if (ObjectUtils.isEmpty(bankCurrencyDto.getBpi().getId())) {
            exchangeRate.setId(UUID.randomUUID().toString());
        }
        exchangeRate.setCurrencyId(newCurrency.getId());
        exchangeRate.setRate(bankCurrencyDto.getBpi().getRate());
        exchangeRate.setReteFloat(bankCurrencyDto.getBpi().getRateFloat());
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        sdFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = sdFormat.parse(bankCurrencyDto.getUpdatedIso());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        exchangeRate.setUpdated(date);
        exchangeRateDao.save(exchangeRate);

        return BankCurrencyDto.convertByDto(currency, exchangeRate);
    }

    public boolean delete(String id) {
        Currency currency = currencyDao.findById(id);
        ExchangeRate exchangeRate = exchangeRateDao.findByCurrencyId(currency.getId());
        if (!ObjectUtils.isEmpty(currency)) {
            currencyDao.delete(currency);
            if (!ObjectUtils.isEmpty(exchangeRate)) {
                exchangeRateDao.delete(exchangeRate);
            }
            return true;
        }
        return false;
    }

    public SearchBankCurrencyResponseDto findCurrencyByChartName(String chartName) {
        List<Currency> currencies = currencyDao.findCurrenciesByChartName(chartName);
        SearchBankCurrencyResponseDto responseDto = new SearchBankCurrencyResponseDto();
        SearchBankCurrencyResponseDto.BpiDto bpiDto = new SearchBankCurrencyResponseDto.BpiDto();
        currencies.forEach(currency -> {
            ExchangeRate exchangeRate = exchangeRateDao.findExchangeRateByCurrencyIdOrderByUpdatedDesc(currency.getId());
            responseDto.setId(currency.getId());
            responseDto.setChartName(currency.getChartName());
            responseDto.setDisclaimer("This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org");
            responseDto.setUpdatedIso(exchangeRate.getUpdated().toString());
            SearchBankCurrencyResponseDto.NationalCurrencyDto nationalCurrencyDto = new SearchBankCurrencyResponseDto.NationalCurrencyDto();
            nationalCurrencyDto.setId(exchangeRate.getId());
            nationalCurrencyDto.setCode(currency.getCode());
            nationalCurrencyDto.setSymbol(currency.getSymbol());
            nationalCurrencyDto.setRate(exchangeRate.getRate());
            nationalCurrencyDto.setDescription(currency.getDescription());
            nationalCurrencyDto.setRateFloat(exchangeRate.getReteFloat());
            if ("USD".equals(currency.getCode())) {
                bpiDto.setUsd(nationalCurrencyDto);
            }
            if ("GBP".equals(currency.getCode())) {
                bpiDto.setGbp(nationalCurrencyDto);
            }
            if ("EUR".equals(currency.getCode())) {
                bpiDto.setEur(nationalCurrencyDto);
            }
        });
        responseDto.setBpi(bpiDto);

        return responseDto;
    }
}