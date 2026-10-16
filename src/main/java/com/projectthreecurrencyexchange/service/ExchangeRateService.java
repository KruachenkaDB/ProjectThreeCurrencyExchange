package com.projectthreecurrencyexchange.service;
import com.projectthreecurrencyexchange.dao.CurrencyDao;
import com.projectthreecurrencyexchange.dao.ExchangeRateDao;
import com.projectthreecurrencyexchange.exception.InvalidParameterException;
import com.projectthreecurrencyexchange.model.Currency;
import com.projectthreecurrencyexchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeRateService {
    private ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    private CurrencyDao currencyDao = new CurrencyDao();

    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateDao.findAllRate();
    }

    public ExchangeRate getExchangeRateByCodes(String baseCode, String targetCode) {
        return exchangeRateDao.findByCodes(baseCode, targetCode);
    }

    public ExchangeRate addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        if ((rate.compareTo(BigDecimal.ZERO) <= 0)) {
            throw new InvalidParameterException("The rate variable must be greater than zero");
        }

        Currency baseCurrency = currencyDao.findByCode(baseCurrencyCode);
        Currency targetCurrency = currencyDao.findByCode(targetCurrencyCode);

        return exchangeRateDao.add(new ExchangeRate(baseCurrency, targetCurrency, rate));
    }

    public ExchangeRate updateExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        if ((rate.compareTo(BigDecimal.ZERO) <= 0)) {
            throw new InvalidParameterException("The rate variable must be greater than zero");
        }

        ExchangeRate exchangeRate = exchangeRateDao.findByCodes(baseCurrencyCode, targetCurrencyCode);
        exchangeRate.setRate(rate);
        return exchangeRateDao.update(exchangeRate);
    }
}