package com.projectthreecurrencyexchange.service;

import com.projectthreecurrencyexchange.dao.ExchangeRateDao;
import com.projectthreecurrencyexchange.exception.InvalidParameterException;
import com.projectthreecurrencyexchange.exception.NotFoundException;
import com.projectthreecurrencyexchange.model.Exchange;
import com.projectthreecurrencyexchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeService {
    private ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

    public Exchange calculateExchangeAmount(String from, String to, BigDecimal amount) {
        if ((amount.compareTo(BigDecimal.ZERO) <= 0)) {
            throw new InvalidParameterException("Переменная amount должна быть больше нуля");
        }

        ExchangeRate exchangeRate = null;
        //#1
        try {
            exchangeRate = exchangeRateDao.findByCodes(from, to);
            BigDecimal convertedAmount = (exchangeRate.getRate().multiply(amount));
            convertedAmount = convertedAmount.setScale(2, RoundingMode.HALF_UP);

            return new Exchange(exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), exchangeRate.getRate(), amount, convertedAmount);

        } catch (NotFoundException ignored) {
        }

        //#2
        try {
            exchangeRate = exchangeRateDao.findByCodes(to, from);
            BigDecimal rate = BigDecimal.ONE.divide(exchangeRate.getRate(), 4, RoundingMode.HALF_UP);
            BigDecimal convertedAmount = rate.multiply(amount);
            convertedAmount = convertedAmount.setScale(2, RoundingMode.HALF_UP);

            return new Exchange(exchangeRate.getTargetCurrency(), exchangeRate.getBaseCurrency(), rate, amount, convertedAmount);
        } catch (NotFoundException ignored) {
        }

        //#3
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
        } catch (NotFoundException ignored) {}

        throw new NotFoundException("Курс обмена не найден");
    }
//    При последующем тестировании проверить, что JSON содержит два знака после запятой, например 125.60, а не 125.6
}
