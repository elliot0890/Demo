package com.elliot.demo.model.dao;

import com.elliot.demo.model.entity.ExchangeRate;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author shi-wei
 * @date 2022/1/15
 */
public interface ExchangeRateDao extends CrudRepository<ExchangeRate, Integer> {

    ExchangeRate findByCurrencyId(String id);

    ExchangeRate findExchangeRateByCurrencyIdOrderByUpdatedDesc(String currencyId);
}
