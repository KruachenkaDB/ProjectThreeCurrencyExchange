package com.projectthreecurrencyexchange.service;

import com.projectthreecurrencyexchange.dao.ExchangeRateDao;
import com.projectthreecurrencyexchange.model.Exchange;
import com.projectthreecurrencyexchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeService {
    private ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

    //название подумать
    public Exchange mathExchange(String from, String to, BigDecimal amount) {
        //#1
        ExchangeRate exchangeRate = exchangeRateDao.findByCodes(from, to);
        if (exchangeRate != null) {
            BigDecimal convertedAmount = (exchangeRate.getRate().multiply(amount));
            return new Exchange(exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), exchangeRate.getRate(), amount, convertedAmount);
        }

        //#2
        exchangeRate = exchangeRateDao.findByCodes(to, from);
        if (exchangeRate != null) {
//            округлить до 2-х или 4-х знаков после запятой
            BigDecimal rate = BigDecimal.ONE.divide(exchangeRate.getRate(), 8, RoundingMode.HALF_UP);
            BigDecimal convertedAmount = rate.multiply(amount);
            return new Exchange(exchangeRate.getTargetCurrency(), exchangeRate.getBaseCurrency(), rate, amount, convertedAmount);
        }

        //#3
        ExchangeRate exchangeRateTo = exchangeRateDao.findByCodes("USD", to);
        ExchangeRate exchangeRateFrom = exchangeRateDao.findByCodes("USD", from);
        if (exchangeRateTo != null && exchangeRateFrom != null) {
//            округлить до 2-х или 4-х знаков после запятой
            BigDecimal rate =exchangeRateTo.getRate().divide(exchangeRateFrom.getRate(), 8, RoundingMode.HALF_UP);
//разобраться с точностью
            BigDecimal convertedAmount = rate.multiply(amount);
            return new Exchange(exchangeRateFrom.getTargetCurrency(), exchangeRateTo.getTargetCurrency(), rate, amount, convertedAmount);
        }
        return null;
    }
}
