package com.projectthreecurrencyexchange.service;
import com.projectthreecurrencyexchange.dao.ExchangeRateDao;
import com.projectthreecurrencyexchange.model.Currency;
import com.projectthreecurrencyexchange.model.ExchangeRate;

import java.util.List;

public class ExchangeRateService {
    private ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

    public List<ExchangeRate> getAllRate() {
        return exchangeRateDao.findAllRate();
    }

    //подумать над названием метода
    public ExchangeRate getCurrencyByCodes(String baseCode, String targetCode) {
        return exchangeRateDao.findByCodes(baseCode, targetCode);
    }
}
