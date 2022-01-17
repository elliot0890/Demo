package com.elliot.demo.model.dao;

import com.elliot.demo.model.entity.Currency;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author shi-wei
 * @date 2022/1/15
 */
public interface CurrencyDao extends CrudRepository<Currency, Integer> {
    Currency findById(String id);

    List<Currency> findCurrenciesByChartName(String chartName);
}
