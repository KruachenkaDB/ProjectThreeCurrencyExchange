package com.projectthreecurrencyexchange.service;

import com.projectthreecurrencyexchange.dao.CurrencyDao;
import com.projectthreecurrencyexchange.dao.ExchangeRateDao;
import com.projectthreecurrencyexchange.exception.EntityExistsException;
import com.projectthreecurrencyexchange.exception.InvalidParameterException;
import com.projectthreecurrencyexchange.model.Currency;
import com.projectthreecurrencyexchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeRateService {
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    private final CurrencyDao currencyDao = new CurrencyDao();

    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateDao.findAll();
    }

    public ExchangeRate getExchangeRateByCurrencyCodes(String baseCode, String targetCode) {
        return exchangeRateDao.findByCodes(baseCode, targetCode);
    }

    public ExchangeRate addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        validateRate(rate);

        Currency baseCurrency = currencyDao.findByCode(baseCurrencyCode);
        Currency targetCurrency = currencyDao.findByCode(targetCurrencyCode);

        if (!exchangeRateDao.existsByCurrencyPair(baseCurrency.getId(), targetCurrency.getId())) {
            return exchangeRateDao.add(new ExchangeRate(baseCurrency, targetCurrency, rate));
        } else {
            throw new EntityExistsException("Exchange rate for this currency pair already exists");
        }
    }

    public ExchangeRate updateExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        validateRate(rate);

        ExchangeRate exchangeRate = exchangeRateDao.findByCodes(baseCurrencyCode, targetCurrencyCode);
        exchangeRate.setRate(rate);
        return exchangeRateDao.update(exchangeRate);
    }

    private void validateRate(BigDecimal rate) {
        if (rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidParameterException("The rate variable must be greater than zero");
        }
    }
}