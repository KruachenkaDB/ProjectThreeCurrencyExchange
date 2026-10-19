package com.projectthreecurrencyexchange.service;

import com.projectthreecurrencyexchange.dao.ExchangeRateDao;
import com.projectthreecurrencyexchange.exception.InvalidParameterException;
import com.projectthreecurrencyexchange.exception.NotFoundException;
import com.projectthreecurrencyexchange.model.Exchange;
import com.projectthreecurrencyexchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeService {
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

    public Exchange calculateExchangeAmount(String from, String to, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidParameterException("The amount variable must be greater than zero");
        }

        ExchangeRate exchangeRate = null;
        try {
            exchangeRate = exchangeRateDao.findByCodes(from, to);
            BigDecimal convertedAmount = (exchangeRate.getRate().multiply(amount));
            convertedAmount = convertedAmount.setScale(2, RoundingMode.HALF_UP);

            return new Exchange(exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), exchangeRate.getRate(), amount, convertedAmount);

        } catch (NotFoundException ignored) {
//            #1 если прямого курса нет, переходим к поиску обратного
        }

        try {
            exchangeRate = exchangeRateDao.findByCodes(to, from);
            BigDecimal rate = BigDecimal.ONE.divide(exchangeRate.getRate(), 4, RoundingMode.HALF_UP);
            BigDecimal convertedAmount = rate.multiply(amount);
            convertedAmount = convertedAmount.setScale(2, RoundingMode.HALF_UP);

            return new Exchange(exchangeRate.getTargetCurrency(), exchangeRate.getBaseCurrency(), rate, amount, convertedAmount);
        } catch (NotFoundException ignored) {
//            #2 если обратного курса нет, переходим к расчёту через USD
        }

        try {
            ExchangeRate exchangeRateTo = exchangeRateDao.findByCodes("USD", to);
            ExchangeRate exchangeRateFrom = exchangeRateDao.findByCodes("USD", from);
            if (exchangeRateTo != null && exchangeRateFrom != null) {
                BigDecimal rate = exchangeRateTo.getRate().divide(exchangeRateFrom.getRate(), 4, RoundingMode.HALF_UP);
                BigDecimal convertedAmount = rate.multiply(amount);
                convertedAmount = convertedAmount.setScale(2, RoundingMode.HALF_UP);
                amount = amount.setScale(2, RoundingMode.HALF_UP);

                return new Exchange(exchangeRateFrom.getTargetCurrency(), exchangeRateTo.getTargetCurrency(), rate, amount, convertedAmount);
            }
        } catch (NotFoundException ignored) {
//            #3 если нужных курсов через USD нет, в конце выбрасываем общее исключение NotFoundException
        }

        throw new NotFoundException("Exchange rate not found");
    }
}
