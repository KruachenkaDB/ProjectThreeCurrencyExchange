package com.projectthreecurrencyexchange.service;
import com.projectthreecurrencyexchange.dao.CurrencyDao;
import com.projectthreecurrencyexchange.dao.ExchangeRateDao;
import com.projectthreecurrencyexchange.model.Currency;
import com.projectthreecurrencyexchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeRateService {
    private ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    private CurrencyDao currencyDao = new CurrencyDao();

    public List<ExchangeRate> getAllRate() {
        return exchangeRateDao.findAllRate();
    }

    //подумать над названием метода
    public ExchangeRate getCurrencyByCodes(String baseCode, String targetCode) {
        return exchangeRateDao.findByCodes(baseCode, targetCode);
    }

    public ExchangeRate addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        Currency baseCurrency = currencyDao.findByCode(baseCurrencyCode);
        Currency targetCurrency = currencyDao.findByCode(targetCurrencyCode);

        if ((baseCurrency != null) && (targetCurrency != null)) {
            return exchangeRateDao.add(new ExchangeRate(baseCurrency, targetCurrency, rate));
        }

        return null;
    }
}
